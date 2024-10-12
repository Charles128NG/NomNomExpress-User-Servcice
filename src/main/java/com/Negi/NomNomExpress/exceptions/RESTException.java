package com.Negi.NomNomExpress.exceptions;

import org.springframework.http.HttpStatus;
import lombok.Data;

@Data
public class RESTException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private HttpStatus httpStatus;
	private String errorMsg;
	public RESTException(String message, HttpStatus status) {
		super(message);
		this.httpStatus  = status;
		this.errorMsg = message;
	}
}
