package com.example.kothaijabencd.utils;

public class ReadWriterRiderDetails {
    String name, dateOfBirth, contact, address, occupation, gender, religion, create_date, transport, bikeInfo, userToken;
    int user_role, active_flag;

    public ReadWriterRiderDetails() {
    }

    public ReadWriterRiderDetails(String name, String dateOfBirth, String contact, String address, String occupation, String gender, String religion, String create_date, String transport, String bikeInfo, String userToken, int user_role, int active_flag) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contact = contact;
        this.address = address;
        this.occupation = occupation;
        this.gender = gender;
        this.religion = religion;
        this.create_date = create_date;
        this.transport = transport;
        this.bikeInfo = bikeInfo;
        this.userToken = userToken;
        this.user_role = user_role;
        this.active_flag = active_flag;
    }

    public ReadWriterRiderDetails(String userToken) {
        this.userToken = userToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getBikeInfo() {
        return bikeInfo;
    }

    public void setBikeInfo(String bikeInfo) {
        this.bikeInfo = bikeInfo;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public int getUser_role() {
        return user_role;
    }

    public void setUser_role(int user_role) {
        this.user_role = user_role;
    }

    public int getActive_flag() {
        return active_flag;
    }

    public void setActive_flag(int active_flag) {
        this.active_flag = active_flag;
    }
}
