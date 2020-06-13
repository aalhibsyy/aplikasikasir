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

public class PelangganTambah extends AppCompatActivity {

    LinearLayout linmitra;
    EditText nama, phone, alamat;
    Button tambah;
    TextView level;

    Database db;
    String getid, getalamat, getphone, getnama, getdata, getuser, getToken;
    String[] namalevel = {"Kasir", "Admin"};
    int pilihharga, pilihlevel;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pelanggan_tambah);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbarclose);

        TextView textToolbar = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.toolbartext);
        Button kembali = (Button) getSupportActionBar().getCustomView().findViewById(R.id.kembali);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        nama = findViewById(R.id.nama);
        phone = findViewById(R.id.phone);
        alamat = findViewById(R.id.alamat);
        tambah = findViewById(R.id.tambah);

        Intent data = getIntent();
        getdata = data.getStringExtra("data");
        getid = data.getStringExtra("id");
        getalamat = data.getStringExtra("alamat");
        getnama = data.getStringExtra("nama");
        getphone = data.getStringExtra("phone");
        db = new Database(getApplicationContext());
        getToken = Sumber.getData("akun", "token", getApplicationContext());



        getuser = Sumber.getData("akun", "user", getApplicationContext());

        Log.e("getdata", "" + getdata);

        if (getdata.equals("update")) {

            nama.setText(getnama);
            phone.setText(getphone);
            alamat.setText(getalamat);
            tambah.setText("Update Pelanggan");

            textToolbar.setText("Update Pelanggan");

        } else {

            textToolbar.setText("Tambah Pelanggan");

        }

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getnama = nama.getText().toString();
                getalamat = alamat.getText().toString();
                getphone = phone.getText().toString();


                if (getnama.equals("") || getalamat.equals("") || getphone.equals("")) {

                    Sumber.toastShow(getApplicationContext(), "Harap isi secara lengkap");

                } else {

                    if (getdata.equals("update")) {

                        pelangganUpdate();

                    } else {

                        pelangganTambah();
                    }

                }

            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(PelangganTambah.this, Admin.class);
                startActivity(in);

            }
        });

    }


    private void pelangganTambah() {

        pDialog.setMessage("Tambah Pelanggan ...");
        showDialog();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Koneksi.pelanggan_api, new Response.Listener<String>() {
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

                        Toast.makeText(PelangganTambah.this, obj.getString("status"), Toast.LENGTH_SHORT).show();

                        Intent in = new Intent(PelangganTambah.this, Admin.class);
                        startActivity(in);

                    } else {
                        Log.e("response", " " + response);
                        //If the server response is not success
                        //Displaying an error message on toast

                        hideDialog();

                        Toast.makeText(PelangganTambah.this, obj.getString("status"), Toast.LENGTH_SHORT).show();


                        Intent in = new Intent(PelangganTambah.this, Admin.class);
                        startActivity(in);


                    }

                } catch (JSONException e) {
                    Log.e("response", " " + response);
                    e.printStackTrace();

                    hideDialog();

                    Intent in = new Intent(PelangganTambah.this, Admin.class);
                    startActivity(in);

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        hideDialog();
                        Toast.makeText(PelangganTambah.this, "Tambah Gagal", Toast.LENGTH_SHORT).show();
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
                params.put("nama", getnama);
                params.put("alamat", getalamat);
                params.put("handphone", getphone);

                //returning parameter
                return params;
            }
        };

        Volley.getInstance().addToRequestQueue(stringRequest);

    }

    private void pelangganUpdate() {

        pDialog.setMessage("Update Pelanggan ...");
        showDialog();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Koneksi.pelanggan_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If we are getting success from server

                Log.e("response", " " + response);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    Log.e("obj", "" + obj);

                    if (!obj.getBoolean("error")) {

                        Toast.makeText(PelangganTambah.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                        cekUser();


                    } else {
                        //If the server response is not success
                        //Displaying an error message on toast
//                        Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        hideDialog();

                        Toast.makeText(PelangganTambah.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

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

                        Toast.makeText(PelangganTambah.this, "Update Gagal", Toast.LENGTH_SHORT).show();

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
                params.put("nama", getnama);
                params.put("handphone", getphone);
                params.put("alamat", getalamat);

                //returning parameter
                return params;
            }
        };

        Volley.getInstance().addToRequestQueue(stringRequest);

    }

    private void cekUser() {

        if (getuser.equals("kasir")) {

            Intent in = new Intent(PelangganTambah.this, Kasir.class);
            startActivity(in);

        } else {

            Intent in = new Intent(PelangganTambah.this, Admin.class);
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

