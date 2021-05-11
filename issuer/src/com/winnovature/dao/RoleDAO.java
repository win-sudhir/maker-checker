package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.utils.DatabaseManager;

public class RoleDAO {
	static Logger log = Logger.getLogger(RoleDAO.class.getName());

	public static JSONArray getUserRoles(Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jo = null;
		JSONArray roles = null;
		String query = null;
		try {

			query = "select * from role_master";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			roles = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();
				jo.put("roleId", rs.getString("role_id"));
				jo.put("roleDescription", rs.getString("role_description"));
				roles.put(jo);
			}

		} catch (Exception e) {
			log.error("getUserRoles() Getting Error   : " + e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return roles;
	}

}
