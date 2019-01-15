package com.himorfosis.kasirmegono.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.Penjualan.PenjualanAdapter;
import com.himorfosis.kasirmegono.Penjualan.PenjualanClassData;
import com.himorfosis.kasirmegono.Penjualan.PenjualanDetail;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LaporanHarian extends Fragment {

    ListView list;
    ProgressBar progressBar;
    TextView kosong;
    List<PenjualanClassData> listpemesanan = new ArrayList<>();
    PenjualanAdapter adapter;

    String datetime, today;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.tabpenjualan, container, false);

    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onViewCreated(view, savedInstanceState);
//        getActivity().setTitle("Penjualan");

        list = view.findViewById(R.id.list);
        progressBar = view.findViewById(R.id.progress);
        kosong = view.findViewById(R.id.kosong);

        cekHari();

        getPemesanan();

    }

    private void getPemesanan() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Koneksi.pemesanan_api, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Showing json data in log monitor
                        Log.e("response", "" + response);
                        Log.e("get", "" + Request.Method.GET);

                        int cekData = 0;

                        progressBar.setVisibility(View.GONE);
                        list.setVisibility(View.VISIBLE);

                        try {

                            JSONArray jsonArray = response.getJSONArray("pemesanan");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                PenjualanClassData item = new PenjualanClassData();

                                item.setId_produk(jsonObject.getInt("id_produk"));
                                item.setId_pemesanan(jsonObject.getInt("id_pemesanan"));
                                item.setId_kasir(jsonObject.getInt("id_kasir"));
                                item.setNama_kasir(jsonObject.getString("nama_kasir"));
                                item.setBayar(jsonObject.getInt("bayar"));
                                item.setNama_produk(jsonObject.getString("nama_produk"));
                                item.setWaktu(jsonObject.getString("waktu"));
                                item.setJumlah(jsonObject.getInt("jumlah"));
                                item.setKembalian(jsonObject.getInt("kembalian"));
                                item.setTotal_harga(jsonObject.getInt("total_harga"));

                                String tanggal = item.getWaktu().substring(0, 10);

//                                Log.e("tanggal", "" +tanggal);

                                if (tanggal.length() > 0) {

                                    if (today.equals(tanggal)) {

                                        listpemesanan.add(item);

                                    }

                                }

                            }

                            Log.e("list produk", "" + listpemesanan);

                            progressBar.setVisibility(View.GONE);

                            if (listpemesanan.isEmpty()) {

                                kosong.setVisibility(View.VISIBLE);
                                kosong.setText("Laporan kosong");

                            }

                            adapter = new PenjualanAdapter(getContext(), listpemesanan);

                            list.setAdapter(adapter);

                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Intent in = new Intent(getContext(), PenjualanDetail.class);

                                    PenjualanClassData data = listpemesanan.get(position);

                                    in.putExtra("id", String.valueOf(data.getId_pemesanan()));
                                    in.putExtra("bayar", String.valueOf(data.getBayar()));
                                    in.putExtra("total", String.valueOf(data.getTotal_harga()));
                                    in.putExtra("kembalian", String.valueOf(data.getKembalian()));
                                    in.putExtra("waktu", data.getWaktu());
//                                    in.putExtra("kasir", data.getNama_kasir());

                                    startActivity(in);

                                }
                            });


                            // get data and play doa from array datadoa

                            Log.e("cek data", "" + cekData);

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("error", "" + e);

                            progressBar.setVisibility(View.GONE);
                            kosong.setVisibility(View.VISIBLE);
                            kosong.setText("Laporan kosong");

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
                        kosong.setText("Laporan kosong");
//

                    }
                });

        //adding the string request to request queue
        Volley.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    private void cekHari() {

        Calendar cal = Calendar.getInstance();

        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        today = date.format(cal.getTime());

        Log.e("today", "" +today);

    }

}
