package com.winnovature.service;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.winnovature.dto.ResponseDTO;

public class LoginService {
	static Logger log = Logger.getLogger(LoginService.class.getName());

	public static ResponseDTO userLogin(Connection conn, String userId, String password, String captcha,
			String captchaHeader, String encDataHeader) {
		// TODO Auto-generated method stub
		return null;
	}

	public static ResponseDTO userLogin(Connection conn, String userId, String password, String captcha,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
