package com.savecontroladoria.tarefa;

/**
 * Created by ADMIN on 17/11/2015.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootComplete extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("BootComp", "entrou no boot");
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Log.i("BootComp", "entrou na tarefa do boot");
            MyTaskService.cancelAll(context);
            MyTaskService.scheduleRepeat(context);
        }
    }

}
