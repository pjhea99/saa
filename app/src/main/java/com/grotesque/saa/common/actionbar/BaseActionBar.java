package com.grotesque.saa.common.actionbar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grotesque.saa.R;
import com.grotesque.saa.model.ActionBarType;
import com.grotesque.saa.util.DensityScaleUtil;
import com.grotesque.saa.util.FontManager;

import java.util.Locale;

import static com.grotesque.saa.util.LogUtils.makeLogTag;


public class BaseActionBar extends LinearLayout implements View.OnClickListener{

    private static final String TAG = makeLogTag(BaseActionBar.class);

    private OnActionBarListener mOnActionBarListener;
    private OnSelectedTitleTab H;
    private String a;
    private Context mContext = null;

    private LinearLayout mCommonModeLayout;

    private ImageButton mLeftBtn;
    private ImageButton mRightBtn;

    private ImageView mTitleImgView;
    private TextView mTitleView;
    private TextView mTitleCountView;
    private ImageView mMenuNewIcon;

    private boolean mSearchEnable;
    private String mActionBarTitle;
    private int mLeftIcon;
    private int mRightIcon;
    private int mActionBarBackground;
    private int mTitleColor;
    private int mTitleImage;
    private String mLeftBtnDescription;
    private String mRightBtnDescription;


    public BaseActionBar(Context context) {
        super(context);
        this.mContext = context;
        initUI();
        b();
    }

    public BaseActionBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initUI();
        initUI(context, attrs);
        b();
    }

    private void initUI() {
        LayoutInflater.from(mContext).inflate(R.layout.layout_common_action_bar, this, true);
        mCommonModeLayout = (LinearLayout)findViewById(R.id.common_mode);
        mTitleView = (TextView)findViewById(R.id.title);
        mTitleView.setTypeface(FontManager.getInstance(mContext).getTypeface());
        mTitleView.setOnClickListener(this);
        mTitleCountView = (TextView)findViewById(R.id.titleCount);


        mLeftBtn = (ImageButton)findViewById(R.id.leftButton);
        mLeftBtn.setOnClickListener(this);

        mRightBtn = (ImageButton)findViewById(R.id.rightButton);
        mRightBtn.setOnClickListener(this);


        mTitleImgView = (ImageView)findViewById(R.id.titleImageView);
        mMenuNewIcon = (ImageView)findViewById(R.id.menu_new_icon);
    }

    private void initUI(Context context, AttributeSet attributeset) {
        TypedArray styledAttributes = context.obtainStyledAttributes(attributeset, R.styleable.BaseActionBar);

        mTitleImage = styledAttributes.getResourceId(R.styleable.BaseActionBar_titleImage, 0);

        mSearchEnable = styledAttributes.getBoolean(R.styleable.BaseActionBar_searchEnable, false);

        mActionBarTitle = styledAttributes.getString(R.styleable.BaseActionBar_actionbarTitle);

        mTitleColor = styledAttributes.getInt(R.styleable.BaseActionBar_titleColor, 0);


        mRightIcon = styledAttributes.getResourceId(R.styleable.BaseActionBar_rightIcon, 0);

        mLeftIcon = styledAttributes.getResourceId(R.styleable.BaseActionBar_leftIcon, 0);

        mActionBarBackground = styledAttributes.getResourceId(R.styleable.BaseActionBar_actionbarBackground, 0);


        mLeftBtnDescription = styledAttributes.getString(R.styleable.BaseActionBar_leftButtonDescription);
        mRightBtnDescription = styledAttributes.getString(R.styleable.BaseActionBar_rightButtonDescription);
    }

    static OnActionBarListener b(BaseActionBar brunchactionbar) {
        return brunchactionbar.mOnActionBarListener;
    }

    private void b() {

        if(mTitleImage > 0){
            mTitleImgView.setImageResource(mTitleImage);
            mTitleView.setVisibility(GONE);
        }else {
            mTitleImgView.setVisibility(GONE);
            if(mActionBarTitle != null)
                mTitleView.setText(mActionBarTitle);
        }

        if(mTitleColor != 0)
            mTitleView.setTextColor(mTitleColor);

        if (mRightIcon > 0) {
            mRightBtn.setImageResource(mRightIcon);
            mRightBtn.setVisibility(VISIBLE);
            if (mRightBtnDescription != null)
                mRightBtn.setContentDescription(mRightBtnDescription);
        } else {
            mRightBtn.setVisibility(INVISIBLE);
        }

        if (mLeftIcon > 0) {
            mLeftBtn.setImageResource(mLeftIcon);
            mLeftBtn.setVisibility(VISIBLE);
            if (mLeftBtnDescription != null)
                mLeftBtn.setContentDescription(mLeftBtnDescription);
        } else {
            mLeftBtn.setVisibility(INVISIBLE);
        }

        if (mActionBarBackground > 0) setBackgroundResource(mActionBarBackground);
        else setBackgroundColor(getResources().getColor(R.color.brunchTransparent));

    }


    static boolean c(BaseActionBar brunchactionbar)
    {
        return brunchactionbar.mSearchEnable;
    }

    public void setActionBarBackgroundResource(int resource){
        setBackgroundResource(resource);
    }

    public void setActionBarColor(int color) {
        mRightBtn.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        mLeftBtn.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        mTitleImgView.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        mTitleView.setTextColor(color);
    }

    public void setDiscoverVisibility(boolean flag)
    {
        if(flag)
        {
            if(getVisibility() == GONE) {
                ObjectAnimator objectanimator = ObjectAnimator.ofFloat(this, View.ALPHA, 0.0F, 1.0F);
                objectanimator.setInterpolator(new LinearInterpolator());
                objectanimator.setDuration(300L);
                objectanimator.start();
                setVisibility(VISIBLE);
            }
            /*
            mRightBtn.setVisibility(VISIBLE);
            mLeftBtn.setVisibility(VISIBLE);
            mCommonModeLayout.setVisibility(VISIBLE);
            */
            return;
        } else {
            setVisibility(GONE);
            /*
            mRightBtn.setVisibility(INVISIBLE);
            mLeftBtn.setVisibility(INVISIBLE);
            mCommonModeLayout.setVisibility(INVISIBLE);
            */
            return;
        }
    }

    public void setHasNewMessage(boolean flag)
    {
        setSelected(flag);
        if(flag)
        {
            mMenuNewIcon.setVisibility(VISIBLE);
            return;
        } else
        {
            mMenuNewIcon.setVisibility(GONE);
            return;
        }
    }

    public void setLeftButton(int drawable) {
        mLeftIcon = drawable;
        if(mLeftIcon > 0) {
            mLeftBtn.setImageResource(mLeftIcon);
            mLeftBtn.setVisibility(VISIBLE);
        }else
            mLeftBtn.setVisibility(INVISIBLE);

    }

    public void setLeftButtonContentDescription(int i1)
    {
        mLeftBtn.setContentDescription(mContext.getString(i1));
    }

    public void setOnActionBarListener(OnActionBarListener onactionbarlistener)
    {
        mOnActionBarListener = onactionbarlistener;
    }

    public void setOnSelectedTitleTab(OnSelectedTitleTab onselectedtitletab)
    {
        H = onselectedtitletab;
    }

    public void setRightButton(int drawable) {
        mRightIcon = drawable;
        if(mRightIcon > 0) {
            mRightBtn.setImageResource(mRightIcon);
            mRightBtn.setVisibility(VISIBLE);
        }else
            mRightBtn.setVisibility(INVISIBLE);

    }

    public void setRightButtonContentDescription(int i1)
    {
        mRightBtn.setContentDescription(mContext.getString(i1));
    }

    public void setTitle(String string) {
        mActionBarTitle = string;
        mTitleView.setText(mActionBarTitle);
        mTitleImgView.setVisibility(GONE);
    }
    public void setTitleColor(int color) {
        mTitleColor = color;
        mTitleView.setTextColor(mTitleColor);
    }
    public void setTitleCount(int count) {
        if(count < 1) {
            mTitleCountView.setText("");
            mTitleCountView.setVisibility(GONE);
            return;
        }
        mTitleCountView.setVisibility(VISIBLE);

        if(count > 999) {
            mTitleCountView.setText(String.format(Locale.KOREA, "%s+", "999"));
        } else  {
            mTitleCountView.setText(String.valueOf(count));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title:
                mOnActionBarListener.onTitleTextClicked();
                break;
            case R.id.leftButton:
                mOnActionBarListener.onLeftButtonClicked();
                break;
            case R.id.rightButton:
                mOnActionBarListener.onRightButtonClicked();
                break;
        }

    }

    public enum TitleTap
    {
        LEFT ("LEFT", 0),
        RIGHT ("RIGHT", 1);

        private String direction;
        private int num;

        TitleTap(String direction, int num) {
            this.direction = direction;
            this.num = num;
        }
        public String getDirection(){
            return this.direction;
        }
    }

    public interface OnActionBarListener
    {
        void onTitleTextClicked();

        void onLeftButtonClicked();

        void onRightButtonClicked();
    }

    public interface OnSelectedTitleTab {
        void onSelectedTab(TitleTap titletap);
    }

}
