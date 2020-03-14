package com.thohao.roomdb_2table_rxjava_mvvm.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.thohao.roomdb_2table_rxjava_mvvm.database.dao.ClassDao;
import com.thohao.roomdb_2table_rxjava_mvvm.database.dao.StudentDao;
import com.thohao.roomdb_2table_rxjava_mvvm.model.Classes;
import com.thohao.roomdb_2table_rxjava_mvvm.model.Students;

@Database(entities = {Classes.class, Students.class}, version = 1, exportSchema = false)
public abstract class ClassDatabase extends RoomDatabase {
    private static ClassDatabase instance;

    public abstract ClassDao classDao();

    public abstract StudentDao studentDao();

    public static synchronized ClassDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ClassDatabase.class, "class_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

}
