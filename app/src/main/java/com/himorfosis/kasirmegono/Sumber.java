package com.himorfosis.kasirmegono;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class Sumber {

    //the constants
    public static void saveData (String DBNAME, String Tablekey, String value, Context context){

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(DBNAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(Tablekey, value);
        editor.commit();
    }

    public static String getData (String DBNAME, String Tablekey, Context context){

        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(DBNAME, Context.MODE_PRIVATE);
        text = settings.getString(Tablekey, null);
        return text;
    }

    public static void deleteData (String DBNAME, Context context){

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(DBNAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.clear();
        editor.commit();
    }

    //the constants
    public static void saveDataInt (String DBNAME, String Tablekey, int value, Context context){

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(DBNAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt(Tablekey, value);
        editor.commit();
    }

    public static int getDataInt (String DBNAME, String Tablekey, Context context){

        SharedPreferences settings;
        int text;
        settings = context.getSharedPreferences(DBNAME, Context.MODE_PRIVATE);
        text = settings.getInt(Tablekey, 0);
        return text;
    }

    public static void deleteDataInt (String DBNAME, Context context){

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(DBNAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.clear();
        editor.commit();
    }


    public static void logShow(String message, String value) {
        Log.e("", message + " : " + value);
    }

    public static void toastShow(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

    }

    public static void toastShow(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

    }

    public static void dialogText(Context context, String text) {

        ProgressDialog pDialog;
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);

        pDialog.setMessage(text);

    }

    public static void dialogShow(Context context) {

        ProgressDialog pDialog;

        // Progress dialog
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);

        if (!pDialog.isShowing())
            pDialog.show();

    }

    public static void dialogHide(Context context) {

        ProgressDialog pDialog;

        // Progress dialog
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);

        if (pDialog.isShowing())
            pDialog.dismiss();

    }

}
