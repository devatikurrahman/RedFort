package com.redfort;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SpecialOfferFragment extends Fragment {

	private View rootView;
	public static final String FRAGMENT_TAG = "TAG_SpecialOfferFragment";
	
	WebView special_offers_web_view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState == null){
			rootView = inflater.inflate(R.layout.special_offers_fragment, container, false);
		}
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (savedInstanceState == null) 
			Initialize();
	}
	
	public void Initialize() {
		MainActivity.top_bar_title.setText("Special Offers");
		MainActivity.slide_menu_button.setBackgroundResource(R.drawable.top_left_slide_button);
		
		special_offers_web_view = (WebView) rootView.findViewById(R.id.special_offers_web_view);
		special_offers_web_view.getSettings().setJavaScriptEnabled(true);
		special_offers_web_view.loadUrl("http://redfort.org/");
	}
}
