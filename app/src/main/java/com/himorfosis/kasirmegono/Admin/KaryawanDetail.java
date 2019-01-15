package com.himorfosis.kasirmegono.Admin;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;

public class KaryawanDetail extends AppCompatActivity {

    TextView email, phone, alamat, nama;

    String getemail, getphone, getalamat, getid, getnama, getpass;

    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karyawan_detail);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbarclose);

        TextView textToolbar = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.toolbartext);
        Button kembali = (Button) getSupportActionBar().getCustomView().findViewById(R.id.kembali);

        textToolbar.setText("Karyawan Detail");

        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        alamat = findViewById(R.id.alamat);
        nama = findViewById(R.id.nama);
        update = findViewById(R.id.update);

        Intent data = getIntent();

        getid = data.getStringExtra("id");
        getemail = data.getStringExtra("email");
        getphone = data.getStringExtra("phone");
        getalamat = data.getStringExtra("alamat");
        getnama = data.getStringExtra("nama");
        getpass = data.getStringExtra("password");

        email.setText(getemail);
        phone.setText(getphone);
        alamat.setText(getalamat);
        nama.setText(getnama);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getApplicationContext(), KaryawanTambah.class);

                in.putExtra("data", "update");
                in.putExtra("id", getid);
                in.putExtra("email", getemail);
                in.putExtra("alamat", getalamat);
                in.putExtra("phone", getphone);
                in.putExtra("nama", getnama);
                in.putExtra("password", getpass);

                startActivity(in);

            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


    }
}
