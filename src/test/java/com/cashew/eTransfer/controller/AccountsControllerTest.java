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
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.Is.isA;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccountsControllerTest {

    @InjectMocks
    private AccountsController controller;


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
        public void testRetrieveAllAccounts() throws Exception {
            Map<Long, AccountsDto> data = getTestData();
            List<AccountsDto> expectedRecords = new ArrayList<>();

            Collections.sort(expectedRecords, Comparator.comparing(AccountsDto::getAccountId));

            List<AccountsDto> actualRecords = om.readValue(mockMvc.perform(get("/accounts"))
                    .andDo(print())
                    .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                    .andExpect(jsonPath("$.*", hasSize(expectedRecords.size())))
                    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<AccountsDto>>() {
            });

            for (int i = 0; i < expectedRecords.size(); i++) {
                Assert.assertTrue(new ReflectionEquals(expectedRecords.get(i)).matches(actualRecords.get(i)));
            }
        }
        private Map<Long, AccountsDto> getTestData() throws ParseException {
            Map<Long, AccountsDto> data = new LinkedHashMap<>();

            AccountsDto acc1 = new AccountsDto(1L
                    ,"Trupe"
                    ,new BigDecimal(20.15),
                    "17f904c1-806f-4252-9103-74e7a5d3e340");

            data.put(1L, acc1);

            AccountsDto acc2 = new AccountsDto(2L
                    ,"Trupe"
                    ,new BigDecimal(77.10),
                    "fd796d75-1bcf-4a95-bf1a-f7b296adb79f");
            data.put(2L, acc2);

            AccountsDto acc3 = new AccountsDto(3L
                    ,"Trupe"
                    ,new BigDecimal(100),
                    "3d253e29-8785-464f-8fa0-9e4b57699db9");
            data.put(3L, acc3);
            return data;
        }
    }


