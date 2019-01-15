package com.himorfosis.kasirmegono.Produk;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.himorfosis.kasirmegono.Admin.Admin;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.UUID;

public class ProdukTambah extends AppCompatActivity {

    EditText nama, hargaumum, hargagojek, hargagrab;
    TextView kategori;
    Button tambah;
    ImageView gambar;
    LinearLayout pilihgambar;

    String getid, getnama, getkategori, getharga, gethargagojek, gethargagrab, path, getgambar;

    private Uri filePath;
    ProgressDialog pDialog;

    private static final int STORAGE_PERMISSION_CODE = 123;
    private int PICK_IMAGE_REQUEST = 1;

    private Bitmap bitmap;

    String[] hargaproduk = {"1000", "1500", "2000", "2500", "3000", "5000", "7000", "10000", "12000", "15000"};
    String[] kategoriproduk = {"Makanan", "Minuman"};
    int pilihharga, pilihkategori;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produk_tambah);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbarclose);

        TextView textToolbar = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.toolbartext);
        Button kembali = (Button) getSupportActionBar().getCustomView().findViewById(R.id.kembali);
        textToolbar.setText("Tambah Produk");

        nama = findViewById(R.id.nama);
        kategori = findViewById(R.id.kategori);
        hargaumum = findViewById(R.id.hargaumum);
        hargagojek = findViewById(R.id.hargagojek);
        hargagrab = findViewById(R.id.hargagrab);
        tambah = findViewById(R.id.tambah);
        gambar = findViewById(R.id.gambar);
        pilihgambar = findViewById(R.id.pilihgambar);

        Intent bundle = getIntent();

        getid = bundle.getStringExtra("id");
        getnama = bundle.getStringExtra("nama");
        getharga = bundle.getStringExtra("harga");
        getkategori = bundle.getStringExtra("kategori");
        getgambar = bundle.getStringExtra("gambar");
        gethargagojek = bundle.getStringExtra("harga_gojek");
        gethargagrab = bundle.getStringExtra("harga_grab");

        //Requesting storage permission
        requestStoragePermission();

        //         Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        pilihgambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getnama = nama.getText().toString();
                getkategori = kategori.getText().toString();
                getharga = hargaumum.getText().toString();
                gethargagojek = hargagojek.getText().toString();
                gethargagrab = hargagrab.getText().toString();

                if (getnama.equals("") || getkategori.equals("") || getharga.equals("") || gethargagojek.equals("") || gethargagrab.equals("")) {

                    Sumber.toastShow(ProdukTambah.this, "Harap isi secara lengkap");

                } else {


                    pDialog.setMessage("Upload produk ...");
                    showDialog();

                    produk();

                    hideDialog();


                }

            }
        });

        kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pilihkategori();
            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(ProdukTambah.this, Admin.class);
                startActivity(in);

            }
        });

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

    private void produk() {

        //getting the actual path of the image

        path = getPath(filePath);

        Log.e("gambar", "" + path);

        //Uploading image

        try {

            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, Koneksi.produk_tambah)
                    .addFileToUpload(path, "gambar") //Adding file
                    .addParameter("nama_produk", getnama)
                    .addParameter("harga", getharga)
                    .addParameter("harga_gojek", gethargagojek)
                    .addParameter("harga_grab", gethargagrab)
                    .addParameter("kategori", kategori.getText().toString())

                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(5)
                    .startUpload();

            //Starting the upload

            Sumber.toastShow(ProdukTambah.this, "Produk berhasil ditambah");

            Intent intent = new Intent(ProdukTambah.this, Admin.class);
            startActivity(intent);

        } catch (Exception exc) {

            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();

            Sumber.toastShow(ProdukTambah.this, "Gagal");

        }

    }


    //method to get the file path from uri
    public String getPath(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Izin diberikan sekarang Anda dapat membaca penyimpanan", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Ups Anda baru saja menolak izin", Toast.LENGTH_LONG).show();
            }
        }

    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            Log.e("file path", "" + filePath);

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                gambar.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void showDialog() {

        if (!pDialog.isShowing())
            pDialog.show();

    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void isikategori() {

        final LayoutInflater inflater = LayoutInflater.from(ProdukTambah.this);
        View dialogview = inflater.inflate(R.layout.inflatterdata, null);
        final EditText data = dialogview.findViewById(R.id.data);

        AlertDialog.Builder builder = new AlertDialog.Builder(ProdukTambah.this);

        builder.setTitle("Tambah kategori");
        builder.setView(dialogview);
        builder.setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                kategori.setText(data.getText().toString());

            }
        });

        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
