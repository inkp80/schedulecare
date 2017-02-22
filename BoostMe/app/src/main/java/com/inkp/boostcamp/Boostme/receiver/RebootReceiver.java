package com.inkp.boostcamp.Boostme.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.inkp.boostcamp.Boostme.NotificationUtills;
import com.inkp.boostcamp.Boostme.Service.Rebooted;
import com.inkp.boostcamp.Boostme.activities.MainActivity;

/**
 * Created by inkp on 2017-02-22.
 */

public class RebootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            final Intent RebootIntent = new Intent(context, Rebooted.class);
            Intent intentBootNoti = new Intent(context, MainActivity.class);
            NotificationUtills.NotificationSomethings(context, intentBootNoti);
            context.startService(RebootIntent);
        } else {
            Log.e("Rebooted,", "Received unexpected intent " + intent.toString());
        }
    }
}