package com.example.projectsnakereloaded;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Represents the local database of the App.
 */
@Database(entities = {Stats.class}, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract StatsDao statsDao();

    /**
     * Get's the actual Instance of the Database when
     * @param context
     * @return
     */
    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "statsdatabase")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
