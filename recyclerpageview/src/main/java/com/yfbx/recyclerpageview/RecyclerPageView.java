package com.yfbx.recyclerpageview;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author:Edward
 * Date:2018/7/4
 * Description:
 */

public class RecyclerPageView extends RecyclerView {

    private static final String TAG = "RecyclerPageView";

    private Handler handler = new Handler();
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
        if (!helper.isScrolling()) {
            handler.postDelayed(resetRun, 1000);
        }
    }

    private Runnable resetRun = new Runnable() {
        @Override
        public void run() {
            reset();
        }
    };

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
        setLayoutManager(manager);
        setAdapter(adapter);
    }

    /**
     * 若在布局中设置了 LayoutManager 和 spanCount 属性则调用该方法
     */
    @Override
    public void setAdapter(Adapter adapter) {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            manager = (GridLayoutManager) getLayoutManager();
            manager.setOrientation(HORIZONTAL);
        } else {
            throw new ClassCastException("必须使用 GridLayoutManager");
        }
        pageItemSize = manager.getSpanCount();
        PageSnapHelper snapHelper = new PageSnapHelper();
        snapHelper.attachToRecyclerView(this);
        super.setAdapter(adapter);
    }


    /**
     * 刷新重置
     */
    private synchronized void reset() {
        if (getItemCount() <= pageItemSize) {
            loadedPageIndex = 0;
            scrollToPosition(0);
            notifyPageChange();
        }
    }

    /**
     * 数据刷新
     */
    public void notifyDataSetChanged() {
        checkEmpty();
        reset();
        getAdapter().notifyDataSetChanged();
    }


    /**
     * 翻页事件
     */
    protected void notifyPageChange() {
        if (pageChangeListener != null) {
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
            if (nextPageIndex < computeTotalPage) {
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
        //数据刷新后，未翻页时，页码不正确(可能是复用/缓存导致的)
        if (loadMoreListener != null && loadedPageIndex == 0) {
            return 0;
        }
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
        return firstVisibleItemPosition / manager.getSpanCount();
    }

    /**
     * 得到后台返回数据总页数
     */
    public void setTotalPage(int totalPage) {
        computeTotalPage = totalPage;
    }

    /**
     * 根据后台返回的总items数，计算总页数
     */
    public void setTotalCount(int totalCount) {
        computeTotalPage = (totalCount + pageItemSize - 1) / pageItemSize;
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
}
