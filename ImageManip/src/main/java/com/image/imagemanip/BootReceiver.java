package com.image.imagemanip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.appbucks.sdk.AppBucksAPI;

/**
 * Created by sudheendra.sn on 10/29/13.
 */
public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent)
    {
        AppBucksAPI.initialize(context, "Image Effects", R.drawable.ic_launcher, 429390694, "a495d0e3-6919-4c07-b8eb-83a7d7d8ae61", false, null, null);
    }
}
