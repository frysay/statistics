package com.statistics.validator;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.statistics.body.request.TransactionRequest;

@Aspect
@Component
public class TransactionRequestValidatorAspect {

	private static Logger log = Logger.getLogger(TransactionRequestValidatorAspect.class);

	@Around(value = "execution(* com.statistics.controller.TransactionController..*(..)) && args(request,..))")
	public ResponseEntity<Void> validateTransaction(ProceedingJoinPoint joinPoint, TransactionRequest request) throws Throwable {
		if(System.currentTimeMillis() <= request.getTimestamp() + 60000) {
			joinPoint.proceed();
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} else {
			log.debug("The following request has been discarded because outdate: " + request);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
	}
}
