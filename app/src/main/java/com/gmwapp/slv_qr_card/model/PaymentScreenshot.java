package com.gmwapp.slv_qr_card.model;

public class PaymentScreenshot {
    String id,user_id,image,status,datetime;
    public PaymentScreenshot(){

    }

    public PaymentScreenshot(String id, String user_id, String image, String status, String datetime, String type) {
        this.id = id;
        this.user_id = user_id;
        this.image = image;
        this.status = status;
        this.datetime = datetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
