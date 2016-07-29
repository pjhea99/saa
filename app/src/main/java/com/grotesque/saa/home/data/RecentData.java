package com.grotesque.saa.home.data;

import android.content.Context;

import com.grotesque.saa.common.api.RetrofitApi;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.grotesque.saa.util.LogUtils.LOGE;
import static com.grotesque.saa.util.LogUtils.makeLogTag;

public class RecentData extends RecentDataDynamicModel{
    private static final String TAG = makeLogTag(RecentData.class);
    public ArrayList<DocumentList> calcioList;
    private String mMid;
    private HashMap<String, String> mQuery = new HashMap<>();
    private OnLoadedListener mOnloadedListener;



    public RecentData(String mid) {
        calcioList = new ArrayList<>();
        mMid = mid;
        mQuery.put("act", "dispBoardContentList");
        mQuery.put("mid", mMid);
        mQuery.put("page", "1");
    }
    public ArrayList<DocumentList> getCalcioList() {
        if(calcioList != null)
            return calcioList;
        return null;
    }

    public void setCalcioList(ArrayList<DocumentList> list) {
        calcioList.clear();
        calcioList.addAll(list);
        mOnloadedListener.onLoaded();
    }

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public void loadData(Context context) {

        Call<DocumentContainer> call = RetrofitApi.getInstance().getBoardList(mQuery);
        call.enqueue(new Callback<DocumentContainer>() {
            @Override
            public void onResponse(Call<DocumentContainer> call, Response<DocumentContainer> response) {
                setCalcioList(response.body().getDocumentList());
            }

            @Override
            public void onFailure(Call<DocumentContainer> call, Throwable t) {
                LOGE(TAG, "onFailure : " + t);
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
