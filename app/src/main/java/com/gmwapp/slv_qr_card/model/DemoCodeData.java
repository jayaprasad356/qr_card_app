package com.gmwapp.slv_qr_card.model;

public class DemoCodeData {
    private Integer id;
    private String company_name;
    private String city;
    private String country;
    private String website;
    private String email;
    private String zip_code;
    private String business_id;

    // Constructor
    public DemoCodeData(Integer id, String company_name, String city, String country, String website, String email, String zip_code, String business_id) {
        this.id = id;
        this.company_name = company_name;
        this.city = city;
        this.country = country;
        this.website = website;
        this.email = email;
        this.zip_code = zip_code;
        this.business_id = business_id;
    }

    public DemoCodeData() {

    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return company_name;
    }

    public void setCompanyName(String company_name) {
        this.company_name = company_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZipCode() {
        return zip_code;
    }

    public void setZipCode(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getBusinessId() {
        return business_id;
    }

    public void setBusinessId(String business_id) {
        this.business_id = business_id;
    }
}

