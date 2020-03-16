package com.thohao.roomdb_2table_rxjava_mvvm.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.appcompat.widget.SearchView;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

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
        DialogClassAdd.CreateClassListener, ClassAdapter.OnClassClickListener, DialogClassUpdate.UpdateClassListener {

    private ClassActivityViewModel classActivityViewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView mRecyclerView;
    private ClassAdapter classAdapter;

    private static final String TAG = "ccc_classactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        intToolbar();

        FloatingActionButton fab = findViewById(R.id.fab);
//set recyclerview
        mRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
//Viewmodel
        classActivityViewModel = ViewModelProviders.of(this).get(ClassActivityViewModel.class);

        final Disposable disposable = classActivityViewModel.getAllClass()
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

//Floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogClassAdd();
            }
        });

//Swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0
                , ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                new MaterialAlertDialogBuilder(ClassActivity.this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                        .setMessage("Do you want delete ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//snackbar undo
                                Snackbar snackbar = Snackbar.make(mRecyclerView, "The item was deleted", Snackbar.LENGTH_LONG)
                                        .setAction("UNDO", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                classAdapter.undoDelete();
                                                classActivityViewModel.updateClass(classAdapter.getClassAt(viewHolder.getAdapterPosition()));
                                                Toast.makeText(ClassActivity.this, "Undo successfull", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addCallback(new Snackbar.Callback() {
                                            @Override
                                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                                if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                                    classActivityViewModel.deleteClass(classAdapter.getClassAt(viewHolder.getAdapterPosition()));
                                                    Toast.makeText(ClassActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
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
                                classActivityViewModel.updateClass(classAdapter.getClassAt(viewHolder.getAdapterPosition()));
                                //call update để hiện thị lại item đã bị ẩn đi sau khi swipe
                                Toast.makeText(ClassActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                            }
                        })
                        //setOnCancelListener khi bỏ chọn dialog để hiện thị lại item
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                classActivityViewModel.updateClass(classAdapter.getClassAt(viewHolder.getAdapterPosition()));
                                Toast.makeText(ClassActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create().show();
            }
        }).attachToRecyclerView(mRecyclerView);

    }

    //open insert class dialog
    public void openDialogClassAdd() {
        DialogClassAdd dialogClassAdd = new DialogClassAdd();
        dialogClassAdd.show(getSupportFragmentManager(), "create dialog");
    }

    //setAdapter
    private void setDataToRecyclerView(List<Classes> classes) {
        classAdapter = new ClassAdapter(classes);
        classAdapter.setItemClickListener(this);
        mRecyclerView.setAdapter(classAdapter);
    }

    //toolBar
    private void intToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setTitle("LiveF Manager");
        toolbar.setTitleTextColor(Color.WHITE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
//Searching
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                classAdapter.getFilter().filter(newText);
                return false;
            }
        });
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
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteClassAndStudent();
                            Toast.makeText(ClassActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Toast.makeText(ClassActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                        }
                    }).create().show();
            return true;
        }
//logout
        else if (id == R.id.action_logout) {
            Log.d(TAG, "Log out");
            startActivity(new Intent(ClassActivity.this, LoginActivity.class));
            //de ket thuc activity
            finish();

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

    @Override
    public void onClassLongClick(Classes classes) {
        openDialogUpdateClass(classes);
        Log.d(TAG, "onLongClick item");
    }

    public void moveToStudentActivity(Classes classes) {
        Intent intent = new Intent(ClassActivity.this, StudentsActivity.class);
        intent.putExtra("classname", classes.getClassname());
        intent.putExtra("id", classes.getId());
        startActivity(intent);
    }

    @Override
    public void saveNameClass(Classes classes) {
        Log.d(TAG, "save Class " + classes.getClassname());
        classActivityViewModel.insertClass(classes);
    }

    @Override
    public void updateNameClass(Classes classes) {
        Classes currentClass = classes;
        currentClass.setId(classes.getId());
        classActivityViewModel.updateClass(currentClass);

    }

    private void openDialogUpdateClass(Classes classes) {
        DialogClassUpdate dialogClassUpdate = new DialogClassUpdate();
        dialogClassUpdate.setClass(classes);
        dialogClassUpdate.show(getSupportFragmentManager(), "update class dialog");
    }

    //onDestroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
