package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.utils.DatabaseManager;

public class PermissionDAO {
	static Logger log = Logger.getLogger(PermissionDAO.class.getName());
	
	public JSONArray getRolePermission(Connection conn, String roleId) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONArray permArray = null;
		String query = null;
		try {
			//query = "Select group_concat(distinct rpm.roleid) as role_id, group_concat(rpm.permissionid) as permissions,rm.role_description, pm.permissionid from role_permissions_map rpm INNER JOIN permissions_master pm ON rpm.permissionid=pm.permissionid "
				//	+ "INNER JOIN role_master rm ON rpm.roleid=rm.role_id where rpm.is_deleted=? and rm.is_deleted=? and pm.is_deleted=? group by rm.role_id"; //AND rpm.roleid=?
			query = "select m.module_name, m.permitted_operations, p.permissionid, p.permission from modules m, permissions_master p, role_permissions_map r where m.module_name=p.modulename and p.permissionid=r.permissionid and roleid=?  AND m.is_deleted=? AND p.is_deleted=? AND r.is_deleted=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, roleId);
			ps.setString(2, "0");
			ps.setString(3, "0");
			ps.setString(4, "0");
			//
			rs = ps.executeQuery();
			permArray = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();
				jo.put("moduleName", rs.getString("module_name"));
				jo.put("permissionId", rs.getString("permissionid"));
				jo.put("permission", rs.getString("permission"));
				//jo.put("permittedOperations", rs.getString("permitted_operations"));
				permArray.put(jo);
			}

		} catch (Exception e) {
			log.error("Getting Error getRolePemission : "+ e);
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closePreparedStatement(ps);
		}
		return permArray;
	}
}
