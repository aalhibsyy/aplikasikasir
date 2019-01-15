package com.himorfosis.kasirmegono.Reward;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;
import com.himorfosis.kasirmegono.Reward.RewardHasil;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class ScanReward extends Fragment implements ZXingScannerView.ResultHandler {

    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        scannerView = new ZXingScannerView(getActivity());
        if (savedInstanceState != null) {
            mAutoFocus = savedInstanceState.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = savedInstanceState.getIntegerArrayList(SELECTED_FORMATS);

        } else {
            mAutoFocus = true;
            mSelectedIndices = null;
        }

//        setupFormats();
        return scannerView;

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
//        return inflater.inflate(R.layout.riwayat, container, false);

    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Scan Reward");

        int currentApiVersion = Build.VERSION.SDK_INT;


        if (currentApiVersion >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(getContext(), "Silahkan validasi", Toast.LENGTH_LONG).show();

            } else {
                requestPermission();
            }
        }

    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getActivity(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(getActivity());
//                    setContentView(scannerView);

                    scannerView.setResultHandler(this);
                    scannerView.startCamera();
//                    scannerView.setFlash(mFlash);
                    scannerView.setAutoFocus(mAutoFocus);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();

            } else {
                requestPermission();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(getActivity(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(getActivity(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            if (shouldShowRequestPermissionRationale(CAMERA)) {

                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }

                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(Result result) {

        final String myResult = result.getText();

        Log.d("Kode reward", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());

        Intent intent = new Intent(getActivity(), RewardHasil.class);
        intent.putExtra("kode", myResult);

        startActivity(intent);

//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Scan Result");
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                scannerView.resumeCameraPreview(ScanReward.this);
//
//            }
//        });

//        builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myResult));
//                startActivity(browserIntent);
//            }
//        });

//        builder.setMessage(result.getText());
//        AlertDialog alert1 = builder.create();
//        alert1.show();

    }

}
