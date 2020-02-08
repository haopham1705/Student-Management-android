package com.thohao.roomdb_2table_rxjava_mvvm.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.thohao.roomdb_2table_rxjava_mvvm.R;
import com.thohao.roomdb_2table_rxjava_mvvm.database.dao.OnListener;
import com.thohao.roomdb_2table_rxjava_mvvm.model.Students;
import com.thohao.roomdb_2table_rxjava_mvvm.utils.DataConverter;
import com.thohao.roomdb_2table_rxjava_mvvm.view.StudentsActivity;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<Students> studentsList;
    private OnStudentClickListener onStudentClickListener;

    public StudentAdapter(List<Students> studentsList) {
        this.studentsList = studentsList;
    }

    public void setItemClickListener(OnStudentClickListener onStudentClickListener) {
        this.onStudentClickListener = onStudentClickListener;
    }

    @NonNull
    @Override
    public StudentAdapter.StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_student_layout, null);
        StudentViewHolder studentViewHolder = new StudentViewHolder(view, onStudentClickListener);
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

    public class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTxtName;
        private TextView mTxtAge;
        private TextView mTxtAddress;
        private ImageView mStudentImage;
        private MaterialCardView mCardView;


        private OnStudentClickListener onStudentClickListener;

        public StudentViewHolder(@NonNull View itemView, OnStudentClickListener onStudentClickListener) {
            super(itemView);
            this.onStudentClickListener = onStudentClickListener;
            mTxtName = itemView.findViewById(R.id.textview_name);
            mTxtAge = itemView.findViewById(R.id.textview_age);
            mTxtAddress = itemView.findViewById(R.id.textview_address);
            mStudentImage = itemView.findViewById(R.id.image_view);
            mCardView = itemView.findViewById(R.id.select_image);
            mCardView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Students currentStudents = studentsList.get(position);
//            Students students = new Students(currentStudents.getName(), currentStudents.getAge(), currentStudents.getAddress(), currentStudents.getImage());
//            students.setId(currentStudents.getId());
            onStudentClickListener.onStudentClick(currentStudents);
        }
    }
    public interface OnStudentClickListener {
        void onStudentClick(Students students);
    }
}