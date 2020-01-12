package com.thohao.roomdb_2table_rxjava_mvvm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thohao.roomdb_2table_rxjava_mvvm.R;
import com.thohao.roomdb_2table_rxjava_mvvm.model.Students;
import com.thohao.roomdb_2table_rxjava_mvvm.utils.DataConverter;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<Students>studentsList;
    public StudentAdapter(List<Students> studentsList) {
        this.studentsList = studentsList;
    }

    @NonNull
    @Override
    public StudentAdapter.StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_student_layout, null);
        StudentViewHolder studentViewHolder = new StudentViewHolder(view);
        return studentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.StudentViewHolder holder, int position) {
        Students students = studentsList.get(position);
        holder.mTxtName.setText(students.getName());
        holder.mTxtAge.setText(students.getAge());
        holder.mTxtAddress.setText(students.getAddress());
        holder.mStudentImage.setImageBitmap(DataConverter.convertByteArrayToImage(students.getImage()));

    }

    public Students getStudentAt(int positon) {
        Students students = studentsList.get(positon);
        students.setId(studentsList.get(positon).getId());
        return students;
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView mTxtName;
        public TextView mTxtAge;
        public TextView mTxtAddress;
        public ImageView mStudentImage;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            mTxtName=itemView.findViewById(R.id.textview_name);
            mTxtAge = itemView.findViewById(R.id.textview_age);
            mTxtAddress = itemView.findViewById(R.id.textview_address);
            mStudentImage = itemView.findViewById(R.id.image_view);
        }
    }
}
