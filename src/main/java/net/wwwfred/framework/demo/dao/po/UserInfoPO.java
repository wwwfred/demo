package net.wwwfred.framework.demo.dao.po;

import java.util.List;

import net.wwwfred.framework.demo.model.MemberModel;
import net.wwwfred.framework.demo.model.UserCashAccountBalanceModel;
import net.wwwfred.framework.demo.model.UserCashAccountModel;
import net.wwwfred.framework.po.BasePO;

public class UserInfoPO extends BasePO{
	private static final long serialVersionUID = 1L;

	private MemberModel member;
	
	private UserCashAccountModel userCashAccount;
	
	private List<UserCashAccountBalanceModel> userCashAccountBalanceList;

	public MemberModel getMember() {
		return member;
	}

	public void setMember(MemberModel member) {
		this.member = member;
	}

	public UserCashAccountModel getUserCashAccount() {
		return userCashAccount;
	}

	public void setUserCashAccount(UserCashAccountModel userCashAccount) {
		this.userCashAccount = userCashAccount;
	}

	public List<UserCashAccountBalanceModel> getUserCashAccountBalanceList() {
		return userCashAccountBalanceList;
	}

	public void setUserCashAccountBalanceList(
			List<UserCashAccountBalanceModel> userCashAccountBalanceList) {
		this.userCashAccountBalanceList = userCashAccountBalanceList;
	}
	
}
