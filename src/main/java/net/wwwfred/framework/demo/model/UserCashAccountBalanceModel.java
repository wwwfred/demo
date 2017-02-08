package net.wwwfred.framework.demo.model;

import net.wwwfred.framework.core.dao.ColumnAnnotaion;
import net.wwwfred.framework.core.dao.TableAnnotaion;
import net.wwwfred.framework.model.BaseModel;

import org.springframework.stereotype.Component;

@Component
@TableAnnotaion(value="T_CASH_BALANCE",seqTableName="SEQ_T_CASH_BALANCE")
public class UserCashAccountBalanceModel extends BaseModel{

	private static final long serialVersionUID = 1L;

	@ColumnAnnotaion("CURRENCY_TYPE_CODE")
	private String currencyTypeCode;
	@ColumnAnnotaion("BALANCE")
	private Long balance;
	@ColumnAnnotaion("CASH_ACCOUNT_ID")
	private Long cashAccountId;
	@ColumnAnnotaion("USER_ID")
	private Long userId;
	public String getCurrencyTypeCode() {
		return currencyTypeCode;
	}
	public void setCurrencyTypeCode(String currencyTypeCode) {
		this.currencyTypeCode = currencyTypeCode;
	}
	public Long getBalance() {
		return balance;
	}
	public void setBalance(Long balance) {
		this.balance = balance;
	}
	public Long getCashAccountId() {
		return cashAccountId;
	}
	public void setCashAccountId(Long cashAccountId) {
		this.cashAccountId = cashAccountId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
