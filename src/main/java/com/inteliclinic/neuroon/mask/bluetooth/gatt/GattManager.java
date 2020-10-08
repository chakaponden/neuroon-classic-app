package com.inteliclinic.neuroon.mask.bluetooth.gatt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.inteliclinic.neuroon.mask.bluetooth.DeviceStateEvent;
import de.greenrobot.event.EventBus;
import java.lang.reflect.Method;
import java.util.UUID;

public class GattManager {
    private static final boolean DEBUG = false;
    private static final String TAG = GattManager.class.getSimpleName();
    protected static final UUID UUID_CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    /* access modifiers changed from: private */
    public final Object lock = new Object();
    /* access modifiers changed from: private */
    public CharacteristicChangeListener mCharacteristicListener;
    /* access modifiers changed from: private */
    public int mConnectionState = 0;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public boolean mError;
    /* access modifiers changed from: private */
    public BluetoothGatt mGatt;
    /* access modifiers changed from: private */
    public final GattCallback mGattCallback = new GattCallback();
    /* access modifiers changed from: private */
    public Handler mHandler;
    private HandlerThread mHandlerThread;
    /* access modifiers changed from: private */
    public boolean mRequestDone;
    private Handler mWorkerHandler;

    public GattManager(Context context, CharacteristicChangeListener listener) {
        this.mContext = context;
        this.mCharacteristicListener = listener;
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mHandlerThread = new HandlerThread(TAG);
        this.mHandlerThread.start();
        this.mWorkerHandler = new Handler(this.mHandlerThread.getLooper());
    }

    private synchronized boolean refreshDeviceCache(BluetoothGatt gatt) {
        boolean z;
        try {
            Method localMethod = gatt.getClass().getMethod("refresh", new Class[0]);
            if (localMethod != null) {
                z = ((Boolean) localMethod.invoke(gatt, new Object[0])).booleanValue();
            }
        } catch (Exception e) {
            Log.e(TAG, "An exception occured while refreshing device");
        }
        z = false;
        return z;
    }

    public synchronized void connectDeviceAsync(BluetoothDevice device, GattConnectionCallback listener) {
        if (this.mGatt == null || this.mConnectionState == 0) {
            this.mConnectionState = 1;
            final BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(device.getAddress());
            this.mGattCallback.setConnectionListener(listener);
            this.mHandler.post(new Runnable() {
                public void run() {
                    remoteDevice.connectGatt(GattManager.this.mContext, false, GattManager.this.mGattCallback);
                }
            });
        } else {
            listener.onError();
        }
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
        	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        	at java.base/java.util.Objects.checkIndex(Objects.java:372)
        	at java.base/java.util.ArrayList.get(ArrayList.java:459)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    public synchronized void disconnect() {
        /*
            r3 = this;
            monitor-enter(r3)
            android.bluetooth.BluetoothGatt r1 = r3.mGatt     // Catch:{ all -> 0x002a }
            if (r1 == 0) goto L_0x0009
            int r1 = r3.mConnectionState     // Catch:{ all -> 0x002a }
            if (r1 != 0) goto L_0x000b
        L_0x0009:
            monitor-exit(r3)
            return
        L_0x000b:
            android.bluetooth.BluetoothGatt r0 = r3.mGatt     // Catch:{ all -> 0x002a }
            android.bluetooth.BluetoothGatt r1 = r3.mGatt     // Catch:{ all -> 0x002a }
            r1.disconnect()     // Catch:{ all -> 0x002a }
            java.lang.Object r2 = r3.lock     // Catch:{ InterruptedException -> 0x0022 }
            monitor-enter(r2)     // Catch:{ InterruptedException -> 0x0022 }
        L_0x0015:
            int r1 = r3.mConnectionState     // Catch:{ all -> 0x001f }
            if (r1 == 0) goto L_0x002d
            java.lang.Object r1 = r3.lock     // Catch:{ all -> 0x001f }
            r1.wait()     // Catch:{ all -> 0x001f }
            goto L_0x0015
        L_0x001f:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x001f }
            throw r1     // Catch:{ InterruptedException -> 0x0022 }
        L_0x0022:
            r1 = move-exception
        L_0x0023:
            r3.refreshDeviceCache(r0)     // Catch:{ all -> 0x002a }
            r0.close()     // Catch:{ all -> 0x002a }
            goto L_0x0009
        L_0x002a:
            r1 = move-exception
            monitor-exit(r3)
            throw r1
        L_0x002d:
            monitor-exit(r2)     // Catch:{ all -> 0x001f }
            goto L_0x0023
        */
        throw new UnsupportedOperationException("Method not decompiled: com.inteliclinic.neuroon.mask.bluetooth.gatt.GattManager.disconnect():void");
    }

    public synchronized void requestPriority(int priority) {
        if (this.mGatt != null && this.mConnectionState == 3 && Build.VERSION.SDK_INT >= 21) {
            this.mGatt.requestConnectionPriority(priority);
        }
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
        	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        	at java.base/java.util.Objects.checkIndex(Objects.java:372)
        	at java.base/java.util.ArrayList.get(ArrayList.java:459)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    public synchronized android.bluetooth.BluetoothGattCharacteristic readCharacteristic(java.util.UUID r5, java.util.UUID r6) {
        /*
            r4 = this;
            monitor-enter(r4)
            r1 = 0
            r4.mError = r1     // Catch:{ all -> 0x003d }
            r1 = 0
            r4.mRequestDone = r1     // Catch:{ all -> 0x003d }
            android.bluetooth.BluetoothGatt r1 = r4.mGatt     // Catch:{ all -> 0x003d }
            android.bluetooth.BluetoothGattService r1 = r1.getService(r5)     // Catch:{ all -> 0x003d }
            android.bluetooth.BluetoothGattCharacteristic r0 = r1.getCharacteristic(r6)     // Catch:{ all -> 0x003d }
            android.bluetooth.BluetoothGatt r1 = r4.mGatt     // Catch:{ all -> 0x003d }
            r1.readCharacteristic(r0)     // Catch:{ all -> 0x003d }
            java.lang.Object r2 = r4.lock     // Catch:{ InterruptedException -> 0x0038 }
            monitor-enter(r2)     // Catch:{ InterruptedException -> 0x0038 }
        L_0x0019:
            boolean r1 = r4.mRequestDone     // Catch:{ all -> 0x0035 }
            if (r1 == 0) goto L_0x0023
            byte[] r1 = r0.getValue()     // Catch:{ all -> 0x0035 }
            if (r1 != 0) goto L_0x003b
        L_0x0023:
            int r1 = r4.mConnectionState     // Catch:{ all -> 0x0035 }
            r3 = 3
            if (r1 != r3) goto L_0x003b
            boolean r1 = r4.mError     // Catch:{ all -> 0x0035 }
            if (r1 != 0) goto L_0x003b
            r1 = 0
            r4.mRequestDone = r1     // Catch:{ all -> 0x0035 }
            java.lang.Object r1 = r4.lock     // Catch:{ all -> 0x0035 }
            r1.wait()     // Catch:{ all -> 0x0035 }
            goto L_0x0019
        L_0x0035:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0035 }
            throw r1     // Catch:{ InterruptedException -> 0x0038 }
        L_0x0038:
            r1 = move-exception
        L_0x0039:
            monitor-exit(r4)
            return r0
        L_0x003b:
            monitor-exit(r2)     // Catch:{ all -> 0x0035 }
            goto L_0x0039
        L_0x003d:
            r1 = move-exception
            monitor-exit(r4)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.inteliclinic.neuroon.mask.bluetooth.gatt.GattManager.readCharacteristic(java.util.UUID, java.util.UUID):android.bluetooth.BluetoothGattCharacteristic");
    }

    public synchronized void writeCharacteristic(UUID serviceUuid, UUID characteristic, byte[] value) {
        writeCharacteristic(serviceUuid, characteristic, value, true);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0014, code lost:
        r1 = r0.getCharacteristic(r6);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void writeCharacteristic(java.util.UUID r5, java.util.UUID r6, final byte[] r7, final boolean r8) {
        /*
            r4 = this;
            monitor-enter(r4)
            android.bluetooth.BluetoothGatt r2 = r4.mGatt     // Catch:{ all -> 0x0025 }
            if (r2 == 0) goto L_0x000a
            int r2 = r4.mConnectionState     // Catch:{ all -> 0x0025 }
            r3 = 3
            if (r2 == r3) goto L_0x000c
        L_0x000a:
            monitor-exit(r4)
            return
        L_0x000c:
            android.bluetooth.BluetoothGatt r2 = r4.mGatt     // Catch:{ all -> 0x0025 }
            android.bluetooth.BluetoothGattService r0 = r2.getService(r5)     // Catch:{ all -> 0x0025 }
            if (r0 == 0) goto L_0x000a
            android.bluetooth.BluetoothGattCharacteristic r1 = r0.getCharacteristic(r6)     // Catch:{ all -> 0x0025 }
            if (r1 == 0) goto L_0x000a
            android.os.Handler r2 = r4.mWorkerHandler     // Catch:{ all -> 0x0025 }
            com.inteliclinic.neuroon.mask.bluetooth.gatt.GattManager$2 r3 = new com.inteliclinic.neuroon.mask.bluetooth.gatt.GattManager$2     // Catch:{ all -> 0x0025 }
            r3.<init>(r8, r1, r7)     // Catch:{ all -> 0x0025 }
            r2.post(r3)     // Catch:{ all -> 0x0025 }
            goto L_0x000a
        L_0x0025:
            r2 = move-exception
            monitor-exit(r4)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.inteliclinic.neuroon.mask.bluetooth.gatt.GattManager.writeCharacteristic(java.util.UUID, java.util.UUID, byte[], boolean):void");
    }

    public synchronized void writeCharacteristicAsync(UUID service, UUID characteristic, final byte[] value, final GattCharacteristicWriteCallback listener) {
        if (this.mGatt != null && this.mConnectionState == 3) {
            final BluetoothGattCharacteristic tempCharacteristic = this.mGatt.getService(service).getCharacteristic(characteristic);
            if (tempCharacteristic != null) {
                this.mWorkerHandler.post(new Runnable() {
                    public void run() {
                        boolean unused = GattManager.this.mError = false;
                        boolean unused2 = GattManager.this.mRequestDone = false;
                        tempCharacteristic.setValue(value);
                        if (GattManager.this.mGatt.writeCharacteristic(tempCharacteristic)) {
                            try {
                                synchronized (GattManager.this.lock) {
                                    while (!GattManager.this.mRequestDone && GattManager.this.mConnectionState == 3 && !GattManager.this.mError) {
                                        boolean unused3 = GattManager.this.mRequestDone = false;
                                        GattManager.this.lock.wait();
                                    }
                                }
                            } catch (InterruptedException e) {
                            }
                            if (listener != null) {
                                listener.onEnd();
                            }
                        }
                    }
                });
            }
        }
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
        	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        	at java.base/java.util.Objects.checkIndex(Objects.java:372)
        	at java.base/java.util.ArrayList.get(ArrayList.java:459)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    public synchronized void readDescriptor(java.util.UUID r5, java.util.UUID r6, java.util.UUID r7) {
        /*
            r4 = this;
            monitor-enter(r4)
            r1 = 0
            r4.mError = r1     // Catch:{ all -> 0x0041 }
            r1 = 0
            r4.mRequestDone = r1     // Catch:{ all -> 0x0041 }
            android.bluetooth.BluetoothGatt r1 = r4.mGatt     // Catch:{ all -> 0x0041 }
            android.bluetooth.BluetoothGattService r1 = r1.getService(r5)     // Catch:{ all -> 0x0041 }
            android.bluetooth.BluetoothGattCharacteristic r1 = r1.getCharacteristic(r6)     // Catch:{ all -> 0x0041 }
            android.bluetooth.BluetoothGattDescriptor r0 = r1.getDescriptor(r7)     // Catch:{ all -> 0x0041 }
            android.bluetooth.BluetoothGatt r1 = r4.mGatt     // Catch:{ all -> 0x0041 }
            r1.readDescriptor(r0)     // Catch:{ all -> 0x0041 }
            java.lang.Object r2 = r4.lock     // Catch:{ InterruptedException -> 0x003c }
            monitor-enter(r2)     // Catch:{ InterruptedException -> 0x003c }
        L_0x001d:
            boolean r1 = r4.mRequestDone     // Catch:{ all -> 0x0039 }
            if (r1 == 0) goto L_0x0027
            byte[] r1 = r0.getValue()     // Catch:{ all -> 0x0039 }
            if (r1 != 0) goto L_0x003f
        L_0x0027:
            int r1 = r4.mConnectionState     // Catch:{ all -> 0x0039 }
            r3 = 3
            if (r1 != r3) goto L_0x003f
            boolean r1 = r4.mError     // Catch:{ all -> 0x0039 }
            if (r1 != 0) goto L_0x003f
            r1 = 0
            r4.mRequestDone = r1     // Catch:{ all -> 0x0039 }
            java.lang.Object r1 = r4.lock     // Catch:{ all -> 0x0039 }
            r1.wait()     // Catch:{ all -> 0x0039 }
            goto L_0x001d
        L_0x0039:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0039 }
            throw r1     // Catch:{ InterruptedException -> 0x003c }
        L_0x003c:
            r1 = move-exception
        L_0x003d:
            monitor-exit(r4)
            return
        L_0x003f:
            monitor-exit(r2)     // Catch:{ all -> 0x0039 }
            goto L_0x003d
        L_0x0041:
            r1 = move-exception
            monitor-exit(r4)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.inteliclinic.neuroon.mask.bluetooth.gatt.GattManager.readDescriptor(java.util.UUID, java.util.UUID, java.util.UUID):void");
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
        	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        	at java.base/java.util.Objects.checkIndex(Objects.java:372)
        	at java.base/java.util.ArrayList.get(ArrayList.java:459)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    public synchronized boolean writeDescriptor(java.util.UUID r6, java.util.UUID r7, byte[] r8, java.util.UUID r9) {
        /*
            r5 = this;
            r4 = 3
            r1 = 0
            monitor-enter(r5)
            android.bluetooth.BluetoothGatt r2 = r5.mGatt     // Catch:{ all -> 0x0052 }
            if (r2 == 0) goto L_0x000b
            int r2 = r5.mConnectionState     // Catch:{ all -> 0x0052 }
            if (r2 == r4) goto L_0x000d
        L_0x000b:
            monitor-exit(r5)
            return r1
        L_0x000d:
            android.bluetooth.BluetoothGatt r2 = r5.mGatt     // Catch:{ all -> 0x0052 }
            android.bluetooth.BluetoothGattService r2 = r2.getService(r6)     // Catch:{ all -> 0x0052 }
            android.bluetooth.BluetoothGattCharacteristic r2 = r2.getCharacteristic(r7)     // Catch:{ all -> 0x0052 }
            android.bluetooth.BluetoothGattDescriptor r0 = r2.getDescriptor(r9)     // Catch:{ all -> 0x0052 }
            if (r0 == 0) goto L_0x000b
            r2 = 0
            r5.mError = r2     // Catch:{ all -> 0x0052 }
            r2 = 0
            r5.mRequestDone = r2     // Catch:{ all -> 0x0052 }
            r0.setValue(r8)     // Catch:{ all -> 0x0052 }
            android.bluetooth.BluetoothGatt r2 = r5.mGatt     // Catch:{ all -> 0x0052 }
            r2.writeDescriptor(r0)     // Catch:{ all -> 0x0052 }
            java.lang.Object r3 = r5.lock     // Catch:{ InterruptedException -> 0x0046 }
            monitor-enter(r3)     // Catch:{ InterruptedException -> 0x0046 }
        L_0x002e:
            boolean r2 = r5.mRequestDone     // Catch:{ all -> 0x0043 }
            if (r2 != 0) goto L_0x0048
            int r2 = r5.mConnectionState     // Catch:{ all -> 0x0043 }
            if (r2 != r4) goto L_0x0048
            boolean r2 = r5.mError     // Catch:{ all -> 0x0043 }
            if (r2 != 0) goto L_0x0048
            r2 = 0
            r5.mRequestDone = r2     // Catch:{ all -> 0x0043 }
            java.lang.Object r2 = r5.lock     // Catch:{ all -> 0x0043 }
            r2.wait()     // Catch:{ all -> 0x0043 }
            goto L_0x002e
        L_0x0043:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0043 }
            throw r2     // Catch:{ InterruptedException -> 0x0046 }
        L_0x0046:
            r2 = move-exception
            goto L_0x000b
        L_0x0048:
            boolean r2 = r5.mRequestDone     // Catch:{ all -> 0x0043 }
            if (r2 == 0) goto L_0x0050
            r2 = 1
            monitor-exit(r3)     // Catch:{ all -> 0x0043 }
            r1 = r2
            goto L_0x000b
        L_0x0050:
            monitor-exit(r3)     // Catch:{ all -> 0x0043 }
            goto L_0x000b
        L_0x0052:
            r1 = move-exception
            monitor-exit(r5)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.inteliclinic.neuroon.mask.bluetooth.gatt.GattManager.writeDescriptor(java.util.UUID, java.util.UUID, byte[], java.util.UUID):boolean");
    }

    public synchronized void writeDescriptorAsync(UUID service, UUID characteristic, final byte[] value, UUID descriptor) {
        if (this.mGatt != null && this.mConnectionState == 3) {
            final BluetoothGattDescriptor tempDescriptor = this.mGatt.getService(service).getCharacteristic(characteristic).getDescriptor(descriptor);
            if (tempDescriptor == null) {
                this.mGatt.disconnect();
            } else {
                this.mWorkerHandler.post(new Runnable() {
                    public void run() {
                        boolean unused = GattManager.this.mError = false;
                        boolean unused2 = GattManager.this.mRequestDone = false;
                        tempDescriptor.setValue(value);
                        if (GattManager.this.mGatt != null) {
                            GattManager.this.mGatt.writeDescriptor(tempDescriptor);
                            try {
                                synchronized (GattManager.this.lock) {
                                    while (!GattManager.this.mRequestDone && GattManager.this.mConnectionState == 3 && !GattManager.this.mError) {
                                        boolean unused3 = GattManager.this.mRequestDone = false;
                                        GattManager.this.lock.wait();
                                    }
                                    if (!GattManager.this.mRequestDone && GattManager.this.mGatt != null) {
                                        GattManager.this.mGatt.disconnect();
                                    }
                                }
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                });
            }
        }
    }

    public synchronized void setNotification(UUID serviceUuid, UUID characteristicUuid, boolean enable) {
        byte[] bArr;
        if (this.mGatt.getService(serviceUuid) == null) {
            if (Crashlytics.getInstance() != null) {
                printBleDeviceCharacteristicsToCrashlytics(this.mGatt);
                Crashlytics.logException(new UnsupportedOperationException("Service " + serviceUuid + " not found"));
            }
            this.mGatt.disconnect();
        } else {
            this.mGatt.setCharacteristicNotification(this.mGatt.getService(serviceUuid).getCharacteristic(characteristicUuid), enable);
            if (enable) {
                bArr = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
            } else {
                bArr = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
            }
            writeDescriptorAsync(serviceUuid, characteristicUuid, bArr, UUID_CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR);
        }
    }

    private void printBleDeviceCharacteristicsToCrashlytics(BluetoothGatt gatt) {
        if (Crashlytics.getInstance() != null) {
            Crashlytics.log("Started device characteristic log");
            for (BluetoothGattService service : gatt.getServices()) {
                Crashlytics.log("Service: " + service.getUuid() + " characteristics:");
                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                    Crashlytics.log(" " + characteristic.getUuid());
                }
            }
            Crashlytics.log("Ended device characteristic log");
        }
    }

    public boolean hasDeviceCharacteristic(UUID service, UUID characteristic) {
        BluetoothGattService gattService;
        if (this.mGatt == null || this.mConnectionState != 3 || (gattService = this.mGatt.getService(service)) == null || gattService.getCharacteristic(characteristic) == null) {
            return false;
        }
        return true;
    }

    public class GattCallback extends BluetoothGattCallback {
        private GattConnectionCallback connectionListener;

        public GattCallback() {
        }

        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (status == 0 && newState == 2) {
                int unused = GattManager.this.mConnectionState = 2;
                BluetoothGatt unused2 = GattManager.this.mGatt = gatt;
                if (!GattManager.this.mGatt.discoverServices()) {
                    GattManager.this.mGatt.close();
                    BluetoothGatt unused3 = GattManager.this.mGatt = null;
                    int unused4 = GattManager.this.mConnectionState = 0;
                    synchronized (GattManager.this.lock) {
                        GattManager.this.lock.notifyAll();
                    }
                }
            } else if (status != 0) {
                gatt.close();
                BluetoothGatt unused5 = GattManager.this.mGatt = null;
                int unused6 = GattManager.this.mConnectionState = 0;
                synchronized (GattManager.this.lock) {
                    GattManager.this.lock.notifyAll();
                }
                if (this.connectionListener != null) {
                    this.connectionListener.onError();
                    this.connectionListener = null;
                }
                EventBus.getDefault().postSticky(new DeviceStateEvent(gatt.getDevice(), status, newState));
            } else {
                if (GattManager.this.mGatt == null) {
                    gatt.close();
                    BluetoothGatt unused7 = GattManager.this.mGatt = null;
                }
                int unused8 = GattManager.this.mConnectionState = 0;
                synchronized (GattManager.this.lock) {
                    GattManager.this.lock.notifyAll();
                }
                EventBus.getDefault().postSticky(new DeviceStateEvent(gatt.getDevice(), status, newState));
            }
        }

        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            boolean unused = GattManager.this.mRequestDone = true;
            synchronized (GattManager.this.lock) {
                GattManager.this.lock.notifyAll();
            }
        }

        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            boolean unused = GattManager.this.mRequestDone = true;
            synchronized (GattManager.this.lock) {
                GattManager.this.lock.notifyAll();
            }
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            boolean unused = GattManager.this.mRequestDone = true;
            synchronized (GattManager.this.lock) {
                GattManager.this.lock.notifyAll();
            }
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == 0) {
                int unused = GattManager.this.mConnectionState = 3;
                synchronized (GattManager.this.lock) {
                    GattManager.this.lock.notifyAll();
                }
                if (this.connectionListener != null) {
                    this.connectionListener.onConnected();
                    this.connectionListener = null;
                }
                EventBus.getDefault().postSticky(new DeviceStateEvent(gatt.getDevice(), 0, 2));
                return;
            }
            synchronized (GattManager.this.lock) {
                GattManager.this.lock.notifyAll();
            }
            if (this.connectionListener != null) {
                this.connectionListener.onError();
                this.connectionListener = null;
            }
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            boolean unused = GattManager.this.mRequestDone = true;
            synchronized (GattManager.this.lock) {
                GattManager.this.lock.notifyAll();
            }
        }

        public void onCharacteristicChanged(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            final byte[] value = characteristic.getValue();
            GattManager.this.mHandler.post(new Runnable() {
                public void run() {
                    if (GattManager.this.mCharacteristicListener != null) {
                        GattManager.this.mCharacteristicListener.onCharacteristicChanged(gatt.getDevice().getAddress(), characteristic, value);
                    }
                }
            });
        }

        public void setConnectionListener(GattConnectionCallback connectionListener2) {
            this.connectionListener = connectionListener2;
        }
    }
}
