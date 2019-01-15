package com.himorfosis.kasirmegono.Pemesanan;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.himorfosis.kasirmegono.Database;
import com.himorfosis.kasirmegono.Kasir.BeliClassData;
import com.himorfosis.kasirmegono.Kasir.Kasir;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;

import java.util.ArrayList;
import java.util.List;

public class PeriksaPemesanan extends Fragment {

    TextView totalitem, totalbayar;
    Button tambahitem, bayar;
    ListView list;

    Database db;
    List<BeliClassData> datapesanan = new ArrayList<>();
    PeriksaPemesananAdapter adapter;

    int bayartotal = 0;
    int itemtotal =  0;

    String getpembeli;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.periksa_pemesanan, container, false);

    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onViewCreated(view, savedInstanceState);
//        getActivity().setTitle("Pemesanan");

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        final Button kembali = (Button) actionBar.getCustomView().findViewById(R.id.kembali);
        kembali.setVisibility(View.VISIBLE);

        getpembeli = Sumber.getData("pemesanan", "pembeli", getContext());

        db = new Database(getContext());

        totalitem = view.findViewById(R.id.totalitem);
        totalbayar = view.findViewById(R.id.totalbayar);
        tambahitem = view.findViewById(R.id.tambahitem);
        bayar = view.findViewById(R.id.bayar);
        list = view.findViewById(R.id.list);

        datapesanan = db.getBeli();

        for (int i = 0; i < datapesanan.size(); i++ ) {

            BeliClassData data = datapesanan.get(i);

            int jumlah = data.getJumlah_produk();
            int harga = data.getHarga_produk();
            int harga_gojek = data.getHarga_gojek();
            int harga_grab = data.getHarga_grab();

            Log.e("jumlah", "" +jumlah );
            Log.e("harga", "" +harga );
            Log.e("harga gojek", "" +harga_gojek );
            Log.e("harga grab", "" +harga_grab );

            // hitung total bayar

            if (getpembeli.equals("Umum")) {

                Log.e("Periksa Pemesanan", " umum");

                int biaya = harga * jumlah;
                bayartotal = bayartotal + biaya;

            } else if (getpembeli.equals("Gojek")) {

                Log.e("Periksa Pemesanan", " gojek");

                int biaya = harga_gojek * jumlah;
                bayartotal = bayartotal + biaya;

            } else if (getpembeli.equals("Grab")) {

                Log.e("Periksa Pemesanan", " grab");

                int biaya = harga_grab * jumlah;
                bayartotal = bayartotal + biaya;

            }

            // hitung total item

            itemtotal = itemtotal + jumlah;

        }

        totalbayar.setText(String.valueOf("Rp " + bayartotal));

        totalitem.setText("Total " + String.valueOf(itemtotal) + " pesanan");

        adapter = new PeriksaPemesananAdapter(getContext(), datapesanan, getFragmentManager());
        list.setAdapter(adapter);

        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getContext(), Pembayaran.class);
                startActivity(in);

                Sumber.saveData("tagihan", "data", String.valueOf(bayartotal), getContext());
                Sumber.saveData("tagihan", "total", String.valueOf(itemtotal), getContext());

            }
        });

        tambahitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Sumber.toastShow(getContext(), "tambah item");

            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getContext(), Kasir.class);
                startActivity(in);

            }
        });

    }
}
