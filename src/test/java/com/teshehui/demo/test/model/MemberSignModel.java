package com.teshehui.demo.test.model;

import java.util.Date;

import net.wwwfred.framework.core.dao.ColumnAnnotaion;
import net.wwwfred.framework.core.dao.IdAnnotaion;
import net.wwwfred.framework.core.dao.TableAnnotaion;
import net.wwwfred.framework.model.BaseModel;

import org.springframework.stereotype.Component;

//MEMBER_SIGN_ID	NUMBER	12	0	0	-1			BYTE		
//USER_ID	NUMBER	12	0	0	0		关联会员ID	BYTE		
//CURRENT_SIGN_NUM	NUMBER	12	0	-1	0		当前连续签到次数	BYTE		
//MAX_SIGN_NUM	NUMBER	12	0	-1	0		最大连续签到次数	BYTE		
//MIN_SIGN_NUM	NUMBER	12	0	-1	0		最小连续签到次数	BYTE		
//LAST_SIGN_TIME	DATE	7	0	-1	0		最后签到时间	BYTE		

@TableAnnotaion("T_MEMBER_SIGN")
@Component
public class MemberSignModel extends BaseModel{

	private static final long serialVersionUID = 7605983289629884755L;

	@IdAnnotaion("MEMBER_SIGN_ID")
	private Long id;
	
	@ColumnAnnotaion("USER_ID")
	private Long userId;
	private Long realCurrentSignNum;
	@ColumnAnnotaion("CURRENT_SIGN_NUM")
	private Long currentSignNum;
	@ColumnAnnotaion("MAX_SIGN_NUM")
	private Long maxSignNum;
	@ColumnAnnotaion("MIN_SIGN_NUM")
	private Long minSignNum;
	@ColumnAnnotaion("LAST_SIGN_TIME")
	private Date lastSignTime;
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
	public Long getCurrentSignNum() {
		return currentSignNum;
	}
	public void setCurrentSignNum(Long currentSignNum) {
		this.currentSignNum = currentSignNum;
	}
	public Long getMaxSignNum() {
		return maxSignNum;
	}
	public void setMaxSignNum(Long maxSignNum) {
		this.maxSignNum = maxSignNum;
	}
	public Long getMinSignNum() {
		return minSignNum;
	}
	public void setMinSignNum(Long minSignNum) {
		this.minSignNum = minSignNum;
	}
	public Date getLastSignTime() {
		return lastSignTime;
	}
	public void setLastSignTime(Date lastSignTime) {
		this.lastSignTime = lastSignTime;
	}
	public Long getRealCurrentSignNum() {
		return realCurrentSignNum;
	}
	public void setRealCurrentSignNum(Long realCurrentSignNum) {
		this.realCurrentSignNum = realCurrentSignNum;
	}
	
}
