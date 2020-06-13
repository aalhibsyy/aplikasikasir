package com.himorfosis.kasirmegono.Produk;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.himorfosis.kasirmegono.Admin.Admin;
import com.himorfosis.kasirmegono.Admin.PelangganTambah;
import com.himorfosis.kasirmegono.Database;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;
import com.himorfosis.kasirmegono.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProdukDetail extends AppCompatActivity {

    TextView tvkode,tvnama, tvharga, tvstok,tvkategori;
    String getid,getkode, getnama, getharga,getstok, getkategori, getgambar,user, getToken;
    ImageView gambar;
    Database db;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produk_detail);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbarclose);

        TextView textToolbar = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.toolbartext);
        Button kembali = (Button) getSupportActionBar().getCustomView().findViewById(R.id.kembali);
        textToolbar.setText("Detail Produk");

        //      Progress dialog
        pDialog = new ProgressDialog(ProdukDetail.this);
        pDialog.setCancelable(false);

        db = new Database(getApplicationContext());
        getToken = Sumber.getData("akun", "token", getApplicationContext());

        tvkode = findViewById(R.id.kode);
        tvnama = findViewById(R.id.nama);
        tvharga = findViewById(R.id.harga);
        tvstok = findViewById(R.id.stok);
        tvkategori = findViewById(R.id.kategori);
        gambar = findViewById(R.id.gambar);
        Button hapus = findViewById(R.id.hapus);
        Button update = findViewById(R.id.update);

        Intent bundle = getIntent();

        getid = bundle.getStringExtra("id");
        getkode = bundle.getStringExtra("kode");
        getnama = bundle.getStringExtra("nama");
        getharga = bundle.getStringExtra("harga");
        getstok =bundle.getStringExtra("stok");
        getkategori = bundle.getStringExtra("kategori");
        getgambar = bundle.getStringExtra("gambar");
        user = Sumber.getData("akun", "user", ProdukDetail.this);
        Log.e("produk id", getid);
        Log.e("produk nama", getnama);
        Log.e("produk harga", getharga);
        Log.e("produk gambar", getgambar);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getApplicationContext(), ProdukTambah.class);

                in.putExtra("data", "update");
                in.putExtra("id", getid);
                in.putExtra("kode", getkode);
                in.putExtra("nama", getnama);
                in.putExtra("harga", getharga);
                in.putExtra("stok", getstok);
                in.putExtra("kategori", getkategori);
                in.putExtra("gambar", getgambar);

                startActivity(in);
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("produk", " hapus");

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProdukDetail.this);
                alertDialogBuilder.setMessage("Apakah anda akan menghapus produk " + getnama);
                alertDialogBuilder.setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                hapusProduk();
                            }
                        });

                alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        tvkode.setText(getkode);
        tvnama.setText(getnama);
        tvharga.setText(getharga);
        tvstok.setText(getstok);
        tvkategori.setText(getkategori);

        Glide.with(getApplicationContext())
                .load(getgambar)
                .into(gambar);

        Glide.with(getApplicationContext()).load(getgambar).into(gambar);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                finish();

                Intent in = new Intent(ProdukDetail.this, Admin.class);
                startActivity(in);

            }
        });
    }

    private void hapusProduk() {

        pDialog.setMessage("Hapus Produk ...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Koneksi.produk_delete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        Log.e("response", " " + response);

                        try {

                            JSONObject data = new JSONObject(response);

                            if (!data.getBoolean("error")) {

                                Sumber.toastShow(getApplicationContext(), "Produk terhapus");

                                Intent in = new Intent(ProdukDetail.this, Admin.class);
                                startActivity(in);


                            } else {

                                Sumber.toastShow(getApplicationContext(), "Gagal terhapus");

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("error", "" + e);

                            Sumber.toastShow(getApplicationContext(), "succes");

                            Intent in = new Intent(ProdukDetail.this, Admin.class);
                            startActivity(in);

                        }

                        hideDialog();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

//                        Sumber.dialogHide(getApplicationContext());
                        hideDialog();

                        Log.e("error", "" + error);


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

                // mengirim data json ke server
                params.put("id_produk", getid);

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
