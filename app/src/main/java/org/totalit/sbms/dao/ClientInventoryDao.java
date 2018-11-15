package org.totalit.sbms.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.totalit.sbms.domain.ClientInventory;
import org.totalit.sbms.domain.VisitPlan;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ClientInventoryDao {

    @Query("SELECT * FROM client_inventory WHERE id = :id")
    ClientInventory findOneById(int id);

    @Query("Select * FROM client_inventory WHERE client_id LIKE :id")
    List<ClientInventory> findByClientId(int id);

    @Query("SELECT * FROM client_inventory")
    List<ClientInventory> getAll();

    @Query("SELECT * FROM client_inventory where real_id =0 and sync_status =0 and client_id=:clientId")
    List<ClientInventory> getAllNotSynced(int clientId);

    @Query("SELECT * FROM client_inventory where real_id =0 and sync_status =0")
    List<ClientInventory> getAllNotSynced();

    @Query("SELECT * FROM client_inventory where real_id =:realId")
    ClientInventory getByRealId(int realId);

    @Update
    void update(ClientInventory clientInventory);

    @Insert(onConflict = REPLACE)
    void insertUser(ClientInventory clientInventory);

    @Query("DELETE FROM client_inventory")
    public void deleteClientInventory();

    @Query("UPDATE client_inventory Set client_id = :relClientId WHERE sync_status =0 and client_id = :clientId")
    void updateAllClientInventory( int clientId, int relClientId);

}
