package com.grotesque.saa.board.holder;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.grotesque.saa.R;
import com.grotesque.saa.common.Drawables;
import com.grotesque.saa.common.api.RetrofitApi;
import com.grotesque.saa.common.data.ResponseData;
import com.grotesque.saa.common.holder.BaseViewHolder;
import com.grotesque.saa.common.widget.CustomProgressbarDrawable;
import com.grotesque.saa.home.data.DocumentList;
import com.grotesque.saa.util.FontManager;
import com.grotesque.saa.util.NavigationUtils;
import com.grotesque.saa.util.ParseUtils;
import com.grotesque.saa.util.StringUtils;
import com.grotesque.saa.util.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.grotesque.saa.util.LogUtils.LOGE;

/**
 * Created by KH on 2016-07-26.
 */
public class BoardCardHolder extends BaseViewHolder<DocumentList> {

    LayoutInflater mLayoutInflater;

    TextView mByView;
    TextView mCountView;
    TextView mTimeView;
    TextView mTitleView;
    TextView mImageTitleView;
    TextView mSubTitleView;
    TextView mUserView;
    TextView mCommentCountView;
    TextView mVoteCountView;
    SimpleDraweeView mImageView;
    View mCommentView;
    View mLikeView;
    View mShareView;
    LinearLayout mTagBody;
    RelativeLayout mImageBody;

    public BoardCardHolder(Context context, View itemView) {
        super(context, itemView);

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mByView = (TextView) itemView.findViewById(R.id.byView);
        mByView.setTypeface(FontManager.getInstance(context).getTypefaceItalic());

        mTitleView = (TextView) itemView.findViewById(R.id.titleView);
        mTitleView.setTypeface(FontManager.getInstance(context).getTypeface());

        mSubTitleView = (TextView) itemView.findViewById(R.id.subtitleView);
        mSubTitleView.setTypeface(FontManager.getInstance(context).getTypeface());

        mImageTitleView = (TextView) itemView.findViewById(R.id.imageTitleView);
        mImageTitleView.setTypeface(FontManager.getInstance(context).getTypeface());

        mUserView = (TextView) itemView.findViewById(R.id.userView);
        mUserView.setTypeface(FontManager.getInstance(context).getTypeface());

        mTimeView = (TextView) itemView.findViewById(R.id.timeView);
        mTimeView.setTypeface(FontManager.getInstance(context).getTypeface());

        mCountView = (TextView)itemView.findViewById(R.id.countView);
        mCountView.setTypeface(FontManager.getInstance(context).getTypefaceItalic());

        mCommentCountView = (TextView) itemView.findViewById(R.id.commentCountView);
        mCommentCountView.setTypeface(FontManager.getInstance(context).getTypeface());

        mVoteCountView = (TextView) itemView.findViewById(R.id.voteCountView);
        mVoteCountView.setTypeface(FontManager.getInstance(context).getTypeface());

        mTagBody = (LinearLayout) itemView.findViewById(R.id.tagBody);

        mCommentView = itemView.findViewById(R.id.commentView);
        mLikeView = itemView.findViewById(R.id.likeView);
        mShareView = itemView.findViewById(R.id.shareView);

        mImageBody = (RelativeLayout) itemView.findViewById(R.id.rlImageBody);

        mImageView = (SimpleDraweeView) itemView.findViewById(R.id.imageView);
        GenericDraweeHierarchy gdh = new GenericDraweeHierarchyBuilder(getContext().getResources())
                .setFailureImage(Drawables.sErrorDrawable)
                .setProgressBarImage(new CustomProgressbarDrawable())
                .setPlaceholderImage(Drawables.sPlaceholderDrawable, ScalingUtils.ScaleType.CENTER_CROP)
                .setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP)
                .build();
        mImageView.setHierarchy(gdh);
    }

    public static BoardCardHolder newInstance(Context context, ViewGroup parent){
        return new BoardCardHolder(context, LayoutInflater.from(context).inflate(R.layout.listrow_board_card, parent, false));
    }

    @Override
    public void onBindView(DocumentList documentList) {

    }

    @Override
    public void onBindView(List<DocumentList> item, int position) {

    }

    @Override
    public void onBindView(final List<DocumentList> item, final int position, final String mid) {
        String title = item.get(position).getTitle();
        mTitleView.setText(Html.fromHtml(title));
        mSubTitleView.setText(title.contains("스포") ? "" : Html.fromHtml(item.get(position).getContent().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "")));
        mTimeView.setText(StringUtils.convertDate(item.get(position).getRegdate()));
        mCountView.setText(String.format(Locale.US, "조회 %s", item.get(position).getReadedCount()));
        mUserView.setText(item.get(position).getNickName());
        mCommentCountView.setText(String.format(Locale.US, "%s", item.get(position).getCommentCount()));
        mVoteCountView.setText(item.get(position).getVotedCount());

        mImageBody.setVisibility(View.VISIBLE);
        mTitleView.setVisibility(View.GONE);
        mSubTitleView.setVisibility(View.GONE);
        mTagBody.setVisibility(View.GONE);

        if(item.get(position).hasImg()){
            mImageTitleView.setText(Html.fromHtml(title));
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(ParseUtils.parseCardImgUrl(item.get(position).getContent())))
                    .build();
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(mImageView.getController())
                    .build();
            mImageView.setController(draweeController);
        }else{
            if(item.get(position).hasYoutube()){
                mImageTitleView.setText(Html.fromHtml(title));

                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("http://img.youtube.com/vi/"+StringUtils.getYoutubeId(item.get(position).getContent()) + "/0.jpg"))
                        .setResizeOptions(new ResizeOptions(150, 180))
                        .build();
                DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(mImageView.getController())
                        .build();
                mImageView.setController(draweeController);

            }else {
                mTitleView.setVisibility(View.VISIBLE);
                mSubTitleView.setVisibility(View.VISIBLE);
                mImageBody.setVisibility(View.GONE);
            }
        }

        if(!item.get(position).getTags().equals("")){
            mTagBody.setVisibility(View.VISIBLE);
            mTagBody.removeAllViews();
            String[] tag = item.get(position).getTags().split(",");
            for(String str : tag){
                RelativeLayout childView = (RelativeLayout) mLayoutInflater.inflate(R.layout.layout_tags, null);
                TextView tagView = (TextView) childView.findViewById(R.id.txt_postview_tags);
                tagView.setTypeface(FontManager.getInstance(getContext()).getTypeface());
                tagView.setText(Html.fromHtml(str));
                mTagBody.addView(childView);
            }
        }


        mCommentCountView.setTextColor(UIUtils.getCategoryColor(item.get(position).getCategorySrl()));
        mVoteCountView.setTextColor(UIUtils.getCategoryColor(item.get(position).getCategorySrl()));

        mCommentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationUtils.goCommentListAcitivity(getContext(), mid, item.get(position));
            }
        });

        mLikeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitApi.getInstance().procBoardVoteDocument(item.get(position).getDocumentSrl()).enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if(response.body().getMessage().equals("추천했습니다.")){
                            Toast.makeText(getContext(), "추천 완료", Toast.LENGTH_LONG).show();
                        }else if(response.body().getMessage().equals("추천할 수 없습니다.")){
                            Toast.makeText(getContext(), "이미 추천하신 게시물입니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                    }
                });
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.goContentActivity(getContext(), mid, (ArrayList<DocumentList>) item, position-1);
            }
        });
    }
}
