package com.mrii.prodes.repository.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DefaultIcon {
    @SerializedName("img_id")
    @Expose
    private String imgId;
    @SerializedName("img_parent_id")
    @Expose
    private String imgParentId;
    @SerializedName("img_type")
    @Expose
    private String imgType;
    @SerializedName("img_path")
    @Expose
    private String imgPath;
    @SerializedName("img_width")
    @Expose
    private String imgWidth;
    @SerializedName("img_height")
    @Expose
    private String imgHeight;
    @SerializedName("img_desc")
    @Expose
    private String imgDesc;

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getImgParentId() {
        return imgParentId;
    }

    public void setImgParentId(String imgParentId) {
        this.imgParentId = imgParentId;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(String imgWidth) {
        this.imgWidth = imgWidth;
    }

    public String getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(String imgHeight) {
        this.imgHeight = imgHeight;
    }

    public String getImgDesc() {
        return imgDesc;
    }

    public void setImgDesc(String imgDesc) {
        this.imgDesc = imgDesc;
    }
}
