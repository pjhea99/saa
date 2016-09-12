package com.grotesque.saa.board;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.grotesque.saa.R;
import com.grotesque.saa.common.fragment.BaseFragment;
import com.grotesque.saa.home.data.DocumentList;
import com.grotesque.saa.util.NavigationUtils;
import com.grotesque.saa.util.ParseUtils;

/**
 * Created by 경환 on 2016-05-03.
 */
public class SpecialFragment extends BaseFragment {
    private String mModuleId;
    private DocumentList mDocuData;
    private SimpleDraweeView mImageView;
    private TextView mTitleView;
    private TextView mUserView;
    private TextView mCategoryView;
    private TextView mSummaryView;
    public static SpecialFragment newInstance(String moduleId, DocumentList data){
        SpecialFragment fragment = new SpecialFragment();
        Bundle args = new Bundle();
        args.putString("mid", moduleId);
        args.putParcelable("data", data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentViewResource() {
        return R.layout.fragment_special_text;
    }

    @Override
    protected void initOnCreate(Bundle paramBundle) {
        if(getArguments() != null){
            mModuleId = getArguments().getString("mid");
            mDocuData = getArguments().getParcelable("data");
        }
    }

    @Override
    protected void onInitCreated(Bundle paramBundle) {
        ImageRequest imageRequest;

        if(mDocuData.hasImg()){
            imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(ParseUtils.parseImgUrl(mDocuData.getContent())))
                    .build();
        }else{
            imageRequest = ImageRequestBuilder
                    .newBuilderWithResourceId(R.drawable.bg)
                    .build();
        }
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setOldController(mImageView.getController())
                .build();
        mImageView.setController(draweeController);

        mTitleView.setText(Html.fromHtml(mDocuData.getTitle()));
        mUserView.setText(mDocuData.getUserName());
        mSummaryView.setText(Html.fromHtml(mDocuData.getContent().replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "")));

    }

    @Override
    protected void registerView(View view) {
        mImageView = (SimpleDraweeView) view.findViewById(R.id.imageView);
        mTitleView = (TextView) view.findViewById(R.id.titleView);
        mUserView = (TextView) view.findViewById(R.id.userView);
        mCategoryView = (TextView) view.findViewById(R.id.magazineView);
        mSummaryView = (TextView) view.findViewById(R.id.summaryView);
        mSummaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.goContentActivity(getActivity(), mModuleId, mDocuData);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
