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
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blankj.utilcode.util.SPStaticUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mstech.lifeline.R;
import com.mstech.lifeline.adapter.NewchatAdapter;
import com.mstech.lifeline.models.SideMenuFriendsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HARISH GADDAM
 */

public class Newchatmessage extends AppCompatActivity {

    NewchatAdapter adapter;
    RecyclerView Eventsrec;
    private Uri mCropImageUri;
    SharedPreferences sharedPreferences;
    EditText messageedt;
    String custid, merch, list, ismulvalue, imgString = "";
    FloatingActionButton newchat;
    ImageButton gallery, sendmessage;
    ImageView selectimage;
    private Button btnSendRquestMore;

    LinearLayoutManager mLayoutManager;
    public static ArrayList<String> selectedStrings;
    public static ArrayList<String> selectedcids;
    List<String> listss;
    //    private List<FriendsModel> Eventlist = new ArrayList<FriendsModel>();
    private final List<SideMenuFriendsModel> Eventlist = new ArrayList<SideMenuFriendsModel>();
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newchatmessage);
        type = getIntent().getExtras().getString("from");
        Eventsrec = findViewById(R.id.newchatrecid);
        messageedt = findViewById(R.id.text_send);
        sendmessage = findViewById(R.id.btn_send);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        custid = SPStaticUtils.getString("customerid");
        selectimage = findViewById(R.id.messageimageid);
        gallery = findViewById(R.id.btn_galery);
        btnSendRquestMore = findViewById(R.id.btnSendRquestMore);

        selectedStrings = new ArrayList<>();
        selectedcids = new ArrayList<>();
        adapter = new NewchatAdapter(this, Eventlist);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        Eventsrec.setLayoutManager(mLayoutManager);
        Eventsrec.setAdapter(adapter);

        btnSendRquestMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Newchatmessage.this, ChatListFriendsActivity.class);
                startActivity(intent);
            }
        });

        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("public")) {
                    sendPostmessage();
                } else {
                    listss = adapter.getitems();
                    list = Arrays.toString(listss.toArray()).replace("[", "").replace("]", "");

                    if (listss.size() == 0) {
                        Toast.makeText(getApplicationContext(), "Select atleast one member to send message", Toast.LENGTH_LONG).show();
                    } else {
                        sendmessage();
                    }
                }

            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick(v);
            }
        });
        if (type.equals("public")) {
            btnSendRquestMore.setVisibility(View.GONE);
            Eventsrec.setVisibility(View.GONE);
        } else {
            GETMEMBERS();
        }

        setActionBarTitle();
    }

    public void onSelectImageClick(View view) {
//        CropImage.startPickImageActivity(this);
    }

  /*  @Override
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
                selectimage.setVisibility(View.VISIBLE);
                selectimage.setImageURI(result.getUri());
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
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(getApplicationContext(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

  */  /**
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

    public void GETMEMBERS() {
        Eventlist.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String urlold = "http://151/.106.38.222:1000/api/Getsocial?cid=11&oid=1"; // removed

        // added on 28/8/2020
        String url = "http://civiccare.net/api/GetMemberFriends?mid=" + SPStaticUtils.getString("customerid") + "&status=1&srchname=";
        Log.d("sss", "url:" + url);
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("c", response.toString());

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                SideMenuFriendsModel friends = new SideMenuFriendsModel(
                                        obj.getString("MemberId"),
                                        obj.getString("FirstName"),
                                        obj.getString("LastName"),
                                        obj.getString("Mobile"),
                                        obj.getString("EmailId"),
                                        obj.getString("CustomerImagePath"),
                                        obj.getString("IsFriend"),
                                        obj.getString("RequestStatus"),
                                        obj.getString("RequestSent"),
                                        obj.getString("RequestStatus")
                                );
                                Eventlist.add(friends);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(movieReq);
    }

    private void sendmessage() {

        int countval = listss.toArray().length;
        if (countval == 1) {
            ismulvalue = "0";
        } else {
            ismulvalue = "1";
        }
        final String messagevalue = messageedt.getText().toString().trim();
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            String url="http://151.106.38.222:1000/api/InsertChatNewMessage";

            //updated on 28/8/2020
            String url = "http://civiccare.net/api/InsertChatNewMessage";
            Log.e("url; ", "" + url);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Pimage", imgString);
            jsonBody.put("imgextension", ".jpg");
            jsonBody.put("vidextension", "");
            jsonBody.put("Pvideo", "");
//            jsonBody.put("orgid", "1");
            jsonBody.put("frmid", custid);
            jsonBody.put("toids", list);
            jsonBody.put("msg", messagevalue);
            jsonBody.put("ismultiple", ismulvalue);
            jsonBody.put("isgroup", "0");

            final String requestBody = jsonBody.toString();
            Log.e("requestBody: ", requestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Register_data", "response" + response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        selectedcids.clear();
                        messageedt.getText().clear();
                        listss.clear();
                        Toast.makeText(Newchatmessage.this, "" + jsonObject.getString("StatusMessage"), Toast.LENGTH_LONG).show();
                        onBackPressed();
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

    private void sendPostmessage() {

        final String messagevalue = messageedt.getText().toString().trim();
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            String url="http://151.106.38.222:1000/api/InsertChatNewMessage";

            //updated on 28/8/2020
            String url = "http://civiccare.net/api/WallPostMessage";
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("Pimage", imgString);
            jsonBody.put("Pvideo", "");
            jsonBody.put("imgextension", ".jpg");
            jsonBody.put("vidextension", "");
            jsonBody.put("videoname", "");
//            jsonBody.put("orgid", "1");
            jsonBody.put("mid", custid);
            jsonBody.put("msg", messagevalue);
            jsonBody.put("groupid", "0");

            final String requestBody = jsonBody.toString();
            Log.e("requestBody: ", requestBody);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Register_data", "response" + response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        messageedt.getText().clear();
                        Toast.makeText(Newchatmessage.this, "" + jsonObject.getString("StatusMessage"), Toast.LENGTH_LONG).show();
                        onBackPressed();
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
