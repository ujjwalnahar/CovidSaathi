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
public static String branchConverter(String branch){
    String branchShort;
    switch (branch){
        case "Computer Science":branchShort="CS";break;
        case "Information Technology":branchShort="IT";break;
        case "Civil Engineering":branchShort="CE";break;
        case "Industrial Production":branchShort="IP";break;
        case "Mechanical Engineering":branchShort="ME";break;
        case "Electrical Engineering":branchShort="EE";break;
        case "Electronic and Telecommunication":branchShort="EC";break;
        default:branchShort="CS";
    }
    return branchShort;
}
public static String convertTypetoInt(String type){
        Integer num;
    switch (type){
        case "Previous Year Papers":num=1;break;
        case "Books":num=2;break;
        case "Notes":num=3;break;
        case "Lab Work":num=4;break;
        case "Others":num=5;break;
        default:num=2;}
     return num.toString();
}
}
