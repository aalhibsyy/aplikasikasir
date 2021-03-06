package com.himorfosis.kasirmegono.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.himorfosis.kasirmegono.Database;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;
import com.himorfosis.kasirmegono.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PenggunaDetail extends AppCompatActivity {

    TextView username, phone, level, nama, password;

    String getusername, getphone, getlevel, getid, getnama, getpass, namalevel, getToken;
    Database db;
    Button update, delete;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengguna_detail);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbarclose);

        TextView textToolbar = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.toolbartext);
        Button kembali = (Button) getSupportActionBar().getCustomView().findViewById(R.id.kembali);

        textToolbar.setText("Pengguna Detail");

        username = findViewById(R.id.username);
        phone = findViewById(R.id.phone);
        level = findViewById(R.id.level);
        nama = findViewById(R.id.nama);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);

        Intent data = getIntent();

        getid = data.getStringExtra("id");
        getusername = data.getStringExtra("username");
        getphone = data.getStringExtra("phone");
        getlevel = data.getStringExtra("level");
        getnama = data.getStringExtra("nama");
        getpass = data.getStringExtra("password");

        if (getlevel.equals("1")) {
            namalevel= "Admin";
        } else if(getlevel.equals("2")) {
            namalevel= "Kasir";
        }

        username.setText(getusername);
        phone.setText(getphone);
        level.setText(namalevel);
        nama.setText(getnama);

        db = new Database(getApplicationContext());
        getToken = Sumber.getData("akun", "token", getApplicationContext());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getApplicationContext(), PenggunaTambah.class);

                in.putExtra("data", "update");
                in.putExtra("id", getid);
                in.putExtra("nama", getnama);
                in.putExtra("username", getusername);
                in.putExtra("password", getpass);
                in.putExtra("phone", getphone);
                in.putExtra("level", getlevel);

                startActivity(in);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();

            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void delete() {

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Koneksi.pengguna_delete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", " " + response);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    Log.e("obj", "" + obj);
                    String error = obj.getString("error");
                    Log.e("error", " " + error);
                    Log.e("response", " " + response);
                    if (error.equals("false")) {
//                        Toast.makeText(PenggunaDetail.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
//                        Toast.makeText(PenggunaDetail.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.e("response", " " + response);
                    e.printStackTrace();
//                    hideDialog();

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

//                        hideDialog();

                        Toast.makeText(PenggunaDetail.this, "Delete Gagal", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ getToken);
                return params;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //Adding parameters to request
                params.put("id", getid);

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
