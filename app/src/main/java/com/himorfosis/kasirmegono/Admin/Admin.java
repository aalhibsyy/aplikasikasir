package com.himorfosis.kasirmegono.Admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.Login;
import com.himorfosis.kasirmegono.Penjualan.PenjualanClassData;
import com.himorfosis.kasirmegono.Produk.Produk;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Reward.ScanReward;
import com.himorfosis.kasirmegono.Sumber;
import com.himorfosis.kasirmegono.Volley;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Admin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment;
    FragmentTransaction ft;

    TextView email, user;

    // pdf

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font blueFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLUE);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);

    String today;

    ProgressDialog pDialog;

    List<PenjualanClassData> dataPemesanan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //      Progress dialog
        pDialog = new ProgressDialog(Admin.this);
        pDialog.setCancelable(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        String getemail = Sumber.getData("akun", "email", getApplicationContext());
        String getuser = Sumber.getData("akun", "user", getApplicationContext());

        email = headerView.findViewById(R.id.emailuser);
        user = headerView.findViewById(R.id.namauser);

        email.setText(getemail);
        user.setText(getuser);

        // date today

        Calendar cal = Calendar.getInstance();

        DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        today = date.format(cal.getTime());

        Log.e("date today", "" + today);

        if (navigationView != null) {

            Menu menu = navigationView.getMenu();
            onNavigationItemSelected(menu.getItem(0));

        }

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah anda ingin keluar");
        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        finishAffinity();

                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.admin, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

//        int id = item.getItemId();
        item.setCheckable(true);

        Log.e("id", "" + item);

        switch (item.getItemId()) {

            case R.id.produk:
                fragment = new Produk();

                break;

            case R.id.karyawan:
                fragment = new Karyawan();

                break;

            case R.id.mitra:
                fragment = new MitraFragment();

                break;

            case R.id.laporan:
                fragment = new Laporan();

                break;

            case R.id.scanreward:

                fragment = new ScanReward();

                break;

            case R.id.eksport:

                pDialog.setMessage("Memproses Data ...");
                showDialog();

                getDataPenjualan();

                break;

            case R.id.logout:
                Sumber.deleteData("akun", getApplicationContext());
                Intent in = new Intent(Admin.this, Login.class);
                startActivity(in);

                break;

        }

        if (fragment != null) {

            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    // new table

    public void buatTabel() {

        int sukses = 0;

        String FILE = Environment.getExternalStorageDirectory().toString()
                + "/PDF/" + "Laporan" + today + ".pdf";

        // Create New Blank Document
        Document document = new Document(PageSize.A4);

        // Create Directory in External Storage
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/PDF");
        myDir.mkdirs();

        // Create Pdf Writer for Writting into New Created Document
        try {

            PdfWriter.getInstance(document, new FileOutputStream(FILE));

            // Open Document for Writting into document
            document.open();

            // User Define Method

            Paragraph preface = new Paragraph();

            addEmptyLine(preface, 1);

            preface.add(new Paragraph("Laporan Penjualan Kasir Megono", catFont));
            preface.setAlignment(Element.ALIGN_CENTER);

            PdfPTable table = new PdfPTable(new float[]{2, 1, 2, 1, 1});
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell("No transaksi");
            table.addCell("Waktu");
            table.addCell("Kasir");
            table.addCell("Jumlah");
            table.addCell("Jumlah");
            table.setHeaderRows(1);

            PdfPCell[] cells = table.getRow(0).getCells();

            Log.e("cell", "" + cells.length);

            for (int j = 0; j < cells.length; j++) {

                cells[j].setBackgroundColor(BaseColor.LIGHT_GRAY);
            }

            Log.e("datapemesanan", "" + dataPemesanan.size());

            for (int i = 1; i < dataPemesanan.size(); i++) {

                PenjualanClassData data = dataPemesanan.get(i);

                String notransaksi = String.valueOf(data.getId_pemesanan());
                String jumlah = String.valueOf(data.getJumlah());
                String harga = String.valueOf(data.getTotal_harga());

//                Log.e("no", "" +notransaksi);

                table.addCell(notransaksi);
                table.addCell(data.getWaktu());
                table.addCell(data.getEmail_kasir());
                table.addCell(jumlah);
                table.addCell(harga);

            }

            document.add(table);

            sukses = 1;

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();

            hideDialog();

        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            hideDialog();

        }

        // Close Document after writting all content

        document.close();

        hideDialog();

        Log.e("sukses", "" + sukses);


        // save data to google drive

        if (sukses == 1) {

            File outputFile = new File(Environment.getExternalStoragePublicDirectory("PDF"), "Laporan" + today + ".pdf");
            Uri uri = Uri.fromFile(outputFile);

            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.setPackage("com.google.android.apps.docs");

            startActivity(Intent.createChooser(share, "Share"));

        }


    }




    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {

            paragraph.add(new Paragraph(" "));

        }
    }

    private void getDataPenjualan() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Koneksi.pemesanan_api, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Showing json data in log monitor
                        Log.e("response", "" + response);

                        try {

                            JSONArray jsonArray = response.getJSONArray("pemesanan");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                PenjualanClassData item = new PenjualanClassData();

                                item.setId_pemesanan(jsonObject.getInt("id_pemesanan"));
                                item.setId_produk(jsonObject.getInt("id_produk"));
                                item.setEmail_kasir(jsonObject.getString("email"));
                                item.setBayar(jsonObject.getInt("bayar"));
                                item.setNama_produk(jsonObject.getString("nama_produk"));
                                item.setWaktu(jsonObject.getString("waktu"));
                                item.setJumlah(jsonObject.getInt("jumlah"));
                                item.setKembalian(jsonObject.getInt("kembalian"));
                                item.setTotal_harga(jsonObject.getInt("total_harga"));

                                dataPemesanan.add(item);

                            }

                            buatTabel();

                            // get data and play doa from array datadoa

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("error", "" + e);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("error", "" + error);

                        //displaying the error in toast if occurred
//                        Toast.makeText(Admin.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

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
