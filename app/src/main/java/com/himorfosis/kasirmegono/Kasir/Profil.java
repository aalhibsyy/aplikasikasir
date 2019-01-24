package com.himorfosis.kasirmegono.Kasir;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.himorfosis.kasirmegono.Admin.KaryawanClassData;
import com.himorfosis.kasirmegono.Admin.KaryawanDetail;
import com.himorfosis.kasirmegono.Admin.KaryawanListAdapter;
import com.himorfosis.kasirmegono.Admin.MitraClassData;
import com.himorfosis.kasirmegono.Admin.MitraDetail;
import com.himorfosis.kasirmegono.Admin.MitraListAdapter;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;
import com.himorfosis.kasirmegono.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profil extends Fragment {

    TextView email, user, phone, alamat, nama;

    String getemail, getuser, getid, getphone, getalamat, getdari;

    ProgressBar progressBar;
    LinearLayout frame, linalamat, linphone;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.profil, container, false);

    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profil");

        email = view.findViewById(R.id.email);
        nama = view.findViewById(R.id.nama);
        user = view.findViewById(R.id.user);
        phone = view.findViewById(R.id.phone);
        alamat = view.findViewById(R.id.alamat);
        progressBar = view.findViewById(R.id.progress);
        frame = view.findViewById(R.id.frame);
        linalamat = view.findViewById(R.id.linalamat);
        linphone = view.findViewById(R.id.linphone);

        getemail = Sumber.getData("akun", "email", getContext());
        getuser = Sumber.getData("akun", "user", getContext());
        getid = Sumber.getData("akun", "id", getContext());

        if (getuser.equals("Kasir")) {

            dataKasir();

        } else {

            dataMitra();

        }

        user.setText(getuser);

    }

    private void dataKasir() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Koneksi.kasir, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressBar.setVisibility(View.GONE);
                        frame.setVisibility(View.VISIBLE);

                        try {

                            JSONArray jsonArray = response.getJSONArray("kasir");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {

                                //getting the json object of the particular index inside the array
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                KaryawanClassData item = new KaryawanClassData();

                                item.setId_karyawan(jsonObject.getInt("id_kasir"));
                                item.setEmail(jsonObject.getString("email"));
                                item.setAlamat(jsonObject.getString("alamat"));
                                item.setHandphone(jsonObject.getString("handphone"));
                                item.setNama(jsonObject.getString("nama_kasir"));
                                item.setHandphone(jsonObject.getString("handphone"));
                                item.setPassword(jsonObject.getString("password"));

                                String id = String.valueOf(item.getId_karyawan());

                                if (getid.equals(id)) {

                                    nama.setText(item.getNama());
                                    email.setText(item.getEmail());
                                    alamat.setText(item.getAlamat());
                                    phone.setText(item.getHandphone());

                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("error", "" + e);

                            Sumber.toastShow(getContext(), "error");

                            progressBar.setVisibility(View.GONE);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("error", "" +error);

                        progressBar.setVisibility(View.GONE);

                        Sumber.toastShow(getContext(), "error");
                        //

                    }
                });

        //adding the string request to request queue
        Volley.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    private void dataMitra() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Koneksi.mitra, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        progressBar.setVisibility(View.GONE);
                        frame.setVisibility(View.VISIBLE);

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

                                String id = String.valueOf(item.getId_mitra());

                                if (getid.equals(id)) {

                                    nama.setText(item.getNama_mitra());
                                    email.setText(item.getEmail());

                                    linalamat.setVisibility(View.INVISIBLE);
                                    linphone.setVisibility(View.INVISIBLE);


                                }

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("error", "" + e);

                            progressBar.setVisibility(View.GONE);

                            Sumber.toastShow(getContext(), "error");


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("error", "" +error);

                        progressBar.setVisibility(View.GONE);

                        Sumber.toastShow(getContext(), "error");

                    }
                });

        //adding the string request to request queue
        Volley.getInstance().addToRequestQueue(jsonObjectRequest);

    }

}
