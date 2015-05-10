package com.example.conket_owner;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.*;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity implements OnClickListener {

    Button btnjoin, btnlogin, btnreserch;
    EditText editid, editpw;

    String url = "http://182.219.219.143:12345/DBServer/JSPServer/Login.jsp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // 로딩화면 출력
        startActivity(new Intent(this, LoadingActivity.class));

        editid = (EditText) findViewById(R.id.editid);
        editpw = (EditText) findViewById(R.id.editpw);

        btnjoin = (Button) findViewById(R.id.btnjoin);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        btnreserch = (Button) findViewById(R.id.btnreserch);

        btnjoin.setOnClickListener(this);
        btnlogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnjoin:
                Intent join = new Intent(this, JoinActivity.class);
                startActivity(join);
                break;
            case R.id.btnlogin:
                loginProcess(url);
                break;
        }
    }

    void loginProcess(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject json = new JSONObject(response);
                            String confirm = json.getString("login");
                            if (confirm.equals("success")) {
                                Intent login = new Intent(LoginActivity.this, StorelistActivity.class);
                                login.putExtra("id", editid.getText().toString());
                                startActivity(login);
                            } else if(confirm.equals("fail")) {
                                System.out.println("로그인실패");
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
                params.put("user_id", editid.getText().toString());
                params.put("user_pw", editpw.getText().toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
