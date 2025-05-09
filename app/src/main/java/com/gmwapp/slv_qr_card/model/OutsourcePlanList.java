package com.gmwapp.slv_qr_card.model;

public class OutsourcePlanList {
    private String id;
    private String name;
    private String description;
    private String image;
    private String demo_video;
    private String monthly_codes;
    private String monthly_earnings;
    private String yearly_earnings;
    private String per_code_cost;
    private String price;
    private String type;
    private String min_refers;
    private String num_sync;
    private String sub_description;
    private String active_link;
    private String sync_cost;
    private Integer status;

    // Constructor, getters, and setters
    public OutsourcePlanList(
            String id,
            String name,
            String description,
            String image,
            String demo_video,
            String monthly_codes,
            String monthly_earnings,
            String yearly_earnings,
            String per_code_cost,
            String price,
            String type,
            String min_refers,
            String num_sync,
            String sub_description,
            String active_link,
            String sync_cost,
            Integer status
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.demo_video = demo_video;
        this.monthly_codes = monthly_codes;
        this.monthly_earnings = monthly_earnings;
        this.yearly_earnings = yearly_earnings;
        this.per_code_cost = per_code_cost;
        this.price = price;
        this.type = type;
        this.num_sync = num_sync;
        this.sub_description = sub_description;
        this.active_link = active_link;
        this.min_refers = min_refers;
        this.sync_cost = sync_cost;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDemo_video() {
        return demo_video;
    }

    public void setDemo_video(String demo_video) {
        this.demo_video = demo_video;
    }

    public String getMonthly_codes() {
        return monthly_codes;
    }

    public void setMonthly_codes(String monthly_codes) {
        this.monthly_codes = monthly_codes;
    }

    public String getMonthly_earnings() {
        return monthly_earnings;
    }

    public void setMonthly_earnings(String monthly_earnings) {
        this.monthly_earnings = monthly_earnings;
    }

    public String getYearly_earnings() {
        return yearly_earnings;
    }

    public void setYearly_earnings(String yearly_earnings) {
        this.yearly_earnings = yearly_earnings;
    }

    public String getPer_code_cost() {
        return per_code_cost;
    }

    public void setPer_code_cost(String per_code_cost) {
        this.per_code_cost = per_code_cost;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMin_refers() {
        return min_refers;
    }

    public void setMin_refers(String min_refers) {
        this.min_refers = min_refers;
    }

    public String getNum_sync() {
        return num_sync;
    }

    public void setNum_sync(String num_sync) {
        this.num_sync = num_sync;
    }

    public String getSub_description() {
        return sub_description;
    }

    public void setSub_description(String sub_description) {
        this.sub_description = sub_description;
    }

    public String getActive_link() {
        return active_link;
    }

    public void setActive_link(String active_link) {
        this.active_link = active_link;
    }

    public String getSync_cost() {
        return sync_cost;
    }

    public void setSync_cost(String sync_cost) {
        this.sync_cost = sync_cost;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

