package com.example.conket_owner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AfterjoinActivity extends Activity implements OnClickListener {

	Button btnbacklogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.afterjoin);

		btnbacklogin = (Button) findViewById(R.id.btnbacklogin);

		btnbacklogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent login = new Intent(this, LoginActivity.class);
		login.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(login);
		finish();

	}

}
