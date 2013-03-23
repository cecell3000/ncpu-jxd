package com.vektor.ncpu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

public class StartUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive( final Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
        	
        	Intent serviceIntent = new Intent(context, BootService.class);
        	context.startService(serviceIntent);
       }
    }
}
