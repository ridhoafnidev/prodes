package com.mrii.prodes.model;

import com.google.gson.annotations.SerializedName;

public class UmkmResponse {
    @SerializedName("status")
    private Integer status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private UmkmModel data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UmkmModel getData() {
        return data;
    }

    public void setData(UmkmModel data) {
        this.data = data;
    }
}
