package org.totalit.sbms.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.totalit.sbms.domain.Category;
import org.totalit.sbms.domain.Supplier;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface SupplierDao {
    @Query("SELECT * FROM supplier")
    List<Supplier> getSuppliers();

    @Query("Select * FROM supplier where name=:name")
    Supplier getSupplierByName(String name);

    @Insert(onConflict = REPLACE)
    void insertSupplier(Supplier supplier);

}
