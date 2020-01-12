package com.thohao.roomdb_2table_rxjava_mvvm.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "class_table")
public class Classes {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String classname;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassname() {
        return classname;
    }

    public byte[] getImage() {
        return image;
    }

    public Classes(String classname, byte[] image) {
        this.classname = classname;
        this.image = image;
    }
}
