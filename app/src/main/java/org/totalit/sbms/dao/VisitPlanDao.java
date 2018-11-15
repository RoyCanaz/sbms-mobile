package org.totalit.sbms.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.totalit.sbms.domain.VisitPlan;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface VisitPlanDao {

    @Query("SELECT * FROM visit_plan WHERE id = :id")
    VisitPlan findOneById(int id);

    @Query("Select * FROM visit_plan WHERE client_id LIKE :id ")
    List<VisitPlan> findByClientId(int id);

    @Query("SELECT * FROM visit_plan")
    List<VisitPlan> getAll();

    @Query("SELECT * FROM visit_plan where real_id =0 and sync_status =0 and client_id=:clientId")
    List<VisitPlan> getAllNotSynced(int clientId);

    @Query("SELECT * FROM visit_plan where real_id =0 and sync_status =0")
    List<VisitPlan> getAllNotSynced();

    @Query("SELECT * FROM visit_plan where real_id =:realId")
    VisitPlan getByRealId(int realId);

    @Query("SELECT * FROM visit_plan where created_by =:userid")
    List<VisitPlan> findByCreatedBy(int userid);

    @Insert(onConflict = REPLACE)
    void insertVisitPlan(VisitPlan VisitPlan);

    @Query("DELETE FROM visit_plan")
    public void deleteVisitPlan();

    @Update
    void updateVisitPlan(VisitPlan VisitPlan);

    @Query("UPDATE visit_plan Set client_id = :relClientId WHERE sync_status =0 and client_id = :clientId")
    void updateAllVisitPlan( int clientId, int relClientId);

}
