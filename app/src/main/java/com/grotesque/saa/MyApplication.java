package com.grotesque.saa;

import android.app.Application;
import android.graphics.Bitmap;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.grotesque.saa.common.Arrays;
import com.grotesque.saa.common.Colors;
import com.grotesque.saa.common.Drawables;
import com.grotesque.saa.util.OkHttpNetworkFetcher;

/**
 * Created by 경환 on 2016-04-09.
 */
public class MyApplication extends Application {
    private static MyApplication appInstance;
    @Override
    public void onCreate() {
        super.onCreate();

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setBitmapsConfig(Bitmap.Config.ARGB_8888)
                .setNetworkFetcher(OkHttpNetworkFetcher.getInstance())
                .setDownsampleEnabled(true)
                .build();

        Fresco.initialize(this, config);
        Arrays.init(getResources());
        Colors.init(getResources());
        Drawables.init(getResources());
        appInstance = this;
    }
    public static MyApplication getInstance() {
        return appInstance;
    }
}
