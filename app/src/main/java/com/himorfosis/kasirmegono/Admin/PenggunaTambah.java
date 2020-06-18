package com.himorfosis.kasirmegono.Admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.himorfosis.kasirmegono.Database;
import com.himorfosis.kasirmegono.Kasir.Kasir;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;
import com.himorfosis.kasirmegono.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PenggunaTambah extends AppCompatActivity {

    LinearLayout linmitra;
    EditText nama, email, password, phone, alamat;
    Button tambah;
    TextView level;

    Database db;
    String getid, getusername, getpassword, getphone, idlevel, getnama, getdata, getuser, getlevel, xlevel, getToken;
    String[] namalevel = {"Kasir", "Admin"};
    int pilihharga, pilihlevel;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengguna_tambah);

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
        phone = findViewById(R.id.phone);
        alamat = findViewById(R.id.alamat);
        tambah = findViewById(R.id.tambah);
        level = findViewById(R.id.level);

        Intent data = getIntent();

        getdata = data.getStringExtra("data");
        getid = data.getStringExtra("id");
        getusername = data.getStringExtra("username");
        getnama = data.getStringExtra("nama");
        getpassword = data.getStringExtra("password");
        getphone = data.getStringExtra("phone");
        getlevel = data.getStringExtra("level");
        db = new Database(getApplicationContext());
        getToken = Sumber.getData("akun", "token", getApplicationContext());



        getuser = Sumber.getData("akun", "user", getApplicationContext());

        Log.e("getdata", "" + getdata);

        if (getdata.equals("update")) {

            nama.setText(getnama);
            email.setText(getusername);
//            password.setText(getpassword);
            phone.setText(getphone);
            level.setText(getlevel);

            tambah.setText("Update Pengguna");

            textToolbar.setText("Update Pengguna");

        } else {

            textToolbar.setText("Tambah Pengguna");

        }

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getnama = nama.getText().toString();
                getusername = email.getText().toString();
                getpassword = password.getText().toString();
                getphone = phone.getText().toString();


                if (getnama.equals("") || getusername.equals("")) {

                    Sumber.toastShow(getApplicationContext(), "Harap isi secara lengkap");

                } else {

                    if (getdata.equals("update")) {

                        penggunaUpdate();

                    } else {

                        penggunaTambah();
                    }

                }

            }
        });

        level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pilihlevel();
            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }

    private void pilihlevel() {

        AlertDialog dialog = new AlertDialog.Builder(PenggunaTambah.this)

                .setTitle("Pilih level :")
                .setSingleChoiceItems(namalevel, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        pilihlevel = which;

                    }
                })

                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })

                .setPositiveButton("Pilih", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        level.setText(namalevel[pilihlevel]);
                        xlevel= namalevel[pilihlevel];

                    }
                })

                .create();
        dialog.show();

    }

    private void penggunaTambah() {

        pDialog.setMessage("Tambah Pengguna ...");
        showDialog();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Koneksi.pengguna_api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If we are getting success from server

                Log.e("response", " " + response);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    Log.e("obj", "" + obj);
                    String error = obj.getString("error");
                    Log.e("error", " " + error);
                    Log.e("response", " " + response);
                    if (error.equals("false")) {
                        Log.e("response", " " + response);

                        Toast.makeText(PenggunaTambah.this, obj.getString("status"), Toast.LENGTH_SHORT).show();

                        finish();

                    } else {
                        Log.e("response", " " + response);
                        //If the server response is not success
                        //Displaying an error message on toast

                        hideDialog();

                        Toast.makeText(PenggunaTambah.this, obj.getString("status"), Toast.LENGTH_SHORT).show();


                        finish();


                    }

                } catch (JSONException e) {
                    Log.e("response", " " + response);
                    e.printStackTrace();

                    hideDialog();

                    finish();

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        hideDialog();
                        Toast.makeText(PenggunaTambah.this, "Tambah Gagal", Toast.LENGTH_SHORT).show();
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
                if (xlevel.equals("Kasir")){
                    idlevel = "2";
                } else if (xlevel.equals("Admin")){
                    idlevel = "1";
                }
                //Adding parameters to request
                params.put("nama", getnama);
                params.put("username", getusername);
                params.put("password", getpassword);
                params.put("handphone", getphone);
                params.put("level", idlevel);

                //returning parameter
                return params;
            }
        };

        Volley.getInstance().addToRequestQueue(stringRequest);

    }

    private void penggunaUpdate() {

        pDialog.setMessage("Update Pengguna ...");
        showDialog();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Koneksi.pengguna_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If we are getting success from server

                Log.e("response", " " + response);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    Log.e("obj", "" + obj);

                    if (!obj.getBoolean("error")) {

                        Toast.makeText(PenggunaTambah.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                        cekUser();


                    } else {
                        //If the server response is not success
                        //Displaying an error message on toast
//                        Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        hideDialog();

                        Toast.makeText(PenggunaTambah.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                        cekUser();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    hideDialog();

                    cekUser();


                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        hideDialog();

                        Toast.makeText(PenggunaTambah.this, "Update Gagal", Toast.LENGTH_SHORT).show();

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
                if (xlevel.equals("Kasir")){
                    idlevel = "2";
                } else if (xlevel.equals("Admin")){
                    idlevel = "1";
                }
                //Adding parameters to request
                params.put("id", getid);
                params.put("nama", getnama);
                params.put("username", getusername);
                params.put("password", getpassword);
                params.put("handphone", getphone);
                params.put("level", idlevel);

                //returning parameter
                return params;
            }
        };

        Volley.getInstance().addToRequestQueue(stringRequest);

    }

    private void cekUser() {

        if (getuser.equals("kasir")) {

            Intent in = new Intent(PenggunaTambah.this, Kasir.class);
            startActivity(in);

        } else {

            Intent in = new Intent(PenggunaTambah.this, Admin.class);
            startActivity(in);
        }

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

