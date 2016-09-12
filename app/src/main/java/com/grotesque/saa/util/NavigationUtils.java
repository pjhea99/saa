package com.grotesque.saa.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.grotesque.saa.R;
import com.grotesque.saa.board.BoardActivity;
import com.grotesque.saa.comment.CommentListActivity;
import com.grotesque.saa.content.ContentActivity;
import com.grotesque.saa.content.data.ContentItem;
import com.grotesque.saa.home.data.DocumentList;
import com.grotesque.saa.image.ImageViewActivity;
import com.grotesque.saa.image.ImageViewerActivity;
import com.grotesque.saa.player.PlayerActivity;
import com.grotesque.saa.search.SearchActivity;

import java.util.ArrayList;

/**
 * Created by 경환 on 2016-04-04.
 */
public class NavigationUtils {
    public static void goCommentListAcitivity(Context context, String mid, DocumentList documentList){
        Intent intent = new Intent(context, CommentListActivity.class);
        intent.putExtra("mid", mid);
        intent.putExtra("array", documentList);
        context.startActivity(intent);
    }

    public static void goContentActivity(Context context, String condition){
        Intent intent = new Intent(context, ContentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("condition", condition);
        context.startActivity(intent);
    }
    public static void goContentActivity(Context context, String mid,  ArrayList<DocumentList> documentLists, int position){
        Intent intent = new Intent(context, ContentActivity.class);
        intent.putExtra("mid", mid);
        intent.putExtra("arrayList", documentLists);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }
    public static void goContentActivity(Context context, String mid,  DocumentList documentList){
        Intent intent = new Intent(context, ContentActivity.class);
        intent.putExtra("mid", mid);
        intent.putExtra("array", documentList);
        intent.putExtra("position", 0);
        context.startActivity(intent);
    }
    public static void goBoardActivity(Context context, String mid){
        Intent intent = new Intent(context, BoardActivity.class);
        intent.putExtra("mid", mid);
        context.startActivity(intent);
    }
    public static void goPlayerActivity(Context context, String url){
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
    public static void goSearchActivity(Context context, String mid){
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("mid", mid);
        context.startActivity(intent);
    }
    public static void goImageViewerActivity(Context context, ArrayList<ContentItem> arrayList, int position){
        Intent intent = new Intent(context, ImageViewerActivity.class);
        intent.putExtra("array", arrayList);
        intent.putExtra("position", position);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.aviary_slide_in_right, R.anim.nothing);
    }
    public static void goImageViewActivity(Context context, String Uri, int ImageType){
        Intent intent = new Intent(context, ImageViewActivity.class);
        intent.putExtra("image", Uri);
        intent.putExtra("image_type", ImageType);
        context.startActivity(intent);
    }
}
