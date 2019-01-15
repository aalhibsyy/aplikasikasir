package com.himorfosis.kasirmegono.Kasir;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.himorfosis.kasirmegono.R;
import com.himorfosis.kasirmegono.Sumber;

public class Profil extends Fragment {

    TextView email, user, phone, alamat;

    String getemail, getuser, getphone, getalamat, getdari;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.profil, container, false);

    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profil");

        email = view.findViewById(R.id.email);
        user = view.findViewById(R.id.user);
        phone = view.findViewById(R.id.phone);
        alamat = view.findViewById(R.id.alamat);

        getemail = Sumber.getData("akun", "email", getContext());
        getuser = Sumber.getData("akun", "user", getContext());


        email.setText(getemail);
        user.setText(getuser);

}
}
