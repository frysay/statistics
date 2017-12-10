package com.statistics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.statistics.body.response.StatisticsResponse;
import com.statistics.cache.StatisticsCache;

@Service
public class StatisticsService {
	
	private StatisticsCache cache;
	
	@Autowired
	public StatisticsService(StatisticsCache cache) {
		this.cache = cache;
	}

	public StatisticsResponse processRequest() {
		return new StatisticsResponse(cache.getStatistics());
	}
}
