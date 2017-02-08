package com.teshehui.demo.test.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.wwwfred.framework.core.cache.RedisCache;
import net.wwwfred.framework.core.dao.DaoQueryCondition;
import net.wwwfred.framework.core.dao.DaoQueryOperator;
import net.wwwfred.framework.core.dao.mybatis.MybatisDao;
import net.wwwfred.framework.util.date.DatetimeFormat;
import net.wwwfred.framework.util.date.DatetimeUtil;
import net.wwwfred.framework.util.sort.SortUtil;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.teshehui.demo.test.model.MemberSignLogModel;
import com.teshehui.demo.test.model.MemberSignModel;

public class TestUserSign {

	private static ApplicationContext context;
	
	public static void init()
	{
		context = new ClassPathXmlApplicationContext("config/spring/spring-config-framework.xml");
	}
	
	public static void main(String[] args) {
		
		init();
		testUserSignData();
		
	}

	private static void testUserSignData() {
		// TODO Auto-generated method stub
		
		MybatisDao mybatisDao2 = context.getBean("mybatisDao2", MybatisDao.class);
		
		RedisCache redisCache = context.getBean(RedisCache.class);
		
		Long count = mybatisDao2.count(null, false, MemberSignModel.class, new DaoQueryCondition[]{});
		System.out.println(count);
		
		List<MemberSignLogModel> list = mybatisDao2.selectList(new String[]{"userId","signTime"}, false, MemberSignLogModel.class, null, null, new DaoQueryCondition[]{});
		list = SortUtil.getSortedList(list, new Comparator<MemberSignLogModel>() {

			@Override
			public int compare(MemberSignLogModel o1, MemberSignLogModel o2) {
				int userIdResult = o1.getUserId().compareTo(o2.getUserId());
				if(userIdResult==0)
				{
					long signTimeResult = o1.getSignTime().getTime()-o2.getSignTime().getTime();
					return new Long(signTimeResult).intValue();
				}
				return userIdResult;
			}
		});
		Map<Long, Map<String,List<String>>> map = new LinkedHashMap<Long,Map<String,List<String>>>();
		for (MemberSignLogModel m : list) {
			Long userId = m.getUserId();
			long signTimeLong = m.getSignTime().getTime();
			String signDate = DatetimeUtil.longToDateTimeString(signTimeLong, DatetimeFormat.STANDARED_DATE_FORMAT);
			String signTime = DatetimeUtil.longToDateTimeString(signTimeLong, DatetimeFormat.STANDARED_DATE_TIME_FORMAT);
			if(!map.containsKey(userId))
			{
				map.put(userId, new LinkedHashMap<String, List<String>>());
			}
			if(!map.get(userId).containsKey(signDate))
			{
				map.get(userId).put(signDate, new ArrayList<String>());
			}
			map.get(userId).get(signDate).add(signTime);
		}
		Map<Long, List<String>> map2 = new LinkedHashMap<Long, List<String>>();
		Set<Long> keySet = map.keySet();
		for (Long userId : keySet) {
			if(!map2.containsKey(userId))
			{
				map2.put(userId, new ArrayList<String>());
			}
			map2.get(userId).addAll(map.get(userId).keySet());
		}
		Map<Long, List<String>> map3 = new LinkedHashMap<Long, List<String>>();
		for (Long userId : keySet) {
			List<String> signDateList = map2.get(userId);
			if(signDateContinue(signDateList))
			{
				map3.put(userId, signDateList);
			}
		}
		Map<Long, Integer> map4 = new LinkedHashMap<Long, Integer>();
		Set<Long> userIdKeySet = map3.keySet();
		for (Long userId : userIdKeySet) {
			map4.put(userId, map3.get(userId).size());
		}
		map4 = SortUtil.getSortedValueMap(map4,new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o2.compareTo(o1);
			}
		});
		userIdKeySet = map4.keySet();
		Map<Long, Integer> map5 = new LinkedHashMap<Long, Integer>();
		for (Long userId : userIdKeySet) {
			Integer signCount = map4.get(userId);
			if(signCount>1)
			{
				map5.put(userId, signCount);
			}
		}
		System.out.println(map5);
		
//		List<MemberSignModel> memberSignList = mybatisDao2.selectList(new String[]{"userId","currentSignNum","maxSignNum","minSignNum"}, false, MemberSignModel.class, null, null, new DaoQueryCondition("userId", DaoQueryOperator.IN, map5.keySet()));
//		for (MemberSignModel memberSignModel : memberSignList) {
//			Long userId = memberSignModel.getUserId();
//			Integer realCurrentSignNum = map5.get(userId);
//			memberSignModel.setRealCurrentSignNum(realCurrentSignNum.longValue());
//			memberSignModel.setCreateTime(null);
//		}
//		memberSignList = SortUtil.getSortedList(memberSignList, new Comparator<MemberSignModel>() {
//
//			@Override
//			public int compare(MemberSignModel o1, MemberSignModel o2) {
//				// TODO Auto-generated method stub
//				return o2.getRealCurrentSignNum().compareTo(o1.getRealCurrentSignNum());
//			}
//		});
//		System.out.println(JSONUtil.toString(memberSignList));
		
		userIdKeySet = map5.keySet();
		for (Long userId : userIdKeySet) {
			Map<String, Object> valueMap = new HashMap<String, Object>();
			valueMap.put("currentSignNum", map5.get(userId));
			valueMap.put("maxSignNum", map5.get(userId));
			mybatisDao2.update(valueMap, MemberSignModel.class, new DaoQueryCondition("userId", DaoQueryOperator.EQ, userId));
			redisCache.setObject(userId+"sign",map5.get(userId));
		}
	}

	private static boolean signDateContinue(List<String> signDateList) {
		// TODO Auto-generated method stub
		List<Long> list = new ArrayList<Long>();
		for (String signDate : signDateList) {
			list.add(DatetimeUtil.dateTimeStringToLong(signDate, DatetimeFormat.STANDARED_DATE_FORMAT));
		}
		int size = list.size();
		for (int i = 0; i < size-1; i++) {
			if(list.get(i+1)-list.get(i)!=3600*1000*24L)
			{
				return false;
			}
		}
		return true;
	}
	
}
