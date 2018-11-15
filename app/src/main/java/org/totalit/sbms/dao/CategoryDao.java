package org.totalit.sbms.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.totalit.sbms.domain.Category;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM category")
    List<Category> getNames();

    @Query("Select id FROM category where name=:name")
    int getId(String name);

    @Insert(onConflict = REPLACE)
    void insertCategory(Category category);

    @Insert(onConflict = REPLACE)
    void insertAllCategories(Category... category);
}
