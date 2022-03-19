package com.server.withme.serivce.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.server.withme.entity.Account;
import com.server.withme.model.SendMailDto;
import com.server.withme.repository.AccountRepository;
import com.server.withme.serivce.IMailService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MailService implements IMailService{

    private final JavaMailSender javaMailSender;
    
    private final AccountRepository accountRepository;
    
    protected final TemplateEngine templateEngine;
	
	@Override
	public ArrayList<String> makeMailTemplate(SendMailDto sendMailDto) {
		Context ctx = new Context();
		 Account account = accountRepository.findByAccountId(sendMailDto.getAccountId())
				 .orElseThrow(() -> new UsernameNotFoundException("not found user"));
		
		ctx.setVariable("title", "[WithMe] withme 서비스 이메일 인증");
		ctx.setVariable("organization", "WithMe");
		ctx.setVariable("name", account.getName());
		ctx.setVariable("accountId", "accountId는 "+account.getAccountId());
		ctx.setVariable("emailVertify", "안녕하세요 "+account.getName()+"님\n WithMe 서비스에 가입해주셔서 감사합니다. 이메일 인증을 완료해주세요.");

		String contents = this.changeContextToString(ctx);
		String title = "[WithMe] withme 서비스 이메일 인증";
		
		return new ArrayList<>(Arrays.asList(title,contents));
	}

    @Override
    public boolean sendMail(SendMailDto sendMailDto) {
    	List<String> mailContents = this.makeMailTemplate(sendMailDto);

    	String subject = mailContents.get(1);
    	String text = mailContents.get(0);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(sendMailDto.getEmail());
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String changeContextToString(Context ctx) {
        return templateEngine.process("mail/emailVertify", ctx);
    }
}
