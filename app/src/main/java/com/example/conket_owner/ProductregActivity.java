package com.example.conket_owner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

public class ProductregActivity extends Activity implements OnClickListener {

    Button btnsave;
    final static int SELECT_IMAGE = 1;
    private static final int CAMERA_CAPTURE = 0;
    private TextView mDateYear;
    private TextView mDateMonth;
    private TextView mDateDay;
    private TextView mPickDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID=0;

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
        finish();

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
                        Intent i = new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(new File("/sdcard/image.jpg")));
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView cameraview = (ImageView) findViewById(R.id.cameraview);
            cameraview.setImageBitmap(bitmap);
        }
        if (resultCode == RESULT_OK && requestCode == CAMERA_CAPTURE) {
            File file = new File("/sdcard/image.jpg");
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

}
