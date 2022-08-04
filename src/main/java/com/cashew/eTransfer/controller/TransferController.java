package com.cashew.eTransfer.controller;

import javax.validation.Valid;

import com.cashew.eTransfer.dto.TransferResult;
import com.cashew.eTransfer.exception.OverDraftException;
import com.cashew.eTransfer.dto.TransferRequest;
import com.cashew.eTransfer.exception.SystemException;
import com.cashew.eTransfer.service.AccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cashew.eTransfer.exception.AccountNotExistException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;

@RestController
@RequestMapping("transfer")
@Api(tags = {"Transfer Controller"}, description = "Provide APIs for transfer transaction ")
public class TransferController {
	
	private static final Logger log = LoggerFactory.getLogger(TransferController.class);
	
	@Autowired
	private AccountsService accountService;

	@PostMapping(consumes = { "application/json" })
	@ApiOperation(value = "API to transfer money between from/to accounts", response = TransferResult.class, produces = "application/json")
	public ResponseEntity transferMoney(@RequestBody @Valid TransferRequest request) throws SystemException {

		try {
			BigDecimal balanceAfterTransfer  = accountService.transferMoney(request);
			
			TransferResult result = new TransferResult();
			result.setAccountFromId(request.getAccountFromId());
			result.setBalanceAfterTransfer(balanceAfterTransfer);
			
			return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
		} catch (AccountNotExistException | OverDraftException exception) {
			log.error("Fail to transfer balances, please check with system administrator.");
			throw exception;
		} catch ( SystemException systemException) {
			log.error("Transaction Failed, please check with system administrator.");
			throw systemException;
		}
	}
}
