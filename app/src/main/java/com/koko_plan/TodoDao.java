package com.koko_plan;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDao {

    @Insert
    void insert(Todo todo);

    @Update
    void update(Todo todo);

    @Delete
    void delete(Todo todo);

    @Query("SELECT * FROM todoTable")
    LiveData<List<Todo>> getAll(); //LiveData

    @Query("DELETE FROM todoTable")
    void deleteAll();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAll(List<Todo> todo);

}

