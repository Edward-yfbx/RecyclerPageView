package com.yfbx.demo.adapter;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yfbx.demo.bean.Bean;
import com.yfbx.recyclerpageview.R;

import java.util.List;

/**
 * Author:Edward
 * Date:2018/3/22
 * Description:Normal Adapter
 */

public class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.MyViewHolder> {


    private List<Bean> data;

    public NormalAdapter(List<Bean> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemTxt.setText(data.get(position).getName());
        holder.indexTxt.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemTxt;
        TextView indexTxt;

        MyViewHolder(View itemView) {
            super(itemView);
            itemTxt = itemView.findViewById(R.id.txt);
            indexTxt = itemView.findViewById(R.id.indexTxt);
        }
    }
}
