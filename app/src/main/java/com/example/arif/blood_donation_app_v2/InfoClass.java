package com.example.arif.blood_donation_app_v2;

import android.os.Parcel;
import android.os.Parcelable;

public class InfoClass {
    private String name , email, phoneNumber , bloodGroup , donateFrequency , disease , password, state , country;
    private Double latitute , longitute,distance;

    public InfoClass(){

    }




    public InfoClass(String name, String email, String phoneNumber, String bloodGroup, String donateFrequency, String disease, String password , String state , String counrty , Double latitute , Double longitute, Double distance) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.bloodGroup = bloodGroup;
        this.donateFrequency = donateFrequency;
        this.disease = disease;
        this.password = password;
        this.state = state;
        this.country = counrty;
        this.latitute=latitute;
        this.longitute= longitute;
        this.distance = distance;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getDonateFrequency() {
        return donateFrequency;
    }

    public void setDonateFrequency(String donateFrequency) { this.donateFrequency = donateFrequency; }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }
    public Double getLatitute() {
        return latitute;
    }

    public void setLatitute(Double latitute) {
        this.latitute = latitute;
    }

    public Double getLongitute() {
        return longitute;
    }

    public void setLongitute(Double longitute) {
        this.longitute = longitute;
    }
    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

}

