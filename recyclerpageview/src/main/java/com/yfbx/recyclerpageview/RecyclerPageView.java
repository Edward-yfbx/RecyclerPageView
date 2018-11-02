package com.yfbx.recyclerpageview;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Author:Edward
 * Date:2018/7/4
 * Description:
 */

public class RecyclerPageView extends RecyclerView {

    private static final String TAG = "RecyclerPageView";

    private boolean isHandled;
    private boolean btnFlag;
    private int oldState;
    private int draggingX = 0;
    private int settingX = 0;

    private Handler handler;
    private GridLayoutManager manager;
    private int pageItemSize;
    private boolean isScrolling;
    private OnRecyclerPageChangeListener pageChangeListener;
    private OnLoadMoreListener loadMoreListener;


    public RecyclerPageView(Context context) {
        super(context);
    }

    public RecyclerPageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerPageView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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

    /**
     * 滑动监听
     */
    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        isScrolling = state != RecyclerView.SCROLL_STATE_IDLE;
        //按钮翻页
        if (btnFlag && state == RecyclerView.SCROLL_STATE_IDLE) {
            notifyPageChange();
            btnFlag = false;
        }
        //当滑动状态不为IDLE时，说明重新开始滑动，重置翻页事件处理状态
        if (state != RecyclerView.SCROLL_STATE_IDLE) {
            isHandled = false;
        }
        //滑动翻页
        if (state == RecyclerView.SCROLL_STATE_IDLE && oldState == RecyclerView.SCROLL_STATE_SETTLING) {
            if (draggingX * settingX > 0) {
                notifyPageChange();
            }
            draggingX = 0;
            settingX = 0;
        }
        oldState = state;
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (handler != null) {
            handler.post(loadFirstMore);
        }

        if (oldState == RecyclerView.SCROLL_STATE_DRAGGING && draggingX == 0) {
            draggingX = dx;
        }
        if (oldState == RecyclerView.SCROLL_STATE_SETTLING && settingX == 0) {
            settingX = dx;
        }
    }

    /**
     * 翻页事件
     */
    private void notifyPageChange() {
        if (!isHandled && pageChangeListener != null) {
            isHandled = pageChangeListener.onPageChanged(getCurrentPage());
        }
        //最后一页可见
        int lastVisiblePage = getItemCount() - pageItemSize;
        if (manager.findLastVisibleItemPosition() > lastVisiblePage) {
            if (loadMoreListener != null) {
                loadMoreListener.loadMore(getCurrentPage() + 1);
            }
        }
    }


    /**
     * 设置当前页
     */
    public void setCurrentIndex(int index) {
        //如果页面正在滚动则不处理
        if (isScrolling) {
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
        btnFlag = true;
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
     * 总页数
     */
    public int getTotalPage() {
        int pageItemSize = manager.getSpanCount();
        int totalSize = manager.getItemCount();
        return totalSize == 0 ? 0 : (totalSize + pageItemSize - 1) / pageItemSize;
    }

    /**
     * 总条目数
     */
    public int getItemCount() {
        return manager.getItemCount();
    }

    /**
     * 每页条目数
     */
    public int getPageItemSize() {
        return pageItemSize;
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
        handler = new Handler();
    }


    private Runnable loadFirstMore = new Runnable() {
        @Override
        public void run() {
            loadFirstMore();
        }
    };

    /**
     * 当前总item数量 <= 每页展示item数量时，说明只有一页数据，
     * 需要自动加载下一页，以触发翻页事件
     */
    private void loadFirstMore() {
        int count = getItemCount();
        if (count > 0 && count <= pageItemSize) {
            //重置翻页事件处理状态，否则无法触发翻页事件
            isHandled = false;
            notifyPageChange();
            //最后一页可见,无法触发notifyPageChange中的loadMore,所以单独调用loadMore;
            loadMoreListener.loadMore(getCurrentPage() + 1);
        }
    }
}
