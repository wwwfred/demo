package net.wwwfred.framework.demo.model;

import net.wwwfred.framework.core.dao.ColumnAnnotaion;
import net.wwwfred.framework.core.dao.TableAnnotaion;
import net.wwwfred.framework.model.BaseModel;

import org.springframework.stereotype.Component;

@Component
@TableAnnotaion(value="T_CASH_ACCOUNT",seqTableName="SEQ_T_CASH_ACCOUNT")
public class UserCashAccountModel extends BaseModel{

	private static final long serialVersionUID = 1L;

	@ColumnAnnotaion("USER_ID")
	private Long userId;
	@ColumnAnnotaion("USER_NAME")
	private String userName;
	@ColumnAnnotaion("PAY_PASSWORD")
	private String payPassword;
	@ColumnAnnotaion("USER_TYPE")
	private String userType;
	@ColumnAnnotaion("MEMBER_CARD_NO")
	private String memberCardNo;
	@ColumnAnnotaion("MOBILE")
	private String mobilePhone;
	@ColumnAnnotaion("EMAIL")
	private String email;
	@ColumnAnnotaion("CURRENCY_TYPE_CODE")
	private String currencyType;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPayPassword() {
		return payPassword;
	}
	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getMemberCardNo() {
		return memberCardNo;
	}
	public void setMemberCardNo(String memberCardNo) {
		this.memberCardNo = memberCardNo;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	
}
