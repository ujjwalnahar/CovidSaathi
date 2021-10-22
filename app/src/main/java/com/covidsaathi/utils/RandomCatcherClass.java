package com.covidsaathi.utils;

import android.content.Context;
import android.content.res.TypedArray;

import com.covidsaathi.R;

public class RandomCatcherClass {
    public static TypedArray getColorList(Context context) {
        TypedArray ta = context.getResources().obtainTypedArray(R.array.colorList);
        int[] colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }
        return ta;
    }
    public static String getRandomGreeting() {
        String line = "Greetings, Bonjour, What's up, Hi, Hey, Howdy-do, Howdy, Yo, Welcome, Good day, How are you, How u doin', Wassup, Yahallo";
        return line;
    }
}
