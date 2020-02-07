package com.thohao.roomdb_2table_rxjava_mvvm.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.thohao.roomdb_2table_rxjava_mvvm.model.Students;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface StudentDao {

    @Insert
    void insert(Students students);

    @Update
    void update(Students students);

    @Delete
    void delete(Students students);
//delete all students
    @Query("DELETE FROM student_table")
    void deleteAllStudents();
//delete students by class
    @Query("DELETE FROM student_table WHERE class_id==:class_id")
    void deleteAllStudentsUnderClass(int class_id);

    @Query("SELECT * FROM student_table WHERE class_id LIKE :class_id")
    Flowable<List<Students>> getAllStudentsByClass(int class_id);
}
