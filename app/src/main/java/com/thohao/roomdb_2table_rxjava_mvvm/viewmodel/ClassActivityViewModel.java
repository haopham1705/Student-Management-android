package com.thohao.roomdb_2table_rxjava_mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.thohao.roomdb_2table_rxjava_mvvm.model.Classes;
import com.thohao.roomdb_2table_rxjava_mvvm.repository.ClassRepository;

import java.util.List;

import io.reactivex.Flowable;

public class ClassActivityViewModel extends AndroidViewModel {

    private ClassRepository classRepository;

    public ClassActivityViewModel(@NonNull Application application) {
        super(application);
        classRepository = new ClassRepository(application);
    }

    //get all class
    public Flowable<List<Classes>> getAllClass() {
        return classRepository.getAllClasses();
    }

    //get loading state
    public MutableLiveData<Boolean> getIsLoading() {
        return classRepository.getIsLoading();
    }

    //Insert
    public void insertClass(Classes classes) {
        classRepository.insertClass(classes);
    }

    //update
    public void updateClass(Classes classes) {

        classRepository.updateClass(classes);
    }

    //delete
    public void deleteClass(Classes classes) {
        classRepository.deleteClass(classes);

    }

    //delete all
    public void deleteAllClass() {
        classRepository.deleteAllClass();

    }
}
