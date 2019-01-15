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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MitraFragment extends Fragment {

    ListView list;
    ProgressBar progressBar;
    TextView kosong;
    List<MitraClassData> listmitra = new ArrayList<>();
    MitraListAdapter adapter;

    FloatingActionButton tambah;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.mitrafragment, container, false);

    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Mitra");

        list = view.findViewById(R.id.list);
        progressBar = view.findViewById(R.id.progress);
        kosong = view.findViewById(R.id.kosong);
        tambah = view.findViewById(R.id.tambah);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getContext(), MitraTambah.class);
                in.putExtra("data", "tambah");
                startActivity(in);

            }
        });

        getMitra();

    }

    private void getMitra() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Koneksi.mitra, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressBar.setVisibility(View.GONE);
                        list.setVisibility(View.VISIBLE);

                        try {

                            JSONArray jsonArray = response.getJSONArray("mitra");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {

                                //getting the json object of the particular index inside the array
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                MitraClassData item = new MitraClassData();

                                item.setId_mitra(jsonObject.getInt("id_mitra"));
                                item.setEmail(jsonObject.getString("email"));
                                item.setNama_mitra(jsonObject.getString("nama_mitra"));
                                item.setPassword(jsonObject.getString("password"));

                                listmitra.add(item);

                            }

                            progressBar.setVisibility(View.GONE);

                            if (listmitra.isEmpty()) {

                                kosong.setVisibility(View.VISIBLE);
                                kosong.setText("Mitra kosong");

                            }

                            adapter = new MitraListAdapter(getContext(), listmitra);

                            list.setAdapter(adapter);

                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Intent in = new Intent(getContext(), MitraDetail.class);

                                    MitraClassData data = listmitra.get(position);

                                    in.putExtra("id", String.valueOf(data.getId_mitra()));
                                    in.putExtra("nama", data.getNama_mitra());
                                    in.putExtra("email", data.getEmail());
                                    in.putExtra("password",data.getPassword());

                                    startActivity(in);

                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("error", "" + e);

                            progressBar.setVisibility(View.GONE);
                            kosong.setVisibility(View.VISIBLE);
                            kosong.setText("Produk kosong");
//                            checkout.setVisibility(View.INVISIBLE);

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


                    }
                });

        //adding the string request to request queue
        Volley.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
