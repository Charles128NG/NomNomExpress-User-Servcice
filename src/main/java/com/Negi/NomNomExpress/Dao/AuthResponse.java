package com.Negi.NomNomExpress.Dao;

import lombok.Data;

@Data
public class AuthResponse {
	String accessToken;
	String tokenType = "JWT TOKEN";
	
	public AuthResponse(String token) {
		this.accessToken =  token;
	}
}
