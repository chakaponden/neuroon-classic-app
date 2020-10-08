package com.google.android.gms.measurement.internal;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.WorkerThread;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzpz;
import com.google.android.gms.internal.zzqb;
import com.google.android.gms.internal.zzsn;
import com.inteliclinic.neuroon.models.data.SugarDatabase;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

class zze extends zzz {
    /* access modifiers changed from: private */
    public static final Map<String, String> zzaVB = new ArrayMap(13);
    private final zzc zzaVC = new zzc(getContext(), zzjQ());
    /* access modifiers changed from: private */
    public final zzaf zzaVD = new zzaf(zzjl());

    public static class zza {
        long zzaVE;
        long zzaVF;
        long zzaVG;
    }

    interface zzb {
        boolean zza(long j, zzqb.zzb zzb);

        void zzc(zzqb.zze zze);
    }

    private class zzc extends SQLiteOpenHelper {
        zzc(Context context, String str) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, 1);
        }

        @WorkerThread
        private void zza(SQLiteDatabase sQLiteDatabase, String str, String str2, String str3, Map<String, String> map) throws SQLiteException {
            if (!zza(sQLiteDatabase, str)) {
                sQLiteDatabase.execSQL(str2);
            }
            try {
                zza(sQLiteDatabase, str, str3, map);
            } catch (SQLiteException e) {
                zze.this.zzAo().zzCE().zzj("Failed to verify columns on table that was just created", str);
                throw e;
            }
        }

        @WorkerThread
        private void zza(SQLiteDatabase sQLiteDatabase, String str, String str2, Map<String, String> map) throws SQLiteException {
            Set<String> zzb = zzb(sQLiteDatabase, str);
            for (String str3 : str2.split(",")) {
                if (!zzb.remove(str3)) {
                    throw new SQLiteException("Table " + str + " is missing required column: " + str3);
                }
            }
            if (map != null) {
                for (Map.Entry next : map.entrySet()) {
                    if (!zzb.remove(next.getKey())) {
                        sQLiteDatabase.execSQL((String) next.getValue());
                    }
                }
            }
            if (!zzb.isEmpty()) {
                throw new SQLiteException("Table " + str + " table has extra columns");
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:16:0x0041  */
        @android.support.annotation.WorkerThread
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean zza(android.database.sqlite.SQLiteDatabase r11, java.lang.String r12) {
            /*
                r10 = this;
                r8 = 0
                r9 = 0
                java.lang.String r1 = "SQLITE_MASTER"
                r0 = 1
                java.lang.String[] r2 = new java.lang.String[r0]     // Catch:{ SQLiteException -> 0x0026, all -> 0x003e }
                r0 = 0
                java.lang.String r3 = "name"
                r2[r0] = r3     // Catch:{ SQLiteException -> 0x0026, all -> 0x003e }
                java.lang.String r3 = "name=?"
                r0 = 1
                java.lang.String[] r4 = new java.lang.String[r0]     // Catch:{ SQLiteException -> 0x0026, all -> 0x003e }
                r0 = 0
                r4[r0] = r12     // Catch:{ SQLiteException -> 0x0026, all -> 0x003e }
                r5 = 0
                r6 = 0
                r7 = 0
                r0 = r11
                android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ SQLiteException -> 0x0026, all -> 0x003e }
                boolean r0 = r1.moveToFirst()     // Catch:{ SQLiteException -> 0x0048 }
                if (r1 == 0) goto L_0x0025
                r1.close()
            L_0x0025:
                return r0
            L_0x0026:
                r0 = move-exception
                r1 = r9
            L_0x0028:
                com.google.android.gms.measurement.internal.zze r2 = com.google.android.gms.measurement.internal.zze.this     // Catch:{ all -> 0x0045 }
                com.google.android.gms.measurement.internal.zzp r2 = r2.zzAo()     // Catch:{ all -> 0x0045 }
                com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCF()     // Catch:{ all -> 0x0045 }
                java.lang.String r3 = "Error querying for table"
                r2.zze(r3, r12, r0)     // Catch:{ all -> 0x0045 }
                if (r1 == 0) goto L_0x003c
                r1.close()
            L_0x003c:
                r0 = r8
                goto L_0x0025
            L_0x003e:
                r0 = move-exception
            L_0x003f:
                if (r9 == 0) goto L_0x0044
                r9.close()
            L_0x0044:
                throw r0
            L_0x0045:
                r0 = move-exception
                r9 = r1
                goto L_0x003f
            L_0x0048:
                r0 = move-exception
                goto L_0x0028
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zze.zzc.zza(android.database.sqlite.SQLiteDatabase, java.lang.String):boolean");
        }

        @WorkerThread
        private Set<String> zzb(SQLiteDatabase sQLiteDatabase, String str) {
            HashSet hashSet = new HashSet();
            Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM " + str + " LIMIT 0", (String[]) null);
            try {
                Collections.addAll(hashSet, rawQuery.getColumnNames());
                return hashSet;
            } finally {
                rawQuery.close();
            }
        }

        @WorkerThread
        public SQLiteDatabase getWritableDatabase() {
            if (!zze.this.zzaVD.zzv(zze.this.zzCp().zzBN())) {
                throw new SQLiteException("Database open failed");
            }
            try {
                return super.getWritableDatabase();
            } catch (SQLiteException e) {
                zze.this.zzaVD.start();
                zze.this.zzAo().zzCE().zzfg("Opening the database failed, dropping and recreating it");
                zze.this.getContext().getDatabasePath(zze.this.zzjQ()).delete();
                try {
                    SQLiteDatabase writableDatabase = super.getWritableDatabase();
                    zze.this.zzaVD.clear();
                    return writableDatabase;
                } catch (SQLiteException e2) {
                    zze.this.zzAo().zzCE().zzj("Failed to open freshly created database", e2);
                    throw e2;
                }
            }
        }

        @WorkerThread
        public void onCreate(SQLiteDatabase database) {
            if (Build.VERSION.SDK_INT >= 9) {
                File file = new File(database.getPath());
                file.setReadable(false, false);
                file.setWritable(false, false);
                file.setReadable(true, true);
                file.setWritable(true, true);
            }
        }

        @WorkerThread
        public void onOpen(SQLiteDatabase database) {
            if (Build.VERSION.SDK_INT < 15) {
                Cursor rawQuery = database.rawQuery("PRAGMA journal_mode=memory", (String[]) null);
                try {
                    rawQuery.moveToFirst();
                } finally {
                    rawQuery.close();
                }
            }
            zza(database, "events", "CREATE TABLE IF NOT EXISTS events ( app_id TEXT NOT NULL, name TEXT NOT NULL, lifetime_count INTEGER NOT NULL, current_bundle_count INTEGER NOT NULL, last_fire_timestamp INTEGER NOT NULL, PRIMARY KEY (app_id, name)) ;", "app_id,name,lifetime_count,current_bundle_count,last_fire_timestamp", (Map<String, String>) null);
            zza(database, "user_attributes", "CREATE TABLE IF NOT EXISTS user_attributes ( app_id TEXT NOT NULL, name TEXT NOT NULL, set_timestamp INTEGER NOT NULL, value BLOB NOT NULL, PRIMARY KEY (app_id, name)) ;", "app_id,name,set_timestamp,value", (Map<String, String>) null);
            zza(database, "apps", "CREATE TABLE IF NOT EXISTS apps ( app_id TEXT NOT NULL, app_instance_id TEXT, gmp_app_id TEXT, resettable_device_id_hash TEXT, last_bundle_index INTEGER NOT NULL, last_bundle_end_timestamp INTEGER NOT NULL, PRIMARY KEY (app_id)) ;", "app_id,app_instance_id,gmp_app_id,resettable_device_id_hash,last_bundle_index,last_bundle_end_timestamp", zze.zzaVB);
            zza(database, "queue", "CREATE TABLE IF NOT EXISTS queue ( app_id TEXT NOT NULL, bundle_end_timestamp INTEGER NOT NULL, data BLOB NOT NULL);", "app_id,bundle_end_timestamp,data", (Map<String, String>) null);
            zza(database, "raw_events_metadata", "CREATE TABLE IF NOT EXISTS raw_events_metadata ( app_id TEXT NOT NULL, metadata_fingerprint INTEGER NOT NULL, metadata BLOB NOT NULL, PRIMARY KEY (app_id, metadata_fingerprint));", "app_id,metadata_fingerprint,metadata", (Map<String, String>) null);
            zza(database, "raw_events", "CREATE TABLE IF NOT EXISTS raw_events ( app_id TEXT NOT NULL, name TEXT NOT NULL, timestamp INTEGER NOT NULL, metadata_fingerprint INTEGER NOT NULL, data BLOB NOT NULL);", "app_id,name,timestamp,metadata_fingerprint,data", (Map<String, String>) null);
            zza(database, "event_filters", "CREATE TABLE IF NOT EXISTS event_filters ( app_id TEXT NOT NULL, audience_id INTEGER NOT NULL, filter_id INTEGER NOT NULL, event_name TEXT NOT NULL, data BLOB NOT NULL, PRIMARY KEY (app_id, event_name, audience_id, filter_id));", "app_id,audience_id,filter_id,event_name,data", (Map<String, String>) null);
            zza(database, "property_filters", "CREATE TABLE IF NOT EXISTS property_filters ( app_id TEXT NOT NULL, audience_id INTEGER NOT NULL, filter_id INTEGER NOT NULL, property_name TEXT NOT NULL, data BLOB NOT NULL, PRIMARY KEY (app_id, property_name, audience_id, filter_id));", "app_id,audience_id,filter_id,property_name,data", (Map<String, String>) null);
            zza(database, "audience_filter_values", "CREATE TABLE IF NOT EXISTS audience_filter_values ( app_id TEXT NOT NULL, audience_id INTEGER NOT NULL, current_results BLOB, PRIMARY KEY (app_id, audience_id));", "app_id,audience_id,current_results", (Map<String, String>) null);
        }

        @WorkerThread
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    static {
        zzaVB.put("app_version", "ALTER TABLE apps ADD COLUMN app_version TEXT;");
        zzaVB.put("app_store", "ALTER TABLE apps ADD COLUMN app_store TEXT;");
        zzaVB.put("gmp_version", "ALTER TABLE apps ADD COLUMN gmp_version INTEGER;");
        zzaVB.put("dev_cert_hash", "ALTER TABLE apps ADD COLUMN dev_cert_hash INTEGER;");
        zzaVB.put("measurement_enabled", "ALTER TABLE apps ADD COLUMN measurement_enabled INTEGER;");
        zzaVB.put("last_bundle_start_timestamp", "ALTER TABLE apps ADD COLUMN last_bundle_start_timestamp INTEGER;");
        zzaVB.put("day", "ALTER TABLE apps ADD COLUMN day INTEGER;");
        zzaVB.put("daily_public_events_count", "ALTER TABLE apps ADD COLUMN daily_public_events_count INTEGER;");
        zzaVB.put("daily_events_count", "ALTER TABLE apps ADD COLUMN daily_events_count INTEGER;");
        zzaVB.put("daily_conversions_count", "ALTER TABLE apps ADD COLUMN daily_conversions_count INTEGER;");
        zzaVB.put("remote_config", "ALTER TABLE apps ADD COLUMN remote_config BLOB;");
        zzaVB.put("config_fetched_time", "ALTER TABLE apps ADD COLUMN config_fetched_time INTEGER;");
        zzaVB.put("failed_config_fetch_time", "ALTER TABLE apps ADD COLUMN failed_config_fetch_time INTEGER;");
    }

    zze(zzw zzw) {
        super(zzw);
    }

    private boolean zzCw() {
        return getContext().getDatabasePath(zzjQ()).exists();
    }

    @WorkerThread
    @TargetApi(11)
    static int zza(Cursor cursor, int i) {
        if (Build.VERSION.SDK_INT >= 11) {
            return cursor.getType(i);
        }
        CursorWindow window = ((SQLiteCursor) cursor).getWindow();
        int position = cursor.getPosition();
        if (window.isNull(position, i)) {
            return 0;
        }
        if (window.isLong(position, i)) {
            return 1;
        }
        if (window.isFloat(position, i)) {
            return 2;
        }
        if (window.isString(position, i)) {
            return 3;
        }
        return window.isBlob(position, i) ? 4 : -1;
    }

    @WorkerThread
    private long zza(String str, String[] strArr, long j) {
        Cursor cursor = null;
        try {
            Cursor rawQuery = getWritableDatabase().rawQuery(str, strArr);
            if (rawQuery.moveToFirst()) {
                j = rawQuery.getLong(0);
                if (rawQuery != null) {
                    rawQuery.close();
                }
            } else if (rawQuery != null) {
                rawQuery.close();
            }
            return j;
        } catch (SQLiteException e) {
            zzAo().zzCE().zze("Database error", str, e);
            throw e;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x008f  */
    /* JADX WARNING: Removed duplicated region for block: B:41:? A[RETURN, SYNTHETIC] */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void zza(java.lang.String r8, com.google.android.gms.internal.zzpz.zza r9) {
        /*
            r7 = this;
            r0 = 0
            r7.zzjv()
            r7.zzjk()
            com.google.android.gms.common.internal.zzx.zzcM(r8)
            com.google.android.gms.common.internal.zzx.zzz(r9)
            com.google.android.gms.internal.zzpz$zzb[] r1 = r9.zzaZt
            com.google.android.gms.common.internal.zzx.zzz(r1)
            com.google.android.gms.internal.zzpz$zze[] r1 = r9.zzaZs
            com.google.android.gms.common.internal.zzx.zzz(r1)
            java.lang.Integer r1 = r9.zzaZr
            if (r1 != 0) goto L_0x0029
            com.google.android.gms.measurement.internal.zzp r0 = r7.zzAo()
            com.google.android.gms.measurement.internal.zzp$zza r0 = r0.zzCF()
            java.lang.String r1 = "Audience with no ID"
            r0.zzfg(r1)
        L_0x0028:
            return
        L_0x0029:
            java.lang.Integer r1 = r9.zzaZr
            int r3 = r1.intValue()
            com.google.android.gms.internal.zzpz$zzb[] r2 = r9.zzaZt
            int r4 = r2.length
            r1 = r0
        L_0x0033:
            if (r1 >= r4) goto L_0x004e
            r5 = r2[r1]
            java.lang.Integer r5 = r5.zzaZv
            if (r5 != 0) goto L_0x004b
            com.google.android.gms.measurement.internal.zzp r0 = r7.zzAo()
            com.google.android.gms.measurement.internal.zzp$zza r0 = r0.zzCF()
            java.lang.String r1 = "Event filter with no ID. Audience definition ignored. appId, audienceId"
            java.lang.Integer r2 = r9.zzaZr
            r0.zze(r1, r8, r2)
            goto L_0x0028
        L_0x004b:
            int r1 = r1 + 1
            goto L_0x0033
        L_0x004e:
            com.google.android.gms.internal.zzpz$zze[] r2 = r9.zzaZs
            int r4 = r2.length
            r1 = r0
        L_0x0052:
            if (r1 >= r4) goto L_0x006d
            r5 = r2[r1]
            java.lang.Integer r5 = r5.zzaZv
            if (r5 != 0) goto L_0x006a
            com.google.android.gms.measurement.internal.zzp r0 = r7.zzAo()
            com.google.android.gms.measurement.internal.zzp$zza r0 = r0.zzCF()
            java.lang.String r1 = "Property filter with no ID. Audience definition ignored. appId, audienceId"
            java.lang.Integer r2 = r9.zzaZr
            r0.zze(r1, r8, r2)
            goto L_0x0028
        L_0x006a:
            int r1 = r1 + 1
            goto L_0x0052
        L_0x006d:
            r1 = 1
            com.google.android.gms.internal.zzpz$zzb[] r4 = r9.zzaZt
            int r5 = r4.length
            r2 = r0
        L_0x0072:
            if (r2 >= r5) goto L_0x007d
            r6 = r4[r2]
            boolean r6 = r7.zza((java.lang.String) r8, (int) r3, (com.google.android.gms.internal.zzpz.zzb) r6)
            if (r6 != 0) goto L_0x0093
            r1 = r0
        L_0x007d:
            if (r1 == 0) goto L_0x0099
            com.google.android.gms.internal.zzpz$zze[] r4 = r9.zzaZs
            int r5 = r4.length
            r2 = r0
        L_0x0083:
            if (r2 >= r5) goto L_0x0099
            r6 = r4[r2]
            boolean r6 = r7.zza((java.lang.String) r8, (int) r3, (com.google.android.gms.internal.zzpz.zze) r6)
            if (r6 != 0) goto L_0x0096
        L_0x008d:
            if (r0 != 0) goto L_0x0028
            r7.zzB(r8, r3)
            goto L_0x0028
        L_0x0093:
            int r2 = r2 + 1
            goto L_0x0072
        L_0x0096:
            int r2 = r2 + 1
            goto L_0x0083
        L_0x0099:
            r0 = r1
            goto L_0x008d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zze.zza(java.lang.String, com.google.android.gms.internal.zzpz$zza):void");
    }

    @WorkerThread
    private boolean zza(String str, int i, zzpz.zzb zzb2) {
        zzjv();
        zzjk();
        zzx.zzcM(str);
        zzx.zzz(zzb2);
        if (TextUtils.isEmpty(zzb2.zzaZw)) {
            zzAo().zzCF().zze("Event filter had no event name. Audience definition ignored. audienceId, filterId", Integer.valueOf(i), String.valueOf(zzb2.zzaZv));
            return false;
        }
        try {
            byte[] bArr = new byte[zzb2.getSerializedSize()];
            zzsn zzE = zzsn.zzE(bArr);
            zzb2.writeTo(zzE);
            zzE.zzJo();
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("audience_id", Integer.valueOf(i));
            contentValues.put("filter_id", zzb2.zzaZv);
            contentValues.put("event_name", zzb2.zzaZw);
            contentValues.put(SugarDatabase.NAME, bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("event_filters", (String) null, contentValues, 5) == -1) {
                    zzAo().zzCE().zzfg("Failed to insert event filter (got -1)");
                }
                return true;
            } catch (SQLiteException e) {
                zzAo().zzCE().zzj("Error storing event filter", e);
                return false;
            }
        } catch (IOException e2) {
            zzAo().zzCE().zzj("Configuration loss. Failed to serialize event filter", e2);
            return false;
        }
    }

    @WorkerThread
    private boolean zza(String str, int i, zzpz.zze zze) {
        zzjv();
        zzjk();
        zzx.zzcM(str);
        zzx.zzz(zze);
        if (TextUtils.isEmpty(zze.zzaZL)) {
            zzAo().zzCF().zze("Property filter had no property name. Audience definition ignored. audienceId, filterId", Integer.valueOf(i), String.valueOf(zze.zzaZv));
            return false;
        }
        try {
            byte[] bArr = new byte[zze.getSerializedSize()];
            zzsn zzE = zzsn.zzE(bArr);
            zze.writeTo(zzE);
            zzE.zzJo();
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("audience_id", Integer.valueOf(i));
            contentValues.put("filter_id", zze.zzaZv);
            contentValues.put("property_name", zze.zzaZL);
            contentValues.put(SugarDatabase.NAME, bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("property_filters", (String) null, contentValues, 5) != -1) {
                    return true;
                }
                zzAo().zzCE().zzfg("Failed to insert property filter (got -1)");
                return false;
            } catch (SQLiteException e) {
                zzAo().zzCE().zzj("Error storing property filter", e);
                return false;
            }
        } catch (IOException e2) {
            zzAo().zzCE().zzj("Configuration loss. Failed to serialize property filter", e2);
            return false;
        }
    }

    @WorkerThread
    private long zzb(String str, String[] strArr) {
        Cursor cursor = null;
        try {
            cursor = getWritableDatabase().rawQuery(str, strArr);
            if (cursor.moveToFirst()) {
                long j = cursor.getLong(0);
                if (cursor != null) {
                    cursor.close();
                }
                return j;
            }
            throw new SQLiteException("Database returned empty set");
        } catch (SQLiteException e) {
            zzAo().zzCE().zze("Database error", str, e);
            throw e;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    /* access modifiers changed from: private */
    public String zzjQ() {
        if (!zzCp().zzkr()) {
            return zzCp().zzkR();
        }
        if (zzCp().zzks()) {
            return zzCp().zzkR();
        }
        zzAo().zzCG().zzfg("Using secondary database");
        return zzCp().zzkS();
    }

    @WorkerThread
    public void beginTransaction() {
        zzjv();
        getWritableDatabase().beginTransaction();
    }

    @WorkerThread
    public void endTransaction() {
        zzjv();
        getWritableDatabase().endTransaction();
    }

    /* access modifiers changed from: package-private */
    @WorkerThread
    public SQLiteDatabase getWritableDatabase() {
        zzjk();
        try {
            return this.zzaVC.getWritableDatabase();
        } catch (SQLiteException e) {
            zzAo().zzCF().zzj("Error opening database", e);
            throw e;
        }
    }

    @WorkerThread
    public void setTransactionSuccessful() {
        zzjv();
        getWritableDatabase().setTransactionSuccessful();
    }

    @WorkerThread
    public void zzA(String str, int i) {
        zzx.zzcM(str);
        zzjk();
        zzjv();
        try {
            getWritableDatabase().execSQL("delete from user_attributes where app_id=? and name in (select name from user_attributes where app_id=? and name like '_ltv_%' order by set_timestamp desc limit ?,10);", new String[]{str, str, String.valueOf(i)});
        } catch (SQLiteException e) {
            zzAo().zzCE().zze("Error pruning currencies", str, e);
        }
    }

    /* access modifiers changed from: package-private */
    public void zzB(String str, int i) {
        zzjv();
        zzjk();
        zzx.zzcM(str);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete("property_filters", "app_id=? and audience_id=?", new String[]{str, String.valueOf(i)});
        writableDatabase.delete("event_filters", "app_id=? and audience_id=?", new String[]{str, String.valueOf(i)});
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x007a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.android.gms.internal.zzqb.zzf zzC(java.lang.String r10, int r11) {
        /*
            r9 = this;
            r8 = 0
            r9.zzjv()
            r9.zzjk()
            com.google.android.gms.common.internal.zzx.zzcM(r10)
            android.database.sqlite.SQLiteDatabase r0 = r9.getWritableDatabase()
            java.lang.String r1 = "audience_filter_values"
            r2 = 1
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ SQLiteException -> 0x0080, all -> 0x0076 }
            r3 = 0
            java.lang.String r4 = "current_results"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0080, all -> 0x0076 }
            java.lang.String r3 = "app_id=? AND audience_id=?"
            r4 = 2
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ SQLiteException -> 0x0080, all -> 0x0076 }
            r5 = 0
            r4[r5] = r10     // Catch:{ SQLiteException -> 0x0080, all -> 0x0076 }
            r5 = 1
            java.lang.String r6 = java.lang.String.valueOf(r11)     // Catch:{ SQLiteException -> 0x0080, all -> 0x0076 }
            r4[r5] = r6     // Catch:{ SQLiteException -> 0x0080, all -> 0x0076 }
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ SQLiteException -> 0x0080, all -> 0x0076 }
            boolean r0 = r1.moveToFirst()     // Catch:{ SQLiteException -> 0x0061 }
            if (r0 != 0) goto L_0x003b
            if (r1 == 0) goto L_0x0039
            r1.close()
        L_0x0039:
            r0 = r8
        L_0x003a:
            return r0
        L_0x003b:
            r0 = 0
            byte[] r0 = r1.getBlob(r0)     // Catch:{ SQLiteException -> 0x0061 }
            com.google.android.gms.internal.zzsm r2 = com.google.android.gms.internal.zzsm.zzD(r0)     // Catch:{ SQLiteException -> 0x0061 }
            com.google.android.gms.internal.zzqb$zzf r0 = new com.google.android.gms.internal.zzqb$zzf     // Catch:{ SQLiteException -> 0x0061 }
            r0.<init>()     // Catch:{ SQLiteException -> 0x0061 }
            r0.mergeFrom(r2)     // Catch:{ IOException -> 0x0052 }
        L_0x004c:
            if (r1 == 0) goto L_0x003a
            r1.close()
            goto L_0x003a
        L_0x0052:
            r2 = move-exception
            com.google.android.gms.measurement.internal.zzp r3 = r9.zzAo()     // Catch:{ SQLiteException -> 0x0061 }
            com.google.android.gms.measurement.internal.zzp$zza r3 = r3.zzCE()     // Catch:{ SQLiteException -> 0x0061 }
            java.lang.String r4 = "Failed to merge filter results"
            r3.zze(r4, r10, r2)     // Catch:{ SQLiteException -> 0x0061 }
            goto L_0x004c
        L_0x0061:
            r0 = move-exception
        L_0x0062:
            com.google.android.gms.measurement.internal.zzp r2 = r9.zzAo()     // Catch:{ all -> 0x007e }
            com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCE()     // Catch:{ all -> 0x007e }
            java.lang.String r3 = "Database error querying filter results"
            r2.zzj(r3, r0)     // Catch:{ all -> 0x007e }
            if (r1 == 0) goto L_0x0074
            r1.close()
        L_0x0074:
            r0 = r8
            goto L_0x003a
        L_0x0076:
            r0 = move-exception
            r1 = r8
        L_0x0078:
            if (r1 == 0) goto L_0x007d
            r1.close()
        L_0x007d:
            throw r0
        L_0x007e:
            r0 = move-exception
            goto L_0x0078
        L_0x0080:
            r0 = move-exception
            r1 = r8
            goto L_0x0062
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zze.zzC(java.lang.String, int):com.google.android.gms.internal.zzqb$zzf");
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x003d  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String zzCq() {
        /*
            r5 = this;
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r5.getWritableDatabase()
            java.lang.String r2 = "select app_id from queue where app_id not in (select app_id from apps where measurement_enabled=0) order by rowid limit 1;"
            r3 = 0
            android.database.Cursor r2 = r1.rawQuery(r2, r3)     // Catch:{ SQLiteException -> 0x0023, all -> 0x0038 }
            boolean r1 = r2.moveToFirst()     // Catch:{ SQLiteException -> 0x0043 }
            if (r1 == 0) goto L_0x001d
            r1 = 0
            java.lang.String r0 = r2.getString(r1)     // Catch:{ SQLiteException -> 0x0043 }
            if (r2 == 0) goto L_0x001c
            r2.close()
        L_0x001c:
            return r0
        L_0x001d:
            if (r2 == 0) goto L_0x001c
            r2.close()
            goto L_0x001c
        L_0x0023:
            r1 = move-exception
            r2 = r0
        L_0x0025:
            com.google.android.gms.measurement.internal.zzp r3 = r5.zzAo()     // Catch:{ all -> 0x0041 }
            com.google.android.gms.measurement.internal.zzp$zza r3 = r3.zzCE()     // Catch:{ all -> 0x0041 }
            java.lang.String r4 = "Database error getting next bundle app id"
            r3.zzj(r4, r1)     // Catch:{ all -> 0x0041 }
            if (r2 == 0) goto L_0x001c
            r2.close()
            goto L_0x001c
        L_0x0038:
            r1 = move-exception
            r2 = r0
            r0 = r1
        L_0x003b:
            if (r2 == 0) goto L_0x0040
            r2.close()
        L_0x0040:
            throw r0
        L_0x0041:
            r0 = move-exception
            goto L_0x003b
        L_0x0043:
            r1 = move-exception
            goto L_0x0025
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zze.zzCq():java.lang.String");
    }

    /* access modifiers changed from: package-private */
    @WorkerThread
    public void zzCr() {
        zzjk();
        zzjv();
        if (zzCw()) {
            long j = zzCo().zzaXm.get();
            long elapsedRealtime = zzjl().elapsedRealtime();
            if (Math.abs(elapsedRealtime - j) > zzCp().zzBR()) {
                zzCo().zzaXm.set(elapsedRealtime);
                zzCs();
            }
        }
    }

    /* access modifiers changed from: package-private */
    @WorkerThread
    public void zzCs() {
        int delete;
        zzjk();
        zzjv();
        if (zzCw() && (delete = getWritableDatabase().delete("queue", "abs(bundle_end_timestamp - ?) > cast(? as integer)", new String[]{String.valueOf(zzjl().currentTimeMillis()), String.valueOf(zzCp().zzBQ())})) > 0) {
            zzAo().zzCK().zzj("Deleted stale rows. rowsDeleted", Integer.valueOf(delete));
        }
    }

    @WorkerThread
    public long zzCt() {
        return zza("select max(bundle_end_timestamp) from queue", (String[]) null, 0);
    }

    @WorkerThread
    public long zzCu() {
        return zza("select max(timestamp) from raw_events", (String[]) null, 0);
    }

    public boolean zzCv() {
        return zzb("select count(1) > 0 from raw_events", (String[]) null) != 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x008c  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.android.gms.measurement.internal.zzi zzI(java.lang.String r13, java.lang.String r14) {
        /*
            r12 = this;
            r10 = 0
            com.google.android.gms.common.internal.zzx.zzcM(r13)
            com.google.android.gms.common.internal.zzx.zzcM(r14)
            r12.zzjk()
            r12.zzjv()
            android.database.sqlite.SQLiteDatabase r0 = r12.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0073, all -> 0x0089 }
            java.lang.String r1 = "events"
            r2 = 3
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ SQLiteException -> 0x0073, all -> 0x0089 }
            r3 = 0
            java.lang.String r4 = "lifetime_count"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0073, all -> 0x0089 }
            r3 = 1
            java.lang.String r4 = "current_bundle_count"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0073, all -> 0x0089 }
            r3 = 2
            java.lang.String r4 = "last_fire_timestamp"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0073, all -> 0x0089 }
            java.lang.String r3 = "app_id=? and name=?"
            r4 = 2
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ SQLiteException -> 0x0073, all -> 0x0089 }
            r5 = 0
            r4[r5] = r13     // Catch:{ SQLiteException -> 0x0073, all -> 0x0089 }
            r5 = 1
            r4[r5] = r14     // Catch:{ SQLiteException -> 0x0073, all -> 0x0089 }
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r11 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ SQLiteException -> 0x0073, all -> 0x0089 }
            boolean r0 = r11.moveToFirst()     // Catch:{ SQLiteException -> 0x0096, all -> 0x0090 }
            if (r0 != 0) goto L_0x0044
            if (r11 == 0) goto L_0x0042
            r11.close()
        L_0x0042:
            r1 = r10
        L_0x0043:
            return r1
        L_0x0044:
            r0 = 0
            long r4 = r11.getLong(r0)     // Catch:{ SQLiteException -> 0x0096, all -> 0x0090 }
            r0 = 1
            long r6 = r11.getLong(r0)     // Catch:{ SQLiteException -> 0x0096, all -> 0x0090 }
            r0 = 2
            long r8 = r11.getLong(r0)     // Catch:{ SQLiteException -> 0x0096, all -> 0x0090 }
            com.google.android.gms.measurement.internal.zzi r1 = new com.google.android.gms.measurement.internal.zzi     // Catch:{ SQLiteException -> 0x0096, all -> 0x0090 }
            r2 = r13
            r3 = r14
            r1.<init>(r2, r3, r4, r6, r8)     // Catch:{ SQLiteException -> 0x0096, all -> 0x0090 }
            boolean r0 = r11.moveToNext()     // Catch:{ SQLiteException -> 0x0096, all -> 0x0090 }
            if (r0 == 0) goto L_0x006d
            com.google.android.gms.measurement.internal.zzp r0 = r12.zzAo()     // Catch:{ SQLiteException -> 0x0096, all -> 0x0090 }
            com.google.android.gms.measurement.internal.zzp$zza r0 = r0.zzCE()     // Catch:{ SQLiteException -> 0x0096, all -> 0x0090 }
            java.lang.String r2 = "Got multiple records for event aggregates, expected one"
            r0.zzfg(r2)     // Catch:{ SQLiteException -> 0x0096, all -> 0x0090 }
        L_0x006d:
            if (r11 == 0) goto L_0x0043
            r11.close()
            goto L_0x0043
        L_0x0073:
            r0 = move-exception
            r1 = r10
        L_0x0075:
            com.google.android.gms.measurement.internal.zzp r2 = r12.zzAo()     // Catch:{ all -> 0x0093 }
            com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCE()     // Catch:{ all -> 0x0093 }
            java.lang.String r3 = "Error querying events"
            r2.zzd(r3, r13, r14, r0)     // Catch:{ all -> 0x0093 }
            if (r1 == 0) goto L_0x0087
            r1.close()
        L_0x0087:
            r1 = r10
            goto L_0x0043
        L_0x0089:
            r0 = move-exception
        L_0x008a:
            if (r10 == 0) goto L_0x008f
            r10.close()
        L_0x008f:
            throw r0
        L_0x0090:
            r0 = move-exception
            r10 = r11
            goto L_0x008a
        L_0x0093:
            r0 = move-exception
            r10 = r1
            goto L_0x008a
        L_0x0096:
            r0 = move-exception
            r1 = r11
            goto L_0x0075
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zze.zzI(java.lang.String, java.lang.String):com.google.android.gms.measurement.internal.zzi");
    }

    @WorkerThread
    public void zzJ(String str, String str2) {
        zzx.zzcM(str);
        zzx.zzcM(str2);
        zzjk();
        zzjv();
        try {
            zzAo().zzCK().zzj("Deleted user attribute rows:", Integer.valueOf(getWritableDatabase().delete("user_attributes", "app_id=? and name=?", new String[]{str, str2})));
        } catch (SQLiteException e) {
            zzAo().zzCE().zzd("Error deleting user attribute", str, str2, e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0082  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.android.gms.measurement.internal.zzai zzK(java.lang.String r10, java.lang.String r11) {
        /*
            r9 = this;
            r8 = 0
            com.google.android.gms.common.internal.zzx.zzcM(r10)
            com.google.android.gms.common.internal.zzx.zzcM(r11)
            r9.zzjk()
            r9.zzjv()
            android.database.sqlite.SQLiteDatabase r0 = r9.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0069, all -> 0x007f }
            java.lang.String r1 = "user_attributes"
            r2 = 2
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ SQLiteException -> 0x0069, all -> 0x007f }
            r3 = 0
            java.lang.String r4 = "set_timestamp"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0069, all -> 0x007f }
            r3 = 1
            java.lang.String r4 = "value"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0069, all -> 0x007f }
            java.lang.String r3 = "app_id=? and name=?"
            r4 = 2
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ SQLiteException -> 0x0069, all -> 0x007f }
            r5 = 0
            r4[r5] = r10     // Catch:{ SQLiteException -> 0x0069, all -> 0x007f }
            r5 = 1
            r4[r5] = r11     // Catch:{ SQLiteException -> 0x0069, all -> 0x007f }
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r7 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ SQLiteException -> 0x0069, all -> 0x007f }
            boolean r0 = r7.moveToFirst()     // Catch:{ SQLiteException -> 0x008c, all -> 0x0086 }
            if (r0 != 0) goto L_0x003f
            if (r7 == 0) goto L_0x003d
            r7.close()
        L_0x003d:
            r1 = r8
        L_0x003e:
            return r1
        L_0x003f:
            r0 = 0
            long r4 = r7.getLong(r0)     // Catch:{ SQLiteException -> 0x008c, all -> 0x0086 }
            r0 = 1
            java.lang.Object r6 = r9.zzb((android.database.Cursor) r7, (int) r0)     // Catch:{ SQLiteException -> 0x008c, all -> 0x0086 }
            com.google.android.gms.measurement.internal.zzai r1 = new com.google.android.gms.measurement.internal.zzai     // Catch:{ SQLiteException -> 0x008c, all -> 0x0086 }
            r2 = r10
            r3 = r11
            r1.<init>(r2, r3, r4, r6)     // Catch:{ SQLiteException -> 0x008c, all -> 0x0086 }
            boolean r0 = r7.moveToNext()     // Catch:{ SQLiteException -> 0x008c, all -> 0x0086 }
            if (r0 == 0) goto L_0x0063
            com.google.android.gms.measurement.internal.zzp r0 = r9.zzAo()     // Catch:{ SQLiteException -> 0x008c, all -> 0x0086 }
            com.google.android.gms.measurement.internal.zzp$zza r0 = r0.zzCE()     // Catch:{ SQLiteException -> 0x008c, all -> 0x0086 }
            java.lang.String r2 = "Got multiple records for user property, expected one"
            r0.zzfg(r2)     // Catch:{ SQLiteException -> 0x008c, all -> 0x0086 }
        L_0x0063:
            if (r7 == 0) goto L_0x003e
            r7.close()
            goto L_0x003e
        L_0x0069:
            r0 = move-exception
            r1 = r8
        L_0x006b:
            com.google.android.gms.measurement.internal.zzp r2 = r9.zzAo()     // Catch:{ all -> 0x0089 }
            com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCE()     // Catch:{ all -> 0x0089 }
            java.lang.String r3 = "Error querying user property"
            r2.zzd(r3, r10, r11, r0)     // Catch:{ all -> 0x0089 }
            if (r1 == 0) goto L_0x007d
            r1.close()
        L_0x007d:
            r1 = r8
            goto L_0x003e
        L_0x007f:
            r0 = move-exception
        L_0x0080:
            if (r8 == 0) goto L_0x0085
            r8.close()
        L_0x0085:
            throw r0
        L_0x0086:
            r0 = move-exception
            r8 = r7
            goto L_0x0080
        L_0x0089:
            r0 = move-exception
            r8 = r1
            goto L_0x0080
        L_0x008c:
            r0 = move-exception
            r1 = r7
            goto L_0x006b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zze.zzK(java.lang.String, java.lang.String):com.google.android.gms.measurement.internal.zzai");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00ad  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Map<java.lang.Integer, java.util.List<com.google.android.gms.internal.zzpz.zzb>> zzL(java.lang.String r11, java.lang.String r12) {
        /*
            r10 = this;
            r9 = 0
            r10.zzjv()
            r10.zzjk()
            com.google.android.gms.common.internal.zzx.zzcM(r11)
            com.google.android.gms.common.internal.zzx.zzcM(r12)
            android.support.v4.util.ArrayMap r8 = new android.support.v4.util.ArrayMap
            r8.<init>()
            android.database.sqlite.SQLiteDatabase r0 = r10.getWritableDatabase()
            java.lang.String r1 = "event_filters"
            r2 = 2
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ SQLiteException -> 0x00b3, all -> 0x00a9 }
            r3 = 0
            java.lang.String r4 = "audience_id"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x00b3, all -> 0x00a9 }
            r3 = 1
            java.lang.String r4 = "data"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x00b3, all -> 0x00a9 }
            java.lang.String r3 = "app_id=? AND event_name=?"
            r4 = 2
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ SQLiteException -> 0x00b3, all -> 0x00a9 }
            r5 = 0
            r4[r5] = r11     // Catch:{ SQLiteException -> 0x00b3, all -> 0x00a9 }
            r5 = 1
            r4[r5] = r12     // Catch:{ SQLiteException -> 0x00b3, all -> 0x00a9 }
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ SQLiteException -> 0x00b3, all -> 0x00a9 }
            boolean r0 = r1.moveToFirst()     // Catch:{ SQLiteException -> 0x0094 }
            if (r0 != 0) goto L_0x0047
            java.util.Map r0 = java.util.Collections.emptyMap()     // Catch:{ SQLiteException -> 0x0094 }
            if (r1 == 0) goto L_0x0046
            r1.close()
        L_0x0046:
            return r0
        L_0x0047:
            r0 = 1
            byte[] r0 = r1.getBlob(r0)     // Catch:{ SQLiteException -> 0x0094 }
            com.google.android.gms.internal.zzsm r0 = com.google.android.gms.internal.zzsm.zzD(r0)     // Catch:{ SQLiteException -> 0x0094 }
            com.google.android.gms.internal.zzpz$zzb r2 = new com.google.android.gms.internal.zzpz$zzb     // Catch:{ SQLiteException -> 0x0094 }
            r2.<init>()     // Catch:{ SQLiteException -> 0x0094 }
            r2.mergeFrom(r0)     // Catch:{ IOException -> 0x0085 }
            r0 = 0
            int r3 = r1.getInt(r0)     // Catch:{ SQLiteException -> 0x0094 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r3)     // Catch:{ SQLiteException -> 0x0094 }
            java.lang.Object r0 = r8.get(r0)     // Catch:{ SQLiteException -> 0x0094 }
            java.util.List r0 = (java.util.List) r0     // Catch:{ SQLiteException -> 0x0094 }
            if (r0 != 0) goto L_0x0075
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ SQLiteException -> 0x0094 }
            r0.<init>()     // Catch:{ SQLiteException -> 0x0094 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ SQLiteException -> 0x0094 }
            r8.put(r3, r0)     // Catch:{ SQLiteException -> 0x0094 }
        L_0x0075:
            r0.add(r2)     // Catch:{ SQLiteException -> 0x0094 }
        L_0x0078:
            boolean r0 = r1.moveToNext()     // Catch:{ SQLiteException -> 0x0094 }
            if (r0 != 0) goto L_0x0047
            if (r1 == 0) goto L_0x0083
            r1.close()
        L_0x0083:
            r0 = r8
            goto L_0x0046
        L_0x0085:
            r0 = move-exception
            com.google.android.gms.measurement.internal.zzp r2 = r10.zzAo()     // Catch:{ SQLiteException -> 0x0094 }
            com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCE()     // Catch:{ SQLiteException -> 0x0094 }
            java.lang.String r3 = "Failed to merge filter"
            r2.zze(r3, r11, r0)     // Catch:{ SQLiteException -> 0x0094 }
            goto L_0x0078
        L_0x0094:
            r0 = move-exception
        L_0x0095:
            com.google.android.gms.measurement.internal.zzp r2 = r10.zzAo()     // Catch:{ all -> 0x00b1 }
            com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCE()     // Catch:{ all -> 0x00b1 }
            java.lang.String r3 = "Database error querying filters"
            r2.zzj(r3, r0)     // Catch:{ all -> 0x00b1 }
            if (r1 == 0) goto L_0x00a7
            r1.close()
        L_0x00a7:
            r0 = r9
            goto L_0x0046
        L_0x00a9:
            r0 = move-exception
            r1 = r9
        L_0x00ab:
            if (r1 == 0) goto L_0x00b0
            r1.close()
        L_0x00b0:
            throw r0
        L_0x00b1:
            r0 = move-exception
            goto L_0x00ab
        L_0x00b3:
            r0 = move-exception
            r1 = r9
            goto L_0x0095
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zze.zzL(java.lang.String, java.lang.String):java.util.Map");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00ad  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Map<java.lang.Integer, java.util.List<com.google.android.gms.internal.zzpz.zze>> zzM(java.lang.String r11, java.lang.String r12) {
        /*
            r10 = this;
            r9 = 0
            r10.zzjv()
            r10.zzjk()
            com.google.android.gms.common.internal.zzx.zzcM(r11)
            com.google.android.gms.common.internal.zzx.zzcM(r12)
            android.support.v4.util.ArrayMap r8 = new android.support.v4.util.ArrayMap
            r8.<init>()
            android.database.sqlite.SQLiteDatabase r0 = r10.getWritableDatabase()
            java.lang.String r1 = "property_filters"
            r2 = 2
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ SQLiteException -> 0x00b3, all -> 0x00a9 }
            r3 = 0
            java.lang.String r4 = "audience_id"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x00b3, all -> 0x00a9 }
            r3 = 1
            java.lang.String r4 = "data"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x00b3, all -> 0x00a9 }
            java.lang.String r3 = "app_id=? AND property_name=?"
            r4 = 2
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ SQLiteException -> 0x00b3, all -> 0x00a9 }
            r5 = 0
            r4[r5] = r11     // Catch:{ SQLiteException -> 0x00b3, all -> 0x00a9 }
            r5 = 1
            r4[r5] = r12     // Catch:{ SQLiteException -> 0x00b3, all -> 0x00a9 }
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ SQLiteException -> 0x00b3, all -> 0x00a9 }
            boolean r0 = r1.moveToFirst()     // Catch:{ SQLiteException -> 0x0094 }
            if (r0 != 0) goto L_0x0047
            java.util.Map r0 = java.util.Collections.emptyMap()     // Catch:{ SQLiteException -> 0x0094 }
            if (r1 == 0) goto L_0x0046
            r1.close()
        L_0x0046:
            return r0
        L_0x0047:
            r0 = 1
            byte[] r0 = r1.getBlob(r0)     // Catch:{ SQLiteException -> 0x0094 }
            com.google.android.gms.internal.zzsm r0 = com.google.android.gms.internal.zzsm.zzD(r0)     // Catch:{ SQLiteException -> 0x0094 }
            com.google.android.gms.internal.zzpz$zze r2 = new com.google.android.gms.internal.zzpz$zze     // Catch:{ SQLiteException -> 0x0094 }
            r2.<init>()     // Catch:{ SQLiteException -> 0x0094 }
            r2.mergeFrom(r0)     // Catch:{ IOException -> 0x0085 }
            r0 = 0
            int r3 = r1.getInt(r0)     // Catch:{ SQLiteException -> 0x0094 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r3)     // Catch:{ SQLiteException -> 0x0094 }
            java.lang.Object r0 = r8.get(r0)     // Catch:{ SQLiteException -> 0x0094 }
            java.util.List r0 = (java.util.List) r0     // Catch:{ SQLiteException -> 0x0094 }
            if (r0 != 0) goto L_0x0075
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ SQLiteException -> 0x0094 }
            r0.<init>()     // Catch:{ SQLiteException -> 0x0094 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ SQLiteException -> 0x0094 }
            r8.put(r3, r0)     // Catch:{ SQLiteException -> 0x0094 }
        L_0x0075:
            r0.add(r2)     // Catch:{ SQLiteException -> 0x0094 }
        L_0x0078:
            boolean r0 = r1.moveToNext()     // Catch:{ SQLiteException -> 0x0094 }
            if (r0 != 0) goto L_0x0047
            if (r1 == 0) goto L_0x0083
            r1.close()
        L_0x0083:
            r0 = r8
            goto L_0x0046
        L_0x0085:
            r0 = move-exception
            com.google.android.gms.measurement.internal.zzp r2 = r10.zzAo()     // Catch:{ SQLiteException -> 0x0094 }
            com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCE()     // Catch:{ SQLiteException -> 0x0094 }
            java.lang.String r3 = "Failed to merge filter"
            r2.zze(r3, r11, r0)     // Catch:{ SQLiteException -> 0x0094 }
            goto L_0x0078
        L_0x0094:
            r0 = move-exception
        L_0x0095:
            com.google.android.gms.measurement.internal.zzp r2 = r10.zzAo()     // Catch:{ all -> 0x00b1 }
            com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCE()     // Catch:{ all -> 0x00b1 }
            java.lang.String r3 = "Database error querying filters"
            r2.zzj(r3, r0)     // Catch:{ all -> 0x00b1 }
            if (r1 == 0) goto L_0x00a7
            r1.close()
        L_0x00a7:
            r0 = r9
            goto L_0x0046
        L_0x00a9:
            r0 = move-exception
            r1 = r9
        L_0x00ab:
            if (r1 == 0) goto L_0x00b0
            r1.close()
        L_0x00b0:
            throw r0
        L_0x00b1:
            r0 = move-exception
            goto L_0x00ab
        L_0x00b3:
            r0 = move-exception
            r1 = r9
            goto L_0x0095
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zze.zzM(java.lang.String, java.lang.String):java.util.Map");
    }

    @WorkerThread
    public void zzZ(long j) {
        zzjk();
        zzjv();
        if (getWritableDatabase().delete("queue", "rowid=?", new String[]{String.valueOf(j)}) != 1) {
            zzAo().zzCE().zzfg("Deleted fewer rows from queue than expected");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x00ea  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.android.gms.measurement.internal.zze.zza zza(long r12, java.lang.String r14, boolean r15, boolean r16) {
        /*
            r11 = this;
            com.google.android.gms.common.internal.zzx.zzcM(r14)
            r11.zzjk()
            r11.zzjv()
            r0 = 1
            java.lang.String[] r10 = new java.lang.String[r0]
            r0 = 0
            r10[r0] = r14
            com.google.android.gms.measurement.internal.zze$zza r8 = new com.google.android.gms.measurement.internal.zze$zza
            r8.<init>()
            r9 = 0
            android.database.sqlite.SQLiteDatabase r0 = r11.getWritableDatabase()     // Catch:{ SQLiteException -> 0x00cf, all -> 0x00e6 }
            java.lang.String r1 = "apps"
            r2 = 4
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ SQLiteException -> 0x00cf, all -> 0x00e6 }
            r3 = 0
            java.lang.String r4 = "day"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x00cf, all -> 0x00e6 }
            r3 = 1
            java.lang.String r4 = "daily_events_count"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x00cf, all -> 0x00e6 }
            r3 = 2
            java.lang.String r4 = "daily_public_events_count"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x00cf, all -> 0x00e6 }
            r3 = 3
            java.lang.String r4 = "daily_conversions_count"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x00cf, all -> 0x00e6 }
            java.lang.String r3 = "app_id=?"
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ SQLiteException -> 0x00cf, all -> 0x00e6 }
            r5 = 0
            r4[r5] = r14     // Catch:{ SQLiteException -> 0x00cf, all -> 0x00e6 }
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ SQLiteException -> 0x00cf, all -> 0x00e6 }
            boolean r2 = r1.moveToFirst()     // Catch:{ SQLiteException -> 0x00f0 }
            if (r2 != 0) goto L_0x005b
            com.google.android.gms.measurement.internal.zzp r0 = r11.zzAo()     // Catch:{ SQLiteException -> 0x00f0 }
            com.google.android.gms.measurement.internal.zzp$zza r0 = r0.zzCF()     // Catch:{ SQLiteException -> 0x00f0 }
            java.lang.String r2 = "Not updating daily counts, app is not known"
            r0.zzj(r2, r14)     // Catch:{ SQLiteException -> 0x00f0 }
            if (r1 == 0) goto L_0x0059
            r1.close()
        L_0x0059:
            r0 = r8
        L_0x005a:
            return r0
        L_0x005b:
            r2 = 0
            long r2 = r1.getLong(r2)     // Catch:{ SQLiteException -> 0x00f0 }
            int r2 = (r2 > r12 ? 1 : (r2 == r12 ? 0 : -1))
            if (r2 != 0) goto L_0x0079
            r2 = 1
            long r2 = r1.getLong(r2)     // Catch:{ SQLiteException -> 0x00f0 }
            r8.zzaVF = r2     // Catch:{ SQLiteException -> 0x00f0 }
            r2 = 2
            long r2 = r1.getLong(r2)     // Catch:{ SQLiteException -> 0x00f0 }
            r8.zzaVE = r2     // Catch:{ SQLiteException -> 0x00f0 }
            r2 = 3
            long r2 = r1.getLong(r2)     // Catch:{ SQLiteException -> 0x00f0 }
            r8.zzaVG = r2     // Catch:{ SQLiteException -> 0x00f0 }
        L_0x0079:
            long r2 = r8.zzaVF     // Catch:{ SQLiteException -> 0x00f0 }
            r4 = 1
            long r2 = r2 + r4
            r8.zzaVF = r2     // Catch:{ SQLiteException -> 0x00f0 }
            if (r15 == 0) goto L_0x0089
            long r2 = r8.zzaVE     // Catch:{ SQLiteException -> 0x00f0 }
            r4 = 1
            long r2 = r2 + r4
            r8.zzaVE = r2     // Catch:{ SQLiteException -> 0x00f0 }
        L_0x0089:
            if (r16 == 0) goto L_0x0092
            long r2 = r8.zzaVG     // Catch:{ SQLiteException -> 0x00f0 }
            r4 = 1
            long r2 = r2 + r4
            r8.zzaVG = r2     // Catch:{ SQLiteException -> 0x00f0 }
        L_0x0092:
            android.content.ContentValues r2 = new android.content.ContentValues     // Catch:{ SQLiteException -> 0x00f0 }
            r2.<init>()     // Catch:{ SQLiteException -> 0x00f0 }
            java.lang.String r3 = "day"
            java.lang.Long r4 = java.lang.Long.valueOf(r12)     // Catch:{ SQLiteException -> 0x00f0 }
            r2.put(r3, r4)     // Catch:{ SQLiteException -> 0x00f0 }
            java.lang.String r3 = "daily_public_events_count"
            long r4 = r8.zzaVE     // Catch:{ SQLiteException -> 0x00f0 }
            java.lang.Long r4 = java.lang.Long.valueOf(r4)     // Catch:{ SQLiteException -> 0x00f0 }
            r2.put(r3, r4)     // Catch:{ SQLiteException -> 0x00f0 }
            java.lang.String r3 = "daily_events_count"
            long r4 = r8.zzaVF     // Catch:{ SQLiteException -> 0x00f0 }
            java.lang.Long r4 = java.lang.Long.valueOf(r4)     // Catch:{ SQLiteException -> 0x00f0 }
            r2.put(r3, r4)     // Catch:{ SQLiteException -> 0x00f0 }
            java.lang.String r3 = "daily_conversions_count"
            long r4 = r8.zzaVG     // Catch:{ SQLiteException -> 0x00f0 }
            java.lang.Long r4 = java.lang.Long.valueOf(r4)     // Catch:{ SQLiteException -> 0x00f0 }
            r2.put(r3, r4)     // Catch:{ SQLiteException -> 0x00f0 }
            java.lang.String r3 = "apps"
            java.lang.String r4 = "app_id=?"
            r0.update(r3, r2, r4, r10)     // Catch:{ SQLiteException -> 0x00f0 }
            if (r1 == 0) goto L_0x00cd
            r1.close()
        L_0x00cd:
            r0 = r8
            goto L_0x005a
        L_0x00cf:
            r0 = move-exception
            r1 = r9
        L_0x00d1:
            com.google.android.gms.measurement.internal.zzp r2 = r11.zzAo()     // Catch:{ all -> 0x00ee }
            com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCE()     // Catch:{ all -> 0x00ee }
            java.lang.String r3 = "Error updating daily counts"
            r2.zzj(r3, r0)     // Catch:{ all -> 0x00ee }
            if (r1 == 0) goto L_0x00e3
            r1.close()
        L_0x00e3:
            r0 = r8
            goto L_0x005a
        L_0x00e6:
            r0 = move-exception
            r1 = r9
        L_0x00e8:
            if (r1 == 0) goto L_0x00ed
            r1.close()
        L_0x00ed:
            throw r0
        L_0x00ee:
            r0 = move-exception
            goto L_0x00e8
        L_0x00f0:
            r0 = move-exception
            goto L_0x00d1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zze.zza(long, java.lang.String, boolean, boolean):com.google.android.gms.measurement.internal.zze$zza");
    }

    /* access modifiers changed from: package-private */
    @WorkerThread
    public void zza(ContentValues contentValues, String str, Object obj) {
        zzx.zzcM(str);
        zzx.zzz(obj);
        if (obj instanceof String) {
            contentValues.put(str, (String) obj);
        } else if (obj instanceof Long) {
            contentValues.put(str, (Long) obj);
        } else if (obj instanceof Float) {
            contentValues.put(str, (Float) obj);
        } else {
            throw new IllegalArgumentException("Invalid value type");
        }
    }

    @WorkerThread
    public void zza(zzqb.zze zze) {
        zzjk();
        zzjv();
        zzx.zzz(zze);
        zzx.zzcM(zze.appId);
        zzx.zzz(zze.zzbaq);
        zzCr();
        long currentTimeMillis = zzjl().currentTimeMillis();
        if (zze.zzbaq.longValue() < currentTimeMillis - zzCp().zzBQ() || zze.zzbaq.longValue() > zzCp().zzBQ() + currentTimeMillis) {
            zzAo().zzCF().zze("Storing bundle outside of the max uploading time span. now, timestamp", Long.valueOf(currentTimeMillis), zze.zzbaq);
        }
        try {
            byte[] bArr = new byte[zze.getSerializedSize()];
            zzsn zzE = zzsn.zzE(bArr);
            zze.writeTo(zzE);
            zzE.zzJo();
            byte[] zzg = zzCk().zzg(bArr);
            zzAo().zzCK().zzj("Saving bundle, size", Integer.valueOf(zzg.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", zze.appId);
            contentValues.put("bundle_end_timestamp", zze.zzbaq);
            contentValues.put(SugarDatabase.NAME, zzg);
            try {
                if (getWritableDatabase().insert("queue", (String) null, contentValues) == -1) {
                    zzAo().zzCE().zzfg("Failed to insert bundle (got -1)");
                }
            } catch (SQLiteException e) {
                zzAo().zzCE().zzj("Error storing bundle", e);
            }
        } catch (IOException e2) {
            zzAo().zzCE().zzj("Data loss. Failed to serialize bundle", e2);
        }
    }

    @WorkerThread
    public void zza(zza zza2) {
        zzx.zzz(zza2);
        zzjk();
        zzjv();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zza2.zzwK());
        contentValues.put("app_instance_id", zza2.zzBj());
        contentValues.put("gmp_app_id", zza2.zzBk());
        contentValues.put("resettable_device_id_hash", zza2.zzBl());
        contentValues.put("last_bundle_index", Long.valueOf(zza2.zzBr()));
        contentValues.put("last_bundle_start_timestamp", Long.valueOf(zza2.zzBm()));
        contentValues.put("last_bundle_end_timestamp", Long.valueOf(zza2.zzBn()));
        contentValues.put("app_version", zza2.zzli());
        contentValues.put("app_store", zza2.zzBo());
        contentValues.put("gmp_version", Long.valueOf(zza2.zzBp()));
        contentValues.put("dev_cert_hash", Long.valueOf(zza2.zzBq()));
        contentValues.put("measurement_enabled", Boolean.valueOf(zza2.zzAr()));
        contentValues.put("day", Long.valueOf(zza2.zzBv()));
        contentValues.put("daily_public_events_count", Long.valueOf(zza2.zzBw()));
        contentValues.put("daily_events_count", Long.valueOf(zza2.zzBx()));
        contentValues.put("daily_conversions_count", Long.valueOf(zza2.zzBy()));
        contentValues.put("config_fetched_time", Long.valueOf(zza2.zzBs()));
        contentValues.put("failed_config_fetch_time", Long.valueOf(zza2.zzBt()));
        try {
            if (getWritableDatabase().insertWithOnConflict("apps", (String) null, contentValues, 5) == -1) {
                zzAo().zzCE().zzfg("Failed to insert/update app (got -1)");
            }
        } catch (SQLiteException e) {
            zzAo().zzCE().zzj("Error storing app", e);
        }
    }

    public void zza(zzh zzh, long j) {
        zzjk();
        zzjv();
        zzx.zzz(zzh);
        zzx.zzcM(zzh.zzaUa);
        zzqb.zzb zzb2 = new zzqb.zzb();
        zzb2.zzbag = Long.valueOf(zzh.zzaVN);
        zzb2.zzbae = new zzqb.zzc[zzh.zzaVO.size()];
        Iterator<String> it = zzh.zzaVO.iterator();
        int i = 0;
        while (it.hasNext()) {
            String next = it.next();
            zzqb.zzc zzc2 = new zzqb.zzc();
            zzb2.zzbae[i] = zzc2;
            zzc2.name = next;
            zzCk().zza(zzc2, zzh.zzaVO.get(next));
            i++;
        }
        try {
            byte[] bArr = new byte[zzb2.getSerializedSize()];
            zzsn zzE = zzsn.zzE(bArr);
            zzb2.writeTo(zzE);
            zzE.zzJo();
            zzAo().zzCK().zze("Saving event, name, data size", zzh.mName, Integer.valueOf(bArr.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", zzh.zzaUa);
            contentValues.put("name", zzh.mName);
            contentValues.put("timestamp", Long.valueOf(zzh.zzaez));
            contentValues.put("metadata_fingerprint", Long.valueOf(j));
            contentValues.put(SugarDatabase.NAME, bArr);
            try {
                if (getWritableDatabase().insert("raw_events", (String) null, contentValues) == -1) {
                    zzAo().zzCE().zzfg("Failed to insert raw event (got -1)");
                }
            } catch (SQLiteException e) {
                zzAo().zzCE().zzj("Error storing raw event", e);
            }
        } catch (IOException e2) {
            zzAo().zzCE().zzj("Data loss. Failed to serialize event params/data", e2);
        }
    }

    @WorkerThread
    public void zza(zzi zzi) {
        zzx.zzz(zzi);
        zzjk();
        zzjv();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzi.zzaUa);
        contentValues.put("name", zzi.mName);
        contentValues.put("lifetime_count", Long.valueOf(zzi.zzaVP));
        contentValues.put("current_bundle_count", Long.valueOf(zzi.zzaVQ));
        contentValues.put("last_fire_timestamp", Long.valueOf(zzi.zzaVR));
        try {
            if (getWritableDatabase().insertWithOnConflict("events", (String) null, contentValues, 5) == -1) {
                zzAo().zzCE().zzfg("Failed to insert/update event aggregates (got -1)");
            }
        } catch (SQLiteException e) {
            zzAo().zzCE().zzj("Error storing event aggregates", e);
        }
    }

    /* access modifiers changed from: package-private */
    public void zza(String str, int i, zzqb.zzf zzf) {
        zzjv();
        zzjk();
        zzx.zzcM(str);
        zzx.zzz(zzf);
        try {
            byte[] bArr = new byte[zzf.getSerializedSize()];
            zzsn zzE = zzsn.zzE(bArr);
            zzf.writeTo(zzE);
            zzE.zzJo();
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("audience_id", Integer.valueOf(i));
            contentValues.put("current_results", bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("audience_filter_values", (String) null, contentValues, 5) == -1) {
                    zzAo().zzCE().zzfg("Failed to insert filter results (got -1)");
                }
            } catch (SQLiteException e) {
                zzAo().zzCE().zzj("Error storing filter results", e);
            }
        } catch (IOException e2) {
            zzAo().zzCE().zzj("Configuration loss. Failed to serialize filter results", e2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0174, code lost:
        r2 = e;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0174 A[ExcHandler: SQLiteException (e android.database.sqlite.SQLiteException), Splitter:B:1:0x000a] */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x018d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void zza(java.lang.String r15, long r16, com.google.android.gms.measurement.internal.zze.zzb r18) {
        /*
            r14 = this;
            r3 = 0
            com.google.android.gms.common.internal.zzx.zzz(r18)
            r14.zzjk()
            r14.zzjv()
            android.database.sqlite.SQLiteDatabase r2 = r14.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0174, all -> 0x0189 }
            boolean r4 = android.text.TextUtils.isEmpty(r15)     // Catch:{ SQLiteException -> 0x0174, all -> 0x0189 }
            if (r4 == 0) goto L_0x0077
            java.lang.String r4 = "select app_id, metadata_fingerprint from raw_events where app_id in (select app_id from apps where config_fetched_time >= ?) order by rowid limit 1;"
            r5 = 1
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch:{ SQLiteException -> 0x0174, all -> 0x0189 }
            r6 = 0
            java.lang.String r7 = java.lang.String.valueOf(r16)     // Catch:{ SQLiteException -> 0x0174, all -> 0x0189 }
            r5[r6] = r7     // Catch:{ SQLiteException -> 0x0174, all -> 0x0189 }
            android.database.Cursor r3 = r2.rawQuery(r4, r5)     // Catch:{ SQLiteException -> 0x0174, all -> 0x0189 }
            boolean r4 = r3.moveToFirst()     // Catch:{ SQLiteException -> 0x0174 }
            if (r4 != 0) goto L_0x0030
            if (r3 == 0) goto L_0x002f
            r3.close()
        L_0x002f:
            return
        L_0x0030:
            r4 = 0
            java.lang.String r15 = r3.getString(r4)     // Catch:{ SQLiteException -> 0x0174 }
            r4 = 1
            java.lang.String r4 = r3.getString(r4)     // Catch:{ SQLiteException -> 0x0174 }
            r3.close()     // Catch:{ SQLiteException -> 0x0174 }
            r12 = r4
            r11 = r3
        L_0x003f:
            java.lang.String r3 = "raw_events_metadata"
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            r5 = 0
            java.lang.String r6 = "metadata"
            r4[r5] = r6     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            java.lang.String r5 = "app_id=? and metadata_fingerprint=?"
            r6 = 2
            java.lang.String[] r6 = new java.lang.String[r6]     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            r7 = 0
            r6[r7] = r15     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            r7 = 1
            r6[r7] = r12     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            r7 = 0
            r8 = 0
            java.lang.String r9 = "rowid"
            java.lang.String r10 = "2"
            android.database.Cursor r11 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10)     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            boolean r3 = r11.moveToFirst()     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            if (r3 != 0) goto L_0x009a
            com.google.android.gms.measurement.internal.zzp r2 = r14.zzAo()     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCE()     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            java.lang.String r3 = "Raw event metadata record is missing"
            r2.zzfg(r3)     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            if (r11 == 0) goto L_0x002f
            r11.close()
            goto L_0x002f
        L_0x0077:
            java.lang.String r4 = "select metadata_fingerprint from raw_events where app_id = ? order by rowid limit 1;"
            r5 = 1
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch:{ SQLiteException -> 0x0174, all -> 0x0189 }
            r6 = 0
            r5[r6] = r15     // Catch:{ SQLiteException -> 0x0174, all -> 0x0189 }
            android.database.Cursor r3 = r2.rawQuery(r4, r5)     // Catch:{ SQLiteException -> 0x0174, all -> 0x0189 }
            boolean r4 = r3.moveToFirst()     // Catch:{ SQLiteException -> 0x0174 }
            if (r4 != 0) goto L_0x008f
            if (r3 == 0) goto L_0x002f
            r3.close()
            goto L_0x002f
        L_0x008f:
            r4 = 0
            java.lang.String r4 = r3.getString(r4)     // Catch:{ SQLiteException -> 0x0174 }
            r3.close()     // Catch:{ SQLiteException -> 0x0174 }
            r12 = r4
            r11 = r3
            goto L_0x003f
        L_0x009a:
            r3 = 0
            byte[] r3 = r11.getBlob(r3)     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            com.google.android.gms.internal.zzsm r3 = com.google.android.gms.internal.zzsm.zzD(r3)     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            com.google.android.gms.internal.zzqb$zze r4 = new com.google.android.gms.internal.zzqb$zze     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            r4.<init>()     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            r4.mergeFrom(r3)     // Catch:{ IOException -> 0x010d }
            boolean r3 = r11.moveToNext()     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            if (r3 == 0) goto L_0x00be
            com.google.android.gms.measurement.internal.zzp r3 = r14.zzAo()     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            com.google.android.gms.measurement.internal.zzp$zza r3 = r3.zzCF()     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            java.lang.String r5 = "Get multiple raw event metadata records, expected one"
            r3.zzfg(r5)     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
        L_0x00be:
            r11.close()     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            r0 = r18
            r0.zzc(r4)     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            java.lang.String r3 = "raw_events"
            r4 = 4
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            r5 = 0
            java.lang.String r6 = "rowid"
            r4[r5] = r6     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            r5 = 1
            java.lang.String r6 = "name"
            r4[r5] = r6     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            r5 = 2
            java.lang.String r6 = "timestamp"
            r4[r5] = r6     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            r5 = 3
            java.lang.String r6 = "data"
            r4[r5] = r6     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            java.lang.String r5 = "app_id=? and metadata_fingerprint=?"
            r6 = 2
            java.lang.String[] r6 = new java.lang.String[r6]     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            r7 = 0
            r6[r7] = r15     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            r7 = 1
            r6[r7] = r12     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            r7 = 0
            r8 = 0
            java.lang.String r9 = "rowid"
            r10 = 0
            android.database.Cursor r3 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10)     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            boolean r2 = r3.moveToFirst()     // Catch:{ SQLiteException -> 0x0174 }
            if (r2 != 0) goto L_0x0122
            com.google.android.gms.measurement.internal.zzp r2 = r14.zzAo()     // Catch:{ SQLiteException -> 0x0174 }
            com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCF()     // Catch:{ SQLiteException -> 0x0174 }
            java.lang.String r4 = "Raw event data disappeared while in transaction"
            r2.zzfg(r4)     // Catch:{ SQLiteException -> 0x0174 }
            if (r3 == 0) goto L_0x002f
            r3.close()
            goto L_0x002f
        L_0x010d:
            r2 = move-exception
            com.google.android.gms.measurement.internal.zzp r3 = r14.zzAo()     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            com.google.android.gms.measurement.internal.zzp$zza r3 = r3.zzCE()     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            java.lang.String r4 = "Data loss. Failed to merge raw event metadata"
            r3.zze(r4, r15, r2)     // Catch:{ SQLiteException -> 0x0196, all -> 0x0194 }
            if (r11 == 0) goto L_0x002f
            r11.close()
            goto L_0x002f
        L_0x0122:
            r2 = 0
            long r4 = r3.getLong(r2)     // Catch:{ SQLiteException -> 0x0174 }
            r2 = 3
            byte[] r2 = r3.getBlob(r2)     // Catch:{ SQLiteException -> 0x0174 }
            com.google.android.gms.internal.zzsm r2 = com.google.android.gms.internal.zzsm.zzD(r2)     // Catch:{ SQLiteException -> 0x0174 }
            com.google.android.gms.internal.zzqb$zzb r6 = new com.google.android.gms.internal.zzqb$zzb     // Catch:{ SQLiteException -> 0x0174 }
            r6.<init>()     // Catch:{ SQLiteException -> 0x0174 }
            r6.mergeFrom(r2)     // Catch:{ IOException -> 0x0159 }
            r2 = 1
            java.lang.String r2 = r3.getString(r2)     // Catch:{ SQLiteException -> 0x0174 }
            r6.name = r2     // Catch:{ SQLiteException -> 0x0174 }
            r2 = 2
            long r8 = r3.getLong(r2)     // Catch:{ SQLiteException -> 0x0174 }
            java.lang.Long r2 = java.lang.Long.valueOf(r8)     // Catch:{ SQLiteException -> 0x0174 }
            r6.zzbaf = r2     // Catch:{ SQLiteException -> 0x0174 }
            r0 = r18
            boolean r2 = r0.zza(r4, r6)     // Catch:{ SQLiteException -> 0x0174 }
            if (r2 != 0) goto L_0x0167
            if (r3 == 0) goto L_0x002f
            r3.close()
            goto L_0x002f
        L_0x0159:
            r2 = move-exception
            com.google.android.gms.measurement.internal.zzp r4 = r14.zzAo()     // Catch:{ SQLiteException -> 0x0174 }
            com.google.android.gms.measurement.internal.zzp$zza r4 = r4.zzCE()     // Catch:{ SQLiteException -> 0x0174 }
            java.lang.String r5 = "Data loss. Failed to merge raw event"
            r4.zze(r5, r15, r2)     // Catch:{ SQLiteException -> 0x0174 }
        L_0x0167:
            boolean r2 = r3.moveToNext()     // Catch:{ SQLiteException -> 0x0174 }
            if (r2 != 0) goto L_0x0122
            if (r3 == 0) goto L_0x002f
            r3.close()
            goto L_0x002f
        L_0x0174:
            r2 = move-exception
        L_0x0175:
            com.google.android.gms.measurement.internal.zzp r4 = r14.zzAo()     // Catch:{ all -> 0x0191 }
            com.google.android.gms.measurement.internal.zzp$zza r4 = r4.zzCE()     // Catch:{ all -> 0x0191 }
            java.lang.String r5 = "Data loss. Error selecting raw event"
            r4.zzj(r5, r2)     // Catch:{ all -> 0x0191 }
            if (r3 == 0) goto L_0x002f
            r3.close()
            goto L_0x002f
        L_0x0189:
            r2 = move-exception
            r11 = r3
        L_0x018b:
            if (r11 == 0) goto L_0x0190
            r11.close()
        L_0x0190:
            throw r2
        L_0x0191:
            r2 = move-exception
            r11 = r3
            goto L_0x018b
        L_0x0194:
            r2 = move-exception
            goto L_0x018b
        L_0x0196:
            r2 = move-exception
            r3 = r11
            goto L_0x0175
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zze.zza(java.lang.String, long, com.google.android.gms.measurement.internal.zze$zzb):void");
    }

    @WorkerThread
    public boolean zza(zzai zzai) {
        zzx.zzz(zzai);
        zzjk();
        zzjv();
        if (zzK(zzai.zzaUa, zzai.mName) == null) {
            if (zzaj.zzfq(zzai.mName)) {
                if (zzb("select count(1) from user_attributes where app_id=? and name not like '!_%' escape '!'", new String[]{zzai.zzaUa}) >= ((long) zzCp().zzBL())) {
                    return false;
                }
            } else {
                if (zzb("select count(1) from user_attributes where app_id=?", new String[]{zzai.zzaUa}) >= ((long) zzCp().zzBM())) {
                    return false;
                }
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzai.zzaUa);
        contentValues.put("name", zzai.mName);
        contentValues.put("set_timestamp", Long.valueOf(zzai.zzaZp));
        zza(contentValues, "value", zzai.zzNc);
        try {
            if (getWritableDatabase().insertWithOnConflict("user_attributes", (String) null, contentValues, 5) == -1) {
                zzAo().zzCE().zzfg("Failed to insert/update user property (got -1)");
            }
        } catch (SQLiteException e) {
            zzAo().zzCE().zzj("Error storing user property", e);
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x0059  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String zzaa(long r8) {
        /*
            r7 = this;
            r0 = 0
            r7.zzjk()
            r7.zzjv()
            android.database.sqlite.SQLiteDatabase r1 = r7.getWritableDatabase()     // Catch:{ SQLiteException -> 0x003f, all -> 0x0054 }
            java.lang.String r2 = "select app_id from apps where app_id in (select distinct app_id from raw_events) and config_fetched_time < ? order by failed_config_fetch_time limit 1;"
            r3 = 1
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch:{ SQLiteException -> 0x003f, all -> 0x0054 }
            r4 = 0
            java.lang.String r5 = java.lang.String.valueOf(r8)     // Catch:{ SQLiteException -> 0x003f, all -> 0x0054 }
            r3[r4] = r5     // Catch:{ SQLiteException -> 0x003f, all -> 0x0054 }
            android.database.Cursor r2 = r1.rawQuery(r2, r3)     // Catch:{ SQLiteException -> 0x003f, all -> 0x0054 }
            boolean r1 = r2.moveToFirst()     // Catch:{ SQLiteException -> 0x005f }
            if (r1 != 0) goto L_0x0034
            com.google.android.gms.measurement.internal.zzp r1 = r7.zzAo()     // Catch:{ SQLiteException -> 0x005f }
            com.google.android.gms.measurement.internal.zzp$zza r1 = r1.zzCK()     // Catch:{ SQLiteException -> 0x005f }
            java.lang.String r3 = "No expired configs for apps with pending events"
            r1.zzfg(r3)     // Catch:{ SQLiteException -> 0x005f }
            if (r2 == 0) goto L_0x0033
            r2.close()
        L_0x0033:
            return r0
        L_0x0034:
            r1 = 0
            java.lang.String r0 = r2.getString(r1)     // Catch:{ SQLiteException -> 0x005f }
            if (r2 == 0) goto L_0x0033
            r2.close()
            goto L_0x0033
        L_0x003f:
            r1 = move-exception
            r2 = r0
        L_0x0041:
            com.google.android.gms.measurement.internal.zzp r3 = r7.zzAo()     // Catch:{ all -> 0x005d }
            com.google.android.gms.measurement.internal.zzp$zza r3 = r3.zzCE()     // Catch:{ all -> 0x005d }
            java.lang.String r4 = "Error selecting expired configs"
            r3.zzj(r4, r1)     // Catch:{ all -> 0x005d }
            if (r2 == 0) goto L_0x0033
            r2.close()
            goto L_0x0033
        L_0x0054:
            r1 = move-exception
            r2 = r0
            r0 = r1
        L_0x0057:
            if (r2 == 0) goto L_0x005c
            r2.close()
        L_0x005c:
            throw r0
        L_0x005d:
            r0 = move-exception
            goto L_0x0057
        L_0x005f:
            r1 = move-exception
            goto L_0x0041
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zze.zzaa(long):java.lang.String");
    }

    public long zzb(zzqb.zze zze) throws IOException {
        zzjk();
        zzjv();
        zzx.zzz(zze);
        zzx.zzcM(zze.appId);
        try {
            byte[] bArr = new byte[zze.getSerializedSize()];
            zzsn zzE = zzsn.zzE(bArr);
            zze.writeTo(zzE);
            zzE.zzJo();
            long zzr = zzCk().zzr(bArr);
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", zze.appId);
            contentValues.put("metadata_fingerprint", Long.valueOf(zzr));
            contentValues.put("metadata", bArr);
            try {
                getWritableDatabase().insertWithOnConflict("raw_events_metadata", (String) null, contentValues, 4);
                return zzr;
            } catch (SQLiteException e) {
                zzAo().zzCE().zzj("Error storing raw event metadata", e);
                throw e;
            }
        } catch (IOException e2) {
            zzAo().zzCE().zzj("Data loss. Failed to serialize event metadata", e2);
            throw e2;
        }
    }

    /* access modifiers changed from: package-private */
    @WorkerThread
    public Object zzb(Cursor cursor, int i) {
        int zza2 = zza(cursor, i);
        switch (zza2) {
            case 0:
                zzAo().zzCE().zzfg("Loaded invalid null value from database");
                return null;
            case 1:
                return Long.valueOf(cursor.getLong(i));
            case 2:
                return Float.valueOf(cursor.getFloat(i));
            case 3:
                return cursor.getString(i);
            case 4:
                zzAo().zzCE().zzfg("Loaded invalid blob type value, ignoring it");
                return null;
            default:
                zzAo().zzCE().zzj("Loaded invalid unknown value type, ignoring it", Integer.valueOf(zza2));
                return null;
        }
    }

    /* access modifiers changed from: package-private */
    @WorkerThread
    public void zzb(String str, zzpz.zza[] zzaArr) {
        zzjv();
        zzjk();
        zzx.zzcM(str);
        zzx.zzz(zzaArr);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            zzfb(str);
            for (zzpz.zza zza2 : zzaArr) {
                zza(str, zza2);
            }
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    @WorkerThread
    public void zzd(String str, byte[] bArr) {
        zzx.zzcM(str);
        zzjk();
        zzjv();
        ContentValues contentValues = new ContentValues();
        contentValues.put("remote_config", bArr);
        try {
            if (((long) getWritableDatabase().update("apps", contentValues, "app_id = ?", new String[]{str})) == 0) {
                zzAo().zzCE().zzfg("Failed to update remote config (got 0)");
            }
        } catch (SQLiteException e) {
            zzAo().zzCE().zzj("Error storing remote config", e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x009e  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<com.google.android.gms.measurement.internal.zzai> zzeX(java.lang.String r12) {
        /*
            r11 = this;
            r10 = 0
            com.google.android.gms.common.internal.zzx.zzcM(r12)
            r11.zzjk()
            r11.zzjv()
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            android.database.sqlite.SQLiteDatabase r0 = r11.getWritableDatabase()     // Catch:{ SQLiteException -> 0x00a8, all -> 0x009b }
            java.lang.String r1 = "user_attributes"
            r2 = 3
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ SQLiteException -> 0x00a8, all -> 0x009b }
            r3 = 0
            java.lang.String r4 = "name"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x00a8, all -> 0x009b }
            r3 = 1
            java.lang.String r4 = "set_timestamp"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x00a8, all -> 0x009b }
            r3 = 2
            java.lang.String r4 = "value"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x00a8, all -> 0x009b }
            java.lang.String r3 = "app_id=?"
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ SQLiteException -> 0x00a8, all -> 0x009b }
            r5 = 0
            r4[r5] = r12     // Catch:{ SQLiteException -> 0x00a8, all -> 0x009b }
            r5 = 0
            r6 = 0
            java.lang.String r7 = "rowid"
            com.google.android.gms.measurement.internal.zzd r8 = r11.zzCp()     // Catch:{ SQLiteException -> 0x00a8, all -> 0x009b }
            int r8 = r8.zzBM()     // Catch:{ SQLiteException -> 0x00a8, all -> 0x009b }
            java.lang.String r8 = java.lang.String.valueOf(r8)     // Catch:{ SQLiteException -> 0x00a8, all -> 0x009b }
            android.database.Cursor r7 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8)     // Catch:{ SQLiteException -> 0x00a8, all -> 0x009b }
            boolean r0 = r7.moveToFirst()     // Catch:{ SQLiteException -> 0x0085, all -> 0x00a2 }
            if (r0 != 0) goto L_0x0050
            if (r7 == 0) goto L_0x004e
            r7.close()
        L_0x004e:
            r0 = r9
        L_0x004f:
            return r0
        L_0x0050:
            r0 = 0
            java.lang.String r3 = r7.getString(r0)     // Catch:{ SQLiteException -> 0x0085, all -> 0x00a2 }
            r0 = 1
            long r4 = r7.getLong(r0)     // Catch:{ SQLiteException -> 0x0085, all -> 0x00a2 }
            r0 = 2
            java.lang.Object r6 = r11.zzb((android.database.Cursor) r7, (int) r0)     // Catch:{ SQLiteException -> 0x0085, all -> 0x00a2 }
            if (r6 != 0) goto L_0x007b
            com.google.android.gms.measurement.internal.zzp r0 = r11.zzAo()     // Catch:{ SQLiteException -> 0x0085, all -> 0x00a2 }
            com.google.android.gms.measurement.internal.zzp$zza r0 = r0.zzCE()     // Catch:{ SQLiteException -> 0x0085, all -> 0x00a2 }
            java.lang.String r1 = "Read invalid user property value, ignoring it"
            r0.zzfg(r1)     // Catch:{ SQLiteException -> 0x0085, all -> 0x00a2 }
        L_0x006e:
            boolean r0 = r7.moveToNext()     // Catch:{ SQLiteException -> 0x0085, all -> 0x00a2 }
            if (r0 != 0) goto L_0x0050
            if (r7 == 0) goto L_0x0079
            r7.close()
        L_0x0079:
            r0 = r9
            goto L_0x004f
        L_0x007b:
            com.google.android.gms.measurement.internal.zzai r1 = new com.google.android.gms.measurement.internal.zzai     // Catch:{ SQLiteException -> 0x0085, all -> 0x00a2 }
            r2 = r12
            r1.<init>(r2, r3, r4, r6)     // Catch:{ SQLiteException -> 0x0085, all -> 0x00a2 }
            r9.add(r1)     // Catch:{ SQLiteException -> 0x0085, all -> 0x00a2 }
            goto L_0x006e
        L_0x0085:
            r0 = move-exception
            r1 = r7
        L_0x0087:
            com.google.android.gms.measurement.internal.zzp r2 = r11.zzAo()     // Catch:{ all -> 0x00a5 }
            com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCE()     // Catch:{ all -> 0x00a5 }
            java.lang.String r3 = "Error querying user properties"
            r2.zze(r3, r12, r0)     // Catch:{ all -> 0x00a5 }
            if (r1 == 0) goto L_0x0099
            r1.close()
        L_0x0099:
            r0 = r10
            goto L_0x004f
        L_0x009b:
            r0 = move-exception
        L_0x009c:
            if (r10 == 0) goto L_0x00a1
            r10.close()
        L_0x00a1:
            throw r0
        L_0x00a2:
            r0 = move-exception
            r10 = r7
            goto L_0x009c
        L_0x00a5:
            r0 = move-exception
            r10 = r1
            goto L_0x009c
        L_0x00a8:
            r0 = move-exception
            r1 = r10
            goto L_0x0087
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zze.zzeX(java.lang.String):java.util.List");
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x016f  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.android.gms.measurement.internal.zza zzeY(java.lang.String r12) {
        /*
            r11 = this;
            r10 = 0
            r9 = 1
            r8 = 0
            com.google.android.gms.common.internal.zzx.zzcM(r12)
            r11.zzjk()
            r11.zzjv()
            android.database.sqlite.SQLiteDatabase r0 = r11.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            java.lang.String r1 = "apps"
            r2 = 17
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r3 = 0
            java.lang.String r4 = "app_instance_id"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r3 = 1
            java.lang.String r4 = "gmp_app_id"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r3 = 2
            java.lang.String r4 = "resettable_device_id_hash"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r3 = 3
            java.lang.String r4 = "last_bundle_index"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r3 = 4
            java.lang.String r4 = "last_bundle_start_timestamp"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r3 = 5
            java.lang.String r4 = "last_bundle_end_timestamp"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r3 = 6
            java.lang.String r4 = "app_version"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r3 = 7
            java.lang.String r4 = "app_store"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r3 = 8
            java.lang.String r4 = "gmp_version"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r3 = 9
            java.lang.String r4 = "dev_cert_hash"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r3 = 10
            java.lang.String r4 = "measurement_enabled"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r3 = 11
            java.lang.String r4 = "day"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r3 = 12
            java.lang.String r4 = "daily_public_events_count"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r3 = 13
            java.lang.String r4 = "daily_events_count"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r3 = 14
            java.lang.String r4 = "daily_conversions_count"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r3 = 15
            java.lang.String r4 = "config_fetched_time"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r3 = 16
            java.lang.String r4 = "failed_config_fetch_time"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            java.lang.String r3 = "app_id=?"
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r5 = 0
            r4[r5] = r12     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ SQLiteException -> 0x0154, all -> 0x016b }
            boolean r0 = r1.moveToFirst()     // Catch:{ SQLiteException -> 0x0175 }
            if (r0 != 0) goto L_0x0090
            if (r1 == 0) goto L_0x008e
            r1.close()
        L_0x008e:
            r0 = r8
        L_0x008f:
            return r0
        L_0x0090:
            com.google.android.gms.measurement.internal.zza r0 = new com.google.android.gms.measurement.internal.zza     // Catch:{ SQLiteException -> 0x0175 }
            com.google.android.gms.measurement.internal.zzw r2 = r11.zzaTV     // Catch:{ SQLiteException -> 0x0175 }
            r0.<init>(r2, r12)     // Catch:{ SQLiteException -> 0x0175 }
            r2 = 0
            java.lang.String r2 = r1.getString(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r0.zzeM(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r2 = 1
            java.lang.String r2 = r1.getString(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r0.zzeN(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r2 = 2
            java.lang.String r2 = r1.getString(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r0.zzeO(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r2 = 3
            long r2 = r1.getLong(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r0.zzS(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r2 = 4
            long r2 = r1.getLong(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r0.zzO(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r2 = 5
            long r2 = r1.getLong(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r0.zzP(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r2 = 6
            java.lang.String r2 = r1.getString(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r0.setAppVersion(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r2 = 7
            java.lang.String r2 = r1.getString(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r0.zzeP(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r2 = 8
            long r2 = r1.getLong(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r0.zzQ(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r2 = 9
            long r2 = r1.getLong(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r0.zzR(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r2 = 10
            boolean r2 = r1.isNull(r2)     // Catch:{ SQLiteException -> 0x0175 }
            if (r2 == 0) goto L_0x014b
            r2 = r9
        L_0x00f2:
            if (r2 == 0) goto L_0x0152
            r2 = r9
        L_0x00f5:
            r0.setMeasurementEnabled(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r2 = 11
            long r2 = r1.getLong(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r0.zzV(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r2 = 12
            long r2 = r1.getLong(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r0.zzW(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r2 = 13
            long r2 = r1.getLong(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r0.zzX(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r2 = 14
            long r2 = r1.getLong(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r0.zzY(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r2 = 15
            long r2 = r1.getLong(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r0.zzT(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r2 = 16
            long r2 = r1.getLong(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r0.zzU(r2)     // Catch:{ SQLiteException -> 0x0175 }
            r0.zzBi()     // Catch:{ SQLiteException -> 0x0175 }
            boolean r2 = r1.moveToNext()     // Catch:{ SQLiteException -> 0x0175 }
            if (r2 == 0) goto L_0x0144
            com.google.android.gms.measurement.internal.zzp r2 = r11.zzAo()     // Catch:{ SQLiteException -> 0x0175 }
            com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCE()     // Catch:{ SQLiteException -> 0x0175 }
            java.lang.String r3 = "Got multiple records for app, expected one"
            r2.zzfg(r3)     // Catch:{ SQLiteException -> 0x0175 }
        L_0x0144:
            if (r1 == 0) goto L_0x008f
            r1.close()
            goto L_0x008f
        L_0x014b:
            r2 = 10
            int r2 = r1.getInt(r2)     // Catch:{ SQLiteException -> 0x0175 }
            goto L_0x00f2
        L_0x0152:
            r2 = r10
            goto L_0x00f5
        L_0x0154:
            r0 = move-exception
            r1 = r8
        L_0x0156:
            com.google.android.gms.measurement.internal.zzp r2 = r11.zzAo()     // Catch:{ all -> 0x0173 }
            com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCE()     // Catch:{ all -> 0x0173 }
            java.lang.String r3 = "Error querying app"
            r2.zze(r3, r12, r0)     // Catch:{ all -> 0x0173 }
            if (r1 == 0) goto L_0x0168
            r1.close()
        L_0x0168:
            r0 = r8
            goto L_0x008f
        L_0x016b:
            r0 = move-exception
            r1 = r8
        L_0x016d:
            if (r1 == 0) goto L_0x0172
            r1.close()
        L_0x0172:
            throw r0
        L_0x0173:
            r0 = move-exception
            goto L_0x016d
        L_0x0175:
            r0 = move-exception
            goto L_0x0156
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zze.zzeY(java.lang.String):com.google.android.gms.measurement.internal.zza");
    }

    public long zzeZ(String str) {
        zzx.zzcM(str);
        zzjk();
        zzjv();
        try {
            return (long) getWritableDatabase().delete("raw_events", "rowid in (select rowid from raw_events where app_id=? order by rowid desc limit -1 offset ?)", new String[]{str, String.valueOf(zzCp().zzeW(str))});
        } catch (SQLiteException e) {
            zzAo().zzCE().zzj("Error deleting over the limit events", e);
            return 0;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x006c  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] zzfa(java.lang.String r10) {
        /*
            r9 = this;
            r8 = 0
            com.google.android.gms.common.internal.zzx.zzcM(r10)
            r9.zzjk()
            r9.zzjv()
            android.database.sqlite.SQLiteDatabase r0 = r9.getWritableDatabase()     // Catch:{ SQLiteException -> 0x0052, all -> 0x0068 }
            java.lang.String r1 = "apps"
            r2 = 1
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ SQLiteException -> 0x0052, all -> 0x0068 }
            r3 = 0
            java.lang.String r4 = "remote_config"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x0052, all -> 0x0068 }
            java.lang.String r3 = "app_id=?"
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ SQLiteException -> 0x0052, all -> 0x0068 }
            r5 = 0
            r4[r5] = r10     // Catch:{ SQLiteException -> 0x0052, all -> 0x0068 }
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ SQLiteException -> 0x0052, all -> 0x0068 }
            boolean r0 = r1.moveToFirst()     // Catch:{ SQLiteException -> 0x0072 }
            if (r0 != 0) goto L_0x0034
            if (r1 == 0) goto L_0x0032
            r1.close()
        L_0x0032:
            r0 = r8
        L_0x0033:
            return r0
        L_0x0034:
            r0 = 0
            byte[] r0 = r1.getBlob(r0)     // Catch:{ SQLiteException -> 0x0072 }
            boolean r2 = r1.moveToNext()     // Catch:{ SQLiteException -> 0x0072 }
            if (r2 == 0) goto L_0x004c
            com.google.android.gms.measurement.internal.zzp r2 = r9.zzAo()     // Catch:{ SQLiteException -> 0x0072 }
            com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCE()     // Catch:{ SQLiteException -> 0x0072 }
            java.lang.String r3 = "Got multiple records for app config, expected one"
            r2.zzfg(r3)     // Catch:{ SQLiteException -> 0x0072 }
        L_0x004c:
            if (r1 == 0) goto L_0x0033
            r1.close()
            goto L_0x0033
        L_0x0052:
            r0 = move-exception
            r1 = r8
        L_0x0054:
            com.google.android.gms.measurement.internal.zzp r2 = r9.zzAo()     // Catch:{ all -> 0x0070 }
            com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCE()     // Catch:{ all -> 0x0070 }
            java.lang.String r3 = "Error querying remote config"
            r2.zze(r3, r10, r0)     // Catch:{ all -> 0x0070 }
            if (r1 == 0) goto L_0x0066
            r1.close()
        L_0x0066:
            r0 = r8
            goto L_0x0033
        L_0x0068:
            r0 = move-exception
            r1 = r8
        L_0x006a:
            if (r1 == 0) goto L_0x006f
            r1.close()
        L_0x006f:
            throw r0
        L_0x0070:
            r0 = move-exception
            goto L_0x006a
        L_0x0072:
            r0 = move-exception
            goto L_0x0054
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zze.zzfa(java.lang.String):byte[]");
    }

    /* access modifiers changed from: package-private */
    @WorkerThread
    public void zzfb(String str) {
        zzjv();
        zzjk();
        zzx.zzcM(str);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete("property_filters", "app_id=?", new String[]{str});
        writableDatabase.delete("event_filters", "app_id=?", new String[]{str});
    }

    public void zzfc(String str) {
        try {
            getWritableDatabase().execSQL("delete from raw_events_metadata where app_id=? and metadata_fingerprint not in (select distinct metadata_fingerprint from raw_events where app_id=?)", new String[]{str, str});
        } catch (SQLiteException e) {
            zzAo().zzCE().zzj("Failed to remove unused event metadata", e);
        }
    }

    public long zzfd(String str) {
        zzx.zzcM(str);
        return zza("select count(1) from events where app_id=? and name not like '!_%' escape '!'", new String[]{str}, 0);
    }

    /* access modifiers changed from: protected */
    public void zziJ() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:56:0x00da  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<android.util.Pair<com.google.android.gms.internal.zzqb.zze, java.lang.Long>> zzn(java.lang.String r12, int r13, int r14) {
        /*
            r11 = this;
            r10 = 0
            r1 = 1
            r9 = 0
            r11.zzjk()
            r11.zzjv()
            if (r13 <= 0) goto L_0x004e
            r0 = r1
        L_0x000c:
            com.google.android.gms.common.internal.zzx.zzac(r0)
            if (r14 <= 0) goto L_0x0050
        L_0x0011:
            com.google.android.gms.common.internal.zzx.zzac(r1)
            com.google.android.gms.common.internal.zzx.zzcM(r12)
            android.database.sqlite.SQLiteDatabase r0 = r11.getWritableDatabase()     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00d6 }
            java.lang.String r1 = "queue"
            r2 = 2
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00d6 }
            r3 = 0
            java.lang.String r4 = "rowid"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00d6 }
            r3 = 1
            java.lang.String r4 = "data"
            r2[r3] = r4     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00d6 }
            java.lang.String r3 = "app_id=?"
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00d6 }
            r5 = 0
            r4[r5] = r12     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00d6 }
            r5 = 0
            r6 = 0
            java.lang.String r7 = "rowid"
            java.lang.String r8 = java.lang.String.valueOf(r13)     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00d6 }
            android.database.Cursor r2 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8)     // Catch:{ SQLiteException -> 0x00e3, all -> 0x00d6 }
            boolean r0 = r2.moveToFirst()     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            if (r0 != 0) goto L_0x0052
            java.util.List r0 = java.util.Collections.emptyList()     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            if (r2 == 0) goto L_0x004d
            r2.close()
        L_0x004d:
            return r0
        L_0x004e:
            r0 = r9
            goto L_0x000c
        L_0x0050:
            r1 = r9
            goto L_0x0011
        L_0x0052:
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            r0.<init>()     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            r3 = r9
        L_0x0058:
            r1 = 0
            long r4 = r2.getLong(r1)     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            r1 = 1
            byte[] r1 = r2.getBlob(r1)     // Catch:{ IOException -> 0x007a }
            com.google.android.gms.measurement.internal.zzaj r6 = r11.zzCk()     // Catch:{ IOException -> 0x007a }
            byte[] r1 = r6.zzp(r1)     // Catch:{ IOException -> 0x007a }
            boolean r6 = r0.isEmpty()     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            if (r6 != 0) goto L_0x0093
            int r6 = r1.length     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            int r6 = r6 + r3
            if (r6 <= r14) goto L_0x0093
        L_0x0074:
            if (r2 == 0) goto L_0x004d
            r2.close()
            goto L_0x004d
        L_0x007a:
            r1 = move-exception
            com.google.android.gms.measurement.internal.zzp r4 = r11.zzAo()     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            com.google.android.gms.measurement.internal.zzp$zza r4 = r4.zzCE()     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            java.lang.String r5 = "Failed to unzip queued bundle"
            r4.zze(r5, r12, r1)     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            r1 = r3
        L_0x0089:
            boolean r3 = r2.moveToNext()     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            if (r3 == 0) goto L_0x0074
            if (r1 > r14) goto L_0x0074
            r3 = r1
            goto L_0x0058
        L_0x0093:
            com.google.android.gms.internal.zzsm r6 = com.google.android.gms.internal.zzsm.zzD(r1)     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            com.google.android.gms.internal.zzqb$zze r7 = new com.google.android.gms.internal.zzqb$zze     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            r7.<init>()     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            r7.mergeFrom(r6)     // Catch:{ IOException -> 0x00c6 }
            int r1 = r1.length     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            int r1 = r1 + r3
            java.lang.Long r3 = java.lang.Long.valueOf(r4)     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            android.util.Pair r3 = android.util.Pair.create(r7, r3)     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            r0.add(r3)     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            goto L_0x0089
        L_0x00ad:
            r0 = move-exception
            r1 = r2
        L_0x00af:
            com.google.android.gms.measurement.internal.zzp r2 = r11.zzAo()     // Catch:{ all -> 0x00e0 }
            com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCE()     // Catch:{ all -> 0x00e0 }
            java.lang.String r3 = "Error querying bundles"
            r2.zze(r3, r12, r0)     // Catch:{ all -> 0x00e0 }
            java.util.List r0 = java.util.Collections.emptyList()     // Catch:{ all -> 0x00e0 }
            if (r1 == 0) goto L_0x004d
            r1.close()
            goto L_0x004d
        L_0x00c6:
            r1 = move-exception
            com.google.android.gms.measurement.internal.zzp r4 = r11.zzAo()     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            com.google.android.gms.measurement.internal.zzp$zza r4 = r4.zzCE()     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            java.lang.String r5 = "Failed to merge queued bundle"
            r4.zze(r5, r12, r1)     // Catch:{ SQLiteException -> 0x00ad, all -> 0x00de }
            r1 = r3
            goto L_0x0089
        L_0x00d6:
            r0 = move-exception
            r2 = r10
        L_0x00d8:
            if (r2 == 0) goto L_0x00dd
            r2.close()
        L_0x00dd:
            throw r0
        L_0x00de:
            r0 = move-exception
            goto L_0x00d8
        L_0x00e0:
            r0 = move-exception
            r2 = r1
            goto L_0x00d8
        L_0x00e3:
            r0 = move-exception
            r1 = r10
            goto L_0x00af
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zze.zzn(java.lang.String, int, int):java.util.List");
    }

    public void zzz(List<Long> list) {
        zzx.zzz(list);
        zzjk();
        zzjv();
        StringBuilder sb = new StringBuilder("rowid in (");
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= list.size()) {
                break;
            }
            if (i2 != 0) {
                sb.append(",");
            }
            sb.append(list.get(i2).longValue());
            i = i2 + 1;
        }
        sb.append(")");
        int delete = getWritableDatabase().delete("raw_events", sb.toString(), (String[]) null);
        if (delete != list.size()) {
            zzAo().zzCE().zze("Deleted fewer rows from raw events table than expected", Integer.valueOf(delete), Integer.valueOf(list.size()));
        }
    }
}
