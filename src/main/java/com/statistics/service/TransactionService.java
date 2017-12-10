package com.statistics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.statistics.body.request.TransactionRequest;
import com.statistics.cache.StatisticsCache;

@Service
public class TransactionService {
	
	private StatisticsCache cache;
	
	@Autowired
	public TransactionService(StatisticsCache cache) {
		this.cache = cache;
	}

	public void processRequest(String transactionId, TransactionRequest transaction) {
		cache.addTransactionToExpiringMap(transactionId, transaction);
	}
	
	public void processRequest(TransactionRequest transaction) {
		cache.addTransactionToExpiringMap(String.valueOf(System.currentTimeMillis()), transaction);
	}
}
