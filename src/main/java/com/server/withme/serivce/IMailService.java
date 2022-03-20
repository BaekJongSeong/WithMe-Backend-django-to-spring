package com.server.withme.serivce;

import java.util.ArrayList;

import org.thymeleaf.context.Context;

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
}
