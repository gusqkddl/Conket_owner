package com.example.conket_owner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class JoinActivity extends Activity implements OnClickListener  {

    Button btnjoin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        btnjoin = (Button) findViewById(R.id.btnjoin);

        btnjoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent afterjoin = new Intent(this, AfterjoinActivity.class);
        startActivity(afterjoin);

    }

}
