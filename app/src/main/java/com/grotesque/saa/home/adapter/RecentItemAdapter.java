package com.grotesque.saa.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.grotesque.saa.R;
import com.grotesque.saa.common.Drawables;
import com.grotesque.saa.common.widget.CustomProgressbarDrawable;
import com.grotesque.saa.home.data.DocumentList;
import com.grotesque.saa.util.FontManager;
import com.grotesque.saa.util.NavigationUtils;
import com.grotesque.saa.util.ParseUtils;

import java.util.ArrayList;

import static com.grotesque.saa.util.LogUtils.makeLogTag;

/**
 * Created by 경환 on 2016-04-02.
 */
public class RecentItemAdapter extends RecyclerView.Adapter<RecentItemAdapter.ViewItemHolder>{
    private static final String TAG = makeLogTag(RecentItemAdapter.class);
    Context mContext;
    ArrayList<DocumentList> mArrayList = new ArrayList<>();
    String mid;
    public class ViewItemHolder extends RecyclerView.ViewHolder{
        View mColorCover;
        FrameLayout mImageCoverLayout;
        SimpleDraweeView mImageView;
        TextView mCategoryView;
        TextView mTitleView;
        TextView mSubTitleView;
        TextView mByView;
        TextView mWriterView;
        public ViewItemHolder(View itemView) {
            super(itemView);
            mColorCover = itemView.findViewById(R.id.color_cover);
            mImageCoverLayout = (FrameLayout) itemView.findViewById(R.id.imageCoverLayout);
            mImageView = (SimpleDraweeView) itemView.findViewById(R.id.articleBG);
            mCategoryView = (TextView) itemView.findViewById(R.id.categoryView);
            mTitleView = (TextView) itemView.findViewById(R.id.titleView);
            mSubTitleView = (TextView) itemView.findViewById(R.id.subtitleView);
            mByView = (TextView) itemView.findViewById(R.id.byView);
            mWriterView = (TextView) itemView.findViewById(R.id.writerView);

            GenericDraweeHierarchy gdh = new GenericDraweeHierarchyBuilder(mContext.getResources())
                    .setFailureImage(Drawables.sErrorDrawable)
                    .setProgressBarImage(new CustomProgressbarDrawable())
                    .setPlaceholderImage(Drawables.sPlaceholderDrawable, ScalingUtils.ScaleType.CENTER_CROP)
                    .setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP)
                    .build();
            mImageView.setHierarchy(gdh);
            mByView.setTypeface(FontManager.getInstance(mContext).getTypefaceItalic());
            mCategoryView.setTypeface(FontManager.getInstance(mContext).getTypefaceItalic());
        }
    }

    public RecentItemAdapter(String mid, Context context) {
        this.mid = mid;
        this.mContext = context;
    }
    public void setArrayList(ArrayList<DocumentList> arraylist) {
        mArrayList = arraylist;
        notifyDataSetChanged();
    }
    @Override
    public ViewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_keyword_article, parent, false);
        return new ViewItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewItemHolder vh, final int position) {
        switch (mArrayList.get(position).getCategorySrl()){
            case "0":
                vh.mCategoryView.setText("");
                vh.mCategoryView.setVisibility(View.GONE);
                break;
            case "10282945":
                vh.mCategoryView.setText("국대");
                vh.mCategoryView.setTextColor(ContextCompat.getColorStateList(mContext, R.color.free_speech_red));
            case "112817":
                vh.mCategoryView.setText("k리그");
                vh.mCategoryView.setTextColor(ContextCompat.getColorStateList(mContext, R.color.colorAccent));
                break;
            case "112800":
                vh.mCategoryView.setText("ita");
                vh.mCategoryView.setTextColor(ContextCompat.getColorStateList(mContext, R.color.colorPrimary));
                break;
            case "112806":
                vh.mCategoryView.setText("etc");
                vh.mCategoryView.setTextColor(ContextCompat.getColorStateList(mContext, R.color.brunch_mint));
                break;

        }
        if(mArrayList.get(position).hasImg()){
            vh.mImageCoverLayout.setVisibility(View.VISIBLE);
            vh.mColorCover.setVisibility(View.GONE);
            vh.mSubTitleView.setVisibility(View.GONE);

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(ParseUtils.parseImgUrl(mArrayList.get(position).getContent())))
                    .build();
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(vh.mImageView.getController())
                    .build();
            vh.mImageView.setController(draweeController);


            vh.mTitleView.setTextColor(Color.parseColor("#FFFFFFFF"));
            vh.mWriterView.setTextColor(Color.parseColor("#99FFFFFF"));
            vh.mByView.setTextColor(Color.parseColor("#99FFFFFF"));
            vh.mCategoryView.setTextColor(ContextCompat.getColorStateList(mContext, R.color.white));
        }else {
            vh.mImageCoverLayout.setVisibility(View.GONE);
            vh.mColorCover.setVisibility(View.VISIBLE);
            vh.mSubTitleView.setVisibility(View.VISIBLE);

            vh.mTitleView.setTextColor(Color.parseColor("#FF333333"));
            vh.mWriterView.setTextColor(Color.parseColor("#99333333"));
            vh.mByView.setTextColor(Color.parseColor("#99333333"));
            vh.mColorCover.setBackgroundResource(R.drawable.library_bg_card_default);

            if(mArrayList.get(position).getTitle().contains("스포")){
                vh.mSubTitleView.setVisibility(View.GONE);
            }else{
                vh.mSubTitleView.setText(Html.fromHtml(mArrayList.get(position).getContent().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "")));
            }
        }

        vh.mTitleView.setText(Html.fromHtml(mArrayList.get(position).getTitle()));
        vh.mWriterView.setText(mArrayList.get(position).getNickName());

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.goContentActivity(mContext, mid, mArrayList, position);
            }
        });

    }

    @Override
    public void onViewRecycled(ViewItemHolder holder) {
        if (holder.mImageView.getController() != null) {
            holder.mImageView.getController().onDetach();
        }
        if (holder.mImageView.getTopLevelDrawable() != null) {
            holder.mImageView.getTopLevelDrawable().setCallback(null);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
}
