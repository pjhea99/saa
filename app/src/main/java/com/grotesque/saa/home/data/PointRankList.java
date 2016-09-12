package com.grotesque.saa.home.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KH on 2016-09-02.
 */
public class PointRankList implements Parcelable{
    String profile;
    String nick;
    String point;

    public PointRankList(String profile, String nick, String point) {
        this.profile = profile;
        this.nick = nick;
        this.point = point;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    protected PointRankList(Parcel in) {
        profile = in.readString();
        nick = in.readString();
        point = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(profile);
        dest.writeString(nick);
        dest.writeString(point);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PointRankList> CREATOR = new Creator<PointRankList>() {
        @Override
        public PointRankList createFromParcel(Parcel in) {
            return new PointRankList(in);
        }

        @Override
        public PointRankList[] newArray(int size) {
            return new PointRankList[size];
        }
    };
}
