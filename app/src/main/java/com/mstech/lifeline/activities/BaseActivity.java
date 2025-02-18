package com.mstech.lifeline.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by HARISH GADDAM
 */

public abstract class BaseActivity extends AppCompatActivity {

    /**
     *
     * There are three types of identifier on android phone.

     IMEI
     IMSI

     String ts = Context.TELEPHONY_SERVICE;
     TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(ts);
     String imsi = mTelephonyMgr.getSubscriberId();
     String imei = mTelephonyMgr.getDeviceId();
     Android ID It is a 64-bit hex string which is generated on the device's first boot. Generally it won't be changed unless is factory reset.

     Secure.getString(getContentResolver(), Secure.ANDROID_ID);*/

    /**" ==> Android Unique ID: 5 Solutions to identity an Android device with an Unique ID "
     https://medium.com/@ssaurel/how-to-retrieve-an-unique-id-to-identify-android-devices-6f99fd5369eb#:~:text=TelephonyManager%20telephonyManager%3BtelephonyManager%20%3D%20(TelephonyManager)%20getSystemService(Context.&text=*%20getDeviceId()%20returns%20the%20unique,or%20ESN%20for%20CDMA%20phones.
     *
     *String androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
     *
     * */

    public Context mContext = com.mstech.lifeline.activities.BaseActivity.this;
    public Intent intent;
    private Dialog dialog = null;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static String formatDateFromOnetoAnother(String date,String givenformat,String resultformat) {

        String result = "";
        SimpleDateFormat sdf;
        SimpleDateFormat sdf1;

        try {
            sdf = new SimpleDateFormat(givenformat);
            sdf1 = new SimpleDateFormat(resultformat);
            result = sdf1.format(sdf.parse(date));
        }
        catch(Exception e) {
            e.printStackTrace();
            return "";
        }
        finally {
            sdf=null;
            sdf1=null;
        }
        return result;
    }

    public AlertDialog showDialog(Context mContext, String strTitle, String strMessage, String strPositiveLable,
                                                         DialogInterface.OnClickListener positiveOnClick,
                                                         String strNegativeLabel, DialogInterface.OnClickListener negativeOnClick,
                                                         boolean isCancelable)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(strTitle);
        builder.setCancelable(isCancelable);
        builder.setMessage(strMessage);
        builder.setPositiveButton(strPositiveLable, positiveOnClick);
        builder.setNegativeButton(strNegativeLabel, negativeOnClick);

        AlertDialog alert = builder.create();
        alert.show();

        return alert;
    }


    public void showAlertDialogDialog(String title, String Message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
//        builder.setIcon(drawable);
        builder.setCancelable(true);
        builder.setMessage(Message);
        String positiveText = getString(android.R.string.ok);

        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //opration do here on Click "Ok"
                    }
                });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //opration do here on Click "Close"
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showPDialog(@NonNull String str) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(str);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public void hidePDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    protected void hideKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        /*try {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            Log.e("MultiBackStack", "Failed to add fragment to back stack", e);
        }*/
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public String getStringValue(int str) {
        return getResources().getString(str);
    }

    protected void showToast(String mToastMsg) {
        Toast.makeText(this, mToastMsg, Toast.LENGTH_LONG).show();
    }

    protected boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean d = cm.getActiveNetworkInfo() != null;
        if (!d) {
            showToast("Check your internet connection");
        }
        return cm.getActiveNetworkInfo() != null;
    }

    public boolean checkWriteExternalPermission() {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = mContext.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public boolean checkCameraPermission() {
        String permission = Manifest.permission.CAMERA;
        int res = mContext.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public String getCompleteAddressString(double latitude, double longitude) {

        String strAdd = "";

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

        } catch (IOException e) {
            e.printStackTrace();
        }

        return strAdd;
    }
}
