package com.statistics.integration.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.statistics.body.request.TransactionRequest;
import com.statistics.cache.StatisticsCache;
import com.statistics.integration.util.IntegrationTestConfig;
import com.statistics.service.StatisticsService;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class StatisticsControllerTest extends IntegrationTestConfig {
	
	@SpyBean
	StatisticsService service;
	
	@SpyBean
	StatisticsCache cache;
	
	@Test
	public void getStatistics_2validTransaction_200() throws Exception {
		long transactionTime1 = System.currentTimeMillis();			
		String transactionId1 = "transactionId1";
		double amount1 = 5.00;
		
		reportTransaction(transactionId1, amount1, transactionTime1);
		
		long transactionTime2 = transactionTime1 - 10000;			
		String transactionId2 = "transactionId2";
		double amount2 = 7.00;
		
		reportTransaction(transactionId2, amount2, transactionTime2);

		mockMvc.perform(get("/statistics").accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.sum").value(12.0))
		.andExpect(jsonPath("$.avg").value(6.0))
		.andExpect(jsonPath("$.min").value(5.0))
		.andExpect(jsonPath("$.max").value(7.0))
		.andExpect(jsonPath("$.count").value(2));
			
		verify(service).processRequest();
		verify(cache).getStatistics();
	}
	
	@Test
	public void getStatistics_2validTransaction1expired_200() throws Exception {
		long transactionTime1 = System.currentTimeMillis();			
		String transactionId1 = "transactionId1";
		double amount1 = 5.00;
		
		reportTransaction(transactionId1, amount1, transactionTime1);
		
		long transactionTime2 = transactionTime1 - 58000;			
		String transactionId2 = "transactionId2";
		double amount2 = 7.00;
		
		reportTransaction(transactionId2, amount2, transactionTime2);
		
		long transactionTime3 = transactionTime1 - 20000;			
		String transactionId3 = "transactionId3";
		double amount3 = 9.00;
		
		reportTransaction(transactionId3, amount3, transactionTime3);
		
		Thread.sleep(3000);

		mockMvc.perform(get("/statistics").accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.sum").value(14.0))
		.andExpect(jsonPath("$.avg").value(7.0))
		.andExpect(jsonPath("$.min").value(5.0))
		.andExpect(jsonPath("$.max").value(9.0))
		.andExpect(jsonPath("$.count").value(2));
			
		verify(service).processRequest();
		verify(cache).getStatistics();
	}
	
	@Test
	public void getStatistics_allTransactionExpired_200() throws Exception {
		long transactionTime1 = System.currentTimeMillis() - 58000;			
		String transactionId1 = "transactionId1";
		double amount1 = 5.00;
		
		reportTransaction(transactionId1, amount1, transactionTime1);
		
		long transactionTime2 = transactionTime1;			
		String transactionId2 = "transactionId2";
		double amount2 = 7.00;
		
		reportTransaction(transactionId2, amount2, transactionTime2);
				
		Thread.sleep(3000);

		mockMvc.perform(get("/statistics").accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.sum").value(0.0))
		.andExpect(jsonPath("$.avg").value(0.0))
		.andExpect(jsonPath("$.min").value("Infinity"))
		.andExpect(jsonPath("$.max").value("-Infinity"))
		.andExpect(jsonPath("$.count").value(0));
			
		verify(service).processRequest();
		verify(cache).getStatistics();
	}
	
	@Test
	public void getStatistics_noTransactionReportedYet_200() throws Exception {

		mockMvc.perform(get("/statistics").accept(MediaType.APPLICATION_JSON_VALUE))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.sum").value(0.0))
		.andExpect(jsonPath("$.avg").value(0.0))
		.andExpect(jsonPath("$.min").value("Infinity"))
		.andExpect(jsonPath("$.max").value("-Infinity"))
		.andExpect(jsonPath("$.count").value(0));
			
		verify(service).processRequest();
		verify(cache).getStatistics();
	}
	
	private void reportTransaction(String transactionId, double amount, long currentTimeMillis) throws Exception {
		TransactionRequest transactionRequest = createTransaction( amount, currentTimeMillis);
		String json = toJson(transactionRequest);
		mockMvc.perform(post("/transactions/" + transactionId).contentType(MediaType.APPLICATION_JSON).content(json));
	}
	
	private TransactionRequest createTransaction(double amount, long timestamp) {
		TransactionRequest transaction = new TransactionRequest();
		transaction.setAmount(amount);
		transaction.setTimestamp(timestamp);
		return transaction;
	}
}
