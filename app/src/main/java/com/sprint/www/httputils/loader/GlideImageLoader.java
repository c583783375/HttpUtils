package com.sprint.www.httputils.loader;

import android.app.Activity;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.sprint.www.httputils.R;
import com.yancy.gallerypick.inter.ImageLoader;
import com.yancy.gallerypick.widget.GalleryImageView;

/**
 * GlideImageLoader
 * Created by Yancy on 2016/10/28.
 */
public class GlideImageLoader implements ImageLoader {

    private final static String TAG = "GlideImageLoader";

    @Override
    public void displayImage(Activity activity, Context context, String path, GalleryImageView galleryImageView, int width, int height) {
        Glide.with(context)
                .load(path)
                .placeholder(R.mipmap.gallery_pick_photo)
                .centerCrop()
                .into(galleryImageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}
