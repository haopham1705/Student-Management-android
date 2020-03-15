package com.thohao.roomdb_2table_rxjava_mvvm.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thohao.roomdb_2table_rxjava_mvvm.R;
import com.thohao.roomdb_2table_rxjava_mvvm.model.Students;
import com.thohao.roomdb_2table_rxjava_mvvm.utils.DataConverter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

public class DialogStudentAdd extends AppCompatDialogFragment {

    private EditText mTxtName;
    private EditText mTxtAge;
    private EditText mTxtAddress;
    private ImageView mImageview;
    private MaterialCardView mImageSelectBtn;
    private Button mSaveButton;
    private Bitmap mBitmap;
    private OnCreateStudentListener onCreateStudentListener;
    private static final String TAG = "ccc_dialogstudentadd";
    //date picker
    final Calendar myCalendar = Calendar.getInstance();
    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mTxtAge.setText(sdf.format(myCalendar.getTime()));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_student_layout, null);

        builder.setView(view);
        builder.setCancelable(true);
        builder.setTitle(null);

        mTxtName = view.findViewById(R.id.editext_name);
        mTxtAge = view.findViewById(R.id.edittext_age);
        mTxtAddress = view.findViewById(R.id.edittext_address);
        mImageSelectBtn = view.findViewById(R.id.select_image);
        mSaveButton = view.findViewById(R.id.btn_save);
        mImageview = view.findViewById(R.id.image_view);

        //date picker
        mTxtAge.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    new DatePickerDialog(getActivity(), date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return true;
            }
        });
        /*mTxtAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });*/


        mImageSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//Go to gallery
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });
//button save
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mTxtName.getText().toString();
                String age = mTxtAge.getText().toString();
                String address = mTxtAddress.getText().toString();

                if (!name.isEmpty() && !age.isEmpty() && !address.isEmpty() && mBitmap != null) {
                    Students students = new Students(name, age, address, DataConverter.convertImageToByteArray(mBitmap));
                    onCreateStudentListener.saveNewStudent(students);
                    Log.d(TAG, "Saved");
                    Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });

        return builder.create();
    }

    //activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                File imageFile = new File(getRealPathFromURI(selectedImage));
                try {
                    mBitmap = new Compressor(getActivity()).compressToBitmap(imageFile);
                    mImageview.setImageBitmap(mBitmap);
                    Log.d(TAG, "Activity Result");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onCreateStudentListener = (OnCreateStudentListener) context;
    }

   /* @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = new SimpleDateFormat("MM/dd/yyyy",
                Locale.UK).format(myCalendar.getTime());
        mTxtAge.setText(selectedDate);

    }*/

    //interface
    public interface OnCreateStudentListener {
        void saveNewStudent(Students students);

    }


}
