package com.grotesque.saa.common.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grotesque.saa.R;
import com.grotesque.saa.board.BoardActivity;
import com.grotesque.saa.fixture.FixtureActivity;
import com.grotesque.saa.post.PostActivity;
import com.grotesque.saa.rank.RankActivity;
import com.grotesque.saa.util.AccountUtils;
import com.grotesque.saa.util.ImageLoader;
import com.grotesque.saa.util.LUtils;
import com.grotesque.saa.util.LoginAndAuthHelper;

import static com.grotesque.saa.util.LogUtils.LOGE;
import static com.grotesque.saa.util.LogUtils.makeLogTag;


public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = makeLogTag(BaseActivity.class);
    private static final int NAV_DRAWER_ITEM_SIZE = 6;
    public static final int NAV_DRAWER_INVALID = -1;
    public static final int NAV_DRAWER_BOARD = 0;
    public static final int NAV_DRAWER_RANK = 1;
    public static final int NAV_DRAWER_WRITE = 2;
    public static final int NAV_DRAWER_MEMO = 3;
    public static final int NAV_DRAWER_FIXTURE = 4;
    public static final int NAV_DRAWER_SETTING = 5;

    private static final int MAIN_CONTENT_FADEOUT_DURATION = 150;
    private static final int MAIN_CONTENT_FADEIN_DURATION = 250;

    private static final int NAVDRAWER_LAUNCH_DELAY = 250;


    public static LoginAndAuthHelper mLoginAndAuthHelper;

    private LUtils mLUtils;
    private ImageLoader mImageLoader;
    private Handler mHandler;
    private DrawerLayout mDrawerLayout;
    private View[] mNavDrawerItemViews = null;

    private ImageView mProfileView;
    private TextView mNameView;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();

        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate().alpha(1).setDuration(MAIN_CONTENT_FADEIN_DURATION);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLUtils = LUtils.getInstance(this);
        mImageLoader = new ImageLoader(this);
        mHandler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLoginAndAuthHelper != null) {
            mLoginAndAuthHelper.stop();
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    public void setupNavDrawer(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(mDrawerLayout == null){
            LOGE(TAG, "drawerLayout is a null");
            return;
        }
        LinearLayout navDrawer = (LinearLayout) findViewById(R.id.navdrawer);
        if(navDrawer != null) {
            ImageButton settingButton = (ImageButton) findViewById(R.id.setting_button);

            settingButton.setOnClickListener(this);

            mNameView = (TextView) findViewById(R.id.profileName);
            mProfileView = (ImageView) findViewById(R.id.profileImage);


            mNameView.setText(AccountUtils.getActiveAccountName(this));
            mProfileView.setOnClickListener(this);

            View board = findViewById(R.id.drawer_item_board);
            View rank = findViewById(R.id.drawer_item_rank);
            View write = findViewById(R.id.drawer_item_write);
            View memo = findViewById(R.id.drawer_item_memo);
            View fixture = findViewById(R.id.drawer_item_fixture);


            mNavDrawerItemViews = new View[NAV_DRAWER_ITEM_SIZE];
            mNavDrawerItemViews[NAV_DRAWER_BOARD] = board;
            mNavDrawerItemViews[NAV_DRAWER_RANK] = rank;
            mNavDrawerItemViews[NAV_DRAWER_WRITE] = write;
            mNavDrawerItemViews[NAV_DRAWER_MEMO] = memo;
            mNavDrawerItemViews[NAV_DRAWER_FIXTURE] = fixture;
            mNavDrawerItemViews[NAV_DRAWER_SETTING] = settingButton;

            if (getSelfNavDrawerItem() != -1)
                mNavDrawerItemViews[getSelfNavDrawerItem()].setSelected(true);

            for (View v : mNavDrawerItemViews) {
                v.setOnClickListener(this);
            }
        }

    }
    protected int getSelfNavDrawerItem() {
        return NAV_DRAWER_INVALID;
    }

    public void goToNavDrawerItem(int i){
        switch (i){
            case NAV_DRAWER_BOARD:
                createBackStack(new Intent(this, BoardActivity.class).putExtra("mid", "freeboard3"));
                return;
            case NAV_DRAWER_RANK:
                createBackStack(new Intent(this, RankActivity.class));
                return;
            case NAV_DRAWER_WRITE:
                startActivity(new Intent(this, PostActivity.class));
                return;
            case NAV_DRAWER_MEMO:
                return;
            case NAV_DRAWER_FIXTURE:
                createBackStack(new Intent(this, FixtureActivity.class));
                return;
            case NAV_DRAWER_SETTING:
                return;
        }
    }

    private void createBackStack(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            TaskStackBuilder builder = TaskStackBuilder.create(this);
            builder.addNextIntentWithParentStack(intent);
            builder.startActivities();
            overridePendingTransition(0, 0);
        } else {
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
    }
    private void onNavDrawerItemClicked(final int itemId) {
        if (itemId == getSelfNavDrawerItem()) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        if(itemId == NAV_DRAWER_WRITE || itemId == NAV_DRAWER_MEMO){
            goToNavDrawerItem(itemId);
        }else{
            // launch the target Activity after a short delay, to allow the close animation to play
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToNavDrawerItem(itemId);
                }
            }, NAVDRAWER_LAUNCH_DELAY);

            // change the active item on the list so the user can see the item changed
            setSelectedNavDrawerItem(itemId);
            // fade out the main content
            View mainContent = findViewById(R.id.main_content);
            if (mainContent != null) {
                mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
            }
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }
    public void setSelectedNavDrawerItem(int itemId){
        mNavDrawerItemViews[itemId].setSelected(true);
    }

    public boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    public void openNavDrawer() {
        if(mDrawerLayout != null) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.drawer_item_board:
                onNavDrawerItemClicked(NAV_DRAWER_BOARD);
                break;
            case R.id.drawer_item_rank:
                onNavDrawerItemClicked(NAV_DRAWER_RANK);
                break;
            case R.id.drawer_item_write:
                onNavDrawerItemClicked(NAV_DRAWER_WRITE);
                break;
            case R.id.drawer_item_memo:
                onNavDrawerItemClicked(NAV_DRAWER_MEMO);
                break;
            case R.id.drawer_item_fixture:
                onNavDrawerItemClicked(NAV_DRAWER_FIXTURE);
                break;
            case R.id.setting_button:
                onNavDrawerItemClicked(NAV_DRAWER_SETTING);
                break;
            case R.id.profileImage:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 5) {
            if (resultCode == RESULT_OK) {

            }
        }
    }
}
