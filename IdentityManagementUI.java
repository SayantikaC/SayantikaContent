package com.cg.ibs.im.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import com.cg.ibs.bean.AddressBean;
import com.cg.ibs.bean.ApplicantBean;
import com.cg.ibs.bean.ApplicantBean.ApplicantStatus;
import com.cg.ibs.bean.ApplicantBean.Gender;
import com.cg.ibs.im.exception.IBSCustomException;
import com.cg.ibs.im.service.BankerSeviceImpl;
import com.cg.ibs.im.service.CustomerServiceImpl;

public class IdentityManagementUI {
	static Scanner scanner;

	private CustomerServiceImpl customer = new CustomerServiceImpl();
	private BankerSeviceImpl banker = new BankerSeviceImpl();

	void init() throws IBSCustomException {
		UserMenu choice = null;
		while (UserMenu.QUIT != choice) {
			System.out.println("------------------------");
			System.out.println("Choose your identity from MENU:");
			System.out.println("------------------------");
			for (UserMenu menu : UserMenu.values()) {
				System.out.println((menu.ordinal()) + "\t" + menu);
			}
			System.out.println("Choice");
			int ordinal = scanner.nextInt();

			if (0 <= (ordinal) && UserMenu.values().length > ordinal) {
				choice = UserMenu.values()[ordinal];
				switch (choice) {
				case BANKER:
					selectBankerAction();
					break;
				case CUSTOMER:
					selectCustomerAction();
					break;
				case SERVICE_PROVIDER:
					selectSPAction(); // Group no. 6
					break;
				case QUIT:
					System.out.println("Application closed!!");
					break;
				}
			} else {
				System.out.println("Please enter a valid option.");
				choice = null;
			}

		}

	}

	public void selectBankerAction() throws IBSCustomException {
		if (bankerLogin()) {
			BankerAction choice = null;
			System.out.println("------------------------");
			System.out.println("Choose a valid option");
			System.out.println("------------------------");
			for (BankerAction menu : BankerAction.values()) {
				System.out.println(menu.ordinal() + "\t" + menu);
			}
			System.out.println("Choices:");
			int ordinal = scanner.nextInt();

			if (0 <= ordinal && BankerAction.values().length > ordinal) {
				choice = BankerAction.values()[ordinal];
				switch (choice) {
				case VIEW_PENDING_DETAILS:
					pendingApplications();
					break;
				case VIEW_APPROVED_DETAILS:
					approvedApplications();
					break;
				case VIEW_DENIED_DETAILS:
					deniedApplications();
					break;
				case QUIT:
					System.out.println("BACK ON HOME PAGE!!");
					break;
				}
			} else {
				System.out.println("Please enter a valid option.");
				choice = null;
			}
		}
	}

	public void selectCustomerAction() throws IBSCustomException {
		CustomerMenu choice = null;
		System.out.println("------------------------");
		System.out.println("Choose an appropriate option from MENU:");
		System.out.println("------------------------");
		for (CustomerMenu menu : CustomerMenu.values()) {
			System.out.println(menu.ordinal() + "\t" + menu);
		}
		System.out.println("Choice");
		int ordinal = scanner.nextInt();

		if (0 <= ordinal && UserMenu.values().length > ordinal) {
			choice = CustomerMenu.values()[ordinal];
			switch (choice) {
			case SIGNUP:
				signUp();
				break;
			case LOGIN:
				login();
				break;
			case CHECK_STATUS:
				checkStatus();
				break;
			case GO_BACK:
				System.out.println();
				break;
			}
		} else {
			System.out.println("Please enter a valid option.");
			choice = null;
		}

	}

	public void selectSPAction() {
		System.out.println("Out of Scope!!!!!!!!!"); // LATER~~
	}

	void pendingApplications() throws IBSCustomException {
		Set<Long> pendingList = banker.viewPendingApplications();
		if (pendingList.size() > 0) {
			Iterator<Long> iterator = pendingList.iterator();
			while (iterator.hasNext()) {
				System.out.println(iterator.next());
			}
		} else {
			System.out.println("There are no pending applicant requests.");
		}

		System.out.println("Enter an application number to check details:");
		long applicantId = scanner.nextLong();
		// banker.displayDetails(applicantId);
		// Display all details

		String confirmation = "no";
		while (confirmation.toLowerCase().equals("no")) {
			System.out.println("------------------------");
			System.out.println("Choose valid option:");
			System.out.println("------------------------");
			System.out.println("1.\tApprove application");
			System.out.println("2.\tDeny application");
			int choice = scanner.nextInt();
			while (choice != 1 || choice != 2) {
				System.out.println("Please enter a valid choice");
				choice = scanner.nextInt();
			}

			System.out.println("Are you sure?\n1. yes\n2.no");
			confirmation = scanner.next();
			while (confirmation.toLowerCase() != "yes" && confirmation.toLowerCase() != "no") {
				System.out.println("Please enter a valid choice");
				confirmation = scanner.next();
			}
			if (confirmation.toLowerCase().equals("yes")) {
				if (choice == 1) {
					banker.updateStatus(applicantId, ApplicantStatus.APPROVED);
					String uci = banker.createNewCustomer(applicantId);
					banker.createNewAccount(uci);
				} else if (choice == 2) {
					banker.updateStatus(applicantId, ApplicantStatus.DENIED);
				}

			} else {
				// to do when confirmation is no
			}
			confirmation = confirmation.toLowerCase();
		}
	}

	void approvedApplications() {
		Set<Long> approvedList = banker.viewApprovedApplications();
		if (approvedList.size() > 0) {
			Iterator<Long> iterator = approvedList.iterator();
			while (iterator.hasNext()) {
				System.out.println(iterator.next());
			}
		} else {
			System.out.println("There are no approved applications.");
		}
	}

	void deniedApplications() {
		Set<Long> deniedList = banker.viewDeniedApplications();
		if (deniedList.size() > 0) {
			Iterator<Long> iterator = deniedList.iterator();
			while (iterator.hasNext()) {
				System.out.println(iterator.next());
			}
		} else {
			System.out.println("There are no denied applications.");
		}
	}

	public void signUp() throws IBSCustomException {
		System.out.println("Do you want to open an individual account or Joint account?");
		// Functions for individual/joint account
		// Set account type

		ApplicantBean applicant = new ApplicantBean();

		System.out.println("Enter the following Details:");
		System.out.println("Enter the first name");
		String firstName = scanner.next();
		while(!customer.verifyName(firstName)){
			System.out.println("Please enter an appropriate name");
			firstName=scanner.next("[a-zA-z ]+");
		}
		applicant.setFirstName(firstName);

		System.out.println("Enter the last name");
		String lastName = scanner.next();
		while(!customer.verifyName(lastName)){
			System.out.println("Please enter an appropriate name");
			lastName=scanner.next();
		}
		applicant.setLastName(lastName);

		System.out.println("Enter Father's name");
		String fatherName = scanner.next();
		while(!customer.verifyName(fatherName)){
			System.out.println("Please enter an appropriate name");
			fatherName=scanner.next();
		}
		applicant.setFatherName(fatherName);

		System.out.println("Enter Mother's name");
		String motherName = scanner.next();
		while(!customer.verifyName(motherName)){
			System.out.println("Please enter an appropriate name");
			motherName=scanner.next();
		}
		applicant.setMotherName(motherName);

		System.out.println("Enter your Date of Birth in DD-MM-YYYY format");
		String date = scanner.next();
		//if format of date is invalid, ask for DOB again.
		DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate dt = LocalDate.parse(date, dtFormat);
		applicant.setDob(dt);
		
		// Enter Gender
		Gender genderChoice = null;
		System.out.println("Choose your gender from the menu:");
		for (Gender menu : Gender.values()) {
			System.out.println(menu.ordinal() + "\t" + menu);
		}
		System.out.println("Choice");
		int gender = scanner.nextInt();

		if (0 <= gender && Gender.values().length > gender) {
			genderChoice = Gender.values()[gender];
			switch (genderChoice) {
			case MALE:
				applicant.setGender(Gender.MALE);
				break;
			case FEMALE:
				applicant.setGender(Gender.FEMALE);
				break;
			case OTHERS:
				applicant.setGender(Gender.OTHERS);
				break;
			}
		} else {
			System.out.println("Please enter a valid option.");
			genderChoice = null;
		}

		// Permanent Address
		AddressBean address = addAddress();
		applicant.setPermanentAddress(address);
		
		// Current Address
		System.out.println("Is your current address same as permanent address?\n1. yes\n2. no");
		int addressSame = scanner.nextInt();
		while(addressSame!=1 && addressSame!=2){
			System.out.println("Please enter a valid choice. Is your current address same as"
					+ " permanent address?\1. yes\n2. no");
			addressSame = scanner.nextInt();
		}
		
		if(addressSame==1){
			applicant.setCurrentAddress(address);
		} else if (addressSame == 2){
			address = addAddress();
			applicant.setCurrentAddress(address);
		}
		
		System.out.println("Enter Mobile number");
		String mobileNumber = scanner.next();
		while(!customer.verifyMobileNumber(mobileNumber)){
			System.out.println("Please enter an appropriate phone number");
			mobileNumber=scanner.next();
		}
		applicant.setMobileNumber(mobileNumber);
		

		System.out.println("Enter Alternate Mobile Number");
		String alternateMobileNumber = scanner.next();
		while(!customer.verifyMobileNumber(alternateMobileNumber)){
			System.out.println("Please enter an appropriate phone number");
			alternateMobileNumber=scanner.next();
		}
		applicant.setMobileNumber(alternateMobileNumber);
		
		System.out.println("Enter email id");
		//verify email id
		applicant.setEmailId(scanner.next());

		System.out.println("Enter Aadhar Number");
		String aadharNumber = scanner.next();
		while(!customer.verifyAadharNumber(aadharNumber)){
			System.out.println("Please enter an appropriate aadhar number");
			aadharNumber=scanner.next();
		}
		applicant.setAadharNumber(aadharNumber);

		System.out.println("Enter Pan Number");
		String panNumber = scanner.next();
		while(!customer.verifyPanNumber(panNumber)){
			System.out.println("Please enter an appropriate PAN number");
			panNumber=scanner.next();
		}
		applicant.setPanNumber(panNumber);

		System.out.println("Upload two Government ID proofs");
		// Upload documents

		// Submit application
		System.out.println("Your application has been sent to the bank.");
		
		applicant.setApplicationDate(LocalDate.now());
		applicant.setApplicantStatus(ApplicantStatus.PENDING);
		
		customer.saveApplicantDetails(applicant);
		System.out.println("Keep updated with your status.\nYour applicant "
								+ "id is"+ applicant.getApplicantId());
		

	}
	
	public AddressBean addAddress(){
		System.out.println("Enter your permanent address:");
		AddressBean address = new AddressBean();
		System.out.println("House Number:");
		address.setHouseNumber(scanner.next());
		System.out.println("Landmark:");
		address.setLandmark(scanner.next());
		System.out.println("Area:");
		//verification that it doesn't take an empty input
		address.setArea(scanner.next());
		System.out.println("City:");
		address.setCity(scanner.next());
		System.out.println("State:");
		address.setState(scanner.next());
		System.out.println("Country:");
		address.setCountry(scanner.next());
		System.out.println("Pincode:");
		String pinCode = scanner.next();
		while(!customer.verifyPincode(pinCode)){
			System.out.println("Please enter an appropriate pincode");
			pinCode=scanner.next();
		}
		address.setPincode(pinCode);
		return address;
	}

	public void login() throws IBSCustomException {

		System.out.println("Please enter the username"); // Login using UCI
		String user = scanner.next();
		System.out.println("Enter the password");
		String password = scanner.next();
		if (customer.login(user, password)) {
			System.out.println("Welcome to the Home Page!!");
		} else {
			System.out.println("INVALID DETAILS! Enter the details again.");
			login();
		}
	}

	public void checkStatus() throws IBSCustomException {
		System.out.println("Enter the applicant ID to check status:");
		long applicantId = scanner.nextLong();
		while (!customer.verifyApplicantId(applicantId)) {
			System.out.println("Please enter a valid applicant ID");
			applicantId = scanner.nextLong();
		}
		ApplicantStatus status = customer.checkStatus(applicantId);
		System.out.println("Your application status is: " + status);

	}

	public boolean bankerLogin() {
		System.out.println("Enter a login ID:");
		String bankUser = scanner.next();
		System.out.println("Enter password:");
		String bankPassword = scanner.next();
		if (!banker.verifyLogin(bankUser, bankPassword)) {
			System.out.println("Please enter valid details");
			bankerLogin();
		}
		return true;
	}

	public static void main(String[] args) throws IBSCustomException {

		scanner = new Scanner(System.in);
		IdentityManagementUI identityManagement = new IdentityManagementUI();
		identityManagement.init();
		scanner.close();

	}
}
