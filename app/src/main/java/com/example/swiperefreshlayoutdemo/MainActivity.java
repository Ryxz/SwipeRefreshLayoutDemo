package com.example.swiperefreshlayoutdemo;

import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.Toast;

import com.example.swiperefreshlayoutdemo.adapter.RefreshAdapter;
import com.example.swiperefreshlayoutdemo.view.RefreshItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    List<String> mDatas = new ArrayList<>();
    private RefreshAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN,Color.RED);
    }

    private void initData() {
        for (int i = 0;i<10;i++) {
            mDatas.add("item" + 1);
        }
        initRecyclerView();
    }


    private void initRecyclerView() {
        adapter = new RefreshAdapter(this,mDatas);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addItemDecoration(new RefreshItemDecoration(this,RefreshItemDecoration.VERTICAL_LIST));

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void initListener() {
        initPullRefreshListener();
        initLoadMoreListener();
    }

    private void initPullRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> headDatas = new ArrayList<>();

                        for (int i = 1;i<30;i++) {
                            headDatas.add("Heard Item" + i);
                        }
                        adapter.addHeaderItem(headDatas);
                        //刷新完成
                        swipeRefreshLayout.setRefreshing(false);

                        Toast.makeText(MainActivity.this,"更新了" + headDatas.size()+"条数据",Toast.LENGTH_LONG);
                    }
                },3000);
            }
        });
    }

    private void initLoadMoreListener() {

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //判断RecyclerView的状态 空闲且是最后一个可见的Item时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {

                    adapter.changeMoreStatus(adapter.LOADING_MORE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<String> footerDatas = new ArrayList<>();
                            for (int i = 0;i < 10;i++) {
                                footerDatas.add("footer item" + i);
                            }
                            adapter.addFooterItem(footerDatas);

                            adapter.changeMoreStatus(adapter.PUBLIC_LOAD_MORE);
                            Toast.makeText(MainActivity.this,"更新了" + footerDatas.size() + "条数据",Toast.LENGTH_LONG);
                        }
                    },3000);
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem=layoutManager.findLastVisibleItemPosition();
            }
        });
    }


}
