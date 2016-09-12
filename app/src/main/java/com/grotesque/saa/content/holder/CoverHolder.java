package com.grotesque.saa.content.holder;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grotesque.saa.R;
import com.grotesque.saa.common.holder.BaseViewHolder;
import com.grotesque.saa.content.data.ContentItem;
import com.grotesque.saa.util.DensityScaleUtil;
import com.grotesque.saa.util.FontManager;

import java.util.List;

/**
 * Created by 경환 on 2016-05-10.
 */
public class CoverHolder extends BaseViewHolder<ContentItem> {
    private RelativeLayout mTransView;
    private TextView mTitleView;
    public static CoverHolder newInstance(Context context, ViewGroup parent){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_postview_content_info_trans_img, parent, false);
        return new CoverHolder(parent.getContext(), v);
    }

    public CoverHolder(Context context, View itemView) {
        super(context, itemView);
        mTransView = (RelativeLayout) itemView.findViewById(R.id.view_trans_inner);
        mTitleView = (TextView) itemView.findViewById(R.id.txt_coverview_title);
        mTitleView.setTypeface(FontManager.getInstance(context).getTypeface());
    }

    @Override
    public void onBindView(ContentItem contentItem) {

    }

    @Override
    public void onBindView(List<ContentItem> item, int position) {
        mTransView.setLayoutParams(new LinearLayout.LayoutParams(-1, (getContext().getResources().getDisplayMetrics().heightPixels - DensityScaleUtil.dipToPixel(getContext(), 50F)) / 2));
        mTitleView.setText(Html.fromHtml(item.get(position).getText()));

        for(ContentItem c : item){
            if(c.getType().equals("image")){
                mTransView.setLayoutParams(new LinearLayout.LayoutParams(-1, getContext().getResources().getDisplayMetrics().heightPixels ));
                break;
            }
        }
    }

    @Override
    public void onBindView(List<ContentItem> item, int position, String mid) {

    }
}
