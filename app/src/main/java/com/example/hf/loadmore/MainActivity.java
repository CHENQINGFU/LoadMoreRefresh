package com.example.hf.loadmore;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private List<String> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager layoutManager;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private int lastVisibleItem = 0;
    private final int LoadCountFirst = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initData();
        initRecyclerView();
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // 开始刷新，设置当前为刷新状态
                //swipeRefreshLayout.setRefreshing(true);

                // 这里是主线程
                // 一些比较耗时的操作，比如联网获取数据，需要放到子线程去执行
                final Random random = new Random();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> dataList = new ArrayList<String>();
                        dataList.add("1");
                        Log.d("HF dataList.get(0)", dataList.get(0));
                        adapter.refresh(dataList);
                        Toast.makeText(MainActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();

                        // 加载完数据设置为不刷新状态，将下拉进度收起来
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);

                // 这个不能写在外边，不然会直接收起来
                //swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(this, getDatas(0, LoadCountFirst), getDatas(0, LoadCountFirst).size() > 0 ? true : false);
        recyclerView.setAdapter(adapter);
        //加载动画
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (adapter.isFadeTips() == false && lastVisibleItem + 1 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + LoadCountFirst);
                            }
                        }, 500);
                    }

                    if (adapter.isFadeTips() == true && lastVisibleItem + 2 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //上拉载入条数+LoadCountFirst
                                updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + LoadCountFirst);
                            }
                        }, 500);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //最后一条item的position
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        List<String> newDatas = getDatas(fromIndex, toIndex);
        if (newDatas.size() > 0) {
            adapter.updateList(newDatas, true);
        } else {
            adapter.updateList(null, false);
        }
    }

    private void initData() {
        for (int i = 1; i <= 100; i++)
            dataList.add("item" + i);
    }

    //获取数据item条数
    private List<String> getDatas(final int firstIndex, final int lastIndex) {
        List<String> resList = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            if (i < dataList.size()) {
                resList.add(dataList.get(i));
            }
        }
        return resList;
    }

}
