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
@Service("itemService")
@Slf4j
public class ItemService {

    @DistributedTransaction(rbName = "rbSubStock")
    public void subStock() {
        log.info("sub item stock");
        throw new RuntimeException("manual item error!");
    }

    public void rbSubStock() {
        log.info("roll back sub item stock operate！");
    }
}
