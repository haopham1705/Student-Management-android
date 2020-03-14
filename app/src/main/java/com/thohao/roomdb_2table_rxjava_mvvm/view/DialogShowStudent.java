package com.thohao.roomdb_2table_rxjava_mvvm.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thohao.roomdb_2table_rxjava_mvvm.R;
import com.thohao.roomdb_2table_rxjava_mvvm.model.Students;
import com.thohao.roomdb_2table_rxjava_mvvm.utils.DataConverter;

public class DialogShowStudent extends AppCompatDialogFragment {
    private TextView mName;
    private TextView mAge;
    private TextView mAddress;
    private ImageView mImageview;
    private Bitmap mBitmap;
    private OnShowStudentLayer onShowStudentLayer;
    private static final String TAG = "ccc_dialogstudentupdate";
    private Students students;

    public void setStudents(Students students) {
        this.students = students;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_show_student_layout, null);

        builder.setView(view);
        builder.setCancelable(true);
        builder.setTitle(null);

        mName = view.findViewById(R.id.txt_name);
        mAge = view.findViewById(R.id.txt_age);
        mAddress = view.findViewById(R.id.txt_address);
        mImageview = view.findViewById(R.id.image_view);

//        String name =students.getName();
//        String age =students.getAge();
//        String address =students.getAddress();
        mName.setText("Name: " + students.getName());
        mAge.setText("Age: " + students.getAge());
        mAddress.setText("Address: " + students.getAddress());
        mImageview.setImageBitmap(DataConverter.convertByteArrayToImage(students.getImage()));
        return builder.create();
    }

    public interface OnShowStudentLayer {
        void showStudents(Students students);
    }
}
