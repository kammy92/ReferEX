package com.referex.utils;

public class AppConfigURL {
    //   public static String BASE_URL = "https://project-clearsale-cammy92.c9users.io/";


    public static String version = "v1";
    public static String BASE_URL = "https://project-referex-cammy92.c9users.io/api/" + version + "/";

    public static String URL_LOGIN = BASE_URL + "referee/login";
    public static String URL_SIGN_UP = BASE_URL + "referee/register";

    public static String URL_GETOTP = BASE_URL + "user/otp";
    public static String URL_REGISTER = BASE_URL + "user/register";

    public static String URL_SKILL = BASE_URL + "skills/parent";

    

}