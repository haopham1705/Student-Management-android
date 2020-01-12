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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
        implements DialogStudentAdd.OnCreateStudentListener {

    private static final int START_DELAY = 2;
    private TextView mTxtTitleToolbar;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private StudentAdapter studentAdapter;
    private String mClass = "";
    private int class_id;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private StudentActivityViewModel studentActivityViewModel;

    private static final String TAG = "ccc_studentactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        mTxtTitleToolbar = findViewById(R.id.toolbar_title);
        mProgressBar = findViewById(R.id.progress);
        mRecyclerView = findViewById(R.id.recycler_view);
        Context context;
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

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                studentActivityViewModel.delete(studentAdapter.getStudentAt(viewHolder.getAdapterPosition()));

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

    private void openAddStudentDialog() {
        DialogStudentAdd dialogStudentAdd = new DialogStudentAdd();
        dialogStudentAdd.show(getSupportFragmentManager(), "Create Student Dialog");


    }

    private void intToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
//action menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            deleteAllStudent();
            return true;
        } else if (id == R.id.action_search) {
            Toast.makeText(StudentsActivity.this,"Searching..."+TAG,Toast.LENGTH_SHORT).show();
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
}
