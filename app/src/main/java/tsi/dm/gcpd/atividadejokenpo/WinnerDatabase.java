package tsi.dm.gcpd.atividadejokenpo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Winner.class}, exportSchema = false, version = 1)
public abstract class WinnerDatabase extends RoomDatabase {
    public abstract WinnerDao winnerDao();

    private static WinnerDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;
    //static final ExecutorService databaseWriteExecutor =
    //        Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static WinnerDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (WinnerDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    WinnerDatabase.class, "winners")
                                    .allowMainThreadQueries()
                                    .build();
                }
            }
        }
        return instance;
    }
}
