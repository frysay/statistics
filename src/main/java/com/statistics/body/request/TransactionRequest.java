package com.statistics.body.request;

import lombok.Data;

@Data
public class TransactionRequest {

	private double amount;

	private long timestamp;	
}
