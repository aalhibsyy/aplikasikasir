package com.himorfosis.kasirmegono;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.himorfosis.kasirmegono.Admin.Admin;
import com.himorfosis.kasirmegono.Kasir.Kasir;
import com.himorfosis.kasirmegono.Mitra.Mitra;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button login;

    String getemail, getpass;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

//      Progress dialog
        pDialog = new ProgressDialog(Login.this);
        pDialog.setCancelable(false);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getemail = email.getText().toString();
                getpass = password.getText().toString();

                if (getemail.equals("") || getemail.equals("null") || getpass.equals("") || getpass.equals("null")) {

                    Sumber.toastShow(getApplicationContext(), "Harap isi secara lengkap");

                } else {

                    login();

                }

            }
        });

    }

    private void login() {

        pDialog.setMessage("Login ...");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Koneksi.login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        Log.e("response", " " + response);

                        try {

                            JSONObject data = new JSONObject(response);

                            if (!data.getBoolean("error")) {

                                JSONObject akun = data.getJSONObject("user");

                                String email = akun.getString("email");
                                String pass = akun.getString("password");
                                String user = akun.getString("user");

                                Log.e("akun", "" + email);
                                Log.e("akun", "" + pass);
                                Log.e("akun", "" + user);

                                if (user.equals("Admin")) {

                                    Intent intent = new Intent(Login.this, Admin.class);
                                    startActivity(intent);

                                } else if (user.equals("Kasir")) {

                                    String id = akun.getString("id");
                                    Sumber.saveData("akun", "id", id, getApplicationContext());

                                    Log.e("id", "" +id);

                                    Intent intent = new Intent(Login.this, Kasir.class);
                                    startActivity(intent);

//                                    Sumber.toastShow(getApplicationContext(), "Kasir");

                                } else {

                                    String id = akun.getString("id");
                                    Sumber.saveData("akun", "id", id, getApplicationContext());

                                    Intent intent = new Intent(Login.this, Mitra.class);
                                    startActivity(intent);

                                }

                                Sumber.saveData("akun", "email", getemail, getApplicationContext());
                                Sumber.saveData("akun", "password", getpass, getApplicationContext());
                                Sumber.saveData("akun", "user", user, getApplicationContext());


                            }

                            Sumber.toastShow(getApplicationContext(), "Login Sukses");

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Log.e("error", "" +e );

                            Sumber.toastShow(getApplicationContext(), "Login Gagal");

                        }

                        hideDialog();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

//                        Sumber.dialogHide(getApplicationContext());
                        hideDialog();

                        Log.e("error", "" + error);


                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                // mengirim data json ke server
                params.put("email", getemail);
                params.put("password", getpass);

                //returning parameter
                return params;
            }
        };

        Volley.getInstance().addToRequestQueue(stringRequest);

    }

    private void showDialog() {

        if (!pDialog.isShowing())
            pDialog.show();

    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        finishAffinity();

        this.doubleBackToExitPressedOnce = true;

    }

}
