package com.chen.baselibrary.util;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

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

    private DiskCacheUtil() {
    }

    public static final DiskCacheUtil getInstance() {
        return instance;
    }

    /**
     * 开启缓存，需首先调用此方法，然后再调用其他存储/读取等方法
     * open和close是成对存在的
     *
     * @param context
     * @throws FileUtils.LackOfSpaceException
     * @throws IOException
     */
    public void open(Context context) throws FileUtils.LackOfSpaceException, IOException {
        open(context, "");
    }

    /**
     * 开启缓存，需首先调用此方法，然后再调用其他存储/读取等方法
     * open和close是成对存在的
     *
     * @param context
     * @param subDir  子目录名称
     * @throws FileUtils.LackOfSpaceException
     * @throws IOException
     */
    public void open(Context context, String subDir) throws FileUtils.LackOfSpaceException, IOException {
        if (!this.mIsOpen || this.mDiskLruCache == null) {
            File cacheDir = new File(FileUtils.getDiskCacheDir(context).getPath() + File.separator + subDir);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            this.mDiskLruCache = DiskLruCache.open(cacheDir, SystemUtils.getVersionCode(), 1, Integer.MAX_VALUE);
            this.mIsOpen = true;
        }
    }

    /**
     * 设置AES加密密钥，明文，必须为16位
     * 该方法必须在调用所有save/get API方法之前调用
     * 否则将导致加密/解密的密钥不一致
     *
     * @param password
     */
    public void setAESPassword(String password) {
        if (password != null) {
            this.AES_PASSWORD = password;
        }
    }

    /**
     * 保存boolean类型数据
     *
     * @param key   键
     * @param value
     * @throws IOException
     */
    public void saveBoolean(String key, boolean value) throws IOException {
        this.saveString(key, value ? "true" : "false", false);
    }

    /**
     * 缓存字符数据
     *
     * @param key         键
     * @param value       值
     * @param needEncrypt 是否需要AES加密
     * @throws IOException
     */
    public void saveString(String key, String value, boolean needEncrypt) throws IOException {
        checkNotNull(key);
        checkNotNull(value);

        checkIsOpen();
        String md5Key = EncryptionUtil.GetMD5Code(key.trim());
        String content = value;
        if (needEncrypt) {
            content = EncryptionUtil.AESEncrypt(value, this.AES_PASSWORD);
        }
        DiskLruCache.Editor editor = this.mDiskLruCache.edit(md5Key);
        OutputStream outputStream = editor.newOutputStream(0);
        outputStream.write(content.getBytes("UTF-8"));
        editor.commit();
        this.mDiskLruCache.flush();
    }

    /**
     * 检查DiskLruCache是否开启
     *
     * @return true检查成功 false检查失败
     */
    private boolean checkIsOpen() {
        if (this.mIsOpen && this.mDiskLruCache != null) {
            return true;
        }
        throw new IllegalArgumentException("DiskLruCache未开启");
    }

    /**
     * 获取boolean类型数据
     *
     * @param key 键
     * @return
     * @throws IOException
     */
    public boolean getBoolean(String key) throws IOException {
        String value = this.getString(key, false);
        return "true".equals(value);
    }

    /**
     * 获取缓存的字符串
     *
     * @param key         键
     * @param needDecrypt 是否需要解密
     * @return 明文value
     */
    public String getString(String key, boolean needDecrypt) throws IOException {
        checkNotNull(key);

        checkIsOpen();
        String md5Key = EncryptionUtil.GetMD5Code(key.trim());
        DiskLruCache.Snapshot snapshot = this.mDiskLruCache.get(md5Key);
        if (snapshot != null) {
            String value = snapshot.getString(0);
            if (needDecrypt) {
                value = EncryptionUtil.AESDecrypt(value, this.AES_PASSWORD);
            }
            return value;
        }
        return null;
    }

    /**
     * 保存Int类型数据
     *
     * @param key   键
     * @param value
     * @throws IOException
     */
    public void saveInt(String key, int value) throws IOException {
        this.saveString(key, String.valueOf(value), false);
    }

    /**
     * 获取int类型数据
     *
     * @param key          键
     * @param defaultValue
     * @return
     * @throws IOException
     */
    public int getInt(String key, int defaultValue) throws IOException {
        String value = this.getString(key, false);
        if (value != null) {
            return Integer.valueOf(value);
        } else {
            return defaultValue;
        }
    }

    /**
     * 保存Float类型数据
     *
     * @param key   键
     * @param value
     * @throws IOException
     */
    public void saveFloat(String key, float value) throws IOException {
        this.saveString(key, String.valueOf(value), false);
    }

    /**
     * 获取Float类型数据
     *
     * @param key          键
     * @param defaultValue
     * @return
     * @throws IOException
     */
    public float getFloat(String key, float defaultValue) throws IOException {
        String value = this.getString(key, false);
        if (value != null) {
            return Float.valueOf(value);
        } else {
            return defaultValue;
        }
    }

    /**
     * 保存double类型数据
     *
     * @param key   键
     * @param value
     * @throws IOException
     */
    public void saveDouble(String key, double value) throws IOException {
        this.saveString(key, String.valueOf(value), false);
    }

    /**
     * 获取double类型数据
     *
     * @param key          键
     * @param defaultValue
     * @return
     * @throws IOException
     */
    public double getDouble(String key, double defaultValue) throws IOException {
        String value = this.getString(key, false);
        if (value != null) {
            return Double.valueOf(value);
        } else {
            return defaultValue;
        }
    }

    /**
     * 保存Serializable对象
     *
     * @param key   键
     * @param value
     * @throws IOException
     */
    public void saveSerializable(String key, Serializable value) throws IOException {
        checkNotNull(key);
        checkNotNull(value);

        checkIsOpen();
        String md5Key = EncryptionUtil.GetMD5Code(key.trim());
        DiskLruCache.Editor editor = this.mDiskLruCache.edit(md5Key);
        OutputStream outputStream = editor.newOutputStream(0);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(value);
        editor.commit();
        this.mDiskLruCache.flush();
        objectOutputStream.close();
        outputStream.close();
    }

    /**
     * 获取Serializable对象
     *
     * @param key 键
     * @return
     * @throws IOException
     */
    public Object getSerializable(String key) throws IOException {
        checkNotNull(key);

        checkIsOpen();
        String md5Key = EncryptionUtil.GetMD5Code(key.trim());
        DiskLruCache.Snapshot snapshot = this.mDiskLruCache.get(md5Key);
        if (snapshot != null) {
            InputStream inputStream = snapshot.getInputStream(0);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            try {
                Object obj = objectInputStream.readObject();
                objectInputStream.close();
                inputStream.close();
                return obj;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 保存数据流
     *
     * @param key        键
     * @param dataStream
     */
    public void saveStream(String key, byte[] dataStream) throws IOException {
        checkNotNull(key);
        checkNotNull(dataStream);

        checkIsOpen();
        String md5Key = EncryptionUtil.GetMD5Code(key.trim());
        DiskLruCache.Editor editor = this.mDiskLruCache.edit(md5Key);
        OutputStream outputStream = editor.newOutputStream(0);
        outputStream.write(dataStream);
        editor.commit();
        this.mDiskLruCache.flush();
    }

    /**
     * 获取数据流
     *
     * @param key 键
     * @return
     * @throws IOException
     */
    public byte[] getStream(String key) throws IOException {
        checkNotNull(key);

        checkIsOpen();
        String md5Key = EncryptionUtil.GetMD5Code(key.trim());
        DiskLruCache.Snapshot snapshot = this.mDiskLruCache.get(md5Key);
        if (snapshot != null) {
            InputStream inputStream = snapshot.getInputStream(0);
            byte[] data = new byte[inputStream.available()];
            inputStream.close();
            return data;
        }
        return null;
    }

    /**
     * 关闭缓存
     * open和close是成对存在的
     *
     * @throws IOException
     */
    public void close() throws IOException {
        this.mIsOpen = false;
        if (this.mDiskLruCache != null && !this.mDiskLruCache.isClosed()) {
            this.mDiskLruCache.close();
        }
        this.mDiskLruCache = null;
    }
}
