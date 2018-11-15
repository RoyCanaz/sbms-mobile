package org.totalit.sbms.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.totalit.sbms.domain.Branch;
import org.totalit.sbms.domain.Client;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface BranchDao {

    @Query("SELECT * FROM branch WHERE id = :id")
    Branch findOneById(int id);

    @Query("Select * FROM branch WHERE client_id LIKE :id LIMIT 1 ")
    Branch findByClientId(int id);

    @Query("SELECT * FROM branch WHERE real_id = :realId")
    Branch findOneByRealId(int realId);

    @Query("SELECT * FROM branch")
    List<Branch> getAll();

    @Query("SELECT * FROM branch where real_id =0 and sync_status =0 and client_id=:clientId")
    List<Branch> getAllNotSynced(int clientId);

    @Query("SELECT * FROM branch where real_id =0 and sync_status =0")
    List<Branch> getAllNotSynced();

    @Query("SELECT * FROM branch WHERE client_id = :clientId")
    List<Branch> getAllByClientId(int clientId);

    @Insert(onConflict = REPLACE)
    void insertBranch(Branch branch);

    @Query("UPDATE branch Set client_id = :relClientId WHERE sync_status =0 and client_id = :clientId")
    void updateAllBranches( int clientId, int relClientId);

    @Query("DELETE FROM branch")
    public void deleteBranch();

    @Update
    void updateBranch(Branch branch);
}
