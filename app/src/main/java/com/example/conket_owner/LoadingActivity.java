package com.example.conket_owner;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class LoadingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loading);
		Handler hd = new Handler();
		hd.postDelayed(new Runnable() {
			@Override
			public void run() {
				finish();
			}
		}, 3000);
	}
}