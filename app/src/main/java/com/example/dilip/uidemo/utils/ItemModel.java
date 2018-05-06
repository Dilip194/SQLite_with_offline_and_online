package com.example.dilip.uidemo.utils;

import android.graphics.Bitmap;

public class ItemModel {

    private int id;
    private String specialization;
    private String descriptions;
    private String image_path;
    private Bitmap image;

    public ItemModel(int id, String specialization, String descriptions, Bitmap image) {
        this.id = id;
        this.specialization = specialization;
        this.descriptions = descriptions;
        this.image = image;
    }

    public ItemModel() {
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
