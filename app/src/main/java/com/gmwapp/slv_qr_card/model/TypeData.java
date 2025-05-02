package com.gmwapp.slv_qr_card.model;

public class TypeData {
    private String id, name;
    private boolean isSelected; // New field

    public TypeData() {
    }

    public TypeData(String id, String name) {
        this.id = id;
        this.name = name;
        this.isSelected = false; // Default to not selected
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}


