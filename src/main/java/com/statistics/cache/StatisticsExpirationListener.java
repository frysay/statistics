package com.statistics.cache;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.statistics.body.request.TransactionRequest;

import net.jodah.expiringmap.ExpirationListener;

@Component
public class StatisticsExpirationListener implements ExpirationListener<String, TransactionRequest> {
	
	private static Logger log = Logger.getLogger(StatisticsExpirationListener.class);
		
	private ApplicationEventPublisher eventPublisher;
	
	@Autowired
	public StatisticsExpirationListener(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void expired(String key, TransactionRequest value) {
		log.debug("Expired: " + key + " - " + value);
		ApplicationEvent event = new StatisticsExpirationEvent("update-required");
		this.eventPublisher.publishEvent(event);
	}
}
