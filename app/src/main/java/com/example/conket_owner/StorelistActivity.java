package com.example.conket_owner;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorelistActivity extends Activity implements OnItemClickListener, OnClickListener{
    Activity act = this;
    ListView listView;
    Button btnreg;

    String ip_address;
    String user_id;
    String url;
    String url2;

    ProgressDialog dia;
    private List<Store> storeItems = new ArrayList<Store>();
    private StoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storelist);

        ip_address = this.getResources().getString(R.string.ip_address);
        url = ip_address+":12345/DBServer/JSPServer/Store_info.jsp";
        url2 = ip_address+":12345/DBServer/img/store/";

        dia = new ProgressDialog(this);
        dia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dia.setMessage("Wait..");
        dia.show();

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        listView = (ListView) findViewById(R.id.storelist);
        adapter = new StoreAdapter(this);
        listView.setAdapter(adapter);
        getStoreInfo(url);

        btnreg = (Button) findViewById(R.id.btnreg);
        btnreg.setOnClickListener(this);
        listView.setOnItemClickListener(this);

    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("shop_id", storeItems.get(position).getId());
        startActivity(intent);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent storereg = new Intent(this, StoreregActivity.class);
		startActivity(storereg);
	}

    void getStoreInfo(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("[]")) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Store store = new Store();
                                    store.setName(jsonObject.getString("name"));
                                    store.setNumber(jsonObject.getString("number"));
                                    store.setComment(jsonObject.getString("comment"));
                                    store.setId(jsonObject.getString("id"));
                                    store.setImage_path(jsonObject.getString("img_path"));

                                    storeItems.add(store);
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
                params.put("user_id", user_id);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    public class StoreAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        public StoreAdapter(Context c) {
            inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return storeItems.size();
        }

        public Object getItem(int position) {
            return storeItems.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            NetworkImageView imageView;
            TextView number;
            TextView comment;
            TextView name;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.storeitem, parent, false);
            }

            Store store = storeItems.get(position);

            imageView = (NetworkImageView) convertView.findViewById(R.id.store_image);
            imageView.setImageUrl(url2+store.getImage_path(), imageLoader);

            number = (TextView) convertView.findViewById(R.id.store_number);
            number.setText(storeItems.get(position).getNumber());
            comment = (TextView) convertView.findViewById((R.id.store_comment));
            comment.setText(storeItems.get(position).getComment());
            name = (TextView) convertView.findViewById(R.id.store_name);
            name.setText(storeItems.get(position).getName());

            dia.dismiss();

            return convertView;
        }
    }
}

