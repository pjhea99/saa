package com.grotesque.saa.fixture;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.grotesque.saa.R;
import com.grotesque.saa.common.api.service.StringResponseInterface;
import com.grotesque.saa.common.fragment.BaseActionBarFragment;
import com.grotesque.saa.common.fragment.BaseFragment;
import com.grotesque.saa.fixture.adapter.FixtureAdapter;
import com.grotesque.saa.fixture.data.FixtureData;
import com.grotesque.saa.util.ParseUtils;
import com.grotesque.saa.util.ToStringConverterFactory;
import com.rey.material.widget.ProgressView;

import org.jsoup.Jsoup;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.grotesque.saa.util.LogUtils.LOGE;

/**
 * Created by KH on 2016-08-01.
 */
public class FixtureFragment extends BaseFragment {
    private static final String TAG = FixtureFragment.class.getSimpleName();
    private static final String FIXTURE_URL = "http://www.legaseriea.it/en/serie-a-tim/fixture-and-results/2016-17/UNICO/UNI/";

    private int mRound = 0;

    private ArrayList<FixtureData> mArrayList;
    private FixtureAdapter mAdapter;

    private RelativeLayout mProgressView;
    private RecyclerView mFixtureList;
    private StringResponseInterface mService;

    public static FixtureFragment newInstance(int round){
        LOGE(TAG, round + " newInstance");
        FixtureFragment fragment = new FixtureFragment();
        Bundle args = new Bundle();
        args.putInt("round", round);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentViewResource() {
        return R.layout.fragment_fixture;
    }

    @Override
    protected void initOnCreate(Bundle paramBundle) {
        if(getArguments() != null)
            mRound = getArguments().getInt("round");

        mArrayList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FIXTURE_URL)
                .addConverterFactory(new ToStringConverterFactory())
                .build();
        mService = retrofit.create(StringResponseInterface.class);
    }

    @Override
    protected void onInitCreated(Bundle paramBundle) {
        if(mRound == 1) onLoadFixture();
    }

    protected void registerView(View view) {
        mProgressView = (RelativeLayout) view.findViewById(R.id.loading_progress);
        mAdapter = new FixtureAdapter(getActivity(), mArrayList);
        mFixtureList = (RecyclerView) view.findViewById(R.id.fixtureList);
        mFixtureList.setAdapter(mAdapter);
        mFixtureList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    public void onLoadFixture(){
        LOGE(TAG, "round" + mRound);
        mProgressView.setVisibility(View.VISIBLE);
        mService
                .getStringHtml(String.valueOf(mRound))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        mArrayList.clear();
                        mArrayList.addAll(ParseUtils.parseFixtue(Jsoup.parse(response.body())));
                        mAdapter.notifyDataSetChanged();
                        mProgressView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        mProgressView.setVisibility(View.GONE);
                    }
                });
    }
}
