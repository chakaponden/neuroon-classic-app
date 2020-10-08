package com.inteliclinic.neuroon.mask.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.inteliclinic.lucid.IMaskUserManager;
import com.inteliclinic.neuroon.managers.ManagerStartedEvent;
import com.inteliclinic.neuroon.mask.BleSettingsCmdFrame;
import com.inteliclinic.neuroon.mask.MaskDownloadStoppedEvent;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.mask.bluetooth.gatt.CharacteristicChangeListener;
import com.inteliclinic.neuroon.mask.bluetooth.gatt.GattCharacteristicWriteCallback;
import com.inteliclinic.neuroon.mask.bluetooth.gatt.GattConnectionCallback;
import com.inteliclinic.neuroon.mask.bluetooth.gatt.GattManager;
import com.inteliclinic.neuroon.models.bluetooth.BleDataFrame;
import com.inteliclinic.neuroon.utils.BytesUtils;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

abstract class BleManagerBase implements IBleManager, CharacteristicChangeListener {
    /* access modifiers changed from: private */
    public static final ArrayList<UUID> DFU_NOTIFIABLE_CHARACTERISTIC = new ArrayList<>();
    /* access modifiers changed from: private */
    public static final ArrayList<UUID> NOTIFIABLE_CHARACTERISTIC = new ArrayList<>();
    private static final short PACKETS_COUNT = 2048;
    private static final String TAG = BleManagerBase.class.getSimpleName();
    protected static final UUID UUID_BLE_LOG_RECEIVE_CHARACTERISTIC = UUID.fromString("D09E0401-D97F-E2D3-840C-A11CB81C0886");
    protected static final UUID UUID_DATA_RECEIVE_CHARACTERISTIC = UUID.fromString("D09E0301-D97F-E2D3-840C-A11CB81C0886");
    protected static final UUID UUID_DATA_SEND_CHARACTERISTIC = UUID.fromString("D09E0302-D97F-E2D3-840C-A11CB81C0886");
    protected static final UUID UUID_DFU_RECEIVE_CHARACTERISTIC = UUID.fromString("C7350801-D97F-E2D3-840C-A11CB81C0886");
    protected static final UUID UUID_DFU_SEND_CHARACTERISTIC = UUID.fromString("C7350802-D97F-E2D3-840C-A11CB81C0886");
    protected static final UUID UUID_DFU_SERVICE = UUID.fromString("C7350100-D97F-E2D3-840C-A11CB81C0886");
    protected static final UUID UUID_DFU_SETTINGS_RECEIVE_CHARACTERISTIC = UUID.fromString("C7350701-D97F-E2D3-840C-A11CB81C0886");
    protected static final UUID UUID_DFU_SETTINGS_SEND_CHARACTERISTIC = UUID.fromString("C7350702-D97F-E2D3-840C-A11CB81C0886");
    protected static final UUID UUID_SERVICE = UUID.fromString("D09E0100-D97F-E2D3-840C-A11CB81C0886");
    protected static final UUID UUID_SETTINGS_RECEIVE_CHARACTERISTIC = UUID.fromString("D09E0201-D97F-E2D3-840C-A11CB81C0886");
    protected static final UUID UUID_SETTINGS_SEND_CHARACTERISTIC = UUID.fromString("D09E0202-D97F-E2D3-840C-A11CB81C0886");
    private static BluetoothAdapter mBluetoothAdapter;
    private static BluetoothDevice mBluetoothDevice;
    /* access modifiers changed from: private */
    public Runnable mConnectWithDfuDeviceRunnable;
    private Context mContext;
    /* access modifiers changed from: private */
    public BluetoothDevice mDfuDevice;
    /* access modifiers changed from: private */
    public boolean mDfuUpdate;
    private int mFirmwareCrc;
    private byte[] mFirmwareData;
    private int mFirmwarePackageSent;
    private int mFirmwarePackageSize;
    private int mFirmwareSize;
    /* access modifiers changed from: private */
    public boolean mFirmwareStarted;
    private int mFirmwareType;
    /* access modifiers changed from: private */
    public final GattManager mGattManager;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mIsConnected;
    private boolean mStopDownloadSleep;
    private List<BleDataFrame> sleepData;
    private int sleepLength;
    private int sleepNum;
    private IMaskUserManager userManager;

    /* access modifiers changed from: protected */
    public abstract void scanLeDevice(boolean z);

    static {
        NOTIFIABLE_CHARACTERISTIC.add(UUID_DATA_RECEIVE_CHARACTERISTIC);
        NOTIFIABLE_CHARACTERISTIC.add(UUID_SETTINGS_RECEIVE_CHARACTERISTIC);
        DFU_NOTIFIABLE_CHARACTERISTIC.add(UUID_DFU_SETTINGS_RECEIVE_CHARACTERISTIC);
    }

    protected BleManagerBase(Context context, IMaskUserManager userManager2) {
        this.mContext = context;
        this.userManager = userManager2;
        this.mGattManager = new GattManager(context, this);
        EventBus.getDefault().register(this);
    }

    public static BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public Handler getHandler() {
        return this.mHandler;
    }

    public synchronized boolean isConnected() {
        return this.mIsConnected;
    }

    public void onEvent(DeviceStateEvent event) {
        this.mIsConnected = event.isConnected();
        if (isConnected()) {
            mBluetoothDevice = event.getDevice();
            return;
        }
        this.mStopDownloadSleep = false;
        if (this.mDfuUpdate && this.mFirmwareStarted) {
            this.mDfuUpdate = false;
            this.mFirmwareStarted = false;
            EventBus.getDefault().postSticky(new DfuUpdateFailEvent());
        } else if (this.mDfuUpdate && this.mConnectWithDfuDeviceRunnable != null) {
            this.mHandler.postDelayed(this.mConnectWithDfuDeviceRunnable, 100);
        }
        mBluetoothDevice = null;
    }

    public void onEvent(ReceivedLogEvent event) {
    }

    public void onEvent(ReceivedDfuSettingsEvent event) {
        boolean z = true;
        if (event.getStatus() == 17 && !this.mFirmwareStarted) {
            this.mFirmwareStarted = true;
            sendHeader(this.mFirmwareType, 1, this.mFirmwareSize, this.mFirmwareCrc);
            this.mFirmwarePackageSent = -1;
            sendFirmwareInt();
        } else if (event.getStatus() == 33) {
            sendFirmwareInt();
            EventBus.getDefault().post(new DfuProgressEvent(this.mFirmwarePackageSent, this.mFirmwarePackageSize));
        } else if (event.getStatus() == 34) {
            this.mFirmwarePackageSent--;
            sendFirmwareInt();
        } else if (event.getStatus() == 18) {
            this.mDfuUpdate = false;
            this.mFirmwareStarted = false;
            disconnectDevice();
            EventBus eventBus = EventBus.getDefault();
            if (this.mFirmwarePackageSent != this.mFirmwarePackageSize) {
                z = false;
            }
            eventBus.postSticky(new DfuSentEvent(z, System.currentTimeMillis()));
        }
    }

    private void sendHeader(int appType, int binVersion, int binLength, int binCrc) {
        byte[] dataToSend = new byte[20];
        int i = 0 + 1;
        dataToSend[0] = 3;
        int i2 = i + 1;
        dataToSend[i] = (byte) appType;
        int i3 = i2 + 1;
        dataToSend[i2] = (byte) (appType >> 8);
        int i4 = i3 + 1;
        dataToSend[i3] = (byte) (appType >> 16);
        int i5 = i4 + 1;
        dataToSend[i4] = (byte) (appType >> 24);
        int i6 = i5 + 1;
        dataToSend[i5] = (byte) binVersion;
        int i7 = i6 + 1;
        dataToSend[i6] = (byte) (binVersion >> 8);
        int i8 = i7 + 1;
        dataToSend[i7] = (byte) (binVersion >> 16);
        int i9 = i8 + 1;
        dataToSend[i8] = (byte) (binVersion >> 24);
        int i10 = i9 + 1;
        dataToSend[i9] = (byte) binLength;
        int i11 = i10 + 1;
        dataToSend[i10] = (byte) (binLength >> 8);
        int i12 = i11 + 1;
        dataToSend[i11] = (byte) (binLength >> 16);
        int i13 = i12 + 1;
        dataToSend[i12] = (byte) (binLength >> 24);
        int i14 = i13 + 1;
        dataToSend[i13] = (byte) binCrc;
        int i15 = i14 + 1;
        dataToSend[i14] = (byte) (binCrc >> 8);
        dataToSend[i15] = (byte) (binCrc >> 16);
        dataToSend[i15 + 1] = (byte) (binCrc >> 24);
        sendDfuSettings(dataToSend);
    }

    private void handleBleData(BleDataFrame characteristic) {
        if (characteristic.getFrameNumber() == 0) {
            this.sleepData = new ArrayList();
            this.sleepData.add(characteristic);
            this.sleepLength = characteristic.getSleepLength();
            this.mGattManager.requestPriority(1);
            EventBus.getDefault().postSticky(new ReceivingSleepEvent(this.sleepNum, characteristic.getFrameNumber(), this.sleepLength));
        } else if (characteristic.getFrameNumber() == 1) {
            if (this.sleepData == null) {
                return;
            }
            if (!isCorrectUser(characteristic.getUserId())) {
                int sleepNumber = this.sleepNum;
                this.sleepNum = -1;
                this.sleepLength = -1;
                MaskManager.getInstance().setMaskDownloadingSleep(false);
                EventBus.getDefault().post(new ReceivedSleepEvent(sleepNumber, this.sleepData, true));
                return;
            } else if (characteristic.getFrameNumber() == this.sleepData.size()) {
                this.sleepData.add(characteristic);
                EventBus.getDefault().postSticky(new ReceivingSleepEvent(this.sleepNum, characteristic.getFrameNumber(), this.sleepLength));
            } else {
                characteristic.setFrameNumber(this.sleepData.size());
            }
        } else if (characteristic.getFrameNumber() == 65535) {
            this.mGattManager.requestPriority(0);
            int sleepNumber2 = this.sleepNum;
            this.sleepNum = -1;
            this.sleepLength = -1;
            if (sleepNumber2 != -1) {
                MaskManager.getInstance().setMaskDownloadingSleep(false);
                EventBus.getDefault().post(new ReceivedSleepEvent(sleepNumber2, this.sleepData, false));
                return;
            }
            return;
        } else if (this.sleepData == null) {
            return;
        } else {
            if (characteristic.getFrameNumber() == this.sleepData.size()) {
                this.sleepData.add(characteristic);
                EventBus.getDefault().postSticky(new ReceivingSleepEvent(this.sleepNum, characteristic.getFrameNumber(), this.sleepLength));
            } else {
                characteristic.setFrameNumber(this.sleepData.size());
            }
        }
        if (this.mStopDownloadSleep) {
            this.mStopDownloadSleep = false;
            this.sleepNum = -1;
            this.sleepLength = -1;
            this.sleepData = null;
        } else if (!MaskManager.getInstance().isMultiplePacketsAvailable()) {
            sendData(new BleDataFrame(this.sleepNum, characteristic.getFrameNumber() + 1).toBytes());
        } else if (characteristic.getFrameNumber() % 2048 == 2047) {
            sendData(new BleDataFrame(this.sleepNum, characteristic.getFrameNumber() + 1, 2048).toBytes(), false);
        }
    }

    private boolean isCorrectUser(byte[] userId) {
        boolean a = Arrays.equals(userId, BytesUtils.toByteArray(170));
        boolean b = Arrays.equals(userId, this.userManager.getUserMaskHash());
        boolean c = Arrays.equals(userId, BytesUtils.toByteArray(0));
        if (a || b || c) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void getBluetooth() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
    }

    private void scanForDevices(boolean enable) {
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            scanLeDevice(enable);
        }
    }

    public void onCharacteristicChanged(String deviceAddress, BluetoothGattCharacteristic characteristic, byte[] value) {
        if (UUID_SETTINGS_RECEIVE_CHARACTERISTIC.equals(characteristic.getUuid())) {
            EventBus.getDefault().post(new ReceivedStateEvent(characteristic));
        } else if (UUID_DATA_RECEIVE_CHARACTERISTIC.equals(characteristic.getUuid())) {
            handleBleData(new BleDataFrame(this.sleepNum, value));
        } else if (UUID_BLE_LOG_RECEIVE_CHARACTERISTIC.equals(characteristic.getUuid())) {
            EventBus.getDefault().post(new ReceivedLogEvent(characteristic));
        } else if (UUID_DFU_RECEIVE_CHARACTERISTIC.equals(characteristic.getUuid())) {
            EventBus.getDefault().post(new ReceivedDfuEvent(characteristic));
        } else if (UUID_DFU_SETTINGS_RECEIVE_CHARACTERISTIC.equals(characteristic.getUuid())) {
            EventBus.getDefault().post(new ReceivedDfuSettingsEvent(characteristic));
        }
    }

    public void scanForDevices() {
        scanForDevices(true);
    }

    public void stopScan() {
        scanForDevices(false);
    }

    public void startBluetooth() {
        EventBus.getDefault().post(new ManagerStartedEvent(this));
        getBluetooth();
        if (MaskManager.getInstance().pairedWithDevice()) {
            scanForDevices(true);
        }
    }

    public void stopBluetooth() {
        scanForDevices(false);
        mBluetoothDevice = null;
        this.sleepData = null;
        this.mDfuUpdate = false;
        this.mIsConnected = false;
        this.mStopDownloadSleep = false;
    }

    public void connectWithDevice(BluetoothDevice device) {
        if (!this.mIsConnected || mBluetoothDevice == null) {
            scanForDevices(false);
            mBluetoothDevice = device;
            if (!this.mDfuUpdate) {
                this.mGattManager.connectDeviceAsync(device, new GattConnectionCallback() {
                    public void onConnected() {
                        Iterator it = BleManagerBase.NOTIFIABLE_CHARACTERISTIC.iterator();
                        while (it.hasNext()) {
                            UUID characteristic = (UUID) it.next();
                            if (BleManagerBase.this.mGattManager.hasDeviceCharacteristic(BleManagerBase.UUID_SERVICE, characteristic)) {
                                BleManagerBase.this.mGattManager.setNotification(BleManagerBase.UUID_SERVICE, characteristic, true);
                            } else {
                                BleManagerBase.this.mHandler.postDelayed(new Runnable() {
                                    public void run() {
                                        BleManagerBase.this.mGattManager.disconnect();
                                    }
                                }, 20);
                                return;
                            }
                        }
                    }

                    public void onError() {
                    }
                });
                return;
            }
            return;
        }
        throw new IllegalStateException("Already connected with device");
    }

    public void sendFirmwareData(byte[] firmwareData, int firmwareType, int firmwareSize, int firmwareCrc) {
        if (this.mIsConnected && mBluetoothDevice != null && !this.mDfuUpdate) {
            sendSettings(new BleSettingsCmdFrame(SettingsCharacteristic.BLE_CMD_GOTO_DFU).toBytes());
            this.mDfuUpdate = true;
            this.mFirmwareStarted = false;
            this.mFirmwareType = firmwareType;
            this.mFirmwareSize = firmwareSize;
            this.mFirmwareCrc = firmwareCrc;
            this.mFirmwarePackageSize = (int) Math.ceil(((double) this.mFirmwareSize) / 16.0d);
            this.mFirmwareData = firmwareData;
            this.mFirmwarePackageSent = 0;
            this.mDfuDevice = mBluetoothDevice;
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    if (BleManagerBase.this.mDfuUpdate && !BleManagerBase.this.mFirmwareStarted) {
                        boolean unused = BleManagerBase.this.mDfuUpdate = false;
                        EventBus.getDefault().postSticky(new DfuUpdateFailEvent());
                    }
                }
            }, 60000);
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    BleManagerBase.this.mGattManager.disconnect();
                }
            }, 1000);
            this.mConnectWithDfuDeviceRunnable = new Runnable() {
                public void run() {
                    BleManagerBase.this.mGattManager.connectDeviceAsync(BleManagerBase.this.mDfuDevice, new GattConnectionCallback() {
                        public void onConnected() {
                            Iterator it = BleManagerBase.DFU_NOTIFIABLE_CHARACTERISTIC.iterator();
                            while (it.hasNext()) {
                                UUID characteristic = (UUID) it.next();
                                if (BleManagerBase.this.mGattManager.hasDeviceCharacteristic(BleManagerBase.UUID_DFU_SERVICE, characteristic)) {
                                    BleManagerBase.this.mGattManager.setNotification(BleManagerBase.UUID_DFU_SERVICE, characteristic, true);
                                } else {
                                    BleManagerBase.this.mHandler.postDelayed(new Runnable() {
                                        public void run() {
                                            BleManagerBase.this.mGattManager.disconnect();
                                        }
                                    }, 20);
                                    return;
                                }
                            }
                        }

                        public void onError() {
                            if (BleManagerBase.this.mDfuUpdate && BleManagerBase.this.mConnectWithDfuDeviceRunnable != null) {
                                BleManagerBase.this.mHandler.postDelayed(BleManagerBase.this.mConnectWithDfuDeviceRunnable, 100);
                            }
                        }
                    });
                }
            };
            this.mHandler.postDelayed(this.mConnectWithDfuDeviceRunnable, 4000);
        }
    }

    public void resetAll() {
        stopBluetooth();
        getBluetooth();
    }

    public void sendSettings(byte[] settingsData) {
        if (this.mIsConnected && mBluetoothDevice != null && !this.mDfuUpdate) {
            this.mGattManager.writeCharacteristic(UUID_SERVICE, UUID_SETTINGS_SEND_CHARACTERISTIC, settingsData);
        }
    }

    public void sendData(byte[] data) {
        sendData(data, true);
    }

    private void sendData(byte[] data, boolean handle) {
        if (this.mIsConnected && mBluetoothDevice != null && !this.mDfuUpdate) {
            if (handle) {
                this.mGattManager.writeCharacteristic(UUID_SERVICE, UUID_DATA_SEND_CHARACTERISTIC, data);
            } else {
                this.mGattManager.writeCharacteristic(UUID_SERVICE, UUID_DATA_SEND_CHARACTERISTIC, data, false);
            }
        }
    }

    public void sendSettings(BluetoothDevice device, final byte[] bytes) {
        this.mGattManager.disconnect();
        this.mGattManager.connectDeviceAsync(device, new GattConnectionCallback() {
            public void onConnected() {
                BleManagerBase.this.mGattManager.writeCharacteristicAsync(BleManagerBase.UUID_SERVICE, BleManagerBase.UUID_SETTINGS_SEND_CHARACTERISTIC, bytes, new GattCharacteristicWriteCallback() {
                    public void onEnd() {
                        BleManagerBase.this.mGattManager.disconnect();
                    }
                });
            }

            public void onError() {
            }
        });
    }

    public boolean isMaskUpdating() {
        return this.mDfuUpdate;
    }

    public void disconnectDevice() {
        if (mBluetoothDevice != null) {
            this.mGattManager.disconnect();
        }
    }

    public void connectWithDfuDevice() {
    }

    public void stopDownloadSleep() {
        this.mStopDownloadSleep = true;
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                EventBus.getDefault().post(new MaskDownloadStoppedEvent());
            }
        }, 200);
    }

    public void clearMaskData() {
        sendSettings(new BleSettingsCmdWithKeyFrame(SettingsCharacteristic.BLE_CMD_EPOCH_CLEAR, 2135833481).toBytes());
    }

    public void clearMaskRawData() {
        sendSettings(new BleSettingsCmdWithKeyFrame(SettingsCharacteristic.BLE_CMD_RAW_CLEAR, 1550525843).toBytes());
    }

    public void downloadSleep(int i) {
        this.sleepNum = i;
        if (!MaskManager.getInstance().isMultiplePacketsAvailable()) {
            sendData(new BleDataFrame(i, 0).toBytes());
        } else {
            sendData(new BleDataFrame(i, 0, 2048).toBytes());
        }
    }

    public void sendDfuSettings(byte[] settingsData) {
        if (this.mIsConnected && mBluetoothDevice != null && this.mDfuUpdate) {
            this.mGattManager.writeCharacteristic(UUID_DFU_SERVICE, UUID_DFU_SETTINGS_SEND_CHARACTERISTIC, settingsData);
        }
    }

    private void sendFirmwareInt() {
        this.mFirmwarePackageSent++;
        int startPackageNum = this.mFirmwarePackageSent * 16;
        if (startPackageNum < this.mFirmwareSize) {
            byte[] bytes = Arrays.copyOfRange(this.mFirmwareData, startPackageNum, startPackageNum + 16);
            int i = this.mFirmwareData.length - startPackageNum;
            if (i < 16) {
                while (i < 16) {
                    bytes[i] = -1;
                    i++;
                }
            }
            sendFirmwareInt(bytes, this.mFirmwarePackageSent + 1);
            return;
        }
        byte[] dataToSend = new byte[20];
        dataToSend[0] = 0;
        sendDfuSettings(dataToSend);
    }

    private void sendFirmwareInt(byte[] settingsData, int packetNumber) {
        if (this.mIsConnected && mBluetoothDevice != null && this.mDfuUpdate) {
            byte[] copyDataToSent = new byte[20];
            copyDataToSent[2] = (byte) packetNumber;
            copyDataToSent[3] = (byte) (packetNumber >> 8);
            System.arraycopy(settingsData, 0, copyDataToSent, 4, 16);
            int i = BytesUtils.crc16(Arrays.copyOfRange(copyDataToSent, 2, 20));
            copyDataToSent[0] = (byte) i;
            copyDataToSent[1] = (byte) (i >> 8);
            this.mGattManager.writeCharacteristic(UUID_DFU_SERVICE, UUID_DFU_SEND_CHARACTERISTIC, copyDataToSent);
        }
    }
}
