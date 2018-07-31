package com.cg.paymentwalletapplication.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import com.cg.paymentwalletapplication.dto.CustomerBean;
import com.cg.paymentwalletapplication.exception.IPaymentWalletException;
import com.cg.paymentwalletapplication.exception.PaymentWalletException;
import com.cg.paymentwalletapplication.util.DBUtil;

public class WalletDaoImpl implements IWalletDao {
	
	private static Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	static {
		try {
			connection=new DBUtil().getConnection();
		} catch (ClassNotFoundException e) {
			try {
				throw new PaymentWalletException(IPaymentWalletException.MESSAGE1);
			} catch (PaymentWalletException paymentWalletException) {
				System.out.println(paymentWalletException.getMessage());
			}
		} catch (IOException e) {
			try {
				throw new PaymentWalletException(IPaymentWalletException.MESSAGE1);
			} catch (PaymentWalletException paymentWalletException) {
				System.out.println(paymentWalletException.getMessage());
			}
		}
	}

	public String createAccount(CustomerBean customerBean) {
		String sql = "INSERT into customerDetails values (?, ?, ?, ?, ?, ?, ?)";
		try {
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setString(1, customerBean.getMobileNo());
			preparedStatement.setString(2, customerBean.getName());
			preparedStatement.setInt(3, customerBean.getAge());
			preparedStatement.setString(4, customerBean.getEmailId());
			preparedStatement.setString(5, customerBean.getPassword());
			preparedStatement.setString(6, customerBean.getGender());
			preparedStatement.setDouble(7, customerBean.getBalance());
			preparedStatement.executeUpdate();
		} catch (SQLException sqlException) {
			System.out.println(sqlException.getMessage());
		}
		String phone = customerBean.getMobileNo();
		return phone;
	}
	public CustomerBean getCustomerDetails(String mob) {
		CustomerBean search = null;
		String sql = "SELECT * FROM customerDetails WHERE custContact = ?";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, mob);
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				String mobileNo = resultSet.getString("custContact");
				String name = resultSet.getString("custName");
				String password=resultSet.getString("password");
				int age=resultSet.getInt("custAge");
				String gender=resultSet.getString("custGender");
				String email = resultSet.getString("custEmail");
				double balance = resultSet.getDouble("custBalance");
				search = new CustomerBean();
				
				search.setName(name);
				search.setMobileNo(mobileNo);
				search.setAge(age);
				search.setPassword(password);
				search.setGender(gender);
				search.setEmailId(email);
				search.setBalance(balance);
			}
		} catch (SQLException sqlException) {
			System.out.println(sqlException.getMessage());
		}
		return search;
	}
	public CustomerBean showBalance(String custContact) {
		CustomerBean search = null;
		String sql = "SELECT custName,custContact,custBalance FROM customerDetails WHERE custContact = ?";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, custContact);
			resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				String mobileNo = resultSet.getString("custContact");
				String name = resultSet.getString("custName");
				double balance = resultSet.getDouble("custBalance");
				search = new CustomerBean();
				
				search.setName(name);
				search.setMobileNo(mobileNo);
				search.setBalance(balance);
			}
		} catch (SQLException sqlException) {
			System.out.println(sqlException.getMessage());
		}
		
		return search;
	}

	public boolean withdrawAmount(double withdrawAmt, String custContact) {
		boolean result = false;
		CustomerBean bean=getCustomerDetails(custContact);
		if (bean!=null) {
			if (bean.getBalance()>withdrawAmt) {
				String sql = "UPDATE customerDetails set custBalance = custBalance-? where custContact = ?";
				bean.setBalance(bean.getBalance()-withdrawAmt);
				
				try {
					PreparedStatement pstmt = connection.prepareStatement(sql);
					pstmt.setDouble(1, withdrawAmt);
					pstmt.setString(2, custContact);
					String transactions = "Withdrawn: " + withdrawAmt;
					String QueryT = "Insert into transaction values("+custContact+",'"+transactions+"')";
					PreparedStatement pstmt1 = connection.prepareStatement(QueryT);
					pstmt1.executeUpdate();
					result = pstmt.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				result = true;
			}
		}
		return result;
	}

	public boolean depositAmount(double depositAmt, String custContact) {
		
		boolean result = false;
		CustomerBean bean=getCustomerDetails(custContact);
		if (bean!=null) {
			String sql = "UPDATE customerDetails set custBalance = custBalance+? where custContact = ?";
			bean.setBalance(bean.getBalance()+depositAmt);
			
			try {
				PreparedStatement pstmt = connection.prepareStatement(sql);
				pstmt.setDouble(1, depositAmt);
				pstmt.setString(2, custContact);
				String transactions = "Deposited: " + depositAmt;
				String QueryT = "INSERT into transaction values("+custContact+",'"+transactions+"')";
				PreparedStatement pstmt1 = connection.prepareStatement(QueryT);
				pstmt1.executeUpdate();
				result = pstmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			result = true;
		}
		return result;
	}

	public boolean fundTransfer(String senderCont, String receiverCont, double custAmount)
			throws PaymentWalletException {
		boolean result = false;
		CustomerBean sender=getCustomerDetails(senderCont);
		CustomerBean receiver=getCustomerDetails(receiverCont);
		try {
			connection = DBUtil.getConnection();
			if(sender!=null && receiver!=null) {
			if(sender.getBalance()>custAmount) {
				String RQuery = "SELECT * from customerDetails where custContact=?";
				String SQuery = "UPDATE customerDetails SET custBalance = custBalance-? where custContact=?";

				PreparedStatement recieverPstmt = connection.prepareStatement(RQuery);
				PreparedStatement senderPstmt = connection.prepareStatement(SQuery);

				recieverPstmt.setString(1, receiverCont);
				senderPstmt.setDouble(1, custAmount);
				senderPstmt.setString(2, senderCont);
				String transactions = "Transferred: " + custAmount + " to "+ receiverCont ;
				String QueryT = "INSERT into transaction values("+senderCont+",'"+transactions+"')";
				PreparedStatement pstmt1 = connection.prepareStatement(QueryT);
				pstmt1.executeUpdate();
				
				ResultSet rs = recieverPstmt.executeQuery();
				while (rs.next()) {
					String recieverQuery1 = "UPDATE customerDetails SET custBalance= custBalance+? where custContact=?";
					PreparedStatement recieverPstmt1 = connection.prepareStatement(recieverQuery1);
					
					recieverPstmt1.setDouble(1, custAmount);
					recieverPstmt1.setString(2, receiverCont);
					recieverPstmt1.executeUpdate();
					senderPstmt.executeUpdate();
				
					String transaction1 = "Received: " + custAmount + " from "+ senderCont;
					String QueryR = "INSERT into transaction values("+receiverCont+",'"+transaction1+"')";
					PreparedStatement pstmt2 = connection.prepareStatement(QueryR);
					pstmt2.executeUpdate();
					result = true;
			}
		}else {
			result=false;
			} 
		}
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
			return result;
	}

	public CustomerBean login(String mobileNo, String password) throws PaymentWalletException {
		CustomerBean custLogin = getCustomerDetails(mobileNo);
		
		if (custLogin.getMobileNo().equals(mobileNo) && custLogin.getPassword().equals(password)) {
			return custLogin;
		} else
			throw new PaymentWalletException(IPaymentWalletException.ERROR7);
	}

	public StringBuilder printTransactions(String mobileNumber) {
		String sql = "SELECT * from transaction where custContact = ?";
		String transactions = null;
		StringBuilder allTransactions=new StringBuilder();
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, mobileNumber);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				transactions = resultSet.getString("transactions");
				allTransactions.append(transactions+"\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allTransactions;
	}
}