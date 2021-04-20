package com.projectupma.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class AppHelper {
    static SharedPreferences sharedPreferences;

    public static AppHelper getInstance() {
        return new AppHelper();
    }

    public static boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected())
            return true;
        return false;
    }

    public boolean checkUserLoggedIn(Context context) {
        SharedPreferences shared = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Boolean bool = shared.getBoolean("isSignedIn", false);
        Toast.makeText(context, bool.toString(), Toast.LENGTH_LONG).show();
        if (shared.getBoolean("isSignedIn", false)) {
            return true;
        }
        return false;
    }

}
