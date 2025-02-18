package com.mstech.lifeline.models;

import com.google.gson.annotations.SerializedName;

public class Details{

	@SerializedName("IsFriend")
	private int isFriend;

	@SerializedName("ReferredById")
	private int referredById;

	@SerializedName("Latitude")
	private double latitude;

	@SerializedName("Gender")
	private int gender;

	@SerializedName("Createdstring")
	private String createdstring;

	@SerializedName("ModifiedDate")
	private String modifiedDate;

	@SerializedName("CommunityBelong")
	private Object communityBelong;

	@SerializedName("MemberId")
	private int memberId;

	@SerializedName("CoordinatorNumber")
	private String coordinatorNumber;

	@SerializedName("RequestSent")
	private int requestSent;

	@SerializedName("GeoAddress")
	private String geoAddress;

	@SerializedName("MemberInfo")
	private String memberInfo;

	@SerializedName("ProfilePic")
	private String  profilePic;

	@SerializedName("TotalRecords")
	private int totalRecords;

	@SerializedName("CoordinatorId")
	private int coordinatorId;

	@SerializedName("HelpLineNumber")
	private String helpLineNumber;

	@SerializedName("Modifiedstring")
	private String modifiedstring;

	@SerializedName("CountryId")
	private int countryId;

	@SerializedName("Status")
	private int status;

	@SerializedName("EmailId")
	private Object emailId;

	@SerializedName("IsCoordinator")
	private int isCoordinator;

	@SerializedName("FirstName")
	private String firstName;

	@SerializedName("CustomerImagePath")
	private String customerImagePath;

	@SerializedName("LocationId")
	private int locationId;

	@SerializedName("Mobile")
	private Object mobile;

	@SerializedName("Longitude")
	private double longitude;

	@SerializedName("Pin")
	private String pin;

	@SerializedName("UserId")
	private String userId;

	@SerializedName("CreatedDate")
	private String createdDate;

	@SerializedName("LastName")
	private String lastName;

	@SerializedName("PostCode")
	private Object postCode;

	@SerializedName("Location")
	private Object location;

	@SerializedName("RequestStatus")
	private Object requestStatus;

	public int getIsFriend(){
		return isFriend;
	}

	public int getReferredById(){
		return referredById;
	}

	public double getLatitude(){
		return latitude;
	}

	public int getGender(){
		return gender;
	}

	public String getCreatedstring(){
		return createdstring;
	}

	public String getModifiedDate(){
		return modifiedDate;
	}

	public Object getCommunityBelong(){
		return communityBelong;
	}

	public int getMemberId(){
		return memberId;
	}

	public String getCoordinatorNumber(){
		return coordinatorNumber;
	}

	public int getRequestSent(){
		return requestSent;
	}

	public String getGeoAddress(){
		return geoAddress;
	}

	public String getMemberInfo(){
		return memberInfo;
	}

	public String getProfilePic(){
		return profilePic;
	}

	public int getTotalRecords(){
		return totalRecords;
	}

	public int getCoordinatorId(){
		return coordinatorId;
	}

	public String getHelpLineNumber(){
		return helpLineNumber;
	}

	public String getModifiedstring(){
		return modifiedstring;
	}

	public int getCountryId(){
		return countryId;
	}

	public int getStatus(){
		return status;
	}

	public Object getEmailId(){
		return emailId;
	}

	public int getIsCoordinator(){
		return isCoordinator;
	}

	public String getFirstName(){
		return firstName;
	}

	public String getCustomerImagePath(){
		return customerImagePath;
	}

	public int getLocationId(){
		return locationId;
	}

	public Object getMobile(){
		return mobile;
	}

	public double getLongitude(){
		return longitude;
	}

	public String getPin(){
		return pin;
	}

	public String getUserId(){
		return userId;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public String getLastName(){
		return lastName;
	}

	public Object getPostCode(){
		return postCode;
	}

	public Object getLocation(){
		return location;
	}

	public Object getRequestStatus(){
		return requestStatus;
	}
}