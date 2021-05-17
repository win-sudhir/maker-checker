package com.winnovature.validation;

public enum CustomerErrorCode {
	WINNBU000("Customer added successfully."),
	WINNBU001("Request parameters are missing."),
//	WINNBU002("Request headers are missing."),
//	WINNBU003("Request headers are missing."),
//	WINNBU004("Request headers are missing."),
//	WINNBU005("Request headers are missing."),
	
	WINNCBU001("Customer creation request initiated, Please wait for approval."),
	WINNCBU002("Customer updation request initiated, Please wait for approval."),
	WINNCBU003("Customer approval request initiated, Please wait for approval."),
	WINNCBU004("Customer rejection request initiated, Please wait for approval."),
	WINNCBU005("Customer deletion request initiated, Please wait for approval."),
		
	WINNCBU006("Customer creation request approved successfully."),
	WINNCBU007("Customer delete request approved successfully."),
	WINNCBU008("Customer delete request rejected successfully."),
	WINNCBU009("Customer creation request rejected successfully."),
	WINNCBU0010("Customer update request approved successfully."),
	WINNCBU0011("Customer update request rejected successfully."),
	WINNCBU0012("Something went wrong, Please try again."),
	
	WINNBU0019("EmailId is already present in customer."),
	WINNBU0020("Mobile number is already present in customer."),
	WINNBU0021("Address proof number is already present in customer."),
	WINNBU0022("Pan number is already present in customer."),
	WINNBU0023("Vehicle number is already present."),
	WINNBU0030("Failed.");
	
	private String errorMessage;
	
	CustomerErrorCode(String errorMessage){
		this.errorMessage=errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
