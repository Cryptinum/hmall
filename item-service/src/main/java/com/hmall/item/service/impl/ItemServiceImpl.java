package com.hmall.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.common.exception.BizIllegalException;
import com.hmall.common.utils.BeanUtils;
import com.hmall.item.domain.dto.ItemDTO;
import com.hmall.item.domain.dto.OrderDetailDTO;
import com.hmall.item.domain.po.Item;
import com.hmall.item.mapper.ItemMapper;
import com.hmall.item.service.IItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author 虎哥
 */
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements IItemService {

    @Override
    @Transactional
    public void deductStock(List<OrderDetailDTO> items) {
        // String sqlStatement = "com.hmall.mapper.item.ItemMapper.updateStock";
        // boolean r = false;
        // try {
        //     r = executeBatch(items, (sqlSession, entity) -> sqlSession.update(sqlStatement, entity));
        // } catch (Exception e) {
        //     throw new BizIllegalException("更新库存异常，可能是库存不足!", e);
        // }
        // if (!r) {
        //     throw new BizIllegalException("库存不足！");
        // }
        // 告别硬编码的魔术字符串
        // String sqlStatement = "com.hmall.mapper.item.ItemMapper.updateStock";

        boolean r = false;
        try {
            // 使用类型安全的方式执行批量更新
            r = executeBatch(items, (sqlSession, entity) -> {
                // 通过 sqlSession 获取 Mapper 代理对象，然后直接调用方法
                // 这样写有编译时检查，并且支持IDE导航和重构
                ItemMapper mapper = sqlSession.getMapper(ItemMapper.class);
                mapper.updateStock(entity);
            });
        } catch (Exception e) {
            // 注意：MyBatisPlus的批量更新在失败时会直接抛出异常，
            // 而不是返回false，所以这里的异常捕获是关键。
            throw new BizIllegalException("更新库存异常，可能是库存不足!", e);
        }
        // 异常不抛出，r会是true。如果抛出了异常，这里的代码不会执行。
        // 所以!r的判断可能需要根据业务逻辑调整。
        // 但通常MyBatisPlus批量操作失败会直接抛异常。
        if (!r) {
            // 这一行可能永远不会被执行
            throw new BizIllegalException("库存不足！");
        }
    }

    @Override
    public void restoreStock(List<OrderDetailDTO> items) {
        // 这里的逻辑实际上和 deductStock 一致，只是语义不同
        boolean r;
        try {
            r = executeBatch(items, (sqlSession, entity) -> {
                ItemMapper mapper = sqlSession.getMapper(ItemMapper.class);
                mapper.restoreStock(entity);
            });
        } catch (Exception e) {
            throw new BizIllegalException("恢复库存异常，可能出现业务一场!", e);
        }
        if (!r) {
            throw new BizIllegalException("恢复库存失败！");
        }

    }

    @Override
    public List<ItemDTO> queryItemByIds(Collection<Long> ids) {
        return BeanUtils.copyList(listByIds(ids), ItemDTO.class);
    }
}
