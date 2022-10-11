package tsi.dm.gcpd.atividadejokenpo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WinnerDao {
    @Query("SELECT * FROM winner")
    List<Winner> getAll();

    @Insert
    void insert(Winner winner);

    @Query("DELETE FROM winner")
    void clearTable();

    @Query("SELECT * FROM winner ORDER BY _id DESC")
    Winner getLastWinner();

    @Update
    void updatePlayerName(Winner winner);
}
