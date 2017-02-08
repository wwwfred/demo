package com.teshehui.demo.test.service;

import java.util.List;

import net.wwwfred.framework.core.dao.DaoQueryCondition;
import net.wwwfred.framework.core.dao.DaoQueryOperator;
import net.wwwfred.framework.core.dao.mybatis.MybatisDao;
import net.wwwfred.framework.demo.model.MemberModel;
import net.wwwfred.framework.util.json.JSONUtil;
import net.wwwfred.framework.util.log.LogUtil;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestService {

	private static ApplicationContext context;
	
	public static void init()
	{
		context = new ClassPathXmlApplicationContext("config/spring/spring-config-framework.xml");
	}
	
	public static void main(String[] args) {
		
		init();
//		testInsert();
		testPageQuery();
	}

	public static void testPageQuery() {
		
		MybatisDao mybatisDao = context.getBean("mybatisDao", MybatisDao.class);
		List<Long> list = mybatisDao.selectList(new String[]{"userId"}, false, MemberModel.class, 2, 5, new DaoQueryCondition("userName", DaoQueryOperator.LIKE, "%wwwTest%"));
		LogUtil.i(JSONUtil.toString(list));
	}

	public static void testInsert() {
		
		MybatisDao mybatisDao = context.getBean("mybatisDao", MybatisDao.class);
		for (int i = 0; i < 20; i++) {
			MemberModel member = new MemberModel();
			member.setUserName("wwwTest"+(i+1));
			member.setBirthday("1988-02-06");
			member.setSex("1");
			member.setMobilePhone((15171466141L+i)+"");
			mybatisDao.insertOne(member);
		}
		
	}
	

	
}
