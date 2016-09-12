package com.grotesque.saa.common.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.grotesque.saa.R;
import com.grotesque.saa.activity.SignInActivity;
import com.grotesque.saa.util.AccountUtils;
import com.grotesque.saa.util.FontManager;
import com.grotesque.saa.util.SoftInputUtils;

/**
 * Created by 경환 on 2016-04-07.
 */
public class CommentBar extends LinearLayout implements View.OnClickListener, View.OnTouchListener, TextWatcher {

    public static final int SUBMIT = 0;
    public static final int EDIT = 1;
    public static final int REPLY = 2;


    private int mType;

    private Context mContext;

    private RelativeLayout mCommentBarLayout;
    private EditText mCommentBarEditText;
    private Button mCommentBarButton;
    private OnCommentBarListener onCommentBarListener;

    public CommentBar(Context context) {
        super(context);
        mContext = context;
        registerView();
    }

    public CommentBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        registerView();
    }

    public CommentBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        registerView();
    }
    private void registerView(){
        LayoutInflater.from(mContext).inflate(R.layout.layout_comment_bar, this, true);
        mCommentBarLayout = (RelativeLayout) findViewById(R.id.ll_comment_bar);
        mCommentBarEditText = (EditText) findViewById(R.id.etxt_comment_content);
        mCommentBarEditText.addTextChangedListener(this);
        mCommentBarEditText.setTypeface(FontManager.getInstance(mContext).getTypeface());
        mCommentBarButton = (Button) findViewById(R.id.btn_submit);
        mCommentBarButton.setOnClickListener(this);
        mCommentBarEditText.setOnTouchListener(this);
    }
    public void clearBar(){
        mType = SUBMIT;
        mCommentBarEditText.setText("");
        mCommentBarEditText.setHint("댓글을 입력하세요");
        mCommentBarEditText.clearFocus();
        mCommentBarButton.setText("입력");

        SoftInputUtils.hide(mCommentBarEditText);
    }

    public int getCommentType(){
        return mType;
    }
    public void setHint(String text) { mCommentBarEditText.setHint(text);}
    public void setText(String text){
        mCommentBarEditText.setText(text);
    }

    public void setRightButtonText(int type){
        switch (type){
            case 0:
                mCommentBarButton.setText("입력");
                mType = SUBMIT;
                return;
            case 1:
                mCommentBarButton.setText("수정");
                mType = EDIT;
                return;
            case 2:
                mCommentBarButton.setText("답변");
                mType = REPLY;
                return;
        }


    }
    public void setCommentBarColor(ColorStateList color){
        mCommentBarEditText.setTextColor(color);
        mCommentBarEditText.setHintTextColor(color);
        mCommentBarButton.setTextColor(color);
    }
    public void setOnCommentBarListener(OnCommentBarListener listener){
        onCommentBarListener = listener;
    }

    @Override
    public void onClick(View v) {
        onCommentBarListener.onCommentButtonClick(mType, mCommentBarEditText.getText().toString().replace("\n","<br></br>"));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(!AccountUtils.getLoginState(mContext))
            mContext.startActivity(new Intent(mContext, SignInActivity.class));
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.toString().isEmpty()) {
            mCommentBarButton.setEnabled(false);
        }else{
            mCommentBarButton.setEnabled(true);
        }
    }

    public interface OnCommentBarListener{
        void onCommentButtonClick(int type, String content);
    }


}
