package com.thohao.roomdb_2table_rxjava_mvvm.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.thohao.roomdb_2table_rxjava_mvvm.database.ClassDatabase;
import com.thohao.roomdb_2table_rxjava_mvvm.database.dao.ClassDao;
import com.thohao.roomdb_2table_rxjava_mvvm.database.dao.StudentDao;
import com.thohao.roomdb_2table_rxjava_mvvm.model.Classes;
import com.thohao.roomdb_2table_rxjava_mvvm.model.Students;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class ClassRepository {
    private ClassDao classDao;
    private StudentDao studentDao;

    private Flowable<List<Classes>> allClasses;
    private Flowable<List<Students>> allStudents;

    private static final String TAG = "ccc_classrepository";

    public ClassRepository(Application application) {
        ClassDatabase classDatabase = ClassDatabase.getInstance(application);
        classDao = classDatabase.classDao();
        studentDao = classDatabase.studentDao();
    }

//get all class
    public Flowable<List<Classes>> getAllClasses() {
        return classDao.getAllClass();
    }
//get all student by class
    public Flowable<List<Students>> getAllStudents(int class_id) {
        return studentDao.getAllStudentsByClass(class_id);
    }

//Insert class
    public void insertClass(final Classes classes) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                classDao.insert(classes);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError" + e.getMessage());
                    }
                });
    }

//Update class
    public void updateClass(final Classes classes) {

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                classDao.update(classes);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplate");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError" + e.getMessage());
                    }
                });
    }

//Delete class
    public void deleteClass(final Classes classes) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                classDao.delete(classes);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onSubscribe");
                        //deleteAllStudents(classes.getId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "OnError" + e.getMessage());
                    }
                });
    }

//DeleteAll Class
    public void deleteAllClass() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                classDao.deleteAllClass();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
//                        deleteAllStudents();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError" + e.getMessage());
                    }
                });
    }

//Insert student
    public void insertStudent(final Students students) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                studentDao.insert(students);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError" + e.getMessage());
                    }
                });
    }

//Update student
    public void updateStudent(final Students students) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                studentDao.update(students);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError" + e.getMessage());

                    }
                });
    }
//delete student
    public void deleteStudent(final Students students) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                studentDao.delete(students);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError" + e.getMessage());
                    }
                });
    }

//delete All Student
    public void deleteAllStudents() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                studentDao.deleteAllStudents();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError" + e.getMessage());

                    }
                });
    }

//delete all student by class
    public void deleteAllStudentsByClass(final int class_id) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                studentDao.deleteAllStudentsUnderClass(class_id);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError" + e.getMessage());
                    }
                });
    }
}
