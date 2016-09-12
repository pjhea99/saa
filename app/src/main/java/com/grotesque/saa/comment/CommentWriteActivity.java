package com.grotesque.saa.comment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.grotesque.saa.R;
import com.grotesque.saa.common.actionbar.BaseActionBar;
import com.grotesque.saa.util.FontManager;

/**
 * Created by KH on 2016-08-02.
 */
public class CommentWriteActivity extends AppCompatActivity implements BaseActionBar.OnActionBarListener {

    private BaseActionBar mActionBar;
    private EditText mEditText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_write);

        Bundle args = getIntent().getExtras();

        mActionBar = (BaseActionBar) findViewById(R.id.actionBar);
        mActionBar.setOnActionBarListener(this);
        mEditText = (EditText) findViewById(R.id.edittext_comment);
        mEditText.requestFocus();
        mEditText.setTypeface(FontManager.getInstance(this).getTypeface());
        if(args != null) {
            mEditText.setText(args.getString("comment").replace("<br />", "\n"));
            mEditText.setSelection(mEditText.getText().length());
        }
    }

    @Override
    public void onTitleTextClicked() {

    }

    @Override
    public void onLeftButtonClicked() {
        finish();
    }

    @Override
    public void onRightButtonClicked() {
        Intent intent = getIntent();
        intent.putExtra("comment", mEditText.getText().toString().replace("\n", "<br />"));
        setResult(RESULT_OK, intent);
        finish();
    }
}
