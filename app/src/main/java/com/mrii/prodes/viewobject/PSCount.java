package com.mrii.prodes.viewobject;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "id")
public class PSCount {

    @NonNull
    @SerializedName("id")
    public String id;

    @SerializedName("blog_noti_unread_count")
    public String blogNotiUnreadCount;

    @SerializedName("buyer_unread_count")
    public String buyerUnreadCount;

    @SerializedName("seller_unread_count")
    public String sellerUnreadCount;

}
