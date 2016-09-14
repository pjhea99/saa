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
import com.grotesque.saa.util.UIUtils;

import java.util.List;

/**
 * Created by 경환 on 2016-05-10.
 */
public class NextHolder extends BaseViewHolder<ContentItem> {
    private View mCoverView;
    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mWriterView;
    
    public static NextHolder newInstance(Context context, ViewGroup parent){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_postview_next_post, parent, false);
        return new NextHolder(parent.getContext(), v);
    }
    public NextHolder(Context context, View itemView) {
        super(context, itemView);
        mCoverView = itemView.findViewById(R.id.cover_next);
        mImageView = (ImageView) itemView.findViewById(R.id.img_next);
        mTitleView = (TextView) itemView.findViewById(R.id.txt_postview_next_post_title);
        mWriterView = (TextView) itemView.findViewById(R.id.txt_postview_next_post_subtitle);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.goContentActivity(getContext(), "next");
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
            mCoverView.setBackgroundColor(UIUtils.getCategoryColor(item.get(position).getDocumentData().getCategorySrl()));
            //mCoverView.setBackgroundColor(Color.parseColor("#4cFF0000"));
        }
        mTitleView.setText(Html.fromHtml(item.get(position).getDocumentData().getTitle()));
        mWriterView.setText(item.get(position).getDocumentData().getNickName());
    }

    @Override
    public void onBindView(List<ContentItem> item, int position, String mid) {

    }
}
