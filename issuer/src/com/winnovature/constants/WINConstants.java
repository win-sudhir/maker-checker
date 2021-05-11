package com.winnovature.constants;

public class WINConstants {
	//checker status
	public static String NEW = "NEW";
	public static String ACTIVE = "ACTIVE";
	public static String DELETE = "DELETE";
	public static String DEATCIVATE = "DEATCIVATE";
	public static String PARENT = "ADMIN";
	public static String APPROVE = "APPROVE";
	public static String ATCIVATE = "ATCIVATE";
	public static String REJECT = "REJECT";
	//maker status
	public static String ACTPENDING = "ACTPENDING";
	public static String DELPENDING = "DELPENDING";
	public static String UPPENDING = "UPPENDING";
	public static String REJPENDING = "UPPENDING";
	
	//status messages
	public static String NEWREQ = "Creation Request Initiated.";
	public static String REJREQ = "Reject Request Initiated.";
	public static String DELREQ = "Delete Request Initiated.";
	public static String UPREQ = "Update Request Initiated.";
	public static String ACTREQ = "Activation Request Initiated.";
	
	public static String NEWAPP = "Creation Request Approved.";
	public static String REJAPP = "Reject Request Approved.";
	public static String DELAPP = "Delete Request Approved.";
	public static String UPAPP = "Update Request Approved.";
	public static String ACTAPP = "Activation Request Approved.";
	
	public static String NEWREJ = "Creation Request Rejected.";
	public static String REJREJ = "Reject Request Rejected.";
	public static String DELREJ = "Delete Request Rejected.";
	public static String UPREJ = "Update Request Rejected.";
	public static String ACTREJ = "Activation Request Rejected.";
	
	//roles for maker and checker
	public static String MAKERROLEID = "10";
	public static String CHECKERROLEID = "11";
	public static String CUSTOMERROLEID = "2";
	public static String AGENTROLEID = "7";
	public static String BRANCHROLEID = "6";
	public static String localBLURL = "http://localhost:8080/issuer/tag/excemanagement";
	
	public static String SUPERADMIN="1";		
	public static String ADMIN="2";				
	public static String FASTAGMAKER="3";		
	public static String FASTAGCHECKER="4";		
	public static String BRANCH="5";			
	public static String AGENT="6";				
	public static String BRANCHMAKER="7";		
	public static String BRANCHCHECKER="8";		
	public static String SUPPLIER="9";			
	public static String CUSTOMER="10";			
	public static String RECONMAKER="11";		
	public static String RECONCHECKER="12";		
	public static String DISPUTEMAKER="13";		
	public static String DISPUTECHECKER="14";
}
