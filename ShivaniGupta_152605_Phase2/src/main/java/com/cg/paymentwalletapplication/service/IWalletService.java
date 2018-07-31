package com.cg.paymentwalletapplication.service;

import com.cg.paymentwalletapplication.dto.CustomerBean;
import com.cg.paymentwalletapplication.exception.PaymentWalletException;

public interface IWalletService {
	public String createAccount(CustomerBean customerBean);

	public CustomerBean showBalance(String custContact);

	public boolean withdrawAmount(double withdrawAmt, String custContact);

	public boolean depositAmount(double depositAmt, String custContact);

	public boolean fundTransfer(String senderCont, String receiverCont, double custBalance)
			throws PaymentWalletException;

	public StringBuilder printTransactions(String mob);

	public boolean validateDetails(CustomerBean customerBean) throws PaymentWalletException;

	public CustomerBean login(String id, String password) throws PaymentWalletException;
}
