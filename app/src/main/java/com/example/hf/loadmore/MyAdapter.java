package com.example.hf.loadmore;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HF on 2017/7/5.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> dataList;
    private boolean hasMore = true;
    private boolean fadeTips = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private int normalType = 0;
    private int footType = 1;

    //屏幕包含最多的条目
    private final int CONTENT_SCREEN = 5;

    public MyAdapter(Context context, List<String> dataList, boolean hasMore){
        this.mContext = context;
        this.dataList = dataList;
        this.hasMore = hasMore;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == normalType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
            final NormalViewHolder normalViewHolder = new NormalViewHolder(view);
            normalViewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = normalViewHolder.getAdapterPosition();
                    String s = dataList.get(position);
                    Intent intent = new Intent(mContext, MainDetail.class);
                    intent.putExtra("detail", s);
                    Log.d("HF", s);
                    mContext.startActivity(intent);
                }
            });
            return normalViewHolder;
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.load_more_footview_layout, parent, false);
            FootViewHolder footViewHolder = new FootViewHolder(view);
            Log.d("HF", "HF");
            return footViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
 /*       NormalViewHolder normalViewHolder = (NormalViewHolder)holder;
        normalViewHolder.itemTv.setText(dataList.get(position).toString());*/
            if (holder instanceof NormalViewHolder) {
                ((NormalViewHolder) holder).itemTv.setText(dataList.get(position));
            } else {
                ((FootViewHolder) holder).loadMore.setVisibility(View.VISIBLE);
                if (hasMore == true) {
                    fadeTips = false;
                    if (dataList.size() >= CONTENT_SCREEN) {
                        ((FootViewHolder) holder).mProgressBar.setVisibility(View.VISIBLE);
                        ((FootViewHolder) holder).loadMore.setText("正在加载更多...");
                    }
                } else {
                    if (dataList.size() >= CONTENT_SCREEN) {
                        ((FootViewHolder) holder).mProgressBar.setVisibility(View.GONE);
                        ((FootViewHolder) holder).loadMore.setText("没有更多数据了");
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((FootViewHolder) holder).loadMore.setVisibility(View.GONE);
                                fadeTips = true;
                                hasMore = true;
                            }
                        }, 500);
                    }
                }
            }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    //真实dataList长度
    public int getRealLastPosition() {
        return dataList.size();
    }

    public void updateList(List<String> newDatas, boolean hasMore) {
        if (newDatas != null) {
            dataList.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }


    public boolean isFadeTips() {
        return fadeTips;
    }


    @Override
    public int getItemViewType(int position) {
        //若position为最后一条item的位置
        if (position == getItemCount() - 1) {
            return footType;
        } else {
            return normalType;
        }
    }

    public void refresh(List<String> items) {
        dataList.addAll(0, items);
        notifyDataSetChanged();
    }

    class NormalViewHolder extends RecyclerView.ViewHolder{

        private TextView itemTv;
        private View view;
        public NormalViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            itemTv = (TextView)itemView.findViewById(R.id.item_tv);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder{

        private TextView loadMore;
        private ProgressBar mProgressBar;

        public FootViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar)itemView.findViewById(R.id.pbLoad);
            loadMore = (TextView)itemView.findViewById(R.id.load_more_tv);
        }
    }
}
