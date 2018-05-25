package com.example.hp.studyforfun;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by hp on 18-03-2018.
 */

public class Myapplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
