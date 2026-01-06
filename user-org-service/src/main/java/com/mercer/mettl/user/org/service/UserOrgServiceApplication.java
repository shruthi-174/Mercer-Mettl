package com.mercer.mettl.user.org.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class UserOrgServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserOrgServiceApplication.class, args);
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//		System.out.println(encoder.encode("Password@123"));
	}
}
