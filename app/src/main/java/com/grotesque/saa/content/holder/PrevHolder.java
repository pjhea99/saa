package com.grotesque.saa.content.holder;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.grotesque.saa.R;
import com.grotesque.saa.common.holder.BaseViewHolder;
import com.grotesque.saa.content.data.ContentItem;
import com.grotesque.saa.util.NavigationUtils;
import com.grotesque.saa.util.ParseUtils;

import java.util.List;

/**
 * Created by 경환 on 2016-05-10.
 */
public class PrevHolder extends BaseViewHolder<ContentItem> {
    private View mCoverView;
    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mWriterView;

    public static PrevHolder newInstance(Context context, ViewGroup parent){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_postview_prev_post, parent, false);
        return new PrevHolder(parent.getContext(), v);
    }
    public PrevHolder(Context context, View itemView) {
        super(context, itemView);
        mCoverView = itemView.findViewById(R.id.cover_prev);
        mImageView = (ImageView) itemView.findViewById(R.id.img_prev);
        mTitleView = (TextView) itemView.findViewById(R.id.txt_postview_prev_post_title);
        mWriterView = (TextView) itemView.findViewById(R.id.txt_postview_prev_post_subtitle);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.goContentActivity(getContext(), "prev");
            }
        });
    }

    @Override
    public void onBindView(ContentItem contentItem) {

    }

    @Override
    public void onBindView(List<ContentItem> item, int position) {
        if(item.get(position).getDocumentData().hasImg()){
            Glide
                    .with(mImageView.getContext())
                    .load(ParseUtils.parseImgUrl(item.get(position).getDocumentData().getContent()))
                    .asBitmap()
                    .into(mImageView);
            mCoverView.setBackgroundColor(Color.parseColor("#4c000000"));
        }else{
            Glide.clear(mImageView);
            mCoverView.setBackgroundColor(Color.parseColor("#4c000000"));
            //mCoverView.setBackgroundColor(Color.parseColor("#4c1e90ff"));
        }
        mTitleView.setText(Html.fromHtml(item.get(position).getDocumentData().getTitle()));
        mWriterView.setText(item.get(position).getDocumentData().getNickName());
    }

    @Override
    public void onBindView(List<ContentItem> item, int position, String mid) {

    }
}
