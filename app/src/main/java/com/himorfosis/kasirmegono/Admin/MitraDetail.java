package com.himorfosis.kasirmegono.Admin;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.himorfosis.kasirmegono.R;

public class MitraDetail extends AppCompatActivity {

    TextView email, nama;
    Button update;

    String getemail,  getid, getnama, getpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mitra_detail);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbarclose);

        TextView textToolbar = (TextView) getSupportActionBar().getCustomView().findViewById(R.id.toolbartext);
        Button kembali = (Button) getSupportActionBar().getCustomView().findViewById(R.id.kembali);

        textToolbar.setText("Mitra Detail");

        email = findViewById(R.id.email);
        nama = findViewById(R.id.nama);
        update = findViewById(R.id.update);

        Intent data = getIntent();

        getid = data.getStringExtra("id");
        getemail = data.getStringExtra("email");
        getnama = data.getStringExtra("nama");
        getpass = data.getStringExtra("password");

        email.setText(getemail);
        nama.setText(getnama);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getApplicationContext(), MitraTambah.class);

                in.putExtra("data", "update");
                in.putExtra("id", getid);
                in.putExtra("email", getemail);
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
