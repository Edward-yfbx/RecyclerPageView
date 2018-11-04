package com.yfbx.recyclerpageview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.yfbx.recyclerpageview.adapter.OnBindViewListener;
import com.yfbx.recyclerpageview.adapter.PageAdapter;

import java.util.List;

/**
 * Author:Edward
 * Date:2018/7/4
 * Description:
 */

public class RecyclerPageView extends RecyclerView {

    private static final String TAG = "RecyclerPageView";

    private ScrollHelper helper;
    private GridLayoutManager manager;
    private OnRecyclerPageChangeListener pageChangeListener;
    private OnLoadMoreListener loadMoreListener;

    private int pageItemSize;
    private int loadedPageIndex;
    private int computeTotalPage;
    private View emptyView;


    public RecyclerPageView(Context context) {
        this(context, null);
    }

    public RecyclerPageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerPageView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        helper = new ScrollHelper(this);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        helper.onScrollStateChanged(state);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        helper.onScrolled(dx, dy);
    }

    /**
     * 空视图
     */
    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    /**
     * 若未在布局中设置 LayoutManager 和 spanCount 属性则调用该方法
     * 在设置Adapter的时候进行初始化
     */
    public void setAdapter(Adapter adapter, int pageItemSize) {
        GridLayoutManager manager = new GridLayoutManager(getContext(), pageItemSize);
        manager.setOrientation(HORIZONTAL);
        setLayoutManager(manager);
        setAdapter(adapter);
    }

    /**
     * 若在布局中设置了 LayoutManager 和 spanCount 属性则调用该方法
     */
    @Override
    public void setAdapter(Adapter adapter) {
        manager = (GridLayoutManager) getLayoutManager();
        pageItemSize = manager.getSpanCount();
        PageSnapHelper snapHelper = new PageSnapHelper();
        snapHelper.attachToRecyclerView(this);
        super.setAdapter(adapter);
    }


    public void refresh() {
        loadedPageIndex = 0;
        Log.i(TAG, "开始刷新,loadedPageIndex = " + loadedPageIndex);
    }

    public void completeRefresh(int totalItems) {
        checkEmpty();
        Log.i(TAG, "数据加载完成,loadedPageIndex = " + loadedPageIndex);
        Log.i(TAG, "---------------------------------------------");
        if (loadedPageIndex == 0) {
            computeTotalPage = (totalItems + pageItemSize - 1) / pageItemSize;
            Log.i(TAG, "刷新完成，首次预加载");
            notifyPageChange();
        }
        getAdapter().notifyDataSetChanged();
    }


    /**
     * 翻页事件
     */
    protected void notifyPageChange() {
        if (pageChangeListener != null) {
            Log.i(TAG, "notifyPageChange: 翻页" + getCurrentPage());
            pageChangeListener.onPageChanged(getCurrentPage());
        }
        loadNextPage();
    }

    /**
     * 加载下一页数据
     */
    private void loadNextPage() {
        if (loadMoreListener != null) {
            int nextPageIndex = loadedPageIndex + 1;
            if (nextPageIndex <= computeTotalPage) {
                loadedPageIndex = nextPageIndex;
                loadMoreListener.loadMore(nextPageIndex);
            }
        }
    }

    /**
     * 是否展示空视图
     */
    private void checkEmpty() {
        if (emptyView != null) {
            int visible = getItemCount() == 0 ? VISIBLE : GONE;
            emptyView.setVisibility(visible);
        }
    }

    /**
     * 设置当前页
     */
    public void setCurrentIndex(int index) {
        //如果页面正在滚动则不处理
        if (helper.isScrolling()) {
            return;
        }
        //页码越界，页码等于当前页码,不处理
        int currentPage = getCurrentPage();
        if (index >= getTotalPage() || index < 0 || index == currentPage) {
            return;
        }
        int gap = (index - currentPage) * pageItemSize + 1;
        int position = manager.findFirstVisibleItemPosition() + gap;
        manager.smoothScrollToPosition(this, null, position);
        helper.setFlag(true);
    }

    /**
     * 上一页
     */
    public void toPrePage() {
        setCurrentIndex(getCurrentPage() - 1);
    }

    /**
     * 下一页
     */
    public void toNextPage() {
        setCurrentIndex(getCurrentPage() + 1);
    }

    /**
     * 获取当前页码
     */
    public int getCurrentPage() {
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
        return firstVisibleItemPosition / manager.getSpanCount();
    }

    /**
     * 实际已加载总页数
     */
    public int getTotalPage() {
        int pageItemSize = manager.getSpanCount();
        int totalSize = manager.getItemCount();
        return totalSize == 0 ? 0 : (totalSize + pageItemSize - 1) / pageItemSize;
    }

    /**
     * 根据返回数据计算的总页数
     */
    public int getComputeTotalPage() {
        return computeTotalPage;
    }

    /**
     * 总条目数
     */
    public int getItemCount() {
        return manager.getItemCount();
    }


    /**
     * 设置翻页监听
     */
    public void setOnPageChangeListener(OnRecyclerPageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
    }

    /**
     * 设置加载更多监听
     */
    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }


    public <T> void setPageAdapter(@LayoutRes int itemLayout, List<T> data, OnBindViewListener listener) {
        setAdapter(new PageAdapter<>(itemLayout, data, listener));
    }

    public <T> void setPageAdapter(@LayoutRes int itemLayout, int pageItemSize, List<T> data, OnBindViewListener listener) {
        setAdapter(new PageAdapter<>(itemLayout, data, listener), pageItemSize);
    }

    public void notifyDataSetChanged() {
        getAdapter().notifyDataSetChanged();
    }


}
