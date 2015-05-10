package com.example.conket_owner;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
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
import android.widget.Toast;

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


public class ProductlistFragment extends Fragment {

    Button btnreg;
    GridView gridView;
    View v;
    LayoutInflater inflater;
    String shop_id;
    ProgressDialog dia;

    List<Product> productItems = new ArrayList<Product>();
    ProductAdapter adapter;

    String ip_address;
    String url;
    String url2;

    static ProductlistFragment newInstance() {
        return new ProductlistFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        v = inflater.inflate(R.layout.productlist, container, false);

        ip_address = this.getResources().getString(R.string.ip_address);
        url = ip_address+":12345/DBServer/JSPServer/Product_info.jsp";
        url2 = ip_address+":12345/DBServer/img/product/";

        dia = new ProgressDialog(v.getContext());
        dia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dia.setMessage("Wait..");
        dia.show();

        Intent intent = getActivity().getIntent();
        shop_id = intent.getStringExtra("shop_id");

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
        adapter = new ProductAdapter(v.getContext());
        gridView.setAdapter(adapter);
        getProductInfo(url);

        return v;
    }

    void getProductInfo(String url) {
        RequestQueue queue = Volley.newRequestQueue(v.getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.trim().equals("[]")) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Product product = new Product();
                                    product.setName(jsonObject.getString("name"));
                                    product.setNumber(jsonObject.getString("origin"));
                                    product.setComment(jsonObject.getString("price"));
                                    product.setId(jsonObject.getString("goods_id"));
                                    product.setImage_path(jsonObject.getString("img_path"));

                                    productItems.add(product);
                                }

                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                            dia.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("shop_id", shop_id);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public class ProductAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        public ProductAdapter(Context c) {
            inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return productItems.size();
        }

        public Object getItem(int position) {
            return productItems.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            NetworkImageView imageView;
            TextView name;
            TextView origin;
            TextView price;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.productitem, parent, false);
            }

            Product product = productItems.get(position);

            imageView = (NetworkImageView) convertView.findViewById(R.id.pro_image);
            imageView.setImageUrl(url2+product.getImage_path(), imageLoader);

            name = (TextView) convertView.findViewById(R.id.pro_name);
            name.setText(productItems.get(position).getNumber());
            origin = (TextView) convertView.findViewById((R.id.pro_from));
            origin.setText(productItems.get(position).getComment());
            price = (TextView) convertView.findViewById(R.id.pro_price);
            price.setText(productItems.get(position).getName());

            dia.dismiss();

            return convertView;
        }
    }
}