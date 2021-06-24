package com.example.projectsnakereloaded;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addStats(Stats stats);

    @Query("select * from stats")
    List<Stats> getStats();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateStats(Stats stats);
}
