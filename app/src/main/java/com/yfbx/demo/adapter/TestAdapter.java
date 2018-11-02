package com.yfbx.demo.adapter;


import com.yfbx.recyclerpageview.R;
import com.yfbx.demo.bean.Bean;

import java.util.List;

/**
 * Author:Edward
 * Date:2018/3/22
 * Description:
 */

public class TestAdapter extends BaseRecyclerAdapter<Bean> {


    public TestAdapter(List<Bean> data) {
        super(data);
    }


    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_text;
    }

    @Override
    public void onBindData(XHolder holder, int position, Bean bean) {
        holder.setText(R.id.txt, bean.getName());
        holder.setText(R.id.indexTxt, String.valueOf(position + 1));
    }
}
