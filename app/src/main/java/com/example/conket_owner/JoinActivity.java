package com.example.conket_owner;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JoinActivity extends Activity implements OnClickListener {
    Button btn_join;
    Button btn_check;
    EditText edit_id;
    EditText edit_pw;
    EditText edit_pwcheck;
    EditText edit_stonum;
    EditText edit_na;
    EditText edit_phone;
    boolean idcheck = false;
    ProgressDialog dia;
    Context mContext = this;

    String ip_address;
    String url;
    String url2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        ip_address = this.getResources().getString(R.string.ip_address);

        url = ip_address+":12345/DBServer/JSPServer/Join.jsp";
        url2 = ip_address+":12345/DBServer/JSPServer/ID_Confirm.jsp";

        btn_join = (Button) findViewById(R.id.btnjoin);
        btn_check = (Button) findViewById(R.id.btncheck);

        edit_id = (EditText) findViewById(R.id.editid);
        edit_pw = (EditText) findViewById(R.id.editpw);
        edit_pwcheck = (EditText) findViewById(R.id.editpwcheck);
        edit_stonum = (EditText) findViewById(R.id.editstonum);
        edit_na = (EditText) findViewById(R.id.editna);
        edit_phone = (EditText) findViewById(R.id.editphone);

        btn_check.setOnClickListener(this);
        btn_join.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnjoin:
                if (idcheck == true) {

                    if (!edit_id.getText().equals("") &&
                            !edit_pw.getText().equals("") &&
                            !edit_pwcheck.getText().equals("") &&
                            !edit_na.getText().equals("") &&
                            !edit_phone.getText().equals("")) {
                        dia = new ProgressDialog(this);
                        dia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dia.setMessage("Wait...");
                        dia.show();
                        joinProcess(url);
                    } else {
                        //?? ?????
                        Toast.makeText(this, "No empty please", Toast.LENGTH_LONG);
                    }
                } else {
                    Toast.makeText(this, "Please check your ID first.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btncheck:
                if (edit_id.getText().length() < 7) {
                    Toast.makeText(this, "Please make your ID longer than 6 words.", Toast.LENGTH_LONG).show();
                } else {
                    confirmProcess(url2);
                }
                break;
        }
    }

    void joinProcess(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.start();
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String ornot = json.getString("join");
                            if (ornot.equals("success")) {
                                dia.dismiss();
                                Intent join = new Intent(JoinActivity.this, AfterjoinActivity.class);
                                startActivity(join);
                            } else if (ornot.equals("fail")) {
                                dia.dismiss();
                                Toast.makeText(JoinActivity.this, "FAIL.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", edit_id.getText().toString());
                params.put("pw", edit_pw.getText().toString());
                params.put("storenum", edit_stonum.getText().toString());
                params.put("name", edit_na.getText().toString());
                params.put("role", "owner".toString());
                params.put("phone", edit_phone.getText().toString());
                return params;
            }
        };
        queue.add(jsObjRequest);
    }

    void confirmProcess(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject json = new JSONObject(response);
                            String confirm = json.getString("confirm");
                            if (confirm.equals("possible")) {
                                Toast.makeText(JoinActivity.this ,"This ID can be used.",Toast.LENGTH_LONG).show();
                                idcheck = true;
                            } else if(confirm.equals("impossible")) {
                                Toast.makeText(JoinActivity.this ,"This ID is being used already.", Toast.LENGTH_LONG).show();
                                edit_id.setText("");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<String, String>();
                params.put("confirm_id",edit_id.getText().toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
