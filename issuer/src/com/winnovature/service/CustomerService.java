package com.winnovature.service;

import java.sql.Connection;
import java.util.List;

import org.apache.log4j.Logger;

import com.winnovature.constants.IDGenerator;
import com.winnovature.constants.WINConstants;
import com.winnovature.dao.AccountDAO;
import com.winnovature.dao.AddressDAO;
import com.winnovature.dao.CustomerDAO;
import com.winnovature.dao.DifferenceDAO;
import com.winnovature.dao.KycDAO;
import com.winnovature.dao.VehicleDAO;
import com.winnovature.dto.AccountDTO;
import com.winnovature.dto.AddressDTO;
import com.winnovature.dto.CustomerDTO;
import com.winnovature.dto.KycDTO;
import com.winnovature.dto.ResponseDTO;
import com.winnovature.dto.VehicleDTO;
import com.winnovature.dto.VehicleDTO.Vehicles;
import com.winnovature.utils.EmailTemplate;
import com.winnovature.utils.PasswordManager;
import com.winnovature.utils.SendMailService;
import com.winnovature.validation.CustomerErrorCode;
import com.winnovature.validation.CustomerValidation;

public class CustomerService {
	static Logger log = Logger.getLogger(CustomerService.class.getName());
	public static ResponseDTO addCustomer(Connection conn, CustomerDTO customerDTO, AddressDTO addressDTO, AccountDTO accountDTO, KycDTO kycDTO, VehicleDTO vehicleDTO, String userId) {
		ResponseDTO responseDTO = new ResponseDTO();
		//Place validation here 
		responseDTO = CustomerValidation.validateCustomerRequest(customerDTO, addressDTO, accountDTO, kycDTO, vehicleDTO, conn);
		if(responseDTO.getStatus().equals(ResponseDTO.failure)) {
			return responseDTO;
		}
		//end
		String customerId = new IDGenerator().getCustomerId();
		String walletId = new IDGenerator().getWalletId();
		log.info("CustomerId : "+customerId);
		log.info("WalletId : "+walletId);
		customerDTO.setUserId(customerId);
		customerDTO.setWalletId(walletId);
		addressDTO.setUserId(customerId);
		accountDTO.setUserId(customerId);
		kycDTO.setUserId(customerId);
		kycDTO.setAddressProofDocPath(kycDTO.getAddressProofDoc());
		kycDTO.setIdProofDocPath(kycDTO.getIdProofDoc());
		CustomerDAO.addCustomer(conn, customerDTO, userId);
		AddressDAO.addAddress(conn, addressDTO);
		AccountDAO.addAccount(conn, accountDTO, userId);
		KycDAO.addKYC(conn, kycDTO, userId);
		VehicleDAO.addVehicle(conn, vehicleDTO, customerId);
		//Email//////////////////////////////////////
		String password = PasswordManager.getPasswordSaltString();
		CustomerDAO.insertUser(customerId, WINConstants.CUSTOMER,userId,password,customerDTO.getEmailId(),conn);
		String emailBody = EmailTemplate.getEmailBody(customerId, password);
		int emailStatus = new SendMailService().sendMail(customerDTO.getEmailId(),"Created Successfully ", "", emailBody, "");
		log.info("EMAIL STATUS : "+emailStatus);
		/////////////////////////////////////////////
		///////////////////////SENDIND SMS///////////////////////////////////////
		String smsContent = "Your FASTag wallet account has been created at Tollpay system successfully and credentials are sent on your registered emailId.";
		TransactionService.sendSMS(customerDTO.getContactNumber(), smsContent);
		//////////////////////////////////////////////////////////////////////////
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(CustomerErrorCode.WINNCBU001.getErrorMessage());
		responseDTO.setErrorCode(CustomerErrorCode.WINNCBU001.name());
		/*
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setErrorCode("WINCUBU0001");
		responseDTO.setMessage("Customer added successfully.");
		*/
		return responseDTO;
	}
	
	public static ResponseDTO getSingleCustomer(Connection conn, String customerId) {
		//, CustomerDTO customerDTO, AddressDTO addressDTO, AccountDTO accountDTO, KycDTO kycDTO, VehicleDTO vehicleDTO,
		CustomerDTO customer=new CustomerDAO().getOneCustomers(conn, customerId); 
		System.out.println(customer);
		AddressDTO addressDTO=new AddressDAO().getAddressById(conn, customerId);
		AccountDTO accountDTO=new AccountDAO().getAccountById(conn, customerId);
		KycDTO kycDTO=new KycDAO().getKycById(conn, customerId);
		List<Vehicles> vehicles=new VehicleDAO().getVehicleById(conn, customerId);
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setCustomer(customer);
		responseDTO.setAccount(accountDTO);
		responseDTO.setAddress(addressDTO);	
		responseDTO.setKyc(kycDTO);
		responseDTO.setVehicles(vehicles);
		
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setErrorCode("WINCUBU000");
		//responseDTO.setMessage("Customer added successfully.");
		return responseDTO;
	}
	
	public static ResponseDTO getCustomerByIdForChecker(Connection conn, String customerId) {
		//, CustomerDTO customerDTO, AddressDTO addressDTO, AccountDTO accountDTO, KycDTO kycDTO, VehicleDTO vehicleDTO,
		//CustomerDTO customer=new CustomerDAO().getOneCustomers(conn, customerId);  
		//System.out.println(customer);
		//AddressDTO addressDTO=new AddressDAO().getAddressById(conn, customerId);
		//AccountDTO accountDTO=new AccountDAO().getAccountById(conn, customerId);
		//KycDTO kycDTO=new KycDAO().getKycById(conn, customerId);
		//List<Vehicles> vehicles=new VehicleDAO().getVehicleById(conn, customerId);   
		
		
		CustomerDTO customer=new CustomerDAO().getCustomerForChecker(conn, customerId); 
		System.out.println(customer);
		AddressDTO addressDTO=new AddressDAO().getAddressByIdForChecker(conn, customerId);
		AccountDTO accountDTO=new AccountDAO().getAccountByIdForChecker(conn, customerId);
		KycDTO kycDTO=new KycDAO().getKycByIdForChecker(conn, customerId);
		List<Vehicles> vehicles=new VehicleDAO().getVehicleByIdForChecker(conn, customerId);   
		
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setCustomer(customer);
		responseDTO.setAccount(accountDTO);
		responseDTO.setAddress(addressDTO);	
		responseDTO.setKyc(kycDTO);
		responseDTO.setVehicles(vehicles);
		
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setErrorCode("WINCUBU000");
		//responseDTO.setMessage("Customer added successfully.");
		return responseDTO;
	}

	public static ResponseDTO deleteCustomer(Connection conn, String customerId, String userId, String type) {
		ResponseDTO responseDTO = new ResponseDTO();
		if(type.equalsIgnoreCase("APPROVE")) {
			new CustomerDAO().deleteCustomer(conn, userId, customerId);
			responseDTO.setMessage(CustomerErrorCode.WINNCBU007.getErrorMessage());
			responseDTO.setErrorCode(CustomerErrorCode.WINNCBU007.name());
		}else if(type.equalsIgnoreCase("REJECT")) {
			new CustomerDAO().deleteRejectCustomer(conn, userId, customerId);
			responseDTO.setMessage(CustomerErrorCode.WINNCBU008.getErrorMessage());
			responseDTO.setErrorCode(CustomerErrorCode.WINNCBU008.name());
		}
		responseDTO.setErrorCode("WINCUBU0003");
		return responseDTO;
	}
	
	public static ResponseDTO deleteCustomerRequest(Connection conn, String customerId, String userId) {
		ResponseDTO responseDTO = new ResponseDTO();
		new CustomerDAO().deleteCustomerRequest(conn, userId, customerId);
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(CustomerErrorCode.WINNCBU005.getErrorMessage());
		responseDTO.setErrorCode(CustomerErrorCode.WINNCBU005.name());
		return responseDTO;
	}
	
	public static ResponseDTO updateCustomer(Connection conn, CustomerDTO customerDTO, AddressDTO addressDTO, AccountDTO accountDTO, KycDTO kycDTO, VehicleDTO vehicleDTO, String userId) {
		ResponseDTO responseDTO = new ResponseDTO();
		
		CustomerDAO.updateCustomer(conn, customerDTO, userId);
		AddressDAO.updateAddress(conn, addressDTO, userId);
		AccountDAO.updateAccount(conn, accountDTO, userId);
		KycDAO.updateKYC(conn, kycDTO, userId);
		VehicleDAO.updateVehicle(conn, vehicleDTO, customerDTO.getUserId());
		
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setErrorCode("WINCUBU0002");
		responseDTO.setMessage("Customer updated successfully.");
		return responseDTO;
	}
	
	public static ResponseDTO editCustomer(Connection conn, CustomerDTO customerDTO, AddressDTO addressDTO, AccountDTO accountDTO, KycDTO kycDTO, VehicleDTO vehicleDTO, String userId) {
		ResponseDTO responseDTO = new ResponseDTO();
		
		//CustomerDAO.updateCustomer(conn, customerDTO, userId); 
		//AddressDAO.updateAddress(conn, addressDTO, userId);
		//AccountDAO.updateAccount(conn, accountDTO, userId);
		//KycDAO.updateKYC(conn, kycDTO, userId);
		//VehicleDAO.updateVehicle(conn, vehicleDTO, customerDTO.getUserId());
		
		CustomerDAO.addEditedCustomer(conn, customerDTO, userId);
		AddressDAO.addEditedAddress(conn, addressDTO, userId);
		AccountDAO.addEditedAccount(conn, accountDTO, userId);
		//KycDAO.addEditedKYC(conn, kycDTO, userId);
		//VehicleDAO.addEditedVehicle(conn, vehicleDTO, customerDTO.getUserId());
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setMessage(CustomerErrorCode.WINNCBU002.getErrorMessage());
		responseDTO.setErrorCode(CustomerErrorCode.WINNCBU002.name());
		
		return responseDTO;
	}
	
	public static ResponseDTO addVehicle(Connection conn, VehicleDTO vehicleDTO, String userId, String customerId) {
		ResponseDTO responseDTO = new ResponseDTO();
		log.info("CustomerId : "+customerId);
		VehicleDAO.addVehicle(conn, vehicleDTO, customerId);
		responseDTO.setStatus(ResponseDTO.success);
		responseDTO.setErrorCode("WINVEHBU0001");
		responseDTO.setMessage("Vehicle added successfully.");
		return responseDTO;
	}
	
	public static ResponseDTO approveCustomer(Connection conn, String customerId, String userId, String type, String remark) {
		ResponseDTO responseDTO = new ResponseDTO();
		String smsContent = null;
		if(type.equalsIgnoreCase("APPROVE")) {
			new CustomerDAO().approveCustomer(conn, userId, customerId);
			responseDTO.setStatus(ResponseDTO.success);
			responseDTO.setMessage(CustomerErrorCode.WINNCBU006.getErrorMessage());
			responseDTO.setErrorCode(CustomerErrorCode.WINNCBU006.name());
			smsContent = "Your FASTag wallet has been approved and account converted into full KYC.";
		} else if(type.equalsIgnoreCase("REJECT")) {
			new CustomerDAO().rejectCustomer(conn, userId, customerId, remark);
			responseDTO.setStatus(ResponseDTO.success);
			responseDTO.setMessage(CustomerErrorCode.WINNCBU009.getErrorMessage());
			responseDTO.setErrorCode(CustomerErrorCode.WINNCBU009.name());
			smsContent = "Your FASTag wallet has been rejected.";
		}
		///////////////////////SENDIND SMS///////////////////////////////////////
		CustomerDTO customerDTO = new CustomerDAO().geCustomersWalletInfo(conn, customerId);
		TransactionService.sendSMS(customerDTO.getContactNumber(), smsContent);
		//////////////////////////////////////////////////////////////////////////
		return responseDTO;
	}
	
	public static CustomerDTO getCustomerWalletInfo(Connection conn, String customerId) {
		CustomerDTO customerDTO = new CustomerDAO().geCustomersWalletInfo(conn, customerId);
		return customerDTO;
	}

	public static ResponseDTO approveEditedCustomer(String customerId, String type, String userId, Connection conn) {
		String result = new CustomerDAO().approveEditedCustomer(customerId, type, userId, conn);
		ResponseDTO responseDTO = new ResponseDTO();
		if(result.equals("1")){
			responseDTO.setStatus(ResponseDTO.success);
			responseDTO.setMessage(CustomerErrorCode.WINNCBU0010.getErrorMessage());
			responseDTO.setErrorCode(CustomerErrorCode.WINNCBU0010.name());
			return responseDTO;
		}
		else if(result.equals("2")) {
			responseDTO.setStatus(ResponseDTO.success);
			responseDTO.setMessage(CustomerErrorCode.WINNCBU0011.getErrorMessage());
			responseDTO.setErrorCode(CustomerErrorCode.WINNCBU0011.name());
			return responseDTO;
		}
		responseDTO.setStatus(ResponseDTO.failure);
		responseDTO.setMessage(CustomerErrorCode.WINNCBU0012.getErrorMessage());
		responseDTO.setErrorCode(CustomerErrorCode.WINNCBU0012.name());
		return responseDTO;
	}

	public static String viewEditedDifference(String customerId, String userId, Connection conn) {
		return DifferenceDAO.getCustomerEditedDifference(customerId, conn);
		//return null;
	}
}
		
