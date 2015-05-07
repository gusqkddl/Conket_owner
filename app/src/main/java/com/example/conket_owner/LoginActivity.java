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
                new loginProcess().execute(url);
                break;
        }
    }

    //네트워크는 멀티스레드로 해야 에러가 안남
    private class loginProcess extends AsyncTask<String, String, InputStream> {

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
                        if (tagName.equals("login")) {
                            //로그인성공
                            if (parser.getText().equals("success")) {
                                Intent login = new Intent(LoginActivity.this, StorelistActivity.class);
                                login.putExtra("id", editid.getText().toString());
                                startActivity(login);
                            }
                            //로그인실패
                            else {
                                System.out.println("로그인실패");
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
                nameValuePairs.add(new BasicNameValuePair("user_id", editid.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("user_pw", editpw.getText().toString()));

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
