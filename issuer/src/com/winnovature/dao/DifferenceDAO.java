package com.winnovature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
//import org.json.JSONArray;
import org.json.JSONObject;

import com.winnovature.utils.DatabaseManager;

public class DifferenceDAO {
	static Logger log = Logger.getLogger(DifferenceDAO.class.getName());
	
	public static String getAgentEditedDifference(String agentId, Connection conn) {
		JSONObject json = new JSONObject();
		//JSONArray jsonArray = new JSONArray();
		
		ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null;
		PreparedStatement ps = null, ps1 = null, ps2 = null, ps3 = null, ps4 = null, ps5 = null;
		try {
			String bankAgentId = null, branchId = null, agentName = null, contactPersonName = null, emailId = null, contactNumber = null;
			String query = "SELECT * FROM agent_info WHERE agent_id=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, agentId);
			rs = ps.executeQuery();
			if (rs.next()) {
				bankAgentId = rs.getString("bank_agent_id");
				branchId = rs.getString("branch_id");
				agentName = rs.getString("agent_name");
				emailId = rs.getString("email_id");
				contactNumber = rs.getString("contact_number");
				contactPersonName = rs.getString("contact_person_name");
			}
			
			String newBankAgentId = null, newBranchId = null, newAgentName = null, newContactPersonName = null, newEmailId = null, newContactNumber = null;
			String query1 = "SELECT * FROM agent_edited_info WHERE agent_id=?";
			ps1 = conn.prepareStatement(query1);
			ps1.setString(1, agentId);
			rs1 = ps1.executeQuery();
			if (rs1.next()) {
				newBankAgentId = rs1.getString("bank_agent_id");
				newBranchId = rs1.getString("branch_id");
				newAgentName = rs1.getString("agent_name");
				newEmailId = rs1.getString("email_id");
				newContactNumber = rs1.getString("contact_number");
				newContactPersonName = rs1.getString("contact_person_name");
			}
			//newBankAgentId, 
			if(!newBankAgentId.equals(bankAgentId)) {
				json.put("newBankAgentId", newBankAgentId);
				json.put("bankAgentId", bankAgentId);
			}
			//newBranchId, 
			if(!newBranchId.equals(branchId)) {
				json.put("newBranchId", newBranchId);
				json.put("branchId", branchId);
			}
			//newAgentName, newContactPersonName, newEmailId, newContactNumber;
			if(!newAgentName.equals(agentName)) {
				json.put("newAgentName", newAgentName);
				json.put("agentName", agentName);
			}
			
			if(!newContactPersonName.equals(contactPersonName)) {
				json.put("newContactPersonName", newContactPersonName);
				json.put("contactPersonName", contactPersonName);
			}
			if(!newEmailId.equals(emailId)) {
				json.put("newEmailId", newEmailId);
				json.put("emailId", emailId);
			}
			if(!newContactNumber.equals(contactNumber)) {
				json.put("newContactNumber", newContactNumber);
				json.put("contactNumber", contactNumber);
			}
			//account difference
			//user_id, bank_name, account_number, account_type, ifsc_code, branch_address
			String bankName = null, accountNumber = null, accountType = null, ifscCode = null, branchAddress = null;
			String newBankName = null, newAccountNumber = null, newAccountType = null, newIfscCode = null, newBranchAddress = null;
			String query2 = "SELECT * FROM account_info WHERE user_id=?";
			ps2 = conn.prepareStatement(query2);
			ps2.setString(1, agentId);
			rs2 = ps2.executeQuery();
			if (rs2.next()) {
				bankName = rs2.getString("bank_name");
				accountNumber = rs2.getString("account_number");
				accountType = rs2.getString("account_type");
				ifscCode = rs2.getString("ifsc_code");
				branchAddress = rs2.getString("branch_address");
			}
			
			String query3 = "SELECT * FROM account_edited_info WHERE user_id=?";
			ps3 = conn.prepareStatement(query3);
			ps3.setString(1, agentId);
			rs3 = ps3.executeQuery();
			if (rs3.next()) {
				newBankName = rs3.getString("bank_name");
				newAccountNumber = rs3.getString("account_number");
				newAccountType = rs3.getString("account_type");
				newIfscCode = rs3.getString("ifsc_code");
				newBranchAddress = rs3.getString("branch_address");
			}
			if(!newBankName.equals(bankName)) {
				json.put("newBankName", newBankName);
				json.put("bankName", bankName);
			}
			if(!newAccountNumber.equals(accountNumber)) {
				json.put("newAccountNumber", newAccountNumber);
				json.put("accountNumber", accountNumber);
			}
			if(!newAccountType.equals(accountType)) {
				json.put("newAccountType", newAccountType);
				json.put("accountType", accountType);
			}
			if(!newIfscCode.equals(ifscCode)) {
				json.put("newIfscCode", newIfscCode);
				json.put("ifscCode", ifscCode);
			}
			if(!newBranchAddress.equals(branchAddress)) {
				json.put("newBranchAddress", newBranchAddress);
				json.put("branchAddress", branchAddress);
			}
			//address difference
			String resiAddress1 = null,  resiAddress2 = null,  resiPin = null,  resiCity = null,  resiState = null,  
					businessAdd1 = null, businessAdd2 = null, businessPin = null, businessCity = null, businessState = null;
			String newResiAddress1 = null,  newResiAddress2 = null,  newResiPin = null,  newResiCity = null,  newResiState = null,  newBusinessAdd1 = null, newBusinessAdd2 = null, newBusinessPin = null, newBusinessCity = null, newBusinessState = null;
			String query4 = "SELECT * FROM address_info WHERE user_id=?";
			ps4 = conn.prepareStatement(query4);
			ps4.setString(1, agentId);
			rs4 = ps4.executeQuery();
			if (rs4.next()) {
				//user_id, resi_address1, resi_address2, resi_pin, resi_city, resi_state,
				//business_add1, business_add2, business_pin, business_city, business_state
				resiAddress1 = rs4.getString("resi_address1");
				resiAddress2 = rs4.getString("resi_address2");
				resiPin = rs4.getString("resi_pin");
				resiCity = rs4.getString("resi_city");
				resiState = rs4.getString("resi_state");
				
				businessAdd1 = rs4.getString("business_add1");
				businessAdd2 = rs4.getString("business_add2");
				businessPin = rs4.getString("business_pin");
				businessCity = rs4.getString("business_city");
				businessState = rs4.getString("business_state");
			}
			
			String query5 = "SELECT * FROM address_edited_info WHERE user_id=?";
			ps5 = conn.prepareStatement(query5);
			ps5.setString(1, agentId);
			rs5 = ps5.executeQuery();
			if (rs5.next()) {
				//user_id, resi_address1, resi_address2, resi_pin, resi_city, resi_state,
				//business_add1, business_add2, business_pin, business_city, business_state
				newResiAddress1 = rs5.getString("resi_address1");
				newResiAddress2 = rs5.getString("resi_address2");
				newResiPin = rs5.getString("resi_pin");
				newResiCity = rs5.getString("resi_city");
				newResiState = rs5.getString("resi_state");
				
				newBusinessAdd1 = rs5.getString("business_add1");
				newBusinessAdd2 = rs5.getString("business_add2");
				newBusinessPin = rs5.getString("business_pin");
				newBusinessCity = rs5.getString("business_city");
				newBusinessState = rs5.getString("business_state");
			}
			if(!newResiAddress1.equals(resiAddress1)) {
				json.put("newResiAddress1", newResiAddress1);
				json.put("resiAddress1", resiAddress1);
			}
			if(!newResiAddress2.equals(resiAddress2)) {
				json.put("newResiAddress2", newResiAddress2);
				json.put("resiAddress2", resiAddress2);
			}
			if(!newResiPin.equals(resiPin)) {
				json.put("newResiPin", newResiPin);
				json.put("resiPin", resiPin);
			}
			if(!newResiCity.equals(resiCity)) {
				json.put("newResiCity", newResiCity);
				json.put("resiCity", resiCity);
			}
			if(!newResiState.equals(resiState)) {
				json.put("newResiState", newResiState);
				json.put("resiState", resiState);
			}
			
			if(!newBusinessAdd1.equals(businessAdd1)) {
				json.put("newBusinessAddress1", newBusinessAdd1);
				json.put("BusinessAddress1", businessAdd1);
			}
			if(!newBusinessAdd2.equals(businessAdd2)) {
				json.put("newBusinessAddress2", newBusinessAdd2);
				json.put("BusinessAddress2", businessAdd2);
			}
			if(!newBusinessPin.equals(businessPin)) {
				json.put("newBusinessPin", newBusinessPin);
				json.put("BusinessPin", businessPin);
			}
			if(!newBusinessCity.equals(businessCity)) {
				json.put("newBusinessCity", newBusinessCity);
				json.put("BusinessCity", businessCity);
			}
			if(!newBusinessState.equals(businessState)) {
				json.put("newBusinessState", newBusinessState);
				json.put("BusinessState", businessState);
			}
			//jsonArray.put(json);
			return json.toString();
		}

		catch (Exception e) {
			log.error("getAgentEditedDifference()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closeResultSet(rs1);
			DatabaseManager.closeResultSet(rs2);
			DatabaseManager.closeResultSet(rs3);
			DatabaseManager.closeResultSet(rs4);
			DatabaseManager.closeResultSet(rs5);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closePreparedStatement(ps1);
			DatabaseManager.closePreparedStatement(ps2);
			DatabaseManager.closePreparedStatement(ps3);
			DatabaseManager.closePreparedStatement(ps4);
			DatabaseManager.closePreparedStatement(ps5);
		}
		
		return null;
	}

	public static String getCustomerEditedDifference(String customerId, Connection conn) {
		JSONObject json = new JSONObject();
		//JSONArray jsonArray = new JSONArray();
		
		ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null;
		PreparedStatement ps = null, ps1 = null, ps2 = null, ps3 = null, ps4 = null, ps5 = null;
		try {
			//isCorporate = null,
			String dob = null, gender = null, customerName = null, emailId = null, contactNumber = null, occupation=null, isWallet=null;
			//customer_name, email_id, contact_number, dob, is_corporate, gender, occupation, is_wallet
			String query = "SELECT * FROM customer_info WHERE user_id=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, customerId);
			rs = ps.executeQuery();
			if (rs.next()) {
				dob = rs.getString("dob");
				//isCorporate = rs.getString("is_corporate");
				gender = rs.getString("gender");
				emailId = rs.getString("email_id");
				contactNumber = rs.getString("contact_number");
				customerName = rs.getString("customer_name");
				occupation = rs.getString("occupation");
				isWallet = rs.getString("is_wallet");
			}
			//newIsCorporate = null,
			String newDob = null, newGender = null, newCustomerName = null, newEmailId = null, newContactNumber = null, newOccupation=null, newIsWallet=null;
			String query1 = "SELECT * FROM customer_edited_info WHERE user_id=?";
			ps1 = conn.prepareStatement(query1);
			ps1.setString(1, customerId);
			rs1 = ps1.executeQuery();
			if (rs1.next()) {
				newDob = rs1.getString("dob");
				//newIsCorporate = rs1.getString("is_corporate");
				newGender = rs1.getString("gender");
				newEmailId = rs1.getString("email_id");
				newContactNumber = rs1.getString("contact_number");
				newCustomerName = rs1.getString("customer_name");
				newOccupation = rs1.getString("occupation");
				newIsWallet = rs1.getString("is_wallet");
			}
			if(!newDob.equals(dob)) {
				json.put("newDob", newDob);
				json.put("dob", dob);
			}
			/*if(!newIsCorporate.equals(isCorporate)) {
				json.put("newIsCorporate", newIsCorporate);
				json.put("branchId", isCorporate);
			}*/
			if(!newOccupation.equals(occupation)) {
				json.put("newOccupation", newOccupation);
				json.put("occupation", occupation);
			}
			
			if(!newCustomerName.equals(customerName)) {
				json.put("newCustomerName", newCustomerName);
				json.put("customerName", customerName);
			}
			if(!newEmailId.equals(emailId)) {
				json.put("newEmailId", newEmailId);
				json.put("emailId", emailId);
			}
			if(!newContactNumber.equals(contactNumber)) {
				json.put("newContactNumber", newContactNumber);
				json.put("contactNumber", contactNumber);
			}
			if(!newGender.equals(gender)) {
				json.put("newGender", newGender);
				json.put("gender", gender);
			}
			if(!newIsWallet.equals(isWallet)) {
				json.put("newIsWallet", newIsWallet);
				json.put("isWallet", isWallet);
			}
			//account difference
			//user_id, bank_name, account_number, account_type, ifsc_code, branch_address
			String bankName = null, accountNumber = null, accountType = null, ifscCode = null, branchAddress = null;
			String newBankName = null, newAccountNumber = null, newAccountType = null, newIfscCode = null, newBranchAddress = null;
			String query2 = "SELECT * FROM account_info WHERE user_id=?";
			ps2 = conn.prepareStatement(query2);
			ps2.setString(1, customerId);
			rs2 = ps2.executeQuery();
			if (rs2.next()) {
				bankName = rs2.getString("bank_name");
				accountNumber = rs2.getString("account_number");
				accountType = rs2.getString("account_type");
				ifscCode = rs2.getString("ifsc_code");
				branchAddress = rs2.getString("branch_address");
			}
			
			String query3 = "SELECT * FROM account_edited_info WHERE user_id=?";
			ps3 = conn.prepareStatement(query3);
			ps3.setString(1, customerId);
			rs3 = ps3.executeQuery();
			if (rs3.next()) {
				newBankName = rs3.getString("bank_name");
				newAccountNumber = rs3.getString("account_number");
				newAccountType = rs3.getString("account_type");
				newIfscCode = rs3.getString("ifsc_code");
				newBranchAddress = rs3.getString("branch_address");
			}
			if(!newBankName.equals(bankName)) {
				json.put("newBankName", newBankName);
				json.put("bankName", bankName);
			}
			if(!newAccountNumber.equals(accountNumber)) {
				json.put("newAccountNumber", newAccountNumber);
				json.put("accountNumber", accountNumber);
			}
			if(!newAccountType.equals(accountType)) {
				json.put("newAccountType", newAccountType);
				json.put("accountType", accountType);
			}
			if(!newIfscCode.equals(ifscCode)) {
				json.put("newIfscCode", newIfscCode);
				json.put("ifscCode", ifscCode);
			}
			if(!newBranchAddress.equals(branchAddress)) {
				json.put("newBranchAddress", newBranchAddress);
				json.put("branchAddress", branchAddress);
			}
			//address difference
			String resiAddress1 = null,  resiAddress2 = null,  resiPin = null,  resiCity = null,  resiState = null,  
					businessAdd1 = null, businessAdd2 = null, businessPin = null, businessCity = null, businessState = null;
			String newResiAddress1 = null,  newResiAddress2 = null,  newResiPin = null,  newResiCity = null,  newResiState = null,  newBusinessAdd1 = null, newBusinessAdd2 = null, newBusinessPin = null, newBusinessCity = null, newBusinessState = null;
			String query4 = "SELECT * FROM address_info WHERE user_id=?";
			ps4 = conn.prepareStatement(query4);
			ps4.setString(1, customerId);
			rs4 = ps4.executeQuery();
			if (rs4.next()) {
				//user_id, resi_address1, resi_address2, resi_pin, resi_city, resi_state,
				//business_add1, business_add2, business_pin, business_city, business_state
				resiAddress1 = rs4.getString("resi_address1");
				resiAddress2 = rs4.getString("resi_address2");
				resiPin = rs4.getString("resi_pin");
				resiCity = rs4.getString("resi_city");
				resiState = rs4.getString("resi_state");
				
				businessAdd1 = rs4.getString("business_add1");
				businessAdd2 = rs4.getString("business_add2");
				businessPin = rs4.getString("business_pin");
				businessCity = rs4.getString("business_city");
				businessState = rs4.getString("business_state");
			}
			
			String query5 = "SELECT * FROM address_edited_info WHERE user_id=?";
			ps5 = conn.prepareStatement(query5);
			ps5.setString(1, customerId);
			rs5 = ps5.executeQuery();
			if (rs5.next()) {
				//user_id, resi_address1, resi_address2, resi_pin, resi_city, resi_state,
				//business_add1, business_add2, business_pin, business_city, business_state
				newResiAddress1 = rs5.getString("resi_address1");
				newResiAddress2 = rs5.getString("resi_address2");
				newResiPin = rs5.getString("resi_pin");
				newResiCity = rs5.getString("resi_city");
				newResiState = rs5.getString("resi_state");
				
				newBusinessAdd1 = rs5.getString("business_add1");
				newBusinessAdd2 = rs5.getString("business_add2");
				newBusinessPin = rs5.getString("business_pin");
				newBusinessCity = rs5.getString("business_city");
				newBusinessState = rs5.getString("business_state");
			}
			if(!newResiAddress1.equals(resiAddress1)) {
				json.put("newResiAddress1", newResiAddress1);
				json.put("resiAddress1", resiAddress1);
			}
			if(!newResiAddress2.equals(resiAddress2)) {
				json.put("newResiAddress2", newResiAddress2);
				json.put("resiAddress2", resiAddress2);
			}
			if(!newResiPin.equals(resiPin)) {
				json.put("newResiPin", newResiPin);
				json.put("resiPin", resiPin);
			}
			if(!newResiCity.equals(resiCity)) {
				json.put("newResiCity", newResiCity);
				json.put("resiCity", resiCity);
			}
			if(!newResiState.equals(resiState)) {
				json.put("newResiState", newResiState);
				json.put("resiState", resiState);
			}
			
			if(!newBusinessAdd1.equals(businessAdd1)) {
				json.put("newBusinessAddress1", newBusinessAdd1);
				json.put("BusinessAddress1", businessAdd1);
			}
			if(!newBusinessAdd2.equals(businessAdd2)) {
				json.put("newBusinessAddress2", newBusinessAdd2);
				json.put("BusinessAddress2", businessAdd2);
			}
			if(!newBusinessPin.equals(businessPin)) {
				json.put("newBusinessPin", newBusinessPin);
				json.put("BusinessPin", businessPin);
			}
			if(!newBusinessCity.equals(businessCity)) {
				json.put("newBusinessCity", newBusinessCity);
				json.put("BusinessCity", businessCity);
			}
			if(!newBusinessState.equals(businessState)) {
				json.put("newBusinessState", newBusinessState);
				json.put("BusinessState", businessState);
			}
			//jsonArray.put(json);
			return json.toString();
		}

		catch (Exception e) {
			log.error("getCustomerEditedDifference()  ::  getting error  : ", e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closeResultSet(rs1);
			DatabaseManager.closeResultSet(rs2);
			DatabaseManager.closeResultSet(rs3);
			DatabaseManager.closeResultSet(rs4);
			DatabaseManager.closeResultSet(rs5);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closePreparedStatement(ps1);
			DatabaseManager.closePreparedStatement(ps2);
			DatabaseManager.closePreparedStatement(ps3);
			DatabaseManager.closePreparedStatement(ps4);
			DatabaseManager.closePreparedStatement(ps5);
		}
		
		return null;
	}

	public static String getBranchEditedDifference(String branchId, Connection conn) {
		JSONObject json = new JSONObject();
		//JSONArray jsonArray = new JSONArray();
		
		ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null, rs4 = null, rs5 = null;
		PreparedStatement ps = null, ps1 = null, ps2 = null, ps3 = null, ps4 = null, ps5 = null;
		try {
			//branchId = null,
			//branch_id, bank_branch_id, branch_name, email_id, contact_number
			String bankBranchId = null, branchName = null, emailId = null, contactNumber = null;
			String query = "SELECT * FROM branch_info WHERE branch_id=?";
			ps = conn.prepareStatement(query);
			ps.setString(1, branchId);
			rs = ps.executeQuery();
			if (rs.next()) {
				bankBranchId = rs.getString("bank_branch_id");
				branchName = rs.getString("branch_name");
				emailId = rs.getString("email_id");
				contactNumber = rs.getString("contact_number");
			}
			
			String newBankBranchId = null, newBranchName = null, newEmailId = null, newContactNumber = null;
			String query1 = "SELECT * FROM branch_edited_info WHERE branch_id=?";
			ps1 = conn.prepareStatement(query1);
			ps1.setString(1, branchId);
			rs1 = ps1.executeQuery();
			if (rs1.next()) {
				newBankBranchId = rs1.getString("bank_branch_id");
				newBranchName = rs1.getString("branch_name");
				newEmailId = rs1.getString("email_id");
				newContactNumber = rs1.getString("contact_number");
			}
			//newBankAgentId, 
			if(!newBankBranchId.equals(bankBranchId)) {
				json.put("newBankBranchId", newBankBranchId);
				json.put("bankBranchId", bankBranchId);
			}
			//newBranchId, 
			if(!newBranchName.equals(branchId)) {
				json.put("newBranchName", newBranchName);
				json.put("branchName", branchName);
			}
			if(!newEmailId.equals(emailId)) {
				json.put("newEmailId", newEmailId);
				json.put("emailId", emailId);
			}
			if(!newContactNumber.equals(contactNumber)) {
				json.put("newContactNumber", newContactNumber);
				json.put("contactNumber", contactNumber);
			}
			//account difference
			//user_id, bank_name, account_number, account_type, ifsc_code, branch_address
			String bankName = null, accountNumber = null, accountType = null, ifscCode = null, branchAddress = null;
			String newBankName = null, newAccountNumber = null, newAccountType = null, newIfscCode = null, newBranchAddress = null;
			String query2 = "SELECT * FROM account_info WHERE user_id=?";
			ps2 = conn.prepareStatement(query2);
			ps2.setString(1, branchId);
			rs2 = ps2.executeQuery();
			if (rs2.next()) {
				bankName = rs2.getString("bank_name");
				accountNumber = rs2.getString("account_number");
				accountType = rs2.getString("account_type");
				ifscCode = rs2.getString("ifsc_code");
				branchAddress = rs2.getString("branch_address");
			}
			
			String query3 = "SELECT * FROM account_edited_info WHERE user_id=?";
			ps3 = conn.prepareStatement(query3);
			ps3.setString(1, branchId);
			rs3 = ps3.executeQuery();
			if (rs3.next()) {
				newBankName = rs3.getString("bank_name");
				newAccountNumber = rs3.getString("account_number");
				newAccountType = rs3.getString("account_type");
				newIfscCode = rs3.getString("ifsc_code");
				newBranchAddress = rs3.getString("branch_address");
			}
			if(!newBankName.equals(bankName)) {
				json.put("newBankName", newBankName);
				json.put("bankName", bankName);
			}
			if(!newAccountNumber.equals(accountNumber)) {
				json.put("newAccountNumber", newAccountNumber);
				json.put("accountNumber", accountNumber);
			}
			if(!newAccountType.equals(accountType)) {
				json.put("newAccountType", newAccountType);
				json.put("accountType", accountType);
			}
			if(!newIfscCode.equals(ifscCode)) {
				json.put("newIfscCode", newIfscCode);
				json.put("ifscCode", ifscCode);
			}
			if(!newBranchAddress.equals(branchAddress)) {
				json.put("newBranchAddress", newBranchAddress);
				json.put("branchAddress", branchAddress);
			}
			//address difference
			String resiAddress1 = null,  resiAddress2 = null,  resiPin = null,  resiCity = null,  resiState = null,  
					businessAdd1 = null, businessAdd2 = null, businessPin = null, businessCity = null, businessState = null;
			String newResiAddress1 = null,  newResiAddress2 = null,  newResiPin = null,  newResiCity = null,  newResiState = null,  newBusinessAdd1 = null, newBusinessAdd2 = null, newBusinessPin = null, newBusinessCity = null, newBusinessState = null;
			String query4 = "SELECT * FROM address_info WHERE user_id=?";
			ps4 = conn.prepareStatement(query4);
			ps4.setString(1, branchId);
			rs4 = ps4.executeQuery();
			if (rs4.next()) {
				//user_id, resi_address1, resi_address2, resi_pin, resi_city, resi_state,
				//business_add1, business_add2, business_pin, business_city, business_state
				resiAddress1 = rs4.getString("resi_address1");
				resiAddress2 = rs4.getString("resi_address2");
				resiPin = rs4.getString("resi_pin");
				resiCity = rs4.getString("resi_city");
				resiState = rs4.getString("resi_state");
				
				businessAdd1 = rs4.getString("business_add1");
				businessAdd2 = rs4.getString("business_add2");
				businessPin = rs4.getString("business_pin");
				businessCity = rs4.getString("business_city");
				businessState = rs4.getString("business_state");
			}
			
			String query5 = "SELECT * FROM address_edited_info WHERE user_id=?";
			ps5 = conn.prepareStatement(query5);
			ps5.setString(1, branchId);
			rs5 = ps5.executeQuery();
			if (rs5.next()) {
				//user_id, resi_address1, resi_address2, resi_pin, resi_city, resi_state,
				//business_add1, business_add2, business_pin, business_city, business_state
				newResiAddress1 = rs5.getString("resi_address1");
				newResiAddress2 = rs5.getString("resi_address2");
				newResiPin = rs5.getString("resi_pin");
				newResiCity = rs5.getString("resi_city");
				newResiState = rs5.getString("resi_state");
				
				newBusinessAdd1 = rs5.getString("business_add1");
				newBusinessAdd2 = rs5.getString("business_add2");
				newBusinessPin = rs5.getString("business_pin");
				newBusinessCity = rs5.getString("business_city");
				newBusinessState = rs5.getString("business_state");
			}
			if(!newResiAddress1.equals(resiAddress1)) {
				json.put("newResiAddress1", newResiAddress1);
				json.put("resiAddress1", resiAddress1);
			}
			if(!newResiAddress2.equals(resiAddress2)) {
				json.put("newResiAddress2", newResiAddress2);
				json.put("resiAddress2", resiAddress2);
			}
			if(!newResiPin.equals(resiPin)) {
				json.put("newResiPin", newResiPin);
				json.put("resiPin", resiPin);
			}
			if(!newResiCity.equals(resiCity)) {
				json.put("newResiCity", newResiCity);
				json.put("resiCity", resiCity);
			}
			if(!newResiState.equals(resiState)) {
				json.put("newResiState", newResiState);
				json.put("resiState", resiState);
			}
			
			if(!newBusinessAdd1.equals(businessAdd1)) {
				json.put("newBusinessAddress1", newBusinessAdd1);
				json.put("BusinessAddress1", businessAdd1);
			}
			if(!newBusinessAdd2.equals(businessAdd2)) {
				json.put("newBusinessAddress2", newBusinessAdd2);
				json.put("BusinessAddress2", businessAdd2);
			}
			if(!newBusinessPin.equals(businessPin)) {
				json.put("newBusinessPin", newBusinessPin);
				json.put("BusinessPin", businessPin);
			}
			if(!newBusinessCity.equals(businessCity)) {
				json.put("newBusinessCity", newBusinessCity);
				json.put("BusinessCity", businessCity);
			}
			if(!newBusinessState.equals(businessState)) {
				json.put("newBusinessState", newBusinessState);
				json.put("BusinessState", businessState);
			}
			//jsonArray.put(json);
			return json.toString();
		}

		catch (Exception e) {
			log.error("getBranchEditedDifference()  ::  getting error  : "+ e);
			e.printStackTrace();
		} finally {
			DatabaseManager.closeResultSet(rs);
			DatabaseManager.closeResultSet(rs1);
			DatabaseManager.closeResultSet(rs2);
			DatabaseManager.closeResultSet(rs3);
			DatabaseManager.closeResultSet(rs4);
			DatabaseManager.closeResultSet(rs5);
			DatabaseManager.closePreparedStatement(ps);
			DatabaseManager.closePreparedStatement(ps1);
			DatabaseManager.closePreparedStatement(ps2);
			DatabaseManager.closePreparedStatement(ps3);
			DatabaseManager.closePreparedStatement(ps4);
			DatabaseManager.closePreparedStatement(ps5);
		}
		
		return null;
	}

}
