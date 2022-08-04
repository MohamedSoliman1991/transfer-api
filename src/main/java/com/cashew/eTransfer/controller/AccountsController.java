package com.cashew.eTransfer.controller;

import com.cashew.eTransfer.Application;
import com.cashew.eTransfer.constant.ErrorCode;
import com.cashew.eTransfer.dto.AccountsDto;
import com.cashew.eTransfer.exception.SystemException;
import com.cashew.eTransfer.model.Account;
import com.cashew.eTransfer.service.AccountsService;
import net.bytebuddy.implementation.bytecode.Throw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("accounts")
@Api(tags = { "Accounts Controller" }, description = "Provide API to retrieve all available accounts")
public class AccountsController {

	private static final Logger log = LoggerFactory.getLogger(AccountsController.class);

	@Autowired
	private AccountsService accountService;

	@GetMapping
	@ApiOperation(value = "Get all available accounts info with id,name and balance ", response = Account.class, produces = "application/json")
	@ApiResponses({})
	public List<AccountsDto> getAvailableAccounts() throws SystemException {
      try {
	           return accountService.retrieveAllAccounts();
	  } catch ( SystemException systemException) {
			log.error("Transaction Failed, please check with system administrator.");
			throw new SystemException("General Error. Please try again later", ErrorCode.SYSTEM_ERROR);
		}
	}
}
