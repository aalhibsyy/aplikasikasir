package com.himorfosis.kasirmegono.Pemesanan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.himorfosis.kasirmegono.Database;
import com.himorfosis.kasirmegono.Kasir.BeliClassData;
import com.himorfosis.kasirmegono.Kasir.Kasir;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;
import com.himorfosis.kasirmegono.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KembalianBayaran extends AppCompatActivity {

    TextView kembalian, total, dibayar;
    Button selesai;

    String getbayar, gettagihan, datetime, getidkasir, getuser, itemtotal, jsondata, id_reward, jumlah_poin, getToken, idPelanggan;

    JSONObject jsonpost;
    Database db;

    List<BeliClassData> datapesanan = new ArrayList<BeliClassData>();

    List<String> id_produk = new ArrayList<String>();
    List<String> jumlah_produk = new ArrayList<String>();
    List<String> harga = new ArrayList<String>();

    int intkembalian;

    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kembalian_bayaran);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);

        TextView textToolbar = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.toolbartext);
        Button kembali = (Button) getSupportActionBar().getCustomView().findViewById(R.id.kembali);
        textToolbar.setText("Kembalian");

        db = new Database(getApplicationContext());
        getToken = Sumber.getData("akun", "token", getApplicationContext());

        //        Progress dialog

        pDialog = new ProgressDialog(KembalianBayaran.this);
        pDialog.setCancelable(false);

        kembalian = findViewById(R.id.kembalian);
        total = findViewById(R.id.total);
        dibayar = findViewById(R.id.dibayar);
        selesai = findViewById(R.id.selesai);

        Intent get = getIntent();

        getbayar = get.getStringExtra("bayar");

        // get waktu sekarang

        waktu();

        // get data form sharepref

        gettagihan = Sumber.getData("tagihan", "data", getApplicationContext());
        getidkasir = Sumber.getData("akun", "id", getApplicationContext());
        getuser = Sumber.getData("akun", "user", getApplicationContext());
        itemtotal = Sumber.getData("tagihan", "total", getApplicationContext());
        idPelanggan = Sumber.getData("pemesanan", "pembeli", getApplicationContext());


        // get data from db

        datapesanan = db.getBeli();

        int bayar = Integer.valueOf(getbayar);
        int tagihan = Integer.valueOf(gettagihan);

        intkembalian = bayar - tagihan;

        kembalian.setText("Rp " + String.valueOf(intkembalian));

        total.setText("Rp " + gettagihan);
        dibayar.setText("Rp " + getbayar);

        Log.e("tagihan", "" +gettagihan);
        Log.e("kasir", "" +getidkasir);
        Log.e("bayar", "" +getbayar);
        Log.e("kembalian", "" +intkembalian);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(KembalianBayaran.this, Pembayaran.class);
                startActivity(in);

            }
        });

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pDialog.setMessage("Proses transaksi ...");
                showDialog();



                    dataJson();

                    pemesananPost();


            }
        });
    }

    private void dataJson() {

        try {

            jsonpost = new JSONObject();

            jsonpost.put("waktu", datetime);
            jsonpost.put("id_pelanggan", idPelanggan);
            jsonpost.put("id_kasir", getidkasir);
            jsonpost.put("total_harga", gettagihan);
            jsonpost.put("bayar", getbayar);
            jsonpost.put("kembalian", intkembalian);
            jsonpost.put("jumlah", itemtotal);

            // membuat data json array

            JSONArray jsonarray = new JSONArray();

            for (int i = 0; i < datapesanan.size(); i++) {

                BeliClassData data = datapesanan.get(i);

                id_produk.add(String.valueOf(data.getId_produk()));
                jumlah_produk.add(String.valueOf(data.getJumlah_produk()));
                harga.add(String.valueOf(data.getHarga_produk()));

                JSONObject json = new JSONObject();

                try {

                    json.put("id_produk", id_produk.get(i));
                    json.put("jumlah_produk", jumlah_produk.get(i));
                    json.put("harga",harga.get(i));
                    jsonarray.put(json);

                }catch (JSONException e) {

                    e.printStackTrace();

                    Log.e("error", "" +e);

                }

            }

            jsonpost.put("detail_pemesanan", jsonarray);

            jsondata = "{" + jsonpost.toString().substring(1, jsonpost.toString().length() - 1) + "}";

            Log.e("json post", "" +jsondata);


        } catch (JSONException e) {

            e.printStackTrace();

            Log.e("error", "" +e);

        }

    }

    private void pemesananPost() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Koneksi.pemesanan_post,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        Log.e("response", " " + response);

                        try {

                            JSONObject data = new JSONObject(response);

                            if (!data.getBoolean("error")) {

                                Sumber.toastShow(getApplicationContext(), "Transaksi Sukses");



                                    Intent in = new Intent(KembalianBayaran.this, Kasir.class);
                                    startActivity(in);




                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("error", "" +e );

                            Sumber.toastShow(getApplicationContext(), "Transaksi Gagal");

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                // mengirim data json ke server
                params.put("data_json", jsondata);

                //returning parameter
                return params;
            }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer "+ getToken);
                    return params;
                }


        };

        Volley.getInstance().addToRequestQueue(stringRequest);

    }

    private void waktu() {

        Calendar cal = Calendar.getInstance();

        DateFormat date = new SimpleDateFormat("yyyy-MM-dd, kk:mm");
        datetime = date.format(cal.getTime());


        Log.e("date time", "" + datetime);

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
