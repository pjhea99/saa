package com.grotesque.saa.rank;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import com.grotesque.saa.R;
import com.grotesque.saa.common.activity.BaseActivity;

public class RankActivity extends BaseActivity implements RankFragment.Listener{
    private static final String SEARCH_SUBJECT = "Search_Subject";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        getFragmentManager().beginTransaction().add(R.id.main_content, RankFragment.newInstance()).commit();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 4){
            if(resultCode == RESULT_OK){
                String query = data.getStringExtra(SEARCH_SUBJECT);
                Fragment fragment = getFragmentManager().findFragmentById(R.id.main_content);
                if(fragment != null){
                    RankFragment rankFragment = (RankFragment) fragment;
                    rankFragment.onSearch(query);
                }
            }
        }
    }


    @Override
    protected int getSelfNavDrawerItem() {
        return NAV_DRAWER_RANK;
    }

    @Override
    public void detail(String teamId) {
        getFragmentManager().beginTransaction().add(R.id.main_content, RankDetailFragment.newInstance(teamId)).commit();
    }
}
