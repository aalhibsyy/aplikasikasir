package com.himorfosis.kasirmegono.Reward;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.himorfosis.kasirmegono.Admin.Admin;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;
import com.himorfosis.kasirmegono.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RewardHasil extends AppCompatActivity {

    TextView poin, hadiah;
    Button selesai;

    String getkode, iduser, id_reward, jumlahpoin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rewardhasil);

        hadiah = findViewById(R.id.hadiah);
        poin = findViewById(R.id.poin);
        selesai = findViewById(R.id.selesai);

        iduser = Sumber.getData("akun", "id", getApplicationContext());

        Intent data = getIntent();

        getkode = data.getStringExtra("kode");

        Log.e("id", "" +iduser);
        Log.e("kode", "" +getkode);

        if (getkode.equals("ambilreward")) {

            getReward();

        } else {

            poin.setVisibility(View.VISIBLE);
            poin.setText("Maaf mitra tidak memiliki reward");

        }

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(RewardHasil.this, Admin.class);
                startActivity(in);

            }
        });

    }

    private void getReward() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Koneksi.reward_cek,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        Log.e("response", " " + response);

                        try {

                            JSONObject data = new JSONObject(response);

                            if (!data.getBoolean("error")) {

                                JSONObject akun = data.getJSONObject("reward");

                                id_reward = akun.getString("id_reward");
                                String id_mitra = akun.getString("id_mitra");
                                String getpoin = akun.getString("jumlah_poin");
                                String gethadiah = akun.getString("hadiah");

                                hadiah.setText(gethadiah);
                                poin.setText(getpoin);

                                updateReward();

                            } else {

                                Log.e("update reward", "gagal");

                            }

                            Sumber.toastShow(getApplicationContext(), "Login Sukses");

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("error", "" +e );

                            Sumber.toastShow(getApplicationContext(), "Login Gagal");

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want


                        Log.e("error", "" + error);


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                // mengirim data json ke server
                params.put("id_mitra", iduser);

                //returning parameter
                return params;
            }
        };

        Volley.getInstance().addToRequestQueue(stringRequest);

    }

    private void updateReward() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Koneksi.reward_update,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        Log.e("response", " " + response);

                        try {

                            JSONObject data = new JSONObject(response);

                            if (!data.getBoolean("error")) {

                                Log.e("update reward", "sukses");

                            } else {

                                poin.setVisibility(View.VISIBLE);
                                poin.setText("Maaf anda tidak memiliki reward");

                            }

                            Sumber.toastShow(getApplicationContext(), "Login Sukses");

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("error", "" +e );

                            Sumber.toastShow(getApplicationContext(), "Login Gagal");

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want


                        Log.e("error", "" + error);


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                // mengirim data json ke server
                params.put("id_mitra", iduser);
                params.put("id_reward", id_reward);
                params.put("jumlah_poin", "0");

                //returning parameter
                return params;
            }
        };

        Volley.getInstance().addToRequestQueue(stringRequest);

    }

}
