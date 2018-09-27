package com.chen.baselibrary.util;

import android.content.Context;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

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
    //AES加密默认密钥，必须为16位
    private String AES_PASSWORD = "=DiskCacheUtil==";

    private DiskCacheUtil(){}

    public static final DiskCacheUtil getInstance(){
        return instance;
    }

    /**
     * 开启缓存，需首先调用此方法，然后再调用其他存储/读取等方法
     * open和close是成对存在的
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

    /**
     * 设置AES加密密钥，明文，必须为16位
     * @param password
     */
    public void setAESPassword(String password){
        if(password != null){
            this.AES_PASSWORD = password;
        }
    }
    /**
     * 缓存字符数据
     * @param key 键
     * @param value 值
     * @param needEncrypt 是否需要AES加密
     * @throws IOException
     */
    public void saveString(String key,String value,boolean needEncrypt) throws IOException {
        checkNotNull(key);
        checkNotNull(value);

        checkIsOpen();
        String md5Key = EncryptionUtil.GetMD5Code(key);
        String content = value;
        if(needEncrypt){
            content = EncryptionUtil.AESDecrypt(value,this.AES_PASSWORD);
        }
        DiskLruCache.Editor editor = this.mDiskLruCache.edit(md5Key);
        OutputStream outputStream = editor.newOutputStream(0);
        outputStream.write(content.getBytes("UTF-8"));
        editor.abortUnlessCommitted();
        this.mDiskLruCache.flush();
    }

    /**
     * 获取缓存的字符串
     * @param key 键
     * @param needDecrypt 是否需要解密
     * @return 明文value
     */
    public String getString(String key,boolean needDecrypt) throws IOException {
        checkNotNull(key);

        checkIsOpen();
        String md5Key = EncryptionUtil.GetMD5Code(key);
        DiskLruCache.Snapshot snapshot = this.mDiskLruCache.get(md5Key);
        if(snapshot != null){
            String value = snapshot.getString(0);
            if(needDecrypt){
                value = EncryptionUtil.AESDecrypt(value,this.AES_PASSWORD);
            }
            return value;
        }
        return null;
    }

    /**
     * 关闭缓存
     * open和close是成对存在的
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
