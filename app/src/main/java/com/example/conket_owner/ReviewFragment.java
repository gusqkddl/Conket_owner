package com.example.conket_owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



public class ReviewFragment extends Fragment {

	Button btnwrite
	;
    static ReviewFragment newInstance() {
       return new ReviewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {

    	 View v = inflater.inflate(R.layout.review, container, false);

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
       return v;
    }
 }
