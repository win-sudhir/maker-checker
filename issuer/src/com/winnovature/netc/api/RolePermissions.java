package com.winnovature.netc.api;

import java.io.BufferedReader;
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

import com.winnovature.dao.PermissionDAO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/role/permissions")
public class RolePermissions extends HttpServlet {
	static Logger log = Logger.getLogger(RolePermissions.class.getName());
	private static final long serialVersionUID = 1L;

	public RolePermissions() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		// JSONObject jsonObject=null;
		//JSONObject jo = null;
		StringBuffer jb = new StringBuffer();
		String line = null;
		JSONArray permissions = null;
		Connection conn = null;
		try {
			conn = DatabaseManager.getAutoCommitConnection();
			PermissionDAO PermissionDAO = new PermissionDAO();
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}
			MemoryComponent.closeBufferedReader(reader);
			//jo = new JSONObject();
			permissions = PermissionDAO.getRolePermission(conn, "1");
			out.write(permissions.toString());
			
		} catch (Exception e) {
			log.error("Getting Error in permissions  : "+ e);
		} finally {
			MemoryComponent.closePrintWriter(out);
		}
	}
}