package com.grotesque.saa.common;

import android.content.res.Resources;

import com.grotesque.saa.R;

/**
 * Created by KH on 2016-08-13.
 */
public class Arrays {
    public static void init(final Resources resources) {
        if (sTitle == null) {
            sTitle = resources.getStringArray(R.array.boardTitle);
        }
    }
    public static String[] sTitle;

    private Arrays() {
    }
}
