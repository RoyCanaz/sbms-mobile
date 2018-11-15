package org.totalit.sbms.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.totalit.sbms.domain.Branch;
import org.totalit.sbms.domain.Notes;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;
@Dao
public interface NotesDao {

    @Query("SELECT * FROM notes WHERE id = :id")
    Notes findOneById(int id);

    @Query("Select * FROM notes WHERE client_id LIKE :id ")
    List<Notes> findByClientId(int id);

    @Query("SELECT * FROM notes")
    List<Notes> getAll();

    @Query("SELECT * FROM notes where real_id =0 and sync_status =0 and client_id=:clientId")
    List<Notes> getAllNotSynced(int clientId);

    @Query("SELECT * FROM notes where real_id =0 and sync_status =0")
    List<Notes> getAllNotSynced();

    @Query("SELECT * FROM notes WHERE real_id =:realId")
    Notes getByRealId(int realId);

    @Insert(onConflict = REPLACE)
    void insertNote(Notes notes);

    @Update
    void updateNote(Notes notes);

    @Query("DELETE FROM notes")
    public void deleteNotes();

    @Query("UPDATE notes Set client_id = :relClientId WHERE sync_status =0 and client_id = :clientId")
    void updateAllNotes( int clientId, int relClientId);
}
