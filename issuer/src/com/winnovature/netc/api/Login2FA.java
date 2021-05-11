package com.winnovature.netc.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.winnovature.dao.CustomerDAO;
import com.winnovature.dao.LoginDAO;
import com.winnovature.dao.MenuSubMenuDAO;
import com.winnovature.dao.UserMgmtDAO;
import com.winnovature.dto.CustomerDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.service.LoginService;
import com.winnovature.utils.AuditTrail;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.HashGenerator;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;

@WebServlet("/user2fa/login")
public class Login2FA extends HttpServlet {
	static Logger log = Logger.getLogger(Login2FA.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject jsonRequest = new JSONObject();
		JSONObject jo = new JSONObject();
		PrintWriter out = response.getWriter();
		StringBuffer stringBuffer = new StringBuffer();
		String finalResponse = null;
		ResponseDTO responseDTO = new ResponseDTO();
		Gson gson = new GsonBuilder().create();
		Connection conn = null;
		try {
			conn = DatabaseManager.getAutoCommitConnection();
			log.info("Login start");
			String captchaHeader = request.getHeader("captcha").toString();
			String encDataHeader = request.getHeader("encData").toString();
			
			stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
			jsonRequest = new JSONObject(stringBuffer.toString());
			log.info("REQUEST :: " + jsonRequest);
			
			String userId = jsonRequest.getString("userId");
			String password = jsonRequest.getString("password");
			String captcha = jsonRequest.getString("captcha");
			responseDTO = LoginService.userLogin(conn, userId, password, captcha, request);
			
			UserMgmtDAO dm = new UserMgmtDAO();

			log.info("JSON parameter : Captcha " + captcha + " ,  Captcha In Header : " + captchaHeader
					+ " , encDataHeader : " + encDataHeader);
			log.info("UserId " + userId + "Password " + password);
			
			String ipAddress = request.getRemoteAddr();
			log.info("Loged User IP : "+ipAddress);

			//SHAPass objsha = new SHAPass();

			String encDataResp = HashGenerator.sha256(password + captchaHeader);

			log.info("endDataResp : " + encDataResp);

			if (!encDataHeader.equals(encDataResp)) {
				response.setHeader("captchaResp", "NA");
				response.setHeader("authToken", "NA");
				response.setHeader("encDataResp", "NA");
				jo.put("message", "Invalid values");
				jo.put("status", "0");
				return;
			}
			if (!captcha.equalsIgnoreCase(captchaHeader)) {
				response.setHeader("captchaResp", "NA");
				response.setHeader("authToken", "NA");
				response.setHeader("encDataResp", "NA");
				jo.put("message", "Invalid Captcha");
				jo.put("status", "0");
				return;
			}

			
			
			boolean flag = dm.getLogin(conn, userId, password, ipAddress);
			log.info("Login.java ::: UserId " + userId + " ,password " + password + " ,Flag " + flag);

			if (flag) {
				HttpSession session = request.getSession();

				String sessionID = session.getId();
				session.setAttribute("Authorization", sessionID);
				session.setAttribute("userId", userId);
				// session.setAttribute("auth_token", sessionID);

				System.out.println("New session id set to login :: " + sessionID);

				LoginDAO loginDao = new LoginDAO();
				loginDao.insertSessionId(conn, userId, sessionID); // insert while login
				session.setAttribute("sessionID", sessionID);

				JSONObject jsonObj = dm.getLoginDetails(conn, userId, password);
				String roleId = jsonObj.getString("roleId");
				String role = jsonObj.getString("role");
				String uId = jsonObj.getString("userId");

				Map<String, String> hm = new HashMap<String, String>();
				hm.put("USERID", uId);
				hm.put("ROLEID", roleId);
				hm.put("ROLE", role);
				hm.put("IP", ipAddress);
				hm.put("status", "SUCCESS");
				

				
				AuditTrail auditTrail = new AuditTrail();
				auditTrail.addAuditData(conn, uId, uId, "LOGINUSER", "LOGINSUCCESS", hm, ipAddress);
				
				MenuSubMenuDAO mdm =new MenuSubMenuDAO();
				//mdm.getLoginMenu(roleId,uId);
				jo.put("message", "Successfully logged in.");
				jo.put("status", "1");
				jo.put("authToken", sessionID);
				//jo.put("permissions", dm.permissions());
				jo.put("role", role);
				jo.put("roleId", roleId);
				jo.put("userId", uId);
				jo.put("captchaResp", captcha);
				jo.put("isFirstLogin", dm.isFirstLoginFlag(conn, userId));
				jo.put("menus", mdm.getLoginMenu(conn, roleId,uId));
				session.setAttribute(userId, sessionID);
				session.setAttribute(uId, sessionID);
				
				if(roleId.equals("2")) {
					//check is wallet Y/N 
					CustomerDTO customerDTO = new CustomerDAO().geCustomerWalletInfo(conn, userId);
					jo.put("isWallet", customerDTO.getIsWallet());
				}

				// HttpHeaders responseHeaders = new HttpHeaders();
				// responseHeaders.set("MyResponseHeader", "MyValue");
				log.info("session id ::" + sessionID);
				log.info("encDataResp  ::" + encDataResp);
				log.info("captcha  ::" + captcha);

				response.setHeader("authToken", sessionID);
				response.setHeader("captchaResp", captcha);
				response.setHeader("encDataResp", encDataResp);

			}

			else {
				response.setHeader("captchaResp", captcha);
				response.setHeader("authToken", "NA");
				response.setHeader("encDataResp", encDataResp);

				jo.put("message", "UserId or Password does not match");
				jo.put("status", "0");
			}

		} catch (Exception e) {
			jo.put("message", e.getMessage());
			jo.put("status", "0");

			response.setHeader("authToken", "NA");
			response.setHeader("captchaResp", "NA");
			response.setHeader("encDataResp", "NA");

			log.error("Login.java Getting Exception   :::    ", e);
		}

		finally {
			log.info("********** LOGIN RESPONSE  ***********");
			log.info(jo.toString());
			log.info(" ********** ******** ********* *********** ");
			out.write(jo.toString());
			MemoryComponent.closePrintWriter(out);
			DatabaseManager.commitConnection(conn);
			
		}

	}

	
	public static void main(String[] args) {
		//SHAPass hash = new SHAPass();
		String encDataResp = HashGenerator.sha256("8144c8bff932f910bdb89b9a0fd2b33837d0131afa37e75c8e98d0fa52e729d6" + "abcdef");
		System.out.println(encDataResp);
	}
	 
}