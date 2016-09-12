package com.grotesque.saa.home;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.grotesque.saa.R;
import com.grotesque.saa.common.Arrays;
import com.grotesque.saa.common.Drawables;
import com.grotesque.saa.common.fragment.BaseActionBarFragment;
import com.grotesque.saa.common.widget.CustomProgressbarDrawable;
import com.grotesque.saa.home.adapter.ExploreAdapter;
import com.grotesque.saa.home.adapter.TopTitleListAdapter;
import com.grotesque.saa.home.item.AbstractAdapterItem;
import com.grotesque.saa.home.item.PointRankItem;
import com.grotesque.saa.home.item.RecentItem;
import com.grotesque.saa.util.DensityScaleUtil;

import java.util.ArrayList;

/**
 * Created by 경환 on 2016-04-02.
 */
public class HomeFragment extends BaseActionBarFragment {
    private static final String IMAGE_URL = "https://4789c912aa23fe709d90e1bcd82dd0d2cc174bac.googledrive.com/host/0B9avBiKRgi6GdTFCczlVRE1YWEk";

    ObjectAnimator mObjectAnimator;

    ArrayList<AbstractAdapterItem> mRecentItem;
    ExploreAdapter mRecentBoardAdapter;
    TopTitleListAdapter topTitleListAdapter;

    RecyclerView mRecyclerView;
    ListView mListView;

    SimpleDraweeView mTopImage;

    public static HomeFragment newInstance(){
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
    }

    @Override
    protected int getContentViewResource() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initOnCreate(Bundle paramBundle) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onInitCreated(Bundle paramBundle) {

        Uri uri = Uri.parse(IMAGE_URL);

        ImageRequestBuilder imageRequestBuilder =
                ImageRequestBuilder.newBuilderWithSource(uri);
        if (UriUtil.isNetworkUri(uri)) {
            imageRequestBuilder.setProgressiveRenderingEnabled(true);
        }

        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequestBuilder.build())
                .setOldController(mTopImage.getController())
                .setAutoPlayAnimations(true)
                .setTapToRetryEnabled(true)
                .build();
        mTopImage.setController(draweeController);

        ArrayList<String> mArrayList = new ArrayList<>();

        mArrayList.addAll(java.util.Arrays.asList(Arrays.sTitle).subList(0, 9));

        mObjectAnimator = ObjectAnimator.ofFloat(this.mRecyclerView, View.ALPHA, 0.3F, 0.0F);
        mObjectAnimator.setInterpolator(new LinearInterpolator());
        mObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRecyclerView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mRecyclerView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int i;
                int j;
                if (absListView != null && absListView.getChildCount() > 0) {
                    View view = absListView.getChildAt(0);
                    j = DensityScaleUtil.dipToPixel(getActivity(), 35F);
                    i = DensityScaleUtil.dipToPixel(getActivity(), 90F);
                    j = i - j;
                    float f = view.getY();
                    if (f < (float) j) {
                        if (!mObjectAnimator.isRunning() && mRecyclerView.getVisibility() == View.VISIBLE) {
                            mObjectAnimator.setDuration(300L);
                            mObjectAnimator.start();
                        }
                    } else {
                        if (f > (float) j && f < (float) i) {
                            f = ((float) i - f) / (float) (i - j);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mRecyclerView.setAlpha(1.0F - f * 0.7F);
                            return;
                        }
                        if (f == (float) i) {
                            mRecyclerView.setAlpha(1.0F);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            return;
                        }
                    }
                }
            }
        });

        mRecentItem = new ArrayList<>();
        mRecentItem.add(new RecentItem(mContext, "calcioboard"));
        mRecentItem.add(new RecentItem(mContext, "freeboard3"));
        mRecentItem.add(new RecentItem(mContext, "qna1"));
        mRecentItem.add(new RecentItem(mContext, "rest"));
        mRecentItem.add(new RecentItem(mContext, "game"));
        mRecentItem.add(new PointRankItem(mContext));

        //mRecentItem.add(new RecentItem(mContext, "multimedia1"));


        mRecentBoardAdapter = new ExploreAdapter(mContext);
        mRecentBoardAdapter.addAllDataSetList(mRecentItem);

        mRecentBoardAdapter.notifyDataSetChanged();

        mListView.setAdapter(mRecentBoardAdapter);
        topTitleListAdapter.setArrayList(mArrayList);
    }

    @Override
    protected void registerView(View view) {
        super.registerView(view);
        mListView = (ListView) view.findViewById(R.id.explore_list_view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.today_keyword_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        topTitleListAdapter = new TopTitleListAdapter(this.mContext);
        mRecyclerView.setAdapter(topTitleListAdapter);
        setActionBarColor(Color.rgb(255, 255, 255));


        mTopImage = (SimpleDraweeView) view.findViewById(R.id.top_bg_image);
        GenericDraweeHierarchy gdh = new GenericDraweeHierarchyBuilder(getActivity().getResources())
                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                .build();
        mTopImage.setHierarchy(gdh);
    }
}
