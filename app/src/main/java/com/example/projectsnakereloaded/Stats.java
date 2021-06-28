package com.example.projectsnakereloaded;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/** Represents the stats for the played Games.
 * @author Alexander Storbeck
 */
@Entity(tableName = "stats",
        indices = {@Index(value ="id", unique = true)}
        )
public class Stats {

    @PrimaryKey(autoGenerate = true)
    public long id;

    //public String date;
    public int highestScore;
    public int totalScore;
    public int totalDeaths;
    public int totalFieldsMoved;
    public int totalItemsPickedUp;

    /**
     * Creates the Stats with the specific values.
     * @param highestScore The highest score that was reached over all games.
     * @param totalScore The sum of all reached scores.
     * @param totalDeaths The sum of the total player's deaths.
     */
    public Stats(int highestScore, int totalScore, int totalDeaths, int totalItemsPickedUp, int totalFieldsMoved) {
        this.highestScore = highestScore;
        this.totalScore = totalScore;
        this.totalDeaths = totalDeaths;
        this.totalItemsPickedUp = totalItemsPickedUp;
        this.totalFieldsMoved = totalFieldsMoved;
    }
}
