package com.himorfosis.kasirmegono.Util;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by INEU on 27/05/2017.
 */

public class AppController extends Application {
    private static final String TAG = AppController.class.getSimpleName();
    private static AppController instance;
    RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
    public static synchronized AppController getInstance(){
        return instance;
    }
    private RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    public <I> void addToRequestQueue(Request<I> req, String tag){
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <I> void addToRequestQueue(Request<I> req){
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    public void cancelALlRequest(Object req){
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(req);
        }
    }
}
