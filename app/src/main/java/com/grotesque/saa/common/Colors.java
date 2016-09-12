package com.grotesque.saa.common;

import android.content.res.Resources;

import com.grotesque.saa.R;

/**
 * Created by BH on 2016-09-12.
 */
public class Colors {
    public static int sBlackColor = 0;
    public static int sWhiteColor = 0;
    public static int sMintColor = 0;

    public static void init (final Resources resources) {
        if(sBlackColor == 0) {
            sBlackColor = resources.getColor(R.color.black);
        }
        if(sWhiteColor == 0) {
            sWhiteColor = resources.getColor(R.color.white);
        }
        if(sMintColor == 0) {
            sMintColor = resources.getColor(R.color.brunch_mint);
        }
    }

    private Colors(){

    }
}
