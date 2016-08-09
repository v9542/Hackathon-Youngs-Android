package com.dodotdo.youngs.view;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by BJ on 2015-02-28.
 */
public abstract class ViewHolderFactory {
    public abstract RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType);


    public static interface Updateable<T> {
        public void update(T data);

    }

}