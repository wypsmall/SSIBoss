package com.neo.user.dao;

import com.neo.common.dao.BaseDaoImpl;
import com.neo.common.page.PageBean;
import com.neo.common.page.PageParam;
import com.neo.user.entity.User;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by neowyp on 2016/3/9.
 */
@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl<User> implements IUserDao {

    @Override
    public PageBean listPage(PageParam pageParam, Map<String, Object> paramMap) {
        return super.listPage(pageParam,paramMap);
    }
}
