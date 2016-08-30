package com.neo.test.research.partten1;

import com.neo.test.research.partten1.entity.OrderEntity;
import com.neo.test.research.partten1.handler.AbstractHandler;
import com.neo.test.research.partten1.state.InitState;
import com.neo.test.research.partten1.state.StateAbs;

/**
 * Created by neowyp on 2016/8/29.
 * Author   : wangyunpeng
 * Date     : 2016/8/29
 * Time     : 14:30
 * Version  : V1.0
 * Desc     :
 */
public class Business {
//    private static final List<IHandler> handlers = new ArrayList<IHandler>();
    private static AbstractHandler header;

    public static void main(String[] args) {
        //创建订单实体
        OrderEntity order = new OrderEntity();
        //初始化订单状态
        order.setState(new InitState());


        /**
         * todo 如果将orderentity作为state的上下文
         * 那么state与orderentity就是变成相互依赖
         */

        /**
         * 状态模式将触发方法耦合在内部，将状态之间可进行状态流转的路径用方法表示
         * 状态模式只约束了节点之间能够相互跳转，但是没有约束是否可以进行触发
         * 结论就是状态模式只作为约束条件，如果状态可以跳转，后面要跟着业务校验和具体操作
         * 业务验证：是否满足库存、优惠券限制
         * 具体操作：扣减库存、扣减优惠券、操作失败后的异步回滚
         * 业务验证、具体操作考虑用责任链完成
         */

        //根据当前state的create动作判断状态是否可以流转
        StateAbs createState = order.getState().create();

        initHandler();
        header.handler();


        //如果订单创建成功则修改订单状态
        order.setState(createState);
    }


    private static void initHandler() {
        AbstractHandler handler = ValidStore.build().setNextHandler(
                ValidCoupon.build().setNextHandler(
                        ItemService.build().setNextHandler(
                                PromotionService.build().setNextHandler(null)
                        )
                )
        );

        header = handler;
    }
}
