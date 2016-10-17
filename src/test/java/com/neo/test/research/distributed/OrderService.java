package com.neo.test.research.distributed;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by neowyp on 2016/10/14.
 * Author   : wangyunpeng
 * Date     : 2016/10/14
 * Time     : 16:35
 * Version  : V1.0
 * Desc     :
 */
@Service("orderService")
@Slf4j
public class OrderService {


    @DistributedTransaction(rbName = "rbCreateOrder")
    public void createOrder() {
        log.info("create order");
    }

    public void rbCreateOrder() {
        log.info("roll back create order operate！");

    }
}
