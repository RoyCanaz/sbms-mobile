package org.totalit.sbms.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import org.totalit.sbms.domain.Client;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;
@Dao
public interface ClientDao {
    @Query("SELECT * FROM client  ORDER BY name DESC")
    List<Client> getAll();

    @Query("SELECT * FROM client where sync_status =1 ORDER BY name DESC")
    List<Client> getAllSynced();

    @Query("SELECT * FROM client where sync_status =0")
    List<Client> getAllNotSynced();

    @Query("SELECT * FROM client WHERE id = :clientId")
    Client findOneById(int clientId);

    @Query("SELECT * FROM client WHERE real_id = :clientId")
    Client findOneByRealId(int clientId);

    @Query("SELECT * FROM client")
    List<Client> getNames();


    @Query("Select real_id FROM client where name=:name")
    int getId(String name);

    @Query("DELETE FROM client")
    public void deleteClient();


    @Query("SELECT * FROM client WHERE id = :clientId AND real_id =:uuid")
    Client findOneByIdAndUuid(int clientId, int uuid);


    @Query("SELECT * FROM client WHERE id IN (:userIds)")
    List<Client> findAllByIds(int[] userIds);

    @Query("Select * FROM client WHERE  name LIKE :name and active LIKE :active LIMIT 1 ")
    Client findByNameAndActive(String name, Boolean active);

    @Query("Select Max(id) FROM client")
    int getMaxId();

    @Insert(onConflict = REPLACE)
    void insertUser(Client client);

    @Update
    void updateClient(Client client);

    @Insert(onConflict = REPLACE)
    void insertAll(Client... clients);
}
