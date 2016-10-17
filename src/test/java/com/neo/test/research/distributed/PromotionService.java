package com.neo.test.research.distributed;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by neowyp on 2016/10/14.
 * Author   : wangyunpeng
 * Date     : 2016/10/14
 * Time     : 16:41
 * Version  : V1.0
 * Desc     :
 */
@Service("promotionService")
@Slf4j
public class PromotionService {

    @DistributedTransaction(rbName = "rbSubCoupon")
    public void subCoupon() {
        log.info("sub user coupon!");
    }

    public void rbSubCoupon() {
        log.info("roll back sub user coupon operate！");
    }
}
