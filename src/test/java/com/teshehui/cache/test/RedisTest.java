package com.teshehui.cache.test;

import java.util.ArrayList;

import net.wwwfred.framework.core.cache.RedisCache;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RedisTest {

	private static ApplicationContext context;
	
	public static void init()
	{
		context = new ClassPathXmlApplicationContext("config/spring/spring-config-framework.xml");
	}
	
	public static void main(String[] args) {
		init();
		redisTest();
	}

	private static void redisTest() {
		// TODO Auto-generated method stub
		
		RedisCache redisCache = context.getBean(RedisCache.class);
		
//		redisCache.setObject("a", 123);
//		System.out.println(redisCache.getObject("redis_cate_null"));
//		redisCache.deleteObject("redis_cate_null");
		
		System.out.println(redisCache.keys("redis*"));
		
		long startTime = System.currentTimeMillis();
		Object o = redisCache.getObject("redis_cate_1");
		System.out.println(o);
		try
		{
			System.out.println("useTime="+(System.currentTimeMillis()-startTime)+",size="+((o instanceof ArrayList)?((ArrayList<?>)o).size():o));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.exit(0);
	}
	
}
