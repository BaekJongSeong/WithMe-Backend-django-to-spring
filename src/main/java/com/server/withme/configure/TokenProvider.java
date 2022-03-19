package com.server.withme.configure;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Jwt token provider
 *
 * @author Yejin Cho
 */
@Component
public class TokenProvider implements InitializingBean {

    private final String secret;
    private final long tokenValidityInMilliseconds;

    private static final String AUTHORITIES_KEY = "auth";
    private static final String AUTHORITIES_DELIMITER = ",";

    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-miliseconds}") long tokenValidityInMilliseconds
    ) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // Set base64 encoded key
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Create new token with user's authorities and name
     * @param authentication
     * @return
     */
    public String createToken(Authentication authentication) {
        // Get user authorities converted to string
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(AUTHORITIES_DELIMITER));

        // Set validity -> 1 day
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        // Build JWT token with username and authorities
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * Resolve token and get authorities and principal from token
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        // Make claims -> get authorities converted to string with delimiter AUTHORITIES_DELIMITER
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Get authorities from claim
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(AUTHORITIES_DELIMITER))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // Make principal with subject in claim
        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * Parse and build token and handle token validation exceptions
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        // Try to parse JWT
        // TODO:: handle exceptions
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("Invalid jwt signature");
        } catch (ExpiredJwtException e) {
            System.out.println("Expired token");
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported jwt token");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid jwt token");
        }
        return false;
    }
}
