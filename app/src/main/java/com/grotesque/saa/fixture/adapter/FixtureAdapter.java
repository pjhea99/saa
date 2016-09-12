package com.grotesque.saa.fixture.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.grotesque.saa.R;
import com.grotesque.saa.fixture.data.FixtureData;
import com.grotesque.saa.util.FontManager;
import com.grotesque.saa.util.StringUtils;

import java.util.ArrayList;

import static com.grotesque.saa.util.LogUtils.LOGE;

/**
 * Created by KH on 2016-08-01.
 */
public class FixtureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = FixtureAdapter.class.getSimpleName();
    private static final String IMG_URL = "http://www.legaseriea.it";

    private Context context;
    private ArrayList<FixtureData> arrayList;

    public FixtureAdapter(Context context, ArrayList<FixtureData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public class ViewItemHolder extends RecyclerView.ViewHolder{
        private TextView mDateView;
        private TextView mHomeView;
        private TextView mHomeScoreView;
        private TextView mHomeScorePlayerView;
        private TextView mAwayView;
        private TextView mAwayScoreView;
        private TextView mAwayScorePlayerView;
        private SimpleDraweeView mHomeImgView;
        private SimpleDraweeView mAwayImgView;

        public ViewItemHolder(View itemView) {
            super(itemView);
            mDateView = (TextView) itemView.findViewById(R.id.fixture_date);
            mHomeView = (TextView) itemView.findViewById(R.id.fixture_home_team);
            mHomeScoreView = (TextView) itemView.findViewById(R.id.fixture_home_score);
            mHomeScorePlayerView = (TextView) itemView.findViewById(R.id.fixture_home_score_player);
            mAwayView = (TextView) itemView.findViewById(R.id.fixture_away_team);
            mAwayScoreView = (TextView) itemView.findViewById(R.id.fixture_away_score);
            mAwayScorePlayerView = (TextView) itemView.findViewById(R.id.fixture_away_score_player);
            mHomeImgView = (SimpleDraweeView) itemView.findViewById(R.id.fixture_home_img);
            mAwayImgView = (SimpleDraweeView) itemView.findViewById(R.id.fixture_away_img);

            mDateView.setTypeface(FontManager.getInstance(context).getTypeface());
            mHomeView.setTypeface(FontManager.getInstance(context).getTypeface());
            mHomeScoreView.setTypeface(FontManager.getInstance(context).getTypeface());
            mHomeScorePlayerView.setTypeface(FontManager.getInstance(context).getTypeface());

            mAwayView.setTypeface(FontManager.getInstance(context).getTypeface());
            mAwayScoreView.setTypeface(FontManager.getInstance(context).getTypeface());
            mAwayScorePlayerView.setTypeface(FontManager.getInstance(context).getTypeface());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listrow_fixture, parent, false);
        return new ViewItemHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewItemHolder vih = (ViewItemHolder) holder;
        vih.mDateView.setText(StringUtils.convertFixtureDate(arrayList.get(position).getDate()));
        vih.mHomeView.setText(StringUtils.convertTeamName(arrayList.get(position).getHome()[1]));
        vih.mAwayView.setText(StringUtils.convertTeamName(arrayList.get(position).getAway()[1]));

        vih.mHomeScoreView.setText(arrayList.get(position).getHome()[0]);
        vih.mAwayScoreView.setText(arrayList.get(position).getAway()[0]);

        if(arrayList.get(position).getHome().length > 2){
            vih.mHomeScorePlayerView.setVisibility(View.VISIBLE);
            String homeScored = "";
            for(int i=2; i<arrayList.get(position).getHome().length; i++){
                if(arrayList.get(position).getHome()[i].matches(".*[0-9].*")) {
                    if(arrayList.get(position).getHome()[i-1].equals("+") || i == 2)
                        homeScored = homeScored + arrayList.get(position).getHome()[i];
                    else
                        homeScored = homeScored + "\n" + arrayList.get(position).getHome()[i];
                }
                else
                    homeScored = homeScored + " " + arrayList.get(position).getHome()[i];
            }
            vih.mHomeScorePlayerView.setText(homeScored);
        }else
            vih.mHomeScorePlayerView.setVisibility(View.GONE);

        if(arrayList.get(position).getAway().length > 2){
            vih.mAwayScorePlayerView.setVisibility(View.VISIBLE);
            String awayScored = "";
            for(int i=2; i<arrayList.get(position).getAway().length; i++){
                if(arrayList.get(position).getAway()[i].matches(".*[0-9].*"))
                    if(arrayList.get(position).getAway()[i-1].equals("+") || i == 2)
                        awayScored = awayScored + arrayList.get(position).getAway()[i];
                    else
                        awayScored = awayScored + "\n" + arrayList.get(position).getAway()[i];
                else
                    awayScored = awayScored + " " + arrayList.get(position).getAway()[i];
            }
            vih.mAwayScorePlayerView.setText(awayScored);
        }else
            vih.mAwayScorePlayerView.setVisibility(View.GONE);

        vih.mHomeImgView.setImageURI(IMG_URL+arrayList.get(position).getHomeImg());
        vih.mAwayImgView.setImageURI(IMG_URL+arrayList.get(position).getAwayImg());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
