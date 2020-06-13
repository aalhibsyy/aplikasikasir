package com.himorfosis.kasirmegono.Util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by astria on 30/05/2017.
 */

public class MySingelton {
    private static MySingelton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private MySingelton(Context context){
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue(){
        if(requestQueue==null)
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        return requestQueue;
    }

    public static synchronized MySingelton getmInstance(Context context){
        if(mInstance==null){
            mInstance = new MySingelton(context);
        }
        return mInstance;
    }

    public<T> void addToRequestQue(Request<T> request){
        getRequestQueue().add(request);
    }
}
