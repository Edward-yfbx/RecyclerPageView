package com.yfbx.demo.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Author:Edward
 * Date:2018/7/26
 * Description:
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.XHolder> {

    private List<T> data;
    private OnItemClickListener listener;

    public BaseRecyclerAdapter(List<T> data) {
        this.data = data;
    }


    @Override
    public XHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        return new XHolder(view);
    }

    @Override
    public void onBindViewHolder(XHolder holder, int position) {
        onBindData(holder, position, data.get(position));

        final int index = position;
        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(data.get(index), index);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    @LayoutRes
    public abstract int getLayoutId(int viewType);

    public abstract void onBindData(XHolder holder, int position, T t);


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Item Click Interface
     */
    public interface OnItemClickListener<T> {

        void onItemClick(T t, int position);
    }


    public static class XHolder extends RecyclerView.ViewHolder {

        private View itemView;

        public XHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public View getItemView() {
            return itemView;
        }

        public View getView(@IdRes int resId) {
            return itemView.findViewById(resId);
        }

        public TextView getTextView(@IdRes int resId) {
            return itemView.findViewById(resId);
        }

        public ImageView getImageView(@IdRes int resId) {
            return itemView.findViewById(resId);
        }

        public ImageView getRadioButton(@IdRes int resId) {
            return itemView.findViewById(resId);
        }

        public CheckBox getCheckBox(@IdRes int resId) {
            return itemView.findViewById(resId);
        }

        public void setText(@IdRes int resId, String text) {
            View view = getView(resId);
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            }
        }

        public void setItemRes(@IdRes int resId, @DrawableRes int imgRes) {
            ImageView view = getImageView(resId);
            view.setImageResource(imgRes);
        }

        public void setChecked(@IdRes int resId, boolean checked) {
            CompoundButton button = (CompoundButton) getView(resId);
            button.setChecked(checked);
        }

        public void setVisible(@IdRes int resId, boolean isVisible) {
            getView(resId).setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }
}
