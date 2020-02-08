package com.thohao.roomdb_2table_rxjava_mvvm.database.dao;

import android.view.View;

public interface OnListener {
    boolean onItemLongClick(View v, int position);
    void onItemClick(View v, int position);
}
