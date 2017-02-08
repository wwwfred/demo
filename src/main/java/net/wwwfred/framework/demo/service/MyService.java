package net.wwwfred.framework.demo.service;

import java.util.List;
import java.util.Map;

import net.wwwfred.framework.demo.dao.po.UserInfoPO;
import net.wwwfred.framework.demo.model.MemberModel;
import net.wwwfred.framework.demo.model.UserCashAccountModel;
import net.wwwfred.framework.demo.po.UserPO;

public interface MyService {

	 String sayHello(String name);  
	  
	 List<UserPO> getDataFromCache();  
	 
	 void createUser(String userName,String birthday, String sex, String mobilePhone, String balance);
	 
	 MemberModel getUser(String mobilePhone);
	 
	 Map<MemberModel, UserCashAccountModel> getUserCashAccount(List<String> mobilePhoneList);
	 
	 UserInfoPO queryUserInfo(String mobilePhone);
	 
	 List<MemberModel> queryMemberByName(String name, Integer pageNo, Integer pageSize);
	 
	 void updateUserNameByMobilePhone(String userName,String mobilePhone);
	 
	 void deleteUserByMobilePhone(String mobilePhone);
	 
}
