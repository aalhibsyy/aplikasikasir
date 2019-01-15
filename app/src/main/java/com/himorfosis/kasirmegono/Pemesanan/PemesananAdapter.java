package com.himorfosis.kasirmegono.Pemesanan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.himorfosis.kasirmegono.Database;
import com.himorfosis.kasirmegono.Kasir.BeliClassData;
import com.himorfosis.kasirmegono.Koneksi;
import com.himorfosis.kasirmegono.Produk.ProdukClassData;
import com.himorfosis.kasirmegono.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PemesananAdapter extends BaseAdapter {

    Context context;
    List<ProdukClassData> list;
    private Bitmap bitmap;
    FragmentManager fragmentManager;
    LayoutInflater inflater;
    ArrayList<ProdukClassData> arraylist;
    Database db;
    List<BeliClassData> beliproduk = new ArrayList<>();

    public PemesananAdapter(Context context, List<ProdukClassData> objects, FragmentManager fragmentManager) {

        this.context = context;
        list = objects;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<ProdukClassData>();
        this.arraylist.addAll(list);
        this.fragmentManager = fragmentManager;
    }

    public class ViewHolder {
        LinearLayout ditambah;
        Button tambah;
        ImageView gambar, tambahitem, kurangitem;
        TextView harga, jumlah, namaproduk;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        db = new Database(context);

        final ProdukClassData produk = (ProdukClassData) getItem(position);

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.rowpemesanan, null);

            holder.ditambah = convertView.findViewById(R.id.ditambah);
            holder.tambah = convertView.findViewById(R.id.tambah);
            holder.gambar = convertView.findViewById(R.id.gambarproduk);
            holder.tambahitem = convertView.findViewById(R.id.tambahitem);
            holder.kurangitem = convertView.findViewById(R.id.kurangitem);
            holder.jumlah = convertView.findViewById(R.id.jumlah);
            holder.namaproduk = convertView.findViewById(R.id.namaproduk);

            holder.tambah.getContext();
            holder.tambahitem.getContext();
            holder.kurangitem.getContext();

            holder.tambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db.addBeli(new BeliClassData(produk.getId_produk(), 1, produk.getHarga(), produk.getHarga_gojek(), produk.getHarga_grab(), produk.getHarga(), produk.getNama_produk() ));

                    holder.tambah.setVisibility(View.INVISIBLE);
                    holder.ditambah.setVisibility(View.VISIBLE);

                    Log.e("klik id", "" +produk.getId_produk());

                }
            });


            holder.tambahitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    beliproduk = db.getBeli();

                    Log.e("id", "" +produk.getId_produk());

                    for (int i = 0; i < beliproduk.size(); i++) {

                        BeliClassData data = beliproduk.get(i);

                        Log.e("id tersimpan", "" +data.getId_produk());

                        int idklik = data.getId_produk();
                        int idsimpan = produk.getId_produk();

                        if (idklik == idsimpan) {

                            int jumlahpesanan = data.getJumlah_produk() + 1;

                            String totalpesan = String.valueOf(jumlahpesanan);

                            Log.e("totalpesan", "" +totalpesan);

                            holder.jumlah.setText(totalpesan);

                            db.updateBeli(new BeliClassData(produk.getId_produk(), jumlahpesanan, produk.getHarga(),  produk.getHarga_gojek(), produk.getHarga_grab(), produk.getHarga(), produk.getNama_produk() ));

                            Log.e("tambah id", "" +produk.getId_produk());

                        }

                    }

                }
            });

            holder.kurangitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (int i = 0; i < beliproduk.size(); i++) {

                        BeliClassData data = beliproduk.get(i);

                        int idklik = data.getId_produk();
                        int idsimpan = produk.getId_produk();

                        if (idklik == idsimpan) {

                            int jumlahpesanan = data.getJumlah_produk() - 1;

                            String totalpesan = String.valueOf(jumlahpesanan);

                            holder.jumlah.setText(totalpesan);

                            if (jumlahpesanan == 0) {

                                db.delBeli(String.valueOf(produk.getId_produk()));
                                holder.tambah.setVisibility(View.INVISIBLE);

                            } else {

                                db.updateBeli(new BeliClassData(produk.getId_produk(), jumlahpesanan, produk.getHarga(),  produk.getHarga_gojek(), produk.getHarga_grab(), produk.getHarga(), produk.getNama_produk() ));


                            }

                        }

                    }

                }
            });


            if (produk != null) {

                holder.namaproduk.setText(produk.getNama_produk());

                String getGambar = produk.getGambar();

                if (getGambar != null) {

                    final String urlGambar = Koneksi.gambar + produk.getGambar();

                    Log.e("url gambar", "" + urlGambar);

                    Picasso.with(context).load(urlGambar).into(holder.gambar);

                } else {

                    holder.gambar.setImageResource(R.drawable.broken_image);

                }

            }

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;

    }

}
