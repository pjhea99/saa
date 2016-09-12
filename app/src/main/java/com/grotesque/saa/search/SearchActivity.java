package com.grotesque.saa.search;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.grotesque.saa.R;
import com.grotesque.saa.common.api.service.StringResponseInterface;
import com.grotesque.saa.common.toolbar.SearchToolbar;
import com.grotesque.saa.common.widget.TypefacedTextView;
import com.grotesque.saa.search.adapter.SearchItemAdapter;
import com.grotesque.saa.search.data.SearchItem;
import com.grotesque.saa.util.ParseUtils;
import com.grotesque.saa.util.ToStringConverterFactory;
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by 경환 on 2016-04-23.
 */
public class SearchActivity extends Activity implements SearchToolbar.SearchBarListener {
    private static final String TAG = SearchActivity.class.getSimpleName();

    private ProgressView mProgressView;
    private Retrofit mRetrofit;
    private StringResponseInterface mInterface;
    private SearchToolbar mSearchToolbar;
    private RecyclerView mRecyclerView;
    private TypefacedTextView mEmptyText;
    private SearchItemAdapter mAdapter;
    private ArrayList<SearchItem> mArrayList = new ArrayList<>();
    private HashMap<String, String> mQuery = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle args = getIntent().getExtras();
        String mid = args.getString("mid");

        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://www.serieamania.com/xe/")
                .addConverterFactory(new ToStringConverterFactory())
                .build();
        mInterface = mRetrofit.create(StringResponseInterface.class);

        mAdapter = new SearchItemAdapter(mid, mArrayList, this);

        mSearchToolbar = (SearchToolbar) findViewById(R.id.searchBar);
        mSearchToolbar.setSearchBarListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mEmptyText = (TypefacedTextView) findViewById(R.id.emptyText);

        mProgressView = (ProgressView) findViewById(R.id.loading_progress);

        mQuery.put("act", "");
        mQuery.put("mid", mid);
        mQuery.put("search_target", "title_content");

    }

    @Override
    public void cancelButtonClicked(View paramView) {
        finish();
    }

    @Override
    public void doSearch(String paramString) {
        mQuery.put("search_keyword", paramString);
        mProgressView.setVisibility(View.VISIBLE);
        mInterface
                .getSearch(mQuery)
                .enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body() != null) {
                    mArrayList.clear();
                    ArrayList<?> arrayList = ParseUtils.parseSearch(response.body());
                    if(arrayList != null) {
                        mEmptyText.setVisibility(View.GONE);
                        mArrayList.addAll((Collection<? extends SearchItem>) arrayList);
                    }else
                        mEmptyText.setVisibility(View.VISIBLE);

                    mAdapter.notifyDataSetChanged();
                }
                mProgressView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mProgressView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void searchTextChanged(String paramString) {

    }
}
