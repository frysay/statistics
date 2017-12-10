package com.statistics.cache;

import java.util.DoubleSummaryStatistics;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.statistics.body.request.TransactionRequest;

import lombok.Getter;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

@Component
public class StatisticsCache {
	
	private static Logger log = Logger.getLogger(StatisticsCache.class);
	
	private ExpiringMap<String, TransactionRequest> expiringMap;
	
	@Getter
	private DoubleSummaryStatistics statistics;
	
	@Autowired
	public StatisticsCache(StatisticsExpirationListener expirationListener) {
		expiringMap = ExpiringMap.builder().variableExpiration().expirationListener(expirationListener).build();
		statistics = new DoubleSummaryStatistics();
	}
	
	public void addTransactionToExpiringMap(String transactionId, TransactionRequest transaction) {
		long expirationTime = calculateExpirationTime(transaction.getTimestamp());
		
		log.debug("TransactionId: " + transactionId + " request: " + transaction + " expirationTime: " + expirationTime);
		
		expiringMap.put(transactionId, transaction, ExpirationPolicy.CREATED, expirationTime, TimeUnit.MILLISECONDS);
		updateStatisticResponse();
	}
	
	@EventListener(classes = StatisticsExpirationEvent.class)
	private void updateStatisticResponse() {
		this.statistics = expiringMap.values().stream().mapToDouble((trn) -> trn.getAmount()).summaryStatistics();
	}
	
	private long calculateExpirationTime(long transactionTime) {
		return  transactionTime + 60000 - System.currentTimeMillis();
	}
}
