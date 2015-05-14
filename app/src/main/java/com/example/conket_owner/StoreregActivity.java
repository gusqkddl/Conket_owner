package com.example.conket_owner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.navercorp.volleyextensions.volleyer.Volleyer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class StoreregActivity extends Activity implements OnClickListener {
    Store store = new Store();
    Button btnsave;
    final static int SELECT_IMAGE = 1;
    private static final int CAMERA_CAPTURE = 0;
    ProgressDialog dia;
    String ip_address;
    String url;
    String url2;
    User connected_user;
    EditText et_sto_name;
    EditText et_sto_num;
    EditText et_sto_phone;
    EditText et_sto_info;
    EditText et_sto_beacon;
    ImageView cameraview;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storereg);

        et_sto_name = (EditText)findViewById(R.id.editstoname);
        et_sto_num = (EditText)findViewById(R.id.editstonum);
        et_sto_phone = (EditText)findViewById(R.id.editstophone);
        et_sto_info = (EditText)findViewById(R.id.editstoinfo);
        et_sto_beacon = (EditText)findViewById(R.id.editstobeaid);
        cameraview = (ImageView)findViewById(R.id.cameraview);

        Intent intent = getIntent();
        connected_user = (User) intent.getParcelableExtra("connected_user");

        ip_address = this.getResources().getString(R.string.ip_address);
        url = ip_address + ":12345/DBServer/JSPServer/Store_reg.jsp";
        url2 = ip_address + ":12345/DBServer/JSPServer/Store_img_reg.jsp";

        btnsave = (Button) findViewById(R.id.btnsave);
        btnsave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (!et_sto_name.getText().equals("") &&
                !et_sto_num.getText().equals("") &&
                !et_sto_phone.getText().equals("") &&
                !et_sto_info.getText().equals("") &&
                !et_sto_beacon.getText().equals(""))
        {
            dia = new ProgressDialog(this);
            dia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dia.setMessage("Wait...");
            dia.show();
            store.setName(et_sto_name.getText().toString());
            store.setNumber(et_sto_num.getText().toString());
            store.setPhone(et_sto_phone.getText().toString());
            store.setComment(et_sto_info.getText().toString());
            store.setBeacon(et_sto_beacon.getText().toString());
            store.setUser_id(connected_user.getId());
            setStoreinfo(url);
        } else {
            Toast.makeText(this, "No empty please", Toast.LENGTH_LONG);
        }
    }

    public void onPopupButtonclick(View button) {
        // PopupMenu 객체 생성
        PopupMenu popup = new PopupMenu(this, button);

        // 설정한 popup XML을 inflate
        popup.getMenuInflater().inflate(R.menu.camerapop, popup.getMenu());

        // 팝업메뉴 클릭 시 이벤트
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // TODO Auto-generated method stub
                switch (item.getItemId()) {
                    case R.id.camera://사진 촬영
                        Intent i = new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        Random random = new Random();
                        Integer r = random.nextInt();
                        file = new File("/sdcard/"+r.toString()+".jpg");
                        i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(file));
                        startActivityForResult(i, CAMERA_CAPTURE);
                        break;

                    case R.id.select://사진 앨범
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
                store.setImage_path(file.getName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cameraview.setImageBitmap(bitmap);
            //store.setImage(bitmap);
        }
        if (resultCode == RESULT_OK && requestCode == CAMERA_CAPTURE) {
            Random random = new Random();
            Integer r = random.nextInt();
            file = new File("/sdcard/"+r.toString()+".jpg");
            store.setImage_path(file.getName());
            try {
                captureBmp = Images.Media.getBitmap(getContentResolver(),
                        Uri.fromFile(file));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cameraview.setImageBitmap(captureBmp);
            //store.setImage(captureBmp);
        }
    }

    void setStoreinfo(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            String confirm = json.getString("store");
                            if (confirm.equals("success")) {
                                store.setId(json.getString("shop_id"));
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
                params.put("name", store.getName());
                params.put("number", store.getNumber());
                params.put("phone", store.getPhone());
                params.put("comment", store.getComment());
                params.put("beacon", store.getBeacon());
                File f = new File(store.getImage_path());
                params.put("img_path",f.getName());
                params.put("user_id", store.getUser_id());
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
                Intent login = new Intent(StoreregActivity.this, StorelistActivity.class);
                login.putExtra("connected_user", connected_user);
                startActivity(login);

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