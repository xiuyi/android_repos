package com.chen.baselibrary.util;

import android.app.Fragment;
import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.chen.baselibrary.R;

import java.io.File;

/**
 * Created by chen on 2018/3/21.
 * 图片加载工具，使用Glide框架
 * Glide.with(this)
 * .load(url)
 * .asGif()//指定加载GIF图片
 * .asBitmap()//只加载静态图片
 * .placeholder(R.drawable.loading)//loading图片
 * .error(R.drawable.error)//图片加载错误时的图片
 * .diskCacheStrategy(DiskCacheStrategy.NONE)//是否允许本地缓存
 * .override(100, 100)//修改图片尺寸，避免内存泄漏
 * .into(imageView);
 */

public class ImageLoaderUtils {
    private static final int DEFAULT_LOADING_IMG = R.drawable.icon_noimg_mulcolor_64dp;
    private static final int DEFAULT_ERROR_IMG = R.drawable.icon_noimg_mulcolor_64dp;

    public static void loadImg(Fragment fragment, ImageView imageView, String url) {
        Glide.with(fragment)
                .load(url)
                .placeholder(DEFAULT_LOADING_IMG)
                .error(DEFAULT_ERROR_IMG)
                .into(imageView);
    }
    public static void loadImg(Fragment fragment, ImageView imageView, String url, int placeholderRes, int errorRes) {
        Glide.with(fragment)
                .load(url)
                .placeholder(placeholderRes)
                .error(errorRes)
                .into(imageView);
    }
    /**
     * 清除图片磁盘缓存
     * 必须在子线程运行
     */
    public static void clearImageDiskCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     * 必须在主线程运行
     */
    public static void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Glide.get(context).clearMemory();
            } else {
                throw new RuntimeException("clearMemory()必须在主线程运行");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过URL加载图片
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void loadImg(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .placeholder(DEFAULT_LOADING_IMG)
                .error(DEFAULT_ERROR_IMG)
                .into(imageView);
    }

    /**
     * 通过URL加载图片，自定义placeholder和error
     *
     * @param context
     * @param imageView
     * @param url
     * @param placeholderRes 占位符资源ID
     * @param errorRes 加载错误资源ID
     */
    public static void loadImg(Context context, ImageView imageView, String url, int placeholderRes, int errorRes) {
        Glide.with(context)
                .load(url)
                .placeholder(placeholderRes)
                .error(errorRes)
                .into(imageView);
    }

    /**
     * 通过资源ID加载图片
     *
     * @param context
     * @param imageView
     * @param resourceId
     */
    public static void loadImg(Context context, ImageView imageView, int resourceId) {
        Glide.with(context)
                .load(resourceId)
                .placeholder(DEFAULT_LOADING_IMG)
                .error(DEFAULT_ERROR_IMG)
                .into(imageView);
    }

    /**
     * 加载视频缩略图
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void loadVideoThum(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.RESULT)
                .crossFade().centerCrop().into(new GlideDrawableImageViewTarget(imageView));
    }

    /**
     * 从本地文件加载图片
     *
     * @param context
     * @param imageView
     * @param file           文件
     * @param compressWidth  压缩后的图片宽度
     * @param comperssHeight 压缩后的图片高度
     */
    public static void loadImgFromFile(Context context, ImageView imageView, File file, int compressWidth, int comperssHeight) {
        Glide.with(context)
                .load(file)
                .placeholder(DEFAULT_LOADING_IMG)
                .error(DEFAULT_ERROR_IMG)
                .override(compressWidth, comperssHeight)
                .into(imageView);
    }

    /**
     * 图片加载基础方法
     *
     * @param fragment
     * @param imageView
     * @param path           文件路径 可以是一个URl也可以是一个本地文件路径
     * @param compressWidth  压缩后的图片宽度 <=0 将不压缩
     * @param comperssHeight 压缩后的图片高度 <=0 将不压缩
     * @param corners        图片圆角半径 单位px
     */
    public static void loadImg(Fragment fragment, ImageView imageView, String path, int compressWidth, int comperssHeight, int corners, RequestListener listener) {
        loadImg(fragment.getActivity(), imageView, path, compressWidth, comperssHeight, corners, listener);
    }

    /**
     * 图片加载基础方法
     *
     * @param context
     * @param imageView
     * @param path           文件路径 可以是一个URl也可以是一个本地文件路径
     * @param compressWidth  压缩后的图片宽度 <=0 将不压缩
     * @param comperssHeight 压缩后的图片高度 <=0 将不压缩
     * @param corners        图片圆角半径 单位px
     */
    public static void loadImg(Context context, ImageView imageView, String path, int compressWidth, int comperssHeight, int corners, RequestListener listener) {
        if (TextUtils.isEmpty(path)) {
            path = "";
        }
        DrawableRequestBuilder drb = Glide.with(context)
                .load(path.startsWith("http") ? path : new File(path))
                .placeholder(DEFAULT_LOADING_IMG)
                .error(DEFAULT_ERROR_IMG)
                .override(compressWidth <= 0 ? Target.SIZE_ORIGINAL : compressWidth, comperssHeight <= 0 ? Target.SIZE_ORIGINAL : comperssHeight);
        if (corners > 0) {
            drb.transform(new CornersTransform(context, corners));
        }
        if (listener != null) {
            drb.listener(listener);
        }
        drb.into(imageView);
    }

}
