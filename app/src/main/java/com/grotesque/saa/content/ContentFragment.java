package com.grotesque.saa.content;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.grotesque.saa.R;
import com.grotesque.saa.common.adapter.MultiItemAdapter;
import com.grotesque.saa.common.api.RetrofitApi;
import com.grotesque.saa.common.data.ResponseData;
import com.grotesque.saa.common.fragment.BaseActionBarFragment;
import com.grotesque.saa.common.widget.CommentBar;
import com.grotesque.saa.common.widget.CustomAlertDialog;
import com.grotesque.saa.content.adapter.CommentAdapter;
import com.grotesque.saa.content.adapter.NewContentAdapter;
import com.grotesque.saa.content.data.CommentContainer;
import com.grotesque.saa.content.data.CommentList;
import com.grotesque.saa.content.data.ContentItem;
import com.grotesque.saa.home.data.DocumentList;
import com.grotesque.saa.util.AccountUtils;
import com.grotesque.saa.util.DensityScaleUtil;
import com.grotesque.saa.util.ParseUtils;
import com.grotesque.saa.util.StringUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.grotesque.saa.util.LogUtils.LOGE;
import static com.grotesque.saa.util.LogUtils.makeLogTag;

/**
 * Created by 경환 on 2016-04-04.
 */
public class ContentFragment extends BaseActionBarFragment implements CommentBar.OnCommentBarListener, CommentAdapter.Listener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = makeLogTag(ContentFragment.class);

    private RecyclerView mRecyclerView;
    private RecyclerView mCommentRecyclerView;
    private ImageView mVoteAnimationView;
    private SwipeRefreshLayout mSwipeLayout;

    private boolean mTitleVisible = true;
    private boolean mInfoVisible = true;

    private int mPosition;
    private int mGetTopBoardName = 0;
    private int mGetBottomTitle = 0;
    private int mGetTopInfo = 0;

    private int mWidthPixels;
    private int mHeightPixels;

    private String mMid;
    private String REFERER;
    private int mTotalCommentPage;
    private int mCurrentCommentPage = 1;

    private HashMap<String, String> mCommentQuery = new HashMap<>();

    private SlidingUpPanelLayout mSlidingLayout;
    private CommentBar mCommentBar;
    private View mCoverLayout;
    private TextView mCommentCountView;
    private ObjectAnimator mObjectAnimator;
    private ObjectAnimator mObjectAnimator1;
    private ObjectAnimator mObjectAnimator2;
    private ObjectAnimator mObjectAnimator3;
    private LinearLayoutManager mLayoutManager;

    private CommentList mCurrentComment;
    private DocumentList mCurrentDocument;
    private ArrayList<CommentList> mCommentData = new ArrayList<>();
    private ArrayList<DocumentList> mDocuData;
    private ArrayList<ContentItem> mArrayList = new ArrayList<>();
    private CommentAdapter mCommentAdapter;
    private NewContentAdapter mContentAdapter;

    public static ContentFragment newInstance(String mid, DocumentList array, ArrayList<DocumentList> arrayList, int position){
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putString("mid", mid);
        args.putParcelable("array", array);
        args.putParcelableArrayList("arrayList", arrayList);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentViewResource() {
        return R.layout.fragment_content;
    }

    @Override
    protected void initOnCreate(Bundle paramBundle) {
        Bundle args = getArguments();
        if(args != null){
            mMid = args.getString("mid");
            mCurrentDocument = args.getParcelable("array");
            mDocuData = args.getParcelableArrayList("arrayList");
            mPosition = args.getInt("position");
        }

        mWidthPixels = getResources().getDisplayMetrics().widthPixels;

        DocumentList prevDocument = null;
        DocumentList nextDocument = null;
        if(mDocuData != null) {
            mCurrentDocument = mDocuData.get(mPosition);
            prevDocument = mPosition + 1 < mDocuData.size() ? mDocuData.get(mPosition + 1) : null;
            nextDocument = mPosition - 1 >= 0 ? mDocuData.get(mPosition - 1) : null;
        }

        REFERER = RetrofitApi.API_BASE_URL + mMid +"/"+mCurrentDocument.getDocumentSrl();

        mArrayList.add(new ContentItem(mCurrentDocument.getTitle(), null, null, "cover", false));
        mArrayList.add(new ContentItem("divider", null));
        mArrayList.addAll(ParseUtils.parseContent(mCurrentDocument.getContent()));
        mArrayList.add(new ContentItem(mCurrentDocument.getTags(), null, null, "tags", false));
        if(prevDocument != null)
            mArrayList.add(new ContentItem("prev", prevDocument));
        if(nextDocument != null)
            mArrayList.add(new ContentItem("next", nextDocument));

    }

    @Override
    protected void onInitCreated(Bundle paramBundle) {
        setAdapterItems(mArrayList);
        mCommentQuery.put("act", "dispBoardContentCommentList");
        mCommentQuery.put("document_srl", mCurrentDocument.getDocumentSrl());
        mCommentQuery.put("cpage", "1#comment");
        mTotalCommentPage =  Integer.parseInt(mCurrentDocument.getCommentCount()) / 50  + 1;
        onLoadCommentData();
    }

    @Override
    protected void registerView(View view) {
        super.registerView(view);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.content_list);
        mCommentRecyclerView = (RecyclerView) view.findViewById(R.id.commentList);
        mVoteAnimationView = (ImageView) view.findViewById(R.id.vote_animation);
        mCoverLayout = view.findViewById(R.id.ll_postview_cover_text);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.commentSwipeLayout);

        TextView categoryName = (TextView) view.findViewById(R.id.txt_cover_board_name);
        TextView title = (TextView) view.findViewById(R.id.txt_coverview_title);
        TextView textWriter = (TextView) view.findViewById(R.id.txt_postview_writer_name);
        TextView textTime = (TextView) view.findViewById(R.id.txt_postview_write_time);

        if(!mCurrentDocument.getCategorySrl().equals("0")){
            String category = StringUtils.getCategoryName(mCurrentDocument.getCategorySrl());
            categoryName.setVisibility(View.VISIBLE);
            categoryName.setText(category);
        }else{
            categoryName.setVisibility(View.INVISIBLE);
        }
        setActionBarTitle(StringUtils.getBoardName(mMid));
        title.setText(Html.fromHtml(mCurrentDocument.getTitle()));
        textWriter.setText(mCurrentDocument.getNickName());
        textTime.setText(StringUtils.getCommentTime(mCurrentDocument.getRegdate()));

        mObjectAnimator = ObjectAnimator.ofFloat(view.findViewById(R.id.ll_title), View.ALPHA, 1.0F, 0.0F);
        mObjectAnimator1 = ObjectAnimator.ofFloat(view.findViewById(R.id.ll_title), View.ALPHA, 0.0F, 1.0F);
        mObjectAnimator2 = ObjectAnimator.ofFloat(view.findViewById(R.id.ll_cover_info), View.ALPHA, 1.0F, 0.0F);
        mObjectAnimator3 = ObjectAnimator.ofFloat(view.findViewById(R.id.ll_cover_info), View.ALPHA, 0.0F, 1.0F);


        mContentAdapter = new NewContentAdapter();
        //ContentAdapter mAdapter = new ContentAdapter(getActivity(), mArrayList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mContentAdapter);
        mRecyclerView.addOnScrollListener(onScrollListener);

        mSlidingLayout = (SlidingUpPanelLayout) view.findViewById(R.id.slidingLayout);
        mSlidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }

        });
        mSlidingLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);     }
        });

        mCommentCountView = (TextView) view.findViewById(R.id.commentCount);
        mCommentBar = (CommentBar) view.findViewById(R.id.commentBar);
        mCommentBar.setOnCommentBarListener(this);



        mCommentCountView.setText(mCurrentDocument.getCommentCount());

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setProgressBackgroundColorSchemeResource(R.color.brunch_mint);
        mSwipeLayout.setColorSchemeResources(android.R.color.white);

        mCommentAdapter = new CommentAdapter(getActivity(), mCommentData, this);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommentRecyclerView.setAdapter(mCommentAdapter);

    }

    @Override
    public void onRightButtonClicked() {
        super.onRightButtonClicked();
        RetrofitApi.getInstance().procBoardVoteDocument(mCurrentDocument.getDocumentSrl()).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                LOGE(TAG, response.body().getMessage());
                if(response.body().getMessage().equals("추천했습니다.")){
                    onVoted();
                }else if(response.body().getMessage().equals("추천할 수 없습니다.")){
                    Toast.makeText(mContext, "이미 추천하신 게시물입니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                LOGE(TAG, String.valueOf(t));
            }
        });
    }
    private void onVoted(){
        mVoteAnimationView.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable) mVoteAnimationView.getBackground();
        animationDrawable.start();
        checkIfAnimationDone(animationDrawable);
    }
    private void checkIfAnimationDone(AnimationDrawable anim){
        final AnimationDrawable a = anim;
        int timeBetweenChecks = 300;
        Handler h = new Handler();
        h.postDelayed(new Runnable(){
            public void run(){
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)){
                    checkIfAnimationDone(a);
                } else{
                    mVoteAnimationView.setVisibility(View.GONE);
                }
            }
        }, timeBetweenChecks);
    };
    private void onLoadCommentData(){
        Call<CommentContainer> call = RetrofitApi.getInstance().getCommentList(mCommentQuery);
        call.enqueue(new Callback<CommentContainer>() {
            @Override
            public void onResponse(Call<CommentContainer> call, Response<CommentContainer> response) {
                if(response.code() == 200) {
                    mCommentData.addAll(response.body().getCommentList());
                    LOGE(TAG, "코멘트 갯수 : " + mCommentData.size());
                    mCommentCountView.setText(String.valueOf(mCommentData.size()));
                    mCommentAdapter.notifyDataSetChanged();

                    if(mTotalCommentPage > 1 && mTotalCommentPage > mCurrentCommentPage){
                        mCommentQuery.put("cpage", (++mCurrentCommentPage) + "#comment");
                        onLoadCommentData();
                    }
                    mSwipeLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<CommentContainer> call, Throwable t) {
                LOGE(TAG, "onFailure : " + t);
            }

        });
    }
    public int getScrollY(){
        int i1;
        int j1 = 0;
        int firstVisibleItemPosition;
        int l1;

        View view1;
        if(mRecyclerView.getChildCount() > 0)
            if((view1 = mRecyclerView.getChildAt(0)) != null){
                View view;
                if(mGetTopBoardName == 0) {
                    view = view1.findViewById(R.id.txt_cover_board_name);
                    if(view != null)
                    {
                        mGetTopBoardName = view.getTop();
                    }
                }
                if(mGetBottomTitle == 0) {
                    view = view1.findViewById(R.id.txt_coverview_title);
                    if(view != null) {
                        mGetBottomTitle = view.getBottom();
                    }
                }
                if(mGetTopInfo == 0)
                {
                    view = view1.findViewById(R.id.ll_cover_info);
                    if(view != null) {
                        mGetTopInfo = view.getTop();
                    }
                }
                firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                l1 = view1.getTop();
                i1 = j1;
                if(firstVisibleItemPosition >= 1)
                {
                    i1 = j1;
                    if(mCoverLayout != null)
                        i1 = mCoverLayout.getHeight();
                }
                j1 = -l1;
                return i1 + (firstVisibleItemPosition * view1.getHeight() + j1);
            }
        return 0;
    }
    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
            // code
        }

        @Override
        public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
            int visibleItemCount = mLayoutManager.findLastVisibleItemPosition() - firstVisibleItemPosition + 1;
            int totalItemCount = mLayoutManager.getItemCount();
            int scrolledY = getScrollY();
            int toolbarHeight = DensityScaleUtil.dipToPixel(mContext, 50f);


            if(scrolledY > DensityScaleUtil.dipToPixel(mContext, 10f)){
                View view = mLayoutManager.findViewByPosition(0);
                if(view != null)
                    view.findViewById(R.id.view_divider_postcover).setVisibility(View.VISIBLE);
            }else{
                View view = mLayoutManager.findViewByPosition(0);
                if(view != null)
                    view.findViewById(R.id.view_divider_postcover).setVisibility(View.GONE);
            }
            View view = recyclerView.getChildAt(0);
            if (totalItemCount >= 1 && view != null) {
                float firstItemHeight = view.getHeight();
                int l;
                int a = 0;
                if (view.getY() + firstItemHeight > (float) toolbarHeight)
                    l = firstVisibleItemPosition;
                else
                    l = firstVisibleItemPosition + 1;
                if (l == 0) {
                    setActionBarBackgroundResource(0);
                    if (scrolledY > mCoverLayout.getHeight()-mGetBottomTitle) {
                        if (mTitleVisible) {
                            mObjectAnimator.setInterpolator(new LinearInterpolator());
                            mObjectAnimator.setDuration(400L);
                            mObjectAnimator.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    if (mCoverLayout != null && mCoverLayout.findViewById(R.id.ll_title) != null)
                                        mCoverLayout.findViewById(R.id.ll_title).setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                    if (mCoverLayout != null && mCoverLayout.findViewById(R.id.ll_title) != null)
                                        mCoverLayout.findViewById(R.id.ll_title).setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                            mObjectAnimator.start();
                        }
                        mTitleVisible = false;
                    } else {
                        if (!mTitleVisible) {
                            mObjectAnimator1.setInterpolator(new LinearInterpolator());
                            mObjectAnimator1.setDuration(500L);
                            mObjectAnimator1.start();
                        }
                        mTitleVisible = true;
                        if (mObjectAnimator != null && mObjectAnimator.isRunning())
                            mObjectAnimator.cancel();
                        if (mCoverLayout != null && mCoverLayout.findViewById(R.id.ll_title) != null)
                            mCoverLayout.findViewById(R.id.ll_title).setVisibility(View.VISIBLE);
                    }
                    if (mGetTopInfo > 0 && scrolledY > DensityScaleUtil.dipToPixel(getActivity(), 18F)) {
                        if (mInfoVisible) {
                            mObjectAnimator2.setInterpolator(new LinearInterpolator());
                            mObjectAnimator2.setDuration(400L);
                            mObjectAnimator2.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    if (mCoverLayout != null && mCoverLayout.findViewById(R.id.ll_cover_info) != null)
                                        mCoverLayout.findViewById(R.id.ll_cover_info).setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                    if (mCoverLayout != null && mCoverLayout.findViewById(R.id.ll_cover_info) != null)
                                        mCoverLayout.findViewById(R.id.ll_cover_info).setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                            mObjectAnimator2.start();
                        }
                        mInfoVisible = false;
                    } else {
                        if (!mInfoVisible) {
                            mObjectAnimator3.setInterpolator(new LinearInterpolator());
                            mObjectAnimator3.setDuration(500L);
                            mObjectAnimator3.start();
                        }
                        mInfoVisible = true;
                        if (mObjectAnimator2 != null && mObjectAnimator2.isRunning())
                            mObjectAnimator2.cancel();
                        if (mCoverLayout != null && mCoverLayout.findViewById(R.id.ll_cover_info) != null)
                            mCoverLayout.findViewById(R.id.ll_cover_info).setVisibility(View.VISIBLE);
                    }
                } else if (firstVisibleItemPosition + visibleItemCount == totalItemCount) {
                    View view1 = recyclerView.getChildAt(visibleItemCount - 1);
                    if (view1 != null && view1.getBottom() == recyclerView.getBottom()) {
                        setActionBarBackgroundResource(R.drawable.comm_bg_home_top);
                        setActionBarVisibility(true);
                    }
                } else if (a < l) {
                    if(dy > 0) {
                        setActionBarVisibility(false);
                    }else{
                        setActionBarBackgroundResource(R.drawable.comm_bg_home_top);
                        setActionBarVisibility(true);
                    }

                } else if (a > l) {
                    setActionBarBackgroundResource(R.drawable.comm_bg_home_top);
                    setActionBarVisibility(true);
                }
                a = l;
            }
        }
    };

    private final Callback<JsonObject> commentCallback = new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if(response.body().get("message").toString().contains("등록했습니다.")){
                Toast.makeText(mContext
                        , "댓글 등록"
                        , Toast.LENGTH_LONG)
                        .show();
                mCommentBar.clearBar();
                mCommentData.clear();
                onLoadCommentData();
            }else{
                Toast.makeText(mContext
                        , "댓글 등록에 실패 했습니다"
                        , Toast.LENGTH_LONG)
                        .show();
            }
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            LOGE(TAG, String.valueOf(t));
        }
    };
    private final Callback<JsonObject> deleteCallback = new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if(response.body() != null){
                LOGE(TAG, response.body().toString());
                if(response.body().get("message").toString().equals("삭제했습니다.")){
                    Toast.makeText(mContext
                            , "댓글 삭제"
                            , Toast.LENGTH_LONG)
                            .show();
                    mCommentData.clear();
                    onLoadCommentData();

                }
            }
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {

        }
    };

    @Override
    public void onButtonClicked(int type, String content) {
        switch (type){
            case CommentBar.SUBMIT:
                RetrofitApi.getInstance()
                        .submitComment(mMid
                                , ""
                                , mCurrentDocument.getDocumentSrl()
                                , ""
                                , content
                                , RetrofitApi.API_BASE_URL + mMid +"/"+mCurrentDocument.getDocumentSrl())
                        .enqueue(commentCallback);
                break;
            case CommentBar.REPLY:
                RetrofitApi.getInstance()
                        .submitComment(mMid
                                , mCurrentComment.getCommentSrl()
                                , mCurrentDocument.getDocumentSrl()
                                , ""
                                , content
                                , RetrofitApi.API_BASE_URL + mMid +"/"+mCurrentDocument.getDocumentSrl())
                        .enqueue(commentCallback);
                break;
            case CommentBar.EDIT:
                RetrofitApi.getInstance()
                        .submitComment(mMid
                                , ""
                                , mCurrentDocument.getDocumentSrl()
                                , mCurrentComment.getCommentSrl()
                                , content
                                , RetrofitApi.API_BASE_URL + mMid +"/"+mCurrentDocument.getDocumentSrl())
                        .enqueue(commentCallback);
                break;
        }
        return;
    }
    @Override
    public void onLongClicked(final int position) {
        mCurrentComment = mCommentData.get(position);
        ArrayList arraylist = new ArrayList();
        arraylist.add("답변");
        if(AccountUtils.getActiveAccountName(mContext).equals(mCurrentComment.getUserId())) {
            arraylist.add("수정");
            arraylist.add("삭제");
        }
        CharSequence acharsequence[] = (CharSequence[])arraylist.toArray(new CharSequence[arraylist.size()]);
        (new CustomAlertDialog(mContext)).setItems(acharsequence, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        mCommentBar.setHint("@"+mCurrentComment.getUserName());
                        mCommentBar.setRightButtonText(CommentBar.REPLY);
                        break;
                    case 1:
                        mCommentBar.setRightButtonText(CommentBar.EDIT);
                        mCommentBar.setText(mCommentData.get(position).getContent());
                        break;
                    case 2:
                        RetrofitApi.getInstance().procBoardDeleteComment(
                                mMid,
                                mCurrentDocument.getDocumentSrl(),
                                mCurrentComment.getContent(),
                                REFERER).enqueue(deleteCallback);
                        break;
                }
            }
        }).show();
    }
    @Override
    public void onClicked() {

    }
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    if(mCommentBar.getCommentType() == CommentBar.EDIT || mCommentBar.getCommentType() == CommentBar.REPLY){
                        mCurrentComment = null;
                        mCommentBar.clearBar();
                    }else if(mSlidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
                        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    else {
                        pauseWebView();
                        getActivity().finish();
                    }
                    return true;
                }
                return false;
            }
        });

    }
    private void pauseWebView(){
        View view;
        for(int i=0 ; i<mContentAdapter.getItemCount(); i++){
            view = mRecyclerView.getChildAt(i);
            if(view != null){
                if(view instanceof LinearLayout){
                    if(((LinearLayout)view).getChildAt(0) != null){
                        if(((LinearLayout)mRecyclerView.getChildAt(i)).getChildAt(0) instanceof WebView)
                            ((WebView) ((LinearLayout)mRecyclerView.getChildAt(i)).getChildAt(0)).destroy();
                    }
                }
            }
        }
    }
    public void setAdapterItems(List<ContentItem> items) {
        List<MultiItemAdapter.Row<?>> rows = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            ContentItem item = items.get(i);
            if (item.getType().equals("cover")) {
                rows.add(
                        MultiItemAdapter.Row.create(item, NewContentAdapter.VIEW_TYPE_COVER));
            }else if(item.getType().equals("text")){
                rows.add(
                        MultiItemAdapter.Row.create(item, NewContentAdapter.VIEW_TYPE_TEXT));
            }else if(item.getType().equals("image")){
                rows.add(
                        MultiItemAdapter.Row.create(item, NewContentAdapter.VIEW_TYPE_IMAGE));
            }else if(item.getType().equals("video")){
                rows.add(
                        MultiItemAdapter.Row.create(item, NewContentAdapter.VIEW_TYPE_VIDEO));
            }else if(item.getType().equals("tags")){
                rows.add(
                        MultiItemAdapter.Row.create(item, NewContentAdapter.VIEW_TYPE_TAGS));
            }else if(item.getType().equals("next")){
                rows.add(
                        MultiItemAdapter.Row.create(item, NewContentAdapter.VIEW_TYPE_NEXT));
            }else if(item.getType().equals("prev")){
                rows.add(
                        MultiItemAdapter.Row.create(item, NewContentAdapter.VIEW_TYPE_PREV));
            }else if(item.getType().equals("divider")){
                rows.add(
                        MultiItemAdapter.Row.create(item, NewContentAdapter.VIEW_TYPE_DIVIDER));
            }
        }

        mContentAdapter.setRows(rows);
        mContentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        mSwipeLayout.setRefreshing(true);
        mCommentQuery.put("act", "dispBoardContentCommentList");
        mCommentQuery.put("document_srl", mCurrentDocument.getDocumentSrl());
        mCommentQuery.put("cpage", "1#comment");
        mTotalCommentPage =  Integer.parseInt(mCurrentDocument.getCommentCount()) / 50  + 1;
        mCommentData.clear();
        onLoadCommentData();
    }
}
