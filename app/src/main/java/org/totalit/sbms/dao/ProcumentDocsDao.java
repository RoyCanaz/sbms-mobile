package org.totalit.sbms.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.totalit.sbms.domain.ProcumentDocs;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ProcumentDocsDao {

    @Query("SELECT * FROM procurement_docs WHERE id = :id")
    ProcumentDocs findOneById(int id);

    @Query("SELECT * FROM procurement_docs WHERE id IN (:userIds)")
    List<ProcumentDocs> findAllByIds(int[] userIds);

    @Query("SELECT * FROM procurement_docs WHERE client_id = :clientId")
    ProcumentDocs findByClientId(int clientId);

    @Query("SELECT * FROM procurement_docs WHERE real_id =:realId")
    ProcumentDocs getByRealId(int realId);

    @Query("SELECT * FROM procurement_docs WHERE real_id =0 and sync_status =0 and client_id=:clientId")
    ProcumentDocs getByNotSynced(int clientId);

    @Query("SELECT * FROM procurement_docs where real_id =0 and sync_status =0 and client_id=:clientId")
    List<ProcumentDocs> getAllNotSynced(int clientId);

    @Query("SELECT * FROM procurement_docs where real_id =0 and sync_status =0")
    List<ProcumentDocs> getAllNotSynced();

    @Insert(onConflict = REPLACE)
    void insertProcumentDoc(ProcumentDocs ProcumentDocs);

    @Insert(onConflict = REPLACE)
    void insertAll(ProcumentDocs... ProcumentDocss);

    @Query("DELETE FROM procurement_docs")
    public void deleteProcDocs();

    @Update
    void updateProcurement(ProcumentDocs procumentDocs);

    @Query("UPDATE procurement_docs Set client_id = :relClientId WHERE sync_status =0 and client_id = :clientId")
    void updateProcurement( int clientId, int relClientId);

}
