package com.thohao.roomdb_2table_rxjava_mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.thohao.roomdb_2table_rxjava_mvvm.model.Students;
import com.thohao.roomdb_2table_rxjava_mvvm.repository.ClassRepository;

import java.util.List;

import io.reactivex.Flowable;

public class StudentActivityViewModel extends AndroidViewModel {

    private ClassRepository classRepository;
    public StudentActivityViewModel(@NonNull Application application) {
        super(application);
        classRepository = new ClassRepository(application);
    }

    //get all student
    public Flowable<List<Students>> getAllStudent(int class_id) {
        return classRepository.getAllStudents(class_id);
    }
    //Get Loading State
    public MutableLiveData<Boolean> getIsLoading(){
        return classRepository.getIsLoading();
    }
    //Insert Movie
    public void insert(Students students){
        classRepository.insertStudent(students);
    }

    //Update Movie
    public void update(Students students){
        classRepository.updateStudent(students);
    }

    //Delete Movie
    public void delete(Students students){
        classRepository.deleteStudent(students);
    }

    //Delete All Movie
    public void deleteAllStudentByClass( int class_id){
        classRepository.deleteAllStudentsByClass(class_id);
    }
}
