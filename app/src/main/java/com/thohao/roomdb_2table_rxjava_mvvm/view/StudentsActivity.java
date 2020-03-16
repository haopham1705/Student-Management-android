package com.thohao.roomdb_2table_rxjava_mvvm.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
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
        DialogStudentUpdate.OnUpdateStudentLayer,
        StudentAdapter.OnStudentClickListener,
        DialogShowStudent.OnShowStudentLayer {

    private TextView mTxtTitleToolbar;
    private RecyclerView mRecyclerView;
    private StudentAdapter studentAdapter;
    private String mClass = "";
    private int class_id;
    private List<Students> list;
    private View parent;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private StudentActivityViewModel studentActivityViewModel;
    private static final String TAG = "ccc_studentactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        mTxtTitleToolbar = findViewById(R.id.toolbar_title);
//set recycler view
        mRecyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        studentActivityViewModel = ViewModelProviders.of(this).get(StudentActivityViewModel.class);
//set titles cho Student
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mClass = extras.getString("classname");
            class_id = extras.getInt("id");
            Log.d(TAG, "getExtras: " + class_id + ".." + mClass);
            mTxtTitleToolbar.setText("Class: " + mClass);
        }

        Disposable disposable = studentActivityViewModel.getAllStudent(class_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Students>>() {
                    @Override
                    public void accept(List<Students> studentsList) throws Exception {
                        Log.d(TAG, "getAllStudent");
                        setDataToRecyclerView(studentsList);
                    }
                });
        compositeDisposable.add(disposable);
//swipe to Delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                new MaterialAlertDialogBuilder(StudentsActivity.this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                        .setMessage("Do you want delete ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Snackbar snackbar = Snackbar.make(mRecyclerView, "The item was deleted", Snackbar.LENGTH_LONG)
                                        .setAction("UNDO", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                studentAdapter.undoDelete();
                                                studentActivityViewModel.update(studentAdapter.getStudentAt(viewHolder.getAdapterPosition()));
                                                Toast.makeText(StudentsActivity.this, "Undo successfull", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addCallback(new Snackbar.Callback() {
                                            @Override
                                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                                    studentActivityViewModel.delete(studentAdapter.getStudentAt(viewHolder.getAdapterPosition()));
                                                    Toast.makeText(StudentsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .setActionTextColor(Color.GREEN);
                                snackbar.show();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                //call update để hiện thị lại item đã bị ẩn đi sau khi swipe
                                studentActivityViewModel.update(studentAdapter.getStudentAt(viewHolder.getAdapterPosition()));
                                Toast.makeText(StudentsActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                            }
                        })
                        //setOnCancelListener khi bỏ chọn dialog để hiện thị lại item
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                studentActivityViewModel.update(studentAdapter.getStudentAt(viewHolder.getAdapterPosition()));
                                Toast.makeText(StudentsActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create().show();
            }
        }).attachToRecyclerView(mRecyclerView);

//Open insert dialog
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddStudentDialog();
            }
        });
        intToolbar();
    }

    //insert dialog
    private void openAddStudentDialog() {
        DialogStudentAdd dialogStudentAdd = new DialogStudentAdd();
        dialogStudentAdd.show(getSupportFragmentManager(), "Create Student Dialog");
    }

    //setAdapter
    private void setDataToRecyclerView(List<Students> studentsList) {
        studentAdapter = new StudentAdapter(studentsList);
        studentAdapter.setItemClickListener(this);
        mRecyclerView.setAdapter(studentAdapter);
    }

//toolBar
    private void intToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//back home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back02);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ClassActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
//Search
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                studentAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

//Action Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//Delete all
        if (id == R.id.action_delete_all) {
            new MaterialAlertDialogBuilder(StudentsActivity.this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                    .setMessage("Do you want delete all ?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteAllStudent();
                            Toast.makeText(StudentsActivity.this, "successfull", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Toast.makeText(StudentsActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                        }
                    }).create().show();
            return true;
//Log out
        } else if (id == R.id.action_logout) {
            Log.d(TAG, "Log out");
            startActivity(new Intent(StudentsActivity.this, LoginActivity.class));
            //de ket thuc activity
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //delete all student
    private void deleteAllStudent() {
        studentActivityViewModel.deleteAllStudentByClass(class_id);

    }

//Destroy
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

    //update
    @Override
    public void updateNewStudents(Students students) {
        Students currentStudent = students;
        currentStudent.setClass_id(class_id);
        studentActivityViewModel.update(currentStudent);
    }

    //show
    @Override
    public void showStudents(Students students) {
        Students currentStudent = students;
        currentStudent.setClass_id(class_id);
        studentActivityViewModel.update(currentStudent);
    }

//onStudentClick
    @Override
    public void onStudentClick(Students students) {
        openDialogShowStudent(students);
    }

    //onStudent LongClick
    @Override
    public void onStudentLongClick(Students students) {
        Log.d(TAG, "onStudentClick" + students.getId());

        openDialogUpdateStudent(students);
    }

    //dialog update
    private void openDialogUpdateStudent(Students students) {
        DialogStudentUpdate dialogStudentUpdate = new DialogStudentUpdate();
        dialogStudentUpdate.setStudents(students);
        dialogStudentUpdate.show(getSupportFragmentManager(), "update student dialog");
    }

    private void openDialogShowStudent(Students students) {
        DialogShowStudent dialogShowStudent = new DialogShowStudent();
        dialogShowStudent.setStudents(students);
        dialogShowStudent.show(getSupportFragmentManager(), "Show student dialog");
    }


}
