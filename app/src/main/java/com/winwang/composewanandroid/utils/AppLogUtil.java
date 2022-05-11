package com.winwang.composewanandroid.utils;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;


import com.winwang.composewanandroid.constant.AppConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jince on 2018/7/26.
 */

public class AppLogUtil {
    private static final String TAG = "AppLogUtil";
    private static int LEVEL = 2;
    private static String LOG_FILE_NAME = "tzyk.log";
    private static String LOG_DIR = "tzyk";
    private static boolean FORMAT = false;

    public AppLogUtil() {
    }

    public static void setLogLevelFormat(Application app, int level, boolean format) {
        LEVEL = level;
        FORMAT = format;
    }

    public static int getLevel() {
        return LEVEL;
    }

    public static boolean isFormat() {
        return FORMAT;
    }

    public static void v(String msg) {
        formatLog(2, (String) null, msg, (Throwable) null);
    }

    public static void v(String tag, String msg) {
        formatLog(2, tag, msg, (Throwable) null);
    }

    public static void v(String tag, String msg, Throwable t) {
        formatLog(2, tag, msg, t);
    }

    public static void i(String msg) {
        formatLog(4, (String) null, msg, (Throwable) null);
    }

    public static void i(String tag, String msg) {
        formatLog(4, tag, msg, (Throwable) null);
    }

    public static void i(String tag, String msg, Throwable t) {
        formatLog(4, tag, msg, t);
    }

    public static void d(String msg) {
        formatLog(3, (String) null, msg, (Throwable) null);
    }

    public static void d(String tag, String msg) {
        formatLog(3, tag, msg, (Throwable) null);
    }

    public static void d(String tag, String msg, Throwable t) {
        formatLog(3, tag, msg, t);
    }

    public static void w(String msg) {
        formatLog(5, (String) null, msg, (Throwable) null);
    }

    public static void w(String tag, String msg) {
        formatLog(5, tag, msg, (Throwable) null);
    }

    public static void w(String tag, String msg, Throwable t) {
        formatLog(5, tag, msg, t);
    }

    public static void e(String msg) {
        if (!AppConfig.INSTANCE.isDebug()) {
            return;
        }
        formatLog(6, (String) null, msg, (Throwable) null);
    }

    public static void e(String tag, String msg) {
        if (!AppConfig.INSTANCE.isDebug()) {
            return;
        }
        formatLog(6, tag, msg, (Throwable) null);
    }

    public static void e(String tag, String msg, Throwable t) {
        if (!AppConfig.INSTANCE.isDebug()) {
            return;
        }
        formatLog(6, tag, msg, t);
    }

    private static void persistentToFile(Context context, StringBuffer sb) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String logfilePath = getExternalStorageDir(context) + File.separator + LOG_DIR;
            File logFile = new File(logfilePath);

            try {
                logFile.mkdirs();
                logfilePath = logfilePath + File.separator + LOG_FILE_NAME;
                logFile = new File(logfilePath);
                if (!logFile.exists()) {
                    logFile.createNewFile();
                }
            } catch (IOException e) {
                e(TAG, e.getMessage());
            }

            if (logFile.exists() && !logFile.isDirectory()) {
                int maxSize = checkLogFileMaxSize(logFile);
                if (maxSize > 0) {
                    String e = getExternalStorageDir(context) + File.separator + LOG_DIR + File.separator + System.currentTimeMillis() + LOG_FILE_NAME;
                    File newFile = new File(e);
                    boolean renamed = logFile.renameTo(newFile);
                    if (renamed) {
                        logFile = new File(logfilePath);
                    }
                }

                try {
                    BufferedWriter e1 = new BufferedWriter(new FileWriter(logFile, true));
                    e1.append(sb);
                    e1.close();
                } catch (IOException e) {
                    e(TAG, e.getMessage());
                }
            }
        }

    }

    private static File getExternalStorageDir(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return context.getExternalFilesDir(null);
        } else {
            return Environment.getExternalStorageDirectory();
        }
    }

    private static int checkLogFileMaxSize(File sizefile) {
        if (sizefile.exists()) {
            Long size = Long.valueOf(sizefile.length());
            return size.longValue() > 104857600L ? 2 : (size.longValue() > 5242880L ? 1 : 0);
        } else {
            return 0;
        }
    }

    private static void formatLog(int logLevel, String tag, String msg, Throwable error) {
        if (LEVEL <= logLevel) {
            StackTraceElement stackTrace = (new Throwable()).getStackTrace()[2];
            String classname = stackTrace.getClassName();
            String filename = stackTrace.getFileName();
            String methodname = stackTrace.getMethodName();
            int linenumber = stackTrace.getLineNumber();
            String output = null;
            if (FORMAT) {
                output = String.format("%s.%s(%s:%d)-->%s", new Object[]{classname, methodname, filename, Integer.valueOf(linenumber), msg});
            } else {
                output = msg;
            }

            if (null == tag) {
                tag = filename != null && filename.contains(".java") ? filename.replace(".java", "") : "";
            }

            if (output == null) {
                output = "" + null;
            }

            switch (logLevel) {
                case 2:
                    if (error == null) {
                        Log.v(tag, output);
                    } else {
                        Log.v(tag, output, error);
                    }
                    break;
                case 3:
                    if (error == null) {
                        Log.d(tag, output);
                    } else {
                        Log.d(tag, output, error);
                    }
                    break;
                case 4:
                    if (error == null) {
                        Log.i(tag, output);
                    } else {
                        Log.i(tag, output, error);
                    }
                    break;
                case 5:
                    if (error == null) {
                        Log.w(tag, output);
                    } else {
                        Log.w(tag, output, error);
                    }
                    break;
                case 6:
                    if (error == null) {
                        Log.e(tag, output);
                    } else {
                        Log.e(tag, output, error);
                    }
                default:
                    break;
            }

        }
    }

    public static Map<String, String> getUserErrorTrace(String msg, Throwable e) {
        HashMap logMap = new HashMap();
        logMap.put("desc", msg);
        if (e != null) {
            try {
                StringWriter e1 = new StringWriter();
                PrintWriter pw = new PrintWriter(e1);
                e.printStackTrace(pw);
                pw.close();
                logMap.put("exception", e1.toString());
            } catch (Exception var5) {
                e(TAG, var5.getMessage());
            }
        }

        return logMap;
    }

    public static String getMethodStackList() {
        StringBuilder builder = new StringBuilder();
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        int lenth = 40;
        if (stack.length < lenth) {
            lenth = stack.length;
        }
        for (int i = 0; i < lenth; i++) {
            StackTraceElement s = stack[i];
            builder.append(" clazz= ").append(s.getClassName())
                    .append(" method= ").append(s.getMethodName()).append(" LineNumber= ")
                    .append(s.getLineNumber()).append("\n");
        }
        return builder.toString();
    }
}
