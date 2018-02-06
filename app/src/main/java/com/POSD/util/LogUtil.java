package com.POSD.util;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LogUtil {
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    private static final String ERROR_TAG = "FAILED";
    private static final Object FILE_LOCK = new Object();
    private static final SimpleDateFormat FORMATER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
    private static final int FORMAT_FILE_LOG_LENGTH = formatFileLog(3, "", "").length();
    private static final String GLOBAL_TAG = "POS_LOG.";
    public static final int INFO = 4;
    public static final boolean IS_ADB_ASSERT_OPEN = true;
    public static final boolean IS_ADB_DEBUG_OPEN = true;
    public static final boolean IS_ADB_ERROR_OPEN = true;
    public static final boolean IS_ADB_INFO_OPEN = true;
    public static final boolean IS_ADB_VERBOSE_OPEN = true;
    public static final boolean IS_ADB_WARN_OPEN = true;
    public static final boolean IS_MORE_LOG = false;
    public static final int LOGCAT_FILTER_PRIORITY_ADB = 0;
    public static final int LOGCAT_FILTER_PRIORITY_SAVE_FILE = 8;
    private static final String LOG_FOLDER = "bluetooth_log";
    private static final int METHOD_NAME_LENGTH = getTraceLog("", "", "").length();
    public static final int PRIORITY_HIGHEST = 8;
    public static final int PRIORITY_LOWEST = 0;
    private static final char[] PRIORITY_NAMES = "LLVDIWEA".toCharArray();
    private static final int TRACE_PRIORITY = 4;
    private static final int TRACE_STACK_POSITION = 4;
    private static final String TRACE_TAG = "TRACE";
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    private static FileOutputStream sLogWriter;

    private LogUtil() {
    }

    public static final void close() {
        closeFileLog();
    }

    private static final void closeFileLog() {
        synchronized (FILE_LOCK) {
            if (sLogWriter == null) {
                return;
            }
            try {
                sLogWriter.close();
                Log.i(GLOBAL_TAG, "log file is closed.");
                sLogWriter = null;
            } catch (IOException e) {
                Log.e(GLOBAL_TAG, "log file close error.", e);
                sLogWriter = null;
            } catch (Throwable th) {
                sLogWriter = null;
            }
        }
    }

    public static final int println(int priority, String tag, String msg) {
        if (tag == null) {
            tag = "";
        }
        if (msg == null) {
            msg = "";
        }
        int result = 0;
        if (priority >= 0) {
            result = Log.println(priority, new StringBuilder(GLOBAL_TAG).append(tag).toString(), msg);
        }
        if (priority >= 8) {
            saveToFile(priority, tag, msg);
        }
        return result;
    }

    private static final void saveToFile(int priority, String tag, String msg) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            try {
                if (sLogWriter == null) {
                    createLogFile();
                }
                sLogWriter.write(formatFileLog(priority, tag, msg).getBytes());
                sLogWriter.flush();
            } catch (Throwable t) {
                Log.w(GLOBAL_TAG, "LogUtils.saveToFile error.", t);
                closeFileLog();
            }
        }
    }

    private static final void createLogFile() {
        synchronized (FILE_LOCK) {
            try {
                File fileLog = new File(Environment.getExternalStorageDirectory(), LOG_FOLDER);
                fileLog.mkdirs();
                File file = new File(fileLog, getFileName());
                Log.d("POS_LOG.CREAT_LOG_FILE", "log file path: " + getFileName());
                sLogWriter = new FileOutputStream(file, true);
            } catch (IOException e) {
                throw new RuntimeException("create log file error.", e);
            }
        }
    }

    private static String getFileName() {
        StringBuilder fileName = new StringBuilder();
        fileName.append(new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(Calendar.getInstance().getTime()));
        fileName.append(".txt");
        return fileName.toString();
    }

    private static final String formatFileLog(int priority, String tag, String msg) {
        StringBuilder builder = new StringBuilder((tag.length() + msg.length()) + FORMAT_FILE_LOG_LENGTH);
        builder.append("[");
        builder.append(getTime());
        builder.append("][");
        builder.append(PRIORITY_NAMES[priority]);
        builder.append("][");
        builder.append(tag);
        builder.append("]: ");
        builder.append(msg);
        builder.append("\n");
        return builder.toString();
    }

    private static final String getTime() {
        return FORMATER.format(Calendar.getInstance().getTime());
    }

    public static final int m17v(String tag, String msg) {
        return println(2, tag, msg);
    }

    public static final int m12d(String tag, String msg) {
        return println(3, tag, msg);
    }

    public static final int m16i(String tag, String msg) {
        return println(4, tag, msg);
    }

    public static final int m18w(String tag, String msg) {
        return println(5, tag, msg);
    }

    public static final int m20w(Throwable tr) {
        return m18w(ERROR_TAG, Log.getStackTraceString(tr));
    }

    public static final int m19w(String tag, String msg, Throwable tr) {
        return m18w(tag, new StringBuilder(String.valueOf(msg)).append('\n').append(Log.getStackTraceString(tr)).toString());
    }

    public static final int m13e(String tag, String msg) {
        return println(6, tag, msg);
    }

    public static final int m14e(String tag, String msg, Throwable tr) {
        return m13e(tag, new StringBuilder(String.valueOf(msg)).append('\n').append(Log.getStackTraceString(tr)).toString());
    }

    public static final int m15e(Throwable e) {
        return m13e(ERROR_TAG, Log.getStackTraceString(e));
    }

    private static final String getTraceLog() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if (trace == null || trace.length < 5) {
            return "";
        }
        StackTraceElement ele = trace[4];
        String className = ele.getClassName();
        String simpleClassName = className.substring(className.lastIndexOf(46) + 1);
        String methodName = ele.getMethodName();
        Thread thread = Thread.currentThread();
        return getTraceLog(thread.getName() + "(" + thread.getId() + ")", simpleClassName, methodName);
    }

    private static final String getTraceLog(String threadName, String simpleClassName, String methodName) {
        StringBuilder sb = new StringBuilder(((threadName.length() + simpleClassName.length()) + methodName.length()) + METHOD_NAME_LENGTH);
        sb.append("[");
        sb.append(threadName);
        sb.append("][");
        sb.append(simpleClassName);
        sb.append(".");
        sb.append(methodName);
        sb.append("]");
        return sb.toString();
    }

    private static final String getAllTraces() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if (trace == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 4; i < trace.length; i++) {
            builder.append("\tat ");
            builder.append(trace[i].toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    public static final int trace() {
        return println(4, TRACE_TAG, getTraceLog());
    }

    public static final int trace(String msg) {
        return println(4, TRACE_TAG, getTraceLog() + msg);
    }

    public static final int traces() {
        return println(4, TRACE_TAG, getAllTraces());
    }
}
