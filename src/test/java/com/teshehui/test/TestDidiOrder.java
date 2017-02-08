//package com.teshehui.test;
//
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.teshehui.util.io.IOUtil;
//import com.teshehui.util.json.JSONUtil;
//import com.teshehui.util.log.LogUtil;
//import com.teshehui.util.sort.SortUtil;
//
//public class TestDidiOrder {
//
//	public static void main(String[] args) {
//		
////		driverPhoneQuerySql();
//		
//		driverIllegalOrder();
//	}
//	
//	public static void driverPhoneQuerySql()
//	{
//		String s = new String(IOUtil.getByteArrayFromInputStream(TestDidiOrder.class.getClassLoader().getResourceAsStream("remainpay.txt")),Charset.forName("UTF-8"));
//		OrderModel[] orderArray = JSONUtil.toModel(s, OrderModel[].class);
//		StringBuilder sb = new StringBuilder("select o.order_id,o.driver_phone from t_didi_orders o where o.order_id in(");
//		for (OrderModel one : orderArray) {
//			String orderCode = one.getDIDI_ORDER_CODE();
//			sb.append("'").append(orderCode).append("'").append(",");
//		}
//		int index = sb.lastIndexOf(",");
//		sb.delete(index, index+",".length());
//		sb.append(");");
//		System.out.println(sb.toString());
//	}
//	
//	public static void driverIllegalOrder()
//	{
//		String s1 = new String(IOUtil.getByteArrayFromInputStream(TestDidiOrder.class.getClassLoader().getResourceAsStream("order_driver.txt")),Charset.forName("UTF-8"));
//		OrderDriverModel[] orderDriverArray = JSONUtil.toModel(s1, OrderDriverModel[].class);
//		Map<String, String> orderDriverMap = new HashMap<String, String>();
//		for (OrderDriverModel one : orderDriverArray) {
//			orderDriverMap.put(one.getOrder_id(), one.getDriver_phone());
//		}
//		String s2 = new String(IOUtil.getByteArrayFromInputStream(TestDidiOrder.class.getClassLoader().getResourceAsStream("remainpay.txt")),Charset.forName("UTF-8"));
//		OrderModel[] orderArray = JSONUtil.toModel(s2, OrderModel[].class);
//		for (OrderModel one : orderArray) {
//			String orderCode = one.getDIDI_ORDER_CODE();
//			one.setDRIVER_PHONE(orderDriverMap.get(orderCode));
//			
//			one.setORDER_PRICE(new Double(one.getORDER_PRICE()).doubleValue()/100+"");
//			
//		}
////		Map<String, OrderModel> orderMap = new HashMap<String, TestDidiOrder.OrderModel>();
////		for (OrderModel one : orderArray) {
////			orderMap.put(one.getDIDI_ORDER_CODE(), one);
////		}
//		
//		LogUtil.i(JSONUtil.toString(orderArray));
//		
//		List<OrderModel> illegalOrderArray = new ArrayList<OrderModel>();
//		for (OrderModel one : orderArray) {
//			if(Double.parseDouble(one.getORDER_PRICE())>100.0)
//			{
//				illegalOrderArray.add(one);
//			}
//		}
//		orderArray = illegalOrderArray.toArray(new OrderModel[]{});
//		
//		LogUtil.i(JSONUtil.toString(orderArray));
//		
//		Map<String, Map<String, Map<String, List<OrderModel>>>> map = new LinkedHashMap<String, Map<String,Map<String,List<OrderModel>>>>();
//		for (OrderModel one : orderArray) {
//			String cityName = one.getDRIVER_CARD().substring(0,2);
//			if(!map.containsKey(cityName))
//			{
//				map.put(cityName, new LinkedHashMap<String, Map<String,List<OrderModel>>>());
//			}
//			String carType = one.getORDER_RULE_NAME();
//			if(!map.get(cityName).containsKey(carType))
//			{
//				map.get(cityName).put(carType, new LinkedHashMap<String, List<OrderModel>>());
//			}
//			String driverPhone = one.getDRIVER_CARD()+one.getDRIVER_PHONE();
//			if(!map.get(cityName).get(carType).containsKey(driverPhone))
//			{
//				map.get(cityName).get(carType).put(driverPhone, new ArrayList<OrderModel>());
//			}
//			map.get(cityName).get(carType).get(driverPhone).add(one);
//		}
//		
//		Map<String, Map<String,List<OrderModel>>> map2 = new LinkedHashMap<String, Map<String,List<OrderModel>>>();
//		for (OrderModel one : orderArray) {
//			String driverPhone = one.getDRIVER_CARD()+one.getDRIVER_PHONE();
//			if(!map2.containsKey(driverPhone))
//			{
//				map2.put(driverPhone, new LinkedHashMap<String, List<OrderModel>>());
//			}
//			String passengerPhone = one.getPASSAGE_MOBILE_PHONE();
//			if(!map2.get(driverPhone).containsKey(passengerPhone))
//			{
//				map2.get(driverPhone).put(passengerPhone, new ArrayList<OrderModel>());
//			}
//			map2.get(driverPhone).get(passengerPhone).add(one);
//		}
//		
//		List<CityModel> list = new ArrayList<CityModel>();
//		for (String cityName : map.keySet()) {
//			CityModel city = new CityModel();
//			city.setCityName(cityName);
//			List<CarTypeModel> carTypeList = new ArrayList<CarTypeModel>();
//			for (String carTypeName : map.get(cityName).keySet()) {
//				CarTypeModel carType = new CarTypeModel();
//				carType.setCarType(carTypeName);
//				List<DriverOrderModel> orderList = new ArrayList<DriverOrderModel>();
//				for (String driverPhone : map.get(cityName).get(carTypeName).keySet()) {
//					DriverOrderModel driver = new DriverOrderModel();
//					driver.setDriverLicencePhone(driverPhone);
//					List<OrderModel> driverOrderList = map.get(cityName).get(carTypeName).get(driverPhone);
//					SortUtil.getSortedList(driverOrderList, new Comparator<OrderModel>() {
//
//						@Override
//						public int compare(OrderModel o1, OrderModel o2) {
//							return new Double(Double.parseDouble(o1.getORDER_PRICE())-Double.parseDouble(o2.getORDER_PRICE())).intValue();
//						}
//					});
//					OrderModel oneDiriverOrder = driverOrderList.get(driverOrderList.size()-1);
//					driver.setDriverPhone(oneDiriverOrder.getDRIVER_PHONE());
//					driver.setDriverName(oneDiriverOrder.getDRIVER_NAME());
//					driver.setDriverLicence(oneDiriverOrder.getDRIVER_CARD());
//					driver.setDriverOrderCount(driverOrderList.size()+"");
//					driver.setDriverOrderLargeAmount(oneDiriverOrder.getORDER_PRICE());
//					Double driverTotalAmount = 0.0;
//					for (OrderModel one : driverOrderList) {
//						driverTotalAmount += Double.parseDouble(one.getORDER_PRICE());
//					}
//					driver.setDriverTotalAmount(driverTotalAmount+"");
//					
//					List<PassengerOrderModel> passengerList = new ArrayList<PassengerOrderModel>();
//					Map<String, List<OrderModel>> driverPassengerMap = map2.get(driverPhone);
//					for (String passengerPhone : driverPassengerMap.keySet()) {
//						PassengerOrderModel passenger = new PassengerOrderModel();
//						passenger.setPassengerPhone(passengerPhone);
//						List<OrderModel> passengerOrderList = driverPassengerMap.get(passengerPhone);
//						Double passengerTotalAmount = 0.0;
//						for (OrderModel one : passengerOrderList) {
//							passengerTotalAmount += Double.parseDouble(one.getORDER_PRICE());
//						}
//						passenger.setPassengerName(passengerOrderList.get(0).getUSER_NAME());
//						passenger.setPassengerOrderCount(passengerOrderList.size()+"");
//						passenger.setPassengerTotalAmount(passengerTotalAmount+"");
//						passengerList.add(passenger);
//					}
//					driver.setPassengerArray(passengerList);
//					
//					orderList.add(driver);
//				}
//				carType.setDriverArray(orderList);
//				carTypeList.add(carType);
//			}
//			city.setCarTypeArray(carTypeList);
//			list.add(city);
//		}
//		LogUtil.i(JSONUtil.toString(list));
//	}
//	
//	public static class OrderDriverModel
//	{
//		private String driver_phone;
//		private String order_id;
//		public String getDriver_phone() {
//			return driver_phone;
//		}
//		public void setDriver_phone(String driver_phone) {
//			this.driver_phone = driver_phone;
//		}
//		public String getOrder_id() {
//			return order_id;
//		}
//		public void setOrder_id(String order_id) {
//			this.order_id = order_id;
//		}
//		
//	}
//	
//	public static class OrderModel
//	{
//		private String CREATE_TIME;
//		private String DIDI_ORDER_CODE;
//		private String DRIVER_AVATAR;
//		private String DRIVER_CARD;
//		private String DRIVER_NAME;
//		private String DRIVER_PHONE;
//		private String END_ADDRESS;
//		private String FINISH_TIME;
//		private String MOBILE_PHONE;
//		private String ORDER_PRICE;
//		private String ORDER_RULE_NAME;
//		private String PASSAGE_MOBILE_PHONE;
//		private String PRICE_DETAIL;
//		private String START_ADDRESS;
//		private String START_CITY_NAME;
//		private String USER_NAME;
//		public String getCREATE_TIME() {
//			return CREATE_TIME;
//		}
//		public void setCREATE_TIME(String cREATE_TIME) {
//			CREATE_TIME = cREATE_TIME;
//		}
//		public String getDIDI_ORDER_CODE() {
//			return DIDI_ORDER_CODE;
//		}
//		public void setDIDI_ORDER_CODE(String dIDI_ORDER_CODE) {
//			DIDI_ORDER_CODE = dIDI_ORDER_CODE;
//		}
//		public String getDRIVER_AVATAR() {
//			return DRIVER_AVATAR;
//		}
//		public void setDRIVER_AVATAR(String dRIVER_AVATAR) {
//			DRIVER_AVATAR = dRIVER_AVATAR;
//		}
//		public String getDRIVER_CARD() {
//			return DRIVER_CARD;
//		}
//		public void setDRIVER_CARD(String dRIVER_CARD) {
//			DRIVER_CARD = dRIVER_CARD;
//		}
//		public String getDRIVER_NAME() {
//			return DRIVER_NAME;
//		}
//		public void setDRIVER_NAME(String dRIVER_NAME) {
//			DRIVER_NAME = dRIVER_NAME;
//		}
//		public String getDRIVER_PHONE() {
//			return DRIVER_PHONE;
//		}
//		public void setDRIVER_PHONE(String dRIVER_PHONE) {
//			DRIVER_PHONE = dRIVER_PHONE;
//		}
//		public String getEND_ADDRESS() {
//			return END_ADDRESS;
//		}
//		public void setEND_ADDRESS(String eND_ADDRESS) {
//			END_ADDRESS = eND_ADDRESS;
//		}
//		public String getFINISH_TIME() {
//			return FINISH_TIME;
//		}
//		public void setFINISH_TIME(String fINISH_TIME) {
//			FINISH_TIME = fINISH_TIME;
//		}
//		public String getMOBILE_PHONE() {
//			return MOBILE_PHONE;
//		}
//		public void setMOBILE_PHONE(String mOBILE_PHONE) {
//			MOBILE_PHONE = mOBILE_PHONE;
//		}
//		public String getORDER_PRICE() {
//			return ORDER_PRICE;
//		}
//		public void setORDER_PRICE(String oRDER_PRICE) {
//			ORDER_PRICE = oRDER_PRICE;
//		}
//		public String getORDER_RULE_NAME() {
//			return ORDER_RULE_NAME;
//		}
//		public void setORDER_RULE_NAME(String oRDER_RULE_NAME) {
//			ORDER_RULE_NAME = oRDER_RULE_NAME;
//		}
//		public String getPASSAGE_MOBILE_PHONE() {
//			return PASSAGE_MOBILE_PHONE;
//		}
//		public void setPASSAGE_MOBILE_PHONE(String pASSAGE_MOBILE_PHONE) {
//			PASSAGE_MOBILE_PHONE = pASSAGE_MOBILE_PHONE;
//		}
//		public String getPRICE_DETAIL() {
//			return PRICE_DETAIL;
//		}
//		public void setPRICE_DETAIL(String pRICE_DETAIL) {
//			PRICE_DETAIL = pRICE_DETAIL;
//		}
//		public String getSTART_ADDRESS() {
//			return START_ADDRESS;
//		}
//		public void setSTART_ADDRESS(String sTART_ADDRESS) {
//			START_ADDRESS = sTART_ADDRESS;
//		}
//		public String getSTART_CITY_NAME() {
//			return START_CITY_NAME;
//		}
//		public void setSTART_CITY_NAME(String sTART_CITY_NAME) {
//			START_CITY_NAME = sTART_CITY_NAME;
//		}
//		public String getUSER_NAME() {
//			return USER_NAME;
//		}
//		public void setUSER_NAME(String uSER_NAME) {
//			USER_NAME = uSER_NAME;
//		}
//	}
//	
//	public static class CityModel
//	{
//		private String cityName;
//		private List<CarTypeModel> carTypeArray = new ArrayList<CarTypeModel>();
//		public String getCityName() {
//			return cityName;
//		}
//		public void setCityName(String cityName) {
//			this.cityName = cityName;
//		}
//		public List<CarTypeModel> getCarTypeArray() {
//			return carTypeArray;
//		}
//		public void setCarTypeArray(List<CarTypeModel> carTypeArray) {
//			this.carTypeArray = carTypeArray;
//		}
//		
//	}
//	
//	public static class CarTypeModel
//	{
//		private String carType;
//		private List<DriverOrderModel> driverArray = new ArrayList<DriverOrderModel>();
//		public String getCarType() {
//			return carType;
//		}
//		public void setCarType(String carType) {
//			this.carType = carType;
//		}
//		public List<DriverOrderModel> getDriverArray() {
//			return driverArray;
//		}
//		public void setDriverArray(List<DriverOrderModel> driverArray) {
//			this.driverArray = driverArray;
//		}
//		
//	}
//	
//	public static class DriverOrderModel
//	{
//		private String driverName;
//		private String driverPhone;
//		private String driverLicence;
//		private String driverLicencePhone;
//		
//		private String driverOrderLargeAmount;
//		private String driverOrderCount;
//		private String driverTotalAmount;
//		
//		private List<PassengerOrderModel> passengerArray = new ArrayList<PassengerOrderModel>();
//
//		public String getDriverName() {
//			return driverName;
//		}
//
//		public void setDriverName(String driverName) {
//			this.driverName = driverName;
//		}
//
//		public String getDriverPhone() {
//			return driverPhone;
//		}
//
//		public void setDriverPhone(String driverPhone) {
//			this.driverPhone = driverPhone;
//		}
//
//		public String getDriverLicence() {
//			return driverLicence;
//		}
//
//		public void setDriverLicence(String driverLicence) {
//			this.driverLicence = driverLicence;
//		}
//
//		public String getDriverOrderLargeAmount() {
//			return driverOrderLargeAmount;
//		}
//
//		public void setDriverOrderLargeAmount(String driverOrderLargeAmount) {
//			this.driverOrderLargeAmount = driverOrderLargeAmount;
//		}
//
//		public String getDriverOrderCount() {
//			return driverOrderCount;
//		}
//
//		public void setDriverOrderCount(String driverOrderCount) {
//			this.driverOrderCount = driverOrderCount;
//		}
//
//		public String getDriverTotalAmount() {
//			return driverTotalAmount;
//		}
//
//		public void setDriverTotalAmount(String driverTotalAmount) {
//			this.driverTotalAmount = driverTotalAmount;
//		}
//
//		public List<PassengerOrderModel> getPassengerArray() {
//			return passengerArray;
//		}
//
//		public void setPassengerArray(List<PassengerOrderModel> passengerArray) {
//			this.passengerArray = passengerArray;
//		}
//
//		public String getDriverLicencePhone() {
//			return driverLicencePhone;
//		}
//
//		public void setDriverLicencePhone(String driverLicencePhone) {
//			this.driverLicencePhone = driverLicencePhone;
//		}
//
//	}
//	
//	public static class PassengerOrderModel
//	{
//		private String passengerName;
//		private String passengerPhone;
//		private String passengerOrderCount;
//		private String passengerTotalAmount;
//		public String getPassengerName() {
//			return passengerName;
//		}
//		public void setPassengerName(String passengerName) {
//			this.passengerName = passengerName;
//		}
//		public String getPassengerPhone() {
//			return passengerPhone;
//		}
//		public void setPassengerPhone(String passengerPhone) {
//			this.passengerPhone = passengerPhone;
//		}
//		public String getPassengerTotalAmount() {
//			return passengerTotalAmount;
//		}
//		public void setPassengerTotalAmount(String passengerTotalAmount) {
//			this.passengerTotalAmount = passengerTotalAmount;
//		}
//		public String getPassengerOrderCount() {
//			return passengerOrderCount;
//		}
//		public void setPassengerOrderCount(String passengerOrderCount) {
//			this.passengerOrderCount = passengerOrderCount;
//		}
//		
//	}
//	
//}
