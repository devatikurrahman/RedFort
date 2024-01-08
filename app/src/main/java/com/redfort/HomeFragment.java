package com.redfort;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeFragment extends Fragment implements OnClickListener {

	private View rootView;
	public static final String FRAGMENT_TAG = "TAG_HomeFragment";
	
	SharedPreferences sPref;
	ImageView logo_icon;
	Button call_button;
	TextView description;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState == null){
			rootView = inflater.inflate(R.layout.home_fragment, container, false);
		}
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (savedInstanceState == null) 
			Initialize();
			
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

	public void Initialize() {
		MainActivity.top_bar_title.setText("Red Fort");
		MainActivity.slide_menu_button.setBackgroundResource(R.drawable.top_left_slide_button);
		sPref = getActivity().getSharedPreferences("RedFortPrefs", Context.MODE_PRIVATE);
		
		logo_icon = (ImageView) rootView.findViewById(R.id.logo_icon);
		description = (TextView) rootView.findViewById(R.id.description);
		
		call_button = (Button) rootView.findViewById(R.id.call_button);
		call_button.setOnClickListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.call_button:
			break;

		default:
			break;
		}
	}
}
