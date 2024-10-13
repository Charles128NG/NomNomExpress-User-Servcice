package com.Negi.NomNomExpress.Dao;

import com.Negi.NomNomExpress.entity.UserEntity;
import java.util.Date;

import lombok.Data;

@Data
public class KafkaMessageBody {
	private String userName;
	private String email;
	private Date timestamp;
	
	public KafkaMessageBody(UserEntity user) {
		this.email = user.getUserEmail();
		this.userName = user.getUserName();
		this.timestamp = new Date();
	}
}
