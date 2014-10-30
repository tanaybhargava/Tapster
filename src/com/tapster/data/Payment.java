package com.tapster.data;

public class Payment
{
	@com.google.gson.annotations.SerializedName("id")
	private String mId;
	
	@com.google.gson.annotations.SerializedName("userid")
	private String userid;
	
	@com.google.gson.annotations.SerializedName("creditcard")
	private String creditcard;

	@com.google.gson.annotations.SerializedName("cvv")
	private String cvv;
	
	@com.google.gson.annotations.SerializedName("expirymm")
	private int expiryMM;
	
	@com.google.gson.annotations.SerializedName("expiryyy")
	private int expiryYY;

	public Payment(String userid, String creditcard, String cvv,
			int expiryMonth, int expiryYear)
	{
		super();
		this.userid = userid;
		this.creditcard = creditcard;
		this.cvv = cvv;
		this.expiryMM = expiryMonth;
		this.expiryYY = expiryYear;
	}
}
