package com.example.conket_owner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class OptionFragment extends Fragment {

    static OptionFragment newInstance() {
       return new OptionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {

    	 View v = inflater.inflate(R.layout.option, container, false);
    	 

    
       return v;
    }
 }