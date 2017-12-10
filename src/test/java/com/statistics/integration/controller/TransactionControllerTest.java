package com.statistics.integration.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.statistics.body.request.TransactionRequest;
import com.statistics.cache.StatisticsCache;
import com.statistics.integration.util.IntegrationTestConfig;
import com.statistics.service.TransactionService;

public class TransactionControllerTest extends IntegrationTestConfig {
	
	@SpyBean
	TransactionService service;
	
	@SpyBean
	StatisticsCache cache;
	
	@Test
	public void withTransactionIdTest_validRequest_201() throws Exception {
		long currentTimeMillis = System.currentTimeMillis();				
		String transactionId = "transactionId";
		TransactionRequest transactionRequest = createTransaction(currentTimeMillis - 50000);
		String json = toJson(transactionRequest);

		MvcResult result = mockMvc.perform(post("/transactions/" + transactionId).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isCreated()).andReturn();
	
		assertEquals("", result.getResponse().getContentAsString());
		
		verify(service).processRequest(eq(transactionId), eq(transactionRequest));
		verify(cache).addTransactionToExpiringMap(eq(transactionId), eq(transactionRequest));
	}
	
	@Test
	public void withTransactionIdTest_outdateRequest_204() throws Exception {
		long currentTimeMillis = System.currentTimeMillis();				
		String transactionId = "transactionId";
		TransactionRequest transactionRequest = createTransaction(currentTimeMillis - 70000);
		String json = toJson(transactionRequest);

		MvcResult result = mockMvc.perform(post("/transactions/" + transactionId).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isNoContent()).andReturn();
	
		assertEquals("", result.getResponse().getContentAsString());
		
		verifyZeroInteractions(service);
		verifyZeroInteractions(cache);
	}
	
	@Test
	public void withTransactionIdTest_wrongJsonFormat_400() throws Exception {
		String transactionId = "transactionId";
		String json = "wrong json format";

		MvcResult result = mockMvc.perform(post("/transactions/" + transactionId).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isBadRequest()).andReturn();
	
		assertEquals("", result.getResponse().getContentAsString());
		
		verifyZeroInteractions(service);
		verifyZeroInteractions(cache);
	}
	
	private TransactionRequest createTransaction(long timestamp) {
		TransactionRequest transaction = new TransactionRequest();
		transaction.setAmount(5.00);
		transaction.setTimestamp(timestamp);
		return transaction;
	}
}
