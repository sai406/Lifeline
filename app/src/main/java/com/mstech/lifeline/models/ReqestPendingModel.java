package com.mstech.lifeline.models;

/** HARISH GADDAM */

public class ReqestPendingModel {

    private String CustomerId;
    private String FirstName;
    private String LastName;
    private String Mobile;
    private String EmailId;
    private String CustomerImagePath;
    private String IsFriend;
    private String Status;
    private String Requestsent;
    private String RequestStatus;

    public ReqestPendingModel(String customerId, String firstName, String lastName, String mobile, String emailId, String customerImagePath, String isFriend, String status, String requestsent, String requestStatus) {
        CustomerId = customerId;
        FirstName = firstName;
        LastName = lastName;
        Mobile = mobile;
        EmailId = emailId;
        CustomerImagePath = customerImagePath;
        IsFriend = isFriend;
        Status = status;
        Requestsent = requestsent;
        RequestStatus = requestStatus;
    }

    public ReqestPendingModel() {

    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getCustomerImagePath() {
        return CustomerImagePath;
    }

    public void setCustomerImagePath(String customerImagePath) {
        CustomerImagePath = customerImagePath;
    }

    public String getIsFriend() {
        return IsFriend;
    }

    public void setIsFriend(String isFriend) {
        IsFriend = isFriend;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRequestsent() {
        return Requestsent;
    }

    public void setRequestsent(String requestsent) {
        Requestsent = requestsent;
    }

    public String getRequestStatus() {
        return RequestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        RequestStatus = requestStatus;
    }
}
