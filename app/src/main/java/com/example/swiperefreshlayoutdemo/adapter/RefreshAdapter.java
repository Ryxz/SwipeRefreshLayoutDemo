package com.example.swiperefreshlayoutdemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swiperefreshlayoutdemo.R;

import java.util.List;

/**
 * @Author Ryx on 2018/12/24
 */
public class RefreshAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    List<String> mData;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    //上拉加载更多
    public static final int PUBLIC_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //没有正在加载  隐藏
    public static final int NO_LOAD_MORE = 2;

    //上拉加载更多状态  默认为0
    public int LOAD_MORE_STATUS = 0;



//    public RefreshAdapter(Context mContext, List<String> mData) {
//        this.mContext = mContext;
//        this.mData = mData;
//    }

    public RefreshAdapter(Context context, List<String> data) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == TYPE_ITEM) {
            View itemView = mInflater.inflate(R.layout.item_refresh_recylerview,viewGroup,false);
            return new ItemViewHolder(itemView);
        } else if (i == TYPE_FOOTER) {
            View itemView = mInflater.inflate(R.layout.load_more_footview_layout,viewGroup,false);
            return new FooterViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
            String str = mData.get(i);
            itemViewHolder.mTvContent.setText(str);
        } else if (viewHolder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) viewHolder;

            switch (LOAD_MORE_STATUS) {
                case PUBLIC_LOAD_MORE:
                    footerViewHolder.mTvLoadText.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footerViewHolder.mTvLoadText.setText("正在加载");
                    break;
                    case NO_LOAD_MORE:
                        //隐藏加载更多
                        footerViewHolder.mTvLoadText.setVisibility(View.GONE);
                        break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            //最后一个item设置为footerView
            return  TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public class  ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvContent;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvContent = (TextView) itemView.findViewById(R.id.tvContent);
            initListener(itemView);
        }

        private void initListener(View itemView) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"position" + getAdapterPosition(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;
        private TextView mTvLoadText;
        private LinearLayout mLinearLayout;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pbLoad);
            mTvLoadText = (TextView) itemView.findViewById(R.id.tvLoadText);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.loadLayout);
        }
    }

    public void addHeaderItem(List<String> items) {
        mData.addAll(0,items);
        notifyDataSetChanged();
    }

    public void  addFooterItem(List<String> items) {
        mData.addAll(items);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        //RecyclerView 的count设置为数据总条数+1（footerView）
        return mData.size() + 1;
    }
    /**
     * 更新加载更多状态
     * @param status
     */
    public void changeMoreStatus(int status){
        LOAD_MORE_STATUS = status;
        notifyDataSetChanged();
    }
}
