package com.example.conket_owner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

public class ReviewwriteActivity  extends Activity implements OnClickListener {

    Button btncamera, btnwrite;
    final static int SELECT_IMAGE = 1;
    private static final int CAMERA_CAPTURE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewwrite);

        btnwrite = (Button)findViewById(R.id.btnwrite);
        btnwrite.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        finish();

    }

}

