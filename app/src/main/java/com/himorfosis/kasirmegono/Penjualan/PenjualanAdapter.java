package com.himorfosis.kasirmegono.Penjualan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.himorfosis.kasirmegono.R;

import java.util.List;

public class PenjualanAdapter extends ArrayAdapter<PenjualanClassData> {

    Context context;
    List<PenjualanClassData> list;


    public PenjualanAdapter(Context context, List<PenjualanClassData> objects) {

        super(context, R.layout.rowpenjualan, objects);
        this.context = context;
        list = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int getnomor = position + 1;

        if (convertView == null) {

            LayoutInflater layoutInflater;

            layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.rowpenjualan, null);

        }

        View v = convertView;

        final PenjualanClassData produk = list.get(position);

        if (produk != null) {

            TextView total = (TextView) v.findViewById(R.id.totaltransaksi);
            TextView nomor = (TextView) v.findViewById(R.id.nomor);
            TextView waktu = (TextView) v.findViewById(R.id.waktutransaksi);

            total.setText("Rp " + String.valueOf(produk.getTotal_harga()));
            nomor.setText(String.valueOf(getnomor) +".");
            waktu.setText(produk.getWaktu());


        }

        return v;

    }

}
