package com.mrii.prodes.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mrii.prodes.viewobject.Banner;
import com.mrii.prodes.viewobject.Blog;

import java.util.List;

@Dao
public interface BannerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Banner> banners);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Banner banner);

    @Query("SELECT * FROM Banner WHERE id = :id")
    LiveData<Banner> getBannerById(String id);

    @Query("SELECT * FROM Banner ORDER BY addedDate desc")
    LiveData<List<Banner>> getAllNewsBanner();

    @Query("SELECT * FROM Banner WHERE cityId = :cityId ORDER BY addedDate desc")
    LiveData<List<Blog>> getAllNewsBanner(String cityId);

    @Query("SELECT * FROM Banner ORDER BY addedDate desc limit :limit")
    LiveData<List<Banner>> getAllNewsBannerByLimit(String limit);

    @Query("DELETE FROM Banner")
    void deleteAll();
}
