package com.example.conket_owner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class CouponregActivity extends Activity implements OnClickListener {

    Button btnsave;
    final static int SELECT_IMAGE = 1;
    private static final int CAMERA_CAPTURE = 0;

    EditText coupon_year;
    EditText coupon_month;
    EditText coupon_day;
    EditText coupon_year2;
    EditText coupon_month2;
    EditText coupon_day2;
    EditText edit_info;
    CheckBox is_pull;

    ProgressDialog dia;
    String ip_address;
    String url;
    String url2;
    User connected_user;
    String shop_id;
    Coupon coupon = new Coupon();
    File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.couponreg);

        ip_address = this.getResources().getString(R.string.ip_address);
        url = ip_address + ":12345/DBServer/JSPServer/Coupon_reg.jsp";
        url2 = ip_address + ":12345/DBServer/JSPServer/Coupon_img_reg.jsp";

        Intent intent = getIntent();
        connected_user = (User) intent.getParcelableExtra("connected_user");
        shop_id = intent.getStringExtra("shop_id");

        btnsave = (Button) findViewById(R.id.btnsave);
        coupon_year = (EditText)findViewById(R.id.edityear);
        coupon_month = (EditText)findViewById(R.id.editmonth);
        coupon_day = (EditText)findViewById(R.id.editday);
        coupon_year2 = (EditText)findViewById(R.id.edityear2);
        coupon_month2 = (EditText)findViewById(R.id.editmonth2);
        coupon_day2 = (EditText)findViewById(R.id.editday2);
        edit_info = (EditText)findViewById(R.id.editinfo);
        is_pull = (CheckBox)findViewById(R.id.chpull);

        btnsave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (    !coupon_year.getText().equals("") &&
                !coupon_month.getText().equals("") &&
                !coupon_day.getText().equals("") &&
                !coupon_year2.getText().equals("")&&
                !coupon_month2.getText().equals("")&&
                !coupon_day2.getText().equals("")&&
                !edit_info.getText().equals(""))
        {
            dia = new ProgressDialog(this);
            dia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dia.setMessage("Wait...");
            dia.show();
            coupon.setStarted(coupon_year.getText() + "-" + coupon_month.getText() + "-" + coupon_day.getText());
            coupon.setExpired(coupon_year2.getText() + "-" + coupon_month2.getText() + "-" + coupon_day2.getText());
            coupon.setPull(is_pull.isChecked());
            coupon.setDetail(edit_info.getText().toString());
            coupon.setShopid(shop_id);
            setCouponinfo(url);
        } else {
            Toast.makeText(this, "No empty please", Toast.LENGTH_LONG);
        }

    }

    public void onPopupButtonclick(View button){
        //PopupMenu 객체 생성
        PopupMenu popup = new PopupMenu(this,button);

        //설정한 popup XML을 inflate
        popup.getMenuInflater().inflate(R.menu.camerapop, popup.getMenu());

        //팝업메뉴 클릭 시 이벤트
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // TODO Auto-generated method stub
                switch(item.getItemId()){
                    case R.id.camera:
                        Random random = new Random();
                        Integer r = random.nextInt();
                        file = new File("/sdcard/"+r.toString()+".jpg");
                        Intent i = new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(file));
                        startActivityForResult(i, CAMERA_CAPTURE);
                        break;

                    case R.id.select:
                        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        Intent intent = new Intent(Intent.ACTION_PICK, uri);
                        startActivityForResult(intent, SELECT_IMAGE);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        // TODO Auto-generated method stub
        Bitmap bitmap = null;
        Bitmap captureBmp = null;
        if (resultCode == RESULT_OK && requestCode == SELECT_IMAGE) {
            Uri image = intent.getData();
            try {
                bitmap = Images.Media.getBitmap(getContentResolver(), image);
                file = new File(getPathFromUri(image));
                coupon.setImage_path(file.getName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView cameraview = (ImageView) findViewById(R.id.cameraview);
            cameraview.setImageBitmap(bitmap);
        }
        if (resultCode == RESULT_OK && requestCode == CAMERA_CAPTURE) {
            Random random = new Random();
            Integer r = random.nextInt();
            file = new File("/sdcard/"+r.toString()+".jpg");
            coupon.setImage_path(file.getName());
            try {
                captureBmp = Images.Media.getBitmap(getContentResolver(),
                        Uri.fromFile(file));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView cameraview = (ImageView) findViewById(R.id.cameraview);
            cameraview.setImageBitmap(captureBmp);
        }
    }

    void setCouponinfo(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            String confirm = json.getString("coupon");
                            if (confirm.equals("success")) {
                                new uploadProcess().execute(url2);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dia.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("started", coupon.getStarted());
                params.put("expired", coupon.getExpired());
                params.put("detail", coupon.getDetail());
                params.put("shop_id", shop_id);
                if(coupon.getPull())//신상품이면
                {
                    params.put("pull", "1");
                }
                else
                {
                    params.put("pull", "0");
                }
                File f = new File(coupon.getImage_path());
                params.put("img_path",f.getName());
                return params;
            }
        };
        queue.add(jsObjRequest);
    }

    private class uploadProcess extends AsyncTask<String, String, InputStream> {

        @Override
        protected InputStream doInBackground(String... arg0) {
            return getData((String) arg0[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(InputStream response) {
            try {
                JSONObject json = new JSONObject(response.toString());
                Intent intent = new Intent(CouponregActivity.this, MainActivity.class);
                intent.putExtra("connected_user", connected_user);
                intent.putExtra("shop_id", shop_id);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private InputStream getData(String url) {
            try {
                String sdcard = Environment.getExternalStorageDirectory().getPath();
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url2);
                FileBody bin = new FileBody(file);
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("image", bin);
                post.setEntity(reqEntity);

                HttpResponse response = client.execute(post);
                HttpEntity resEntity = response.getEntity();

                if (resEntity != null)
                {
                    return resEntity.getContent();
                }
            } catch (Exception e) {
            }
            return null;
        }
    }

    public String getPathFromUri(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null );
        cursor.moveToNext();
        String path = cursor.getString( cursor.getColumnIndex( "_data" ) );
        cursor.close();
        return path;
    }
}
