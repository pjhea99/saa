package com.grotesque.saa.post;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.grotesque.saa.BuildConfig;
import com.grotesque.saa.R;
import com.grotesque.saa.common.fragment.BaseFragment;
import com.grotesque.saa.post.adapter.YoutubeAdapter;
import com.grotesque.saa.post.data.YouTubeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.grotesque.saa.util.LogUtils.LOGE;


/**
 * Created by 경환 on 2016-04-13.
 */
public class AttachmentFragment extends BaseFragment implements YoutubeAdapter.Listener {
    private static final String TAG = AttachmentFragment.class.getSimpleName();

    private boolean loading = false;
    private String mNextPageToken;
    private HashMap<String, String> mSearchQuery = new HashMap<>();
    private TextView mEmptyTextView;
    private RecyclerView mRecyclerView;
    private YoutubeAdapter mAdapter;
    private List<YouTubeData.Item> mArrayList = new ArrayList<>();


    public static AttachmentFragment newInstance() {
        AttachmentFragment fragment  = new AttachmentFragment();
        return fragment;
    }
    public static AttachmentFragment newInstance(String search) {
        AttachmentFragment fragment  = new AttachmentFragment();
        Bundle args = new Bundle();
        args.putString("search", search);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getContentViewResource() {
        return R.layout.fragment_attatchment_search;
    }

    @Override
    protected void initOnCreate(Bundle paramBundle) {
        Bundle args = getArguments();
        if(args != null){
            mSearchQuery.put("q", args.getString("search"));
        }
    }

    @Override
    protected void onInitCreated(Bundle paramBundle) {
        LOGE(TAG, "onInitCreated");
        mAdapter = new YoutubeAdapter(mArrayList, getActivity(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager));
        mRecyclerView.setAdapter(mAdapter);

        mSearchQuery.put("key", BuildConfig.YOUTUBE_API_KEY);
        mSearchQuery.put("part", "snippet");
        enqueueService();
    }

    @Override
    protected void registerView(View view) {
        mEmptyTextView = (TextView) view.findViewById(R.id.empty_message_text_view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.search_list_view);

    }

    @Override
    public void selectVideo(YouTubeData.Item videoItem) {
        if(getActivity() instanceof AttachmentListener)
            ((AttachmentListener)getActivity()).addVideo(videoItem);
    }
    public void searchYoutube(String search){
        mArrayList.clear();
        mAdapter.notifyDataSetChanged();
        mSearchQuery.put("q", search);
        enqueueService();
    }
    private void enqueueService(){
        LOGE(TAG, mSearchQuery.toString());
        YoutubeApi.getInstance().getYoutubeData(mSearchQuery).enqueue(new Callback<YouTubeData>() {
            @Override
            public void onResponse(Call<YouTubeData> call, Response<YouTubeData> response) {
                LOGE(TAG, ""+response.code());
                if(response.code() == 200){
                    if(response.body().getItems().size() != 0 ){
                        mEmptyTextView.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                    mArrayList.addAll(response.body().getItems());
                    mAdapter.notifyDataSetChanged();
                    mNextPageToken = response.body().getNextPageToken();

                    LOGE(TAG, mNextPageToken);
                }
                loading = false;
            }

            @Override
            public void onFailure(Call<YouTubeData> call, Throwable t) {
                loading = false;
            }
        });
    }

    public class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

        private int visibleThreshold = 1;


        private RecyclerView.LayoutManager mLayoutManager;

        public EndlessRecyclerOnScrollListener(LinearLayoutManager layoutManager) {
            this.mLayoutManager = layoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int firstVisibleItem = 0;
            int totalItemCount = mLayoutManager.getItemCount();
            int visibleItemCount = mLayoutManager.getChildCount();

            if (mLayoutManager instanceof LinearLayoutManager) {
                firstVisibleItem = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
            }

            if (!loading && (totalItemCount - visibleItemCount)
                    <= (firstVisibleItem + visibleThreshold)) {
                loading = true;
                if(!mNextPageToken.isEmpty())
                    onLoadMore();
            }

        }

        public void onLoadMore(){
            mSearchQuery.put("pageToken", mNextPageToken);
            enqueueService();
        }
    }
    public interface AttachmentListener{
        void addVideo(YouTubeData.Item videoItem);
    }
}
