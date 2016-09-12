package com.grotesque.saa.post.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.grotesque.saa.R;
import com.grotesque.saa.common.Drawables;
import com.grotesque.saa.post.data.PostData;
import com.grotesque.saa.post.helper.ItemTouchHelperAdapter;
import com.grotesque.saa.post.helper.ItemTouchHelperViewHolder;
import com.grotesque.saa.post.helper.OnPostAdapterListener;
import com.grotesque.saa.util.FontManager;
import com.grotesque.saa.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.grotesque.saa.util.LogUtils.makeLogTag;

public class RecyclerListAdapter extends RecyclerView.Adapter
        implements ItemTouchHelperAdapter {
    private final String TAG = makeLogTag(RecyclerListAdapter.class);
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_TEXT = 1;
    private static final int TYPE_IMAGE = 2;
    private static final int TYPE_VIDEO = 3;

    private static Context mContext;
    private List<PostData> mItems = new ArrayList<>();
    private OnPostAdapterListener mDragStartListener;

    public RecyclerListAdapter(Context context, List<PostData> postDatas, OnPostAdapterListener dragStartListener) {
        mContext = context;
        mDragStartListener = dragStartListener;
        mItems = postDatas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_TITLE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_brunchedit_title, parent, false);
            return new ItemTitleHolder(view);
        }else if(viewType == TYPE_TEXT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_brunchedit_text, parent, false);
            return new ItemTextHolder(view);
        }else if(viewType == TYPE_IMAGE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_brunchedit_image, parent, false);
            return new ItemImageHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_link_youtube, parent, false);
        return new ItemVideoHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ItemTitleHolder) {
            ItemTitleHolder th = (ItemTitleHolder) holder;
            th.textView.requestFocus();
            th.textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    mDragStartListener.onFocusChanged(v, hasFocus);
                }
            });
            th.textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_NEXT){
                        mItems.add(new PostData("txt", ""));
                        notifyDataSetChanged();
                    }
                    return false;
                }
            });
        }else if(holder instanceof ItemTextHolder){
            final ItemTextHolder textHolder = (ItemTextHolder) holder;
            textHolder.textView.requestFocus();
            textHolder.textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    mDragStartListener.onFocusChanged(v, hasFocus);
                }
            });
            textHolder.textView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mItems.get(position).setHtml(s.toString().replace("\n", "</br>"));
                }
            });
            textHolder.textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mDragStartListener.onStartDrag(holder);
                    return false;
                }
            });
        }else if(holder instanceof ItemImageHolder){
            ItemImageHolder ih = (ItemImageHolder) holder;

            Uri uri = Uri.parse(mItems.get(position).getHtml());
            ImageRequestBuilder imageRequestBuilder =
                    ImageRequestBuilder.newBuilderWithSource(uri);
            if (UriUtil.isNetworkUri(uri)) {
                imageRequestBuilder.setProgressiveRenderingEnabled(true);
            }


            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequestBuilder.build())
                    .setOldController(ih.imageView.getController())
                    .setControllerListener(ih.mControllerListener)
                    .setAutoPlayAnimations(true)
                    .setTapToRetryEnabled(true)
                    .build();
            ih.imageView.setController(draweeController);


            ih.imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });
        }else if(holder instanceof ItemVideoHolder){
            ItemVideoHolder vh = (ItemVideoHolder) holder;

            Uri uri = Uri.parse(mItems.get(position).getThumbnail());
            ImageRequestBuilder imageRequestBuilder =
                    ImageRequestBuilder.newBuilderWithSource(uri);
            if (UriUtil.isNetworkUri(uri)) {
                imageRequestBuilder.setProgressiveRenderingEnabled(true);
            }


            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequestBuilder.build())
                    .setOldController(vh.mThumbnailView.getController())
                    .setAutoPlayAnimations(true)
                    .setTapToRetryEnabled(true)
                    .build();
            vh.mThumbnailView.setController(draweeController);

            vh.mContentView.setText(mItems.get(position).getContent());
            vh.mTitleView.setText(mItems.get(position).getTitle());
            vh.mLinkView.setText("https://www.youtube.com/watch?v=" + mItems.get(position).getHtml());
            mItems.get(position).setHtml(StringUtils.getYoutubeIframe(mItems.get(position).getHtml()));
        }

    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (mItems.get(position).getType()){
            case "title":
                return TYPE_TITLE;
            case "txt":
                return TYPE_TEXT;
            case "img":
                return TYPE_IMAGE;
            case "video":
                return TYPE_VIDEO;
        }
        return super.getItemViewType(position);
    }
    public static class ItemTitleHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final EditText textView;

        public ItemTitleHolder(View itemView) {
            super(itemView);
            textView = (EditText) itemView.findViewById(R.id.edittext_title);
            textView.setTypeface(FontManager.getInstance(mContext).getTypeface());
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
    public static class ItemTextHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final EditText textView;

        public ItemTextHolder(View itemView) {
            super(itemView);
            textView = (EditText) itemView.findViewById(R.id.edittext_text);
            textView.setTypeface(FontManager.getInstance(mContext).getTypeface());
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public static class ItemImageHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView imageCaptionTextView;
        public final SimpleDraweeView imageView;
        //public final EditText imageCaptionView;

        public ItemImageHolder(View itemView) {
            super(itemView);
            imageCaptionTextView = (TextView) itemView.findViewById(R.id.txt_photo_caption);
            imageView = (SimpleDraweeView) itemView.findViewById(R.id.img_editor_photo);

            ProgressBarDrawable progressBarDrawable = new ProgressBarDrawable();
            progressBarDrawable.setPadding(0);
            progressBarDrawable.setColor(R.color.brunch_mint);

            GenericDraweeHierarchy gdh = new GenericDraweeHierarchyBuilder(mContext.getResources())
                    .setFailureImage(Drawables.sErrorDrawable)
                    .setProgressBarImage(progressBarDrawable)
                    .setPlaceholderImage(Drawables.sPlaceholderDrawable, ScalingUtils.ScaleType.CENTER_CROP)
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                    .build();
            imageView.setHierarchy(gdh);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

        private ControllerListener mControllerListener = new ControllerListener<ImageInfo>() {
            @Override
            public void onSubmit(String id, Object callerContext) {

            }

            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                if (imageInfo != null) {
                    imageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    imageView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    imageView.setAspectRatio((float) imageInfo.getWidth() / imageInfo.getHeight());
                }
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                if (imageInfo != null) {
                    imageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    imageView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    imageView.setAspectRatio((float) imageInfo.getWidth() / imageInfo.getHeight());
                }
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {

            }

            @Override
            public void onFailure(String id, Throwable throwable) {
            }

            @Override
            public void onRelease(String id) {

            }
        };
    }

    public static class ItemVideoHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView mLinkView;
        public final TextView mTitleView;
        public final TextView mContentView;
        public final SimpleDraweeView mThumbnailView;
        public final ImageView mPlayImageView;

        public ItemVideoHolder(View itemView) {
            super(itemView);
            mLinkView = (TextView) itemView.findViewById(R.id.link_url);
            mTitleView = (TextView) itemView.findViewById(R.id.link_title);
            mContentView = (TextView) itemView.findViewById(R.id.link_content);
            mThumbnailView = (SimpleDraweeView) itemView.findViewById(R.id.link_thumbnail_imageview);
            mPlayImageView = (ImageView) itemView.findViewById(R.id.play_image_view);

            mLinkView.setTypeface(FontManager.getInstance(mContext).getTypeface());
            mTitleView.setTypeface(FontManager.getInstance(mContext).getTypeface());
            mContentView.setTypeface(FontManager.getInstance(mContext).getTypeface());
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}