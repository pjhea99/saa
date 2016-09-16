package com.grotesque.saa.board.holder;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grotesque.saa.R;
import com.grotesque.saa.common.holder.BaseViewHolder;
import com.grotesque.saa.home.data.DocumentList;
import com.grotesque.saa.util.FontManager;
import com.grotesque.saa.util.NavigationUtils;
import com.grotesque.saa.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by KH on 2016-07-27.
 */
public class BoardNoticeHolder extends BaseViewHolder<DocumentList>  {

    private TextView mByView;
    private TextView mCommentCountView;
    private TextView mTimeView;
    private TextView mTitleView;
    private TextView mUserView;

    public BoardNoticeHolder(Context context, View itemView) {
        super(context, itemView);

        mByView = (TextView) itemView.findViewById(R.id.byView);
        mByView.setTypeface(FontManager.getInstance(context).getTypefaceItalic());
        mCommentCountView = (TextView) itemView.findViewById(R.id.commentCountView);
        mCommentCountView.setTypeface(FontManager.getInstance(context).getTypeface());
        mTitleView = (TextView) itemView.findViewById(R.id.titleView);
        mTitleView.setTypeface(FontManager.getInstance(context).getTypeface());
        mTimeView = (TextView) itemView.findViewById(R.id.timeView);
        mTimeView.setTypeface(FontManager.getInstance(context).getTypeface());
        mUserView = (TextView) itemView.findViewById(R.id.userView);
        mUserView.setTypeface(FontManager.getInstance(context).getTypeface());
    }

    public static BoardNoticeHolder newInstance(Context context, ViewGroup parent) {
        return new BoardNoticeHolder(context, LayoutInflater.from(context).inflate(R.layout.listrow_board_notice, parent, false));
    }

    @Override
    public void onBindView(DocumentList documentList) {

    }

    @Override
    public void onBindView(List<DocumentList> item, int position) {

    }

    @Override
    public void onBindView(final List<DocumentList> item, final int position, final String mid) {
        mCommentCountView.setText(String.format(Locale.US, "+ %s", item.get(position).getCommentCount()));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.goContentActivity(getContext(), mid, (ArrayList<DocumentList>) item, position-1);
            }
        });
        String title = item.get(position).getTitle();
        mTitleView.setText(Html.fromHtml(title));
        mTimeView.setText(StringUtils.convertDate(item.get(position).getRegdate()));
        mUserView.setText(item.get(position).getNickName());
    }
}
