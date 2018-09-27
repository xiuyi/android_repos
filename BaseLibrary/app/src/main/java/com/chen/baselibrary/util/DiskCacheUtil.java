package com.chen.baselibrary.util;

import android.content.Context;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author chen
 * @date 2018/9/27 上午8:58
 * email xiuyi.chen@erinspur.com
 * desc 文件缓存工具类，存储目录为context.getExternalCacheDir()
 */

public class DiskCacheUtil {
    private static final DiskCacheUtil instance = new DiskCacheUtil();

    private boolean mIsOpen = false;
    private DiskLruCache mDiskLruCache;

    private DiskCacheUtil(){}

    public static final DiskCacheUtil getInstance(){
        return instance;
    }

    /**
     * 开启缓存，需首先调用此方法，然后再调用其他存储/读取等方法
     * @param context
     * @throws FileUtils.LackOfSpaceException
     * @throws IOException
     */
    public void open(Context context) throws FileUtils.LackOfSpaceException, IOException {
        if(!this.mIsOpen || this.mDiskLruCache == null) {
            File cacheDir = FileUtils.getDiskCacheDir(context);
            this.mDiskLruCache = DiskLruCache.open(cacheDir, SystemUtils.getVersionCode(), 1, Integer.MAX_VALUE);
            this.mIsOpen = true;
        }
    }

    public void saveString(String key,String value) throws IOException {
        checkNotNull(key);
        checkNotNull(value);

        checkIsOpen();
        String md5Key = EncryptionUtil.GetMD5Code(key);
        DiskLruCache.Editor editor = this.mDiskLruCache.edit(md5Key);
    }

    /**
     * 关闭缓存
     * @throws IOException
     */
    public void close() throws IOException {
        this.mIsOpen = false;
        if(this.mDiskLruCache != null && !this.mDiskLruCache.isClosed()){
            this.mDiskLruCache.close();
        }
        this.mDiskLruCache = null;
    }

    /**
     * 检查DiskLruCache是否开启
     * @return
     */
    private boolean checkIsOpen(){
        if(this.mIsOpen && this.mDiskLruCache != null){
            return true;
        }
        throw new IllegalArgumentException("DiskLruCache未开启");
    }
}
