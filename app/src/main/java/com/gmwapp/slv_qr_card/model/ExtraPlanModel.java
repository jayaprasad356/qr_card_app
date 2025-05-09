package com.gmwapp.slv_qr_card.model;

public class ExtraPlanModel {
    private String id;
    private String name;
    private String description;
    private String price;
    private String status;

    // Constructor, getters, and setters
    public ExtraPlanModel(
            String id,
            String name,
            String description,
            String price,
            String status
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
