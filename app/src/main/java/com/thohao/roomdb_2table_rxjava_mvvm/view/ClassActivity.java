package com.thohao.roomdb_2table_rxjava_mvvm.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thohao.roomdb_2table_rxjava_mvvm.R;
import com.thohao.roomdb_2table_rxjava_mvvm.adapter.ClassAdapter;
import com.thohao.roomdb_2table_rxjava_mvvm.model.Classes;
import com.thohao.roomdb_2table_rxjava_mvvm.viewmodel.ClassActivityViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ClassActivity extends AppCompatActivity implements
        DialogClassAdd.CreateClassListener, ClassAdapter.OnClassClickListener {

    private ClassActivityViewModel classActivityViewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView mRecyclerView;
    private ClassAdapter classAdapter;
    private ProgressBar mProgressBar;
    private static final String TAG = "ccc_classactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        intToolbar();
        FloatingActionButton fab = findViewById(R.id.fab);
        mRecyclerView = findViewById(R.id.recycler_view);
        mProgressBar = findViewById(R.id.progress);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
//viewmodel
        classActivityViewModel = ViewModelProviders.of(this).get(ClassActivityViewModel.class);

        Disposable disposable = classActivityViewModel.getAllClass()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Classes>>() {
                    @Override
                    public void accept(List<Classes> classes) throws Exception {
                        Log.d(TAG, "called");
                        setDataToRecyclerView(classes);
                    }
                });
        compositeDisposable.add(disposable);

        classActivityViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogClassAdd();
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0
                , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                classActivityViewModel.deleteClass(classAdapter.getClassAt(viewHolder.getAdapterPosition()));

            }
        }).attachToRecyclerView(mRecyclerView);

    }

    public void openDialogClassAdd() {
        DialogClassAdd dialogClassAdd = new DialogClassAdd();
        dialogClassAdd.show(getSupportFragmentManager(), "create dialog");

    }

    private void setDataToRecyclerView(List<Classes> classes) {
        classAdapter = new ClassAdapter(classes);
        classAdapter.setItemClickListener(this);
        mRecyclerView.setAdapter(classAdapter);
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

//option menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        //Delete All
        if (id == R.id.action_delete_all) {
            new MaterialAlertDialogBuilder(ClassActivity.this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                    .setMessage("Do you want delete all ?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteClassAndStudent();
                            Toast.makeText(ClassActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Toast.makeText(ClassActivity.this, "You choice no", Toast.LENGTH_SHORT).show();
                        }
                    }).create().show();
            return true;
        }
//exit
        else if (id == R.id.action_exit) {
            Log.d(TAG, "Exit");
            finish();
            System.exit(0);
        }
//search
        else if (id == R.id.action_search) {
            Toast.makeText(ClassActivity.this, "Searching..." + TAG, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteClassAndStudent() {
        classActivityViewModel.deleteAllClass();
    }
//onClick
    @Override
    public void onClassClick(Classes classes) {
        Log.d(TAG, "onClick item");
        moveToStudentActivity(classes);
    }

    public void moveToStudentActivity(Classes classes) {
        Intent intent = new Intent(ClassActivity.this, StudentsActivity.class);
        intent.putExtra("classname", classes.getClassname());
        intent.putExtra("id", classes.getId());
        startActivity(intent);
    }

    @Override
    public void saveNameClass(Classes classes) {
        Log.d(TAG, "saveNewGenre: " + classes.getClassname());
        classActivityViewModel.insertClass(classes);
    }

//onDestroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
