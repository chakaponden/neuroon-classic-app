package com.inteliclinic.neuroon.mask;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.raizlabs.android.dbflow.sql.language.Condition;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MaskLogger {
    public static final String LOGS_LOCATION = "mask_log";
    private static MaskLogger mLogger;
    private StringBuffer data;
    private File fileHandler;
    private String filename;
    private Date logStart;
    private String mMaskId;

    public MaskLogger(String maskId) {
        this.mMaskId = maskId;
    }

    public static void prepare(Activity activity) {
        checkPermissions(activity);
    }

    private static boolean checkPermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, "android.permission.READ_CONTACTS")) {
            return false;
        }
        ActivityCompat.requestPermissions(activity, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
        return false;
    }

    public static MaskLogger start(Context context, String maskId) {
        if (getActualLogger() == null) {
            mLogger = new MaskLogger(maskId);
        }
        mLogger.openFile(context, maskId);
        log((DateFormat.getDateTimeInstance().format(new Date()) + ": Connected to " + maskId + "\r\n").getBytes());
        return mLogger;
    }

    public static MaskLogger getActualLogger() {
        return mLogger;
    }

    public static List<File> getLogFiles() {
        List<File> ret = new ArrayList<>();
        File myDir = new File(Environment.getExternalStorageDirectory().toString() + "/MaskConnections/");
        if (myDir.exists()) {
            File[] files = myDir.listFiles();
            Arrays.sort(files, new Comparator<File>() {
                public int compare(File lhs, File rhs) {
                    return Long.compare(lhs.lastModified(), rhs.lastModified());
                }
            });
            for (File file : files) {
                if (file.isFile()) {
                    ret.add(0, file);
                }
            }
        }
        return ret;
    }

    public static int log(byte[] logData) {
        if (mLogger != null) {
            return mLogger.logInt(logData);
        }
        return -1;
    }

    public void stop() {
        log((DateFormat.getDateTimeInstance().format(new Date()) + ": Disconnected with " + this.mMaskId + "\r\n").getBytes());
        this.mMaskId = null;
        this.fileHandler = null;
        mLogger = null;
    }

    private int logInt(byte[] logData) {
        if (this.fileHandler == null) {
            return -1;
        }
        try {
            FileOutputStream out = new FileOutputStream(this.fileHandler, true);
            out.write(logData);
            out.flush();
            out.close();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
    }

    private void openFile(Context context, String maskId) {
        File myDir = new File(Environment.getExternalStorageDirectory().toString() + "/MaskConnections/");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        this.fileHandler = new File(myDir, maskId + Condition.Operation.MINUS + DateFormat.getDateTimeInstance().format(new Date()) + ".txt");
        if (this.fileHandler.exists()) {
            this.fileHandler.delete();
        }
    }
}
