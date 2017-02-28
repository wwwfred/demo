package net.wwwfred.framework.demo.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.wwwfred.framework.core.aop.log.LogAnnotaion;
import net.wwwfred.framework.core.cache.MemCache;
import net.wwwfred.framework.core.cache.RedisCache;
import net.wwwfred.framework.core.dao.DaoQueryCondition;
import net.wwwfred.framework.core.dao.DaoQueryOperator;
import net.wwwfred.framework.core.dao.mybatis.MybatisDao;
import net.wwwfred.framework.core.exception.FrameworkRuntimeException;
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

@Service("myService2")
@LogAnnotaion
@Transactional(propagation=Propagation.NOT_SUPPORTED)
public class MyService2Impl implements MyService{

	@Resource
	private MemCache memCache;
	
	@Resource
	private RedisCache redisCache;
	
	@Resource
	private MybatisDao mybatisDao;
	
	@Resource
	private MybatisDao mybatisDao2;
	
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
		
		{
			Long mobilePhoneUserCountResult = mybatisDao.count(null, false, MemberModel.class, new DaoQueryCondition("mobilePhone", DaoQueryOperator.EQ, mobilePhone));
			if(mobilePhoneUserCountResult>0)
				throw new FrameworkRuntimeException("用户已存在");
			MemberModel member = new MemberModel();
			member.setMemberCardNo("123456789");
			member.setUserName(userName+"mySql");
			member.setBirthday(birthday);
			member.setSex(sex);
			member.setMobilePhone(mobilePhone);
			member = mybatisDao.insertOne(member);
			
			mobilePhoneUserCountResult = mybatisDao.count(null, false, UserCashAccountModel.class, new DaoQueryCondition("userId", DaoQueryOperator.EQ, member.getUserId()));
			if(mobilePhoneUserCountResult>0)
				throw new FrameworkRuntimeException("用户账户已存在");
			UserCashAccountModel userCashAccount = new UserCashAccountModel();
			userCashAccount.setUserName(userName);
			userCashAccount.setMobilePhone(mobilePhone);
			userCashAccount.setUserId(member.getUserId());
			userCashAccount = mybatisDao.insertOne(userCashAccount);
			
			mobilePhoneUserCountResult = mybatisDao.count(null, false, UserCashAccountBalanceModel.class, new DaoQueryCondition("cashAccountId", DaoQueryOperator.EQ, userCashAccount.getId()));
			if(mobilePhoneUserCountResult>0)
				throw new FrameworkRuntimeException("用户账户余额已存在");
			Map<String, Long> balanceMap = JSONUtil.toMap(balance, Long.class);
			UserCashAccountBalanceModel userCashAccountBalance = new UserCashAccountBalanceModel();
			String currencyTypeCode = "01";
			userCashAccountBalance.setBalance(balanceMap.get(currencyTypeCode));
			userCashAccountBalance.setCurrencyTypeCode(currencyTypeCode);
			userCashAccountBalance.setCashAccountId(userCashAccount.getId());
			userCashAccountBalance.setUserId(member.getUserId());
			mybatisDao.insertOne(userCashAccountBalance);
			UserCashAccountBalanceModel userCashAccountBalance2 = new UserCashAccountBalanceModel();
			currencyTypeCode = "02";
			userCashAccountBalance2.setBalance(balanceMap.get(currencyTypeCode));
			userCashAccountBalance2.setCurrencyTypeCode(currencyTypeCode);
			userCashAccountBalance2.setCashAccountId(userCashAccount.getId());
			userCashAccountBalance2.setUserId(member.getUserId());
			mybatisDao.insertOne(userCashAccountBalance2);
		}
		{
			Long mobilePhoneUserCountResult = mybatisDao2.count(null, false, MemberModel.class, new DaoQueryCondition("mobilePhone", DaoQueryOperator.EQ, mobilePhone));
			if(mobilePhoneUserCountResult>0)
				throw new FrameworkRuntimeException("用户已存在");
			MemberModel member = new MemberModel();
			member.setMemberCardNo("123456789");
			member.setUserName(userName+"oracle");
			member.setBirthday(birthday);
			member.setSex(sex);
			member.setMobilePhone(mobilePhone);
			member = mybatisDao2.insertOne(member);
			
			mobilePhoneUserCountResult = mybatisDao2.count(null, false, UserCashAccountModel.class, new DaoQueryCondition("userId", DaoQueryOperator.EQ, member.getUserId()));
			if(mobilePhoneUserCountResult>0)
				throw new FrameworkRuntimeException("用户账户已存在");
			UserCashAccountModel userCashAccount = new UserCashAccountModel();
			userCashAccount.setUserName(userName);
			userCashAccount.setMobilePhone(mobilePhone);
			userCashAccount.setUserId(member.getUserId());
			userCashAccount = mybatisDao2.insertOne(userCashAccount);
			
			mobilePhoneUserCountResult = mybatisDao2.count(null, false, UserCashAccountBalanceModel.class, new DaoQueryCondition("cashAccountId", DaoQueryOperator.EQ, userCashAccount.getId()));
			if(mobilePhoneUserCountResult>0)
				throw new FrameworkRuntimeException("用户账户余额已存在");
			Map<String, Long> balanceMap = JSONUtil.toMap(balance, Long.class);
			UserCashAccountBalanceModel userCashAccountBalance = new UserCashAccountBalanceModel();
			String currencyTypeCode = "01";
			userCashAccountBalance.setBalance(balanceMap.get(currencyTypeCode));
			userCashAccountBalance.setCurrencyTypeCode(currencyTypeCode);
			userCashAccountBalance.setCashAccountId(userCashAccount.getId());
			userCashAccountBalance.setUserId(member.getUserId());
			mybatisDao2.insertOne(userCashAccountBalance);
			UserCashAccountBalanceModel userCashAccountBalance2 = new UserCashAccountBalanceModel();
			currencyTypeCode = "02";
			userCashAccountBalance2.setBalance(balanceMap.get(currencyTypeCode));
			userCashAccountBalance2.setCurrencyTypeCode(currencyTypeCode);
			userCashAccountBalance2.setCashAccountId(userCashAccount.getId());
			userCashAccountBalance2.setUserId(member.getUserId());
			mybatisDao2.insertOne(userCashAccountBalance2);
		}
	}
	
	@Override
	public MemberModel getUser(String mobilePhone) {
		return (MemberModel) mybatisDao.selectOne(null, MemberModel.class, new DaoQueryCondition("mobilePhone", DaoQueryOperator.EQ, mobilePhone));
	}
	
	@Override
	public Map<MemberModel, UserCashAccountModel> getUserCashAccount(
			List<String> mobilePhoneList) {
		
		Map<MemberModel, UserCashAccountModel> map = new LinkedHashMap<MemberModel, UserCashAccountModel>();
		
		MemberModel member = mybatisDao.selectOne(null, MemberModel.class, new DaoQueryCondition("mobilePhone", DaoQueryOperator.IN, mobilePhoneList));
		UserCashAccountModel userCashAccount = mybatisDao.selectOne(null, UserCashAccountModel.class, new DaoQueryCondition("userId", DaoQueryOperator.EQ, member.getUserId()));
		map.put(member, userCashAccount);
		
		return map;
	}
	
	@Override
	public UserInfoPO queryUserInfo(String mobilePhone) {
		UserInfoPO userInfo = new UserInfoPO();
		
		MemberModel member = null;
		UserCashAccountModel userCashAccount = null;
		List<UserCashAccountBalanceModel> balanceList = new ArrayList<UserCashAccountBalanceModel>();
		member = mybatisDao2.selectOne(null, MemberModel.class, new DaoQueryCondition("mobilePhone", DaoQueryOperator.EQ, mobilePhone));
		if(member!=null)
		{
			userCashAccount = mybatisDao2.selectOne(null, UserCashAccountModel.class, new DaoQueryCondition("userId", DaoQueryOperator.EQ, member.getUserId()));
			if(userCashAccount!=null)
			{
				balanceList = mybatisDao2.selectList(null, false, UserCashAccountBalanceModel.class, null, null, new DaoQueryCondition("cashAccountId", DaoQueryOperator.EQ, userCashAccount.getId()));
			}
		}
		userInfo.setMember(member);
		userInfo.setUserCashAccount(userCashAccount);
		userInfo.setUserCashAccountBalanceList(balanceList);
		
		return userInfo;
	}
	
	@Override
	public List<MemberModel> queryMemberByName(String name, Integer pageNo, Integer pageSize) {
		// TODO Auto-generated method stub
		MemberModel member = new MemberModel();
		member.setUserName(name);
		return mybatisDao2.selectList(null, false, MemberModel.class, pageNo, pageSize, new DaoQueryCondition("userName", DaoQueryOperator.LIKE, name));
	}
	
	@Override
	@Transactional
	public void updateUserNameByMobilePhone(String userName, String mobilePhone) {
		
		Map<String, Object> fieldValueMap = new HashMap<String, Object>();
		fieldValueMap.put("userName", userName);
		mybatisDao.update(fieldValueMap, MemberModel.class, new DaoQueryCondition("mobilePhone", DaoQueryOperator.EQ, mobilePhone));
		
	}
	
	@Override
	@Transactional
	public void deleteUserByMobilePhone(String mobilePhone) {
		mybatisDao.deleteList(MemberModel.class, new DaoQueryCondition("mobilePhone", DaoQueryOperator.EQ, mobilePhone));
	}
	
}
