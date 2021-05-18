package com.winnovature.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.winnovature.constants.IDGenerator;
import com.winnovature.constants.WINConstants;
import com.winnovature.dao.AccountDAO;
import com.winnovature.dao.AddressDAO;
import com.winnovature.dao.BranchDAO;
import com.winnovature.dao.DifferenceDAO;
import com.winnovature.dto.AccountDTO;
import com.winnovature.dto.AddressDTO;
import com.winnovature.dto.BranchAccountDTO;
import com.winnovature.dto.BranchDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.utils.AuditTrail;
import com.winnovature.utils.EmailTemplate;
import com.winnovature.utils.PasswordManager;
import com.winnovature.utils.SendMailService;
import com.winnovature.validation.BranchErrorCode;
import com.winnovature.validation.BranchValidation;

public class BranchService {
	static Logger log = Logger.getLogger(BranchService.class.getName());

	BranchDAO branchDAO = new BranchDAO();
	ResponseDTO responseDTO = new ResponseDTO();

	public ResponseDTO addBranch(BranchDTO branchDTO, BranchAccountDTO branchAccountDTO, String userId,
			Connection conn, String ipAddress) {
		String response = "0";

		responseDTO = BranchValidation.validateBranchRequest(branchDTO, branchAccountDTO, conn);

		if (responseDTO.getStatus().equals("0")) {
			return responseDTO;
		}

		String newUserId = new IDGenerator().getBranchId();
		branchDTO.setBranchId(newUserId);
		response = branchDAO.addBranchInfo(branchDTO, userId, conn);
		log.info("response ::" + response);
		if (response.equals("0")) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(BranchErrorCode.WINNABU002.getErrorMessage());
			responseDTO.setErrorCode(BranchErrorCode.WINNABU002.name());
			return responseDTO;
		}

		response = branchDAO.addBranchAddressInfo(branchDTO, userId, conn);

		if (response.equals("0")) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(BranchErrorCode.WINNABU002.getErrorMessage());
			responseDTO.setErrorCode(BranchErrorCode.WINNABU002.name());
			return responseDTO;
		}

		response = branchDAO.addBranchAccountInfo(branchDTO, branchAccountDTO, conn);

		if (response.equals("0")) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(BranchErrorCode.WINNABU002.getErrorMessage());
			responseDTO.setErrorCode(BranchErrorCode.WINNABU002.name());
			return responseDTO;
		}

		Map<String, String> hm = addBranchAudit(conn, branchDTO, branchAccountDTO, userId, ipAddress);

		AuditTrail auditTrail = new AuditTrail();
		auditTrail.addAuditData(conn, userId, branchDTO.getBranchId(), "ADDBRANCH", "ADDBRANCHSUCCESS", hm, ipAddress);
		
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(BranchErrorCode.WINNBBU0011.getErrorMessage());
		responseDTO.setErrorCode(BranchErrorCode.WINNBBU0011.name());
		return responseDTO;

	}

	private Map<String, String> addBranchAudit(Connection conn, BranchDTO branchDTO, BranchAccountDTO branchAccountDTO,
			String userId, String ipAddress) {

		Map<String, String> hm = new HashMap<String, String>();
		
		hm.put("branchId", branchDTO.getBranchId());
		hm.put("bankBranchId", branchDTO.getBankBranchId());
		hm.put("branchName", branchDTO.getBranchName());
		hm.put("contactNumber", branchDTO.getContactNumber());

		hm.put("emailId", branchDTO.getEmailId());
		hm.put("address1", branchDTO.getAddress1());
		hm.put("address2", branchDTO.getAddress2());
		hm.put("city", branchDTO.getCity());
		hm.put("state", branchDTO.getState());
		hm.put("pin", branchDTO.getPin());
		
		hm.put("accountNumber", branchAccountDTO.getAccountNumber());
		hm.put("ifscCode", branchAccountDTO.getIfscCode());
		hm.put("accountType", branchAccountDTO.getAccountType());
		hm.put("bankName", branchAccountDTO.getBankName());
		
		hm.put("branchAddress", branchAccountDTO.getBranchAddress());
		hm.put("panCardDoc", branchAccountDTO.getPanCardDoc());
		hm.put("agreementDoc", branchAccountDTO.getAgreementDoc());
		hm.put("createdBy", userId);
		hm.put("ipAddress", ipAddress);
		
		return hm;
	}

	public ResponseDTO getBranchListForMaker(Connection conn) {
		List<BranchDTO> data = branchDAO.getBranchListForMaker(conn);
		responseDTO.setData(data);
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}

	public ResponseDTO getBranchListForChecker(Connection conn) {
		List<BranchDTO> data = branchDAO.getBranchListForChecker(conn);
		responseDTO.setData(data);
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}

	public ResponseDTO deleteBranchChecker(Connection conn, String branchId, String userId, String ipAddress) {
		ResponseDTO responseDTO = new ResponseDTO();
		branchDAO.deleteBranchChecker(conn, userId, branchId);
		
		Map<String, String> hm = new HashMap<String, String>();
		hm.put("branchId", branchId);
		hm.put("status", "SUCCESS");
		hm.put("approvedBy", userId);
		hm.put("ipAddress", ipAddress);
		
		AuditTrail auditTrail = new AuditTrail();
		auditTrail.addAuditData(conn, userId, branchId, "DELETE-BRANCH", "DELETE-BRANCH-SUCCESS", hm, ipAddress);
		
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(BranchErrorCode.WINNBBU006.getErrorMessage());
		responseDTO.setErrorCode(BranchErrorCode.WINNBBU006.name());
		return responseDTO;
	}

	public ResponseDTO deleteBranchMaker(Connection conn, String branchId, String userId) {
		ResponseDTO responseDTO = new ResponseDTO();
		branchDAO.deleteBranchMaker(conn, userId, branchId);
		
		
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(BranchErrorCode.WINNBBU0015.getErrorMessage());
		responseDTO.setErrorCode(BranchErrorCode.WINNBBU0015.name());
		return responseDTO;
	}

	public ResponseDTO approveBranch(Connection conn, String branchId, String userId, String ipAddress) {
		ResponseDTO responseDTO = new ResponseDTO();

		if (branchDAO.approveBranch(conn, userId, branchId).equals("0")) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(BranchErrorCode.WINNABU005.getErrorMessage());
			responseDTO.setErrorCode(BranchErrorCode.WINNABU005.name());
			return responseDTO;
		}

		String branchEmailId = BranchDAO.getBranchEmailId(branchId, conn);

		if (branchEmailId == null) {
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage(BranchErrorCode.WINNABU005.getErrorMessage());
			responseDTO.setErrorCode(BranchErrorCode.WINNABU005.name());
			return responseDTO;
		}

		String password = PasswordManager.getPasswordSaltString();
		BranchDAO.insertUser(branchId, WINConstants.BRANCH, userId, password, branchEmailId, conn);
		String emailBody = EmailTemplate.getUserEmailBody(branchId, password);
		int emailStatus = new SendMailService().sendMail(branchEmailId, "Created Successfully ", "", emailBody, "");
		log.info("EMAIL STATUS : " + emailStatus);

		Map<String, String> hm = new HashMap<String, String>();
		hm.put("branchId", branchId);
		hm.put("status", "SUCCESS");
		hm.put("approvedBy", userId);
		hm.put("ipAddress", ipAddress);
		
		AuditTrail auditTrail = new AuditTrail();
		auditTrail.addAuditData(conn, userId, branchId, "APPROVE-BRANCH", "APPROVE-BRANCH-SUCCESS", hm, ipAddress);
		
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(BranchErrorCode.WINNBBU0016.getErrorMessage());
		responseDTO.setErrorCode(BranchErrorCode.WINNBBU0016.name());
		return responseDTO;
	}

	public ResponseDTO rejectBranch(Connection conn, String branchId, String userId, String ipAddress, String remark) {
		ResponseDTO responseDTO = new ResponseDTO();
		branchDAO.rejectBranch(conn, userId, branchId, remark);
		
		Map<String, String> hm = new HashMap<String, String>();
		hm.put("branchId", branchId);
		hm.put("status", "SUCCESS");
		hm.put("approvedBy", userId);
		hm.put("ipAddress", ipAddress);
		
		AuditTrail auditTrail = new AuditTrail();
		auditTrail.addAuditData(conn, userId, branchId, "REJECT-BRANCH", "REJECT-BRANCH-SUCCESS", hm, ipAddress);
		
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(BranchErrorCode.WINNBBU007.getErrorMessage());
		responseDTO.setErrorCode(BranchErrorCode.WINNBBU007.name());
		return responseDTO;
	}

	public List<String> getBrancgListForSaleAgent(Connection conn) {
		return branchDAO.getBrancgListForSaleAgent(conn);
	}

	public ResponseDTO getBranchById(String branchId, String header, Connection conn) {
		BranchDTO branch = BranchDAO.getBranchById(branchId, conn);
		//AddressDTO addressDTO=new AddressDAO().getAddressById(conn, branchId);
		AccountDTO accountDTO=new AccountDAO().getAccountById(conn, branchId);
		//ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setBranch(branch);
		//responseDTO.setAddress(addressDTO);
		responseDTO.setAccount(accountDTO);
		responseDTO.setStatus(ResponseDTO.success);
		return responseDTO;
	}
	
	public ResponseDTO updateBranch(BranchDTO branchDTO, AddressDTO addressDTO, AccountDTO accountDTO, String userId,
			Connection conn) {
		//String response = BranchDAO.updateBranch(branchDTO, userId, conn);
		String response = BranchDAO.addEditedBranch(branchDTO, userId, conn);
		if(response.equals("0")){
			responseDTO.setStatus(ResponseDTO.failure);
			responseDTO.setMessage("Branch can not update, please try later.");
			responseDTO.setErrorCode("WINBUBU009");
			return responseDTO;
		}
		
		addressDTO.setResiAddress1(branchDTO.getAddress1());
		addressDTO.setResiAddress2(branchDTO.getAddress2());
		addressDTO.setResiPin(branchDTO.getPin());
		addressDTO.setResiCity(branchDTO.getCity());
		addressDTO.setResiState(branchDTO.getState());
		
		addressDTO.setBusinessAdd1(branchDTO.getAddress1());
		addressDTO.setBusinessAdd2(branchDTO.getAddress2());
		addressDTO.setBusinessPin(branchDTO.getPin());
		addressDTO.setBusinessCity(branchDTO.getCity());
		addressDTO.setBusinessState(branchDTO.getState());
				
		
		addressDTO.setUserId(branchDTO.getBranchId());
		accountDTO.setUserId(branchDTO.getBranchId());
		AddressDAO.addEditedAddress(conn, addressDTO, userId);
		AccountDAO.addEditedAccount(conn, accountDTO, userId);
		
		BranchDAO.updateBranchStatus(branchDTO, userId, conn);
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(BranchErrorCode.WINNBBU0012.getErrorMessage());
		responseDTO.setErrorCode(BranchErrorCode.WINNBBU0012.name());
		return responseDTO;
	}

	public ResponseDTO approveEditedBranch(String branchId, String type, String userId, Connection conn) {
		String result = BranchDAO.approveEditedBranch(branchId, type, userId, conn);
		if(result.equals("1")){
			responseDTO.setStatus(ResponseDTO.success);
			responseDTO.setMessage(BranchErrorCode.WINNBBU0017.getErrorMessage());
			responseDTO.setErrorCode(BranchErrorCode.WINNBBU0017.name());
			return responseDTO;
		}
		else if(result.equals("2")) {
			responseDTO.setStatus(ResponseDTO.success);
			responseDTO.setMessage(BranchErrorCode.WINNBBU007.getErrorMessage());
			responseDTO.setErrorCode(BranchErrorCode.WINNBBU007.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.failure);
		responseDTO.setMessage(BranchErrorCode.WINNABU005.getErrorMessage());
		responseDTO.setErrorCode(BranchErrorCode.WINNABU005.name());
		return responseDTO;
	}

	public String viewEditedDifference(String branchId, String userId, Connection conn) {
		return DifferenceDAO.getBranchEditedDifference(branchId, conn);
		//return null;
	}

	
	
	
}
