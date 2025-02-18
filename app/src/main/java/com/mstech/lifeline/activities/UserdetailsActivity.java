package com.mstech.lifeline.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.mstech.lifeline.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * HARISH GADDAM
 */

public class UserdetailsActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText firstname, lastname, emailid, address, postalcode, mobile, city, street, suburb;
    String custid, merch, list, ismulvalue, imgString = "";
    ImageView gallery, sendmessage;
    ImageView selectimage;
    Spinner town, region, state, location;
    Button submit;
    ArrayList stateList = new ArrayList<String>();
    ArrayList stateidlist = new ArrayList<String>();
    ArrayList regionList = new ArrayList<String>();
    ArrayList regionidList = new ArrayList<String>();
    ArrayList townList = new ArrayList<String>();
    ArrayList townidList = new ArrayList<String>();
    ArrayList locationList = new ArrayList<String>();
    ArrayList locationidList = new ArrayList<String>();
    private Uri mCropImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_details_capture);
        getSupportActionBar().hide();
//        selectimage = (ImageView) findViewById(R.id.messageimageid);
        gallery = findViewById(R.id.profileimage);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        address = findViewById(R.id.address);
        emailid = findViewById(R.id.emailid);
        mobile = findViewById(R.id.mobile);
        city = findViewById(R.id.city);
        street = findViewById(R.id.street);
        suburb = findViewById(R.id.suburb);
        postalcode = findViewById(R.id.postalcode);
        state = findViewById(R.id.statespinner);
        town = findViewById(R.id.townspinner);
        region = findViewById(R.id.regionspinner);
        location = findViewById(R.id.locationspinner);
        submit = findViewById(R.id.submit);


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick(v);
            }
        });
        if (!SPStaticUtils.getString("customerid", "0").equals("0")) {
            getProfile();
        }
        getStates();
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getReegions(stateidlist.get(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getTowns(regionidList.get(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        town.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getLocations(townidList.get(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected()) {
                    ToastUtils.showShort("No Internet Connection");
                } else if (firstname.getText().toString().isEmpty()) {
                    ToastUtils.showShort("Enter FirstName");
                } else if (lastname.getText().toString().isEmpty()) {
                    ToastUtils.showShort("Enter LastName");
                } else if ((emailid.getText().toString().isEmpty())) {
                    ToastUtils.showShort("Enter Email-Id");
                } else if (mobile.getText().toString().isEmpty()) {
                    ToastUtils.showShort("Enter Mobile");
                } else if (address.getText().toString().isEmpty()) {
                    ToastUtils.showShort("Enter Address");
                } else if (street.getText().toString().isEmpty()) {
                    ToastUtils.showShort("Enter  Street");
                } else if (city.getText().toString().isEmpty()) {
                    ToastUtils.showShort("Enter City");
                } else if (suburb.getText().toString().isEmpty()) {
                    ToastUtils.showShort("Enter Sub Urb");
                } else if (postalcode.getText().toString().isEmpty()) {
                    ToastUtils.showShort("Enter Postalcode");
                } else {

                    registerMember();

                }
            }
        });

    }

    public void onSelectImageClick(View view) {
//        CropImage.startPickImageActivity(this);
    }

/*
    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getApplicationContext(), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(getApplicationContext(), imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                gallery.setVisibility(View.VISIBLE);
                gallery.setImageURI(result.getUri());
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(result.getUri());
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imgString = encodeImage(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getApplicationContext(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(getApplicationContext(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }
*/

    /**
     * Start crop image activity for the given image.
     */
 /*   private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setFixAspectRatio(true)
                .start(this);
    }*/

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void setActionBarTitle() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Chat");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    public void getStates() {
        stateList.clear();
        stateidlist.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://civiccare.net/api/GetStateddl?cid=" + SPStaticUtils.getString("countryid", "");
        Log.d("sss", "url:" + url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("c", response.toString());
                        stateList.add("Select State");
                        stateidlist.add("0");
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                stateidlist.add(obj.getString("StateId"));
                                stateList.add(obj.getString("StateName"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter adapter = new ArrayAdapter(UserdetailsActivity.this, R.layout.support_simple_spinner_dropdown_item, stateList);
                        state.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(movieReq);
    }

    public void getReegions(String o) {
        regionList.clear();
        regionidList.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://civiccare.net/api/GetRegionddl?sid=" + o;
        Log.d("sss", "url:" + url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("c", response.toString());
                        regionList.add("Select Region");
                        regionidList.add("0");
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                regionidList.add(obj.getString("RegionId"));
                                regionList.add(obj.getString("Region"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter adapter = new ArrayAdapter(UserdetailsActivity.this, R.layout.support_simple_spinner_dropdown_item, regionList);
                        region.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(movieReq);
    }

    public void getTowns(String s) {
        townList.clear();
        townidList.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://civiccare.net/api/GetTownddl?rid=" + s;
        Log.d("sss", "url:" + url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("c", response.toString());
                        townList.add("Select Town");
                        townidList.add("0");
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                townidList.add(obj.getString("TownId"));
                                townList.add(obj.getString("Town"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter adapter = new ArrayAdapter(UserdetailsActivity.this, R.layout.support_simple_spinner_dropdown_item, townList);
                        town.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(movieReq);
    }

    public void getLocations(String s) {
        locationList.clear();
        locationidList.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://civiccare.net/api/GetDdlLocation?TownId=" + s;
        Log.d("sss", "url:" + url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("c", response.toString());
                        locationList.add("Select Location");
                        locationidList.add("0");
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                locationidList.add(obj.getString("LocationId"));
                                locationList.add(obj.getString("Location"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter adapter = new ArrayAdapter(UserdetailsActivity.this, R.layout.support_simple_spinner_dropdown_item, locationList);
                        location.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(movieReq);
    }

    public void getProfile() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://civiccare.net/api/GetMemberProfile?mid=" + SPStaticUtils.getString("customerid", "0");
        Log.d("sss", "url:" + url);
        JsonObjectRequest movieReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("c", response.toString());
                        try {
                            firstname.setText(response.getString("FirstName"));
                            lastname.setText(response.getString("LastName"));
                            emailid.setText(response.getString("EmailId"));
                            postalcode.setText(response.getString("PostCode"));
                            address.setText(response.getString("GeoAddress"));
                            if (response.getString("Street") != "null") {
                                street.setText(response.getString("Street"));
                            }
                            if (response.getString("City") != "null") {
                                city.setText(response.getString("City"));
                            }
                            if (response.getString("Suburb") != "null") {
                                suburb.setText(response.getString("Suburb"));
                            }
                            mobile.setText(response.getString("Mobile"));
                            Glide.with(UserdetailsActivity.this)  //2
                                    .load(response.getString("CustomerImagePath")) //3
                                    .placeholder(R.drawable.ic_loading) //5
                                    .error(R.mipmap.ic_launcher) //6
                                    .into(gallery);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(movieReq);
    }

    private void registerMember() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String url = "http://civiccare.net/api/UpdateMemberProfileMobileApp";
            Log.e("url; ", "" + url);
            JSONObject obj = new JSONObject();
            obj.put("MemberId", SPStaticUtils.getString("customerid", "0"));
            obj.put("FirstName", firstname.getText().toString());
            obj.put("LastName", lastname.getText().toString());
            obj.put("emailid", emailid.getText().toString());
            obj.put("Mobile", SPStaticUtils.getString("mobile", ""));
            obj.put("PostCode", postalcode.getText().toString());
            obj.put("Latitude", "0.0");
            obj.put("Longitude", "0.0");
            obj.put("GeoAddress", address.getText().toString());
            obj.put("Street", street.getText().toString());
            obj.put("Suburb", suburb.getText().toString());
            obj.put("City", city.getText().toString());
            obj.put("ProfilePic", imgString);

            final String requestBody = obj.toString();
            Log.e("requestBody: ", requestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Register_data", "response" + response);

                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("StatusMessage").equals("Registered Successfully.")) {
                            SPStaticUtils.put("customerid", obj.getString("StatusCode"));
                            SPStaticUtils.put("islogin", true);
                            ToastUtils.showShort("Signup Successfull");
                            startActivity(new Intent(UserdetailsActivity.this, AddSosActivity.class));
                        } else {
                            ToastUtils.showShort(obj.getString("StatusMessage"));
                            onBackPressed();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());


                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return requestBody == null ? null : requestBody.getBytes(StandardCharsets.UTF_8);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
