package com.grotesque.saa.content.holder;

import android.content.Context;
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
import com.grotesque.saa.util.FontManager;

import java.util.List;

import static com.grotesque.saa.util.LogUtils.LOGE;

/**
 * Created by 경환 on 2016-05-10.
 */
public class TagHolder extends BaseViewHolder<ContentItem> {
    private static final String TAG = TagHolder.class.getSimpleName();
    private Context mContext;
    private LinearLayout mWrapperView;
    private LayoutInflater mLayoutInflater;

    public static TagHolder newInstance(Context context, ViewGroup parent){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_postview_tags, parent, false);
        return new TagHolder(parent.getContext(), v);
    }

    public TagHolder(Context context, View itemView) {
        super(context, itemView);
        this.mContext = context;
        this.mWrapperView = (LinearLayout) itemView.findViewById(R.id.ll_postview_tags);
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onBindView(ContentItem contentItem) {

    }

    @Override
    public void onBindView(List<ContentItem> item, int position) {
        if(item.get(position).getText().equals(""))
            mWrapperView.setVisibility(View.GONE);
        else {
            mWrapperView.setVisibility(View.VISIBLE);
            mWrapperView.removeAllViews();
            String[] tag = item.get(position).getText().split(",");
            for(String str : tag){
                RelativeLayout childView = (RelativeLayout) mLayoutInflater.inflate(R.layout.layout_tags, null);
                TextView tagView = (TextView) childView.findViewById(R.id.txt_postview_tags);
                tagView.setTypeface(FontManager.getInstance(mContext).getTypeface());
                tagView.setText(Html.fromHtml(str));
                mWrapperView.addView(childView);
            }
        }
    }

    @Override
    public void onBindView(List<ContentItem> item, int position, String mid) {

    }
}
