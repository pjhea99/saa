package com.grotesque.saa.content;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.grotesque.saa.R;
import com.grotesque.saa.home.data.DocumentList;

import java.util.ArrayList;

import static com.grotesque.saa.util.LogUtils.LOGE;
import static com.grotesque.saa.util.LogUtils.makeLogTag;

public class ContentActivity extends YouTubeBaseActivity implements ViewPager.PageTransformer {
    private static final String TAG = makeLogTag(ContentActivity.class);

    private int mCurrentPosition;
    private String mid;

    private ArrayList<DocumentList> mArrayList;
    private DocumentList mDocuList;

    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

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

        if(mArrayList == null) {
            mArrayList = new ArrayList<>();
            mArrayList.add(mDocuList);
        }else{
            for(int i = 0; i < mArrayList.size() ; i++) {
                if(mArrayList.get(i) == null)
                    mArrayList.remove(i);
            }
        }
        mPagerAdapter = new PagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.documentFragment);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setPageTransformer(false, this);
        mViewPager.setCurrentItem(mCurrentPosition);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LOGE(TAG, "onNewIntent");
        Bundle args = intent.getExtras();
        String direction = args.getString("direction");
        if(direction != null && direction.equals("prev")){
            mCurrentPosition++;
        }else{
            mCurrentPosition--;
        }
        mViewPager.setCurrentItem(mCurrentPosition);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(this).clearMemory();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //mPagerAdapter.getItem(mViewPager.getCurrentItem());
        Fragment fragment = getFragmentManager().findFragmentByTag("android:switcher:" + R.id.documentFragment + ":" + mViewPager.getCurrentItem());
        if(fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void transformPage(View page, float position) {
        int i = page.getWidth();
        if(position < -1F) {// 페이지가 완전히 사라졌을 때
            page.setAlpha(0.0F);
            return;
        }
        if(position <= 0.0F) {// 페이지가 사라지고 있을 때
            page.setAlpha(1.0F - Math.abs(position));
            page.setTranslationX((float)i * -position);

            position = 0.7F + (1.0F - 0.7F) * (1.0F - Math.abs(position));
            page.setScaleX(position);
            page.setScaleY(position);
            return;
        }
        if(position <= 1.0F) // 페이지가 나타나고 있을 때
        {
            page.setAlpha(1.0F);
            //page.setTranslationY(0.0F);
            page.setScaleX(1.0F);
            page.setScaleY(1.0F);
        } else
        {
            page.setAlpha(0.0F);
        }
    }


    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ContentFragment.newInstance(mid, mDocuList, mArrayList, position);
        }

        @Override
        public int getCount() {
            return mArrayList.size();
        }
    }
}
