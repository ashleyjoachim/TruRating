package com.franciscoandrade.truerating.model;


public class InspectionResultsModel {
    private String action;
    private String boro;
    private String building;
    private String camis;
    private String critical_flag;
    private String cuisine_description;
    private String dba;
    private String grade;
    private String grade_date;
    private String inspection_date;
    private String inspection_type;
    private String phone;
    private String record_date;
    private String score;
    private String street;
    private String violation_code;
    private String violation_description;
    private String zipcode;

    public InspectionResultsModel(String dba) {
        this.dba = dba;
    }

    public String getAction() {
        return action;
    }

    public String getBoro() {
        return boro;
    }

    public String getBuilding() {
        return building;
    }

    public String getCamis() {
        return camis;
    }

    public String getCritical_flag() {
        return critical_flag;
    }

    public String getCuisine_description() {
        return cuisine_description;
    }

    public String getDba() {
        return dba;
    }

    public String getGrade() {
        return grade;
    }

    public String getGrade_date() {
        return grade_date;
    }

    public String getInspection_date() {
        return inspection_date;
    }

    public String getInspection_type() {
        return inspection_type;
    }

    public String getPhone() {
        return phone;
    }

    public String getRecord_date() {
        return record_date;
    }

    public String getScore() {
        return score;
    }

    public String getStreet() {
        return street;
    }

    public String getViolation_code() {
        return violation_code;
    }

    public String getViolation_description() {
        return violation_description;
    }

    public String getZipcode() {
        return zipcode;
    }
}
