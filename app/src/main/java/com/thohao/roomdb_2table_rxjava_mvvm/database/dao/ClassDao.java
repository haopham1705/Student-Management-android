package com.thohao.roomdb_2table_rxjava_mvvm.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.thohao.roomdb_2table_rxjava_mvvm.model.Classes;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ClassDao {

    @Insert
    void insert(Classes classes);

    @Update
    void update(Classes classes);

    @Delete
    void delete(Classes classes);



    @Query("DELETE FROM class_table")
    void deleteAllClass();

    @Query("SELECT * FROM class_table")
    Flowable<List<Classes>> getAllClass();

}
