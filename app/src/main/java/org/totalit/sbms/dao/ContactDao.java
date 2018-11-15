package org.totalit.sbms.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.totalit.sbms.domain.Contact;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contact WHERE id = :id")
    Contact findOneById(int id);

    @Query("Select * FROM contact WHERE client_id =:id ")
    List<Contact> findByClientId(int id);

    @Query("SELECT * FROM contact")
    List<Contact> getAll();

    @Query("SELECT * FROM contact where sync_status =1 and client_id =:id")
    List<Contact> getAllSyncedAndClient(int id);

    @Query("SELECT * FROM contact where real_id =0 and sync_status =0 and client_id=:clientId")
    List<Contact> getAllNotSynced(int clientId);

    @Query("SELECT * FROM contact where real_id =0 and sync_status =0")
    List<Contact> getAllNotSynced();

    @Query("SELECT * FROM contact WHERE real_id =:realId")
    Contact getByRealId(int realId);

    @Insert(onConflict = REPLACE)
    void insertContact(Contact contact);

    @Update
    void updateContact(Contact contact);

    @Query("DELETE FROM contact")
    public void deleteContact();

    @Query("UPDATE contact Set client_id = :relClientId WHERE sync_status =0 and client_id = :clientId")
    void updateAllContacts( int clientId, int relClientId);
}
