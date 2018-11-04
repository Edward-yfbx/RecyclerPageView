package com.yfbx.recyclerpageview.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Author: Edward
 * Date: 2018/11/4
 * Description:
 */


public class PageAdapter<T> extends RecyclerView.Adapter<PageViewHolder> {

    private int itemLayout;
    private List<T> data;
    private OnBindViewListener listener;

    public PageAdapter(@LayoutRes int itemLayout, List<T> data, OnBindViewListener listener) {
        this.itemLayout = itemLayout;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        listener.onBindItemView(holder, position);
    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

}
