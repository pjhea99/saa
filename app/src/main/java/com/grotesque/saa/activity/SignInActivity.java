package com.grotesque.saa.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.grotesque.saa.R;
import com.grotesque.saa.auth.AuthTokenManager;
import com.grotesque.saa.home.HomeActivity;
import com.grotesque.saa.util.AccountUtils;
import com.grotesque.saa.util.FontManager;
import com.grotesque.saa.util.LoginAndAuthHelper;
import com.grotesque.saa.util.PermissionUtils;

import static com.grotesque.saa.util.LogUtils.LOGD;
import static com.grotesque.saa.util.LogUtils.LOGE;
import static com.grotesque.saa.util.LogUtils.makeLogTag;


public class SignInActivity extends AppCompatActivity implements LoginAndAuthHelper.Callbacks {
    private static final String TAG = makeLogTag(SignInActivity.class);
    private final int MY_PERMISSION_REQUEST_PHONE_STATE = 100;

    private static final int SPLASH_TIME = 2000;
    private long startTime;
    private LoginAndAuthHelper mLoginAndAuthHelper;
    private LinearLayout mLogoLayout;
    private LinearLayout mLoginLayout;
    private RelativeLayout mLoadingProgress;
    private EditText mIdView;
    private EditText mPasswordView;
    private SimpleDraweeView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mImageView = (SimpleDraweeView) findViewById(R.id.imageView);

        mLoadingProgress = (RelativeLayout) findViewById(R.id.loading_progress);
        mLogoLayout = (LinearLayout) findViewById(R.id.logoLayout);
        mLoginLayout = (LinearLayout) findViewById(R.id.loginLayout);

        mIdView = (EditText) findViewById(R.id.idView);
        mPasswordView = (EditText) findViewById(R.id.passwordView);

        ImageRequestBuilder imageRequestBuilder =
                ImageRequestBuilder.newBuilderWithResourceId(R.drawable.bg_sign_in);
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequestBuilder.build())
                .setOldController(mImageView.getController())
                .build();
        mImageView.setController(draweeController);


        mIdView.setTypeface(FontManager.getInstance(this).getTypeface());
        mPasswordView.setTypeface(FontManager.getInstance(this).getTypeface());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkPermission();
        }else{
            isAutoLogin();
        }





        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.signin || id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    showProgress(true);
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

    }
    private void isAutoLogin(){
        if (!AccountUtils.getAutoLogin(this)) showProgress(false);
        else {
            showProgress(true);
            autoLogin();
        }
    }

    private boolean isIdValid(String id) {
        return id.length() > 3;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

    public void attemptLogin() {
        String id;
        String password = "";

        boolean cancel = false;
        View focusView = null;


        // Reset errors.
        mIdView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        id = mIdView.getText().toString();
        password = mPasswordView.getText().toString();
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(id) || !isIdValid(id)) {
            mIdView.setError("아이디가 유효하지 않습니다");
            focusView = mIdView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError("암호가 유효하지 않습니다");
            focusView = mPasswordView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            showProgress(false);
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (mLoginAndAuthHelper != null && mLoginAndAuthHelper.getAccountName().equals(id)) {
                LOGD(TAG, "Helper already set up; simply starting it.");
                mLoginAndAuthHelper.start();
                return;
            }

            LOGD(TAG, "Starting login process with account " + id);

            if (mLoginAndAuthHelper != null) {
                LOGD(TAG, "Tearing down old Helper, was " + mLoginAndAuthHelper.getAccountName());
                if (mLoginAndAuthHelper.isStarted()) {
                    LOGD(TAG, "Stopping old Helper");
                    mLoginAndAuthHelper.stop();
                }
                mLoginAndAuthHelper = null;
            }

            mLoginAndAuthHelper = new LoginAndAuthHelper(this, this, id, password);
            mLoginAndAuthHelper.start();

        }
    }

    public void autoLogin() {
        String accountName = AccountUtils.getActiveAccountName(this);
        String password = AuthTokenManager.getInstance().getAuthToken(this);


        mLoginAndAuthHelper = new LoginAndAuthHelper(this, this, accountName, password);
        mLoginAndAuthHelper.start();
    }

    @Override
    public void onAuthSuccess(String accountName, boolean newlyAuthenticated) {
        AccountUtils.setLoginState(this, true);
        AccountUtils.setAutoLogin(this, true);
        LOGE(TAG, "로그인 성공");
        overridePendingTransition(0, android.R.anim.fade_in);
        startActivity(new Intent(SignInActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void onAuthFailure(int error) {
        switch (error) {
            case 0:
                mIdView.setError("존재하지 않는 회원 아이디입니다.");
                mIdView.requestFocus();
                break;
            case 1:
                mPasswordView.setError("잘못된 비밀번호입니다.");
                mPasswordView.setText("");
                mPasswordView.requestFocus();
                break;
            case 2:
                Toast.makeText(this, "연결이 원할하지 않아 로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();
                break;
        }
        mLoginAndAuthHelper = null;
        showProgress(false);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            if (getBaseContext() != null) {
                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                mLoginLayout.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoginLayout.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
                    }
                });
                mLogoLayout.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLogoLayout.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
                    }
                });
                mLoadingProgress.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoadingProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
            }
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoadingProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginLayout.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
            mLogoLayout.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        if (PermissionUtils.hasMustHavePermissions(this)) {
            isAutoLogin();
            return;
        }

        String[] getNotGrantedPermissions = PermissionUtils.getNotGrantedPermissions(this, PermissionUtils.MUST_HAVE_PERMISSIONS);
        if (getNotGrantedPermissions != null) {
            ActivityCompat.requestPermissions(this, getNotGrantedPermissions, 207);
            return;
        }
        isAutoLogin();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (!PermissionUtils.hasMustHavePermissions(this)) {
            Toast.makeText(this, getString(R.string.permission_finish_message_by_must_have), Toast.LENGTH_LONG).show();
            finish();
        }else isAutoLogin();

    }
}
