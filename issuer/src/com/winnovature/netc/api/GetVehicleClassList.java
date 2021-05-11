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

import com.winnovature.dao.CheckSession;
import com.winnovature.dao.TagVehicleClassDAO;
import com.winnovature.utils.DatabaseManager;
import com.winnovature.utils.MemoryComponent;

@WebServlet("/vehicle/vehicleclasslist")
public class GetVehicleClassList extends HttpServlet {
	static Logger log = Logger.getLogger(GetVehicleClassList.class.getName());
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		Connection conn = null;
		try {

			conn = DatabaseManager.getAutoCommitConnection();

			boolean checkSession = CheckSession.isValidSession(request.getHeader("userId"),
					request.getHeader("Authorization"), conn);

			if (!checkSession) {
				response.setStatus(403);
				return;
			}

			TagVehicleClassDAO dm = new TagVehicleClassDAO();
			String js = null;
			try {
				js = dm.getVehicleClassList();
				out.write(js);
			} catch (Exception e) {
				JSONObject jo = new JSONObject();
				jo.put("message", e.getMessage());
				out.write(jo.toString());
				log.error("GetVehicleClassList.java :: Getting Exception   :::    ", e);
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.commitConnection(conn);
			MemoryComponent.closePrintWriter(out);
		}
	}

}
