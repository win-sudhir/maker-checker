package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.utils.DatabaseManager;

//import com.netc.utils.DBConnection;

public class CityStateDAO {
	static Logger log = Logger.getLogger(CityStateDAO.class.getName());

	public JSONArray getCityList(String rtoCode, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jo = null;
		JSONArray cities = null;
		String query = null;
		try {
			query = "select * from tbl_city_state_master where rto_code=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, rtoCode);

			rs = ps.executeQuery();
			cities = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();
				jo.put("rtoCode", rs.getString("rto_code"));
				jo.put("city", rs.getString("city"));
				cities.put(jo);
			}

		} catch (Exception e) {
			log.error("getCityList() Getting Error   :::    ", e);
		} finally {
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeResultSet(rs);
		}
		return cities;
	}

	public JSONArray getStateList(String rtoCode, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jo = null;
		JSONArray states = null;
		String query = null;
		try {
			query = "select * from tbl_city_state_master where rto_code=? limit 1";
			ps = conn.prepareStatement(query);
			ps.setString(1, rtoCode);

			rs = ps.executeQuery();
			states = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();
				jo.put("rtoCode", rs.getString("rto_code"));
				jo.put("state", rs.getString("state"));
				states.put(jo);
			}

		} catch (Exception e) {
			log.error("getStateList() Getting Error   :::    ", e);
		} finally {
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeResultSet(rs);
		}
		return states;
	}

	public JSONArray getAllCityList(String rtoCode, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jo = null;
		JSONArray cities = null;
		String query = null;
		try {

			query = "select * from tbl_city_state_master";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			cities = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();
				jo.put("rtoCode", rs.getString("rto_code"));
				jo.put("city", rs.getString("city"));
				cities.put(jo);
			}

		} catch (Exception e) {
			log.error("getAllCityList() Getting Error   :::    ", e);
		} finally {
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeResultSet(rs);
		}
		return cities;
	}

	public JSONArray getAllStateList(String rtoCode, Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jo = null;
		JSONArray states = null;
		String query = null;
		try {

			query = "select distinct(state),rto_code from tbl_city_state_master";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();

			states = new JSONArray();
			while (rs.next()) {
				jo = new JSONObject();
				jo.put("rtoCode", rs.getString("rto_code"));
				jo.put("state", rs.getString("state"));
				states.put(jo);
			}

		} catch (Exception e) {
			log.error("getAllStateList() Getting Error   :::    ", e);
		} finally {
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closeResultSet(rs);
		}
		return states;
	}
}
