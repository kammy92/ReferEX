package com.referex.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.referex.utils.UserDetailsPref;
import com.referex.utils.Utils;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName ();
    @Override
    public void onTokenRefresh () {
        super.onTokenRefresh ();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Utils.showLog (Log.DEBUG, TAG, "Refreshed Token: " + refreshedToken, true);

        UserDetailsPref userDetailsPref = UserDetailsPref.getInstance();
        userDetailsPref.putStringPref(getApplicationContext(), UserDetailsPref.USER_FIREBASE_ID, refreshedToken);
    }
}

