package com.statistics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.statistics.body.response.StatisticsResponse;
import com.statistics.service.StatisticsService;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {
	
	private StatisticsService service;
	
	@Autowired
	public StatisticsController(StatisticsService service) {
		this.service = service;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody StatisticsResponse getStatistics() {

		return service.processRequest();
	}
}
