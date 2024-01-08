package com.redfort;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class GalleryFragment extends Fragment implements OnClickListener {

	private View rootView;
	public static final String FRAGMENT_TAG = "TAG_GalleryFragment";
	
	SharedPreferences sPref;
	Button next_button, previous_button;
	ViewPager image_view_pager;
	
	Button indicator0, indicator1, indicator2, indicator3;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState == null){
			rootView = inflater.inflate(R.layout.gallery_fragment, container, false);
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
		MainActivity.top_bar_title.setText("Gallery");
		MainActivity.slide_menu_button.setBackgroundResource(R.drawable.top_left_slide_button);
		sPref = getActivity().getSharedPreferences("RedFortPrefs", Context.MODE_PRIVATE);
		
		next_button = (Button) rootView.findViewById(R.id.next_button);
		next_button.setOnClickListener(this);
		
		previous_button = (Button) rootView.findViewById(R.id.previous_button);
		previous_button.setOnClickListener(this);

		indicator0 = (Button) rootView.findViewById(R.id.indicator0);
		indicator1 = (Button) rootView.findViewById(R.id.indicator1);
		indicator2 = (Button) rootView.findViewById(R.id.indicator2);
		indicator3 = (Button) rootView.findViewById(R.id.indicator3);
		
		PagerInit();
	}
	
	int [] image_array = {R.drawable.slide0, R.drawable.slide1, R.drawable.slide2, R.drawable.slide3};
	
	public void PagerInit(){

		image_view_pager = (ViewPager) rootView.findViewById(R.id.image_view_pager);
		image_view_pager.setAdapter(new PagerAdapter() {

			private LayoutInflater inflater;
			
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView((View) object);
			}

			@Override
			public int getCount() {
				return image_array.length;
			}

			@Override
			public Object instantiateItem(ViewGroup view, int position) {
				inflater = getActivity().getLayoutInflater();
				View imageLayout = inflater.inflate(R.layout.pager_image_view_lay, view, false);
				assert imageLayout != null;
				ImageView imageView = (ImageView) imageLayout.findViewById(R.id.gallery_image);
				
				imageView.setImageResource(image_array[position]);
				view.addView(imageLayout, 0);
				return imageLayout;
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view.equals(object);
			}

			@Override
			public void restoreState(Parcelable state, ClassLoader loader) {
			}

			@Override
			public Parcelable saveState() {
				return null;
			}
		});
		
		image_view_pager.setCurrentItem(0);
		current_item = 0;
		indicator0.setSelected(true);
		image_view_pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				if(position == 0){
					indicator0.setSelected(true);
					indicator1.setSelected(false);
					indicator2.setSelected(false);
					indicator3.setSelected(false);
				} else if(position == 1){
					indicator0.setSelected(false);
					indicator1.setSelected(true);
					indicator2.setSelected(false);
					indicator3.setSelected(false);
				} else if(position == 2){
					indicator0.setSelected(false);
					indicator1.setSelected(false);
					indicator2.setSelected(true);
					indicator3.setSelected(false);
				} else if(position == 3){
					indicator0.setSelected(false);
					indicator1.setSelected(false);
					indicator2.setSelected(false);
					indicator3.setSelected(true);
				}
				
				current_item = position;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.next_button:
			if (current_item<3) {
				image_view_pager.setCurrentItem(++current_item);
			}
			Log.v("next_button", "clicked");
			break;
			
		case R.id.previous_button:
			if (current_item>0) {
				image_view_pager.setCurrentItem(--current_item);
			}
			Log.v("previous_button", "clicked");
			break;

		default:
			break;
		}
	}
	
	int current_item = 0;
}
