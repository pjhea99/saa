package com.grotesque.saa.search.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grotesque.saa.R;
import com.grotesque.saa.common.api.RetrofitApi;
import com.grotesque.saa.search.data.SearchItem;
import com.grotesque.saa.search.data.SearchItemContainer;
import com.grotesque.saa.util.FontManager;
import com.grotesque.saa.util.NavigationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.grotesque.saa.util.LogUtils.LOGE;
import static com.grotesque.saa.util.LogUtils.makeLogTag;


public class SearchItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AbsListView.RecyclerListener{
    private static final String TAG = makeLogTag(SearchItemAdapter.class);

    private static final int FOOTER_VIEWHOLDER = 0;
    private static final int ITEM_VIEWHOLDER = 1;
    private static final int HEADER_VIEWHOLDER = 2;

    private Context mContext;
    private ArrayList<SearchItem> mArrayItems = new ArrayList<>();
    private String mid;

    public SearchItemAdapter(String mid, ArrayList<SearchItem> arrayItems, Context context) {
        this.mid = mid;
        this.mArrayItems = arrayItems;
        this.mContext = context;
    }

    @Override
    public void onMovedToScrapHeap(View view) {

    }

    public class ViewFooterHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayoutFooter;
        ImageView imageLoading;
        public ViewFooterHolder(View itemView) {
            super(itemView);
            relativeLayoutFooter = (RelativeLayout) itemView.findViewById(R.id.rl_footer);
            imageLoading = (ImageView) itemView.findViewById(R.id.image_loading);
            imageLoading.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.rotate_loading));
        }
    }

    public class ViewItemHolder extends RecyclerView.ViewHolder {
        TextView mByView;
        TextView mCountView;
        TextView mTimeView;
        TextView mTitleView;
        TextView mUserView;
        TextView mCommentView;

        public ViewItemHolder(View itemView) {
            super(itemView);

            mByView = (TextView) itemView.findViewById(R.id.byView);
            mByView.setTypeface(FontManager.getInstance(mContext).getTypefaceItalic());
            mCountView = (TextView) itemView.findViewById(R.id.countView);
            mCountView.setTypeface(FontManager.getInstance(mContext).getTypefaceItalic());
            mTitleView = (TextView) itemView.findViewById(R.id.titleView);
            mTitleView.setTypeface(FontManager.getInstance(mContext).getTypeface());
            mUserView = (TextView) itemView.findViewById(R.id.userView);
            mUserView.setTypeface(FontManager.getInstance(mContext).getTypeface());
            mTimeView = (TextView) itemView.findViewById(R.id.timeView);
            mTimeView.setTypeface(FontManager.getInstance(mContext).getTypeface());
            mCommentView = (TextView) itemView.findViewById(R.id.commentCountView);
            mCommentView.setTypeface(FontManager.getInstance(mContext).getTypeface());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> query = new HashMap<>();
                    LOGE(TAG, mid);
                    LOGE(TAG, mArrayItems.get(getAdapterPosition()).getDocument_srl());
                    query.put("act", "dispBoardContentView");
                    query.put("mid", mid);
                    query.put("document_srl", mArrayItems.get(getAdapterPosition()).getDocument_srl());
                    RetrofitApi.getInstance().getBoardView(query).enqueue(new Callback<SearchItemContainer>() {
                        @Override
                        public void onResponse(Call<SearchItemContainer> call, Response<SearchItemContainer> response) {
                            NavigationUtils.goContentActivity(mContext, mid, response.body().getDocumentList());
                        }

                        @Override
                        public void onFailure(Call<SearchItemContainer> call, Throwable t) {
                            LOGE(TAG, String.valueOf(t));
                        }
                    });
                }
            });
        }
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == FOOTER_VIEWHOLDER){
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_footer_loading, viewGroup, false);
            return new ViewFooterHolder(v);
        }else if(viewType == HEADER_VIEWHOLDER){
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.layout_footer_loading, viewGroup, false);
            return new ViewFooterHolder(v);
        }
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listrow_search_item, viewGroup, false);
        return new ViewItemHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ViewFooterHolder){
            ViewFooterHolder vh = (ViewFooterHolder) holder;

        }else{
            final ViewItemHolder vh = (ViewItemHolder) holder;
            vh.mCountView.setText(String.format(Locale.US, "%s hit", mArrayItems.get(position).getHit()));
            String title = mArrayItems.get(position).getTitle();
            title = title.substring(0, title.length() - mArrayItems.get(position).getReply().length());
            vh.mTitleView.setText(Html.fromHtml(title));
            vh.mTimeView.setText(mArrayItems.get(position).getDate());
            vh.mUserView.setText(mArrayItems.get(position).getAuthor());
            vh.mCommentView.setText(String.format(Locale.US, "+ %s", mArrayItems.get(position).getReply().equals("") ? 0 : mArrayItems.get(position).getReply()));

        }
    }

    @Override
    public int getItemViewType(int position) {
        return mArrayItems.get(position) == null ? FOOTER_VIEWHOLDER : ITEM_VIEWHOLDER;
    }

    @Override
    public int getItemCount() {
        return mArrayItems.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

}