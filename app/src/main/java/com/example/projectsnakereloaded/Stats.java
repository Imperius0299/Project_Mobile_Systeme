package com.example.projectsnakereloaded;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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

    public Stats(int highestScore, int totalScore, int totalDeaths) {
        this.highestScore = highestScore;
        this.totalScore = totalScore;
        this.totalDeaths = totalDeaths;
    }
}
