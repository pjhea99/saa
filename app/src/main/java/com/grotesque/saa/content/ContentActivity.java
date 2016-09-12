package com.grotesque.saa.content;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.grotesque.saa.R;
import com.grotesque.saa.home.data.DocumentList;

import java.util.ArrayList;

import static com.grotesque.saa.util.LogUtils.LOGE;
import static com.grotesque.saa.util.LogUtils.makeLogTag;

public class ContentActivity extends YouTubeBaseActivity {
    private static final String TAG = makeLogTag(ContentActivity.class);

    private int mCurrentPosition;
    private String mid;

    private ArrayList<DocumentList> mArrayList;
    private DocumentList mDocuList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Bundle args = getIntent().getExtras();
        if(args != null){
            mid = args.getString("mid");
            mDocuList = args.getParcelable("array");
            mArrayList = args.getParcelableArrayList("arrayList");
            mCurrentPosition = args.getInt("position");
        }
        getFragmentManager().beginTransaction().add(R.id.documentFragment, ContentFragment.newInstance(mid, mDocuList, mArrayList, mCurrentPosition)).commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LOGE(TAG, "onNewIntent");

        Bundle args = intent.getExtras();

        if(args.getString("condition").equals("prev")){
            mCurrentPosition++;
        }else{
            mCurrentPosition--;
        }
        getFragmentManager().beginTransaction().replace(R.id.documentFragment, ContentFragment.newInstance(mid, mDocuList, mArrayList, mCurrentPosition)).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(this).clearMemory();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.documentFragment);
        if(fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
