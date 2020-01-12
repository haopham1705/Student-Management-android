package com.thohao.roomdb_2table_rxjava_mvvm.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "student_table")
public class Students {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int class_id;
    private String name;
    private String age;
    private String address;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    public Students(String name, String age, String address, byte[] image) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.image = image;
    }
//setter
    public void setId(int id) {
        this.id = id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    //getter
    public int getId() {
        return id;
    }

    public int getClass_id() {
        return class_id;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public byte[] getImage() {
        return image;
    }
}
