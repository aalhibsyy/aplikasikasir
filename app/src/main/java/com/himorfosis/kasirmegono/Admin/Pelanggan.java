package com.himorfosis.kasirmegono.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.himorfosis.kasirmegono.Database;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;
import com.himorfosis.kasirmegono.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pelanggan extends Fragment {

    ListView list;
    ProgressBar progressBar;
    TextView kosong;
    List<PelangganClassData> listpelanggan = new ArrayList<>();
    PelangganListAdapter adapter;
    Database db;
    String getToken;
    FloatingActionButton tambah;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.pelanggan, container, false);

    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Pelanggan");

        list = view.findViewById(R.id.list);
        progressBar = view.findViewById(R.id.progress);
        kosong = view.findViewById(R.id.kosong);
        tambah = view.findViewById(R.id.tambah);

        db = new Database(getActivity().getApplicationContext());
        getToken = Sumber.getData("akun", "token", getActivity().getApplicationContext());

        getData();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getContext(), PelangganTambah.class);
                in.putExtra("data", "tambah");
                startActivity(in);

            }
        });



    }

    private void getData() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Koneksi.pelanggan_api, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressBar.setVisibility(View.GONE);
                        list.setVisibility(View.VISIBLE);

                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {

                                //getting the json object of the particular index inside the array
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                PelangganClassData item = new PelangganClassData();

                                item.setId(jsonObject.getInt("id"));
                                item.setAlamat(jsonObject.getString("alamat"));
                                item.setHandphone(jsonObject.getString("handphone"));
                                item.setNama(jsonObject.getString("nama"));

                                listpelanggan.add(item);

                            }

                            progressBar.setVisibility(View.GONE);

                            if (listpelanggan.isEmpty()) {
                                kosong.setVisibility(View.VISIBLE);
                                kosong.setText("Pelanggan kosong");
                            }

                            adapter = new PelangganListAdapter(getContext(), listpelanggan);

                            list.setAdapter(adapter);

                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Intent in = new Intent(getContext(), PelangganDetail.class);

                                    PelangganClassData data = listpelanggan.get(position);

                                    in.putExtra("data", "update");
                                    in.putExtra("id", String.valueOf(data.getId()));
                                    in.putExtra("alamat", String.valueOf(data.getAlamat()));
                                    in.putExtra("phone", String.valueOf(data.getHandphone()));
                                    in.putExtra("nama", data.getNama());
//                                    in.putExtra("dari", "Admin");

                                    startActivity(in);

                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("error", "" + e);

                            progressBar.setVisibility(View.GONE);
                            kosong.setVisibility(View.VISIBLE);
                            kosong.setText("Data kosong");
//                            checkout.setVisibility(View.INVISIBLE);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("error", "" +error);

                        progressBar.setVisibility(View.GONE);

                        kosong.setVisibility(View.VISIBLE);
                        kosong.setText("Data Kosong");
//                        checkout.setVisibility(View.INVISIBLE);
//

                    }
                })

        {
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
