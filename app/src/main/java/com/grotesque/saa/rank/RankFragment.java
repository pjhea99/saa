package com.grotesque.saa.rank;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grotesque.saa.R;
import com.grotesque.saa.common.api.service.StringResponseInterface;
import com.grotesque.saa.common.fragment.BaseActionBarFragment;
import com.grotesque.saa.rank.adapter.RankLandAdapter;
import com.grotesque.saa.rank.adapter.RankPortAdapter;
import com.grotesque.saa.rank.data.LeagueTable;
import com.grotesque.saa.util.ParseUtils;
import com.grotesque.saa.util.ToStringConverterFactory;

import org.jsoup.Jsoup;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.grotesque.saa.util.LogUtils.LOGE;
import static com.grotesque.saa.util.LogUtils.makeLogTag;


public class RankFragment extends BaseActionBarFragment implements RankPortAdapter.Callback {
    private static final String TAG = makeLogTag(RankFragment.class);
    ArrayList<LeagueTable> mArrayList = new ArrayList<>();

    RecyclerView.LayoutManager mLayoutManager;
    RankPortAdapter mPortAdapter;
    RankLandAdapter mLandAdapter;

    RecyclerView mRecyclerView;
    StringResponseInterface mService;


    public static RankFragment newInstance() {
        RankFragment fragment = new RankFragment();
        return fragment;
    }

    public RankFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentViewResource() {
        return R.layout.fragment_rank;
    }

    @Override
    protected void initOnCreate(Bundle paramBundle) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://score.sports.media.daum.net/")
                .addConverterFactory(new ToStringConverterFactory())
                .build();
        mService = retrofit.create(StringResponseInterface.class);
        mPortAdapter = new RankPortAdapter(getActivity(), R.layout.listrow_rank, mArrayList, this);
        mLandAdapter = new RankLandAdapter(getActivity(), R.layout.listrow_rank_land, mArrayList);
    }

    @Override
    protected void registerView(View view) {
        super.registerView(view);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rank_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        Configuration config = getResources().getConfiguration();
        if(config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setAdapter(mLandAdapter);
        }else{
            mRecyclerView.setAdapter(mPortAdapter);
        }
    }

    @Override
    protected void onInitCreated(Bundle paramBundle) {
        mService
                .getStringHtml("record/soccer/seriea/trnk.daum")
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                            LOGE(TAG, response.body());
                            mArrayList.clear();
                            mArrayList.addAll(ParseUtils.parseRankDaum(Jsoup.parse(response.body())));
                            mLandAdapter.notifyDataSetChanged();
                            mPortAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        LOGE(TAG, String.valueOf(t));
                    }
                });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            populateViewForOrientation(inflater, (ViewGroup) getView());
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            populateViewForOrientation(inflater, (ViewGroup) getView());
        }
    }
    private void populateViewForOrientation(LayoutInflater inflater, ViewGroup viewGroup) {
        viewGroup.removeAllViewsInLayout();
        View subview = inflater.inflate(R.layout.fragment_rank, viewGroup);
        mRecyclerView = (RecyclerView) subview.findViewById(R.id.rank_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Configuration config = getResources().getConfiguration();
        if(config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setAdapter(mLandAdapter);
        }else{
            mRecyclerView.setAdapter(mPortAdapter);
        }
    }

    public void onSearch(String query){
        for(int i = 0; i<mArrayList.size(); i++){
            if(mArrayList.get(i).getTeam().contains(query)) {
                mArrayList.get(i).setSelected(true);
                mRecyclerView.scrollToPosition(i);
            }else
                mArrayList.get(i).setSelected(false);

        }
        mLandAdapter.notifyDataSetChanged();
        mPortAdapter.notifyDataSetChanged();
    }

    @Override
    public void clicked(String teamId) {
        if (getActivity() instanceof Listener) {
            ((Listener) getActivity()).detail(teamId);
        }
    }

    public interface Listener{
        void detail(String teamId);
    }
}
