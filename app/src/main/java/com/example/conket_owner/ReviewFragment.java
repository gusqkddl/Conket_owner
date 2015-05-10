package com.example.conket_owner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReviewFragment extends Fragment {

    Button btnwrite;
    ListView listView;
    View v;
    LayoutInflater inflater;
    String shop_id;

    List<Review> reviewItems = new ArrayList<Review>();
    ReviewAdapter adapter;

    String ip_address;
    String url;
    String url2;

    static ReviewFragment newInstance() {
        return new ReviewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.review, container, false);

        ip_address = this.getResources().getString(R.string.ip_address);
        url = ip_address+":12345/DBServer/JSPServer/Review_info.jsp";
        url2 = ip_address+":12345/DBServer/img/review/";

        Intent intent = getActivity().getIntent();
        shop_id = intent.getStringExtra("shop_id");

        btnwrite = (Button) v.findViewById(R.id.btnwrite);
        btnwrite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent write = new Intent(getActivity()
                        .getApplicationContext(), ReviewwriteActivity.class);
                startActivity(write);

            }
        });
        listView = (ListView)v.findViewById(R.id.review_list);
        adapter = new ReviewAdapter(v.getContext());
        listView.setAdapter(adapter);
        getReviewInfo(url);

        return v;
    }

    void getReviewInfo(String url) {
        RequestQueue queue = Volley.newRequestQueue(v.getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Review review = new Review();
                                    review.setId(jsonObject.getString("review_id"));
                                    review.setUser_Id(jsonObject.getString("user_id"));
                                    review.setDate(jsonObject.getString("date"));
                                    review.setReview(jsonObject.getString("content"));
                                    review.setImage_path(jsonObject.getString("img_path"));

                                    reviewItems.add(review);
                                }

                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
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

    public class ReviewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        public ReviewAdapter(Context c) {
            inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return reviewItems.size();
        }

        public Object getItem(int position) {
            return reviewItems.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            NetworkImageView imageView;
            TextView user_id;
            TextView content;
            TextView date;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.reviewitem, parent, false);
            }

            Review review = reviewItems.get(position);

            imageView = (NetworkImageView) convertView.findViewById(R.id.review_image);
            imageView.setImageUrl(url2 + review.getImage_path(), imageLoader);

            user_id = (TextView) convertView.findViewById(R.id.review_user_id);
            StringBuffer tmp_id = new StringBuffer(reviewItems.get(position).getUser_Id());
            user_id.setText(tmp_id.replace(3,tmp_id.length(),"****"));
            content = (TextView) convertView.findViewById((R.id.review_content));
            content.setText(reviewItems.get(position).getReview());
            date = (TextView) convertView.findViewById(R.id.review_date);
            date.setText(reviewItems.get(position).getDate());

            return convertView;
        }
    }
}
