package com.grotesque.saa.comment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.grotesque.saa.R;
import com.grotesque.saa.comment.adapter.NewCommentAdapter;
import com.grotesque.saa.comment.data.CommentData;
import com.grotesque.saa.common.api.RetrofitApi;

import com.grotesque.saa.common.fragment.BaseActionBarFragment;
import com.grotesque.saa.common.widget.CommentBar;
import com.grotesque.saa.common.widget.CustomAlertDialog;
import com.grotesque.saa.common.widget.TypefacedTextView;
import com.grotesque.saa.comment.adapter.CommentAdapter;
import com.grotesque.saa.content.data.CommentContainer;
import com.grotesque.saa.content.data.CommentList;
import com.grotesque.saa.home.data.DocumentList;
import com.grotesque.saa.util.AccountUtils;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.grotesque.saa.util.LogUtils.LOGE;
import static com.grotesque.saa.util.LogUtils.makeLogTag;


/**
 * Created by KH on 2016-07-30.
 */
public class CommentListFragment extends BaseActionBarFragment implements CommentBar.OnCommentBarListener, SwipeRefreshLayout.OnRefreshListener, NewCommentAdapter.Listener {

    private static final String TAG = makeLogTag(CommentListFragment.class);

    private int mTotalCommentPage;
    private int mCurrentCommentPage = 1;

    private HashMap<String, String> mCommentQuery = new HashMap<>();

    private String mMid;
    private String REFERER;

    private DocumentList mDocument;
    private CommentData mComment;

    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRecyclerView;
    private TypefacedTextView mEptyText;
    private RelativeLayout mProgressView;
    private CommentBar mCommentBar;

    private ArrayList<CommentList> mCommentList = new ArrayList<>();
    private ArrayList<CommentData> mCommentList1 = new ArrayList<>();
    private CommentAdapter mCommentAdapter;
    private NewCommentAdapter mCommentAdapter1;



    public static CommentListFragment newInstance (String mid, DocumentList document){
        CommentListFragment fragment = new CommentListFragment();
        Bundle args = new Bundle();
        args.putString("mid", mid);
        args.putParcelable("document", document);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentViewResource() {
        return R.layout.fragment_comment_list;
    }

    @Override
    protected void initOnCreate(Bundle paramBundle) {
        Bundle args = getArguments();
        if(args != null){
            mMid = args.getString("mid");
            mDocument = args.getParcelable("document");
        }
        REFERER = RetrofitApi.API_BASE_URL + mMid +"/"+mDocument.getDocumentSrl();
    }

    @Override
    protected void onInitCreated(Bundle paramBundle) {
        mCommentQuery.put("act", "dispBoardContentCommentList");
        mCommentQuery.put("document_srl", mDocument.getDocumentSrl());
        mCommentQuery.put("cpage", "1#comment");
        mTotalCommentPage =  Integer.parseInt(mDocument.getCommentCount()) / 100  + 1;
        mProgressView.setVisibility(View.VISIBLE);
        onLoadCommentData();
    }

    @Override
    protected void registerView(View view) {
        super.registerView(view);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.commentSwipeLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.commentList);
        mProgressView = (RelativeLayout) view.findViewById(R.id.loading_progress);
        mCommentBar = (CommentBar) view.findViewById(R.id.commentBar);
        mCommentBar.setOnCommentBarListener(this);

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setProgressBackgroundColorSchemeResource(R.color.brunch_mint);
        mSwipeLayout.setColorSchemeResources(android.R.color.white);

        mCommentAdapter1 = new NewCommentAdapter(getActivity(), mCommentList1, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mCommentAdapter1);

        mEptyText = (TypefacedTextView) view.findViewById(R.id.emptyText);
    }
    private void onLoadCommentData(){
        Call<CommentContainer> call = RetrofitApi.getInstance().getCommentList(mCommentQuery);
        call.enqueue(new Callback<CommentContainer>() {
            @Override
            public void onResponse(Call<CommentContainer> call, Response<CommentContainer> response) {
                if(response.code() == 200) {

                    mCommentList.addAll(response.body().getCommentList());

                    if(mTotalCommentPage > 1 && mTotalCommentPage > mCurrentCommentPage){
                        mCommentQuery.put("cpage", (++mCurrentCommentPage) + "#comment");
                        onLoadCommentData();
                    }else{
                        if(mCommentList.size() == 0)
                            mEptyText.setVisibility(View.VISIBLE);
                        else
                            mEptyText.setVisibility(View.GONE);

                        mCommentList1.addAll(onConvertComment(mCommentList));
                        mCommentAdapter1.notifyDataSetChanged();
                        mCommentList.clear();
                        mProgressView.setVisibility(View.GONE);
                    }
                    mSwipeLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<CommentContainer> call, Throwable t) {

            }

        });
    }

    private ArrayList<CommentData> onConvertComment(ArrayList<CommentList> commentLists){
        setActionBarTitleCount(commentLists.size());
        ArrayList<CommentData> commentDatas = new ArrayList<>();
        for(CommentList c : commentLists){
            if(c.getParentSrl().equals("0"))
                commentDatas.add(new CommentData(c, commentLists));
        }
        return commentDatas;
    }
    @Override
    public void onCommentButtonClick(int type, String content) {
        switch (type){
            case CommentBar.SUBMIT:
                RetrofitApi.getInstance()
                        .submitComment(mMid
                                , ""
                                , mDocument.getDocumentSrl()
                                , ""
                                , content
                                , RetrofitApi.API_BASE_URL + mMid +"/"+mDocument.getDocumentSrl())
                        .enqueue(commentCallback);
                break;
            case CommentBar.REPLY:
                RetrofitApi.getInstance()
                        .submitComment(mMid
                                , mComment.getCommentSrl()
                                , mDocument.getDocumentSrl()
                                , ""
                                , content
                                , RetrofitApi.API_BASE_URL + mMid +"/"+mDocument.getDocumentSrl())
                        .enqueue(commentCallback);
                break;
            case CommentBar.EDIT:
                RetrofitApi.getInstance()
                        .submitComment(mMid
                                , ""
                                , mDocument.getDocumentSrl()
                                , mComment.getCommentSrl()
                                , content
                                , RetrofitApi.API_BASE_URL + mMid +"/"+mDocument.getDocumentSrl())
                        .enqueue(commentCallback);
                break;
        }
        return;
    }

    @Override
    public void onRefresh() {
        mSwipeLayout.setRefreshing(true);
        mCurrentCommentPage = 1;
        mCommentQuery.put("act", "dispBoardContentCommentList");
        mCommentQuery.put("document_srl", mDocument.getDocumentSrl());
        mCommentQuery.put("cpage", mCurrentCommentPage + "#comment");
        mTotalCommentPage =  Integer.parseInt(mDocument.getCommentCount()) / 50  + 1;
        mCommentList1.clear();
        onLoadCommentData();
    }

    @Override
    public void onCommentLongClick(final int position) {
        mComment = mCommentList1.get(position);
        ArrayList arraylist = new ArrayList();
        arraylist.add("답변");
        if(AccountUtils.getActiveAccountName(mContext).equals(mComment.getUserId())) {
            arraylist.add("수정");
            arraylist.add("삭제");
        }
        CharSequence acharsequence[] = (CharSequence[])arraylist.toArray(new CharSequence[arraylist.size()]);
        (new CustomAlertDialog(mContext)).setItems(acharsequence, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        mCommentBar.setHint("@"+mComment.getUserName());
                        mCommentBar.setRightButtonText(CommentBar.REPLY);
                        break;
                    case 1:
                        mCommentBar.setRightButtonText(CommentBar.EDIT);
                        mCommentBar.setText(mCommentList.get(position).getContent());
                        break;
                    case 2:
                        RetrofitApi.getInstance().procBoardDeleteComment(
                                mMid,
                                mDocument.getDocumentSrl(),
                                mComment.getContent(),
                                REFERER).enqueue(deleteCallback);
                        break;
                }
            }
        }).show();
    }

    @Override
    public void onReplyClick(int position) {
        ArrayList<CommentData> arrayList = mCommentList1.get(position).getCommentDatas();
        for(CommentData c : arrayList){
            LOGE(TAG, c.getContent());
            mCommentList1.add(++position, c);
        }
        mCommentAdapter1.notifyDataSetChanged();
        LOGE(TAG, "comment size : "+mCommentList1.size());
    }
    private final Callback<JsonObject> commentCallback = new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if(response.body().get("message").toString().contains("등록했습니다.")){
                Toast.makeText(mContext
                        , "댓글 등록"
                        , Toast.LENGTH_LONG)
                        .show();
                mCommentBar.clearBar();
                mCommentList.clear();
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
                    mCommentList.clear();
                    onLoadCommentData();

                }
            }
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {

        }
    };
}
