package com.mrii.prodes.viewobject;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.annotation.NonNull;

@Entity(primaryKeys = "userId")
public class UserLogin {

    @NonNull
    public final String userId;

    public final Boolean login;

    @Embedded(prefix = "user_")
    public final User user;

    public UserLogin(@NonNull String userId, Boolean login, User user) {
        this.userId = userId;
        this.login = login;
        this.user = user;
    }
}
