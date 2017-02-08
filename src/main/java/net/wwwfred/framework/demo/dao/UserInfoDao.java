package net.wwwfred.framework.demo.dao;

import java.util.List;
import java.util.Map;

import net.wwwfred.framework.core.dao.mybatis.MybatisDao;
import net.wwwfred.framework.demo.dao.po.UserInfoPO;
import net.wwwfred.framework.demo.model.MemberModel;

public interface UserInfoDao extends MybatisDao{
	
	MemberModel getMember(MemberModel member);
	
	Map<String,Long> getUserCashAccount(List<String> mobilePhone);
	
	UserInfoPO queryUserInfo(MemberModel member);
	
	void updateUserNameByMobilePhone(MemberModel member);
	
	void deleteUserByMobilePhone(String mobilePhone);

	List<MemberModel> queryMemberByName(MemberModel member, Integer pageNo,
			Integer pageSize);

}
