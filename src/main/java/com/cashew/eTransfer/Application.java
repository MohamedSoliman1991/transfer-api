package com.cashew.eTransfer;

import com.cashew.eTransfer.exception.SystemException;
import com.cashew.eTransfer.model.Account;
import com.cashew.eTransfer.service.AccountsService;
import com.cashew.eTransfer.service.impl.AccountsServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Bean
	CommandLineRunner runner(@Autowired AccountsService accountsService) {
		return args -> {
			// read json and write to db
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Account>> typeReference = new TypeReference<List<Account>>(){};
			InputStream inputStream = TypeReference.class.getResourceAsStream("/accounts.json");
			try {
				List<Account> accountsList = mapper.readValue(inputStream,typeReference);
				accountsService.saveAccounts(accountsList);
				System.out.println("Data Saved!");
			} catch (SystemException | IOException e){
				log.error("Failing inserting accounts into DB.");
				System.out.println("Unable to save Data: " + e.getMessage());
			}
		};
	}
}
