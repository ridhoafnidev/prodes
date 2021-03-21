package com.mrii.prodes.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mrii.prodes.viewobject.Blog;
import com.mrii.prodes.viewobject.InfoCovid;

import java.util.List;

@Dao
public interface InfoCovidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InfoCovid> infoCovids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(InfoCovid infoCovid);

    @Query("SELECT * FROM InfoCovid WHERE id = :id")
    LiveData<InfoCovid> getInfoCovidById(String id);

    @Query("SELECT * FROM InfoCovid ORDER BY addedDate desc")
    LiveData<List<InfoCovid>> getAllNewsInfoCovid();

    @Query("SELECT * FROM InfoCovid WHERE cityId = :cityId ORDER BY addedDate desc")
    LiveData<List<InfoCovid>> getAllNewsInfoCovid(String cityId);

    @Query("SELECT * FROM InfoCovid ORDER BY addedDate desc limit :limit")
    LiveData<List<InfoCovid>> getAllNewsInfoCovidByLimit(String limit);

    @Query("DELETE FROM InfoCovid")
    void deleteAll();

}