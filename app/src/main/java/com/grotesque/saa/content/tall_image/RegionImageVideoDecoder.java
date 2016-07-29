package com.grotesque.saa.content.tall_image;

import android.content.Context;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;

import com.bumptech.glide.load.model.ImageVideoWrapper;

import java.io.IOException;

class RegionImageVideoDecoder extends RegionResourceDecoder<ImageVideoWrapper> {
	public RegionImageVideoDecoder(Context context, Rect region) {
		super(context, region);
	}

	@Override protected BitmapRegionDecoder createDecoder(ImageVideoWrapper source, int width, int height)
			throws IOException {
		BitmapRegionDecoder decoder;
		try {
			decoder = BitmapRegionDecoder.newInstance(source.getStream(), false);
		} catch (Exception ex) {
			decoder = BitmapRegionDecoder.newInstance(source.getFileDescriptor().getFileDescriptor(), false);
		}
		return decoder;
	}
}
