package com.mstech.lifeline.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponse{

	@SerializedName("Details")
	private Details details;

	@SerializedName("Result")
	private Result result;

	public Details getDetails(){
		return details;
	}

	public Result getResult(){
		return result;
	}
}