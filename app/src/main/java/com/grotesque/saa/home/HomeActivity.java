package com.grotesque.saa.home;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.grotesque.saa.R;
import com.grotesque.saa.activity.BaseActivity;
import com.grotesque.saa.auth.AuthTokenManager;
import com.grotesque.saa.util.AccountUtils;
import com.grotesque.saa.util.LoginAndAuthHelper;

import static com.grotesque.saa.util.LogUtils.LOGE;
import static com.grotesque.saa.util.LogUtils.makeLogTag;


public class HomeActivity extends BaseActivity implements LoginAndAuthHelper.Callbacks {

    private static final String TAG = makeLogTag(HomeActivity.class);

    private long backKeyPressedTime = 0;
    private long startTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTime = SystemClock.elapsedRealtime();
        LOGE(TAG, "start : " + startTime);
        setContentView(R.layout.activity_home);
        getFragmentManager().beginTransaction().add(R.id.main_content, HomeFragment.newInstance()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //startLoginProcess();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(this).clearMemory();
        clearApplicationCache(null);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAV_DRAWER_INVALID;
    }

    @Override
    public void onBackPressed() {
        Toast toast = Toast.makeText(this, "뒤로 버튼을 한번 더 누르시면 종료됩니다.",
                Toast.LENGTH_SHORT);

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
        super.onBackPressed();
    }




    private void startLoginProcess() {

        if(!AccountUtils.getAutoLogin(this)){
            return;
        }
        String accountName = AccountUtils.getActiveAccountName(this);
        String password = AuthTokenManager.getInstance().getAuthToken(this);


        mLoginAndAuthHelper = new LoginAndAuthHelper(this, this, accountName, password);
        mLoginAndAuthHelper.start();
    }



    @Override
    public void onAuthSuccess(String accountName, boolean newlyAuthenticated) {
    }

    @Override
    public void onAuthFailure(int error) {
        switch (error){
            case 2:
                Toast.makeText(this, "자동 로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void clearApplicationCache(java.io.File dir){
        if(dir==null)
            dir = getCacheDir();
        else;
        if(dir==null)
            return;
        else;
        java.io.File[] children = dir.listFiles();
        try{
            for(int i=0;i<children.length;i++)
                if(children[i].isDirectory())
                    clearApplicationCache(children[i]);
                else children[i].delete();
        }
        catch(Exception e){}
    }


}
