package com.cashew.eTransfer.controller;

import com.cashew.eTransfer.Utils.TestUtils;
import com.cashew.eTransfer.dto.AccountsDto;
import com.cashew.eTransfer.dto.TransferRequest;
import com.cashew.eTransfer.dto.TransferResult;
import com.cashew.eTransfer.service.impl.AccountsServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;


import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TransferControllerTest {

    @InjectMocks
    private TransferController controller;


    private MockMvc mockMvc;

    @Mock
    private AccountsServiceImpl accountService;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this) ;
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private static final ObjectMapper om = new ObjectMapper();


    @Test
    @DisplayName("POST /transfer ")
    public void whenTransferMoneyWithValidRequest() throws Exception
    {

        //create mock data
        TransferRequest request = new TransferRequest();
        request.setAccountFromId(2L);
        request.setAccountToId(1L);
        request.setAmount(new BigDecimal(10));

        TransferResult transferResponse = new TransferResult();
        transferResponse.setAccountFromId(2L);
        transferResponse.setBalanceAfterTransfer(new BigDecimal(10));

        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectToJson(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.accountFromId").exists())
                .andExpect(jsonPath("$.accountFromId", is(transferResponse.getAccountFromId().intValue())));
    }

    @Test
    @DisplayName("POST /transfer ")
    public void whenTransferMoneyWithBadRequest() throws Exception
    {

        //create mock data
        TransferRequest request = new TransferRequest();
        request.setAccountFromId(0L);
        request.setAccountToId(1L);
        request.setAmount(new BigDecimal(10));

        System.out.println("Request ============"+TestUtils.convertObjectToJson(request));

        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectToJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /transfer ")
    public void whenTransferMoneyWithNegativeAmount() throws Exception
    {
        //create mock data
        TransferRequest request = new TransferRequest();
        request.setAccountFromId(2L);
        request.setAccountToId(1L);
        request.setAmount(new BigDecimal(-10));

        TransferResult transferResponse = new TransferResult();
        transferResponse.setAccountFromId(2L);
        transferResponse.setBalanceAfterTransfer(new BigDecimal(10));

        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectToJson(request)))
                .andExpect(status().isBadRequest());
    }
}

