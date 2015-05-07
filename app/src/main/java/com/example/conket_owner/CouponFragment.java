package com.example.conket_owner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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


public class CouponFragment extends Fragment {

    String shop_id;
	Button btnreg;
    GridView gridView;
    View v;
    LayoutInflater inflater;

    private List<String> mDetails = new ArrayList<String>();
    private List<String> mUsedorNots = new ArrayList<String>();
    private List<String> mExpireds = new ArrayList<String>();
    private List<String> mImage_paths = new ArrayList<String>();
    private List<Bitmap> mImages = new ArrayList<Bitmap>();

    String url = "http://192.168.219.103:12345/DBServer/JSPServer/Coupon_info.jsp";
    String url2 = "http://192.168.219.103:12345/DBServer/img/coupon/";

	static CouponFragment newInstance() {
		return new CouponFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        this.inflater = inflater;
		v = inflater.inflate(R.layout.coupon, container, false);

        Intent intent = getActivity().getIntent();
        shop_id = intent.getStringExtra("id");

		btnreg = (Button)v.findViewById(R.id.btnreg);
		btnreg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent couponreg = new Intent(getActivity()
						.getApplicationContext(), CouponregActivity.class);
				startActivity(couponreg);
			}
		});

        gridView = (GridView)v.findViewById(R.id.couponlist);

        new CouponInfoSend().execute(url);

		return v;
	}

    private class CouponInfoSend extends AsyncTask<String,String,InputStream> {
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
                        if (tagName.equals("Detail")) {
                            mDetails.add(pos,parser.getText());
                        } else if (tagName.equals("UsedorNot")) {
                            mUsedorNots.add(pos, parser.getText());
                        } else if (tagName.equals("Expired")) {
                            mExpireds.add(pos, parser.getText());
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
                for(int i = 0 ; i < mExpireds.size(); i++)
                    new CouponImgSend().execute(url2+mImage_paths.get(i));

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

    private class CouponImgSend extends AsyncTask<String, Integer, Bitmap> {
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
            if(mImages.size() == mExpireds.size()) {
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

    public class GridAdapter extends BaseAdapter {
        private Context mContext;

        public GridAdapter(Context c) {
            mContext = c;
        }

        // 이미지셋에 있는아이템의 수를 반환함(그리드뷰는 아이템의 수에 해당하는 행렬을 준비함)
        public int getCount() {
            return mImages.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // 주어진 위치(position)에 출력할 이미지를 반환함
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            TextView content;
            TextView dday;
            boolean usedornot;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.couponitem, parent, false);
            }

            imageView = (ImageView) convertView.findViewById(R.id.couponimage);
            imageView.setImageBitmap(mImages.get(position));

            content = (TextView) convertView.findViewById(R.id.content);
            content.setText(mDetails.get(position));
            dday = (TextView) convertView.findViewById((R.id.dday));
            dday.setText(mExpireds.get(position));

            return convertView;
        }

        // 출력될 이미지 데이터셋(res/drawable 폴더)
        // 어느 액티비티에서 불러오는지에 따라 서버에 요청하는 데이터도 달라져야함
        //여기 부분 다시 수정해야됨.

    }

}