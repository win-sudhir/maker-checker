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
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.dao.CheckSession;
import com.winnovature.dao.RoleDAO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/user/roles")
public class GetRoles extends HttpServlet {
	static Logger log = Logger.getLogger(GetRoles.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONArray roles = new JSONArray();
		JSONObject resp = new JSONObject();
		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}
			roles = RoleDAO.getUserRoles(conn);
			resp.put("roles", roles);
			response.setStatus(200);
			out.write(resp.toString());
			log.info("*****************Response to user/roles API()****************");
			log.info("--------------------------------------------------------------------------------");
			log.info(resp.length());
			log.info("--------------------------------------------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception occured in user/roles "+e);
		} finally {
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}

	}
}
