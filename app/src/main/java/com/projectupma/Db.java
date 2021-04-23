package com.projectupma;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.projectupma.models.UserModel;

public class Db {
    public final static  String CODE = "Code";
    public final static String DATABASE ="test";
    public final static String SLIDER = DATABASE +"/DASHBOARD/SLIDER";
    public final static String USER = DATABASE +"/USERDOC/USER";
    public final static String SEMESTER="semester";
    public final static String USERID="userId";

    public final static String SUBJECTCODE="subject_code";
    public final static String RESOURCES= DATABASE +"/RESOURCES";
    public final static String DATABASE_NAME = "test";
    public final static String DASHBOARD_SLIDER = DATABASE_NAME + "/DASHBOARD/SLIDER";
    public final static String BASE = DATABASE_NAME + "/BASE";
    public final static String USERDOC_USER = DATABASE_NAME + "/USERDOC/USER";
    public final static String SUBJECTS = DATABASE_NAME + "/SUBJECTS/subjects";
    public final static String TIMETABLE = DATABASE_NAME + "/TIMETABLE";
    public final static String JEC_NOTICE = BASE + "/JEC_NOTICE";
    public final static String JEC_EXAM_NOTICE = BASE + "/JEC_EXAM_NOTICE";
    public final static String NAME = "name";
    public final static String APPROVED = "approved";
    public final static String AUTH = "auth";
    public final static String BRANCH = "branch";
    public final static String EMAIL = "email";
    public final static String PHONE_NO = "phone_no";
    public final static String ID_CARD_URL = "idcard_url";
    public final static String DATE_CREATED = "date_created";
    public final static String PHOTO_URL = "photo_url";
    public final static String ROLL_NO = "roll_no";
    public final static String USER_ID = "user_id";
    public final static String REWARD_ID = "reward_id";
    public final static String CS = "CS";
    public final static String IT = "IT";
    public final static String EC = "EC";
    public final static String EE = "EE";
    public final static String ME = "ME";
    public final static String IP = "IP";
    public static UserModel userModel;
    public static String getUserDoc(String userID) {
        return USERDOC_USER + "/" + userID;
    }

    public static String getTimeTableDoc(String branch, String sem) {
        return TIMETABLE + "/" + branch + "/" + sem;
    }

    public static String getStaticImages() {
        return BASE + "/STATIC_IMAGES/HOME_BACKGROUND";
    }
    public static String getProfileImages() {
        return BASE + "/STATIC_IMAGES/PROFILE_IMAGES";
    }


    public static UserModel getUserModel() {
        return userModel;
    }
}
