package com.server.withme.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.server.withme.entity.Account;
import com.server.withme.serivce.IAccountService;
import com.server.withme.serivce.ISafeZoneService;

import kr.ac.ajou.vhex.server.model.AccountListDto;
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
	
	private final ISafeZoneService safeZoneService;
	
	private final IAccountService accountService;
	
	@GetMapping("/schedular/safe-zone")
	@Scheduled(cron = "0 15 10 * * *")
    public void updateSafeZone() {
		List<Account> accountList = accountService.findAllAccount();
		List<Account> checkedAccountList = accountService.checkSevenDayOver(accountList);
		safeZoneService.updateSafeZone(checkedAccountList);
	}

	@GetMapping("/accounts")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<AccountListDto> accounts(
            @RequestParam(value = "page", defaultValue = "0") int offset,
            @RequestParam(value = "size", defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(offset, limit);
        AccountListDto dto = new AccountListDto();
        dto.setElements(accountService.getAccountList(pageable));
        dto.setCount(accountService.getAccountCount());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/my-account")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Account> myAccount() {
        return ResponseEntity.ok(accountService.getMyAccount());
    }

    @GetMapping("/accounts/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Account> accountWithUsername(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(accountService.getAccountByUsernameOrThrow(username));
    }
}
