package com.ericwyn.kindlesharehelper;

/**
 * 备份用户数据用的工具类
 * Created by Ericwyn on 2017/2/6.
 */

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

public class FileTools {
    private String FROM_DIR;
    private String TO_DIR;

    private String FROM_FILE;
    private String TO_FILE;

    private Context mContext;

    private String SHARED_PREFS;
    private String DATABASES;
    private String APP_PATH;
//    private Context mContext;
    private String BACKUP_PATH;
    private String BACKUP_DATABASES;
    private String BACKUP_SHARED_PREFS;

    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值

    public FileTools(Context context){
        mContext=context;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            TO_DIR = ""+Environment.getExternalStorageDirectory().getPath()+mContext.getPackageName()+"/sendFile";
            TO_FILE=""+Environment.getExternalStorageDirectory().getPath()+mContext.getPackageName()+"/sendFile";
        } else {
            TO_DIR = "com."+mContext.getPackageName()+"/sendFile";
            TO_FILE="com."+mContext.getPackageName()+"/sendFile";
            Toast.makeText(mContext, "没有检测到SD卡，可能无法备份成功", Toast.LENGTH_SHORT).show();
        }
    }

    public FileTools(Context context, String backUpPathName) {
        Log.i("文件复制活动","复制活动载入");
        mContext = context;
        APP_PATH = new StringBuilder("/data/data/").append(mContext.getPackageName()).toString();
        SHARED_PREFS = APP_PATH + "/shared_prefs";
        DATABASES = APP_PATH + "/databases";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            BACKUP_PATH = "/sdcard/"+backUpPathName+"/backup";
        } else {
            BACKUP_PATH = "/com."+backUpPathName+"/backup/";
            Toast.makeText(mContext, "没有检测到SD卡，可能无法备份成功", Toast.LENGTH_SHORT).show();
        }
        BACKUP_PATH += mContext.getPackageName();
        BACKUP_DATABASES = BACKUP_PATH + "/database";
        BACKUP_SHARED_PREFS = BACKUP_PATH + "/shared_prefs";
    }

    /**
     *
     * @param oldPath   源目录
     * @param newPath   目标目录
     * @param successMsg    成功时候的提示语
     * @param failedMsg     失败时候的提示语
     * @return  返回成功或者失败
     */
    public boolean copyDir(String oldPath, String newPath , String successMsg, String failedMsg) {

        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a=new File(oldPath);
            String[] file=a.list();
            File temp=null;
            for (int i = 0; i < file.length; i++) {
                if(oldPath.endsWith(File.separator)){
                    temp=new File(oldPath+file[i]);
                }
                else{
                    temp=new File(oldPath+File.separator+file[i]);
                }

                if(temp.isFile()){
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ( (len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if(temp.isDirectory()){//如果是子文件夹
                    copyDir(oldPath+"/"+file[i],newPath+"/"+file[i],successMsg,failedMsg);
                }
                return true;
            }
            Log.i("备份活动",successMsg);
            //showToast(successMsg);
        }
        catch (Exception e) {
//            showToast(failedMsg);
            Log.i("备份活动",failedMsg);
            e.printStackTrace();
            return false;
        }
        Log.i("备份活动","活动未完成，备份未知错误");

//        showToast("未知错误");
        return false;
    }

    /***
     * 文件的复制类
     * @param fromFile  要复制的文件的名称
     * @param toFile    复
     * @return
     */
    public boolean CopyFile(String fromFile, String toFile)
    {
        try
        {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return true;

        } catch (Exception ex)
        {
            return false;
        }
    }

    /**
     * 获取文件指定文件的指定单位的大小
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath,int sizeType){
        File file=new File(filePath);
        long blockSize=0;
        try {
            if(file.isDirectory()){
                blockSize = getFileSizes(file);
            }else{
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小","获取失败!");
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath){
        File file=new File(filePath);
        long blockSize=0;
        try {
            if(file.isDirectory()){
                blockSize = getFileSizes(file);
            }else{
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小","获取失败!");
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件的大小
     * @param file  文件
     * @return  返回大小
     * @throws Exception    抛出异常
     */
    private static long getFileSize(File file) throws Exception
    {
        long size = 0;
        if (file.exists()){
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        }
        else{
            file.createNewFile();
            Log.e("获取文件大小","文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++){
            if (flist[i].isDirectory()){
                size = size + getFileSizes(flist[i]);
            }
            else{
                size =size + getFileSize(flist[i]);
            }
        }
        return size;
    }
    /**
     * 转换文件大小
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize="0B";
        if(fileS==0){
            return wrongSize;
        }
        if (fileS < 1024){
            fileSizeString = df.format((double) fileS) + "B";
        }
        else if (fileS < 1048576){
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        }
        else if (fileS < 1073741824){
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        }
        else{
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
    /**
     * 转换文件大小,指定转换的类型
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double FormetFileSize(long fileS,int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong=Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong=Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong=Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong=Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }



}