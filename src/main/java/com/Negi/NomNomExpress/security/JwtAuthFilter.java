package com.Negi.NomNomExpress.security;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Negi.NomNomExpress.exceptions.ErrorResponse;
import com.Negi.NomNomExpress.exceptions.RESTException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtAuthFilter extends OncePerRequestFilter{
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private CustomUserDetailService customUserDetailSerice;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
		String path = request.getRequestURI();
		if(path.equals("/login") || path.equals("/register")) {
			filterChain.doFilter(request, response);
			return;
		}
	    try {
	        String token = getTokenFromRequest(request);
	        if (token != null && jwtUtil.validateToken(token)) {
	            String userName = jwtUtil.getUserNameFromToken(token);

	            UserDetails user = customUserDetailSerice.loadUserByUsername(userName);

	            UsernamePasswordAuthenticationToken authentication =
	                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

	            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	            SecurityContextHolder.getContext().setAuthentication(authentication);
	        } 
	        else {
	        	throw new RESTException("Invalid/Missing auth token", HttpStatus.UNAUTHORIZED);
	        }
	    } catch (RESTException e) {
	    	ErrorResponse errResp = new ErrorResponse();
			
			errResp.setMessage(e.getErrorMsg());
			errResp.setStatusCode(e.getHttpStatus().value());
			errResp.setTimestamp(LocalDateTime.now());
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(objectMapper.writeValueAsString(errResp));
			response.getWriter().flush();
	    	return;
	    	
	    } catch (Exception e) {
	    	ErrorResponse errResp = new ErrorResponse();
	    	
	    	errResp.setMessage(e.getMessage());
			errResp.setStatusCode(HttpStatus.UNAUTHORIZED.value());
			errResp.setTimestamp(LocalDateTime.now());
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(objectMapper.writeValueAsString(errResp));
			response.getWriter().flush();
			return;
	    }

	    // Continue with the filter chain regardless of authentication success
	    filterChain.doFilter(request, response);
	}

	private String getTokenFromRequest(HttpServletRequest request) {
	    String bearerToken = request.getHeader("Authorization");
	    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
	        return bearerToken.substring(7);  // Remove "Bearer " prefix
	    }
	    return null;
	}
	
}
