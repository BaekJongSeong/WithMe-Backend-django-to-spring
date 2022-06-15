package com.server.withme.serivce.impl;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.server.withme.entity.Account;
import com.server.withme.model.LocationDto;
import com.server.withme.model.SafeZoneDto;
import com.server.withme.model.SendMailDto;
import com.server.withme.model.VertexDto;
import com.server.withme.serivce.IAccountService;
import com.server.withme.serivce.IMailService;

import lombok.RequiredArgsConstructor;
/**
 * Service for Mail
 *
 * @author Jongseong Baek
 */

@RequiredArgsConstructor
@Service
public class MailService implements IMailService{
	

    private final JavaMailSender javaMailSender;
    
    private final IAccountService accountService;
    
    protected final TemplateEngine templateEngine;
    
	@Value("${API.key}")
	private String key;

	@Value("${API.password}")
	private String password;
	
	@Override
	public ArrayList<String> makeMailTemplate(SendMailDto sendMailDto) {
		Context ctx = new Context();
		Account account = accountService.findByAccountIdOrThrow(sendMailDto.getAccountId());
		
		ctx.setVariable("title", "[WithMe] withme 서비스 이메일 인증");
		ctx.setVariable("organization", "WithMe");
		ctx.setVariable("name", account.getName());
		ctx.setVariable("accountId", "accountId는 "+account.getAccountId().toString());
		ctx.setVariable("emailVertify", "안녕하세요 "+account.getName()+"님\n WithMe 서비스에 가입해주셔서 감사합니다. 이메일 인증을 완료해주세요.");

		String contents = this.changeContextToString(ctx);
		String title = "[WithMe] withme 서비스 이메일 인증";
		
		return new ArrayList<>(Arrays.asList(title,contents));
	}

    @Override
    public boolean sendMail(SendMailDto sendMailDto) {
    	List<String> mailContents = this.makeMailTemplate(sendMailDto);

    	String subject = mailContents.get(0);
    	String text = mailContents.get(1);
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
    
    @Override
    public String makeAlarmTitle(String username) {
    	return username +"님, "+" 경로 안내입니다.\n";
    }
    
    @Override
	public ResponseEntity<String> reDirectUrl(String token, String uid, SafeZoneDto safeZoneDto){
    	URI uri = UriComponentsBuilder
    			.fromUriString("http://121.154.58.201:8040")
    			.path("/zone_manage/certify_put/").encode().build().toUri();
    	
    	RestTemplate restTemplate = new RestTemplate();
    	RequestEntity<Void> req = RequestEntity.get(uri).header("AccessToken",token).build();
    	return restTemplate.exchange(req,String.class);
    }
    
    @Override
	public String pathSearching(String name,Double latitude,Double longitude,LocationDto locationDto) {
    	String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36";
    	VertexDto dest = locationDto.getVertexDto(); 
    	ByteBuffer buffer = StandardCharsets.UTF_8.encode(String.valueOf(longitude)+","+String.valueOf(latitude)+",name="+name);
    	String encode1 = StandardCharsets.UTF_8.decode(buffer).toString();
    	buffer = StandardCharsets.UTF_8.encode(String.valueOf(dest.getLongitude())+","+String.valueOf(dest.getLatitude())+",name="+locationDto.getName());
    	String encode2 = StandardCharsets.UTF_8.decode(buffer).toString();
    	URI uri = UriComponentsBuilder
    			.fromUriString("https://naveropenapi.apigw.ntruss.com")
    			.path("/map-direction-15/v1/driving")
    			.queryParam("start",encode1)
    			.queryParam("goal",encode2).encode().build().toUri();
    	
    	RestTemplate restTemplate = new RestTemplate();

    	RequestEntity<Void> req = RequestEntity.get(uri)
    	.header("X-NCP-APIGW-API-KEY-ID",key)
    	.header("X-NCP-APIGW-API-KEY",password)
    	.build();

    	ResponseEntity<String> result = restTemplate.exchange(req,String.class);
    	
    	System.out.println(result.toString());
    	Integer time = Integer.valueOf(result.toString().split("\"duration\"")[1].replaceAll(":", "").split(",")[0]) / 60000;
 
    	String[] route = result.toString().split(",\"section\":")[1].split(",\"guide\":")[0].split(",\"name\":\"");
    	String[] routeNew = new String[route.length-1];
    	for(int idx=1; idx< route.length; idx++) {
    		routeNew[idx-1] = route[idx].split("\",\"")[0];
    	}
    	//String url="https://naveropenapi.apigw.ntruss.com/map-direction-15/v1/driving?"
    	//		+ "start="+String.valueOf(start.get().getLatitude())+","+String.valueOf(start.get().getLongitude())
    	//		+ "&goal="+String.valueOf(dest.get().getLatitude())+","+String.valueOf(dest.get().getLongitude());
    	String content="";
    	content += "출발 장소는 "+name+"이며 도착 장소는 "+locationDto.getName()+"입니다\n";
    	content += "예상 소요 시간은 "+time.toString()+"분 입니다.\n";
    	content += "빠른길은 ";
    	for(int idx=0; idx< routeNew.length; idx++) {
    		if(idx==routeNew.length-1) {
    			content+=routeNew[idx] + " 입니다."; break;
    		}
    		content+= routeNew[idx]+"->";
    	}
    	System.out.println(content);
    	return content;
    }
}
