package com.example.kothaijabencd.utils;

public class ReadWriteUserDetails {
    String name, dateOfBirth, contact, address, occupation, gender, religion, nidImage, profileImage;

    public ReadWriteUserDetails(String name, String dateOfBirth, String contact, String address, String occupation, String gender, String religion, String nidImage, String profileImage) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contact = contact;
        this.address = address;
        this.occupation = occupation;
        this.gender = gender;
        this.religion = religion;
        this.nidImage = nidImage;
        this.profileImage = profileImage;
    }

    public ReadWriteUserDetails() {
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

    public String getNidImage() {
        return nidImage;
    }

    public void setNidImage(String nidImage) {
        this.nidImage = nidImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
