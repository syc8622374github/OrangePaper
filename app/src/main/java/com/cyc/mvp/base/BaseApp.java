package com.cyc.mvp.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by cyc on 2017/1/19 0019.
 */

public class BaseApp extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
