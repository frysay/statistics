package com.statistics.body.response;

import java.util.DoubleSummaryStatistics;

import lombok.Data;

@Data
public class StatisticsResponse {
	private double sum;
	private double avg;
	private double max;
	private double min;
	private long count;
	
	public StatisticsResponse(DoubleSummaryStatistics statistics) {
		this.sum = statistics.getSum();
		this.avg = statistics.getAverage();
		this.max = statistics.getMax();
		this.min = statistics.getMin();
		this.count = statistics.getCount();
	}
}
