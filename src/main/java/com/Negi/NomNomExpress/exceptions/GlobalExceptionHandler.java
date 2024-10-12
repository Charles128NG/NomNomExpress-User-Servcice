package com.Negi.NomNomExpress.exceptions;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(RESTException.class)
	public ResponseEntity<ErrorResponse> handleRESTException(RESTException e, HttpServletResponse response){
		ErrorResponse errResp = new ErrorResponse();
		
		errResp.setMessage(e.getMessage());
		errResp.setStatusCode(e.getHttpStatus().value());
		errResp.setTimestamp(LocalDateTime.now());
		
		return new ResponseEntity<>(errResp, e.getHttpStatus());
	}
}
