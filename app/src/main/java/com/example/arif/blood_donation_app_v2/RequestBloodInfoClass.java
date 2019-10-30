package com.example.arif.blood_donation_app_v2;

public class RequestBloodInfoClass {

    private String patient_name, hospital_name, hospital_address, bed_number, phone_number , blood_group , date;

    public RequestBloodInfoClass(){

    }




    public RequestBloodInfoClass(String patient_name, String hospital_name, String hospital_address, String bed_number, String phone_number, String blood_group, String date) {
        this.patient_name = patient_name;
        this.hospital_name = hospital_name;
        this.hospital_address = hospital_address;
        this.bed_number = bed_number;
        this.phone_number = phone_number;
        this.blood_group = blood_group;
        this.date = date;



    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getHospital_address() {
        return hospital_address;
    }

    public void setHospital_address(String hospital_address) {
        this.hospital_address = hospital_address;
    }

    public String getBed_number() {
        return bed_number;
    }

    public void setBed_number(String bed_number) {
        this.bed_number = bed_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
