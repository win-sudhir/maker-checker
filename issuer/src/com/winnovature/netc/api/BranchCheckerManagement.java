package com.winnovature.netc.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.winnovature.dao.CheckSession;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.service.BranchService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;
import com.winnovature.validation.BranchErrorCode;

/**
 * Servlet implementation class BranchCheckerManagement
 */
@WebServlet("/branchc/management")
public class BranchCheckerManagement extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(BranchMakerManagement.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject jsonRequest = new JSONObject();
		PrintWriter out = response.getWriter();
		StringBuffer stringBuffer = new StringBuffer();
		String finalResponse = null;
		Gson gson = new GsonBuilder().create();
		ResponseDTO responseDTO = new ResponseDTO();
		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"), request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			String ipAddress = request.getRemoteAddr();

			BranchService branchService = new BranchService();

			stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
			jsonRequest = new JSONObject(stringBuffer.toString());
			log.info("jsonRequest " + jsonRequest);

			String requestType = jsonRequest.getString("requestType");
			log.info("UserManagement requestType " + requestType);
			
			if (requestType.equalsIgnoreCase("approveBranch")) {
				String branchId = jsonRequest.getString("branchId");
				responseDTO = branchService.approveBranch(conn, branchId, request.getHeader("userId"), ipAddress);
			} 
			else if (requestType.equalsIgnoreCase("rejectBranch")) {
				String branchId = jsonRequest.getString("branchId");
				String remark = jsonRequest.getString("remark");
				responseDTO = branchService.rejectBranch(conn, branchId, request.getHeader("userId"), ipAddress, remark);
			}/* 
			else if (("getBranchById").equalsIgnoreCase(requestType)) {
				String branchId = jsonRequest.getString("branchId");
				responseDTO = branchService.getBranchById(branchId, request.getHeader("userId"), conn);
			}/*
			else if (("getBranchList").equalsIgnoreCase(requestType)) {
				responseDTO = branchService.getBranchListForChecker(conn);
			} */
			else if (requestType.equalsIgnoreCase("deleteBranch")) {
				String branchId = jsonRequest.getString("branchId");
				//String type = jsonRequest.getString("type");
				responseDTO = branchService.deleteBranchChecker(conn, branchId, request.getHeader("userId"), ipAddress);
			} 
			
			else {
				log.info("Invalid Request Type");
				responseDTO.setErrorCode(BranchErrorCode.WINNABU0029.name());
				responseDTO.setMessage(BranchErrorCode.WINNABU0029.getErrorMessage());
				responseDTO.setStatus(ResponseDTO.failure);
			}

			finalResponse = gson.toJson(responseDTO);
			log.info("*****************Response to branchc/management API()****************");

		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			out.write(finalResponse);
			log.info(finalResponse);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}

}
