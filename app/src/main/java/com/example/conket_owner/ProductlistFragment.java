package com.example.conket_owner;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ProductlistFragment extends Fragment {

    Button btnreg;
    GridView gridView;
    View v;
    LayoutInflater inflater;
    String shop_id;

    private List<String> mNames = new ArrayList<String>();
    private List<String> mMades = new ArrayList<String>();
    private List<String> mPrices = new ArrayList<String>();
    private List<String> mImage_paths = new ArrayList<String>();
    private List<Bitmap> mImages = new ArrayList<Bitmap>();

    String url = "http://182.219.219.143:12345/DBServer/JSPServer/Product_info.jsp";
    String url2 = "http://182.219.219.143:12345/DBServer/img/product/";

    static ProductlistFragment newInstance() {
        return new ProductlistFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        v = inflater.inflate(R.layout.productlist, container, false);

        Intent intent = getActivity().getIntent();
        shop_id = intent.getStringExtra("id");

        btnreg = (Button)v.findViewById(R.id.btnreg);
        btnreg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent productreg = new Intent(getActivity().getApplicationContext(), ProductregActivity.class );
                startActivity(productreg);
            }
        });


        gridView = (GridView)v.findViewById(R.id.product_list);

        new ProductInfoSend().execute(url);

        return v;
    }

    private class ProductInfoSend extends AsyncTask<String,String,InputStream> {
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
                        if (tagName.equals("name")) {
                            mNames.add(pos,parser.getText());
                        } else if (tagName.equals("made")) {
                            mMades.add(pos, parser.getText());
                        } else if (tagName.equals("price")) {
                            mPrices.add(pos, parser.getText());
                        } else if (tagName.equals("img_path")) {
                            mImage_paths.add(pos++,parser.getText());
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
                    new ProductImgSend().execute(url2+mImage_paths.get(i));

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
                nameValuePairs.add(new BasicNameValuePair("shop_id", shop_id));

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

    private class ProductImgSend extends AsyncTask<String, Integer, Bitmap> {
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
                gridView.setAdapter(new GridAdapter(v.getContext()));
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

    private class GridAdapter extends BaseAdapter {
        private Context mContext;

        private GridAdapter(Context c) {
            mContext = c;
        }

        // ?ù¥ÎØ∏Ï??Öã?óê ?ûà?äî?ïÑ?ù¥?Öú?ùò ?àòÎ•? Î∞òÌôò?ï®(Í∑∏Î¶¨?ìúÎ∑∞Îäî ?ïÑ?ù¥?Öú?ùò ?àò?óê ?ï¥?ãπ?ïò?äî ?ñâ?†¨?ùÑ Ï§?ÎπÑÌï®)
        public int getCount() {
            return mNames.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // Ï£ºÏñ¥Ïß? ?úÑÏπ?(position)?óê Ï∂úÎ†•?ï† ?ù¥ÎØ∏Ï?Î•? Î∞òÌôò?ï®
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            TextView made;
            TextView price;
            TextView name;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.productitem, parent, false);
            }

            imageView = (ImageView) convertView.findViewById(R.id.pro_image);
            imageView.setImageBitmap(mImages.get(position));

            made = (TextView) convertView.findViewById(R.id.pro_from);
            made.setText(mMades.get(position));
            price = (TextView) convertView.findViewById((R.id.pro_price));
            price.setText(mPrices.get(position));
            name = (TextView) convertView.findViewById(R.id.pro_name);
            name.setText(mNames.get(position));

            return convertView;
        }
    }
}