package com.yjt.app.utils;

import android.os.Environment;

import com.yjt.app.base.BaseApplication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    private static FileUtil mFileUtil;

    private FileUtil() { }

    private static synchronized void initialize() {
        if (mFileUtil == null) {
            mFileUtil = new FileUtil();
        }
    }

    public static FileUtil getInstance() {
        if (mFileUtil == null) {
            initialize();
        }
        return mFileUtil;
    }

    public File getFilePath() {
        File path;
        if (isSDCardExsist()) {
            path = getExternalPath("log");
        } else {
            // path = new File(mContext.getFilesDir(),
            // DeviceInfoUtil.getMobileId(mContext) + ".log");
            path = BaseApplication.getInstance().getFilesDir();
        }
        if (path.exists()) {
            return path;
        } else {
            return null;
        }
    }

    public List<File> getFiles() {
        File path;
        if (isSDCardExsist()) {
            path = getExternalPath("log");
        } else {
            path = BaseApplication.getInstance().getFilesDir();
        }
        List<File> files = new ArrayList<>();
        if (path.listFiles() != null && path.exists()) {
            for (File file : path.listFiles()) {
                if (file.getName().endsWith(".log")) {
                    files.add(file);
                }
            }
            return files;
        } else {
            return null;
        }
    }

    public void deleteFile() {
        for (File file : getFilePath().listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.getName().endsWith(".log");
            }
        })) {
            file.delete();
        }
    }

    public void deleteFile(String path) {
        if (isSDCardExsist()) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
    }

    public void deleteFile(File folder) {
        if (isSDCardExsist()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
    }

    public void deleteFile(String path, String fileName) {
        if (isSDCardExsist()) {
            File folder = new File(path);
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.getName().split("\\.")[0].equals(fileName)) {
                    file.delete();
                }
            }
        }
    }

    public boolean saveFile(String message) {
        try {
            File dir = BaseApplication.getInstance().getFilesDir();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, DeviceInfoUtil.getDeviceId(true) + ".log");
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(message.getBytes("UTF-8"));
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean saveFile(String message, boolean isAppend) {
        if (!isSDCardExsist()) {
            LogUtil.print("SDCard is not exsist");
            return false;
        }
        try {
            File dir = getExternalPath("log");
            LogUtil.print("SDCard:" + dir.getPath());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, DeviceInfoUtil.getDeviceId(true) + ".log");
            FileOutputStream fileOutputStream = new FileOutputStream(file,
                                                                     isAppend);
            fileOutputStream.write(message.getBytes("UTF-8"));
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isSDCardExsist() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                                                        .getExternalStorageState());
    }

    public File getExternalPath(String dirName) {
        return new File(Environment.getExternalStorageDirectory()
                                .getAbsolutePath()
                                + "/android/data/"
                                + BaseApplication.getInstance().getPackageName() + "/" + dirName + "/");
    }

    public String readByByte() {
        File dir = getFilePath();
        String[] files = dir.list();
        StringBuffer stringBuffer = new StringBuffer();
        for (String data : files) {
            File file = new File(getFilePath() + "/" + data);
            LogUtil.print("file_name:" + getFilePath() + "/" + data);
            if (file.getName().endsWith(".log")) {
                BufferedInputStream bufferedInputStream = null;
                try {
                    bufferedInputStream = new BufferedInputStream(
                            new FileInputStream(file));
                    byte[] buffer = new byte[1024];
                    int length;
                    showAvailableBytes(bufferedInputStream);
                    while ((length = bufferedInputStream.read(buffer)) != -1) {
                        // System.out.write(buffer, 0, length);
                        stringBuffer.append(new String(buffer, 0, length,
                                                       "UTF-8"));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    if (bufferedInputStream != null) {
                        try {
                            bufferedInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                return null;
            }
        }
        return stringBuffer.toString();
    }

    public String readByLine() {
        File dir = getFilePath();
        String[] files = dir.list();
        StringBuffer stringBuffer = new StringBuffer();
        for (String data : files) {
            File file = new File(getFilePath() + "/" + data);
            LogUtil.print("file_name:" + getFilePath() + "/" + data);
            if (file.getName().endsWith(".log")) {
                BufferedReader bufferedReader = null;
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(
                            new FileInputStream(file), "UTF-8"));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line).append(
                                System.getProperty("line.separator"));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                return null;
            }
        }
        return stringBuffer.toString();
    }

    private void showAvailableBytes(InputStream inputStream) {
        try {
            LogUtil.print("当前输入流中的字节数为：" + inputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
