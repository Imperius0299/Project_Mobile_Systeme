package com.example.projectsnakereloaded;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Represents the Dao with the CRUD methods for updating the stats table.
 * @author Alexander Storbeck
 */
@Dao
public interface StatsDao {

    /**
     * Inserts a stats object in the stats database table.
     * @param stats An instance of stats.
     * @return The id of the inserted row.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addStats(Stats stats);

    /**
     * Gets the stats from the stats table.
     * @return An instance of stats.
     */
    @Query("select * from stats")
    Stats getStats();

    /**
     * Updates the stats row in the database table.
     * @param highestScore The highest Score.
     * @param totalScore The total score.
     * @param totalDeaths The total deaths.
     * @param totalItemsPickedUp The total number of items picked up.
     * @param totalFieldsMoved The total number of fields moved.
     * @param id The id of the row.
     */
    @Query("Update stats set highestScore =:highestScore, totalScore = :totalScore, totalDeaths= :totalDeaths, " +
            "totalItemsPickedUp = :totalItemsPickedUp, totalFieldsMoved = :totalFieldsMoved where id = :id")
    void updateStats(int highestScore, int totalScore, int totalDeaths, int totalItemsPickedUp, int totalFieldsMoved, long id);

}
