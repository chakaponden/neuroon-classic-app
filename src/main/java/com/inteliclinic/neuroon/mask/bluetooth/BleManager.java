package com.inteliclinic.neuroon.mask.bluetooth;

import android.content.Context;
import android.os.Build;
import com.inteliclinic.lucid.IMaskUserManager;
import com.inteliclinic.neuroon.managers.BaseManager;

public final class BleManager extends BaseManager {
    private static IBleManager instance;

    private BleManager() {
    }

    public static IBleManager getInstance() {
        if (instance != null) {
            return instance;
        }
        throw new NullPointerException();
    }

    public static IBleManager getInstance(Context context, IMaskUserManager userManager) {
        if (instance == null) {
            if (Build.VERSION.SDK_INT >= 21) {
                instance = new BleManagerLolipopImpl(context, userManager);
            } else if (Build.VERSION.SDK_INT >= 18) {
                instance = new BleManagerJBImpl(context, userManager);
            } else {
                throw new UnsupportedClassVersionError();
            }
        }
        return instance;
    }

    public String getLucidDelegateKey() {
        return "BleManager";
    }
}
