package com.thohao.roomdb_2table_rxjava_mvvm.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.thohao.roomdb_2table_rxjava_mvvm.R;
import com.thohao.roomdb_2table_rxjava_mvvm.adapter.StudentAdapter;
import com.thohao.roomdb_2table_rxjava_mvvm.model.Students;
import com.thohao.roomdb_2table_rxjava_mvvm.viewmodel.StudentActivityViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class StudentsActivity extends AppCompatActivity
        implements DialogStudentAdd.OnCreateStudentListener,
                    DialogStudentUpdate.UpdateStudentLayer,
                    StudentAdapter.OnStudentClickListener{
    //private static final int START_DELAY = 2;
    private TextView mTxtTitleToolbar;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private StudentAdapter studentAdapter;
    private String mClass = "";
    private int class_id;
    private List<Students> list;
    private View parent;
    //private MaterialAlertDialogBuilder builder;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private StudentActivityViewModel studentActivityViewModel;

    private static final String TAG = "ccc_studentactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        //setItemOnlick
        studentAdapter = new StudentAdapter(list);
        studentAdapter.setItemOnClick(this);

        //parent = findViewById(android.R.id.content);
        mTxtTitleToolbar = findViewById(R.id.toolbar_title);
        mProgressBar = findViewById(R.id.progress);
        mRecyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        studentActivityViewModel = ViewModelProviders.of(this).get(StudentActivityViewModel.class);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mClass = extras.getString("classname");
            class_id = extras.getInt("id");
            Log.d(TAG, "onStart: " + class_id + "" + mClass);
            mTxtTitleToolbar.setText(mClass + " Students");
        }
        Disposable disposable = studentActivityViewModel.getAllStudent(class_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Students>>() {
                    @Override
                    public void accept(List<Students> studentsList) throws Exception {
                        Log.d(TAG, "accept: getAllStudent");
                        studentAdapter = new StudentAdapter(studentsList);
                        mRecyclerView.setAdapter(studentAdapter);
                    }
                });
        compositeDisposable.add(disposable);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
//swipe để delete
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                new MaterialAlertDialogBuilder(StudentsActivity.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                        .setMessage("Do you want delete ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                studentActivityViewModel.delete(studentAdapter.getStudentAt(viewHolder.getAdapterPosition()));
                                Toast.makeText(StudentsActivity.this,"Delete successfull",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                studentActivityViewModel.update(studentAdapter.getStudentAt(viewHolder.getAdapterPosition()));
                                //call update để hiện thị lại item đã bị ẩn đi sau khi wipe
                                Toast.makeText(StudentsActivity.this,"Cancel delete",Toast.LENGTH_SHORT).show();
                            }
                        }).create().show();
                //studentActivityViewModel.delete(studentAdapter.getStudentAt(viewHolder.getAdapterPosition()));

            }
        }).attachToRecyclerView(mRecyclerView);
        studentActivityViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d(TAG, "onChanged: " + aBoolean);
                if (aBoolean != null) {
                    if (aBoolean) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    } else {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddStudentDialog();
            }
        });
        intToolbar();
    }
//Dialog add
    private void openAddStudentDialog() {
        DialogStudentAdd dialogStudentAdd = new DialogStudentAdd();
        dialogStudentAdd.show(getSupportFragmentManager(), "Create Student Dialog");
    }
//actionbar
    private void intToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back02);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ClassActivity.class));
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
//Action Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//Delete all
        if (id == R.id.action_delete_all) {
            new MaterialAlertDialogBuilder(StudentsActivity.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                    .setMessage("Do you want delete all ?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteAllStudent();
                            Toast.makeText(StudentsActivity.this,"successfull",Toast.LENGTH_SHORT).show();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Toast.makeText(StudentsActivity.this,"Cancel delete",Toast.LENGTH_SHORT).show();
                        }
                    }).create().show();
            return true;
        } else if (id == R.id.action_about) {
            Toast.makeText(StudentsActivity.this, "ABOUT..." + TAG, Toast.LENGTH_SHORT).show();
        }
//Search
        else if (id == R.id.action_search) {
            Toast.makeText(StudentsActivity.this, "Searching..." + TAG, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void deleteAllStudent() {
        studentActivityViewModel.deleteAllStudentByClass(class_id);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
    @Override
    public void saveNewStudent(Students students) {
        Students currentStudent = students;
        currentStudent.setClass_id(class_id);
        studentActivityViewModel.insert(currentStudent);
    }

    /*public void snackBar(String message) {
        Snackbar.make(parent,message,Snackbar.LENGTH_SHORT).show();
    }*/
    @Override
    public void updateNewStudents(Students students) {
        Students currentStudent = students;
        currentStudent.setClass_id(class_id);
        studentActivityViewModel.update(currentStudent);
        //snackBar("Student updated");

    }
    @Override
    public void onStudentClick(Students students) {
        Log.d(TAG, "" + students.getId());
        openDialogUpdateStudent(students);
    }
    private void openDialogUpdateStudent(Students students) {
        DialogStudentUpdate dialogStudentUpdate = new DialogStudentUpdate();
        dialogStudentUpdate.setStudents(students);
        dialogStudentUpdate.show(getSupportFragmentManager(),"update student");
    }
}
