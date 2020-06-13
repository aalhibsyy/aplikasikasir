package com.himorfosis.kasirmegono.Pemesanan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.himorfosis.kasirmegono.Admin.PelangganClassData;
import com.himorfosis.kasirmegono.R;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<PelangganClassData> {
    private final Context context;
    private final List<PelangganClassData> userList;

    public UserListAdapter(@NonNull Context context, int resource, @NonNull List<PelangganClassData> objects) {
        super(context, resource, objects);
        userList = objects;
        this.context = context;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_assignment_dialog_list_layout, parent, false);
        TextView id = rowView.findViewById(R.id.tv_id);
        TextView userName = rowView.findViewById(R.id.tv_user_name);
        PelangganClassData user = userList.get(position);

        userName.setText(user.getNama());
        id.setText(user.getId());

        return rowView;
    }

}