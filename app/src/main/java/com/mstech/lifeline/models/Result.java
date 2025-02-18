package com.mstech.lifeline.models;

import com.google.gson.annotations.SerializedName;

public class Result{

	@SerializedName("StatusCode")
	private int statusCode;

	@SerializedName("StatusMessage")
	private String statusMessage;

	public int getStatusCode(){
		return statusCode;
	}

	public String getStatusMessage(){
		return statusMessage;
	}
}