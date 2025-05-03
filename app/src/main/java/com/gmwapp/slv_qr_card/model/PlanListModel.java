package com.gmwapp.slv_qr_card.model;

public class PlanListModel {
    private String id;
    private String name;
    private String description;
    private String image;
    private String demo_video;
    private String invite_bonus;
    private String price;
    private String num_sync;
    private int enable;

    // Constructor, getters, and setters
    public PlanListModel(
            String id,
            String name,
            String description,
            String image,
            String demo_video,
            String invite_bonus,
            String price,
            String num_sync,
            int enable
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.demo_video = demo_video;
        this.invite_bonus = invite_bonus;
        this.price = price;
        this.num_sync = num_sync;
        this.enable = enable;
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

    public String getInvite_bonus() {
        return invite_bonus;
    }

    public void setInvite_bonus(String invite_bonus) {
        this.invite_bonus = invite_bonus;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum_sync() {
        return num_sync;
    }

    public void setNum_sync(String num_sync) {
        this.num_sync = num_sync;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }
}
