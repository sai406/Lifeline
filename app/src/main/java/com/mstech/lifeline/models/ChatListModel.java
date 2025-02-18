package com.mstech.lifeline.models;

public class ChatListModel {

    String CustomerId;
    String fname;
    String lname;
    String Mobile;
    String EmailId;
    String Password;
    String NewPassword;
    String Ministry;
    String ReferrerName;
    String SalvationInfo;
    String Profession;
    String CustomerImagePath;

    public ChatListModel() {
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

    public String getMinistry() {
        return Ministry;
    }

    public void setMinistry(String ministry) {
        Ministry = ministry;
    }

    public String getReferrerName() {
        return ReferrerName;
    }

    public void setReferrerName(String referrerName) {
        ReferrerName = referrerName;
    }

    public String getSalvationInfo() {
        return SalvationInfo;
    }

    public void setSalvationInfo(String salvationInfo) {
        SalvationInfo = salvationInfo;
    }

    public String getCustomerImagePath() {
        return CustomerImagePath;
    }

    public void setCustomerImagePath(String customerImagePath) {
        CustomerImagePath = customerImagePath;
    }

    public String getProfession() {
        return Profession;
    }

    public void setProfession(String profession) {
        Profession = profession;
    }
}
