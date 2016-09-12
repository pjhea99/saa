package com.grotesque.saa.home.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.grotesque.saa.R;
import com.grotesque.saa.common.Drawables;
import com.grotesque.saa.common.widget.CustomProgressbarDrawable;
import com.grotesque.saa.home.data.PointRankList;
import com.grotesque.saa.util.FontManager;

import java.util.ArrayList;

/**
 * Created by KH on 2016-09-02.
 */
public class PointRankAdapter extends RecyclerView.Adapter<PointRankAdapter.ViewPointRankHolder> {
    private Context mContext;
    private ArrayList<PointRankList> mArrayList = new ArrayList<>();

    public PointRankAdapter(Context context) {
        mContext = context;
    }

    public class ViewPointRankHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView mProfileView;
        private TextView mPointView;
        private TextView mNickView;
        private TextView mRankView;

        public ViewPointRankHolder(View itemView) {
            super(itemView);

            mPointView = (TextView) itemView.findViewById(R.id.pointView);
            mPointView.setTypeface(FontManager.getInstance(mContext).getTypeface());

            mNickView = (TextView) itemView.findViewById(R.id.nickView);
            mNickView.setTypeface(FontManager.getInstance(mContext).getTypeface());

            mRankView = (TextView) itemView.findViewById(R.id.rankView);
            mRankView.setTypeface(FontManager.getInstance(mContext).getTypeface());

            mProfileView = (SimpleDraweeView) itemView.findViewById(R.id.profileView);
            GenericDraweeHierarchy gdh = new GenericDraweeHierarchyBuilder(mContext.getResources())
                    .setFailureImage(R.drawable.person_image_empty)
                    .setRoundingParams(new RoundingParams())
                    .setPlaceholderImage(R.drawable.person_image_empty, ScalingUtils.ScaleType.CENTER_CROP)
                    .setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP)
                    .build();
            mProfileView.setHierarchy(gdh);
        }
    }
    public void setArrayList(ArrayList<PointRankList> arraylist) {
        mArrayList = arraylist;
        notifyDataSetChanged();
    }
    @Override
    public ViewPointRankHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listrow_point_rank, parent, false);
        return new ViewPointRankHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewPointRankHolder holder, int position) {
        if(!mArrayList.get(position).getProfile().equals("")){
            RoundingParams roundingParams = holder.mProfileView.getHierarchy().getRoundingParams();
            roundingParams.setBorder(R.color.black, 1);
            roundingParams.setRoundAsCircle(true);
        }
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(mArrayList.get(position).getProfile()))
                .build();
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.mProfileView.getController())
                .build();
        holder.mProfileView.setController(draweeController);
        holder.mNickView.setText(mArrayList.get(position).getNick());
        holder.mPointView.setText(mArrayList.get(position).getPoint());
        holder.mRankView.setText(String.valueOf(position + 1));
    }


    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
}
