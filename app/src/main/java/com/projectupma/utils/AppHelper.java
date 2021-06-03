package com.projectupma.utils;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.projectupma.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppHelper {
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static SharedPreferences sharedPreferences;

    public static String branchConverter(String branch) {
        String branchShort;
        switch (branch) {
            case "Computer Science":
                branchShort = "CS";
                break;
            case "Information Technology":
                branchShort = "IT";
                break;
            case "Civil Engineering":
                branchShort = "CE";
                break;
            case "Industrial Production":
                branchShort = "IP";
                break;
            case "Mechanical Engineering":
                branchShort = "ME";
                break;
            case "Electrical Engineering":
                branchShort = "EE";
                break;
            case "Electronic and Telecommunication":
                branchShort = "EC";
                break;
            default:
                branchShort = "CS";
        }
        return branchShort;
    }

    public static String shortToBranchConverter(String branchShort) {
        String branch;
        switch (branchShort) {
            case "CS":
                branch = "Computer Science";
                break;
            case "IT":
                branch = "Information Technology";
                break;
            case "CE":
                branch = "Civil Engineering";
                break;
            case "IP":
                branch = "Industrial Production";
                break;
            case "ME":
                branch = "Mechanical Engineering";
                break;
            case "EE":
                branch = "Electrical Engineering";
                break;
            case "EC":
                branch = "Electronic and Telecommunication";
                break;
            default:
                branch = "";
        }
        return branch;
    }

    public static String branchToShortConverter(String branch) {
        String branchShort;
        switch (branch) {
            case "Common-1st year":
                branchShort = "ALL";
                break;
            case "Computer Science":
                branchShort = "CS";
                break;
            case "Information Technology":
                branchShort = "IT";
                break;
            case "Civil Engineering":
                branchShort = "CE";
                break;
            case "Industrial Production":
                branchShort = "IP";
                break;
            case "Mechanical Engineering":
                branchShort = "ME";
                break;
            case "Electrical Engineering":
                branchShort = "EE";
                break;
            case "Electronic and Telecommunication":
                branchShort = "EC";
                break;
            default:
                branchShort = "CS";
        }
        return branchShort;
    }

    public static String convertTypetoInt(String type) {
        Integer num;
        switch (type) {
            case "Previous Year Papers":
                num = 1;
                break;
            case "Books":
                num = 2;
                break;
            case "Notes":
                num = 3;
                break;
            case "Lab Work":
                num = 4;
                break;
            case "Others":
                num = 5;
                break;
            default:
                num = 2;
        }
        return num.toString();
    }

    public static void logout() {
        mAuth.signOut();
    }

    public static String getDateFromTimestamp(Timestamp date) {
        Date formatedDate = date.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy ");
        String S = sdf.format(formatedDate);
        return S.toString();


    }
    public static boolean isNightMode() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            return true;
        } else {
            return false;

        }
    }
    public static void replaceFragments(Class fragmentClass, FragmentActivity activity, Bundle bundle) {
        Fragment fragment = null;
//        currentFragment = fragmentClass.getSimpleName();

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragment.setArguments(bundle);
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.dashboard_frameLayout, fragment).addToBackStack(null)
                .commit();
    }
}