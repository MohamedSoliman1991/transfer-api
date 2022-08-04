package com.cashew.eTransfer.service.impl;

import com.cashew.eTransfer.constant.ErrorCode;
import com.cashew.eTransfer.dto.AccountsDto;
import com.cashew.eTransfer.exception.AccountNotExistException;
import com.cashew.eTransfer.exception.OverDraftException;
import com.cashew.eTransfer.exception.SystemException;
import com.cashew.eTransfer.model.Account;
import com.cashew.eTransfer.dto.TransferRequest;
import com.cashew.eTransfer.repository.AccountsRepository;
import com.cashew.eTransfer.service.AccountsService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountsServiceImpl implements AccountsService {

	private static final Logger log = LoggerFactory.getLogger(AccountsServiceImpl.class);
			
	@Autowired
	private AccountsRepository accountsRepository;

	@Override
	public void saveAccounts(List<Account> accountList) {
      accountsRepository.saveAll(accountList);
	}

	@Override
	public List<AccountsDto> retrieveAllAccounts() throws SystemException {
		List<AccountsDto> dotList = accountsRepository.findAll()
				.stream()
				.map(account -> new ModelMapper().map(account, AccountsDto.class))
				.collect(Collectors.toList());
		return dotList;

	}

	@Override
	@Transactional
	public BigDecimal transferMoney(TransferRequest transfer) throws OverDraftException, AccountNotExistException {

			Account accountFrom = accountsRepository.getAccountForUpdate(transfer.getAccountFromId())
					.orElseThrow(() -> new AccountNotExistException("Account with id:" + transfer.getAccountFromId() + " does not exist.", ErrorCode.ACCOUNT_ERROR));

			Account accountTo = accountsRepository.getAccountForUpdate(transfer.getAccountToId())
					.orElseThrow(() -> new AccountNotExistException("Account with id:" + transfer.getAccountFromId() + " does not exist.", ErrorCode.ACCOUNT_ERROR));

			if (accountFrom.getBalance().compareTo(transfer.getAmount()) < 0) {
				throw new OverDraftException("Account with id:" + accountFrom.getAccountId() + " does not have enough balance to transfer.", ErrorCode.ACCOUNT_ERROR);
			}

			accountFrom.setBalance(accountFrom.getBalance().subtract(transfer.getAmount()));
			accountTo.setBalance(accountTo.getBalance().add(transfer.getAmount()));
			return accountFrom.getBalance();
	}
}
