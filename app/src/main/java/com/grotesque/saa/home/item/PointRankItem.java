package com.grotesque.saa.home.item;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.grotesque.saa.R;
import com.grotesque.saa.home.adapter.PointRankAdapter;
import com.grotesque.saa.home.data.PointRankData;
import com.grotesque.saa.util.FontManager;

/**
 * Created by KH on 2016-09-02.
 */
public class PointRankItem extends AbstractAdapterItem implements PointRankData.OnLoadedListener {

    private Context mContext;
    private PointRankAdapter mAdapter;

    public PointRankItem(Context mContext) {
        this.mContext = mContext;
        this.mAdapter = new PointRankAdapter(mContext);
        onLoadData();
    }

    @Override
    public View getView(LayoutInflater layoutInflater, View view) {
        ViewHolder vh;
        if(view == null){
            view = layoutInflater.inflate(R.layout.listrow_explore_item_point_rank, null);
            vh = new ViewHolder(mContext, view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder)view.getTag();
        }
        vh.mTitleView.setText(Html.fromHtml("<font color=#00c3bd>포인트</font> <font color=#333333>랭킹</font>"));
        vh.mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    protected void onLoadData() {
        PointRankData pointRankData = new PointRankData();
        pointRankData.setOnLoadedListener(this);
        mAdapter.setArrayList(pointRankData.getPointRankList());
        pointRankData.loadData(mContext);
    }

    @Override
    public void onLoaded() {
        mAdapter.notifyDataSetChanged();
    }

    class ViewHolder {
        final PointRankItem mItem;
        RecyclerView mRecyclerView;
        TextView mTitleView;
        public ViewHolder(Context context, View view){
            super();
            mItem = PointRankItem.this;
            mRecyclerView = (RecyclerView) view.findViewById(R.id.articleList);
            mTitleView = (TextView) view.findViewById(R.id.titleView);
            FontManager.getInstance(context).setTypeface(mTitleView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context, 0, false));
        }
    }
}
