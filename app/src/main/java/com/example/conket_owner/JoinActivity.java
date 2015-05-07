package com.example.conket_owner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;

public class JoinActivity extends Activity implements OnClickListener  {
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

    String url = "http://182.219.219.143:12345/DBServer/JSPServer/ID_Confirm.jsp";
    String url2 = "http://182.219.219.143:12345/DBServer/JSPServer/Join.jsp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        btn_join = (Button) findViewById(R.id.btnjoin);
        btn_check = (Button) findViewById(R.id.btncheck);

        edit_id = (EditText)findViewById(R.id.editid);
        edit_pw = (EditText)findViewById(R.id.editpw);
        edit_pwcheck = (EditText)findViewById(R.id.editpwcheck);
        edit_stonum = (EditText)findViewById(R.id.editstonum);
        edit_na = (EditText)findViewById(R.id.editna);
        edit_phone = (EditText)findViewById(R.id.editphone);

        btn_check.setOnClickListener(this);
        btn_join.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnjoin:
                if(idcheck == true) {

                    if(!edit_id.getText().equals("") &&
                        !edit_pw.getText().equals("") &&
                         !edit_pwcheck.getText().equals("") &&
                            !edit_na.getText().equals("") &&
                            !edit_phone.getText().equals(""))
                    {
                        dia = new ProgressDialog(this);
                        dia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dia.setMessage("Wait...");
                        dia.show();
                        new joinProcess().execute(url2);
                    }
                    else {
                        //다 채워라
                        Toast.makeText(this,"No empty please",Toast.LENGTH_LONG);
                    }
                }
                else {
                        Toast.makeText(this,"Please check your ID first.",Toast.LENGTH_LONG).show();
                    }
                break;
            case R.id.btncheck:
                if(edit_id.getText().length()<7) {
                    Toast.makeText(this,"Please make your ID longer than 6 words.",Toast.LENGTH_LONG).show();
                } else {
                    new confirmProcess().execute(url);
                }
                break;
        }
    }

    private class confirmProcess extends AsyncTask<String, String, InputStream> {

        @Override
        protected InputStream doInBackground(String... arg0) {
            return getData((String) arg0[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(InputStream is) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                factory.setNamespaceAware(true);

                parser.setInput(is, "utf-8");

                int eventType = parser.getEventType();
                String tagName = "";
                boolean isItemTag = false;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagName = parser.getName();
                        if (tagName.equals("info"))
                            isItemTag = true;
                    } else if (eventType == XmlPullParser.TEXT && isItemTag) {
                        if (tagName.equals("confirm")) {
                            //로그인성공
                            if (parser.getText().equals("possible")) {
                                Toast.makeText(JoinActivity.this ,"This ID can be used.",Toast.LENGTH_LONG).show();
                                idcheck = true;
                            } else if(parser.getText().equals("impossible")) {
                                Toast.makeText(JoinActivity.this ,"This ID is being used already.", Toast.LENGTH_LONG).show();
                                edit_id.setText("");
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        tagName = parser.getName();
                        if (tagName.equals("info"))
                            isItemTag = false;
                        break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private InputStream getData(String url) {

            HttpClient http = new DefaultHttpClient();
            try {

                ArrayList<NameValuePair> nameValuePairs =
                        new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("confirm_id", edit_id.getText().toString()));

                HttpParams params = http.getParams();
                HttpConnectionParams.setConnectionTimeout(params, 5000);
                HttpConnectionParams.setSoTimeout(params, 5000);

                HttpPost httpPost = new HttpPost(url);
                UrlEncodedFormEntity entityRequest =
                        new UrlEncodedFormEntity(nameValuePairs, "UTF-8");

                httpPost.setEntity(entityRequest);

                HttpResponse responsePost = http.execute(httpPost);
                HttpEntity resEntity = responsePost.getEntity();

                return resEntity.getContent();

            } catch (Exception e) {
                return null;
            }
        }
    }

    private class joinProcess extends AsyncTask<String, String, InputStream> {

        @Override
        protected InputStream doInBackground(String... arg0) {
            return getData((String) arg0[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(InputStream is) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                factory.setNamespaceAware(true);

                parser.setInput(is, "utf-8");

                int eventType = parser.getEventType();
                String tagName = "";
                boolean isItemTag = false;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagName = parser.getName();
                        if (tagName.equals("info"))
                            isItemTag = true;
                    } else if (eventType == XmlPullParser.TEXT && isItemTag) {
                        if (tagName.equals("join")) {
                            //로그인성공
                            if (parser.getText().equals("success")) {
                                Intent join = new Intent(JoinActivity.this, AfterjoinActivity.class);
                                startActivity(join);
                            } else if(parser.getText().equals("fail")) {
                                Toast.makeText(JoinActivity.this,"FAIL.",Toast.LENGTH_LONG).show();
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        tagName = parser.getName();
                        if (tagName.equals("info"))
                            isItemTag = false;
                        break;
                    }
                    eventType = parser.next();
                    dia.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private InputStream getData(String url) {

            HttpClient http = new DefaultHttpClient();
            try {

                ArrayList<NameValuePair> nameValuePairs =
                        new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("id", edit_id.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("pw", edit_pw.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("storenum", edit_stonum.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("name", edit_na.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("role", "owner".toString()));
                nameValuePairs.add(new BasicNameValuePair("phone", edit_phone.getText().toString()));

                HttpParams params = http.getParams();
                HttpConnectionParams.setConnectionTimeout(params, 5000);
                HttpConnectionParams.setSoTimeout(params, 5000);

                HttpPost httpPost = new HttpPost(url);
                UrlEncodedFormEntity entityRequest =
                        new UrlEncodedFormEntity(nameValuePairs, "UTF-8");

                httpPost.setEntity(entityRequest);

                HttpResponse responsePost = http.execute(httpPost);
                HttpEntity resEntity = responsePost.getEntity();

                return resEntity.getContent();

            } catch (Exception e) {
                return null;
            }
        }
    }

}
