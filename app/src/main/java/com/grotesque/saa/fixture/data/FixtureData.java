package com.grotesque.saa.fixture.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KH on 2016-08-01.
 */
public class FixtureData implements Parcelable{
    String date;
    String[] home;
    String[] away;
    String description;
    String homeImg;
    String awayImg;



    public FixtureData(String date, String[] home, String[] away, String description, String homeImg, String awayImg) {
        this.date = date;
        this.home = home;
        this.away = away;
        this.description = description;
        this.homeImg = homeImg;
        this.awayImg = awayImg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String[] getHome() {
        return home;
    }

    public void setHome(String[] home) {
        this.home = home;
    }

    public String[] getAway() {
        return away;
    }

    public void setAway(String[] away) {
        this.away = away;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomeImg() {
        return homeImg;
    }

    public void setHomeImg(String homeImg) {
        this.homeImg = homeImg;
    }

    public String getAwayImg() {
        return awayImg;
    }

    public void setAwayImg(String awayImg) {
        this.awayImg = awayImg;
    }

    public static Creator<FixtureData> getCREATOR() {
        return CREATOR;
    }

    protected FixtureData(Parcel in) {
        date = in.readString();
        home = in.createStringArray();
        away = in.createStringArray();
        description = in.readString();
        homeImg = in.readString();
        awayImg = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeStringArray(home);
        dest.writeStringArray(away);
        dest.writeString(description);
        dest.writeString(homeImg);
        dest.writeString(awayImg);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FixtureData> CREATOR = new Creator<FixtureData>() {
        @Override
        public FixtureData createFromParcel(Parcel in) {
            return new FixtureData(in);
        }

        @Override
        public FixtureData[] newArray(int size) {
            return new FixtureData[size];
        }
    };
}
