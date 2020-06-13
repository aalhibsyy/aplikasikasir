package com.himorfosis.kasirmegono.Produk;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.himorfosis.kasirmegono.Admin.Admin;
import com.himorfosis.kasirmegono.Admin.KaryawanTambah;
import com.himorfosis.kasirmegono.Database;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;
import com.himorfosis.kasirmegono.Volley;
import com.himorfosis.kasirmegono.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProdukTambah extends AppCompatActivity {

    LinearLayout buttonChoose;
    Button buttonUpload;
    Toolbar toolbar;
    ImageView imageView;
    EditText nama,kode,harga,stok;
    TextView kategori;
    Bitmap bitmap, decoded;
    int success;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60; // range 1 - 100
    LinearLayout pilihgambar;
    ProgressDialog pDialog;
    private static final String TAG = ProdukTambah.class.getSimpleName();
    private String UPLOAD_URL = Koneksi.produk_tambah;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String[] kategoriproduk = {"Sparepart", "Aksesoris", "Lainnya"};
    int pilihkategori;
    Database db;
    String getid,getkode, getnama, getharga,getstok, getkategori, getgambar,user, getToken, getdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produk_tambah);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbarclose);
        TextView textToolbar = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.toolbartext);
        db = new Database(getApplicationContext());
        getToken = Sumber.getData("akun", "token", getApplicationContext());


        buttonChoose = findViewById(R.id.pilihgambar);
        buttonUpload = findViewById(R.id.tambah);

        kode = (EditText) findViewById(R.id.kode);
        nama = (EditText) findViewById(R.id.nama);
        harga = (EditText) findViewById(R.id.harga);
        stok = (EditText) findViewById(R.id.stok);
        kategori = findViewById(R.id.kategori);


        imageView = (ImageView) findViewById(R.id.gambar);

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihkategori();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getdata.equals("update")) {

                    produkUpdate();

                } else {

                    uploadImage();
                }
            }
        });

        Intent bundle = getIntent();
        getdata = bundle.getStringExtra("data");
        getid = bundle.getStringExtra("id");
        getkode = bundle.getStringExtra("kode");
        getnama = bundle.getStringExtra("nama");
        getharga = bundle.getStringExtra("harga");
        getstok = bundle.getStringExtra("stok");
        getkategori = bundle.getStringExtra("kategori");
        getgambar = bundle.getStringExtra("gambar");
        user = Sumber.getData("akun", "user", ProdukTambah.this);
//        Log.e("produk id", getid);
//        Log.e("produk nama", getnama);
//        Log.e("produk harga", getharga);
//        Log.e("produk gambar", getgambar);
//
//        Log.e("getdata", "" + getdata);

        if (getdata.equals("update")) {

            kode.setText(getkode);
            nama.setText(getnama);
            harga.setText(getharga);
            stok.setText(getstok);
            kategori.setText(getkategori);
            buttonUpload.setText("Update");

            Glide.with(getApplicationContext())
                    .load(getgambar)
                    .into(imageView);

            Glide.with(getApplicationContext()).load(getgambar).into(imageView);

            textToolbar.setText("Update Produk");

        } else {

            textToolbar.setText("Tambah");

        }
    }
    private void pilihkategori() {

        AlertDialog dialog = new AlertDialog.Builder(ProdukTambah.this)

                .setTitle("Pilih kategori produk :")
                .setSingleChoiceItems(kategoriproduk, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pilihkategori = which;
                    }
                })

                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                .setPositiveButton("Pilih", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        kategori.setText(kategoriproduk[pilihkategori]);
                    }
                })

                .create();
        dialog.show();

    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage() {
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            String status = jObj.getString("error");
                            Log.e("Status", " " + status);

                            if (status.equals("success")) {
                                Log.e("v Add", jObj.toString());
                                Toast.makeText(ProdukTambah.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//                                kosong();
                                Intent intent = new Intent(ProdukTambah.this, Admin.class);
                                startActivity(intent);


                            } else {
                                Toast.makeText(ProdukTambah.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                Sumber.toastShow(ProdukTambah.this, "Gagal");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                        loading.dismiss();
                        Intent in = new Intent(ProdukTambah.this, Admin.class);
                        startActivity(in);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();
                        //menampilkan toast
                        Toast.makeText(ProdukTambah.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put("image", getStringImage(decoded));
                params.put("kode_produk", kode.getText().toString().trim());
                params.put("nama_produk", nama.getText().toString().trim());
                params.put("harga", harga.getText().toString().trim());
                params.put("stok", stok.getText().toString().trim());
                params.put("kategori", kategori.getText().toString().trim());

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ getToken);
                return params;
            }
        };

//        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
        Volley.getInstance().addToRequestQueue(stringRequest);
    }


    private void produkUpdate() {
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Koneksi.produk_update,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            String status = jObj.getString("error");
                            Log.e("Status", " " + status);

                            if (status.equals("success")) {
                                Log.e("v Add", jObj.toString());
                                Toast.makeText(ProdukTambah.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//                                kosong();
                                Intent intent = new Intent(ProdukTambah.this, Admin.class);
                                startActivity(intent);


                            } else {
                                Toast.makeText(ProdukTambah.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                Sumber.toastShow(ProdukTambah.this, "Gagal");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                        loading.dismiss();
                        Intent in = new Intent(ProdukTambah.this, Admin.class);
                        startActivity(in);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();
                        //menampilkan toast
                        Toast.makeText(ProdukTambah.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put("image", getStringImage(decoded));
                params.put("kode_produk", kode.getText().toString().trim());
                params.put("nama_produk", nama.getText().toString().trim());
                params.put("harga", harga.getText().toString().trim());
                params.put("stok", stok.getText().toString().trim());
                params.put("kategori", kategori.getText().toString().trim());
                params.put("id",getid);

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+ getToken);
                return params;
            }
        };

//        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
        Volley.getInstance().addToRequestQueue(stringRequest);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void kosong() {
        imageView.setImageResource(0);
        kode.setText(null);
        nama.setText(null);
        harga.setText(null);
        stok.setText(null);
    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imageView.setImageBitmap(decoded);
    }

    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}