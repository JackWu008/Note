package com.jackwu.note.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JackWu on 2017/9/3.
 */

public class AppUtils extends Application {
    private List<Activity> activities;
    private static AppUtils context;

    public AppUtils() {

    }


    @Override
    public void onCreate() {
        super.onCreate();
        activities = new ArrayList<>();
        context = this;
    }

    public static Context getContext() {
        return context;
    }

    public  void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void allFinishActivity() {
        for (Activity a : activities)
            if (a != null)
                a.finish();
        System.exit(0);
    }


    public static boolean isConnectivity() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        //return info != null && info.isAvailable();
        return info != null && info.isConnected();
    }

}
