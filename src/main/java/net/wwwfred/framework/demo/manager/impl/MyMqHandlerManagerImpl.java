package net.wwwfred.framework.demo.manager.impl;

import net.wwwfred.framework.core.jms.JmsMessageListenerContainer;

public class MyMqHandlerManagerImpl extends JmsMessageListenerContainer{

	@Override
	public void handleMessage(Object arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0);
	}

}
