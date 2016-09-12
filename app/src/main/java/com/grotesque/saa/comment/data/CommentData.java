package com.grotesque.saa.comment.data;
import android.os.Parcel;
import android.os.Parcelable;

import com.grotesque.saa.content.data.CommentList;

import java.util.ArrayList;

/**
 * Created by KH on 2016-09-01.
 */
public class CommentData implements Parcelable {
    private String blamedCount;

    private String commentSrl;

    private String content;

    private String depth;

    private String emailAddress;

    private String homepage;

    private String isSecret;

    private String lastUpdate;

    private String nickName;

    private String parentSrl;

    private String regdate;

    private String userId;

    private String userName;

    private String voted_count;

    private ArrayList<CommentData> commentDatas;

    public CommentData(String blamedCount, String commentSrl, String content, String depth, String emailAddress, String homepage, String isSecret, String lastUpdate, String nickName, String parentSrl, String regdate, String userId, String userName, String voted_count) {
        this.blamedCount = blamedCount;
        this.commentSrl = commentSrl;
        this.content = content;
        this.depth = depth;
        this.emailAddress = emailAddress;
        this.homepage = homepage;
        this.isSecret = isSecret;
        this.lastUpdate = lastUpdate;
        this.nickName = nickName;
        this.parentSrl = parentSrl;
        this.regdate = regdate;
        this.userId = userId;
        this.userName = userName;
        this.voted_count = voted_count;
    }

    public CommentData(CommentList commentList, ArrayList<CommentList> commentLists){
        this.blamedCount = commentList.getBlamedCount();
        this.commentSrl = commentList.getCommentSrl();
        this.content = commentList.getContent();
        this.depth = commentList.getDepth();
        this.emailAddress = commentList.getEmailAddress();
        this.homepage = commentList.getHomepage();
        this.isSecret = commentList.getIsSecret();
        this.lastUpdate = commentList.getLastUpdate();
        this.nickName = commentList.getNickName();
        this.parentSrl = commentList.getParentSrl();
        this.regdate = commentList.getRegdate();
        this.userId = commentList.getUserId();
        this.userName = commentList.getUserName();
        this.voted_count = commentList.getVoted_count();
        ArrayList<CommentData> arrayList = new ArrayList<>();
        for(CommentList c : commentLists){
            if(commentList.getCommentSrl().equals(c.getParentSrl())){
                arrayList.add(new CommentData(c, commentLists));
            }
        }
        this.commentDatas = arrayList;
    }

    public String getBlamedCount() {
        return blamedCount;
    }

    public void setBlamedCount(String blamedCount) {
        this.blamedCount = blamedCount;
    }

    public String getCommentSrl() {
        return commentSrl;
    }

    public void setCommentSrl(String commentSrl) {
        this.commentSrl = commentSrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getIsSecret() {
        return isSecret;
    }

    public void setIsSecret(String isSecret) {
        this.isSecret = isSecret;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getParentSrl() {
        return parentSrl;
    }

    public void setParentSrl(String parentSrl) {
        this.parentSrl = parentSrl;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVoted_count() {
        return voted_count;
    }

    public void setVoted_count(String voted_count) {
        this.voted_count = voted_count;
    }

    public ArrayList<CommentData> getCommentDatas() {
        return commentDatas;
    }

    public void setCommentDatas(ArrayList<CommentData> commentDatas) {
        this.commentDatas = commentDatas;
    }

    protected CommentData(Parcel in) {
        blamedCount = in.readString();
        commentSrl = in.readString();
        content = in.readString();
        depth = in.readString();
        emailAddress = in.readString();
        homepage = in.readString();
        isSecret = in.readString();
        lastUpdate = in.readString();
        nickName = in.readString();
        parentSrl = in.readString();
        regdate = in.readString();
        userId = in.readString();
        userName = in.readString();
        voted_count = in.readString();
        commentDatas = in.createTypedArrayList(CommentData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(blamedCount);
        dest.writeString(commentSrl);
        dest.writeString(content);
        dest.writeString(depth);
        dest.writeString(emailAddress);
        dest.writeString(homepage);
        dest.writeString(isSecret);
        dest.writeString(lastUpdate);
        dest.writeString(nickName);
        dest.writeString(parentSrl);
        dest.writeString(regdate);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(voted_count);
        dest.writeTypedList(commentDatas);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommentData> CREATOR = new Creator<CommentData>() {
        @Override
        public CommentData createFromParcel(Parcel in) {
            return new CommentData(in);
        }

        @Override
        public CommentData[] newArray(int size) {
            return new CommentData[size];
        }
    };
}
