package com.cg.paymentwalletapplication.dto;

public class CustomerBean extends Wallet{
	private String name;
	private int age;
	private String emailId;
	private String password;
	private String mobileNo;
	private String gender;
	private Wallet wallet;

	public CustomerBean() {
	}

	public CustomerBean(String name, int age, String emailId, String password, String mobileNo,
			String gender, Wallet wallet) {
		super();
		this.name = name;
		this.age = age;
		this.emailId = emailId;
		this.password = password;
		this.mobileNo = mobileNo;
		this.gender = gender;
		this.wallet = wallet;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	@Override
	public String toString() {
		return "CustomerBean [name=" + name + ", age=" + age + ", emailId=" + emailId + ", password=" + password + ", mobileNo=" + mobileNo + ", gender=" + gender + ", wallet=" + wallet
				+ "]";
	}

}
