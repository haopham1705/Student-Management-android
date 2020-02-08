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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thohao.roomdb_2table_rxjava_mvvm.R;
import com.thohao.roomdb_2table_rxjava_mvvm.model.Students;
import com.thohao.roomdb_2table_rxjava_mvvm.utils.DataConverter;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

public class DialogStudentUpdate extends AppCompatDialogFragment {
    private EditText mName;
    private EditText mAge;
    private EditText mAddress;
    private ImageView mImageview;
    private Bitmap mBitmap;
    private Button mButtonSave;
    private MaterialCardView mImageSelectBtn;
    private DialogStudentUpdate.OnUpdateStudentLayer onUpdateStudentLayer;
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
        View view = inflater.inflate(R.layout.dialog_update_student_layout, null);

        builder.setView(view);
        builder.setCancelable(true);
        builder.setTitle(null);

        mName = view.findViewById(R.id.editext_name);
        mAge = view.findViewById(R.id.edittext_age);
        mAddress = view.findViewById(R.id.edittext_address);
        mImageview = view.findViewById(R.id.image_view);
        mImageSelectBtn = view.findViewById(R.id.select_image);
        mButtonSave = view.findViewById(R.id.btn_save);

        mName.setText(students.getName());
        mAge.setText(students.getAge());
        mAddress.setText(students.getAddress());
//get image
        mImageview.setImageBitmap(DataConverter.convertByteArrayToImage(students.getImage()));
//select image
        mImageSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString();
                String age = mAge.getText().toString();
                String address = mAddress.getText().toString();

                //ImageView img=mImageview.setImageBitmap(DataConverter.convertByteArrayToImage(students.getImage()));
                if (!name.isEmpty() && !age.isEmpty() && !address.isEmpty() && mBitmap != null) {
                    Students currentStudents = new Students(name, age, address, DataConverter.convertImageToByteArray(mBitmap));
                    currentStudents.setId(students.getId());
                    onUpdateStudentLayer.updateNewStudents(currentStudents);
                    dismiss();
                    Log.d(TAG, "Updated");

                    //getActivity() hien toast trong Dialog
                    Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "Return update item");
                    Toast.makeText(getActivity(), "Pls choose your image", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        return builder.create();
    }

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
        onUpdateStudentLayer = (OnUpdateStudentLayer) context;
    }

    public interface OnUpdateStudentLayer {
        void updateNewStudents(Students students);
    }
}
