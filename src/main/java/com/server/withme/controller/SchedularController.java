package com.server.withme.controller;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.withme.entity.Account;
import com.server.withme.entity.AccountOption;
import com.server.withme.serivce.IAccountOptionService;
import com.server.withme.serivce.IAccountService;
import com.server.withme.serivce.ISchedularService;

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
	
	private final ISchedularService schedularService;
	
	private final IAccountService accountService;
	
	private final IAccountOptionService accountOptionService;
	
	@GetMapping("/schedular/safe-zone")
	@Scheduled(cron = "0 15 10 * * *")
    public void updateSafeZone() {
		List<Account> accountList = accountService.findAllAccount();
		List<Account> checkedAccountList = accountService.checkSevenDayOver(accountList);
		List<AccountOption> accountOptionList = accountOptionService.findAllFetchAccountOption(checkedAccountList);
		schedularService.updateSafeZoneBySchedular(accountOptionList);
	}

	@GetMapping("/schedular/expired-ttl")
	@Scheduled(cron = "0 15 02 * * *")
    public void deleteExpireTTL() {
		List<Account> accountList = accountService.findAllAccount();
		List<Account> checkedAccountList = accountService.checkSevenDayOver(accountList);
		List<AccountOption> accountOptionList = accountOptionService.findAllFetchAccountOption(checkedAccountList);
		schedularService.deleteExpireTTLBySchedular(accountOptionList);
	}
	
	
}
