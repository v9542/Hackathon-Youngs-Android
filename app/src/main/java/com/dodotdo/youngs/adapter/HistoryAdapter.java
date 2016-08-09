package com.dodotdo.youngs.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.CountDownTimer;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dodotdo.youngs.R;
import com.dodotdo.youngs.data.History;
import com.dodotdo.youngs.view.MyRecyclerView;
import com.dodotdo.youngs.view.ViewHolderFactory;

import java.io.File;
import java.io.FileDescriptor;
import java.util.List;


/**
 * Created by kimyebon on 2016.08.06
 */
public class HistoryAdapter extends RecyclerView.Adapter<MyRecyclerView.ViewHolder> {

    public Activity mContext;

    private List<History> list;

    private final int VIEWTYPE_ITEM = 1;

    public HistoryAdapter(Activity activity, List<History> list) {
        mContext = activity;
        this.list = list;
        setHasStableIds(true);
    }


    public MyRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view;
        switch (viewType) {
            case VIEWTYPE_ITEM:
                view = LayoutInflater.from(context).inflate(R.layout.viewholder_history, parent, false);
                return new HistoryViewHolder(view);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
      return position;
    };

    @Override
    public void onBindViewHolder(final MyRecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolderFactory.Updateable) {
            ((ViewHolderFactory.Updateable) holder).update(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEWTYPE_ITEM;
    }

    private class HistoryViewHolder extends RecyclerView.ViewHolder implements ViewHolderFactory.Updateable<History> {
        Context context;
        ImageView profile;
        Button play;
        View view;
        boolean isPlaying;

        public HistoryViewHolder(View view) {
            super(view);
            this.view = view;
            context =view.getContext();
            profile = (ImageView)view.findViewById(R.id.profile);
            play = (Button)view.findViewById(R.id.play);
            isPlaying=false;
        }

        @Override
        public void update(final History data) {
            Glide.with(context).load(data.getMember().getProfile_url()).into(profile);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isPlaying) {
                        play.setBackground(context.getResources().getDrawable(R.drawable.play));
                    }else {
                        data.getMp3File_url();
                        play.setBackground(context.getResources().getDrawable(R.drawable.stop));
                    }

                    isPlaying = !isPlaying;
                }
            });
        }

    }


}