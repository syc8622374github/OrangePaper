package com.cyc.daily.module.huaban.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyc.daily.R;
import com.cyc.daily.adapter.BaseRecyclerAdapter;

/**
 * Created by cyc on 2017/1/20 0020.
 */

public class RecyclerHuaBanAdapter extends BaseRecyclerAdapter {
    public RecyclerHuaBanAdapter(RecyclerView mRecyclerView) {
        super(mRecyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_item_standard, parent, false);
        RecyclerShowAPINewsAdapter.ViewHolderGeneral holder = new RecyclerShowAPINewsAdapter.ViewHolderGeneral(view);//使用子类初始化ViewHolder
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
