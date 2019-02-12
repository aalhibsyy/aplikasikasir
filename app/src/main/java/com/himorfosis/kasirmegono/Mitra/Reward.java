package com.himorfosis.kasirmegono.Mitra;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.util.Util;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.Penjualan.PenjualanAdapter;
import com.himorfosis.kasirmegono.Penjualan.PenjualanClassData;
import com.himorfosis.kasirmegono.Penjualan.PenjualanDetail;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Reward.RewardClassData;
import com.himorfosis.kasirmegono.Sumber;
import com.himorfosis.kasirmegono.Volley;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Reward extends Fragment {

    ProgressBar progressBar;
    FrameLayout frame;
    LinearLayout linear;
    TextView rewardkosong, point, totalpoin;
    ImageView gambar;

    String id_mitra;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.reward, container, false);

    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Reward");

        frame = view.findViewById(R.id.frame);
        progressBar = view.findViewById(R.id.progress);
        linear = view.findViewById(R.id.linear);
        rewardkosong = view.findViewById(R.id.rewardkosong);
        gambar = view.findViewById(R.id.gambar);
        point = view.findViewById(R.id.point);
        totalpoin = view.findViewById(R.id.jumlahpoint);

        id_mitra = Sumber.getData("akun", "id", getContext());

        getReward();

    }

    private void getReward() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Koneksi.reward, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Showing json data in log monitor

                        progressBar.setVisibility(View.GONE);
//                        linear.setVisibility(View.VISIBLE);

                        try {

                            JSONArray jsonArray = response.getJSONArray("reward");

                            int kosong = 0;
                            Integer poin = 0;

                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                RewardClassData item = new RewardClassData();

                                item.setId_reward(jsonObject.getInt("id_reward"));
                                item.setId_mitra(jsonObject.getInt("id_mitra"));
                                item.setJumlah_poin(jsonObject.getString("jumlah_poin"));
                                item.setHadiah(jsonObject.getString("hadiah"));

                                poin = Integer.valueOf(item.getJumlah_poin());

                                Log.e("poin", "" + poin);
                                Log.e("idmitra", "" + id_mitra);
                                Log.e("idmitra", "" + item.getId_mitra());

                                if (id_mitra.equals(String.valueOf(item.getId_mitra()))) {

                                    Log.e("poin", "" + poin);

                                    if (poin > 100) {

                                        kosong = 1;

                                        linear.setVisibility(View.VISIBLE);
                                        point.setText("Total : " + item.getJumlah_poin() + " Point");

//                                        String codepoin = item.getJumlah_poin(); // Whatever you need to encode in the QR code

                                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                                        try {
                                            BitMatrix bitMatrix = multiFormatWriter.encode(item.getJumlah_poin(), BarcodeFormat.QR_CODE,200,200);
                                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                                            gambar.setImageBitmap(bitmap);

                                        } catch (WriterException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }

                            }

                            if (kosong == 0) {

                                rewardkosong.setVisibility(View.VISIBLE);
                                totalpoin.setVisibility(View.VISIBLE);
                                totalpoin.setText("Total " + String.valueOf(poin) + " Point");

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("error", "" + e);

                            progressBar.setVisibility(View.GONE);
                            rewardkosong.setVisibility(View.VISIBLE);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("error", "" + error);

                        progressBar.setVisibility(View.GONE);
                        rewardkosong.setVisibility(View.VISIBLE);

                    }
                });

        //adding the string request to request queue
        Volley.getInstance().addToRequestQueue(jsonObjectRequest);


    }

    private void CodeGenerator() {

        String text="" ; // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            gambar.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

}
