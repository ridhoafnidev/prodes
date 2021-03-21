package com.mrii.prodes.repository.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCategory {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("added_date")
    @Expose
    private String addedDate;
    @SerializedName("added_user_id")
    @Expose
    private String addedUserId;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;
    @SerializedName("updated_user_id")
    @Expose
    private String updatedUserId;
    @SerializedName("updated_flag")
    @Expose
    private String updatedFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddedUserId() {
        return addedUserId;
    }

    public void setAddedUserId(String addedUserId) {
        this.addedUserId = addedUserId;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedUserId() {
        return updatedUserId;
    }

    public void setUpdatedUserId(String updatedUserId) {
        this.updatedUserId = updatedUserId;
    }

    public String getUpdatedFlag() {
        return updatedFlag;
    }

    public void setUpdatedFlag(String updatedFlag) {
        this.updatedFlag = updatedFlag;
    }
}
