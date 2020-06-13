package com.himorfosis.kasirmegono.Pemesanan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.himorfosis.kasirmegono.Database;
import com.himorfosis.kasirmegono.Kasir.BeliClassData;
import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;

import java.util.ArrayList;
import java.util.List;

public class PeriksaPemesananAdapter extends BaseAdapter {

    Context context;
    List<BeliClassData> list;
    private Bitmap bitmap;
    FragmentManager fragmentManager;
    LayoutInflater inflater;
    ArrayList<BeliClassData> arraylist;
    Database db;
    List<BeliClassData> jumlahitem = new ArrayList<>();

    public PeriksaPemesananAdapter(Context context, List<BeliClassData> objects, FragmentManager fragmentManager) {

        this.context = context;
        list = objects;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<BeliClassData>();
        this.arraylist.addAll(list);
        this.fragmentManager = fragmentManager;

    }

    public class ViewHolder {
        ImageView tambahitem, kurangitem;
        TextView hargaproduk, jumlah, namaproduk;

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

        String getpembeli = Sumber.getData("pemesanan", "pembeli", context);

        final BeliClassData produk = (BeliClassData) getItem(position);

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.rowpembayaran, null);

            holder.tambahitem = convertView.findViewById(R.id.tambahitem);
            holder.kurangitem = convertView.findViewById(R.id.kurangitem);
            holder.jumlah = convertView.findViewById(R.id.jumlah);
            holder.namaproduk = convertView.findViewById(R.id.namaproduk);
            holder.hargaproduk = convertView.findViewById(R.id.hargaproduk);

            holder.tambahitem.getContext();
            holder.kurangitem.getContext();

            holder.tambahitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    jumlahitem = db.getBeli();

                    Log.e("id", "" + produk.getId_produk());

                    for (int i = 0; i < jumlahitem.size(); i++) {

                        BeliClassData data = jumlahitem.get(i);

                        Log.e("id tersimpan", "" + data.getId_produk());

                        int idklik = data.getId_produk();
                        int idsimpan = produk.getId_produk();

                        if (idklik == idsimpan) {

                            int tambahpesanan = data.getJumlah_produk() + 1;

                            String totalpesan = String.valueOf(tambahpesanan);

                            Log.e("totalpesan", "" + totalpesan);

                            holder.jumlah.setText(totalpesan);

                            db.updateBeli(new BeliClassData(produk.getId_produk(), tambahpesanan, produk.getHarga_produk(),produk.getHarga_produk(), produk.getNama_produk(), produk.getGambar()));

                            Log.e("tambah id", "" + produk.getId_produk());

                            // reload new data

                            Fragment fragment = new PeriksaPemesanan();
                            fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();

                        }

                    }

                }
            });

            holder.kurangitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    jumlahitem = db.getBeli();

                    for (int i = 0; i < jumlahitem.size(); i++) {

                        BeliClassData data = jumlahitem.get(i);

                        int idklik = data.getId_produk();
                        int idsimpan = produk.getId_produk();

                        if (idklik == idsimpan) {

                            int kurangpesanan = data.getJumlah_produk() - 1;

                            String totalpesan = String.valueOf(kurangpesanan);

                            holder.jumlah.setText(totalpesan);

                            if (kurangpesanan == 0) {

                                db.delBeli(String.valueOf(produk.getId_produk()));

                            } else {

                                db.updateBeli(new BeliClassData(produk.getId_produk(), kurangpesanan, produk.getHarga_produk(),  produk.getHarga_produk(), produk.getNama_produk(), produk.getGambar()));

                                // reload new data

                                Fragment fragment = new PeriksaPemesanan();
                                fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();

                            }
                        }

                    }

                }
            });

            if (produk != null) {

                holder.jumlah.setText(String.valueOf(produk.getJumlah_produk()));
//                holder.namaproduk.setText(produk.getNama_produk());
                holder.namaproduk.setText(String.valueOf(produk.getNama_produk()));
                holder.hargaproduk.setText("Rp " + String.valueOf(produk.getHarga_produk() + " per produk"));


            }

        }

        return convertView;
    }
}
