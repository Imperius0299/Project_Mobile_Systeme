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
    Stats getStats();

    @Query("Update stats set highestScore =:highestScore, totalScore = :totalScore, totalDeaths= :totalDeaths, " +
            "totalItemsPickedUp = :totalItemsPickedUp, totalFieldsMoved = :totalFieldsMoved where id = :id")
    void updateStats(int highestScore, int totalScore, int totalDeaths, int totalItemsPickedUp, int totalFieldsMoved, long id);

}
