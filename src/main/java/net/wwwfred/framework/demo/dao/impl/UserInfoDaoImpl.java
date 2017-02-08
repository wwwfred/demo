package net.wwwfred.framework.demo.dao.impl;

import java.util.List;
import java.util.Map;

import net.wwwfred.framework.core.dao.mybatis.impl.MybatisDaoImpl;
import net.wwwfred.framework.demo.dao.UserInfoDao;
import net.wwwfred.framework.demo.dao.po.UserInfoPO;
import net.wwwfred.framework.demo.model.MemberModel;

import org.springframework.stereotype.Repository;

@Repository("userInfoDao")
public class UserInfoDaoImpl extends MybatisDaoImpl implements UserInfoDao{
	
	@Override
	protected String getSqlSessionTemplateName() {
//		return super.getSqlSessionTemplateName();
		return "mybatisSessionTemplate";
	}
	
	@Override
	public MemberModel getMember(MemberModel member) {
		return selectOne("getMember", member);
	}
	
	@Override
	public Map<String, Long> getUserCashAccount(
			List<String> mobilePhoneList) {
		return selectOne("getUserCashAccount", mobilePhoneList);
	}
	
	@Override
	public UserInfoPO queryUserInfo(MemberModel member) {
		return selectOne("queryUserInfo", member);
	}
	
	@Override
	public List<MemberModel> queryMemberByName(MemberModel member,
			Integer pageNo, Integer pageSize) {
		return selectList("queryMemberByName", member, pageNo, pageSize);
	}
	
	@Override
	public void updateUserNameByMobilePhone(MemberModel member) {
		update("updateUserNameByMobilePhone", member);
	}
	
	@Override
	public void deleteUserByMobilePhone(String mobilePhone) {
		delete("deleteUserByMobilePhone", mobilePhone);
	}
	
}
