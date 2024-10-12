package com.Negi.NomNomExpress.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.Negi.NomNomExpress.exceptions.RESTException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	@Value("${custom.jwt-secret}")
	private String jwtSecret;
	
	public String generateToken(Authentication authentication) {
		
		String userName = authentication.getName();
		Date currentDate = new Date();
		Date expiry = new Date(currentDate.getTime()+24*60*60*60);
		
		@SuppressWarnings("deprecation")
		String token = Jwts.builder()
				       .setSubject(userName)
				       .setIssuedAt(currentDate)
				       .setExpiration(expiry)
				       .signWith(SignatureAlgorithm.HS512,jwtSecret.getBytes())
				       .compact();
		
		return token;
	}
	
	public String getUserNameFromToken(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(jwtSecret.getBytes())
				.parseClaimsJws(token)
				.getBody();
		
		return claims.getSubject();		        
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser()
			.setSigningKey(jwtSecret.getBytes())
			.parseClaimsJws(token);
			return true;
		}
		catch(Exception e) {
			throw new RESTException("Invalid/Expired Token", HttpStatus.UNAUTHORIZED);
		}
		
	}

}