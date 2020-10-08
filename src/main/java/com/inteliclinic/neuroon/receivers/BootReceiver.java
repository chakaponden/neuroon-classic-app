package com.inteliclinic.neuroon.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.inteliclinic.neuroon.service.NeuroonService;

public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, NeuroonService.class));
    }
}
