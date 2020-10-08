package com.google.android.gms.measurement.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface zzm extends IInterface {

    public static abstract class zza extends Binder implements zzm {

        /* renamed from: com.google.android.gms.measurement.internal.zzm$zza$zza  reason: collision with other inner class name */
        private static class C0021zza implements zzm {
            private IBinder zzoz;

            C0021zza(IBinder iBinder) {
                this.zzoz = iBinder;
            }

            public IBinder asBinder() {
                return this.zzoz;
            }

            public List<UserAttributeParcel> zza(AppMetadata appMetadata, boolean z) throws RemoteException {
                int i = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.measurement.internal.IMeasurementService");
                    if (appMetadata != null) {
                        obtain.writeInt(1);
                        appMetadata.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!z) {
                        i = 0;
                    }
                    obtain.writeInt(i);
                    this.zzoz.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createTypedArrayList(UserAttributeParcel.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(AppMetadata appMetadata) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.measurement.internal.IMeasurementService");
                    if (appMetadata != null) {
                        obtain.writeInt(1);
                        appMetadata.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(EventParcel eventParcel, AppMetadata appMetadata) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.measurement.internal.IMeasurementService");
                    if (eventParcel != null) {
                        obtain.writeInt(1);
                        eventParcel.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (appMetadata != null) {
                        obtain.writeInt(1);
                        appMetadata.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(EventParcel eventParcel, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.measurement.internal.IMeasurementService");
                    if (eventParcel != null) {
                        obtain.writeInt(1);
                        eventParcel.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.zzoz.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zza(UserAttributeParcel userAttributeParcel, AppMetadata appMetadata) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.measurement.internal.IMeasurementService");
                    if (userAttributeParcel != null) {
                        obtain.writeInt(1);
                        userAttributeParcel.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (appMetadata != null) {
                        obtain.writeInt(1);
                        appMetadata.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void zzb(AppMetadata appMetadata) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.measurement.internal.IMeasurementService");
                    if (appMetadata != null) {
                        obtain.writeInt(1);
                        appMetadata.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.zzoz.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public zza() {
            attachInterface(this, "com.google.android.gms.measurement.internal.IMeasurementService");
        }

        public static zzm zzdn(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.measurement.internal.IMeasurementService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof zzm)) ? new C0021zza(iBinder) : (zzm) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: com.google.android.gms.measurement.internal.AppMetadata} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: com.google.android.gms.measurement.internal.AppMetadata} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: com.google.android.gms.measurement.internal.EventParcel} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: com.google.android.gms.measurement.internal.AppMetadata} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v9, resolved type: com.google.android.gms.measurement.internal.AppMetadata} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v12, resolved type: com.google.android.gms.measurement.internal.AppMetadata} */
        /* JADX WARNING: type inference failed for: r1v0 */
        /* JADX WARNING: type inference failed for: r1v15 */
        /* JADX WARNING: type inference failed for: r1v16 */
        /* JADX WARNING: type inference failed for: r1v17 */
        /* JADX WARNING: type inference failed for: r1v18 */
        /* JADX WARNING: type inference failed for: r1v19 */
        /* JADX WARNING: type inference failed for: r1v20 */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r5, android.os.Parcel r6, android.os.Parcel r7, int r8) throws android.os.RemoteException {
            /*
                r4 = this;
                r1 = 0
                r2 = 1
                switch(r5) {
                    case 1: goto L_0x0010;
                    case 2: goto L_0x0036;
                    case 4: goto L_0x005c;
                    case 5: goto L_0x0074;
                    case 6: goto L_0x0095;
                    case 7: goto L_0x00ae;
                    case 1598968902: goto L_0x000a;
                    default: goto L_0x0005;
                }
            L_0x0005:
                boolean r2 = super.onTransact(r5, r6, r7, r8)
            L_0x0009:
                return r2
            L_0x000a:
                java.lang.String r0 = "com.google.android.gms.measurement.internal.IMeasurementService"
                r7.writeString(r0)
                goto L_0x0009
            L_0x0010:
                java.lang.String r0 = "com.google.android.gms.measurement.internal.IMeasurementService"
                r6.enforceInterface(r0)
                int r0 = r6.readInt()
                if (r0 == 0) goto L_0x0034
                com.google.android.gms.measurement.internal.zzk r0 = com.google.android.gms.measurement.internal.EventParcel.CREATOR
                com.google.android.gms.measurement.internal.EventParcel r0 = r0.createFromParcel(r6)
            L_0x0021:
                int r3 = r6.readInt()
                if (r3 == 0) goto L_0x002d
                com.google.android.gms.measurement.internal.zzb r1 = com.google.android.gms.measurement.internal.AppMetadata.CREATOR
                com.google.android.gms.measurement.internal.AppMetadata r1 = r1.createFromParcel(r6)
            L_0x002d:
                r4.zza((com.google.android.gms.measurement.internal.EventParcel) r0, (com.google.android.gms.measurement.internal.AppMetadata) r1)
                r7.writeNoException()
                goto L_0x0009
            L_0x0034:
                r0 = r1
                goto L_0x0021
            L_0x0036:
                java.lang.String r0 = "com.google.android.gms.measurement.internal.IMeasurementService"
                r6.enforceInterface(r0)
                int r0 = r6.readInt()
                if (r0 == 0) goto L_0x005a
                com.google.android.gms.measurement.internal.zzah r0 = com.google.android.gms.measurement.internal.UserAttributeParcel.CREATOR
                com.google.android.gms.measurement.internal.UserAttributeParcel r0 = r0.createFromParcel(r6)
            L_0x0047:
                int r3 = r6.readInt()
                if (r3 == 0) goto L_0x0053
                com.google.android.gms.measurement.internal.zzb r1 = com.google.android.gms.measurement.internal.AppMetadata.CREATOR
                com.google.android.gms.measurement.internal.AppMetadata r1 = r1.createFromParcel(r6)
            L_0x0053:
                r4.zza((com.google.android.gms.measurement.internal.UserAttributeParcel) r0, (com.google.android.gms.measurement.internal.AppMetadata) r1)
                r7.writeNoException()
                goto L_0x0009
            L_0x005a:
                r0 = r1
                goto L_0x0047
            L_0x005c:
                java.lang.String r0 = "com.google.android.gms.measurement.internal.IMeasurementService"
                r6.enforceInterface(r0)
                int r0 = r6.readInt()
                if (r0 == 0) goto L_0x006d
                com.google.android.gms.measurement.internal.zzb r0 = com.google.android.gms.measurement.internal.AppMetadata.CREATOR
                com.google.android.gms.measurement.internal.AppMetadata r1 = r0.createFromParcel(r6)
            L_0x006d:
                r4.zza(r1)
                r7.writeNoException()
                goto L_0x0009
            L_0x0074:
                java.lang.String r0 = "com.google.android.gms.measurement.internal.IMeasurementService"
                r6.enforceInterface(r0)
                int r0 = r6.readInt()
                if (r0 == 0) goto L_0x0085
                com.google.android.gms.measurement.internal.zzk r0 = com.google.android.gms.measurement.internal.EventParcel.CREATOR
                com.google.android.gms.measurement.internal.EventParcel r1 = r0.createFromParcel(r6)
            L_0x0085:
                java.lang.String r0 = r6.readString()
                java.lang.String r3 = r6.readString()
                r4.zza(r1, r0, r3)
                r7.writeNoException()
                goto L_0x0009
            L_0x0095:
                java.lang.String r0 = "com.google.android.gms.measurement.internal.IMeasurementService"
                r6.enforceInterface(r0)
                int r0 = r6.readInt()
                if (r0 == 0) goto L_0x00a6
                com.google.android.gms.measurement.internal.zzb r0 = com.google.android.gms.measurement.internal.AppMetadata.CREATOR
                com.google.android.gms.measurement.internal.AppMetadata r1 = r0.createFromParcel(r6)
            L_0x00a6:
                r4.zzb(r1)
                r7.writeNoException()
                goto L_0x0009
            L_0x00ae:
                java.lang.String r0 = "com.google.android.gms.measurement.internal.IMeasurementService"
                r6.enforceInterface(r0)
                int r0 = r6.readInt()
                if (r0 == 0) goto L_0x00bf
                com.google.android.gms.measurement.internal.zzb r0 = com.google.android.gms.measurement.internal.AppMetadata.CREATOR
                com.google.android.gms.measurement.internal.AppMetadata r1 = r0.createFromParcel(r6)
            L_0x00bf:
                int r0 = r6.readInt()
                if (r0 == 0) goto L_0x00d2
                r0 = r2
            L_0x00c6:
                java.util.List r0 = r4.zza((com.google.android.gms.measurement.internal.AppMetadata) r1, (boolean) r0)
                r7.writeNoException()
                r7.writeTypedList(r0)
                goto L_0x0009
            L_0x00d2:
                r0 = 0
                goto L_0x00c6
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzm.zza.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }
    }

    List<UserAttributeParcel> zza(AppMetadata appMetadata, boolean z) throws RemoteException;

    void zza(AppMetadata appMetadata) throws RemoteException;

    void zza(EventParcel eventParcel, AppMetadata appMetadata) throws RemoteException;

    void zza(EventParcel eventParcel, String str, String str2) throws RemoteException;

    void zza(UserAttributeParcel userAttributeParcel, AppMetadata appMetadata) throws RemoteException;

    void zzb(AppMetadata appMetadata) throws RemoteException;
}
