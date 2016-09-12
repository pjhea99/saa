package com.grotesque.saa.common.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.grotesque.saa.R;
import com.grotesque.saa.common.actionbar.BaseActionBar;

public class ActionBarFrameLayout extends FrameLayout {
    private final String TAG = ActionBarFrameLayout.class.getSimpleName();
    private BaseActionBar mActionBar;

    public ActionBarFrameLayout(Context paramContext)
    {
      super(paramContext);
    }

    public ActionBarFrameLayout(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public ActionBarFrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
       super(paramContext, paramAttributeSet, paramInt);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBar = ((BaseActionBar)findViewById(R.id.actionBar));
    }
    public int getActionBarHeight() {
        if (this.mActionBar != null) {
            return this.mActionBar.getHeight();
        }
        return 0;
    }

    public void setActionBarBackgroundResource(int resource) {
        if (this.mActionBar != null) {
            this.mActionBar.setActionBarBackgroundResource(resource);
        }
    }
  
    public void setActionBarColor(int paramInt) {
        if (this.mActionBar != null) {
            this.mActionBar.setActionBarColor(paramInt);
        }
    }

    protected void setActionBarTitle(String paramString) {
        if (this.mActionBar != null) {
            this.mActionBar.setTitle(paramString);
        }
    }

    protected void setActionBarTitleCount(int paramInt) {
        if (this.mActionBar != null) {
            this.mActionBar.setTitleCount(paramInt);
        }
    }

    public void setActionBarVisibility(boolean paramBoolean) {
        if (this.mActionBar != null) {
            this.mActionBar.setDiscoverVisibility(paramBoolean);
        }
    }

    public void setHasNewMessage(boolean paramBoolean) {
        if (this.mActionBar != null) {
            this.mActionBar.setHasNewMessage(paramBoolean);
        }
    }

    public void setLeftButton(int paramInt) {
        if (this.mActionBar != null) {
            this.mActionBar.setLeftButton(paramInt);
        }
    }

    public void setLeftButtonContentDescription(int paramInt) {
        if (this.mActionBar != null) {
            this.mActionBar.setLeftButtonContentDescription(paramInt);
        }
    }

    protected void setOnActionBarListener(BaseActionBar.OnActionBarListener paramOnActionBarListener) {
        if (this.mActionBar != null) {
            this.mActionBar.setOnActionBarListener(paramOnActionBarListener);
        }
    }

    protected void setOnSelectedTitleTab(BaseActionBar.OnSelectedTitleTab paramOnSelectedTitleTab) {
        if (this.mActionBar != null) {
            this.mActionBar.setOnSelectedTitleTab(paramOnSelectedTitleTab);
        }
    }

    public void setRightButton(int paramInt) {
        if (this.mActionBar != null) {
            this.mActionBar.setRightButton(paramInt);
        }
    }

    public void setRightButtonContentDescription(int paramInt) {
        if (this.mActionBar != null) {
           this.mActionBar.setRightButtonContentDescription(paramInt);
        }
    }
}
