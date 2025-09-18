// package com.hmall.gateway.filter;
//
// import lombok.Data;
// import org.springframework.cloud.gateway.filter.GatewayFilter;
// import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
// import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
// import org.springframework.stereotype.Component;
//
// import java.util.List;
//
// /**
//  *
//  * @author FragrantXue
//  * Create by 2025.09.17 11:51
//  */
//
// @Component
// public class MyPrintGatewayFilterFactory extends AbstractGatewayFilterFactory<MyPrintGatewayFilterFactory.Config> {
//     /**
//      * 为了自定义Order，也可以返回一个OrderedGatewayFilter，这是一个装饰器模式
//      */
//     @Override
//     public GatewayFilter apply(Config config) {
//         return new OrderedGatewayFilter(
//                 (exchange, chain) -> {
//                     System.out.println("经过了MyPrintGatewayFilter");
//                     System.out.println("a = " + config.getA());
//                     System.out.println("b = " + config.getB());
//                     System.out.println("c = " + config.getC());
//                     return chain.filter(exchange);
//                 },
//                 100
//         );
//     }
//
//     // 自定义配置属性，成员变量的名称十分重要
//     // 加入Data注解，省去getter/setter
//     @Data
//     public static class Config {
//         private String a;
//         private String b;
//         private String c;
//     }
//
//     // 指定成员变量的顺序，必须和Config类中的成员变量一一对应
//     @Override
//     public List<String> shortcutFieldOrder() {
//         return List.of("a", "b", "c");
//     }
//
//     // 将Config的字节码传递给父类，以便父类读取yaml配置
//     public MyPrintGatewayFilterFactory() {
//         super(Config.class);
//     }
// }
