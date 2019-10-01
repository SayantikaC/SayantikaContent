package com.cg.ibs.common.bean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public class AccountBean {
	private BigInteger accountNumber;
	private Set<CustomerBean> accountHolders;
	private BigDecimal currentBalance;
	private String transactionPassword;
	private List<TransactionBean> transactions;

	public AccountBean() {
		super();
	}

	public AccountBean(BigInteger accountNumber, Set<CustomerBean> accountHolders, BigDecimal currentBalance,
			String transactionPassword, List<TransactionBean> transactions) {
		super();
		this.accountNumber = accountNumber;
		this.accountHolders = accountHolders;
		this.currentBalance = currentBalance;
		this.transactionPassword = transactionPassword;
		this.transactions = transactions;
	}

	public BigInteger getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(BigInteger accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Set<CustomerBean> getAccountHolders() {
		return accountHolders;
	}

	public void setAccountHolders(Set<CustomerBean> accountHolders) {
		this.accountHolders = accountHolders;
	}

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getTransactionPassword() {
		return transactionPassword;
	}

	public void setTransactionPassword(String transactionPassword) {
		this.transactionPassword = transactionPassword;
	}

	public List<TransactionBean> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<TransactionBean> transactions) {
		this.transactions = transactions;
	}

}
