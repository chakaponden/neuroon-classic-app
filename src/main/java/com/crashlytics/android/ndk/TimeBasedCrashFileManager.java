package com.crashlytics.android.ndk;

import io.fabric.sdk.android.services.common.CurrentTimeProvider;
import io.fabric.sdk.android.services.common.SystemCurrentTimeProvider;
import java.io.File;

class TimeBasedCrashFileManager implements CrashFileManager {
    private static final String CRASHFILE_EXT = ".ndk.json";
    private static final File[] EMPTY_FILES = new File[0];
    private final File nativeCrashDirectory;
    private final CurrentTimeProvider timeProvider;

    public TimeBasedCrashFileManager(File nativeCrashDirectory2) {
        this(nativeCrashDirectory2, new SystemCurrentTimeProvider());
    }

    TimeBasedCrashFileManager(File nativeCrashDirectory2, CurrentTimeProvider timeProvider2) {
        this.nativeCrashDirectory = nativeCrashDirectory2;
        this.timeProvider = timeProvider2;
    }

    public File getNewCrashFile() {
        return new File(this.nativeCrashDirectory, this.timeProvider.getCurrentTimeMillis() + CRASHFILE_EXT);
    }

    public File getLastWrittenCrashFile() {
        return findLatestCrashFileByValue();
    }

    public void clearCrashFiles() {
        for (File f : getFiles()) {
            f.delete();
        }
    }

    private File findLatestCrashFileByValue() {
        File latestFile = null;
        File[] files = getFiles();
        long highest = 0;
        for (File f : files) {
            long value = Long.parseLong(stripExtension(f.getName()));
            if (value > highest) {
                highest = value;
                latestFile = f;
            }
        }
        return latestFile;
    }

    private String stripExtension(String fileName) {
        return fileName.substring(0, fileName.length() - CRASHFILE_EXT.length());
    }

    private File[] getFiles() {
        File[] files = this.nativeCrashDirectory.listFiles();
        return files == null ? EMPTY_FILES : files;
    }
}
