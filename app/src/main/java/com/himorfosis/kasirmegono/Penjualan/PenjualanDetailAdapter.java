package com.himorfosis.kasirmegono.Penjualan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.himorfosis.kasirmegono.R;

import java.util.List;

public class PenjualanDetailAdapter extends ArrayAdapter<PenjualanClassData> {

    Context context;
    List<PenjualanClassData> list;

    public PenjualanDetailAdapter(Context context, List<PenjualanClassData> objects) {

        super(context, R.layout.rowlaporanproduk, objects);
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

        if (convertView == null) {

            LayoutInflater layoutInflater;

            layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.rowlaporanproduk, null);

        }

        View v = convertView;

        final PenjualanClassData produk = list.get(position);

        if (produk != null) {

            TextView nama = (TextView) v.findViewById(R.id.namaproduk);
            TextView jumlah = (TextView) v.findViewById(R.id.jumlah);
            TextView totalbiaya = (TextView) v.findViewById(R.id.totalbiaya);
            ImageView gambar = v.findViewById(R.id.gambarproduk);

            nama.setText(produk.getNama_produk());
            jumlah.setText(String.valueOf(produk.getJumlah_produk()) +" item");

            String getGambar = produk.getGambar();

            int jumlahproduk= produk.getJumlah_produk();
            int hargaproduk = produk.getHarga_produk();

            if (jumlahproduk == 1) {

                totalbiaya.setText(String.valueOf(hargaproduk));

            } else {

                hargaproduk = hargaproduk * jumlahproduk;

                totalbiaya.setText("Rp " + String.valueOf(hargaproduk));

            }

//            if(getGambar != null) {
//
//                final String urlGambar = Koneksi.gambar + produk.getGambar();
//
//                Log.e("url gambar", "" +urlGambar);
//
//                Picasso.with(getContext()).load(urlGambar).into(gambar);
//
//            } else {
//
//                gambar.setImageResource(R.drawable.broken_image);
//
//            }


        }

        return v;

    }

}
