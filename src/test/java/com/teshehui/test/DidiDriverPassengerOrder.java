package com.teshehui.test;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.wwwfred.framework.util.code.CodeUtil;
import net.wwwfred.framework.util.io.IOUtil;
import net.wwwfred.framework.util.json.JSONUtil;
import net.wwwfred.framework.util.log.LogUtil;
import net.wwwfred.framework.util.sort.SortUtil;

public class DidiDriverPassengerOrder {
	
	private static String lineSeparator = System.getProperty("line.separator");
	private static String tabSeparator = "\t";
	
	public static void main(String[] args) {
		
//		passengerDriverOrder();
		driverPassengerOrder();
		
	}
	
	public static void passengerDriverOrder()
	{
//						未付款滴滴打车“恶意”会员数据(会员总数/总金额/总订单数)
//		车型	会员单笔最大金额	会员手机	会员名称	会员总金额	会员总单数	会员总司机数	司机1（单笔最大金额/车牌/手机/名称/总金额/总订单数）	司机2（单笔最大金额/车牌/手机/名称/总金额/总订单数）	司机3（单笔最大金额/车牌/手机/名称/总金额/总订单数）
		
		String s1 = new String(IOUtil.getByteArrayFromInputStream(DidiDriverPassengerOrder.class.getClassLoader().getResourceAsStream("order_driver.txt")),Charset.forName("UTF-8"));
		OrderDriverModel[] orderDriverArray = JSONUtil.toModel(s1, OrderDriverModel[].class);
		Map<String, String> orderDriverMap = new HashMap<String, String>();
		for (OrderDriverModel one : orderDriverArray) {
			orderDriverMap.put(one.getOrder_id(), one.getDriver_phone());
		}
		String s2 = new String(IOUtil.getByteArrayFromInputStream(DidiDriverPassengerOrder.class.getClassLoader().getResourceAsStream("remainpay.txt")),Charset.forName("UTF-8"));
		OrderModel[] orderArray = JSONUtil.toModel(s2, OrderModel[].class);
		for (OrderModel one : orderArray) {
			String orderCode = one.getDIDI_ORDER_CODE();
			one.setDRIVER_PHONE(orderDriverMap.get(orderCode));
			
			one.setORDER_PRICE(new BigDecimal(one.getORDER_PRICE()).divide(new BigDecimal(100)).setScale(2).toString());
			
		}
		
//		List<OrderModel> orderList = new ArrayList<OrderModel>();
//		for (OrderModel one : orderArray) {
//			if(new BigDecimal(one.getORDER_PRICE()).compareTo(new BigDecimal("100"))>0)
//			{
//				orderList.add(one);
//			}
//		}
//		orderArray = orderList.toArray(new OrderModel[]{});
		
		Map<String, Map<String, List<OrderModel>>> map = new LinkedHashMap<String, Map<String,List<OrderModel>>>();
		Map<String, PassengerModel> passengerMap = new HashMap<String, PassengerModel>();
		Map<String, Map<String,DriverModel>> passengerDriverMap = new HashMap<String, Map<String,DriverModel>>();
		for (OrderModel one : orderArray) {
			String passengerFlag = one.getPASSAGE_MOBILE_PHONE();
			if(!map.containsKey(passengerFlag))
			{
				map.put(passengerFlag, new LinkedHashMap<String, List<OrderModel>>());
			}
			String driverFlag = one.getDRIVER_CARD() + "_" + one.getDRIVER_NAME() + "_" + one.getDRIVER_PHONE();
			if(!map.get(passengerFlag).containsKey(driverFlag))
			{
				map.get(passengerFlag).put(driverFlag, new ArrayList<OrderModel>());
			}
			map.get(passengerFlag).get(driverFlag).add(one);
			
			if(!passengerMap.containsKey(passengerFlag))
			{
				passengerMap.put(passengerFlag, new PassengerModel());
			}
			PassengerModel passenger = passengerMap.get(passengerFlag);
			String cityName = one.getSTART_CITY_NAME();
			cityName = CodeUtil.isEmpty(cityName)?"未知":cityName;
			passenger.setCityName(cityName);
			passenger.setCarType(one.getORDER_RULE_NAME());
			passenger.setPassengerName(one.getUSER_NAME());
			passenger.setPassengerPhone(one.getPASSAGE_MOBILE_PHONE());
			
			if(!passengerDriverMap.containsKey(passengerFlag))
			{
				passengerDriverMap.put(passengerFlag, new HashMap<String, DriverModel>());
			}
			if(!passengerDriverMap.get(passengerFlag).containsKey(driverFlag))
			{
				passengerDriverMap.get(passengerFlag).put(driverFlag, new DriverModel());
			}
			DriverModel driver = passengerDriverMap.get(passengerFlag).get(driverFlag);
			driver.setDriverLicence(one.getDRIVER_CARD());
			driver.setDriverName(one.getDRIVER_NAME());
			driver.setDriverPhone(one.getDRIVER_PHONE());
			
		}
		
		List<PassengerModel> passengerList = new ArrayList<PassengerModel>();
		for (Entry<String, Map<String, List<OrderModel>>> passengerEntry : map.entrySet()) {
			
			String passengerFlag = passengerEntry.getKey();
			PassengerModel passenger = passengerMap.get(passengerFlag);
			List<OrderModel> passengerOrderList = new ArrayList<OrderModel>();
			List<DriverModel> driverList = new ArrayList<DriverModel>();
			Map<String, List<OrderModel>> passengerDriverOrderMap = passengerEntry.getValue();
			for (Entry<String, List<OrderModel>> passengerDriverEntry : passengerDriverOrderMap.entrySet()) {
				String driverFlag = passengerDriverEntry.getKey();
				DriverModel driver = passengerDriverMap.get(passengerFlag).get(driverFlag);
				List<OrderModel> passengerDriverOrderList = passengerDriverEntry.getValue();
				passengerDriverOrderList = SortUtil.getSortedList(passengerDriverOrderList, new Comparator<OrderModel>() {

					@Override
					public int compare(OrderModel o1, OrderModel o2) {
						return new BigDecimal(o2.getORDER_PRICE()).compareTo(new BigDecimal(o1.getORDER_PRICE()));
					}
				});
				BigDecimal driverTotalAmount = new BigDecimal("0");
				for (OrderModel passengerDriverOrder : passengerDriverOrderList) {
					driverTotalAmount = driverTotalAmount.add(new BigDecimal(passengerDriverOrder.getORDER_PRICE()));
				}
				driver.setDriverOrderCount(passengerDriverOrderList.size());
				driver.setDriverOrderLargeAmount(new BigDecimal(passengerDriverOrderList.get(0).getORDER_PRICE()));
				driver.setDriverTotalAmount(driverTotalAmount);
				
				driverList.add(driver);
				
				passengerOrderList.addAll(passengerDriverOrderList);
			}
			driverList = SortUtil.getSortedList(driverList, new Comparator<DriverModel>() {

				@Override
				public int compare(DriverModel o1, DriverModel o2) {
//					int citySize = o1.getCityName().compareTo(o2.getCityName());
//					int carTypeSize = o1.getCarType().compareTo(o2.getCarType());
					int driverOrderLargeAmountSize = o2.getDriverOrderLargeAmount().compareTo(o1.getDriverOrderLargeAmount());
					int driverLicenceSize = o1.getDriverLicence().compareTo(o2.getDriverLicence());
					int driverPhoneSize = o1.getDriverPhone().compareTo(o2.getDriverPhone());
//					return citySize!=0?citySize:(carTypeSize!=0?carTypeSize:(driverOrderLargeAmountSize!=0?driverOrderLargeAmountSize:(driverLicenceSize!=0?driverLicenceSize:driverPhoneSize)));
					return driverOrderLargeAmountSize!=0?driverOrderLargeAmountSize:(driverLicenceSize!=0?driverLicenceSize:driverPhoneSize);
				}
			});
			passenger.setDriverList(driverList);
			
			passengerOrderList = SortUtil.getSortedList(passengerOrderList, new Comparator<OrderModel>() {

					@Override
					public int compare(OrderModel o1, OrderModel o2) {
						return new BigDecimal(o2.getORDER_PRICE()).compareTo(new BigDecimal(o1.getORDER_PRICE()));
					}
				});
			BigDecimal passengerTotalAmount = new BigDecimal("0");
			for (OrderModel passengerOrder : passengerOrderList) {
				passengerTotalAmount = passengerTotalAmount.add(new BigDecimal(passengerOrder.getORDER_PRICE()));
			}
			passenger.setPassengerOrderLargeAmount(new BigDecimal(passengerOrderList.get(0).getORDER_PRICE()));
			passenger.setPassengerTotalAmount(passengerTotalAmount);
			passenger.setPasssengerOrderCount(passengerOrderList.size());
			
			passengerList.add(passenger);
		}
		passengerList = SortUtil.getSortedList(passengerList, new Comparator<PassengerModel>() {

			@Override
			public int compare(PassengerModel o1, PassengerModel o2) {
//				int citySize = o1.getCityName().compareTo(o2.getCityName());
				int carTypeSize = o1.getCarType().compareTo(o2.getCarType());
				int passengerOrderLargeAmountSize = o2.getPassengerOrderLargeAmount().compareTo(o1.getPassengerOrderLargeAmount());
				int passengerMobilePhoneSize = o1.getPassengerPhone().compareTo(o2.getPassengerPhone());
				return carTypeSize!=0?carTypeSize:(passengerOrderLargeAmountSize!=0?passengerOrderLargeAmountSize:passengerMobilePhoneSize);
			}
		});
		
		StringBuilder sb = new StringBuilder();
		Integer totalPassengerCount = passengerList.size();
		BigDecimal totalAmount = new BigDecimal("0");
		Integer totalOrderCount = 0;
		String separator1 = tabSeparator;
		String separator2 = "|";
		String title = "车型"+separator1+"会员单笔最大金额"+separator1
				+ "会员手机号"+separator1 + "会员姓名"+separator1
				+ "会员总金额"+separator1 + "会员总订单数"+separator1+"会员总司机数"+separator1
				+ "司机1（单笔最大金额/车牌/手机/名称/总金额/总订单数）" + separator1+ "司机2（单笔最大金额/车牌/手机/名称/总金额/总订单数）" + separator1
				+ "司机3（单笔最大金额/车牌/手机/名称/总金额/总订单数）" + separator1+ "司机n（单笔最大金额/车牌/手机/名称/总金额/总订单数）" + separator1 + lineSeparator;
		for (PassengerModel one : passengerList) {
			totalAmount = totalAmount.add(one.getPassengerTotalAmount());
			totalOrderCount += one.getPasssengerOrderCount();
			
			sb.append(one.getCarType()).append(separator1)
			.append(one.getPassengerOrderLargeAmount()).append(separator1)
			.append(one.getPassengerPhone()).append(separator1).append(one.getPassengerName()).append(separator1)
			.append(one.getPassengerTotalAmount()).append(separator1).append(one.getPasssengerOrderCount()).append(separator1)
			.append(one.getDriverList().size()).append(separator1);
			for (DriverModel d : one.getDriverList()) {
				sb.append(d.getDriverOrderLargeAmount()+separator2+d.getDriverLicence()+separator2+d.getDriverPhone()+separator2+d.getDriverName()+separator2+d.getDriverTotalAmount()+separator2+d.getDriverOrderCount()).append(separator1);
			}
			sb.append(lineSeparator);
		}
		String head = "总会员数:"+totalPassengerCount + separator1 + "总金额:"+totalAmount+"元" + separator1 + "总订单数:"+totalOrderCount + separator1 + lineSeparator;
		LogUtil.i(head+title+sb.toString());
	}
	
	public static void driverPassengerOrder()
	{
		
//						未付款滴滴打车“恶意”司机数据(司机总数/总金额/总订单数)
//		司机所属行政区	司机车型	司机单笔最大金额	司机车牌	司机手机	司机名称	司机总金额	司机总单数	司机总乘客数	乘客1（单笔最大金额/手机/姓名/总金额/总订单数）	乘客2（单笔最大金额/手机/姓名/总金额/总订单数）	乘客3（单笔最大金额/手机/姓名/总金额/总订单数）
		
		String s1 = new String(IOUtil.getByteArrayFromInputStream(DidiDriverPassengerOrder.class.getClassLoader().getResourceAsStream("order_driver.txt")),Charset.forName("UTF-8"));
		OrderDriverModel[] orderDriverArray = JSONUtil.toModel(s1, OrderDriverModel[].class);
		Map<String, String> orderDriverMap = new HashMap<String, String>();
		for (OrderDriverModel one : orderDriverArray) {
			orderDriverMap.put(one.getOrder_id(), one.getDriver_phone());
		}
		String s2 = new String(IOUtil.getByteArrayFromInputStream(DidiDriverPassengerOrder.class.getClassLoader().getResourceAsStream("remainpay.txt")),Charset.forName("UTF-8"));
		OrderModel[] orderArray = JSONUtil.toModel(s2, OrderModel[].class);
		for (OrderModel one : orderArray) {
			String orderCode = one.getDIDI_ORDER_CODE();
			one.setDRIVER_PHONE(orderDriverMap.get(orderCode));
			
			one.setORDER_PRICE(new BigDecimal(one.getORDER_PRICE()).divide(new BigDecimal(100)).setScale(2).toString());
			
		}
		
//		List<OrderModel> orderList = new ArrayList<OrderModel>();
//		for (OrderModel one : orderArray) {
//			if(new BigDecimal(one.getORDER_PRICE()).compareTo(new BigDecimal("100"))>0)
//			{
//				orderList.add(one);
//			}
//		}
//		orderArray = orderList.toArray(new OrderModel[]{});
		
		Map<String, Map<String, List<OrderModel>>> map = new LinkedHashMap<String, Map<String,List<OrderModel>>>();
		Map<String, DriverModel> driverMap = new HashMap<String, DriverModel>();
		Map<String, Map<String, PassengerModel>> driverPassengerMap = new HashMap<String, Map<String,PassengerModel>>();
		for (OrderModel one : orderArray) {
			String driverFlag = one.getDRIVER_CARD() + "_" + one.getDRIVER_NAME() + "_" + one.getDRIVER_PHONE();
			if(!map.containsKey(driverFlag))
			{
				map.put(driverFlag, new LinkedHashMap<String, List<OrderModel>>());
			}
			String passengerFlag = one.getPASSAGE_MOBILE_PHONE();
			if(!map.get(driverFlag).containsKey(passengerFlag))
			{
				map.get(driverFlag).put(passengerFlag, new ArrayList<OrderModel>());
			}
			map.get(driverFlag).get(passengerFlag).add(one);
			
			if(!driverMap.containsKey(driverFlag))
			{
				driverMap.put(driverFlag, new DriverModel());
			}
			DriverModel driver = driverMap.get(driverFlag);
			driver.setCityName(one.getDRIVER_CARD().substring(0,2));
			driver.setCarType(one.getORDER_RULE_NAME());
			driver.setDriverLicence(one.getDRIVER_CARD());
			driver.setDriverName(one.getDRIVER_NAME());
			driver.setDriverPhone(one.getDRIVER_PHONE());
			
			if(!driverPassengerMap.containsKey(driverFlag))
			{
				driverPassengerMap.put(driverFlag, new HashMap<String, PassengerModel>());
			}
			if(!driverPassengerMap.get(driverFlag).containsKey(passengerFlag))
			{
				driverPassengerMap.get(driverFlag).put(passengerFlag, new PassengerModel());
			}
			PassengerModel passenger = driverPassengerMap.get(driverFlag).get(passengerFlag);
			passenger.setPassengerPhone(one.getPASSAGE_MOBILE_PHONE());
			passenger.setPassengerName(one.getUSER_NAME());
			
		}
		
		List<DriverModel> driverList = new ArrayList<DriverModel>();
		for (Entry<String, Map<String, List<OrderModel>>> driverEntry : map.entrySet()) {
			
			String driverFlag = driverEntry.getKey();
			DriverModel driver = driverMap.get(driverFlag);
			List<OrderModel> driverOrderList = new ArrayList<OrderModel>();
			List<PassengerModel> passengerList = new ArrayList<PassengerModel>();
			Map<String, List<OrderModel>> driverPassengerOrderMap = driverEntry.getValue();
			for (Entry<String, List<OrderModel>> driverPassengerEntry : driverPassengerOrderMap.entrySet()) {
				String passengerFlag = driverPassengerEntry.getKey();
				PassengerModel passenger = driverPassengerMap.get(driverFlag).get(passengerFlag);
				List<OrderModel> driverPassengerOrderList = driverPassengerEntry.getValue();
				driverPassengerOrderList = SortUtil.getSortedList(driverPassengerOrderList, new Comparator<OrderModel>() {

					@Override
					public int compare(OrderModel o1, OrderModel o2) {
						return new BigDecimal(o2.getORDER_PRICE()).compareTo(new BigDecimal(o1.getORDER_PRICE()));
					}
				});
				BigDecimal passengerTotalAmount = new BigDecimal("0");
				for (OrderModel driverPassengerOrder : driverPassengerOrderList) {
					passengerTotalAmount = passengerTotalAmount.add(new BigDecimal(driverPassengerOrder.getORDER_PRICE()));
				}
				passenger.setPassengerOrderLargeAmount(new BigDecimal(driverPassengerOrderList.get(0).getORDER_PRICE()));
				passenger.setPassengerTotalAmount(passengerTotalAmount);
				passenger.setPasssengerOrderCount(driverPassengerOrderList.size());
				passengerList.add(passenger);
				
				driverOrderList.addAll(driverPassengerOrderList);
			}
			passengerList = SortUtil.getSortedList(passengerList, new Comparator<PassengerModel>() {

				@Override
				public int compare(PassengerModel o1, PassengerModel o2) {
					return o2.getPassengerOrderLargeAmount().compareTo(o1.getPassengerOrderLargeAmount());
				}
			});
			driver.setPassengerList(passengerList);
			
			driverOrderList = SortUtil.getSortedList(driverOrderList, new Comparator<OrderModel>() {

					@Override
					public int compare(OrderModel o1, OrderModel o2) {
						return new BigDecimal(o2.getORDER_PRICE()).compareTo(new BigDecimal(o1.getORDER_PRICE()));
					}
				});
			BigDecimal driverTotalAmount = new BigDecimal("0");
			for (OrderModel driverOrder : driverOrderList) {
				driverTotalAmount = driverTotalAmount.add(new BigDecimal(driverOrder.getORDER_PRICE()));
			}
			driver.setDriverOrderLargeAmount(new BigDecimal(driverOrderList.get(0).getORDER_PRICE()));
			driver.setDriverTotalAmount(driverTotalAmount);
			driver.setDriverOrderCount(driverOrderList.size());
			
			driverList.add(driver);
		}
		driverList = SortUtil.getSortedList(driverList, new Comparator<DriverModel>() {

			@Override
			public int compare(DriverModel o1, DriverModel o2) {
				int citySize = o1.getCityName().compareTo(o2.getCityName());
				int carTypeSize = o1.getCarType().compareTo(o2.getCarType());
				int driverOrderLargeAmountSize = o2.getDriverOrderLargeAmount().compareTo(o1.getDriverOrderLargeAmount());
				int driverLicenceSize = o1.getDriverLicence().compareTo(o2.getDriverLicence());
				int driverPhoneSize = o1.getDriverPhone().compareTo(o2.getDriverPhone());
				return citySize!=0?citySize:(carTypeSize!=0?carTypeSize:(driverOrderLargeAmountSize!=0?driverOrderLargeAmountSize:(driverLicenceSize!=0?driverLicenceSize:driverPhoneSize)));
			}
		});
		
		StringBuilder sb = new StringBuilder();
		Integer totalDriverCount = driverList.size();
		BigDecimal totalAmount = new BigDecimal("0");
		Integer totalOrderCount = 0;
		String separator1 = tabSeparator;
		String separator2 = "|";
		String title = "司机所属行政区"+separator1+"司机车型"+separator1+"司机单笔最大金额"+separator1
				+ "司机车牌"+separator1 + "司机手机号"+separator1 + "司机名称"+separator1
				+ "司机总金额"+separator1 + "司机总订单数"+separator1+"司机总乘客数"+separator1
				+ "乘客1（单笔最大金额/手机/姓名/总金额/总订单数）" + separator1+ "乘客2（单笔最大金额/手机/姓名/总金额/总订单数）" + separator1
				+ "乘客3（单笔最大金额/手机/姓名/总金额/总订单数）" + separator1+ "乘客n（单笔最大金额/手机/姓名/总金额/总订单数）" + separator1 + lineSeparator;
		for (DriverModel one : driverList) {
			totalAmount = totalAmount.add(one.getDriverTotalAmount());
			totalOrderCount += one.getDriverOrderCount();
			
			sb.append(one.getCityName()).append(separator1).append(one.getCarType()).append(separator1)
			.append(one.getDriverOrderLargeAmount()).append(separator1).append(one.getDriverLicence()).append(separator1)
			.append(one.getDriverPhone()).append(separator1).append(one.getDriverName()).append(separator1)
			.append(one.getDriverTotalAmount()).append(separator1).append(one.getDriverOrderCount()).append(separator1)
			.append(one.getPassengerList().size()).append(separator1);
			for (PassengerModel p : one.getPassengerList()) {
				sb.append(p.getPassengerOrderLargeAmount()+separator2+p.getPassengerPhone()+separator2+p.getPassengerName()+separator2+p.getPassengerTotalAmount()+separator2+p.getPasssengerOrderCount()).append(separator1);
			}
			sb.append(lineSeparator);
		}
		String head = "总司机数:"+totalDriverCount + separator1 + "总金额:"+totalAmount+"元" + separator1 + "总订单数:"+totalOrderCount + separator1 + lineSeparator;
		LogUtil.i(head+title+sb.toString());
	}
	
	
	
	public static class DriverModel
	{
		private String cityName;
		private String carType;
		private BigDecimal driverOrderLargeAmount;
		private BigDecimal driverTotalAmount;
		private Integer	driverOrderCount;
		private String driverLicence;
		private String driverName;
		private String driverPhone;
		private List<PassengerModel> passengerList = new ArrayList<PassengerModel>();
		public String getCityName() {
			return cityName;
		}
		public void setCityName(String cityName) {
			this.cityName = cityName;
		}
		public String getCarType() {
			return carType;
		}
		public void setCarType(String carType) {
			this.carType = carType;
		}
		
		public BigDecimal getDriverOrderLargeAmount() {
			return driverOrderLargeAmount;
		}
		public void setDriverOrderLargeAmount(BigDecimal driverOrderLargeAmount) {
			this.driverOrderLargeAmount = driverOrderLargeAmount;
		}
		public BigDecimal getDriverTotalAmount() {
			return driverTotalAmount;
		}
		public void setDriverTotalAmount(BigDecimal driverTotalAmount) {
			this.driverTotalAmount = driverTotalAmount;
		}
		public Integer getDriverOrderCount() {
			return driverOrderCount;
		}
		public void setDriverOrderCount(Integer driverOrderCount) {
			this.driverOrderCount = driverOrderCount;
		}
		public String getDriverLicence() {
			return driverLicence;
		}
		public void setDriverLicence(String driverLicence) {
			this.driverLicence = driverLicence;
		}
		public String getDriverName() {
			return driverName;
		}
		public void setDriverName(String driverName) {
			this.driverName = driverName;
		}
		public String getDriverPhone() {
			return driverPhone;
		}
		public void setDriverPhone(String driverPhone) {
			this.driverPhone = driverPhone;
		}
		public List<PassengerModel> getPassengerList() {
			return passengerList;
		}
		public void setPassengerList(List<PassengerModel> passengerList) {
			this.passengerList = passengerList;
		}
		
	}
	
	
	public static class PassengerModel
	{
		private String cityName;
		private String carType;
		private BigDecimal passengerOrderLargeAmount;
		private BigDecimal passengerTotalAmount;
		private Integer passsengerOrderCount;
		private String passengerPhone;
		private String passengerName;
		private List<DriverModel> driverList = new ArrayList<DriverModel>();
		public Integer getPasssengerOrderCount() {
			return passsengerOrderCount;
		}
		public void setPasssengerOrderCount(Integer passsengerOrderCount) {
			this.passsengerOrderCount = passsengerOrderCount;
		}
		public String getPassengerPhone() {
			return passengerPhone;
		}
		public void setPassengerPhone(String passengerPhone) {
			this.passengerPhone = passengerPhone;
		}
		public String getPassengerName() {
			return passengerName;
		}
		public void setPassengerName(String passengerName) {
			this.passengerName = passengerName;
		}
		public BigDecimal getPassengerOrderLargeAmount() {
			return passengerOrderLargeAmount;
		}
		public void setPassengerOrderLargeAmount(BigDecimal passengerOrderLargeAmount) {
			this.passengerOrderLargeAmount = passengerOrderLargeAmount;
		}
		public BigDecimal getPassengerTotalAmount() {
			return passengerTotalAmount;
		}
		public void setPassengerTotalAmount(BigDecimal passengerTotalAmount) {
			this.passengerTotalAmount = passengerTotalAmount;
		}
		public String getCityName() {
			return cityName;
		}
		public void setCityName(String cityName) {
			this.cityName = cityName;
		}
		public String getCarType() {
			return carType;
		}
		public void setCarType(String carType) {
			this.carType = carType;
		}
		public List<DriverModel> getDriverList() {
			return driverList;
		}
		public void setDriverList(List<DriverModel> driverList) {
			this.driverList = driverList;
		}
		
	}
	
	public static class OrderDriverModel
	{
		private String driver_phone;
		private String order_id;
		public String getDriver_phone() {
			return driver_phone;
		}
		public void setDriver_phone(String driver_phone) {
			this.driver_phone = driver_phone;
		}
		public String getOrder_id() {
			return order_id;
		}
		public void setOrder_id(String order_id) {
			this.order_id = order_id;
		}
		
	}
	
	public static class OrderModel
	{
		private String CREATE_TIME;
		private String DIDI_ORDER_CODE;
		private String DRIVER_AVATAR;
		private String DRIVER_CARD;
		private String DRIVER_NAME;
		private String DRIVER_PHONE;
		private String END_ADDRESS;
		private String FINISH_TIME;
		private String MOBILE_PHONE;
		private String ORDER_PRICE;
		private String ORDER_RULE_NAME;
		private String PASSAGE_MOBILE_PHONE;
		private String PRICE_DETAIL;
		private String START_ADDRESS;
		private String START_CITY_NAME;
		private String USER_NAME;
		public String getCREATE_TIME() {
			return CREATE_TIME;
		}
		public void setCREATE_TIME(String cREATE_TIME) {
			CREATE_TIME = cREATE_TIME;
		}
		public String getDIDI_ORDER_CODE() {
			return DIDI_ORDER_CODE;
		}
		public void setDIDI_ORDER_CODE(String dIDI_ORDER_CODE) {
			DIDI_ORDER_CODE = dIDI_ORDER_CODE;
		}
		public String getDRIVER_AVATAR() {
			return DRIVER_AVATAR;
		}
		public void setDRIVER_AVATAR(String dRIVER_AVATAR) {
			DRIVER_AVATAR = dRIVER_AVATAR;
		}
		public String getDRIVER_CARD() {
			return DRIVER_CARD;
		}
		public void setDRIVER_CARD(String dRIVER_CARD) {
			DRIVER_CARD = dRIVER_CARD;
		}
		public String getDRIVER_NAME() {
			return DRIVER_NAME;
		}
		public void setDRIVER_NAME(String dRIVER_NAME) {
			DRIVER_NAME = dRIVER_NAME;
		}
		public String getDRIVER_PHONE() {
			return DRIVER_PHONE;
		}
		public void setDRIVER_PHONE(String dRIVER_PHONE) {
			DRIVER_PHONE = dRIVER_PHONE;
		}
		public String getEND_ADDRESS() {
			return END_ADDRESS;
		}
		public void setEND_ADDRESS(String eND_ADDRESS) {
			END_ADDRESS = eND_ADDRESS;
		}
		public String getFINISH_TIME() {
			return FINISH_TIME;
		}
		public void setFINISH_TIME(String fINISH_TIME) {
			FINISH_TIME = fINISH_TIME;
		}
		public String getMOBILE_PHONE() {
			return MOBILE_PHONE;
		}
		public void setMOBILE_PHONE(String mOBILE_PHONE) {
			MOBILE_PHONE = mOBILE_PHONE;
		}
		public String getORDER_PRICE() {
			return ORDER_PRICE;
		}
		public void setORDER_PRICE(String oRDER_PRICE) {
			ORDER_PRICE = oRDER_PRICE;
		}
		public String getORDER_RULE_NAME() {
			return ORDER_RULE_NAME;
		}
		public void setORDER_RULE_NAME(String oRDER_RULE_NAME) {
			ORDER_RULE_NAME = oRDER_RULE_NAME;
		}
		public String getPASSAGE_MOBILE_PHONE() {
			return PASSAGE_MOBILE_PHONE;
		}
		public void setPASSAGE_MOBILE_PHONE(String pASSAGE_MOBILE_PHONE) {
			PASSAGE_MOBILE_PHONE = pASSAGE_MOBILE_PHONE;
		}
		public String getPRICE_DETAIL() {
			return PRICE_DETAIL;
		}
		public void setPRICE_DETAIL(String pRICE_DETAIL) {
			PRICE_DETAIL = pRICE_DETAIL;
		}
		public String getSTART_ADDRESS() {
			return START_ADDRESS;
		}
		public void setSTART_ADDRESS(String sTART_ADDRESS) {
			START_ADDRESS = sTART_ADDRESS;
		}
		public String getSTART_CITY_NAME() {
			return START_CITY_NAME;
		}
		public void setSTART_CITY_NAME(String sTART_CITY_NAME) {
			START_CITY_NAME = sTART_CITY_NAME;
		}
		public String getUSER_NAME() {
			return USER_NAME;
		}
		public void setUSER_NAME(String uSER_NAME) {
			USER_NAME = uSER_NAME;
		}
	}
	
}
