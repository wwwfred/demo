package net.wwwfred.framework.demo.model;

import java.util.Date;

import net.wwwfred.framework.core.dao.ColumnAnnotaion;
import net.wwwfred.framework.core.dao.IdAnnotaion;
import net.wwwfred.framework.core.dao.TableAnnotaion;
import net.wwwfred.framework.model.BaseModel;

import org.springframework.stereotype.Component;

@Component
@TableAnnotaion(value="T_MEMBER",seqTableName="SEQ_T_MEMBER")
public class MemberModel extends BaseModel{
	private static final long serialVersionUID = 1L;
	@IdAnnotaion("USER_ID")
	private Long userId;
	@ColumnAnnotaion("USER_NAME")
	private String userName;
	@ColumnAnnotaion("USER_TYPE")
	private String userType;
	@ColumnAnnotaion("MEMBER_CARD_NO")
	private String memberCardNo;
	@ColumnAnnotaion("EMAIL")
	private String email;
	@ColumnAnnotaion("PASSWORD")
	private String password;
	@ColumnAnnotaion("USER_LEVEL")
	private String userLevel;
	@ColumnAnnotaion("REAL_NAME")
	private String realName;
	@ColumnAnnotaion("NICK_NAME")
	private String nickName;
	@ColumnAnnotaion("SEX")
	private String sex;
	@ColumnAnnotaion("BIRTHDAY")
	private String birthday;
	@ColumnAnnotaion("CARD_TYPE")
	private String cardType;
	@ColumnAnnotaion("ID_CARD")
	private String idCard;
	@ColumnAnnotaion("PHONE_TEL")
	private String phoneTel;
	@ColumnAnnotaion("PHONE_MOB")
	private String mobilePhone;
	@ColumnAnnotaion("REG_TIME")
	private Date regTime;
	@ColumnAnnotaion("USER_PIC")
	private String userPic;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getPhoneTel() {
		return phoneTel;
	}
	public void setPhoneTel(String phoneTel) {
		this.phoneTel = phoneTel;
	}
	
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	public Date getRegTime() {
		return regTime;
	}
	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}
	public String getUserPic() {
		return userPic;
	}
	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}

}
