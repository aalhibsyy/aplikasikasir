package com.himorfosis.kasirmegono.Pemesanan;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.himorfosis.kasirmegono.Admin.PelangganClassData;
import com.himorfosis.kasirmegono.Admin.PelangganDetail;
import com.himorfosis.kasirmegono.Admin.PelangganListAdapter;
import com.himorfosis.kasirmegono.Database;
import com.himorfosis.kasirmegono.Kasir.BeliClassData;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.Produk.ProdukClassData;
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

import static com.android.volley.VolleyLog.TAG;
import android.content.Intent;

public class TabPemesanan extends Fragment {

    GridView gridView;
    Button checkout;
    TextView jumlahitem, biaya, kosong;
    List<ProdukClassData> listproduk = new ArrayList<>();
    PemesananAdapter adapter;
    ProgressBar progressBar;

    Database db;
    List<BeliClassData> databeli = new ArrayList<>();
    String getpembeli, getuser, idPelanggan,getalamat,getphone,getnama;
    String getToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tabpemesanan, container, false);

    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onViewCreated(view, savedInstanceState);

        db = new Database(getContext());
        getToken = Sumber.getData("akun", "token", getActivity().getApplicationContext());

        gridView = view.findViewById(R.id.gridview);
        checkout = view.findViewById(R.id.checkout);
        kosong = view.findViewById(R.id.kosong);
        progressBar = view.findViewById(R.id.progress);


        Bundle bundle = this.getArguments();

        if(bundle != null){
           idPelanggan = bundle.getString("idPelanggan");
        }
        Log.e("ID PELANGGAN", "" +idPelanggan);
        Sumber.deleteDataInt("pesanan", getContext());
        getuser = Sumber.getData("akun", "user", getContext());

        Log.e("get user", "" +getuser);

        if (getuser.equals("Kasir")) {

            getActivity().setTitle("Pemesanan");

        } else {

            getActivity().setTitle("Produk");

        }

        Calendar cal = Calendar.getInstance();

        DateFormat date = new SimpleDateFormat("yyyy-MM-dd, kk:mm");

        String datetime = date.format(cal.getTime());


        Log.e("date time", "" + datetime);

        getproduk();


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getuser.equals("Kasir")) {

                    cekpembeli();

                }

            }
        });

    }


    private void cekpembeli() {
        Sumber.saveData("pemesanan", "pembeli", idPelanggan, getContext());
        databeli = db.getBeli();
        Log.e("database", "" +databeli);
        if (databeli.isEmpty()) {
            Sumber.toastShow(getContext(), "Harap pilih produk");
        } else {
            Intent in = new Intent(getContext(), Periksa.class);
            startActivity(in);
        }
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

                            JSONArray jsonArray = response.getJSONArray("data");

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

                            adapter = new PemesananAdapter(getContext(), listproduk, getFragmentManager());
                            gridView.setAdapter(adapter);

                            // get data and play doa from array datadoa

                            Log.e("cek data", "" + cekData);

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("error", "" + e);
                            Log.e("TOKEN", "" + getToken);

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
                        Log.e("TOKEN", "" + getToken);

                        progressBar.setVisibility(View.GONE);

                        //displaying the error in toast if occurred
//                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        kosong.setVisibility(View.VISIBLE);
                        kosong.setText("Produk kosong");
                        checkout.setVisibility(View.INVISIBLE);


                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ getToken);
                return params;
            }
        };

        //adding the string request to request queue
        Volley.getInstance().addToRequestQueue(jsonObjectRequest);

    }

}
