package com.cashew.eTransfer.dto;

import java.math.BigDecimal;

public class AccountsDto {
    private Long accountId;
    private String accountHolderName;
    private BigDecimal balance;
    private String accountNo;

    public AccountsDto(Long accountId, String accountHolderName, BigDecimal balance, String accountNo)
    {
            this.accountId = accountId;
            this.accountHolderName = accountHolderName;
            this.balance = balance;
            this.accountNo = accountNo;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}


