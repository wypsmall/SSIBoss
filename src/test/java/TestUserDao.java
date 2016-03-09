import com.neo.user.dao.IUserDao;
import com.neo.user.entity.User;
import com.neo.user.service.IPersonService;
import com.neo.user.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by neowyp on 2016/3/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext*.xml")
public class TestUserDao {
    @Autowired
    ApplicationContext ctx;

    @Autowired
    IUserDao userDao;

    @Autowired
    IPersonService personService;

    @Autowired
    IUserService userService;

//    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("ssi-name" + System.currentTimeMillis());
        user.setPassword("ssi-Pwd" + System.currentTimeMillis());
        userDao.insert(user);
        System.out.println("user id is : " + user.getId());
    }

//    @Test
    public void Test_noTransactionalCfg() {
        personService.noTransactionalCfg();
    }
//    @Test
    public void Test_noTransactionalInjectCfg() {
        personService.noTransactionalInjectCfg();
    }
//    @Test
    public void Test_transactionalCfg() {
        personService.transactionalCfg();
    }

}
