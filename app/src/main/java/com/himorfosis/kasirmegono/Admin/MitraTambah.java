package com.himorfosis.kasirmegono.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;
import com.himorfosis.kasirmegono.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MitraTambah extends AppCompatActivity {

    EditText nama, email, password;
    String getid, getnama, getemail, getpassword, getdata;

    Button tambah;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mitra_tambah);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbarclose);

        TextView textToolbar = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.toolbartext);
        Button kembali = (Button) getSupportActionBar().getCustomView().findViewById(R.id.kembali);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        nama = findViewById(R.id.nama);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        tambah = findViewById(R.id.tambah);

        Intent data = getIntent();

        getdata = data.getStringExtra("data");
        getid = data.getStringExtra("id");
        getnama = data.getStringExtra("nama");
        getemail = data.getStringExtra("email");
        getpassword = data.getStringExtra("password");

        Log.e("id", "" +getid);

        if (getdata.equals("update")) {

            nama.setText(getnama);
            email.setText(getemail);
            password.setText(getpassword);

            tambah.setText("Update Mitra");

            textToolbar.setText("Update Mitra");

        } else {

            textToolbar.setText("Tambah Mitra");

        }

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(MitraTambah.this, Admin.class);
                startActivity(in);

            }
        });

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getnama = nama.getText().toString();
                getemail = email.getText().toString();
                getpassword = password.getText().toString();

                if (getnama.equals("") || getemail.equals("") || getpassword.equals("")) {

                    Sumber.toastShow(getApplicationContext(), "Harap isi secara lengkap");

                } else {

                    if (getdata.equals("update")) {

                        mitraUpdate();

                    } else {


                        mitraTambah();
                    }

                }

            }
        });

    }

    private void mitraTambah() {

        pDialog.setMessage("Tambah Mitra ...");
        showDialog();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Koneksi.mitra_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If we are getting success from server

                Log.e("response", " " + response);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    Log.e("obj", "" + obj);

                    if (!obj.getBoolean("error")) {

                        Toast.makeText(MitraTambah.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //Starting profile activity
                        Intent intent = new Intent(MitraTambah.this, Admin.class);
                        startActivity(intent);

                    } else {

                        //If the server response is not success
                        //Displaying an error message on toast
//                        Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        hideDialog();

                        Toast.makeText(MitraTambah.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                        Intent in = new Intent(MitraTambah.this, Admin.class);
                        startActivity(in);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    hideDialog();

                    Intent in = new Intent(MitraTambah.this, Admin.class);
                    startActivity(in);

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        hideDialog();

                        Toast.makeText(MitraTambah.this, "Tambah Gagal", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //Adding parameters to request
                params.put("id_mitra", String.valueOf(getid));
                params.put("nama_mitra", getnama);
                params.put("email", getemail);
                params.put("password", getpassword);

                //returning parameter
                return params;
            }
        };

        Volley.getInstance().addToRequestQueue(stringRequest);

    }

    private void mitraUpdate() {

        pDialog.setMessage("Update Mitra ...");
        showDialog();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Koneksi.mitra_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If we are getting success from server

                Log.e("response", " " + response);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    Log.e("obj", "" + obj);

                    if (!obj.getBoolean("error")) {

                        Toast.makeText(MitraTambah.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //Starting profile activity
                        Intent intent = new Intent(MitraTambah.this, Admin.class);
                        startActivity(intent);

                    } else {
                        //If the server response is not success
                        //Displaying an error message on toast
//                        Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        hideDialog();

                        Toast.makeText(MitraTambah.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                        Intent in = new Intent(MitraTambah.this, Admin.class);
                        startActivity(in);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    hideDialog();

                    Intent in = new Intent(MitraTambah.this, Admin.class);
                    startActivity(in);

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        hideDialog();

                        Toast.makeText(MitraTambah.this, "Update Gagal", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //Adding parameters to request
                params.put("id_mitra", String.valueOf(getid));
                params.put("nama_mitra", getnama);
                params.put("email", getemail);
                params.put("password", getpassword);

                //returning parameter
                return params;
            }
        };

        Volley.getInstance().addToRequestQueue(stringRequest);
    }

    private void showDialog() {

        if (!pDialog.isShowing())
            pDialog.show();

    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
