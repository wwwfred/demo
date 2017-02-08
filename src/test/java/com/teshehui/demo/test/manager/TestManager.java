package com.teshehui.demo.test.manager;

import net.wwwfred.framework.core.hessian.HessianClientUtil;
import net.wwwfred.framework.demo.controller.spi.request.SayHelloRequest;
import net.wwwfred.framework.demo.manager.MyManager;
import net.wwwfred.framework.spi.request.BaseRequest;
import net.wwwfred.framework.util.json.JSONUtil;
import net.wwwfred.framework.util.log.LogUtil;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestManager {

	private static ApplicationContext context;
	
	public static void init()
	{
		context = new ClassPathXmlApplicationContext("config/spring/spring-config-framework.xml");
	}
	
	public static void main(String[] args) {
		
		init();
		testLocalManager();
		testRemoteDubboManager();
		
//		testRemoteHessianManager();
	}
	
	public static void testLocalManager()
	{
		MyManager myManager = context.getBean("myManagerImpl",MyManager.class);
		LogUtil.i(myManager.sayHello(new SayHelloRequest("1.0.0",null,null,"fred")));
		LogUtil.i(JSONUtil.toString(myManager.getDataFromCache(new BaseRequest("1.0.0", null, null))));
	}
	
	public static void testRemoteDubboManager()
	{
		MyManager myManager = context.getBean("remoteMyDubboManager",MyManager.class);
		LogUtil.i(myManager.sayHello(new SayHelloRequest("1.0.0",null,null,"fred")));
		LogUtil.i(JSONUtil.toString(myManager.getDataFromCache(new BaseRequest("1.0.0", null, null))));
	}
	
	public static void testRemoteHessianManager()
	{
//		MyManager myManager = context.getBean("remoteMyHessianManager",MyManager.class);
//		LogUtil.i(myManager.sayHello(new BaseRequestPO<String>("1.0.0",null,null,"fred")));
//		LogUtil.i(JSONUtil.toString(myManager.getUsers(new BaseRequestPO<Object>("1.0.0", null, null, null))));
		
		MyManager myManager2 = HessianClientUtil.getHessianService("http://127.0.0.1:9090/Demo/myHessianManager.do", MyManager.class);
		LogUtil.i(myManager2.sayHello(new SayHelloRequest("1.0.0",null,null,"fred")));
		LogUtil.i(JSONUtil.toString(myManager2.getDataFromCache(new BaseRequest("1.0.0", null, null))));
	}
	
	
}
