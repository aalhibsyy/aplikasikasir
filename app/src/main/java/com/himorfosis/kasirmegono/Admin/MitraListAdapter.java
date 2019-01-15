package com.himorfosis.kasirmegono.Admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.himorfosis.kasirmegono.R;

import java.util.List;

public class MitraListAdapter extends ArrayAdapter<MitraClassData> {

    Context context;
    List<MitraClassData> list;


    public MitraListAdapter(Context context, List<MitraClassData> objects) {

        super(context, R.layout.rowmitra, objects);
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
            convertView = layoutInflater.inflate(R.layout.rowmitra, null);

        }

        View v = convertView;

        final MitraClassData data = list.get(position);

        if (data != null) {

            TextView nama = (TextView) v.findViewById(R.id.nama);
            TextView nomor = (TextView) v.findViewById(R.id.nomor);
            TextView email = (TextView) v.findViewById(R.id.email);

            email.setText(data.getEmail());
            nomor.setText(String.valueOf(getnomor) +".");
            nama.setText(data.getNama_mitra());

        }

        return v;

    }

}
