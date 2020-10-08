package com.crashlytics.android.beta;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class BuildProperties {
    private static final String BUILD_ID = "build_id";
    private static final String PACKAGE_NAME = "package_name";
    private static final String VERSION_CODE = "version_code";
    private static final String VERSION_NAME = "version_name";
    public final String buildId;
    public final String packageName;
    public final String versionCode;
    public final String versionName;

    BuildProperties(String versionCode2, String versionName2, String buildId2, String packageName2) {
        this.versionCode = versionCode2;
        this.versionName = versionName2;
        this.buildId = buildId2;
        this.packageName = packageName2;
    }

    public static BuildProperties fromProperties(Properties props) {
        return new BuildProperties(props.getProperty(VERSION_CODE), props.getProperty(VERSION_NAME), props.getProperty(BUILD_ID), props.getProperty(PACKAGE_NAME));
    }

    public static BuildProperties fromPropertiesStream(InputStream is) throws IOException {
        Properties props = new Properties();
        props.load(is);
        return fromProperties(props);
    }
}
