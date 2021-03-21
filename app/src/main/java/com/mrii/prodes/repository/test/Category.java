package com.mrii.prodes.repository.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("cat_ordering")
    @Expose
    private String catOrdering;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("added_date")
    @Expose
    private String addedDate;
    @SerializedName("default_icon")
    @Expose
    private DefaultIcon defaultIcon;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatOrdering() {
        return catOrdering;
    }

    public void setCatOrdering(String catOrdering) {
        this.catOrdering = catOrdering;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public DefaultIcon getDefaultIcon() {
        return defaultIcon;
    }

    public void setDefaultIcon(DefaultIcon defaultIcon) {
        this.defaultIcon = defaultIcon;
    }
}
