package com.yfbx.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yfbx.demo.adapter.NormalAdapter;
import com.yfbx.demo.bean.Bean;
import com.yfbx.recyclerpageview.OnLoadMoreListener;
import com.yfbx.recyclerpageview.OnRecyclerPageChangeListener;
import com.yfbx.recyclerpageview.R;
import com.yfbx.recyclerpageview.RecyclerPageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Edward
 * Date: 2018/11/4
 * Description:
 */


public class RecyclerPageActivity extends Activity implements OnRecyclerPageChangeListener, OnLoadMoreListener {

    private static final String TAG = "RecyclerPageActivity";
    private int TOTAL_ITEMS = 50;
    private static final int PAGE_SIZE = 12;

    private TextView pageTxt;
    private RecyclerPageView recyclerView;
    private NormalAdapter adapter;
    private List<Bean> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        pageTxt = findViewById(R.id.page_txt);

        recyclerView = findViewById(R.id.recycler);
        adapter = new NormalAdapter(data);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnPageChangeListener(this);
        recyclerView.setOnLoadMoreListener(this);

        loadData(0);
    }


    /**
     * 模拟加载数据
     */
    private void loadData(int pageIndex) {
        int start = pageIndex * PAGE_SIZE;
        int end = (pageIndex + 1) * PAGE_SIZE;
        for (int i = start; i < end; i++) {
            if (data.size() < TOTAL_ITEMS) {
                data.add(new Bean("Item" + i));
            }
        }
        if (pageIndex == 0) {
            recyclerView.setTotalCount(TOTAL_ITEMS);
        }
        recyclerView.notifyDataSetChanged();
    }


    /**
     * 刷新
     */
    public void onRefresh(View view) {
        data.clear();
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
