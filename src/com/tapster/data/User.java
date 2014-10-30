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

	public String getmId()
	{
		return mId;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getUserid()
	{
		return userid;
	}

	public void setUserid(String userid)
	{
		this.userid = userid;
	}
	
	
	
}
