package com.server.withme;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.software.course.Model.FcmMessage;

import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
@RequiredArgsConstructor
public class FirebaseCloudMessageService {
	
	@Value("${API.path}")
	private String path;
	
	@Value("${API.android}")
	private String android;

    private String API_URL = "https://fcm.googleapis.com/v1/projects/calendarmap-8046b/messages:send";
    private final ObjectMapper objectMapper;

    public void sendMessageTo(String targetToken, String title, String body) throws IOException, FirebaseMessagingException {
        String message = makeMessage(targetToken, title, body);
        
        System.out.println(message);
        
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.Companion.create(message, MediaType.get("application/json; charset=utf-8"));
        //RequestBody requestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(),message);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println("아아 "+response.body().string());
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                    .token(targetToken)
                    .notification(FcmMessage.Notification.builder()
                            .title(title)
                            .body(body)
                            .image(null)
                            .build()
                    ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }


    private String getAccessToken() throws IOException {
       // String firebaseConfigPath = path;
        GoogleCredentials googleCredentials = GoogleCredentials
        		.fromStream(new FileInputStream(path))
                //.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}