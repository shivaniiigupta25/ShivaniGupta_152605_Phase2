package com.cg.paymentwalletapplication.ui;

import java.util.Scanner;

import com.cg.paymentwalletapplication.dto.CustomerBean;
import com.cg.paymentwalletapplication.exception.PaymentWalletException;
import com.cg.paymentwalletapplication.service.WalletServiceImpl;
import com.cg.paymentwalletapplication.service.IWalletService;

public class Client {

	public static void main(String[] args) {
		IWalletService service = new WalletServiceImpl();
		Scanner scanner = new Scanner(System.in);
		int choice = 0;

		do {
			System.out.println(" ");
			System.out.println("********************Payment Wallet Application********************");
			System.out.println("1. Create Account");
			System.out.println("2. Login");
			System.out.println("3. Exit Application");
			System.out.println("******************************************************************");
			choice = scanner.nextInt();
			switch (choice) {
			case 1:
				System.out.println("Enter your Name");
				String name = scanner.next();
				System.out.println("Enter your Mobile Number");
				String mobileNum = scanner.next();
				System.out.println("Enter Your Age");
				int age = scanner.nextInt();
				System.out.println("Enter your Email ID");
				String emailId = scanner.next();
				System.out.println("Enter your Gender");
				String gender = scanner.next().trim().toLowerCase();

				System.out.println("Set your Password");
				String password = scanner.next();
				System.out.println("Enter the Initial Amount to be Deposited");
				double balance = scanner.nextDouble();

				CustomerBean customerBean = new CustomerBean();
				customerBean.setName(name);
				customerBean.setMobileNo(mobileNum);
				customerBean.setEmailId(emailId);
				customerBean.setAge(age);
				customerBean.setGender(gender);
				customerBean.setPassword(password);
				customerBean.setBalance(balance);

				try {
					if (service.validateDetails(customerBean)) {
						String contact = service.createAccount(customerBean);
						System.out.println("Account created successfully with contact " + contact);
					} else
						System.out.println("Account cannot be created...");
				} catch (PaymentWalletException paymentWalletException) {
					System.out.println(paymentWalletException.getMessage());
				}

				break;

			case 2:
				System.out.println("Enter your Mobile no.");
				String mobileNo = scanner.next();
				System.out.println("Enter your Password");
				String pass = scanner.next();
				CustomerBean loginCustomer;
				try {
					loginCustomer = service.login(mobileNo, pass);
					login(loginCustomer);
				} catch (PaymentWalletException paymentWalletException) {
					System.out.println(paymentWalletException.getMessage());
				}

				break;

			case 3:
				System.out.println("****************************Thank You******************************");
				break;

			default:
				System.out.println("Wrong choice!!! Try Again!!!");
				break;
			}
		} while (choice != 3);
		scanner.close();
	}

	private static void login(CustomerBean customerBean) {
		int choiceLogin = 0;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Welcome... " + customerBean.getName() + " You are successfully logged in.... ");
		do {
			System.out.println(" ");
			System.out.println("********************Payment Wallet Application********************");
			System.out.println("1. Show Balance");
			System.out.println("2. Withdraw Amount");
			System.out.println("3. Deposit Amount");
			System.out.println("4. Fund Tansfer");
			System.out.println("5. Print Transactions history");
			System.out.println("6. Log Out");
			System.out.println("*******************************************************************");
			choiceLogin = scanner.nextInt();
			IWalletService service = new WalletServiceImpl();
			String custContact = customerBean.getMobileNo();

			switch (choiceLogin) {
			case 1:
				CustomerBean bean = new CustomerBean();
				bean = service.showBalance(custContact);
				System.out.println("Your Account Balance is " + bean.getBalance());
				break;

			case 2:
				System.out.println("Enter the Amount to be Withdrawn");
				double withdrawAmount = scanner.nextDouble();
				System.out.println("Enter the Mobile Number");
				String mobNum = scanner.next();
				if (service.withdrawAmount(withdrawAmount, mobNum)) {
					System.out.println("Rs. " + withdrawAmount + " Withdrawn from your Wallet...");
					CustomerBean bean2 = service.showBalance(mobNum);
					System.out.println("Updated Account Balance is Rs. " + bean2.getBalance());
				} else
					System.out.println("Withdrawal failed due to Insufficient Balance....");

				break;

			case 3:
				System.out.println("Enter the Amount to be Deposited...");
				double depositAmount = scanner.nextDouble();
				System.out.println("Enter the Mobile Number");
				String mobNum1 = scanner.next();
				if (service.depositAmount(depositAmount, mobNum1)) {
					System.out.println("Rs. " + depositAmount + " deposited to your Wallet...");
					CustomerBean bean3 = service.showBalance(mobNum1);
					System.out.println("Updated Balance is Rs. " + bean3.getBalance());
				} else
					System.out.println("Can't deposit amount...");
				break;

			case 4:
				System.out.println("Enter the Sender Mobile Number...");
				String senderMobNum = scanner.next();
				System.out.println("Enter the Receiver Mobile Number...");
				String receiverMobNum = scanner.next();
				System.out.println("Enter the Amount to be Transfered...");
				double transferAmount = scanner.nextDouble();
				try {
					if (service.fundTransfer(senderMobNum, receiverMobNum, transferAmount)) {
						System.out.println("Rs. " + transferAmount + " Succesfully Transfered to " + receiverMobNum);
						CustomerBean bean4 = service.showBalance(senderMobNum);
						System.out.println("Updated Balance in Sender's Account is Rs. " + bean4.getBalance());
						bean4 = service.showBalance(receiverMobNum);
						System.out.println("Updated Balance in Receiver's Account is Rs. " + bean4.getBalance());
					}
				} catch (PaymentWalletException paymentWalletException) {
					System.out.println(paymentWalletException.getMessage());
				}
				break;

			case 5:
				System.out.println("Enter the Mobile Number...");
				String mob = scanner.next();

				StringBuilder transaction = service.printTransactions(mob);
				System.out.println(transaction);

				break;

			case 6:
				System.out.println("********************Logged Out Successfully********************");
				break;

			default:
				System.out.println("Wrong Choice!!! Try Again...");
				break;
			}
		} while (choiceLogin >= 1 && choiceLogin<=6);
		scanner.close();
	}
}
