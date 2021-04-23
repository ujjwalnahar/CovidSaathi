package com.projectupma.utils;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.projectupma.R;

public class AnimationClass {

    final static int durationalpha=500;
    final static int durationtransition=500;
    final static int durationscale=500;

    public static void setAnimationLTR(View view) {
        AnimationSet set = new AnimationSet(true);
        AlphaAnimation anim1 = new AlphaAnimation(0.0f, 1.0f);

        TranslateAnimation anim2 = new TranslateAnimation(-200, 0, 0, 0);

        ScaleAnimation anim3 = new ScaleAnimation(1.0f, 1.0f, 1.0f, 1.0f, android.view.animation.Animation.RELATIVE_TO_SELF, 0.0f, android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(durationalpha);
        anim2.setDuration(durationtransition);
        anim3.setDuration(durationscale);
        set.addAnimation(anim1);
        set.addAnimation(anim2);
        set.addAnimation(anim3);
        view.startAnimation(set);
    }
    public static void setAnimationRTL(View view) {
        AnimationSet set = new AnimationSet(true);
        AlphaAnimation anim1 = new AlphaAnimation(0.0f, 1.0f);

        TranslateAnimation anim2 = new TranslateAnimation(200, 0, 0, 0);
        ScaleAnimation anim3 = new ScaleAnimation(1.0f, 1.0f, 1.0f, 1.0f, android.view.animation.Animation.RELATIVE_TO_SELF, 1.0f, android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(durationalpha);
        anim2.setDuration(durationtransition);
        anim3.setDuration(durationscale);
        set.addAnimation(anim1);
        set.addAnimation(anim2);
        set.addAnimation(anim3);
        view.startAnimation(set);
    }
    public static void setAnimationBTT(View view) {
        AnimationSet set = new AnimationSet(true);
        AlphaAnimation anim1 = new AlphaAnimation(0.0f, 1.0f);

        TranslateAnimation anim2 = new TranslateAnimation(0, 0, 100, 0);
        ScaleAnimation anim3 = new ScaleAnimation(1.0f, 1.0f, 1.0f, 1.0f, android.view.animation.Animation.RELATIVE_TO_SELF, 1.0f, android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(durationalpha);
        anim2.setDuration(durationtransition);
        anim3.setDuration(durationscale);
        set.addAnimation(anim1);
        set.addAnimation(anim2);
        set.addAnimation(anim3);
        view.startAnimation(set);
    }
    public static void setAnimationTTB(View view) {
        AnimationSet set = new AnimationSet(true);
        AlphaAnimation anim1 = new AlphaAnimation(0.0f, 1.0f);

        TranslateAnimation anim2 = new TranslateAnimation(0, 0, -100, 0);
        ScaleAnimation anim3 = new ScaleAnimation(1.05f, 1.0f, 1.05f, 1.0f, android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f, android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(durationalpha);
        anim2.setDuration(durationtransition);
        anim3.setDuration(durationscale);
        set.addAnimation(anim1);
        set.addAnimation(anim2);
        set.addAnimation(anim3);
        view.startAnimation(set);
    }
    public static void setAnimationNeu(View view) {
        AnimationSet set = new AnimationSet(true);
        AlphaAnimation anim1 = new AlphaAnimation(0.0f, 1.0f);

        TranslateAnimation anim2 = new TranslateAnimation(0, 0, 10, 0);
        ScaleAnimation anim3 = new ScaleAnimation(1.05f, 1.0f, 1.05f, 1.0f, android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f, android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setDuration(durationalpha+500);
        anim2.setDuration(durationtransition);
        anim3.setDuration(durationscale+500);
        set.addAnimation(anim1);
        set.addAnimation(anim2);
        set.addAnimation(anim3);
        view.startAnimation(set);
    }
}