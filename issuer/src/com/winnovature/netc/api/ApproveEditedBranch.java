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
import com.winnovature.dto.AgentDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.service.AgentService;
import com.winnovature.service.BranchService;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;

@WebServlet("/branchedit/approve")
public class ApproveEditedBranch extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(ApproveEditedBranch.class.getName());

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

			boolean checkSession = true;//CheckSession.isValidSession(request.getHeader("userId"), request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}

			String ipAddress = request.getRemoteAddr();
			
			BranchService branchService = new BranchService();
			stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
			jsonRequest = new JSONObject(stringBuffer.toString());
			log.info("jsonRequest " + jsonRequest);
			//AgentDTO agentDTO = new AgentDTO();
			String requestType = jsonRequest.getString("requestType");
			String branchId = jsonRequest.getString("branchId");
			log.info("ApproveEditedBranch requestType " + requestType);

			if (("approveEditedBranch").equalsIgnoreCase(requestType)) {
				String type = jsonRequest.getString("type");
				
				responseDTO = branchService.approveEditedBranch(branchId, type, request.getHeader("userId"),
						conn);
				finalResponse = gson.toJson(responseDTO);
			}

			else if (("viewEditedBranch").equalsIgnoreCase(requestType)) {
				log.info("viewEditedBranch--");
				//agentDTO = new Gson().fromJson(jsonRequest.toString(), AgentDTO.class);
				String result = branchService.viewEditedDifference(branchId, request.getHeader("userId"), conn);
				finalResponse = result;
			} 

			
			log.info("*****************Response to branchedit/approve API()****************");

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
