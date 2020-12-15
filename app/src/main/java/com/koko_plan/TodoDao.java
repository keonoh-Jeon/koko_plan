package com.koko_plan;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
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

    @Query("SELECT * FROM todoTable WHERE date LIKE :day ORDER BY num ASC")
    List<Todo> search(String day);

    @Query("SELECT * FROM todoTable WHERE date LIKE :search ORDER BY num ASC")
    LiveData<List<Todo>> getAll(String search); //LiveData

    @Query("DELETE FROM todoTable")
    void deleteAll();

    /*@Query("SELECT * from todoTable ORDER BY num ASC")
    LiveData<List<Todo>> getAlphabetizedTitles();*/
    //오름차순(ASC) 정렬하여 가져옵니다. (*DESC: 내림차순)

}

