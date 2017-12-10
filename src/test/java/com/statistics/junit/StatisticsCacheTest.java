package com.statistics.junit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.DoubleSummaryStatistics;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.statistics.body.request.TransactionRequest;
import com.statistics.cache.StatisticsCache;
import com.statistics.cache.StatisticsExpirationListener;

@RunWith(SpringRunner.class)
public class StatisticsCacheTest {

	private StatisticsCache cache;

	@MockBean
	StatisticsExpirationListener expirationListener;

	@Before
	public void init() {
		cache = new StatisticsCache(expirationListener);
	}

	@Test
	public void addTransactionToExpiringMap_singleElement_success() {
		String transactionId = "transactionId";
		TransactionRequest transaction = createTransaction(5.0);
		cache.addTransactionToExpiringMap(transactionId, transaction);

		DoubleSummaryStatistics statistics = cache.getStatistics();
		assertTrue(5.0 == statistics.getSum());
		assertTrue(5.0 == statistics.getAverage());
		assertTrue(5.0 == statistics.getMax());
		assertTrue(5.0 == statistics.getMin());
		assertTrue(1 == statistics.getCount());
	}

	@Test
	public void addTransactionToExpiringMap_doubleElements_success() {
		String transactionId1 = "transactionId1";
		TransactionRequest transaction1 = createTransaction(5.0);

		cache.addTransactionToExpiringMap(transactionId1, transaction1);

		String transactionId2 = "transactionId2";
		TransactionRequest transaction2 = createTransaction(0.0);

		cache.addTransactionToExpiringMap(transactionId2, transaction2);

		DoubleSummaryStatistics statistics = cache.getStatistics();
		assertTrue(5.0 == statistics.getSum());
		assertTrue(2.5 == statistics.getAverage());
		assertTrue(5.0 == statistics.getMax());
		assertTrue(0.0 == statistics.getMin());
		assertTrue(2 == statistics.getCount());
	}

	@Test
	public void addTransactionToExpiringMap_duplicatedElements_success() {
		String transactionId = "transactionId";
		TransactionRequest transaction1 = createTransaction(5.0);

		cache.addTransactionToExpiringMap(transactionId, transaction1);

		TransactionRequest transaction2 = createTransaction(2.0);

		cache.addTransactionToExpiringMap(transactionId, transaction2);

		DoubleSummaryStatistics statistics = cache.getStatistics();
		assertTrue(2.0 == statistics.getSum());
		assertTrue(2.0 == statistics.getAverage());
		assertTrue(2.0 == statistics.getMax());
		assertTrue(2.0 == statistics.getMin());
		assertTrue(1 == statistics.getCount());
	}

	@Test(expected = NullPointerException.class)
	public void addTransactionToExpiringMap_transactionIdNull_exception() {
		TransactionRequest transaction = createTransaction(5.0);
		cache.addTransactionToExpiringMap(null, transaction);

		fail("It shouldn't get here");
	}
	
	@Test(expected = NullPointerException.class)
	public void addTransactionToExpiringMap_transactionNull_exception() {
		String transactionId = "transactionId";
		cache.addTransactionToExpiringMap(transactionId, null);

		fail("It shouldn't get here");
	}

	private TransactionRequest createTransaction(double amount) {
		TransactionRequest transaction = new TransactionRequest();
		transaction.setAmount(amount);
		transaction.setTimestamp(System.currentTimeMillis());
		return transaction;
	}

}
