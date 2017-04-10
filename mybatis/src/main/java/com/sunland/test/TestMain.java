package com.sunland.test;

import com.sunland.entity.CrmUser;
import com.sunland.service.CrmUserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by liuye on 2017/4/10 0010.
 */
public class TestMain {

    @Test
    public void testInsert(){
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("applicationContext.xml");

        CrmUserService crmUserService=applicationContext.getBean("crmUserServiceImpl",CrmUserService.class);

        CrmUser crmUser=new CrmUser();
        crmUser.setFirstName("L");
        crmUser.setLastName("H");

        crmUser.setAge(24);
        crmUser.setSex(false);

        crmUserService.insertCrmUser(crmUser);
    }

    @Test
    public void testSelect(){
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("applicationContext.xml");

        CrmUserService crmUserService=applicationContext.getBean("crmUserServiceImpl",CrmUserService.class);

        CrmUser crmUser=crmUserService.selectCrmUserById(1L);
    }

    @Test
    public void testUpdate(){
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("applicationContext.xml");

        CrmUserService crmUserService=applicationContext.getBean("crmUserServiceImpl",CrmUserService.class);

        CrmUser crmUser=crmUserService.selectCrmUserById(1L);

        crmUser.setFirstName("HHH");
        //crmUser.setSex(null);

        crmUserService.updateCrmUser(crmUser);
    }

}
