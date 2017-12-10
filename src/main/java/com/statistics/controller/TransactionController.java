package com.statistics.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.statistics.body.request.TransactionRequest;
import com.statistics.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

	private TransactionService service;

	@Autowired
	public TransactionController(TransactionService service) {
		this.service = service;
	}

	@RequestMapping(value = "/{transaction_id}", method = RequestMethod.POST)
	public ResponseEntity<Void> withTransactionId(@RequestBody @Valid TransactionRequest request,
			@PathVariable("transaction_id") String transactionId) {

		service.processRequest(transactionId, request);
		
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> withoutTransactionId(@RequestBody @Valid TransactionRequest request) {

		service.processRequest(request);
		
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	
}
