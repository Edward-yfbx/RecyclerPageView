package com.yfbx.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yfbx.demo.bean.Bean;
import com.yfbx.recyclerpageview.OnLoadMoreListener;
import com.yfbx.recyclerpageview.OnRecyclerPageChangeListener;
import com.yfbx.recyclerpageview.R;
import com.yfbx.recyclerpageview.RecyclerPageView;
import com.yfbx.recyclerpageview.adapter.OnBindViewListener;
import com.yfbx.recyclerpageview.adapter.PageViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Edward
 * Date: 2018/11/4
 * Description:
 */


public class RecyclerPageActivity extends Activity implements OnRecyclerPageChangeListener, OnLoadMoreListener, OnBindViewListener {

    private static final String TAG = "RecyclerPageActivity";
    private int TOTAL_ITEMS = 50;
    private static final int PAGE_SIZE = 12;

    private TextView pageTxt;
    private RecyclerPageView recyclerView;
    private List<Bean> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        pageTxt = findViewById(R.id.page_txt);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setPageAdapter(R.layout.item_text, data, this);
        recyclerView.setOnPageChangeListener(this);
        recyclerView.setOnLoadMoreListener(this);

        loadData(0);
    }

    @Override
    public void onBindItemView(@NonNull PageViewHolder holder, int position) {
        TextView textView = (TextView) holder.getView(R.id.txt);
        TextView index = (TextView) holder.getView(R.id.indexTxt);

        Bean item = data.get(position);
        textView.setText(item.getName());
        index.setText(String.valueOf(position + 1));
    }

    /**
     * 模拟加载数据
     */
    private void loadData(int pageIndex) {
        Log.i(TAG, "加载第 " + pageIndex + " 页数据");
        int start = pageIndex * PAGE_SIZE;
        int end = (pageIndex + 1) * PAGE_SIZE;
        for (int i = start; i < end; i++) {
            if (data.size() < TOTAL_ITEMS) {
                data.add(new Bean("Item" + i));
            }
        }
        recyclerView.completeRefresh(TOTAL_ITEMS);
    }


    /**
     * 刷新
     */
    public void onRefresh(View view) {
        data.clear();
        recyclerView.refresh();
        loadData(0);
    }

    /**
     * 上一页
     */
    public void clickPrePage(View view) {
        recyclerView.toPrePage();
    }

    /**
     * 下一页
     */
    public void clickNextPage(View view) {
        recyclerView.toNextPage();
    }

    /**
     * 翻页监听
     */
    @Override
    public boolean onPageChanged(int page) {
        setPageTxt(page);
        return true;
    }

    /**
     * 设置页码
     */
    private void setPageTxt(int currentPage) {
        String page = currentPage + 1 + "/" + recyclerView.getComputeTotalPage();
        pageTxt.setText(page);
    }

    /**
     * 加载下一页数据
     */
    @Override
    public void loadMore(int nextPageIndex) {
        loadData(nextPageIndex);
    }


}
