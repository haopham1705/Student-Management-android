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
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private static final String TAG = "ClassRepository";

    public ClassRepository(Application application) {
        ClassDatabase classDatabase = ClassDatabase.getInstance(application);
        classDao = classDatabase.classDao();
        studentDao = classDatabase.studentDao();
    }

    //get all class
    public Flowable<List<Classes>> getAllClasses() {
        return classDao.getAllClass();
    }

    //get loading state
    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    //get all student under the specific class
    public Flowable<List<Students>> getAllStudents(int class_id) {
        return studentDao.getAllStudentsByClass(class_id);
    }

    //insert class
    public void insertClass(final Classes classes) {
        isLoading.setValue(true);
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
                        Log.d(TAG, "onSubscribe : called");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete : called");
                        isLoading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError : called" + e.getMessage());
                    }
                });
    }

    //update class
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
                        Log.d(TAG, "onSubscribe : called");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplate : called");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError : called" + e.getMessage());
                    }
                });
    }

    //delete class
    public void deleteClass(final Classes classes) {
        isLoading.setValue(true);
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
                        Log.d(TAG, "onSubscribe : called");

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onSubscribe : called");
                        //deleteAllStudents(classes.getId());
                        isLoading.setValue(false);


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "OnError : called" + e.getMessage());

                    }
                });
    }

    //deleteAll Class
    public void deleteAllClass() {
        isLoading.setValue(true);
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
                        Log.d(TAG, "onSubscribe : called");

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete : called");
//                        deleteAllStudents();
                        isLoading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError : called" + e.getMessage());
                    }
                });
    }

    //insert student
    public void insertStudent(final Students students) {
        isLoading.setValue(true);
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
                        Log.d(TAG, "onSubscribe : called");

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete : called");
                        isLoading.setValue(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError : called" + e.getMessage());

                    }
                });
    }

    //update student
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
                        Log.d(TAG, "onSubscribe : called");

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete : called");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError : called" + e.getMessage());

                    }
                });
    }

    public void deleteStudent(final Students students) {
        isLoading.setValue(true);
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
                        Log.d(TAG, "onSubscribe : called");

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete : called");
                        isLoading.setValue(false);


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError : called" + e.getMessage());

                    }
                });
    }

    //deleteAllStudent
    public void deleteAllStudents() {
        isLoading.setValue(true);
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
                        Log.d(TAG, "onSubscribe : called");

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete : called");
                        isLoading.setValue(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError : called" + e.getMessage());

                    }
                });

    }

    //delete all student by class
    public void deleteAllStudentsByClass(final int class_id) {
        isLoading.setValue(true);
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
                        Log.d(TAG, "onSubscribe : called");

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete : called");
                        isLoading.setValue(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError : called" + e.getMessage());


                    }
                });
    }
}
