package com.grotesque.saa.common.fragment;

import android.app.Activity;
import android.view.View;

import com.grotesque.saa.common.activity.BaseActivity;
import com.grotesque.saa.common.actionbar.BaseActionBar;

import static com.grotesque.saa.util.LogUtils.makeLogTag;

/**
 * Created by 경환 on 2016-04-02.
 */
public abstract class BaseActionBarFragment
        extends BaseFragment
        implements BaseActionBar.OnActionBarListener {
    private static final String TAG = makeLogTag(BaseActionBarFragment.class);
    private ActionBarFrameLayout mActionBarFragmentLayout;
    private Activity mActivity;

    public void onDestroy() {
        super.onDestroy();
    }

    protected void onKeyboardHide() {
    }

    protected void onKeyboardShow() {
    }

    public void onLeftButtonClicked() {

        if ((this.mActivity instanceof BaseActivity)) {
            if (!((BaseActivity) this.mActivity).isNavDrawerOpen()) {
                ((BaseActivity) this.mActivity).openNavDrawer();
            }
            return;
        }
        getActivity().finish();
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    public void onRightButtonClicked() {}

    public void onTitleTextClicked() {}

    protected void registerView(View view) {
        if ((view instanceof ActionBarFrameLayout)) {
            this.mActionBarFragmentLayout = ((ActionBarFrameLayout) view);
            this.mActionBarFragmentLayout.setOnActionBarListener(this);
        }
        this.mActivity = getActivity();
    }

    public int getActionBarHeight(){
        if (this.mActionBarFragmentLayout != null) {
            return this.mActionBarFragmentLayout.getActionBarHeight();
        }
        return 0;
    }

    public void setActionBarBackgroundResource(int resource){
        if(this.mActionBarFragmentLayout != null) {
            this.mActionBarFragmentLayout.setActionBarBackgroundResource(resource);
        }
    }

    public void setActionBarColor(int paramInt) {
        if (this.mActionBarFragmentLayout != null) {
            this.mActionBarFragmentLayout.setActionBarColor(paramInt);
        }
    }

    public void setActionBarLeftButtonContentDescription(int paramInt) {
        if (this.mActionBarFragmentLayout != null) {
            this.mActionBarFragmentLayout.setLeftButtonContentDescription(paramInt);
        }
    }

    public void setActionBarLeftButtonUI(int paramInt) {
        if (this.mActionBarFragmentLayout != null) {
            this.mActionBarFragmentLayout.setLeftButton(paramInt);
        }
    }

    public void setActionBarRightButtonContentDescription(int paramInt) {
        if (this.mActionBarFragmentLayout != null) {
            this.mActionBarFragmentLayout.setRightButtonContentDescription(paramInt);
        }
    }

    public void setActionBarRightButtonUI(int paramInt) {
        if (this.mActionBarFragmentLayout != null) {
            this.mActionBarFragmentLayout.setRightButton(paramInt);
        }
    }

    public void setActionBarTitle(String paramString) {
        this.mActionBarFragmentLayout.setActionBarTitle(paramString);
    }

    public void setActionBarTitleCount(int paramInt) {
        this.mActionBarFragmentLayout.setActionBarTitleCount(paramInt);
    }

    public void setActionBarVisibility(boolean paramBoolean) {
        if (this.mActionBarFragmentLayout != null) {
            this.mActionBarFragmentLayout.setActionBarVisibility(paramBoolean);
        }
    }

    public void setHasNewMessage(boolean paramBoolean) {
        if (this.mActionBarFragmentLayout != null) {
            this.mActionBarFragmentLayout.setHasNewMessage(paramBoolean);
        }
    }

}
