package com.grotesque.saa.board.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.grotesque.saa.R;
import com.grotesque.saa.common.Drawables;
import com.grotesque.saa.common.widget.CustomProgressbarDrawable;
import com.grotesque.saa.home.data.DocumentList;
import com.grotesque.saa.util.FontManager;
import com.grotesque.saa.util.NavigationUtils;
import com.grotesque.saa.util.ParseUtils;
import com.grotesque.saa.util.StringUtils;

import java.util.ArrayList;

import static com.grotesque.saa.util.LogUtils.makeLogTag;


/**
 * Provide views to RecyclerView with data from arrayItems.
 */
public class StaggeredBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AbsListView.RecyclerListener{
    private static final String TAG = makeLogTag(LinearBoardAdapter.class);

    private static final int FOOTER_VIEWHOLDER = 0;
    private static final int ITEM_VIEWHOLDER = 1;

    private Context context;
    private ArrayList<DocumentList> arrayItems = new ArrayList<>();
    private String mid;

    public StaggeredBoardAdapter(String mid, ArrayList<DocumentList> arrayItems, Context context) {
        this.mid = mid;
        this.arrayItems = arrayItems;
        this.context = context;
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
            imageLoading.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_loading));
        }
    }

    public class ViewItemHolder extends RecyclerView.ViewHolder {
        TextView byView;
        TextView titleView;
        TextView userView;
        TextView commentCountView;
        SimpleDraweeView imageView;

        public ViewItemHolder(View v) {
            super(v);

            imageView = (SimpleDraweeView) v.findViewById(R.id.imageView);
            GenericDraweeHierarchy gdh = new GenericDraweeHierarchyBuilder(context.getResources())
                    .setProgressBarImage(new CustomProgressbarDrawable())
                    .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                    .build();
            imageView.setHierarchy(gdh);

            byView = (TextView) v.findViewById(R.id.byView);
            byView.setTypeface(FontManager.getInstance(context).getTypefaceItalic());

            titleView = (TextView) v.findViewById(R.id.titleView);
            titleView.setTypeface(FontManager.getInstance(context).getTypeface());

            userView = (TextView) v.findViewById(R.id.userView);
            userView.setTypeface(FontManager.getInstance(context).getTypeface());

            commentCountView = (TextView) v.findViewById(R.id.commentCountView);
            commentCountView.setTypeface(FontManager.getInstance(context).getTypeface());
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
        }
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listrow_staggerd_board, viewGroup, false);
        return new ViewItemHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ViewFooterHolder){
            ViewFooterHolder vh = (ViewFooterHolder) holder;

        }else{
            final ViewItemHolder vh = (ViewItemHolder) holder;

            Glide.clear(vh.imageView);
            vh.titleView.setText(Html.fromHtml(arrayItems.get(position).getTitle()));
            vh.userView.setText(arrayItems.get(position).getNickName());
            vh.commentCountView.setText("+"+arrayItems.get(position).getCommentCount());
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavigationUtils.goContentActivity(context, mid, arrayItems, position);
                }
            });
            vh.imageView.setVisibility(View.VISIBLE);
            if(arrayItems.get(position).hasImg()){
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(ParseUtils.parseImgUrl(arrayItems.get(position).getContent())))
                        .setResizeOptions(new ResizeOptions(150, 180))
                        .build();
                DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(vh.imageView.getController())
                        .build();
                vh.imageView.setController(draweeController);
                vh.commentCountView.setTextColor(Color.parseColor("#FFFFFF"));
            }else{
                if(arrayItems.get(position).hasYoutube()){
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("http://img.youtube.com/vi/"+StringUtils.getYoutubeId(arrayItems.get(position).getContent()) + "/0.jpg"))
                            .setResizeOptions(new ResizeOptions(150, 180))
                            .build();
                    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setOldController(vh.imageView.getController())
                            .build();
                    vh.imageView.setController(draweeController);
                    vh.commentCountView.setTextColor(Color.parseColor("#FFFFFF"));
                }else {
                    vh.imageView.setVisibility(View.INVISIBLE);
                    vh.commentCountView.setTextColor(Color.parseColor("#ff00c3bd"));
                }

            }
        }
    }
    public boolean isFirstView(int position){
        return position % 2 == 0;
    }
    @Override
    public int getItemViewType(int position) {
        return arrayItems.get(position) == null ? FOOTER_VIEWHOLDER : ITEM_VIEWHOLDER;
    }

    @Override
    public int getItemCount() {
        return arrayItems.size();
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


