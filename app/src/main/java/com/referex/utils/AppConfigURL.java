package com.referex.utils;

public class AppConfigURL {
    //   public static String BASE_URL = "https://project-clearsale-cammy92.c9users.io/";
    
    
    public static String version = "v1";
    public static String BASE_URL = "https://project-referex-cammy92.c9users.io/api/" + version + "/";
    
    public static String URL_LOGIN = BASE_URL + "referee/login";
    public static String URL_SIGN_UP = BASE_URL + "referee/register";
    
    public static String URL_GETOTP = BASE_URL + "user/otp";
    public static String URL_REGISTER = BASE_URL + "user/register";
    
    public static String URL_LOCATIONS = BASE_URL + "locations";
    public static String URL_SKILL = BASE_URL + "skills/parent";
    
    public static String URL_FEEDBACK = BASE_URL + "feedback";
    public static String URL_INIT = BASE_URL + "/init/application/";
    public static String URL_BOOKMARKED = BASE_URL + "/jobs/bookmarked";
    public static String URL_BOOKMARKED_JOB = BASE_URL + "/bookmark/job";
    public static String URL_RECOMMENDED = BASE_URL + "jobs/recommended";
    public static String URL_SUBMIT_PROFILE = BASE_URL + "submit/profile";
}