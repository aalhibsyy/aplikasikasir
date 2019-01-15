package com.himorfosis.kasirmegono.Penjualan;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.himorfosis.kasirmegono.Admin.Admin;
import com.himorfosis.kasirmegono.Kasir.Kasir;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.Mitra.Mitra;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;
import com.himorfosis.kasirmegono.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PenjualanDetail extends AppCompatActivity {

    TextView dibayar, total, kembalian, waktu, kodetransaksi;
    ListView list;

    String getbayar, gettotal, getkembalian, getwaktu, getuser, getid;

    List<PenjualanClassData> listpenjualan = new ArrayList<>();
    PenjualanDetailAdapter adapter;

    LinearLayout detaill;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.penjualan_detail);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbarclose);

        TextView textToolbar = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.toolbartext);
        Button kembali = (Button) getSupportActionBar().getCustomView().findViewById(R.id.kembali);
        textToolbar.setText("Detail Penjualan");

        dibayar = findViewById(R.id.bayar);
        total = findViewById(R.id.total);
        waktu = findViewById(R.id.waktu);
        kodetransaksi = findViewById(R.id.kodetransaksi);
        kembalian = findViewById(R.id.kembalian);
        detaill = findViewById(R.id.detail);
        progressBar = findViewById(R.id.progress);
        list = findViewById(R.id.list);

        getuser = Sumber.getData("akun", "user", getApplicationContext());

        Intent data = getIntent();

        getid = data.getStringExtra("id");
        getbayar = data.getStringExtra("bayar");
        gettotal = data.getStringExtra("total");
        getkembalian = data.getStringExtra("kembalian");
        getwaktu = data.getStringExtra("waktu");

        Log.e("id", ""+ getid);
        Log.e("bayar", ""+ getbayar);
        Log.e("total", ""+ gettotal);
        Log.e("kembalian", ""+ getkembalian);
        Log.e("waktu", ""+ getwaktu);
//        Log.e("kasir", ""+ getkasir);

        dibayar.setText( ":  Rp " + getbayar);
        total.setText( ":  Rp " + gettotal);
        waktu.setText(":  " +getwaktu);
        kodetransaksi.setText(":  " +getid);
        kembalian.setText( ":  Rp " +getkembalian);

        detailPenjualan();

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getuser.equals("Admin")) {

                    Intent in = new Intent(PenjualanDetail.this, Admin.class);
                    startActivity(in);

                } else if (getuser.equals("Mitra")) {

                    Intent in = new Intent(PenjualanDetail.this, Mitra.class);
                    startActivity(in);

                } else {

                    Intent in = new Intent(PenjualanDetail.this, Kasir.class);
                    startActivity(in);

                }

            }
        });

    }

    private void detailPenjualan() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Koneksi.pemesanan_api, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Showing json data in log monitor
                        Log.e("response", "" + response);
                        Log.e("get", "" + Request.Method.GET);

                        detaill.setVisibility(View.VISIBLE);

                        try {

                            JSONArray jsonArray = response.getJSONArray("pemesanan");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                PenjualanClassData item = new PenjualanClassData();

                                item.setId_pemesanan(jsonObject.getInt("id_pemesanan"));
                                item.setId_produk(jsonObject.getInt("id_produk"));
                                item.setBayar(jsonObject.getInt("bayar"));
                                item.setNama_produk(jsonObject.getString("nama_produk"));
                                item.setWaktu(jsonObject.getString("waktu"));
                                item.setJumlah(jsonObject.getInt("jumlah"));
                                item.setKembalian(jsonObject.getInt("kembalian"));
                                item.setTotal_harga(jsonObject.getInt("total_harga"));
                                item.setJumlah_produk(jsonObject.getInt("jumlah_produk"));
                                item.setGambar(jsonObject.getString("gambar_produk"));
                                item.setHarga_produk(jsonObject.getInt("harga"));

                                String tanggal = item.getWaktu().substring(0, 10);
                                String id = String.valueOf(item.getId_pemesanan());

                                if (getid.equals(id)) {

                                    listpenjualan.add(item);

                                }

                            }

                            adapter = new PenjualanDetailAdapter(getApplicationContext(), listpenjualan);

                            list.setAdapter(adapter);

                            progressBar.setVisibility(View.GONE);

                            // get data and play doa from array datadoa

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("error", "" + e);

                            progressBar.setVisibility(View.GONE);
                            detaill.setVisibility(View.VISIBLE);

//                            kosong.setText("Produk kosong");
//                            checkout.setVisibility(View.INVISIBLE);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("error", "" + error);

                        progressBar.setVisibility(View.GONE);


                    }
                });

        //adding the string request to request queue
        Volley.getInstance().addToRequestQueue(jsonObjectRequest);

    }

}
