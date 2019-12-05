package com.yfbx.recyclerpageview;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Edward
 * Date: 2018/11/4
 * Description: 通过RecyclerView的滚动状态变化，来判断翻页事件.
 * 放在RecyclerPageView中显得臃肿，故抽出做成Helper
 */


public class ScrollHelper {

    private int oldState;
    private int draggingX = 0;
    private int settingX = 0;
    private boolean flag;
    private boolean scrolling;
    private RecyclerPageView recyclerPageView;


    ScrollHelper(RecyclerPageView recyclerPageView) {
        this.recyclerPageView = recyclerPageView;
    }

    void onScrollStateChanged(int state) {
        scrolling = state != RecyclerView.SCROLL_STATE_IDLE;
        //按钮翻页
        if (flag && state == RecyclerView.SCROLL_STATE_IDLE) {
            recyclerPageView.notifyPageChange();
            flag = false;
        }

        //滑动翻页
        if (state == RecyclerView.SCROLL_STATE_IDLE && oldState == RecyclerView.SCROLL_STATE_SETTLING) {
            if (draggingX * settingX > 0) {
                recyclerPageView.notifyPageChange();
            }
            draggingX = 0;
            settingX = 0;
        }
        oldState = state;
    }

    void onScrolled(int dx, int dy) {
        if (oldState == RecyclerView.SCROLL_STATE_DRAGGING && draggingX == 0) {
            draggingX = dx;
        }
        if (oldState == RecyclerView.SCROLL_STATE_SETTLING && settingX == 0) {
            settingX = dx;
        }
    }


    boolean isScrolling() {
        return scrolling;
    }

    void setFlag(boolean flag) {
        this.flag = flag;
    }
}
