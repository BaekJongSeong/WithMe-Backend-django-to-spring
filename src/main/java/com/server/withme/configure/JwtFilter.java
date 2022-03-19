package com.server.withme.configure;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Custom security filter for JWT
 *
 * @author Yejin Cho
 */
public class JwtFilter extends GenericFilterBean {

    private TokenProvider tokenProvider;

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, 
    		FilterChain filterChain) throws IOException, ServletException {

        // Resolve token from request header
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        // set authentication to SecurityContextHolder
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Stored in Security Context: " + authentication.getName() + ", uri: " + requestURI);
        } else {
            System.out.println("There is no valid JWT token, uri: " + requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Resolve token from request header
     * @param request       servletRequest from
     * @return
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
