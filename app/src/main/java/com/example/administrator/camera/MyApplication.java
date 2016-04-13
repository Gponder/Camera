package com.example.administrator.camera;

import android.app.Application;

/**
 * Created by Administrator on 2016/4/13.
 */
public class MyApplication extends Application{

    private static MyApplication appInstance;

    private String id;

    public String getId() {
        return id;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.id = "member";
    }

    public static MyApplication getAppInstance(){
        if (appInstance == null){
            appInstance = new MyApplication();
        }
        return appInstance;
    }
}
