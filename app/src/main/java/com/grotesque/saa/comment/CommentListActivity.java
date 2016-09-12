package com.grotesque.saa.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.grotesque.saa.R;
import com.grotesque.saa.home.data.DocumentList;

/**
 * Created by KH on 2016-07-30.
 */
public class CommentListActivity extends AppCompatActivity{
    private String mid;
    private DocumentList mDocuList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Bundle args = getIntent().getExtras();
        if(args != null){
            mid = args.getString("mid");
            mDocuList = args.getParcelable("array");
        }

        getFragmentManager().beginTransaction().add(R.id.commentFragment, CommentListFragment.newInstance(mid, mDocuList)).commit();

    }
}
