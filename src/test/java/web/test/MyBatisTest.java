package web.test;

import java.util.Date;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.test.web.entity.User;
import com.test.web.service.UserServiceI;

public class MyBatisTest {
	
	private UserServiceI userService;

	@Before
	public void before(){
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring.xml","spring-db.xml"});
		userService = (UserServiceI)context.getBean("userService");
	}
	
	@Test
	public void addUser(){
		User user = new User();
		user.setUserId(UUID.randomUUID().toString().replaceAll("-", ""));
		user.setUserName("白虎神皇xdp");
		user.setUserBirthday(new Date());
		user.setUserSalary(10000D);
		userService.addUser(user);
	}
}
