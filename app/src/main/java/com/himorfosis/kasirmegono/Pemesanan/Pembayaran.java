package com.himorfosis.kasirmegono.Pemesanan;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.himorfosis.kasirmegono.Kasir.Kasir;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;

public class Pembayaran extends AppCompatActivity {

    TextView totalbayar;
    Button bayar, uangpas, dua, seratus, lima;
    EditText jumlahlain;

    int jumlahbayar = 0;
    String tagihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pembayaran);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbarclose);

        TextView textToolbar = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.toolbartext);
        Button kembali = (Button) getSupportActionBar().getCustomView().findViewById(R.id.kembali);
        textToolbar.setText("Pembayaran");

        totalbayar = findViewById(R.id.totalbayar);
        bayar = findViewById(R.id.bayar);
        uangpas = findViewById(R.id.uangpas);
        dua = findViewById(R.id.dua);
        seratus = findViewById(R.id.seratus);
        lima = findViewById(R.id.lima);
        jumlahlain = findViewById(R.id.jumlahlain);

        tagihan = Sumber.getData("tagihan", "data", getApplicationContext());

        totalbayar.setText("Rp " + tagihan);

        uangpas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jumlahbayar = 0;

                seratus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnhitam));
                dua.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnhitam));
                lima.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnhitam));

                uangpas.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnhijau));
                jumlahbayar = Integer.valueOf(tagihan);

            }
        });

        dua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jumlahbayar = 0;

                seratus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnhitam));
                uangpas.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnhitam));
                lima.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnhitam));

                dua.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnhijau));
                jumlahbayar = 20000;

            }
        });

        lima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jumlahbayar = 0;

                seratus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnhitam));
                uangpas.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnhitam));
                dua.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnhitam));

                lima.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnhijau));
                jumlahbayar = 50000;

            }
        });

        seratus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jumlahbayar = 0;

                lima.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnhitam));
                uangpas.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnhitam));
                dua.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnhitam));

                seratus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnhijau));
                jumlahbayar = 100000;

            }
        });

        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int inttagihan = Integer.valueOf(tagihan);

                if (jumlahbayar == 0) {

                    String inputmanual = jumlahlain.getText().toString();

                    if (inputmanual.equals("") || inputmanual.equals("null")) {

                        Sumber.toastShow(getApplicationContext(), "Harap pilih nominal pembayaran");

                    } else {

                        // edit text

                        inttagihan = Integer.valueOf(inputmanual);

                        if (jumlahbayar >= inttagihan) {

                            Intent in = new Intent(Pembayaran.this, KembalianBayaran.class);
                            in.putExtra("bayar", jumlahlain.getText().toString());
                            startActivity(in);

                        } else {

                            Sumber.toastShow(getApplicationContext(), "Pembayaran Kurang");

                        }

                    }

                } else {

                    // btn klick

                    String strbayar = String.valueOf(jumlahbayar);

                    if (jumlahbayar >= inttagihan) {

                        Intent in = new Intent(Pembayaran.this, KembalianBayaran.class);
                        in.putExtra("bayar", strbayar);
                        startActivity(in);

                        Log.e("pilih bayar", "" + jumlahbayar);

                    } else {

                        Sumber.toastShow(getApplicationContext(), "Pembayaran Kurang");

                    }

                }

            }
        });


        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(Pembayaran.this, Kasir.class);
                startActivity(in);

            }
        });


    }
}
