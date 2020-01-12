package com.thohao.roomdb_2table_rxjava_mvvm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.thohao.roomdb_2table_rxjava_mvvm.R;
import com.thohao.roomdb_2table_rxjava_mvvm.model.Classes;
import com.thohao.roomdb_2table_rxjava_mvvm.utils.DataConverter;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.AdapterViewHolder> {

    private List<Classes> classesList;
    private OnClassClickListener onClassClickListener;
    public ClassAdapter(List<Classes> classesList) {
        this.classesList=classesList;
    }

    public void setItemClickListener(OnClassClickListener onClassClickListener) {
        this.onClassClickListener = onClassClickListener;
    }

    @NonNull
    @Override
    public ClassAdapter.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item_layout,null);
        AdapterViewHolder adapterViewHolder = new AdapterViewHolder(view, onClassClickListener);

        return adapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClassAdapter.AdapterViewHolder holder, int position) {
        Classes singleClass = classesList.get(position);
        holder.mImageView.setImageBitmap(DataConverter.convertByteArrayToImage(singleClass.getImage()));
        holder.mClassText.setText(singleClass.getClassname());
    }

    public Classes getClassAt(int positon) {
        Classes classes = classesList.get(positon);
        classes.setId(classesList.get(positon).getId());
        return classes;
    }

    @Override
    public int getItemCount() {
        return classesList.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageView;
        private TextView mClassText;
        private MaterialCardView mCardView;
        private OnClassClickListener onClassClickListener;

        public AdapterViewHolder(@NonNull View itemView, OnClassClickListener onClassClickListener) {
            super(itemView);
            this.onClassClickListener=onClassClickListener;
            mImageView=itemView.findViewById(R.id.image_view);
            mClassText = itemView.findViewById(R.id.text_view);
            mCardView = itemView.findViewById(R.id.card_view);
            mCardView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Classes currentClass = classesList.get(position);
            onClassClickListener.onClassClick(currentClass);

        }
    }
    public interface OnClassClickListener{
        void onClassClick(Classes classes);
    }
}
