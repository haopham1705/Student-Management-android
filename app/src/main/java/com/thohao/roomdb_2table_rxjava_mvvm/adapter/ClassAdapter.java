package com.thohao.roomdb_2table_rxjava_mvvm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.thohao.roomdb_2table_rxjava_mvvm.R;
import com.thohao.roomdb_2table_rxjava_mvvm.model.Classes;
import com.thohao.roomdb_2table_rxjava_mvvm.model.Students;
import com.thohao.roomdb_2table_rxjava_mvvm.utils.DataConverter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.AdapterViewHolder> implements Filterable {

    private List<Classes> classesList;
    private List<Classes> classesListfull;
    //undo delete
    private int mClassPosition;

    private OnClassClickListener onClassClickListener;

    //undo delete
    void setClasses(List<Classes> classes) {
        this.classesList = classes;
        notifyDataSetChanged();
    }

    public ClassAdapter(List<Classes> classesList) {
        this.classesList = classesList;
        classesListfull = new ArrayList<>(classesList);
    }

    public void setItemClickListener(OnClassClickListener onClassClickListener) {
        this.onClassClickListener = onClassClickListener;
    }

    @NonNull
    @Override
    public ClassAdapter.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item_layout, null);
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

    //Undo delete
    public void onItemDissmiss(int position) {

    }

    public void undoDelete() {
        classesList.add(mClassPosition, classesListfull.get(classesListfull.size() - 1));
        classesListfull.remove(classesListfull.size() - 1);
        notifyItemInserted(mClassPosition);
    }


    //getFilter
    @Override
    public Filter getFilter() {
        return classFilter;
    }


    //viewHolder
    public class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView mImageView;
        private TextView mClassText;
        private MaterialCardView mCardView;
        private OnClassClickListener onClassClickListener;

        public AdapterViewHolder(@NonNull View itemView, OnClassClickListener onClassClickListener) {
            super(itemView);
            this.onClassClickListener = onClassClickListener;

            mImageView = itemView.findViewById(R.id.image_view);
            mClassText = itemView.findViewById(R.id.text_view);
            mCardView = itemView.findViewById(R.id.card_view);
//mCardView setOnCLick
            mCardView.setOnClickListener(this);
            mCardView.setOnLongClickListener(this);
        }

        //onClick
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Classes currentClass = classesList.get(position);
            onClassClickListener.onClassClick(currentClass);
        }

        //long click
        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            Classes currentStudents = classesList.get(position);
            onClassClickListener.onClassLongClick(currentStudents);
            return true;
        }

        public void restoreItem() {
            int position = getAdapterPosition();
            Classes item = classesList.get(position);
            classesList.add(position, item);
            notifyItemInserted(position);

        }
    }

    //interface
    public interface OnClassClickListener {
        void onClassClick(Classes classes);

        void onClassLongClick(Classes classes);
    }


    //Fiter-Searching
    private Filter classFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Classes> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(classesListfull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Classes classes : classesListfull) {
                    if (classes.getClassname().toLowerCase().contains(filterPattern)) {
                        filteredList.add(classes);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        //publish results searching
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            classesList.clear();
            classesList.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };


}

