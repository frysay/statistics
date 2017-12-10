package com.statistics.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GenericRestExceptionHandler {

	private static Logger log = Logger.getLogger(GenericRestExceptionHandler.class);
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, RuntimeException.class})
	public void handleValidationException(Exception e) {
		log.error("Invalid request error: " + e.getMessage(), e);
	}

	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public void handleException(Exception e) {
		log.error("Generic error: " + e.getMessage(), e);
	}
}
