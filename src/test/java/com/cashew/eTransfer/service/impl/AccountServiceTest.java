package com.cashew.eTransfer.service.impl;


import com.cashew.eTransfer.dto.AccountsDto;
import com.cashew.eTransfer.exception.AccountNotExistException;
import com.cashew.eTransfer.exception.OverDraftException;
import com.cashew.eTransfer.exception.SystemException;
import com.cashew.eTransfer.model.Account;
import com.cashew.eTransfer.dto.TransferRequest;
import com.cashew.eTransfer.repository.AccountsRepository;
import com.cashew.eTransfer.service.AccountsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccountServiceTest  {

	@Mock
    AccountsRepository accountsRepository;
	
	@InjectMocks
    AccountsServiceImpl accountsService;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this) ;
	}

	@Test
	public void testRetrieveAccountById() throws SystemException {
		when(accountsRepository.findByAccountId(1L)).thenReturn(Optional.of(new Account(1L, BigDecimal.ONE)));
		
		Assert.assertEquals(Optional.of(new Account(1L, BigDecimal.ONE)), accountsRepository.findByAccountId(1L));
	}
	
	@Test(expected = AccountNotExistException.class)
	public void testTransferBalanceFromInvalidAccount() throws SystemException {

		Long accountFromId = null;
		Long accountFromTo = 2L;
		BigDecimal amount = new BigDecimal(10);

		TransferRequest request = new TransferRequest();
		request.setAccountFromId(accountFromId);
		request.setAccountToId(accountFromTo);
		request.setAmount(amount);

		accountsService.transferMoney(request);
	}

	@Test(expected = AccountNotExistException.class)
	public void testTransferBalanceToInvalidAccount() throws SystemException {

		Long accountFromId = 1L;
		Long accountFromTo = null;
		BigDecimal amount = new BigDecimal(10);

		TransferRequest request = new TransferRequest();
		request.setAccountFromId(accountFromId);
		request.setAccountToId(accountFromTo);
		request.setAmount(amount);

		accountsService.transferMoney(request);
	}
	
	@Test
	public void testTransferMoney() throws Exception, Exception, Exception {
		Long accountFromId = 1L;
		Long accountFromTo = 2L;
		BigDecimal amount = new BigDecimal(10);
		
		TransferRequest request = new TransferRequest();
		request.setAccountFromId(accountFromId);
		request.setAccountToId(accountFromTo);
		request.setAmount(amount);
		
		Account accFrom = new Account(accountFromId, BigDecimal.TEN);
		Account accTo = new Account(accountFromId, BigDecimal.TEN);
		
		when(accountsRepository.getAccountForUpdate(accountFromId)).thenReturn(Optional.of(accFrom));
		when(accountsRepository.getAccountForUpdate(accountFromTo)).thenReturn(Optional.of(accTo));

		accountsService.transferMoney(request);
		
		assertEquals(BigDecimal.ZERO, accFrom.getBalance());
		assertEquals(BigDecimal.TEN.add(BigDecimal.TEN), accTo.getBalance());
	}
	
	@Test(expected = OverDraftException.class)
	public void testOverdraftBalance() throws OverDraftException, AccountNotExistException, SystemException {
		Long accountFromId = 1L;
		Long accountFromTo = 2L;
		BigDecimal amount = new BigDecimal(20);
		
		TransferRequest request = new TransferRequest();
		request.setAccountFromId(accountFromId);
		request.setAccountToId(accountFromTo);
		request.setAmount(amount);
		
		Account accFrom = new Account(accountFromId, BigDecimal.TEN);
		Account accTo = new Account(accountFromId, BigDecimal.TEN);
		
		when(accountsRepository.getAccountForUpdate(accountFromId)).thenReturn(Optional.of(accFrom));
		when(accountsRepository.getAccountForUpdate(accountFromTo)).thenReturn(Optional.of(accTo));
		
		accountsService.transferMoney(request);
	}


}
