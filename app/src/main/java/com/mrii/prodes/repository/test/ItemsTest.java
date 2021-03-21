package com.mrii.prodes.repository.test;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mrii.prodes.viewobject.ItemCategory;

public class ItemsTest {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("sub_cat_id")
    @Expose
    private String subCatId;
    @SerializedName("item_type_id")
    @Expose
    private String itemTypeId;
    @SerializedName("item_price_type_id")
    @Expose
    private String itemPriceTypeId;
    @SerializedName("item_currency_id")
    @Expose
    private String itemCurrencyId;
    @SerializedName("item_location_id")
    @Expose
    private String itemLocationId;
    @SerializedName("condition_of_item_id")
    @Expose
    private String conditionOfItemId;
    @SerializedName("deal_option_remark")
    @Expose
    private String dealOptionRemark;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("highlight_info")
    @Expose
    private String highlightInfo;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("deal_option_id")
    @Expose
    private String dealOptionId;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("business_mode")
    @Expose
    private String businessMode;
    @SerializedName("is_sold_out")
    @Expose
    private String isSoldOut;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
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
    @SerializedName("touch_count")
    @Expose
    private String touchCount;
    @SerializedName("favourite_count")
    @Expose
    private String favouriteCount;
    @SerializedName("is_paid")
    @Expose
    private String isPaid;
    @SerializedName("added_date_str")
    @Expose
    private String addedDateStr;
    @SerializedName("paid_status")
    @Expose
    private String paidStatus;
    @SerializedName("photo_count")
    @Expose
    private String photoCount;
    @SerializedName("default_photo")
    @Expose
    private DefaultPhoto defaultPhoto;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("sub_category")
    @Expose
    private SubCategory subCategory;
    @SerializedName("condition_of_item")
    @Expose
    private ConditionOfItem conditionOfItem;
    @SerializedName("is_favourited")
    @Expose
    private String isFavourited;
    @SerializedName("is_owner")
    @Expose
    private String isOwner;

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

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(String itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getItemPriceTypeId() {
        return itemPriceTypeId;
    }

    public void setItemPriceTypeId(String itemPriceTypeId) {
        this.itemPriceTypeId = itemPriceTypeId;
    }

    public String getItemCurrencyId() {
        return itemCurrencyId;
    }

    public void setItemCurrencyId(String itemCurrencyId) {
        this.itemCurrencyId = itemCurrencyId;
    }

    public String getItemLocationId() {
        return itemLocationId;
    }

    public void setItemLocationId(String itemLocationId) {
        this.itemLocationId = itemLocationId;
    }

    public String getConditionOfItemId() {
        return conditionOfItemId;
    }

    public void setConditionOfItemId(String conditionOfItemId) {
        this.conditionOfItemId = conditionOfItemId;
    }

    public String getDealOptionRemark() {
        return dealOptionRemark;
    }

    public void setDealOptionRemark(String dealOptionRemark) {
        this.dealOptionRemark = dealOptionRemark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHighlightInfo() {
        return highlightInfo;
    }

    public void setHighlightInfo(String highlightInfo) {
        this.highlightInfo = highlightInfo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDealOptionId() {
        return dealOptionId;
    }

    public void setDealOptionId(String dealOptionId) {
        this.dealOptionId = dealOptionId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBusinessMode() {
        return businessMode;
    }

    public void setBusinessMode(String businessMode) {
        this.businessMode = businessMode;
    }

    public String getIsSoldOut() {
        return isSoldOut;
    }

    public void setIsSoldOut(String isSoldOut) {
        this.isSoldOut = isSoldOut;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
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

    public String getTouchCount() {
        return touchCount;
    }

    public void setTouchCount(String touchCount) {
        this.touchCount = touchCount;
    }

    public String getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(String favouriteCount) {
        this.favouriteCount = favouriteCount;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public String getAddedDateStr() {
        return addedDateStr;
    }

    public void setAddedDateStr(String addedDateStr) {
        this.addedDateStr = addedDateStr;
    }

    public String getPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(String paidStatus) {
        this.paidStatus = paidStatus;
    }

    public String getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(String photoCount) {
        this.photoCount = photoCount;
    }

    public DefaultPhoto getDefaultPhoto() {
        return defaultPhoto;
    }

    public void setDefaultPhoto(DefaultPhoto defaultPhoto) {
        this.defaultPhoto = defaultPhoto;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public ConditionOfItem getConditionOfItem() {
        return conditionOfItem;
    }

    public void setConditionOfItem(ConditionOfItem conditionOfItem) {
        this.conditionOfItem = conditionOfItem;
    }

    public String getIsFavourited() {
        return isFavourited;
    }

    public void setIsFavourited(String isFavourited) {
        this.isFavourited = isFavourited;
    }

    public String getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(String isOwner) {
        this.isOwner = isOwner;
    }

}
