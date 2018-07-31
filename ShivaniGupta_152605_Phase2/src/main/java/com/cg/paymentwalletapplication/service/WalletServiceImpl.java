package com.cg.paymentwalletapplication.service;

import com.cg.paymentwalletapplication.dao.IWalletDao;
import com.cg.paymentwalletapplication.dao.WalletDaoImpl;
import com.cg.paymentwalletapplication.dto.CustomerBean;
import com.cg.paymentwalletapplication.exception.IPaymentWalletException;
import com.cg.paymentwalletapplication.exception.PaymentWalletException;

public class WalletServiceImpl implements IWalletService {

	private static IWalletDao dao = null;
	
	static {
		dao=new WalletDaoImpl();
	}

	public String createAccount(CustomerBean customerBean) {
		return dao.createAccount(customerBean);
	}
	
	public CustomerBean login(String mobileNum, String password) throws PaymentWalletException {
		return dao.login(mobileNum, password);
	}

	public CustomerBean showBalance(String custContact) {
		return dao.showBalance(custContact);
	}

	public boolean withdrawAmount(double withdrawAmt, String custContact) {
		return dao.withdrawAmount(withdrawAmt, custContact);
	}

	public boolean depositAmount(double depositAmt, String custContact) {
		return dao.depositAmount(depositAmt, custContact);
	}

	public boolean fundTransfer(String senderCont, String receiverCont, double custBalance)
			throws PaymentWalletException {
		return dao.fundTransfer(senderCont, receiverCont, custBalance);
	}

	public StringBuilder printTransactions(String mob) {
		return dao.printTransactions(mob);
	}

	public boolean validateDetails(CustomerBean customerBean) throws PaymentWalletException {
		boolean result = false;
		String regex = "[A-Z]{1}[a-z]+";
		String regex2 = "^[6-9]{1}[0-9]{9}$";
		String regex3 = "[a-z]{1}[a-z0-9_.]*@gmail.com";
		String regex4 = "[0-9]{2}";
		if (customerBean.getName().matches(regex)) {
			if (customerBean.getMobileNo().matches(regex2)) {
				if (customerBean.getEmailId().matches(regex3)) {
					if (String.valueOf(customerBean.getAge()).matches(regex4)) {
						if (customerBean.getGender().matches("male") || customerBean.getGender().matches("female") || customerBean.getGender().matches("m") || customerBean.getGender().matches("f")) {
							result = true;
						} else
							throw new PaymentWalletException(IPaymentWalletException.ERROR5);
					} else
						throw new PaymentWalletException(IPaymentWalletException.ERROR4);
				} else
					throw new PaymentWalletException(IPaymentWalletException.ERROR3);
			} else
				throw new PaymentWalletException(IPaymentWalletException.ERROR2);
		} else
			throw new PaymentWalletException(IPaymentWalletException.ERROR1);
		return result;
	}
}
