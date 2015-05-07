package com.example.conket_owner;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StorelistActivity extends Activity implements OnItemClickListener, OnClickListener{
    Activity act = this;
    GridView gridView;
    Button btnreg;

    private List<String> mNames = new ArrayList<String>();
    private List<String> mNumbers = new ArrayList<String>();
    private List<String> mComments = new ArrayList<String>();
    private List<String> mImage_paths = new ArrayList<String>();
    private List<String> mIds = new ArrayList<String>();
    private List<Bitmap> mImages = new ArrayList<Bitmap>();

    String user_id;
    String url = "http://182.219.219.143:12345/DBServer/JSPServer/Store_info.jsp";
    String url2 = "http://182.219.219.143:12345/DBServer/img/store/";

    ProgressDialog dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storelist);

        dia = new ProgressDialog(this);
        dia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dia.setMessage("Wait..");
        dia.show();

        Intent intent = getIntent();
        user_id = intent.getStringExtra("id");

        gridView = (GridView) findViewById(R.id.storelist);
        //gridView.setAdapter(new StoreAdapter(this));

        btnreg = (Button) findViewById(R.id.btnreg);
        btnreg.setOnClickListener(this);

        gridView.setOnItemClickListener(this);
        new StoreInfoSend().execute(url);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("id", mIds.get(position).toString());
        startActivity(intent);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent storereg = new Intent(this, StoreregActivity.class);
		startActivity(storereg);

	}

    private class StoreInfoSend extends AsyncTask<String,String,InputStream> {
        @Override
        protected InputStream doInBackground(String... arg0) {
                return getData((String)arg0[0]);
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

                int pos=0;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagName = parser.getName();
                        if (tagName.equals("info"))
                            isItemTag = true;
                    } else if (eventType == XmlPullParser.TEXT && isItemTag) {
                        if (tagName.equals("shop_name")) {
                            mNames.add(pos,parser.getText());
                        } else if (tagName.equals("cor_num")) {
                            mNumbers.add(pos, parser.getText());
                        } else if (tagName.equals("shop_info")) {
                            mComments.add(pos, parser.getText());
                        } else if (tagName.equals("img_path")) {
                            mImage_paths.add(pos,parser.getText());
                        } else if (tagName.equals("shop_id")) {
                            mIds.add(pos++,parser.getText());
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        tagName = parser.getName();
                        if (tagName.equals("info")) {
                            isItemTag = false;
                            break;
                        }
                    }
                    eventType = parser.next();
                }
                for(int i = 0 ; i < mNames.size(); i++)
                    new StoreImgSend().execute(url2+mImage_paths.get(i));

            }catch (Exception e) {
                e.printStackTrace();
            }

        }

        private InputStream getData(String url)
        {
            HttpClient http = new DefaultHttpClient();
            try {

                ArrayList<NameValuePair> nameValuePairs =
                        new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("user_id", user_id));

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

            } catch(Exception e){return null;}
        }
    }

    private class StoreImgSend extends AsyncTask<String, Integer, Bitmap> {
        Bitmap bmImg;
        InputStream is;

        @Override
        protected Bitmap doInBackground(String... urls) {
            // TODO Auto-generated method stub
                is = getData(urls[0]);
                bmImg = BitmapFactory.decodeStream(is);
            return bmImg;
        }

        protected void onPostExecute(Bitmap img){
            mImages.add(img);
            if(mImages.size() == mNames.size()) {
                gridView.setAdapter(new StoreAdapter(act));
            }
        }

        private InputStream getData(String url)
        {
            HttpClient http = new DefaultHttpClient();
            try {
                URL myFileUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();

                return conn.getInputStream();

            } catch(Exception e){return null;}
        }

    }
    /*
    private List<Integer> mImages = {
            R.drawable.kangwoncheonggwa,
            R.drawable.hanjinsanghoe,
            R.drawable.jongusanghoe,
            R.drawable.chungroksanghoe,
            //R.drawable.kangwoncheonggwa
    };

        private String[] mNames = {
                "Í∞ïÏõêÏ≤?Í≥?",
                "?ïúÏßÑÏÉÅ?öå",
                "Ï¢ÖÏö∞?ÉÅ?öå",
                "Ï≤?Î°ùÏÉÅ?öå",
                "Í∞ïÏõê?ÉÅ?öå",
                "Í∞êÏûê?ÉÅ?öå"
        };

        private String[] mNumbers = {
                "111-1111",
                "222-2222",
                "333-3333",
                "444-4444",
                "555-5555",
                "666-6666"
        };

        private String[] mComments = {
                "?ÖÅ?Ñ¥?Öá",
                "?ÖÇ?Öà?Ñ∑",
                "?Öã?Öå?Öä",
                "?Ñπ?ò∏",
                "?Öõ?Öï?Öõ?Öë",
                "?Öî?Öê?Öë"
        };
        */

    public class StoreAdapter extends BaseAdapter {
        LayoutInflater inflater;

        public StoreAdapter(Context c) {
            inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return mComments.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            TextView number;
            TextView detail;
            TextView name;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.storeitem, parent, false);
            }

            imageView = (ImageView) convertView.findViewById(R.id.store_image);
            imageView.setImageBitmap(mImages.get(position));

            number = (TextView) convertView.findViewById(R.id.store_number);
            number.setText(mNumbers.get(position));
            detail = (TextView) convertView.findViewById((R.id.store_comment));
            detail.setText(mComments.get(position));
            name = (TextView) convertView.findViewById(R.id.store_name);
            name.setText(mNames.get(position));

            dia.dismiss();

            return convertView;
        }
    }
}

