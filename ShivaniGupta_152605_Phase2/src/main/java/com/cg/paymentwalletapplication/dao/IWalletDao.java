package com.cg.paymentwalletapplication.dao;

import com.cg.paymentwalletapplication.dto.CustomerBean;
import com.cg.paymentwalletapplication.exception.PaymentWalletException;

public interface IWalletDao {
	public String createAccount(CustomerBean customerBean);

	public CustomerBean showBalance(String custContact);

	public boolean withdrawAmount(double withdrawAmt, String custContact);

	public boolean depositAmount(double depositAmt, String custContact);

	public boolean fundTransfer(String senderCont, String receiverCont, double custAmount)
			throws PaymentWalletException;

	public StringBuilder printTransactions(String mob);

	public CustomerBean login(String mobileNum, String password) throws PaymentWalletException;

	public CustomerBean getCustomerDetails(String mob);
}
