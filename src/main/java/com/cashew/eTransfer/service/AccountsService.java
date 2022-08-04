package com.cashew.eTransfer.service;

import java.math.BigDecimal;
import java.util.List;

import com.cashew.eTransfer.dto.AccountsDto;
import com.cashew.eTransfer.exception.OverDraftException;
import com.cashew.eTransfer.exception.SystemException;
import com.cashew.eTransfer.model.Account;
import com.cashew.eTransfer.dto.TransferRequest;

import com.cashew.eTransfer.exception.AccountNotExistException;


public interface AccountsService {

    public void saveAccounts(List<Account> accountList) throws SystemException;

	public List<AccountsDto> retrieveAllAccounts() throws SystemException;

	public BigDecimal transferMoney(TransferRequest transfer) throws OverDraftException, AccountNotExistException, SystemException;

}
