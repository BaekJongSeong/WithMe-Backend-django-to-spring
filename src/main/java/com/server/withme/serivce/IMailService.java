package com.server.withme.serivce;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.thymeleaf.context.Context;

import com.server.withme.model.LocationDto;
import com.server.withme.model.SafeZoneDto;
import com.server.withme.model.SendMailDto;

/**
 * Interface for MailService
 *
 * @author Jongseong Baek
 */
public interface IMailService {

	public ArrayList<String> makeMailTemplate(SendMailDto sendMailDto);
	
	public boolean sendMail(SendMailDto sendMailDto);
	
	public String changeContextToString(Context ctx);
	
    ResponseEntity<String> reDirectUrl(String token, String uid, SafeZoneDto safeZoneDto);
    
	public String pathSearching(String name,Double latitude,Double longitude,LocationDto locationDto);
	
    public String makeAlarmTitle(String username);
}
