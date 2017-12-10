package com.statistics.cache;

import org.springframework.context.ApplicationEvent;

public class StatisticsExpirationEvent extends ApplicationEvent {

	private static final long serialVersionUID = -8642783187025183959L;

	public StatisticsExpirationEvent(Object source) {
		super(source);
	}
	
}
