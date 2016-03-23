package com.neo.annotation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neowyp on 2016/3/23.
 */
public class VShopDao {

    private final static List<VShopDO> vshops = new ArrayList<VShopDO>();
    //模拟测试数据
    {
        vshops.add(new VShopDO(1L,201L,"vshop-1-201"));
        vshops.add(new VShopDO(2L,202L,"vshop-2-202"));
        vshops.add(new VShopDO(3L,203L,"vshop-3-203"));
        vshops.add(new VShopDO(4L,204L,"vshop-4-204"));
        vshops.add(new VShopDO(5L,205L,"vshop-5-205"));
    }


    /**
     * @param id
     * @return
     */
    public static VShopDO getById(Long id) {
        for (VShopDO vshop : vshops) {
            if (vshop.getId().equals(id))
                return vshop;
        }

        return null;
    }


    /**
     * @param shopid
     * @return
     */
    public static VShopDO getByShopId(Long shopid) {
        for (VShopDO vshop : vshops) {
            if (vshop.getShopId().equals(shopid))
                return vshop;
        }

        return null;
    }
}
