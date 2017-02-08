package net.wwwfred.framework.demo.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import net.wwwfred.framework.core.aop.log.LogAnnotaion;
import net.wwwfred.framework.core.cache.MemCache;
import net.wwwfred.framework.core.cache.RedisCache;
import net.wwwfred.framework.core.dao.DaoQueryCondition;
import net.wwwfred.framework.core.dao.DaoQueryOperator;
import net.wwwfred.framework.core.exception.TeshehuiRuntimeException;
import net.wwwfred.framework.demo.dao.UserInfoDao;
import net.wwwfred.framework.demo.dao.po.UserInfoPO;
import net.wwwfred.framework.demo.model.MemberModel;
import net.wwwfred.framework.demo.model.UserCashAccountBalanceModel;
import net.wwwfred.framework.demo.model.UserCashAccountModel;
import net.wwwfred.framework.demo.po.UserPO;
import net.wwwfred.framework.demo.service.MyService;
import net.wwwfred.framework.util.json.JSONUtil;
import net.wwwfred.framework.util.log.LogUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("myService1")
@LogAnnotaion
@Transactional(propagation=Propagation.NOT_SUPPORTED)
public class MyService1Impl implements MyService{

	@Resource
	private MemCache memCache;
	
	@Resource
	private RedisCache redisCache;
	
	@Resource
	private UserInfoDao userInfoDao;
	
	public String sayHello(String name) {
		return "Hello " + name;
	}

	public List<UserPO> getDataFromCache() {
		
		List<UserPO> list = new ArrayList<UserPO>();
		
		// memCache
		Object memCacheJSONStringResult = memCache.getObject("userList");
		if(memCacheJSONStringResult!=null)
		{
			UserPO[] userArray = JSONUtil.toModel(memCacheJSONStringResult.toString(), UserPO[].class);
			LogUtil.i("getUsers from memCache");
			return Arrays.asList(userArray);
		}
		
		// redisCache
		Object redisCacheJSONStringResult = redisCache.getObject("userList");
		if(redisCacheJSONStringResult!=null)
		{
			UserPO[] userArray = JSONUtil.toModel(redisCacheJSONStringResult.toString(), UserPO[].class);
			memCache.cacheObject("userList", redisCacheJSONStringResult.toString());
			LogUtil.i("getUsers from redisCache");
			return Arrays.asList(userArray);
		}
		
		UserPO u1 = new UserPO();
		u1.setName("jack");
		u1.setAge(20);
		u1.setSex("男");

		UserPO u2 = new UserPO();
		u2.setName("tom");
		u2.setAge(21);
		u2.setSex("女");

		UserPO u3 = new UserPO();
		u3.setName("rose");
		u3.setAge(19);
		u3.setSex("女");

		list.add(u1);
		list.add(u2);
		list.add(u3);
		redisCache.setObject("userList", JSONUtil.toString(list));
		LogUtil.i("getUsers from db");
		return list;
	}
	
	@Override
	@Transactional
	public void createUser(String userName, String birthday,String sex, String mobilePhone, String balance) {
		
		Long mobilePhoneUserCountResult = userInfoDao.count(null, false, MemberModel.class, new DaoQueryCondition("mobilePhone", DaoQueryOperator.EQ, mobilePhone));
		if(mobilePhoneUserCountResult>0)
			throw new TeshehuiRuntimeException("用户已存在");
		MemberModel member = new MemberModel();
		member.setMemberCardNo("123456789");
		member.setUserName(userName);
		member.setBirthday(birthday);
		member.setSex(sex);
		member.setMobilePhone(mobilePhone);
		member = userInfoDao.insertOne(member);
		
		mobilePhoneUserCountResult = userInfoDao.count(null, false, UserCashAccountModel.class, new DaoQueryCondition("userId", DaoQueryOperator.EQ, member.getUserId()));
		if(mobilePhoneUserCountResult>0)
			throw new TeshehuiRuntimeException("用户账户已存在");
		UserCashAccountModel userCashAccount = new UserCashAccountModel();
		userCashAccount.setUserName(userName);
		userCashAccount.setMobilePhone(mobilePhone);
		userCashAccount.setUserId(member.getUserId());
		userCashAccount = userInfoDao.insertOne(userCashAccount);
		
		mobilePhoneUserCountResult = userInfoDao.count(null, false, UserCashAccountBalanceModel.class, new DaoQueryCondition("cashAccountId", DaoQueryOperator.EQ, userCashAccount.getId()));
		if(mobilePhoneUserCountResult>0)
			throw new TeshehuiRuntimeException("用户账户余额已存在");
		Map<String, Long> balanceMap = JSONUtil.toMap(balance, Long.class);
		UserCashAccountBalanceModel userCashAccountBalance = new UserCashAccountBalanceModel();
		String currencyTypeCode = "01";
		userCashAccountBalance.setBalance(balanceMap.get(currencyTypeCode));
		userCashAccountBalance.setCurrencyTypeCode(currencyTypeCode);
		userCashAccountBalance.setCashAccountId(userCashAccount.getId());
		userCashAccountBalance.setUserId(member.getUserId());
		userInfoDao.insertOne(userCashAccountBalance);
		UserCashAccountBalanceModel userCashAccountBalance2 = new UserCashAccountBalanceModel();
		currencyTypeCode = "02";
		userCashAccountBalance2.setBalance(balanceMap.get(currencyTypeCode));
		userCashAccountBalance2.setCurrencyTypeCode(currencyTypeCode);
		userCashAccountBalance2.setCashAccountId(userCashAccount.getId());
		userCashAccountBalance2.setUserId(member.getUserId());
		userInfoDao.insertOne(userCashAccountBalance2);
		
	}
	
	@Override
	public MemberModel getUser(String mobilePhone) {
		MemberModel member = new MemberModel();
		member.setMobilePhone(mobilePhone);
		return userInfoDao.getMember(member);
	}
	
	@Override
	public Map<MemberModel, UserCashAccountModel> getUserCashAccount(
			List<String> mobilePhoneList) {
		
		Map<MemberModel, UserCashAccountModel> result = new LinkedHashMap<MemberModel, UserCashAccountModel>();
		
		Map<String, Long> map = userInfoDao.getUserCashAccount(mobilePhoneList);
		for (Entry<String, Long> one : map.entrySet()) {
			MemberModel member = new MemberModel();
			member.setMobilePhone(one.getKey());
			UserCashAccountModel userCashAccount = new UserCashAccountModel();
			userCashAccount.setId(one.getValue());
			
			result.put(member, userCashAccount);
		}
		
		return result;
	}
	
	@Override
	public UserInfoPO queryUserInfo(String mobilePhone) {
		MemberModel member = new MemberModel();
		member.setMobilePhone(mobilePhone);
		return userInfoDao.queryUserInfo(member);
	}
	
	@Override
	public List<MemberModel> queryMemberByName(String name, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		MemberModel member = new MemberModel();
		member.setUserName(name);
		return userInfoDao.queryMemberByName(member,pageNo,pageSize);
	}
	
	@Override
	public void updateUserNameByMobilePhone(String userName, String mobilePhone) {
		
		MemberModel member = new MemberModel();
		member.setMobilePhone(mobilePhone);
		member.setUserName(userName);
		userInfoDao.updateUserNameByMobilePhone(member);
	}
	
	@Override
	@Transactional
	public void deleteUserByMobilePhone(String mobilePhone) {
		userInfoDao.deleteUserByMobilePhone(mobilePhone);
	}

}
