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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.himorfosis.kasirmegono.Kasir.Kasir;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;
import com.himorfosis.kasirmegono.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class KaryawanTambah extends AppCompatActivity {

    LinearLayout linmitra;
    EditText nama, email, password, phone, alamat;
    Button tambah;

    String getid, getemail, getpassword, getphone, getalamat, getnama, getdata, getuser;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karyawan_tambah);

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

        Intent data = getIntent();

        getdata = data.getStringExtra("data");
        getid = data.getStringExtra("id");
        getnama = data.getStringExtra("nama");
        getemail = data.getStringExtra("email");
        getpassword = data.getStringExtra("password");
        getphone = data.getStringExtra("phone");
        getalamat = data.getStringExtra("alamat");

        getuser = Sumber.getData("akun", "user", getApplicationContext());

        Log.e("getdata", "" + getdata);

        if (getdata.equals("update")) {

            nama.setText(getnama);
            email.setText(getemail);
            password.setText(getpassword);
            phone.setText(getphone);
            alamat.setText(getalamat);

            tambah.setText("Update Karyawan");

            textToolbar.setText("Update Karyawan");

        } else {

            textToolbar.setText("Tambah Karyawan");

        }

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getnama = nama.getText().toString();
                getemail = email.getText().toString();
                getpassword = password.getText().toString();
                getphone = phone.getText().toString();
                getalamat = alamat.getText().toString();

                if (getnama.equals("") || getemail.equals("") || getpassword.equals("") || getphone.equals("") || getalamat.equals("")) {

                    Sumber.toastShow(getApplicationContext(), "Harap isi secara lengkap");

                } else {

                    if (getdata.equals("update")) {

                        karyawanUpdate();

                    } else {

                        karyawanTambah();
                    }

                }

            }
        });


        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(KaryawanTambah.this, Admin.class);
                startActivity(in);

            }
        });

    }

    private void karyawanTambah() {

        pDialog.setMessage("Tambah Karyawan ...");
        showDialog();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Koneksi.kasir_post, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If we are getting success from server

                Log.e("response", " " + response);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    Log.e("obj", "" + obj);

                    if (!obj.getBoolean("error")) {

                        Toast.makeText(KaryawanTambah.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                        Intent in = new Intent(KaryawanTambah.this, Admin.class);
                        startActivity(in);

                    } else {

                        //If the server response is not success
                        //Displaying an error message on toast

                        hideDialog();

                        Toast.makeText(KaryawanTambah.this, obj.getString("message"), Toast.LENGTH_SHORT).show();


                        Intent in = new Intent(KaryawanTambah.this, Admin.class);
                        startActivity(in);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    hideDialog();

                    Intent in = new Intent(KaryawanTambah.this, Admin.class);
                    startActivity(in);

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        hideDialog();

                        Toast.makeText(KaryawanTambah.this, "Tambah Gagal", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //Adding parameters to request
                params.put("nama_kasir", getnama);
                params.put("email", getemail);
                params.put("password", getpassword);
                params.put("handphone", getphone);
                params.put("alamat", getalamat);

                //returning parameter
                return params;
            }
        };

        Volley.getInstance().addToRequestQueue(stringRequest);

    }

    private void karyawanUpdate() {

        pDialog.setMessage("Update Karyawan ...");
        showDialog();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Koneksi.kasir_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If we are getting success from server

                Log.e("response", " " + response);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    Log.e("obj", "" + obj);

                    if (!obj.getBoolean("error")) {

                        Toast.makeText(KaryawanTambah.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                        cekUser();


                    } else {
                        //If the server response is not success
                        //Displaying an error message on toast
//                        Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        hideDialog();

                        Toast.makeText(KaryawanTambah.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

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

                        Toast.makeText(KaryawanTambah.this, "Update Gagal", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //Adding parameters to request
                params.put("id_kasir", getid);
                params.put("nama_kasir", getnama);
                params.put("email", getemail);
                params.put("password", getpassword);
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

            Intent in = new Intent(KaryawanTambah.this, Kasir.class);
            startActivity(in);

        } else {

            Intent in = new Intent(KaryawanTambah.this, Admin.class);
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

