package com.grotesque.saa.board;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.grotesque.saa.R;
import com.grotesque.saa.common.activity.BaseActivity;

import static com.grotesque.saa.util.LogUtils.makeLogTag;

/**
 * Created by KH on 2015-10-26.
 */
public class BoardActivity extends BaseActivity implements BoardFragment.Listener{
    private static final String TAG = makeLogTag(BoardActivity.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Bundle args = getIntent().getExtras();

        String currentBoard = null;
        if(args != null){
            currentBoard = args.getString("mid");
        }
        getFragmentManager().beginTransaction().add(R.id.main_content, BoardFragment.newInstance(currentBoard)).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(this).clearMemory();
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAV_DRAWER_BOARD;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void changeBoard(String mid) {
        getFragmentManager().beginTransaction().replace(R.id.main_content, BoardFragment.newInstance(mid)).commit();
    }
}
