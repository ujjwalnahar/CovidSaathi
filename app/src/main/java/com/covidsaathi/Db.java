package com.covidsaathi;

import com.google.firebase.firestore.FirebaseFirestore;
import com.covidsaathi.models.UserModel;

import java.util.ArrayList;

public class Db {

    public final static String SUBJECT_CODE = "Code";
    public final static String DATABASE_COL = "test";
    public final static String USER_COL = DATABASE_COL + "/USERDOC/USER";
    public final static String BASE_DOC = DATABASE_COL + "/BASE";
    public final static String RANDOM_PROFILE_IMAGES_DOC = BASE_DOC + "/STATIC_IMAGES/PROFILE_IMAGES";
    public final static String BACKGROUND_PROFILE_IMAGES_DOC = BASE_DOC + "/STATIC_IMAGES/PROFILE_BACKGROUND_IMAGES";
    public final static String RESOURCES_DOC = DATABASE_COL + "/RESOURCES";
    public final static String DASHBOARD_SLIDER = DATABASE_COL + "/DASHBOARD/SLIDER";
    public final static String SUBJECTS_COL = DATABASE_COL + "/SUBJECTS/subjects";
    public final static String TIMETABLE_COL = DATABASE_COL + "/TIMETABLE";
    public final static String JEC_NOTICE_COL = BASE_DOC + "/JEC_NOTICE";
    public final static String JEC_EXAM_NOTICE_COL = BASE_DOC + "/JEC_EXAM_NOTICE";
    public final static String COMMENTS_COL = DATABASE_COL + "/DIZQUS/COMMENTS";
    public final static String DIZCUS_THREAD_COL = DATABASE_COL + "/DIZQUS/THREAD";
    public final static String CLUBS_LIST_COL=DATABASE_COL+"/CLUBS/clubs";
    public final static String CONST_SEMESTER = "semester";
    public final static String CONST_USERID = "userId";
    public final static String CONST_BRANCH = "branch";
    public final static String CONST_SUBJECTCODE = "subject_code";
    public final static String STUDENT = "student";
    public final static String ADMIN = "admin";
    public final static String SOCIETY = "society";
    public final static String CLASSREPRESENTATIVE = "classRepresentative";

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static ArrayList<String> PROFILE_BACKGROUND_URLS;
    public static UserModel userModel;

    public static String getUserDoc(String userID) {
        return USER_COL + "/" + userID;
    }

    public static String getTimeTableDoc(String branch, String sem) {
        return TIMETABLE_COL + "/" + branch + "/" + sem;
    }

    public static String getStaticImages() {
        return BASE_DOC + "/STATIC_IMAGES/HOME_BACKGROUND";
    }


    public static UserModel getUserModel() {
        return userModel;
    }
}
