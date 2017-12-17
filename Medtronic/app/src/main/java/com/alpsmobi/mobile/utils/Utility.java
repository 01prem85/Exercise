package com.alpsmobi.mobile.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;

public class Utility {

    public static boolean isNetWorkConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean checkWritePermission(Context context){
        return (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }
}
