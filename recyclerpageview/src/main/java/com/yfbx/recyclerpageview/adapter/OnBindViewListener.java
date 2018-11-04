package com.yfbx.recyclerpageview.adapter;

import android.support.annotation.NonNull;

/**
 * Author: Edward
 * Date: 2018/11/4
 * Description:
 */


public interface OnBindViewListener {

    void onBindItemView(@NonNull PageViewHolder holder, int position);
}
