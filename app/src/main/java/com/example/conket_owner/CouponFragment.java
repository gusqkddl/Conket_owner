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
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CouponFragment extends Fragment {

    String shop_id;
    Button btnreg;
    ListView listView;
    View v;
    LayoutInflater inflater;

    List<Coupon> couponItems = new ArrayList<Coupon>();
    CouponAdapter adapter;

    String ip_address;
    String url;
    String url2;

    static CouponFragment newInstance() {
        return new CouponFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        v = inflater.inflate(R.layout.coupon, container, false);

        Intent intent = getActivity().getIntent();
        shop_id = intent.getStringExtra("shop_id");

        ip_address = this.getResources().getString(R.string.ip_address);
        url = ip_address+":12345/DBServer/JSPServer/Coupon_info.jsp";
        url2 = ip_address+":12345/DBServer/img/coupon/";

        btnreg = (Button) v.findViewById(R.id.btnreg);
        btnreg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent couponreg = new Intent(getActivity()
                        .getApplicationContext(), CouponregActivity.class);
                startActivity(couponreg);
            }
        });

        listView = (ListView) v.findViewById(R.id.couponlist);
        adapter = new CouponAdapter(v.getContext());
        listView.setAdapter(adapter);
        getCouponInfo(url);

        return v;
    }

    void getCouponInfo(String url) {
        RequestQueue queue = Volley.newRequestQueue(v.getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("[]")) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Coupon coupon = new Coupon();
                                    coupon.setDetail(jsonObject.getString("detail"));
                                    coupon.setUsedornot(jsonObject.getString("usedornot"));
                                    coupon.setExpired(jsonObject.getString("end_date"));
                                    coupon.setId(jsonObject.getString("coupon_id"));
                                    coupon.setImage_path(jsonObject.getString("img_path"));

                                    couponItems.add(coupon);
                                }

                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("shop_id", shop_id);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public class CouponAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        public CouponAdapter(Context c) {
            inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return couponItems.size();
        }

        public Object getItem(int position) {
            return couponItems.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            NetworkImageView imageView;
            TextView detail;
            CheckBox usedornot;
            TextView expired;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.couponitem, parent, false);
            }

            Coupon coupon = couponItems.get(position);

            imageView = (NetworkImageView) convertView.findViewById(R.id.coupon_image);
            imageView.setImageUrl(url2 + coupon.getImage_path(), imageLoader);

            detail = (TextView) convertView.findViewById(R.id.coupon_detail);
            detail.setText(couponItems.get(position).getDetail());
            usedornot = (CheckBox) convertView.findViewById((R.id.usedornot));
            if(couponItems.get(position).getUsedornot())
                usedornot.setChecked(true);
            else
                usedornot.setChecked(false);
            usedornot.setEnabled(false);
            expired = (TextView) convertView.findViewById(R.id.coupon_expired);
            expired.setText(couponItems.get(position).getExpired());

            return convertView;
        }
    }
}
