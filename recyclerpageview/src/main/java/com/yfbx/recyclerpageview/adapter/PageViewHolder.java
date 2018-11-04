package com.yfbx.recyclerpageview.adapter;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author: Edward
 * Date: 2018/11/4
 * Description:
 */


public class PageViewHolder extends RecyclerView.ViewHolder {

    public PageViewHolder(View itemView) {
        super(itemView);
    }

    public View getItemView() {
        return itemView;
    }

    public View getView(@IdRes int resId) {
        return itemView.findViewById(resId);
    }
}
