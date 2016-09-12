package com.grotesque.saa.home.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by 경환 on 2016-04-03.
 */
public class DocumentList implements Parcelable{

    @SerializedName("document_srl")
    @Expose
    private String documentSrl;
    @SerializedName("category_srl")
    @Expose
    private String categorySrl;
    @SerializedName("member_srl")
    @Expose
    private String memberSrl;
    @SerializedName("nick_name")
    @Expose
    private String nickName;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("readed_count")
    @Expose
    private String readedCount;
    @SerializedName("voted_count")
    @Expose
    private String votedCount;
    @SerializedName("blamed_count")
    @Expose
    private String blamedCount;
    @SerializedName("comment_count")
    @Expose
    private String commentCount;
    @SerializedName("regdate")
    @Expose
    private String regdate;
    @SerializedName("last_update")
    @Expose
    private String lastUpdate;

    @SerializedName("status")
    @Expose
    private String status;

    public DocumentList(String documentSrl, String memberSrl, String nickName, String userId, String userName, String title, String content, String tags, String commentCount, String regdate, String categorySrl) {
        this.documentSrl = documentSrl;
        this.memberSrl = memberSrl;
        this.nickName = nickName;
        this.userId = userId;
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.commentCount = commentCount;
        this.regdate = regdate;
        this.categorySrl = categorySrl;
    }

    public String getDocumentSrl() {
        return documentSrl;
    }

    public void setDocumentSrl(String documentSrl) {
        this.documentSrl = documentSrl;
    }

    public String getCategorySrl() {
        return categorySrl;
    }

    public void setCategorySrl(String categorySrl) {
        this.categorySrl = categorySrl;
    }

    public String getMemberSrl() {
        return memberSrl;
    }

    public void setMemberSrl(String memberSrl) {
        this.memberSrl = memberSrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getReadedCount() {
        return readedCount;
    }

    public void setReadedCount(String readedCount) {
        this.readedCount = readedCount;
    }

    public String getVotedCount() {
        return votedCount;
    }

    public void setVotedCount(String votedCount) {
        this.votedCount = votedCount;
    }

    public String getBlamedCount() {
        return blamedCount;
    }

    public void setBlamedCount(String blamedCount) {
        this.blamedCount = blamedCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }




    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean hasImg(){
        if(this.content.contains("<img "))
            return true;
        else
            return false;
    }
    public boolean hasYoutube(){
        if(this.content.contains("youtube.com"))
            return true;
        else
            return false;
    }

    public DocumentList(Parcel in) {
        readFromParcel(in);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(documentSrl);
        dest.writeString(categorySrl);
        dest.writeString(memberSrl);
        dest.writeString(nickName);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(tags);
        dest.writeString(readedCount);
        dest.writeString(votedCount);
        dest.writeString(blamedCount);
        dest.writeString(commentCount);
        dest.writeString(regdate);
        dest.writeString(lastUpdate);

        dest.writeString(status);
    }
    private void readFromParcel(Parcel in){
        documentSrl = in.readString();
        categorySrl = in.readString();
        memberSrl = in.readString();
        nickName = in.readString();
        userId = in.readString();
        userName = in.readString();
        title = in.readString();
        content = in.readString();
        tags = in.readString();
        readedCount = in.readString();
        votedCount = in.readString();
        blamedCount = in.readString();
        commentCount = in.readString();
        regdate = in.readString();
        lastUpdate = in.readString();
        status = in.readString();
    }
    public static final Creator CREATOR = new Creator() {
        @Override
        public DocumentList createFromParcel(Parcel in) {
            return new DocumentList(in);
        }

        @Override
        public DocumentList[] newArray(int size) {
            return new DocumentList[size];
        }
    };
}
