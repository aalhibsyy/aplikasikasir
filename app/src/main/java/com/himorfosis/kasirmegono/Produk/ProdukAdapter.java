package com.himorfosis.kasirmegono.Produk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProdukAdapter extends ArrayAdapter<ProdukClassData> {

    Context context;
    List<ProdukClassData> list;


    public ProdukAdapter(Context context, List<ProdukClassData> objects) {

        super(context, R.layout.rowproduk, objects);
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
            convertView = layoutInflater.inflate(R.layout.rowproduk, null);

        }

        View v = convertView;

        final ProdukClassData produk = list.get(position);

        if (produk != null) {

            TextView namaproduk = (TextView) v.findViewById(R.id.namaproduk);
            ImageView gambar = (ImageView) v.findViewById(R.id.gambarproduk);
            TextView harga = (TextView) v.findViewById(R.id.harga);


            namaproduk.setText(produk.getNama_produk());
            String getGambar = produk.getGambar();
            harga.setText("Rp " + produk.getHarga());


            if(getGambar != null) {

                final String urlGambar = Koneksi.gambar + produk.getGambar();

                Log.e("url gambar", "" +urlGambar);

                Picasso.with(getContext()).load(urlGambar).into(gambar);

            } else {

                gambar.setImageResource(R.drawable.broken_image);

            }

        }

        return v;

    }

}
