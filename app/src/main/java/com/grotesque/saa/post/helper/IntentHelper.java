package com.grotesque.saa.post.helper;

import android.app.Activity;
import android.content.Intent;

import com.grotesque.saa.comment.CommentWriteActivity;
import com.grotesque.saa.content.data.CommentList;
import com.grotesque.saa.post.PreviewActivity;
import com.grotesque.saa.post.data.PostData;

import java.util.ArrayList;

/**
 * Created by AKiniyalocts on 2/23/15.
 *
 */
public class IntentHelper {
  public final static int FILE_PICK = 1001;
  public final static int POST_PREVIEW = 1002;
  public final static int COMMENT_WRITE = 1003;


  public static void chooseFileIntent(Activity activity){
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*");
    activity.startActivityForResult(intent, FILE_PICK);
  }

  public static void previewIntent(Activity activity, ArrayList<PostData> postDatas, String mid){
    Intent intent = new Intent(activity, PreviewActivity.class);
    intent.putParcelableArrayListExtra("html", postDatas);
    intent.putExtra("mid", mid);
    activity.startActivityForResult(intent, POST_PREVIEW);
  }
  public static void commentWriteIntent(Activity activity, String comment) {
    Intent intent = new Intent(activity, CommentWriteActivity.class);
    intent.putExtra("comment", comment);
    activity.startActivityForResult(intent, COMMENT_WRITE);
  }
}
