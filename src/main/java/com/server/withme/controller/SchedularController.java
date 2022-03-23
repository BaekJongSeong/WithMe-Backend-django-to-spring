package com.server.withme.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * Controller for Schedular API
 * method must be void return type.
 * method must not have parameter.
 *
 * @author Jongseong Baek
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SchedularController {
	
	
	
	@GetMapping("/schedular/")
	@Scheduled(cron = "0 15 10 * * *")
    public void updateSafeZone() {

	}
}
