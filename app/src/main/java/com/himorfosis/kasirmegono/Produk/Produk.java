package com.himorfosis.kasirmegono.Produk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;
import com.himorfosis.kasirmegono.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Produk extends Fragment {

    FloatingActionButton tambah;
    GridView gridView;

    ProgressBar progressBar;
    List<ProdukClassData> listproduk = new ArrayList<>();
    ProdukAdapter adapter;

    TextView kosong;

    ProgressDialog pDialog;

    Integer id_produk;
    String user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.produk, container, false);

    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Produk");

        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        tambah = view.findViewById(R.id.tambah);
        kosong = (TextView) view.findViewById(R.id.kosong);
        gridView = view.findViewById(R.id.gridview);

        user = Sumber.getData("akun", "user", getContext());

//      Progress dialog
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getContext(), ProdukTambah.class);
                startActivity(in);

            }
        });

        produk();

    }

    private void produk() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Koneksi.produk_api, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Showing json data in log monitor
                        Log.e("response", "" + response);
                        Log.e("get", "" + Request.Method.GET);

                        progressBar.setVisibility(View.GONE);
                        gridView.setVisibility(View.VISIBLE);

                        int cekData = 0;

                        try {

                            //we have the array named hero inside the object
                            //so here we are getting that json array

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
                                item.setHarga_gojek(jsonObject.getInt("harga_gojek"));
                                item.setHarga_grab(jsonObject.getInt("harga_grab"));

                                listproduk.add(item);

                            }

                            Log.e("list produk", "" + listproduk);

                            if (listproduk.isEmpty()) {

                                kosong.setVisibility(View.VISIBLE);
                                kosong.setText("Produk kosong");

                            }

                            adapter = new ProdukAdapter(getContext(), listproduk);

                            gridView.setAdapter(adapter);



                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    ProdukClassData data = listproduk.get(position);

                                    Intent intent = new Intent(getContext(), ProdukDetail.class);

                                    intent.putExtra("id", String.valueOf(data.getId_produk()));
                                    intent.putExtra("nama", data.getNama_produk());
                                    intent.putExtra("harga", String.valueOf(data.getHarga()));
                                    intent.putExtra("harga_gojek", String.valueOf(data.getHarga_gojek()));
                                    intent.putExtra("harga_grab", String.valueOf(data.getHarga_grab()));
                                    intent.putExtra("kategori", data.getKategori());
                                    intent.putExtra("gambar", Koneksi.gambar + data.getGambar());

                                    startActivity(intent);

                                    Log.e("id", "" + data.getId_produk());
                                    Log.e("produk", "" + data.getNama_produk());

                                }
                            });


                            // get data and play doa from array datadoa

                            Log.e("cek data", "" + cekData);

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("error", "" + e);

                            progressBar.setVisibility(View.GONE);
                            kosong.setVisibility(View.VISIBLE);
                            kosong.setText("Produk kosong");

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("error", "" + error);

                        progressBar.setVisibility(View.GONE);

                        //displaying the error in toast if occurred
//                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        kosong.setVisibility(View.VISIBLE);
                        kosong.setText("Produk kosong");

                    }
                });

        //adding the string request to request queue
        Volley.getInstance().addToRequestQueue(jsonObjectRequest);

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
