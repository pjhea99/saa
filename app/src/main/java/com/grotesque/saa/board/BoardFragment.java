package com.grotesque.saa.board;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grotesque.saa.R;
import com.grotesque.saa.board.adapter.NewBoardAdapter;
import com.grotesque.saa.board.adapter.StaggeredBoardAdapter;
import com.grotesque.saa.board.adapter.VerticalFragmentPagerAdapter;
import com.grotesque.saa.board.data.NoticeContainer;
import com.grotesque.saa.common.adapter.MultiItemAdapter;
import com.grotesque.saa.common.api.RetrofitApi;
import com.grotesque.saa.common.fragment.BaseActionBarFragment;
import com.grotesque.saa.common.widget.NavigationView;
import com.grotesque.saa.common.widget.VerticalViewPager;
import com.grotesque.saa.home.data.DocumentContainer;
import com.grotesque.saa.home.data.DocumentList;
import com.grotesque.saa.util.DensityScaleUtil;
import com.grotesque.saa.util.NavigationUtils;
import com.grotesque.saa.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.grotesque.saa.util.LogUtils.LOGE;
import static com.grotesque.saa.util.LogUtils.makeLogTag;


public class BoardFragment extends BaseActionBarFragment implements SwipeRefreshLayout.OnRefreshListener, ViewPager.PageTransformer, ViewPager.OnPageChangeListener, NavigationView.OnButtonClickListener {
    private static final String TAG = makeLogTag(BoardFragment.class);
    private static final String BOARD_SECTION = "section";

    private int mCurrentPage = 1;

    private String mModuleId;
    private HashMap<String, String> mQuery;
    private ArrayList<DocumentList> mArrayList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private VerticalFragmentPagerAdapter mVerticalAdapter;

    private SwipeRefreshLayout mSwipeLayout;
    private NavigationView mNavigationView;
    private RelativeLayout mProgressView;
    private RelativeLayout mTabLayout;
    private TextView mNoticeBtn;
    private FrameLayout mBasicBtn;
    private FrameLayout mCardBtn;

    public static BoardFragment newInstance() {
        BoardFragment fragment = new BoardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public static BoardFragment newInstance(String section) {
        BoardFragment fragment = new BoardFragment();
        Bundle args = new Bundle();
        args.putString(BOARD_SECTION, section);
        fragment.setArguments(args);
        return fragment;
    }
    public BoardFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentViewResource() {
        return R.layout.fragment_board;
    }

    @Override
    protected void initOnCreate(Bundle paramBundle) {

        if(getArguments() != null)
            mModuleId = getArguments().getString(BOARD_SECTION);

        mQuery = new HashMap<>();
        mQuery.put("act", "dispBoardContentList");
        mQuery.put("mid", mModuleId);
        mQuery.put("page", "1");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void registerView(View view) {
        super.registerView(view);
        VerticalViewPager pager = (VerticalViewPager) view.findViewById(R.id.vertical_pager);


        FrameLayout boardContainer = (FrameLayout) view.findViewById(R.id.boardContainer);

        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setProgressViewOffset(true, DensityScaleUtil.dipToPixel(mContext, 0), DensityScaleUtil.dipToPixel(mContext, 90));
        mSwipeLayout.setProgressBackgroundColorSchemeResource(R.color.brunch_mint);
        mSwipeLayout.setColorSchemeResources(android.R.color.white);

        mNavigationView = (NavigationView) view.findViewById(R.id.navigationView);
        mNavigationView.setOnButtonClickListener(this);
        mNavigationView.setSelectedMid(mModuleId);

        mProgressView = (RelativeLayout) view.findViewById(R.id.loading_progress);
        mTabLayout = (RelativeLayout) view.findViewById(R.id.tabLayout);

        mNoticeBtn = (TextView) view.findViewById(R.id.tab_indicator_notice);
        mBasicBtn = (FrameLayout) view.findViewById(R.id.tab_indicator_basic_board);
        mCardBtn = (FrameLayout) view.findViewById(R.id.tab_indicator_card_board);

        mNoticeBtn.setOnClickListener(onClickListener);
        mBasicBtn.setOnClickListener(onClickListener);
        mCardBtn.setOnClickListener(onClickListener);

        mCardBtn.setSelected(true);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.boardList);
        boardContainer.setVisibility(View.VISIBLE);
        pager.setVisibility(View.GONE);

        switch (mModuleId) {
            case "multimedia1":
                mTabLayout.setVisibility(View.GONE);
                mAdapter = new StaggeredBoardAdapter(mModuleId, mArrayList, mContext);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
                mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
                layoutParams.setMargins(DensityScaleUtil.dipToPixel(mContext, 20F),
                        0,
                        DensityScaleUtil.dipToPixel(mContext, 20F),
                        0);
                mSwipeLayout.setLayoutParams(layoutParams);
                mRecyclerView.addItemDecoration(new StaggeredItemDecoration(mContext));
                mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(staggeredGridLayoutManager));
                break;
            case "specialreport": {
                boardContainer.setVisibility(View.GONE);
                pager.setVisibility(View.VISIBLE);
                setActionBarBackgroundResource(R.color.brunchTransparent);
                setActionBarColor(getResources().getColor(R.color.white));
                mVerticalAdapter = new VerticalFragmentPagerAdapter(getChildFragmentManager(), mArrayList, mModuleId);
                pager.setAdapter(mVerticalAdapter);
                pager.setPageTransformer(false, this);
                pager.setOnPageChangeListener(this);
                pager.setOffscreenPageLimit(1);
                break;
            }
            case "players1":
                boardContainer.setVisibility(View.GONE);
                pager.setVisibility(View.VISIBLE);
                setActionBarBackgroundResource(R.color.brunchTransparent);
                setActionBarColor(getResources().getColor(R.color.white));
                mVerticalAdapter = new VerticalFragmentPagerAdapter(getChildFragmentManager(), mArrayList, mModuleId);
                pager.setAdapter(mVerticalAdapter);
                pager.setPageTransformer(false, this);
                pager.setOnPageChangeListener(this);
                pager.setOffscreenPageLimit(1);
                break;
            default: {
                //mAdapter = new LinearBoardAdapter(mModuleId, mArrayList, mContext);
                mAdapter = new NewBoardAdapter(mModuleId);
                LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
                mRecyclerView.setLayoutManager(mLinearLayoutManager);
                mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager));
                break;
            }
        }


        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onInitCreated(Bundle paramBundle) {
        setActionBarTitle(StringUtils.getBoardName(mModuleId));

        if(mModuleId.equals("players1") || mModuleId.equals("specialreport"))
            onVerticalRefresh();
        else
            onRefresh();

    }
    @Override
    public void onRefresh() {
        mSwipeLayout.setRefreshing(true);
        mCurrentPage = 1;
        mQuery.put("act", "dispBoardContentList");
        mQuery.put("page","1");
        Call<DocumentContainer> call = RetrofitApi.getInstance().getBoardList(mQuery);
        call.enqueue(new Callback<DocumentContainer>() {
            @Override
            public void onResponse(Call<DocumentContainer> call, Response<DocumentContainer> response) {
                mArrayList.clear();
                mArrayList.addAll(response.body().getDocumentList());
                setAdapterItems(mArrayList);
                mAdapter.notifyDataSetChanged();

                mSwipeLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<DocumentContainer> call, Throwable t) {
                LOGE(TAG, "onFailure : " + t);
                if(t.toString().contains("SocketTimeout")){
                    Toast.makeText(mContext, "서버 연결이 지연되고 있습니다.", Toast.LENGTH_LONG).show();
                }

                mSwipeLayout.setRefreshing(false);

            }

        });
    }
    private void onVerticalRefresh(){
        mProgressView.setVisibility(View.VISIBLE);
        mCurrentPage = 1;
        mQuery.put("act", "dispBoardContentList");
        mQuery.put("page","1");
        Call<DocumentContainer> call = RetrofitApi.getInstance().getBoardList(mQuery);
        call.enqueue(new Callback<DocumentContainer>() {
            @Override
            public void onResponse(Call<DocumentContainer> call, Response<DocumentContainer> response) {
                mArrayList.clear();
                mArrayList.addAll(response.body().getDocumentList());
                mVerticalAdapter.notifyDataSetChanged();
                mProgressView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DocumentContainer> call, Throwable t) {
                LOGE(TAG, "onFailure : " + t);
                if(t.toString().contains("SocketTimeout")){
                    Toast.makeText(mContext, "서버 연결이 지연되고 있습니다.", Toast.LENGTH_LONG).show();
                }
                mProgressView.setVisibility(View.GONE);
            }

        });
    }
    public void onLoadNotice(){
        mQuery.put("act", "dispBoardNoticeList");
        RetrofitApi.getInstance().getNoticeList(mQuery).enqueue(new Callback<NoticeContainer>() {
            @Override
            public void onResponse(Call<NoticeContainer> call, Response<NoticeContainer> response) {

                ArrayList<DocumentList> tempList = response.body().getDocumentList();

                for(int i=0; i<tempList.size(); i++) {
                    ((NewBoardAdapter) mAdapter).setRow(1, MultiItemAdapter.Row.create(tempList.get(i), NewBoardAdapter.NOTICE_HOLDER));
                }

                mAdapter.notifyDataSetChanged();
                mNoticeBtn.setSelected(true);
            }

            @Override
            public void onFailure(Call<NoticeContainer> call, Throwable t) {

            }
        });
    }


    private void setAdapterItems(List<DocumentList> items){
        List<MultiItemAdapter.Row<?>> rows = new ArrayList<>();
        rows.add(MultiItemAdapter.Row.create(null, NewBoardAdapter.HEADER_HOLDER));
        for(int i = 0; i<items.size(); i++){
            if(mBasicBtn.isSelected())
                rows.add(MultiItemAdapter.Row.create(items.get(i), NewBoardAdapter.BASIC_HOLDER));
            else
                rows.add(MultiItemAdapter.Row.create(items.get(i), NewBoardAdapter.CARD_HOLDER));
        }
        if(mAdapter instanceof NewBoardAdapter)
            ((NewBoardAdapter) mAdapter).setRows(rows);
    }
    private void changeItemViewType(int itemViewType){
        if(mAdapter instanceof NewBoardAdapter) {
            NewBoardAdapter tempAdapter = (NewBoardAdapter) mAdapter;
            for (int i = 1; i < tempAdapter.getItemCount(); i++) {
                tempAdapter.setItemViewType(i, itemViewType);
            }
            tempAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onRightButtonClicked() {
        super.onRightButtonClicked();
        NavigationUtils.goSearchActivity(mContext, mModuleId);
    }

    @Override
    public void onTitleTextClicked() {
        mNavigationView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void transformPage(View page, float position) {
        int i = page.getHeight();
        if(position < -1F) // 페이지가 완전히 사라졌을 때
        {
            page.setAlpha(0.0F);
            return;
        }
        if(position <= 0.0F) // 페이지가 사라지고 있을 때
        {
            page.setAlpha(1.0F - Math.abs(position));
            page.setTranslationX(0.0F);
            page.setTranslationY((float)i * -position);
            position = 0.7F + (1.0F - 0.7F) * (1.0F - Math.abs(position));
            page.setScaleX(position);
            page.setScaleY(position);
            return;
        }
        if(position <= 1.0F) // 페이지가 나타나고 있을 때
        {
            page.setAlpha(1.0F);
            page.setTranslationX(0.0F);
            page.setScaleX(1.0F);
            page.setScaleY(1.0F);
        } else
        {
            page.setAlpha(0.0F);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClicked(String mid) {
        mRecyclerView.setVisibility(View.VISIBLE);
        if (getActivity() instanceof Listener && !mid.equals("close")) {
            ((Listener) getActivity()).changeBoard(mid);
        }
    }

    public class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

        private int visibleThreshold = 1;
        private boolean loading = false;
        private int hideTheshold = 20;
        private int scrolledDistance = 0;
        private boolean controlsVisible = true;



        private RecyclerView.LayoutManager mLayoutManager;

        public EndlessRecyclerOnScrollListener(LinearLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
        }
        public EndlessRecyclerOnScrollListener(StaggeredGridLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
            visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int firstVisibleItem = 0;
            int totalItemCount = mLayoutManager.getItemCount();
            int visibleItemCount = mLayoutManager.getChildCount();

            if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                int[] firstVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findFirstVisibleItemPositions(null);
                firstVisibleItem = firstVisibleItemPositions[0];
            } else if (mLayoutManager instanceof LinearLayoutManager) {
                firstVisibleItem = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            } else if (mLayoutManager instanceof GridLayoutManager) {
                firstVisibleItem = ((GridLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            }

            if (firstVisibleItem == 0) {
                if(!controlsVisible) {
                    showTab();
                    controlsVisible = true;
                }
            } else {
                if (scrolledDistance > hideTheshold && controlsVisible) {
                    hideTab();
                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -hideTheshold && !controlsVisible) {
                    showTab();
                    controlsVisible = true;
                    scrolledDistance = 0;
                }
            }

            if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
                scrolledDistance += dy;
            }


            if (!loading && (totalItemCount - visibleItemCount)
                    <= (firstVisibleItem + visibleThreshold)) {
                loading = true;
                mCurrentPage++;
                onLoadMore(mCurrentPage);
            }

        }
        public void hideTab(){
            mTabLayout.animate().translationY(-mTabLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        }
        public void showTab(){
            mTabLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        }
        public void onLoadMore(int current_page){
            // 리스트 업데이트 시 하단에 새로고침 표시를 위해 null 삽입


            if(mAdapter instanceof NewBoardAdapter){
                ((NewBoardAdapter) mAdapter).setRow(MultiItemAdapter.Row.create(null, NewBoardAdapter.FOOTER_HOLDER));
                mAdapter.notifyItemInserted(mArrayList.size()+1);
            }else{
                mArrayList.add(null);
                mAdapter.notifyItemInserted(mArrayList.size());
            }

            //
            mQuery.put("page", String.valueOf(current_page));
            Call<DocumentContainer> call = RetrofitApi.getInstance().getBoardList(mQuery);
            call.enqueue(new Callback<DocumentContainer>() {
                @Override
                public void onResponse(Call<DocumentContainer> call, Response<DocumentContainer> response) {

                    if(mAdapter instanceof NewBoardAdapter){
                        ((NewBoardAdapter) mAdapter).removeRow(mArrayList.size() + 1);
                        mAdapter.notifyItemRemoved(mArrayList.size() + 1);
                    }else{
                        mArrayList.remove(mArrayList.size() - 1);
                        mAdapter.notifyItemRemoved(mArrayList.size());
                    }

                    mArrayList.addAll(response.body().getDocumentList());
                    setAdapterItems(mArrayList);
                    mAdapter.notifyDataSetChanged();
                    loading = false;
                }

                @Override
                public void onFailure(Call<DocumentContainer> call, Throwable t) {
                    LOGE(TAG, "onFailure : " + t);
                }

            });
        }
    }
    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tab_indicator_notice:
                    if(mNoticeBtn.isSelected()){
                        int nc = mAdapter.getItemCount() - (mArrayList.size() +1);
                        for(int i=0; i< nc; i++) {
                            ((NewBoardAdapter) mAdapter).removeRow(1);
                        }
                        mAdapter.notifyDataSetChanged();
                        mNoticeBtn.setSelected(false);
                    }else
                        onLoadNotice();
                    break;
                case R.id.tab_indicator_basic_board:
                    mBasicBtn.setSelected(true);
                    mCardBtn.setSelected(false);
                    changeItemViewType(NewBoardAdapter.BASIC_HOLDER);
                    break;
                case R.id.tab_indicator_card_board:
                    mBasicBtn.setSelected(false);
                    mCardBtn.setSelected(true);
                    changeItemViewType(NewBoardAdapter.CARD_HOLDER);
                    break;
            }
        }
    };

    public interface Listener{
        void changeBoard(String mid);
    }
}
