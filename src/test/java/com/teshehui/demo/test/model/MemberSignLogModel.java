package com.teshehui.demo.test.model;

import java.util.Date;

import net.wwwfred.framework.core.dao.ColumnAnnotaion;
import net.wwwfred.framework.core.dao.IdAnnotaion;
import net.wwwfred.framework.core.dao.TableAnnotaion;
import net.wwwfred.framework.model.BaseModel;

import org.springframework.stereotype.Component;

//MEMBER_SIGN_LOG_ID	NUMBER	12	0	0	-1			BYTE		
//USER_ID	NUMBER	12	0	0	0		关联会员	BYTE		
//USER_NAME	VARCHAR2	50	0	-1	0		关联用户名	BYTE		
//REAL_NAME	VARCHAR2	50	0	-1	0		用户真实姓名	BYTE		
//PHONE_MOB	VARCHAR2	50	0	-1	0		手机号码	BYTE		
//IMEI_ID	VARCHAR2	50	0	-1	0			BYTE		
//SIGN_TIME	DATE	7	0	-1	0		签到时间	BYTE		
//OBTAIN_CASH	NUMBER	12	0	-1	0		获得现金卷	BYTE		
//CURRENT_CASH	NUMBER	12	0	-1	0		连续签到获得现金卷	BYTE		
//CURRENT_SIGN_NUM	NUMBER	12	0	-1	0		连续签到天数	BYTE		

@TableAnnotaion("T_MEMBER_SIGN_LOG")
@Component
public class MemberSignLogModel extends BaseModel{

	private static final long serialVersionUID = -4293858501781344989L;

	@IdAnnotaion("MEMBER_SIGN_LOG_ID")
	private Long id;
	
	@ColumnAnnotaion("USER_ID")
	private Long userId;
	@ColumnAnnotaion("USER_NAME")
	private String userName;
	@ColumnAnnotaion("REAL_NAME")
	private String realName;
	@ColumnAnnotaion("PHONE_MOB")
	private String phoneMob;
	@ColumnAnnotaion("IMEI_ID")
	private String imeiId;
	@ColumnAnnotaion("SIGN_TIME")
	private Date signTime;
	@ColumnAnnotaion("OBTAIN_CASH")
	private Long obtainCash;
	@ColumnAnnotaion("CURRENT_CASH")
	private Long currentCash;
	@ColumnAnnotaion("CURRENT_SIGN_NUM")
	private Long currentSignNum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhoneMob() {
		return phoneMob;
	}

	public void setPhoneMob(String phoneMob) {
		this.phoneMob = phoneMob;
	}

	public String getImeiId() {
		return imeiId;
	}

	public void setImeiId(String imeiId) {
		this.imeiId = imeiId;
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public Long getObtainCash() {
		return obtainCash;
	}

	public void setObtainCash(Long obtainCash) {
		this.obtainCash = obtainCash;
	}

	public Long getCurrentCash() {
		return currentCash;
	}

	public void setCurrentCash(Long currentCash) {
		this.currentCash = currentCash;
	}

	public Long getCurrentSignNum() {
		return currentSignNum;
	}

	public void setCurrentSignNum(Long currentSignNum) {
		this.currentSignNum = currentSignNum;
	}
	
	
}
