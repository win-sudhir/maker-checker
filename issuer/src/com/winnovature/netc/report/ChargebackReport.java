package com.winnovature.netc.report;

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

import com.winnovature.dao.ChargebackReportDAO;
import com.winnovature.dao.CheckSession;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;
import com.winnovature.utils.RequestReaderUtility;

@WebServlet("/report/chargeback")
public class ChargebackReport extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(ChargebackReport.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject jsonRequest = new JSONObject();
		PrintWriter out = response.getWriter();
		StringBuffer stringBuffer = new StringBuffer();
		String finalResponse = null;
		
		Connection conn = null;
		try {
			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);
			if (!checkSession) {
				response.setStatus(403);
				return;
			}

			String userId = "admin";
			stringBuffer = RequestReaderUtility.getStringBufferRequest(request);
			jsonRequest = new JSONObject(stringBuffer.toString());
			log.info("REQUEST :: " + jsonRequest);

			String fromDate = jsonRequest.getString("fromDate");
			log.info("fromDate" + fromDate);
			String toDate = jsonRequest.getString("toDate");
			log.info("toDate" + toDate);
			
			finalResponse = new ChargebackReportDAO().getChargebackReport(fromDate, toDate, userId, conn);
			// }
		} catch (Exception e) {
			log.error(e);
			log.info(e.getMessage());
		} finally {
			log.info("*****************Response to report/chargeback API()****************");
			out.write(finalResponse);
			log.info(finalResponse);
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}
	}
}
