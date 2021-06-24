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
    public int deaths;
    public int numFieldsMoved;
    public int numItemsPickedUp;
}
