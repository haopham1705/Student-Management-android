package com.thohao.roomdb_2table_rxjava_mvvm.view;

import android.Manifest;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thohao.roomdb_2table_rxjava_mvvm.R;
import com.thohao.roomdb_2table_rxjava_mvvm.model.Classes;
import com.thohao.roomdb_2table_rxjava_mvvm.utils.DataConverter;
import com.vanniktech.rxpermission.RealRxPermission;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

public class DialogClassAdd extends AppCompatDialogFragment {

    private EditText mTxtClassName;
    private ImageView mImageView;
    private Button mBtnSave;
    private MaterialCardView mImageSelectButton;
    private Bitmap mBitmap;
    private CreateClassListener mListener;

    private static final String TAG = "ccc_dialogclassadd";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_class_layout, null);

        builder.setView(view);
        builder.setCancelable(true);
        builder.setTitle(null);

        mTxtClassName = view.findViewById(R.id.edittext_className);
        mImageSelectButton = view.findViewById(R.id.select_image);
        mImageView = view.findViewById(R.id.image_view);
        mBtnSave = view.findViewById(R.id.btn_save);

        mImageSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check permission
                RealRxPermission.getInstance(getActivity())
                        .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe();

                //go to gallery choice image
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String class_name = mTxtClassName.getText().toString();
                if (!class_name.isEmpty() && mBitmap != null) {
                    Classes classes = new Classes(class_name, DataConverter.convertImageToByteArray(mBitmap));
                    mListener.saveNameClass(classes);
                    dismiss();
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
                    mImageView.setImageBitmap(mBitmap);
                    Log.d(TAG, "OnActivityResult: ");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreateClassListener) context;
    }

    public interface CreateClassListener {
        void saveNameClass(Classes classes);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result ;
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
}
