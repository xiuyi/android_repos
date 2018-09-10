/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.chen.baselibrary.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

/**
 * @author chen
 * @date 2018/9/10 下午1:52
 * email xiuyi.chen@erinspur.com
 * desc 文件操作工具类
 * <p>
 * 引用：关于android系统各个目录与API的关系以及在4.4以前和以后的内部存储与外部存储的区别，请参考如下博文:
 * https://blog.csdn.net/u010937230/article/details/73303034
 */
public class FileUtils {
    /**
     * 在外部存储的根路径下新建目录或者文件
     *
     * @param name /a/b.txt 类似的路径名
     * @return 返回新创建的文件
     */
    public static File createNewDirOrFileInExternalRootDir(String name) {
        File file = new File(getExternalRootDir(), name);
        //目录
        if (file.isDirectory() && !file.exists()) {
            if (file.mkdirs()) {
                return file;
            }
        }
        //文件
        if (file.isFile() && !file.exists()) {
            try {
                if (file.mkdirs() && file.createNewFile()) {
                    return file;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取外部存储的根路径，
     * 如果没有返回null
     *
     * @return 文件
     */
    public static File getExternalRootDir() {
        //判断外部存储是否可用
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return Environment.getExternalStorageDirectory();
        } else {
            return null;
        }
    }

    /**
     * 获取本地视频缩略图
     *
     * @param filePath 视频文件本地路径
     * @return 图片
     */
    @SuppressLint("NewApi")
    public static Bitmap createVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);

            bitmap = retriever.getFrameAtTime(1000);

        } catch (Exception ex) {

        } finally {
            try {
                retriever.release();

            } catch (RuntimeException ex) {
            }

        }
        return bitmap;

    }

    /**
     * 格式化单位
     *
     * @param length 长度
     * @return xxB  xxKB  xxMB xx GB
     */
    public static String formatFileLength(long length) {
        if (length >> 30 > 0L) {
            float sizeGb = Math.round(10.0F * (float) length / 1.073742E+009F) / 10.0F;
            return sizeGb + " GB";
        }
        if (length >> 20 > 0L) {
            return formatSizeMb(length);
        }
        if (length >> 9 > 0L) {
            float sizekb = Math.round(10.0F * (float) length / 1024.0F) / 10.0F;
            return sizekb + " KB";
        }
        return length + " B";
    }

    /**
     * 转换成Mb单位
     *
     * @param length 长度
     * @return xxMB
     */
    public static String formatSizeMb(long length) {
        float mbSize = Math.round(10.0F * (float) length / 1048576.0F) / 10.0F;
        return mbSize + " MB";
    }

    /**
     * 是否文档
     *
     * @param fileName 包含后缀的文件名
     * @return true 是 false 否
     */
    public static boolean isDocument(String fileName) {
        String lowerCase = (fileName == null ? "" : fileName).toLowerCase();
        return lowerCase.endsWith(".doc") || lowerCase.endsWith(".docx")
                || lowerCase.endsWith("wps");
    }

    /**
     * 是否图片
     *
     * @param fileName 包含后缀的文件名
     * @return true 是 false 否
     */
    public static boolean isPic(String fileName) {
        String lowerCase = (fileName == null ? "" : fileName).toLowerCase();
        return lowerCase.endsWith(".bmp") || lowerCase.endsWith(".png")
                || lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg")
                || lowerCase.endsWith(".gif");
    }

    /**
     * 是否压缩文件
     *
     * @param fileName 包含后缀的文件名
     * @return true 是 false 否
     */
    public static boolean isCompresseFile(String fileName) {
        String lowerCase = (fileName == null ? "" : fileName).toLowerCase();
        return lowerCase.endsWith(".rar") || lowerCase.endsWith(".zip")
                || lowerCase.endsWith(".7z") || lowerCase.endsWith("tar")
                || lowerCase.endsWith(".iso");
    }

    /**
     * 是否文本文档
     *
     * @param fileName 包含后缀的文件名
     * @return true 是 false 否
     */
    public static boolean isTextFile(String fileName) {
        String lowerCase = (fileName == null ? "" : fileName).toLowerCase();
        return lowerCase.endsWith(".txt") || lowerCase.endsWith(".rtf");
    }

    /**
     * 是否Pdf
     *
     * @param fileName 包含后缀的文件名
     * @return true 是 false 否
     */
    public static boolean isPdf(String fileName) {
        return (fileName == null ? "" : fileName).toLowerCase().endsWith(".pdf");
    }

    /**
     * 是否Ppt
     *
     * @param fileName 包含后缀的文件名
     * @return true 是 false 否
     */
    public static boolean isPPt(String fileName) {
        String lowerCase = (fileName == null ? "" : fileName).toLowerCase();
        return lowerCase.endsWith(".ppt") || lowerCase.endsWith(".pptx");
    }

    /**
     * 是否Excel
     *
     * @param fileName 包含后缀的文件名
     * @return true 是 false 否
     */
    public static boolean isXls(String fileName) {
        String lowerCase = (fileName == null ? "" : fileName).toLowerCase();
        return lowerCase.endsWith(".xls") || lowerCase.endsWith(".xlsx");
    }

    /**
     * 是否音频
     *
     * @param fileName 包含后缀的文件名
     * @return true 是 false 否
     */
    public static boolean isAudio(String fileName) {
        String lowerCase = (fileName == null ? "" : fileName).toLowerCase();
        return lowerCase.endsWith(".mp3") || lowerCase.endsWith(".wma")
                || lowerCase.endsWith(".mp4") || lowerCase.endsWith(".rm");
    }

    /**
     * 是否视频
     *
     * @param fileName 包含后缀的文件名
     * @return true 是 false 否
     */
    public static boolean isVideo(String fileName) {
        String lowerCase = (fileName == null ? "" : fileName).toLowerCase();
        return lowerCase.endsWith(".mp4") || lowerCase.endsWith(".3gp")
                || lowerCase.endsWith(".mov");
    }

    /**
     * 获取文件扩展名
     *
     * @param uri 包含后缀的路径
     * @return Extension including the dot("."); "" if there is no extension;
     * null if uri was null.
     */
    @Nullable
    public static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }

    /**
     * 文件拷贝
     *
     * @param src      新文件目录
     * @param filename 新文件名
     * @param srcFile  被拷贝文件
     * @return 0表示复制成功
     */
    public static int copyFile(File src, String filename, File srcFile) {
        src.length();
        if (!src.exists()) {
            return -1;
        }
        return copyFile(src, filename, readFlieToByte(srcFile.getAbsolutePath(), 0, (int) srcFile.length()));
    }

    /**
     * 文件拷贝
     *
     * @param src      新文件目录
     * @param filename 新文件名
     * @param buffer   byte数组
     * @return 0表示复制成功
     */
    public static int copyFile(File src, String filename, byte[] buffer) {
        src.length();
        if (!src.exists()) {
            return -1;
        }
        return copyFile(src.getAbsolutePath(), filename, buffer);
    }

    /**
     * 将文件转为byte数组
     *
     * @param filePath 文件路径
     * @param seek     文件读取位置
     * @param length   文件长度
     * @return 字节数组
     */
    @Nullable
    public static byte[] readFlieToByte(String filePath, int seek, int length) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        if (length == -1) {
            length = (int) file.length();
        }

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            byte[] bs = new byte[length];
            randomAccessFile.seek(seek);
            randomAccessFile.readFully(bs);
            randomAccessFile.close();
            return bs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 拷贝文件
     *
     * @param fileDir  新文件目录
     * @param fileName 新文件名称
     * @param buffer   源文件内容
     * @return 0表示复制成功
     */
    public static int copyFile(String fileDir, String fileName, byte[] buffer) {
        if (buffer == null) {
            return -2;
        }

        try {
            File file = new File(fileDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            File resultFile = new File(file, fileName);
            if (!resultFile.exists()) {
                resultFile.createNewFile();
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                    new FileOutputStream(resultFile, true));
            bufferedOutputStream.write(buffer);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            return 0;

        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * 根据后缀名判断是否是图片文件
     *
     * @param type 包含后缀的文件名
     * @return 是否是图片结果true or false
     */
    public static boolean isImage(String type) {
        if (type != null
                && (type.equals("jpg") || type.equals("gif")
                || type.equals("png") || type.equals("jpeg")
                || type.equals("bmp") || type.equals("wbmp")
                || type.equals("ico") || type.equals("jpe"))) {
            return true;
        }
        return false;
    }

    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return
     */
    public static String getFileExt(String fileName) {

        if (TextUtils.isEmpty(fileName)) {

            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length());
    }

    /**
     * 获取amr文件时长,单位秒
     *
     * @param file 本地文件路径
     * @return 时长 单位秒
     * @throws IOException 异常
     */
    public static int getAmrDuration(File file) {
        try {
            long duration = -1;
            int[] packedSize = {12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0,
                    0, 0};
            RandomAccessFile randomAccessFile = null;
            try {
                randomAccessFile = new RandomAccessFile(file, "rw");
                // 文件的长度
                long length = file.length();
                // 设置初始位置
                int pos = 6;
                // 初始帧数
                int frameCount = 0;
                int packedPos = -1;
                // 初始数据值
                byte[] datas = new byte[1];
                while (pos <= length) {
                    randomAccessFile.seek(pos);
                    if (randomAccessFile.read(datas, 0, 1) != 1) {
                        duration = length > 0 ? ((length - 6) / 650) : 0;
                        break;
                    }
                    packedPos = (datas[0] >> 3) & 0x0F;
                    pos += packedSize[packedPos] + 1;
                    frameCount++;
                }
                // 帧数*20
                duration += frameCount * 20;
            } finally {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            }
            return (int) ((duration / 1000) + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据URL获取视频文件的时长
     *
     * @param url 可以是本地路径，可以是http开头的url
     * @return 视频时长 单位秒
     */
    public static long getVideoDuration(String url) {
        String duration = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //如果是网络路径
            if (url.startsWith("http")) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {//如果是本地路径
                retriever.setDataSource(url);
            }
            duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(duration)) {
            return Long.parseLong(duration);
        } else {
            return 0;
        }
    }

    /**
     * 根据本地视频文件路径，获取视频缩略图
     *
     * @param path 本地文件路径
     * @return 图片
     */
    public static Bitmap getVideoThumbnail(String path) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(path);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 根据路径删除指定的目录，无论存在与否
     * 如果是目录，将删除其下的子目录以及文件
     *
     * @param sPath 要删除的目录path
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean deleteDirOrFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件path
     * @return 删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录以及目录下的文件
     *
     * @param sPath 被删除目录的路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            return false;
        }
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
}