package com.server.withme.configure;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;


import java.util.Collections;


/**
 * Swagger Configuration
 *
 * @author Aziz Hasanov
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BASE_PACKAGE = "com.server.withme";
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
			      .apiInfo(apiInfo())	      
			      .securityContexts(Arrays.asList(securityContext()))
			      .securitySchemes(Arrays.asList(apiKey()))
			      .select()
			      .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
			      .paths(PathSelectors.ant("/**/api/**"))
			      .build();
	}
	private ApiInfo apiInfo() {
	    return new ApiInfo(
	      "WithMe REST API",
	      "WithMe REST API Documentation",
	      "0.1",
	      "Terms of service",
	      new Contact("WithMe Team", "wait" ,"jsbaek01@ajou.ac.kr"),
	      "License of API",
	      "API license URL",
	      Collections.emptyList());
	}
	
	  private ApiKey apiKey() {
		    return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
		  }
	
	private SecurityContext securityContext() { 
	    return SecurityContext.builder().securityReferences(defaultAuth()).build(); 
	}
	
	private List<SecurityReference> defaultAuth() { 
	    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything"); 
	    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1]; 
	    authorizationScopes[0] = authorizationScope; 
	    return Arrays.asList(new SecurityReference("JWT", authorizationScopes)); 
	}
	
	
}
