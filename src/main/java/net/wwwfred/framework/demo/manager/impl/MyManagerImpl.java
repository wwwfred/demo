package net.wwwfred.framework.demo.manager.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.wwwfred.framework.demo.controller.spi.request.CreateUserRequest;
import net.wwwfred.framework.demo.controller.spi.request.DeleteUserRequest;
import net.wwwfred.framework.demo.controller.spi.request.GetUserCashAccountRequest;
import net.wwwfred.framework.demo.controller.spi.request.GetUserFromNameRequest;
import net.wwwfred.framework.demo.controller.spi.request.GetUserRequest;
import net.wwwfred.framework.demo.controller.spi.request.QueryUserInfoRequest;
import net.wwwfred.framework.demo.controller.spi.request.QueryUserListRequest;
import net.wwwfred.framework.demo.controller.spi.request.SayHelloRequest;
import net.wwwfred.framework.demo.controller.spi.request.UpdateUserRequest;
import net.wwwfred.framework.demo.dao.po.UserInfoPO;
import net.wwwfred.framework.demo.manager.MyManager;
import net.wwwfred.framework.demo.manager.util.CheckBaseRequestUtil;
import net.wwwfred.framework.demo.model.MemberModel;
import net.wwwfred.framework.demo.model.UserCashAccountBalanceModel;
import net.wwwfred.framework.demo.model.UserCashAccountModel;
import net.wwwfred.framework.demo.po.UserPO;
import net.wwwfred.framework.demo.service.MyService;
import net.wwwfred.framework.po.ResultSetPO;
import net.wwwfred.framework.spi.request.BaseRequest;
import net.wwwfred.framework.spi.response.BaseResponse;
import net.wwwfred.framework.util.code.CodeUtil;
import net.wwwfred.framework.util.date.DatetimeFormat;
import net.wwwfred.framework.util.date.DatetimeUtil;
import net.wwwfred.framework.util.json.JSONUtil;

import org.springframework.stereotype.Component;

@Component("myManager")
public class MyManagerImpl implements MyManager{

	@Resource
	private Map<String, MyService> myServiceMap;
	
	public BaseResponse<String> sayHello(SayHelloRequest requestPO) {
		
		CheckBaseRequestUtil.checkRequestVersion(requestPO, myServiceMap);
		
		return new BaseResponse<String>(myServiceMap.get(requestPO.getVersion()).sayHello(requestPO.getData()));

	}
	
	public BaseResponse<ResultSetPO<UserPO>> getDataFromCache(
			BaseRequest requestPO) {
		
		CheckBaseRequestUtil.checkRequestVersion(requestPO, myServiceMap);
		
		List<UserPO> userList = myServiceMap.get(requestPO.getVersion()).getDataFromCache();
		ResultSetPO<UserPO> responseData = new ResultSetPO<UserPO>(1, userList.size(), userList);
		return new BaseResponse<ResultSetPO<UserPO>>(responseData);
	}
	
	@Override
	public BaseResponse<UserPO> createUser(CreateUserRequest requestPO) {
		
		CheckBaseRequestUtil.checkRequestVersion(requestPO, myServiceMap);
		
		UserPO user = requestPO.getData();
		myServiceMap.get(requestPO.getVersion()).createUser(user.getName(), user.getBirthday(), user.getSex(), user.getMobilePhone(), user.getBalance());
		
		return new BaseResponse<UserPO>(user);
	}
	
	@Override
	public BaseResponse<UserPO> getUser(GetUserRequest requestPO) {
		
		CheckBaseRequestUtil.checkRequestVersion(requestPO, myServiceMap);
		
		String mobilePhone = requestPO.getData();
		MemberModel member = myServiceMap.get(requestPO.getVersion()).getUser(mobilePhone);
		UserPO user = null;
		if(member!=null)
		{
			user = new UserPO();
			user.setAge(new Long((System.currentTimeMillis()-DatetimeUtil.dateTimeStringToLong(member.getBirthday(), DatetimeFormat.STANDARED_DATE_FORMAT))/(12*30*24*3600*1000L)).intValue());
			user.setUserId(member.getUserId());
			user.setBirthday(member.getBirthday());
			user.setMobilePhone(member.getMobilePhone());
			user.setName(member.getUserName());
			user.setSex(member.getSex());
		}
		
		return new BaseResponse<UserPO>(user);
	}
	
	@Override
	public BaseResponse<UserPO> getUser(GetUserFromNameRequest requestPO) {
		
		CheckBaseRequestUtil.checkRequestVersion(requestPO, myServiceMap);
		
		String userName = requestPO.getData();
		MemberModel member = myServiceMap.get(requestPO.getVersion()).getUser(userName);
		UserPO user = null;
		if(member!=null)
		{
			user = new UserPO();
			user.setAge(new Long((System.currentTimeMillis()-DatetimeUtil.dateTimeStringToLong(member.getBirthday(), DatetimeFormat.STANDARED_DATE_FORMAT))/(12*30*24*3600*1000L)).intValue());
			user.setUserId(member.getUserId());
			user.setBirthday(member.getBirthday());
			user.setMobilePhone(member.getMobilePhone());
			user.setName(member.getUserName());
			user.setSex(member.getSex());
		}
		
		return new BaseResponse<UserPO>(user);
	}
	
	@Override
	public BaseResponse<UserPO> getUserCashAccount(GetUserCashAccountRequest requestPO) {
		CheckBaseRequestUtil.checkRequestVersion(requestPO, myServiceMap);
		
		String mobilePhone = requestPO.getData();
		Map<MemberModel,UserCashAccountModel> map = myServiceMap.get(requestPO.getVersion()).getUserCashAccount(Arrays.asList(new String[]{mobilePhone}));
		UserPO user = new UserPO();
		MemberModel member = map.keySet().iterator().next();
		UserCashAccountModel userCashAccount = map.values().iterator().next();
		user.setAge(new Long((System.currentTimeMillis()-DatetimeUtil.dateTimeStringToLong(member.getBirthday(), DatetimeFormat.STANDARED_DATE_FORMAT))/(12*30*24*3600*1000L)).intValue());
		user.setUserId(member.getUserId());
		user.setBirthday(member.getBirthday());
		user.setMobilePhone(member.getMobilePhone());
		user.setName(member.getUserName());
		user.setSex(member.getSex());
		
		user.setUserCashAccountId(userCashAccount.getId());
		
		return new BaseResponse<UserPO>(user);
	}
	
	@Override
	public BaseResponse<UserPO> queryUserInfo(QueryUserInfoRequest requestPO) {
		CheckBaseRequestUtil.checkRequestVersion(requestPO, myServiceMap);
		
		String mobilePhone = requestPO.getData();
		UserInfoPO userInfo = myServiceMap.get(requestPO.getVersion()).queryUserInfo(mobilePhone);
		UserPO user = new UserPO();
		if(userInfo!=null)
		{
			MemberModel member = userInfo.getMember();
			if(member!=null)
			{
				UserCashAccountModel userCashAccount = userInfo.getUserCashAccount();
				List<UserCashAccountBalanceModel> list = userInfo.getUserCashAccountBalanceList();
				user.setAge(new Long((System.currentTimeMillis()-DatetimeUtil.dateTimeStringToLong(member.getBirthday(), DatetimeFormat.STANDARED_DATE_FORMAT))/(12*30*24*3600*1000L)).intValue());
				user.setUserId(member.getUserId());
				user.setBirthday(member.getBirthday());
				user.setMobilePhone(member.getMobilePhone());
				user.setName(member.getUserName());
				user.setSex(member.getSex());
				
				if(userCashAccount!=null)
				{
					user.setUserCashAccountId(userCashAccount.getId());
				}
				
				if(!CodeUtil.isEmpty(userCashAccount))
					{List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
					for (UserCashAccountBalanceModel one : list) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put(one.getCurrencyTypeCode(), one.getBalance());
						map.put("id", one.getId());
					
						mapList.add(map);
					}
					user.setBalance(JSONUtil.toString(mapList));
				}
				
			}
		}
		
		return new BaseResponse<UserPO>(user);
	}
	
	@Override
	public BaseResponse<ResultSetPO<UserPO>> queryMemberByName(QueryUserListRequest requestPO) {
		
		CheckBaseRequestUtil.checkRequestVersion(requestPO, myServiceMap);
		
		List<MemberModel> memberList = myServiceMap.get(requestPO.getVersion()).queryMemberByName(requestPO.getData(),requestPO.getPageNo(),requestPO.getPageSize());
		
		List<UserPO> list = new ArrayList<UserPO>();
		if(CodeUtil.isEmpty(list))
		{
			for (MemberModel member : memberList) {
				UserPO user = new UserPO();
				if(!CodeUtil.isEmpty(member.getBirthday()))
				{
					user.setAge(new Long((System.currentTimeMillis()-DatetimeUtil.dateTimeStringToLong(member.getBirthday(), DatetimeFormat.STANDARED_DATE_FORMAT))/(12*30*24*3600*1000L)).intValue());
				}
				user.setUserId(member.getUserId());
				user.setBirthday(member.getBirthday());
				user.setMobilePhone(member.getMobilePhone());
				user.setName(member.getUserName());
				user.setSex(member.getSex());
				
				list.add(user);
			}
		}
		ResultSetPO<UserPO> rs = new ResultSetPO<UserPO>(null, null, list);
		return new BaseResponse<ResultSetPO<UserPO>>(rs);

	}
	
	@Override
	public BaseResponse<Object> updateUser(UpdateUserRequest requestPO) {
		CheckBaseRequestUtil.checkRequestVersion(requestPO, myServiceMap);
		
		UserPO user = requestPO.getData();
		myServiceMap.get(requestPO.getVersion()).updateUserNameByMobilePhone(user.getName(), user.getMobilePhone());
		return new BaseResponse<Object>(null);
	}
	
	@Override
	public BaseResponse<Object> deleteUser(DeleteUserRequest requestPO) {
		CheckBaseRequestUtil.checkRequestVersion(requestPO, myServiceMap);
		
		String mobilePhone = requestPO.getData();
		myServiceMap.get(requestPO.getVersion()).deleteUserByMobilePhone(mobilePhone);
		return new BaseResponse<Object>(null);
	}
	
}
