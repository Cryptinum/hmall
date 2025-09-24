package com.hmall.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.api.client.ItemClient;
import com.hmall.api.client.PayClient;
import com.hmall.api.dto.ItemDTO;
import com.hmall.api.dto.OrderDetailDTO;
import com.hmall.api.dto.PayOrderDTO;
import com.hmall.common.constants.MQConstants;
import com.hmall.common.domain.CartCleanMessage;
import com.hmall.common.exception.BadRequestException;
import com.hmall.common.utils.BeanUtils;
import com.hmall.common.utils.MQUtils;
import com.hmall.common.utils.UserContext;
import com.hmall.trade.domain.dto.OrderFormDTO;
import com.hmall.trade.domain.po.Order;
import com.hmall.trade.domain.po.OrderDetail;
import com.hmall.trade.mapper.OrderMapper;
import com.hmall.trade.service.IOrderDetailService;
import com.hmall.trade.service.IOrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private final IOrderDetailService detailService;
    // private final CartClient cartClient;
    private final ItemClient itemClient;

    private final PayClient payClient;

    private final RabbitTemplate rabbitTemplate;

    @Override
    // @Transactional
    @GlobalTransactional  // seata分布式事务
    public Long createOrder(OrderFormDTO orderFormDTO) {
        // 1.订单数据
        Order order = new Order();
        // 1.1.查询商品
        List<OrderDetailDTO> detailDTOS = orderFormDTO.getDetails();
        // 1.2.获取商品id和数量的Map
        Map<Long, Integer> itemNumMap = detailDTOS.stream()
                .collect(Collectors.toMap(OrderDetailDTO::getItemId, OrderDetailDTO::getNum));
        Set<Long> itemIds = itemNumMap.keySet();
        // 1.3.查询商品
        List<ItemDTO> items = itemClient.queryItemsByIds(itemIds);
        if (items == null || items.size() < itemIds.size()) {
            throw new BadRequestException("商品不存在");
        }
        // 1.4.基于商品价格、购买数量计算商品总价：totalFee
        int total = 0;
        for (ItemDTO item : items) {
            total += item.getPrice() * itemNumMap.get(item.getId());
        }
        order.setTotalFee(total);
        // 1.5.其它属性
        order.setPaymentType(orderFormDTO.getPaymentType());
        order.setUserId(UserContext.getUser());
        order.setStatus(1);
        // 1.6.将Order写入数据库order表中
        save(order);

        // 2.保存订单详情
        List<OrderDetail> details = buildDetails(order.getId(), items, itemNumMap);
        detailService.saveBatch(details);

        // // 3.清理购物车商品
        // cartClient.deleteCartItemByIds(itemIds);

        // 4.扣减库存
        try {
            itemClient.deductStock(detailDTOS);
        } catch (Exception e) {
            throw new RuntimeException("库存不足！");
        }

        // 5.发送消息，清理购物车
        CartCleanMessage msg = CartCleanMessage.builder()
                .userId(UserContext.getUser())
                .itemIds(itemIds).build();
        try {
            rabbitTemplate.convertAndSend(
                    MQConstants.TRADE_EXCHANGE_NAME,
                    MQConstants.ORDER_CREATE_KEY,
                    msg);
        } catch (Exception e) {
            log.error("订单微服务-发送清理购物车消息失败，用户id：{}，商品id集合：{}", UserContext.getUser(), itemIds, e);
        }

        // 6. 发送延迟消息，查询订单支付状态
        rabbitTemplate.convertAndSend(
                MQConstants.DELAY_EXCHANGE_NAME,
                MQConstants.DELAY_ROUTING_KEY,
                order.getId(),
                MQUtils.setMessageDelay(15000));

        return order.getId();
    }

    @Override
    public void listenOrderDelayMessage(Long orderId) {
        // 1. 查询订单
        Order order = getById(orderId);

        // 2. 检测订单状态，如果已支付，那么直接返回
        if (order == null || order.getStatus() != 1) {
            return;
        }

        // 3. 如果未支付，那么需要查询支付流水
        PayOrderDTO payOrder = payClient.queryPayOrderByBizOrderNo(orderId);

        // 4. 判断支付流水是否已支付
        if (payOrder == null) {
            return;
        }
        if (payOrder.getStatus() == 3) {
            // 4.1 如果已支付，那么更新订单状态为已支付
            markOrderPaySuccess(orderId);
        } else {
            // 4.2 如果未支付，那么更新订单状态为已取消，同时恢复商品库存
            cancelOrder(orderId);
        }
    }

    @Override
    public void markOrderPaySuccess(Long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(2);
        order.setPayTime(LocalDateTime.now());
        updateById(order);
    }

    @GlobalTransactional
    public void cancelOrder(Long orderId) {
        // 1.更新订单状态为已取消
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(5);
        order.setUpdateTime(LocalDateTime.now());
        order.setCloseTime(LocalDateTime.now());
        updateById(order);

        // 2.恢复商品库存
        List<OrderDetail> details = detailService.lambdaQuery().eq(OrderDetail::getOrderId, orderId).list();
        List<OrderDetailDTO> orderDetailDTOS = BeanUtils.copyList(details, OrderDetailDTO.class);
        itemClient.restoreStock(orderDetailDTOS);
    }

    @Override
    public void listenPaySuccess(Long orderId) {
        // 1. 首先查询订单
        Order order = getById(orderId);

        // 2. 判断订单状态是否是未支付
        if (order == null || order.getStatus() != 1) {
            return;
        }

        // 3. 标记订单状态为已支付
        markOrderPaySuccess(orderId);
    }

    private List<OrderDetail> buildDetails(Long orderId, List<ItemDTO> items, Map<Long, Integer> numMap) {
        List<OrderDetail> details = new ArrayList<>(items.size());
        for (ItemDTO item : items) {
            OrderDetail detail = new OrderDetail();
            detail.setName(item.getName());
            detail.setSpec(item.getSpec());
            detail.setPrice(item.getPrice());
            detail.setNum(numMap.get(item.getId()));
            detail.setItemId(item.getId());
            detail.setImage(item.getImage());
            detail.setOrderId(orderId);
            details.add(detail);
        }
        return details;
    }
}
