package com.cg.paymentwalletapplication.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.cg.paymentwalletapplication.exception.IPaymentWalletException;
import com.cg.paymentwalletapplication.exception.PaymentWalletException;

public class DBUtil {
	public static Connection getConnection() throws ClassNotFoundException, IOException {
		Properties props = new Properties();
		FileInputStream in = new FileInputStream("db.properties");
		props.load(in);
		in.close();
		String driver = props.getProperty("driver");
		if (driver != null) {
			Class.forName(driver);
		}
		String url = props.getProperty("url");
		String username = props.getProperty("username");
		String password = props.getProperty("password");
		Connection con = null;
		try {
			con = DriverManager.getConnection(url, username, password);
		} catch (SQLException sqlException) {
			sqlException.getMessage();
			try {
				throw new PaymentWalletException(IPaymentWalletException.MESSAGE1);
			} catch (PaymentWalletException paymentWalletException) {
				System.out.println(paymentWalletException.getMessage());
			}
		}
		return con;
	}
}
