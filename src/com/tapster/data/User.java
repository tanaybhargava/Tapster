package com.tapster.data;

public class User
{
	@com.google.gson.annotations.SerializedName("id")
	private String mId;
	
	@com.google.gson.annotations.SerializedName("firstName")
	private String firstName;
	
	@com.google.gson.annotations.SerializedName("lastName")
	private String lastName;

	@com.google.gson.annotations.SerializedName("mobile")
	private String mobile;
	
	@com.google.gson.annotations.SerializedName("email")
	private String email;
	
	@com.google.gson.annotations.SerializedName("userid")
	private String userid;
	
}
