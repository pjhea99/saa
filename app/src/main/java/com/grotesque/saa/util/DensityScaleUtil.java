// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.grotesque.saa.util;

import android.content.Context;

public class DensityScaleUtil {
    protected static float sDensityScale = 0.0F;

    public DensityScaleUtil() {
    }

    public static int dipToPixel(Context context, float f) {
        if(sDensityScale == 0.0F)
            sDensityScale = context.getResources().getDisplayMetrics().density;
        return (int)(sDensityScale * f + 0.5F);
    }

    public static float pixelToDip(Context context, int i) {
        if(sDensityScale == 0.0F)
            sDensityScale = context.getResources().getDisplayMetrics().density;
        return (float)i / sDensityScale - 0.5F;
    }
}
