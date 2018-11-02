package com.yfbx.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yfbx.demo.adapter.BaseRecyclerAdapter;
import com.yfbx.demo.adapter.TestAdapter;
import com.yfbx.demo.bean.Bean;
import com.yfbx.recyclerpageview.OnLoadMoreListener;
import com.yfbx.recyclerpageview.OnRecyclerPageChangeListener;
import com.yfbx.recyclerpageview.R;
import com.yfbx.recyclerpageview.RecyclerPageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements BaseRecyclerAdapter.OnItemClickListener<Bean>, OnRecyclerPageChangeListener, OnLoadMoreListener {

    private static final String TAG = "MainActivity";

    private TextView pageTxt;
    private RecyclerPageView recyclerView;
    private TestAdapter adapter;
    private int totalPage = 5;
    private List<Bean> data = new ArrayList<>();

    private static final int pageSize = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pageTxt = findViewById(R.id.page_txt);


        recyclerView = findViewById(R.id.recycler);
        adapter = new TestAdapter(data);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter, pageSize);
        recyclerView.setOnPageChangeListener(this);
        recyclerView.setOnLoadMoreListener(this);
        //加载数据
        loadData(0);

    }


    /**
     * 模拟加载数据
     */
    private void loadData(int pageIndex) {
        if (pageIndex >= totalPage) {
            return;
        }
        int start = pageIndex * pageSize;
        int end = (pageIndex + 1) * pageSize;
        for (int i = start; i < end; i++) {
            data.add(new Bean("Item" + i));
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 点击事件
     */
    @Override
    public void onItemClick(Bean data, int position) {
        Toast.makeText(this, position + ":" + data.getName(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 刷新
     */
    public void onRefresh(View view) {
        Log.i(TAG, "onRefresh: 刷新数据");
        data.clear();
        //recyclerView.refresh();
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
        int total = totalPage * pageSize / recyclerView.getPageItemSize();
        String page = currentPage + 1 + "/" + total;
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
