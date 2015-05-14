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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
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

public class ProductregActivity extends Activity implements OnClickListener {

    Button btnsave;
    final static int SELECT_IMAGE = 1;
    private static final int CAMERA_CAPTURE = 0;
    private TextView mDateYear;
    private TextView mDateMonth;
    private TextView mDateDay;
    private TextView mPickDate;
    private Integer mYear;
    private Integer mMonth;
    private Integer mDay;
    static final int DATE_DIALOG_ID=0;

    /////http
    ProgressDialog dia;
    String ip_address;
    String url;
    String url2;
    User connected_user;
    Product product = new Product();
    File file;
    String shop_id;

    ////
    EditText et_name;
    EditText et_price;
    EditText et_origin;
    EditText et_count;
    EditText et_function;
    CheckBox cb_new;
    CheckBox cb_timely;
    Spinner howcount;

    private DatePickerDialog.OnDateSetListener mDateSetListener = new
            DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch(id){
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,mDateSetListener,mYear,mMonth,mDay);
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productreg);

        ip_address = this.getResources().getString(R.string.ip_address);
        url = ip_address + ":12345/DBServer/JSPServer/Product_reg.jsp";
        url2 = ip_address + ":12345/DBServer/JSPServer/Product_img_reg.jsp";

        et_name = (EditText)findViewById(R.id.editname);
        et_price = (EditText)findViewById(R.id.editprice);
        et_origin = (EditText)findViewById(R.id.editorigin);
        et_count = (EditText)findViewById(R.id.editamount);
        et_function = (EditText)findViewById(R.id.editeffet);
        cb_new = (CheckBox)findViewById(R.id.chnew);
        cb_timely = (CheckBox)findViewById(R.id.chseason);
        howcount = (Spinner)findViewById(R.id.spinamount);

        Intent intent = getIntent();
        connected_user = (User) intent.getParcelableExtra("connected_user");
        shop_id = intent.getStringExtra("shop_id");

        mDateYear = (TextView)findViewById(R.id.edityear);
        mDateMonth = (TextView)findViewById(R.id.editmonth);
        mDateDay = (TextView)findViewById(R.id.editday);
        mPickDate = (TextView)findViewById(R.id.editmonth);

        mPickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(DATE_DIALOG_ID);

            }
        });

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth =c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        //현재 날짜를 표시한다.
        updateDisplay();

        btnsave = (Button) findViewById(R.id.btnsave);

        btnsave.setOnClickListener(this);
        Spinner spinner = (Spinner)findViewById(R.id.spinamount);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.amount, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }


    private void updateDisplay() {
        // TODO Auto-generated method stub
        mDateYear.setText(new StringBuilder()
                .append(mYear));
        mDateMonth.setText(new StringBuilder()
                .append(mMonth+1));
        mDateDay.setText(new StringBuilder()
                .append(mDay));
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (!et_price.getText().equals("") &&
                !et_price.getText().equals("") &&
                !et_origin.getText().equals("") &&
                !et_count.getText().equals("") &&
                !et_function.getText().equals(""))
        {
            dia = new ProgressDialog(this);
            dia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dia.setMessage("Wait...");
            dia.show();
            product.setName(et_name.getText().toString());
            product.setPrice(et_price.getText().toString());
            product.setOrigin(et_origin.getText().toString());
            product.setDate(mYear.toString() + "-" + mMonth.toString() + "-" + mDay.toString());
            product.setCount(et_count.getText().toString());
            product.setComment(et_function.getText().toString());
            product.setNew(cb_new.isChecked());
            product.setTimely(cb_timely.isChecked());
            product.setHowcount(howcount.getSelectedItem().toString());
            product.setShopid(shop_id);
            setProductinfo(url);
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
                product.setImage_path(file.getName());
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
            product.setImage_path(file.getName());
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

    void setProductinfo(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            String confirm = json.getString("product");
                            if (confirm.equals("success")) {
                                product.setId(json.getString("shop_id"));
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
                params.put("name", product.getName());
                params.put("price", product.getPrice());
                params.put("origin", product.getOrigin());
                params.put("date", product.getDate());
                params.put("count", product.getCount());
                params.put("comment", product.getCount());
                params.put("howcount", product.getHowcount());
                params.put("shop_id", product.getShopid());
                if(product.getNew())//신상품이면
                {
                    params.put("new", "1");
                }
                else
                {
                    params.put("new", "0");
                }

                if(product.getTimely())//제철상품이면
                {
                    params.put("timely", "1");
                }
                else
                {
                    params.put("timely", "0");
                }
                File f = new File(product.getImage_path());
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
                Intent intent = new Intent(ProductregActivity.this, MainActivity.class);
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
