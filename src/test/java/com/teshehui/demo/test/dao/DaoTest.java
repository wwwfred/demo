package com.teshehui.demo.test.dao;

import net.wwwfred.framework.demo.dao.UserInfoDao;
import net.wwwfred.framework.demo.model.MemberModel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DaoTest {

	private static ApplicationContext context;
	
	public static void init()
	{
		context = new ClassPathXmlApplicationContext("config/spring/spring-config-framework.xml");
	}
	
	public static void main(String[] args) {
		
		init();

		testUserInfoDao();
	}

	private static void testUserInfoDao() {
		// TODO Auto-generated method stub
		
		UserInfoDao userInfoDao = context.getBean(UserInfoDao.class);
		MemberModel member = new MemberModel();
		member.setUserName("%test%");
		System.out.println(userInfoDao.queryMemberByName(member, 3, 3));
		
		System.out.println("finish");
		
	}
	
}
