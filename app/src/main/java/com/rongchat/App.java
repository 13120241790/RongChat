package com.rongchat;

import android.app.Application;
import android.content.Context;

import io.rong.imkit.RongIM;

/**
 * Created by AMing on 16/3/18.
 * Company RongCloud
 */
public class App extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        RongIM.init(this);
    }

    public static Context getInstance() {
        return mContext;
    }
}
