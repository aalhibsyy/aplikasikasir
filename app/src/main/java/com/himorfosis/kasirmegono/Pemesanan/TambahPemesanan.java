package com.himorfosis.kasirmegono.Pemesanan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.himorfosis.kasirmegono.Database;
import com.himorfosis.kasirmegono.Kasir.BeliClassData;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.Produk.ProdukClassData;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TambahPemesanan extends AppCompatActivity {

    GridView gridView;
    Button checkout;
    TextView jumlahitem, biaya, kosong;
    List<ProdukClassData> listproduk = new ArrayList<>();
    PemesananAdapter adapter;
    ProgressBar progressBar;

    Database db;
    List<BeliClassData> databeli = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_pemesanan);

        // toolbar

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbarclose);

        TextView judul = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.toolbartext);
        judul.setText("Tambah Pesanan");

        Button back = (Button)getSupportActionBar().getCustomView().findViewById(R.id.kembali);
        back.setVisibility(View.VISIBLE);

        db = new Database(TambahPemesanan.this);

        gridView = findViewById(R.id.gridview);
        checkout = findViewById(R.id.checkout);
        kosong = findViewById(R.id.kosong);
        progressBar = findViewById(R.id.progress);

        getproduk();

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getApplicationContext(), Periksa.class);
                startActivity(in);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getApplicationContext(), Periksa.class);
                startActivity(in);

            }
        });

    }

    private void getproduk() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Koneksi.produk_api, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Showing json data in log monitor
                        Log.e("response", "" + response);
                        Log.e("get", "" + Request.Method.GET);

                        int cekData = 0;

                        progressBar.setVisibility(View.GONE);
                        gridView.setVisibility(View.VISIBLE);

                        try {

                            JSONArray jsonArray = response.getJSONArray("produk");

                            Log.e("json array", "" + jsonArray);

                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {

                                //getting the json object of the particular index inside the array
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                ProdukClassData item = new ProdukClassData();

                                item.setId_produk(jsonObject.getInt("id_produk"));
                                item.setKategori(jsonObject.getString("kategori"));
                                item.setNama_produk(jsonObject.getString("nama_produk"));
                                item.setGambar(jsonObject.getString("gambar_produk"));
                                item.setHarga(jsonObject.getInt("harga"));

                                listproduk.add(item);

                            }

                            Log.e("list produk", ""+listproduk);

                            progressBar.setVisibility(View.GONE);
                            checkout.setVisibility(View.VISIBLE);

                            if (listproduk.isEmpty()) {

                                kosong.setVisibility(View.VISIBLE);
                                kosong.setText("Produk kosong");

                            }

                            adapter = new PemesananAdapter(getApplicationContext(), listproduk, getSupportFragmentManager());
                            gridView.setAdapter(adapter);

                            // get data and play doa from array datadoa

                            Log.e("cek data", "" + cekData);

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("error", "" + e);

                            progressBar.setVisibility(View.GONE);
                            kosong.setVisibility(View.VISIBLE);
                            kosong.setText("Produk kosong");
                            checkout.setVisibility(View.INVISIBLE);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("error", "" +error);

                        progressBar.setVisibility(View.GONE);

                        //displaying the error in toast if occurred
//                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        kosong.setVisibility(View.VISIBLE);
                        kosong.setText("Produk kosong");
                        checkout.setVisibility(View.INVISIBLE);


                    }
                });

        //adding the string request to request queue
        Volley.getInstance().addToRequestQueue(jsonObjectRequest);

    }

//    private void getproduk() {
//
//        databeli = db.getBeli();
//
//        for (int i = 0; i < databeli.size(); i++) {
//
//            ProdukClassData item = new ProdukClassData();
//
//            BeliClassData data = databeli.get(i);
//
//            item.setId_produk(data.getId_produk());
////            item.setKategori(jsonObject.getString("kategori"));
//            item.setNama_produk(data.getNama_produk());
//            item.setGambar(data.getGambar());
//            item.setHarga(data.getHarga_produk());
//            item.setHarga_gojek(data.getHarga_gojek());
//            item.setHarga_grab(data.getHarga_grab());
//
//            listproduk.add(item);
//
//        }
//
//        Log.e("list produk", ""+listproduk);
//
//        progressBar.setVisibility(View.GONE);
//        checkout.setVisibility(View.VISIBLE);
//
//        if (listproduk.isEmpty()) {
//
//            kosong.setVisibility(View.VISIBLE);
//            kosong.setText("Produk kosong");
//
//        } else {
//
//            gridView.setVisibility(View.VISIBLE);
//
//            adapter = new PemesananAdapter(getApplicationContext(), listproduk, getSupportFragmentManager());
//            gridView.setAdapter(adapter);
//
//        }
//
//    }

}
