package com.grotesque.saa.home.data;

import android.content.Context;

import com.grotesque.saa.common.api.service.StringResponseInterface;
import com.grotesque.saa.util.ParseUtils;
import com.grotesque.saa.util.ToStringConverterFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.grotesque.saa.util.LogUtils.LOGE;

/**
 * Created by KH on 2016-09-02.
 */
public class PointRankData extends RecentDataDynamicModel{
    private static final String TAG = PointRankData.class.getSimpleName();

    private Retrofit mRetrofit;
    private StringResponseInterface mInterface;
    public ArrayList<PointRankList> mArrayList;
    private OnLoadedListener mOnloadedListener;

    public PointRankData() {
        this.mArrayList = new ArrayList<>();
        this.mRetrofit = new Retrofit.Builder()
                .baseUrl("http://www.serieamania.com/xe/")
                .addConverterFactory(new ToStringConverterFactory())
                .build();
        this.mInterface = mRetrofit.create(StringResponseInterface.class);
    }
    public ArrayList<PointRankList> getPointRankList() {
        if(mArrayList != null)
            return mArrayList;
        return null;
    }

    public void setPointRankList(ArrayList<PointRankList> list) {
        mArrayList.clear();
        mArrayList.addAll(list);
        mOnloadedListener.onLoaded();
    }
    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public void loadData(Context paramContext) {
        mInterface.getPointRanking("point").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                LOGE(TAG, response.body());
                if(response.body() != null){
                    LOGE(TAG, "onResponse");
                    setPointRankList(ParseUtils.parsePointRank(response.body()));
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    public void setOnLoadedListener(OnLoadedListener listener){
        mOnloadedListener = listener;
    }

    public interface OnLoadedListener{
        void onLoaded();
    }

}
