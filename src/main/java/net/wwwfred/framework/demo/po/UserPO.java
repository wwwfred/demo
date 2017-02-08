package net.wwwfred.framework.demo.po;

import net.wwwfred.framework.po.BasePO;

public class UserPO extends BasePO{

	private static final long serialVersionUID = 1L;
	private String name;
	private String mobilePhone;
	private String userHeadUrl;
	private Integer age;
	private String sex;
	private String birthday;
	private String balance;
	private Long userId;
	private Long userCashAccountId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getUserCashAccountId() {
		return userCashAccountId;
	}
	public void setUserCashAccountId(Long userCashAccountId) {
		this.userCashAccountId = userCashAccountId;
	}
	public String getUserHeadUrl() {
		return userHeadUrl;
	}
	public void setUserHeadUrl(String userHeadUrl) {
		this.userHeadUrl = userHeadUrl;
	}
	
}
