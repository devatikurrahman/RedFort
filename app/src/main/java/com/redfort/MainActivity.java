package com.redfort;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	public static ServerAPIFile serverApiFile = new ServerAPIFile();
	public static Button slide_menu_button, top_cart_button;
	RelativeLayout top, left, fragment_lay;
	public static RelativeLayout loading_lay;
	
	public static ArrayList<CartClass> cart_array_list = new ArrayList<CartClass>();
	
	Animation top_view_left2right_animation, top_view_right2left_animation;
	Animation left_view_right2left_animation;

	public static TextView top_bar_title;
	SharedPreferences sPref;
	
	View transparent_view;
	LinearLayout left_menu_lay, home_lay, menu_lay, special_offers_lay, 
		reservation_lay, gallery_lay, review_lay, contact_us_lay, cart_lay;
	
	public static FragmentManager fragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Initialize();
	}
	
	public void Initialize() {
		sPref = getSharedPreferences("RedFortPrefs", Context.MODE_PRIVATE);
		top_bar_title = (TextView) findViewById(R.id.top_bar_title);
		
		loading_lay = (RelativeLayout) findViewById(R.id.loading_lay);
		loading_lay.setOnClickListener(null);
		//loading_lay.setVisibility(View.VISIBLE);
		
		cart_array_list.clear();
		
		top = (RelativeLayout) findViewById(R.id.top);
		top.setOnClickListener(null);
		left = (RelativeLayout) findViewById(R.id.left);
		left.setOnClickListener(null);
		fragment_lay = (RelativeLayout) findViewById(R.id.fragment_lay);
		left.setOnClickListener(null);

		home_lay = (LinearLayout) findViewById(R.id.home_lay);
		menu_lay = (LinearLayout) findViewById(R.id.menu_lay);
		special_offers_lay = (LinearLayout) findViewById(R.id.special_offers_lay);
		reservation_lay = (LinearLayout) findViewById(R.id.reservation_lay);
		gallery_lay = (LinearLayout) findViewById(R.id.gallery_lay);
		review_lay = (LinearLayout) findViewById(R.id.review_lay);
		contact_us_lay = (LinearLayout) findViewById(R.id.contact_us_lay);
		cart_lay = (LinearLayout) findViewById(R.id.cart_lay);

		home_lay.setOnClickListener(this);
		menu_lay.setOnClickListener(this);
		special_offers_lay.setOnClickListener(this);
		reservation_lay.setOnClickListener(this);
		gallery_lay.setOnClickListener(this);
		review_lay.setOnClickListener(this);
		contact_us_lay.setOnClickListener(this);
		cart_lay.setOnClickListener(this);
		
		home_lay.setSelected(true);
		
		left_menu_lay = (LinearLayout) findViewById(R.id.left_menu_lay);
		left_menu_lay.setOnClickListener(null);
		
		transparent_view = (View) findViewById(R.id.transparent_view);
		transparent_view.setOnClickListener(this);
		
		slide_menu_button = (Button) findViewById(R.id.slide_menu_button);
		slide_menu_button.setOnClickListener(this);
		
		top_cart_button = (Button) findViewById(R.id.top_cart_button);
		top_cart_button.setOnClickListener(this);

		left_view_right2left_animation = AnimationUtils.loadAnimation(this, R.anim.left_view_right2left_animation);
		left_view_right2left_animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				top.bringToFront();
				loading_lay.bringToFront();
			}
		});
		
		top_view_right2left_animation = AnimationUtils.loadAnimation(this, R.anim.top_view_right2left_animation);
		top_view_right2left_animation.setFillAfter(true);
		top_view_right2left_animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				transparent_view.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				loading_lay.bringToFront();
			}
		});
		
		top_view_left2right_animation = AnimationUtils.loadAnimation(this, R.anim.top_view_left2right_animation);
		top_view_left2right_animation.setFillAfter(true);
		top_view_left2right_animation.setFillEnabled(true);
		top_view_left2right_animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				left.bringToFront();
				transparent_view.bringToFront();
				transparent_view.setVisibility(View.VISIBLE);
			}
		});
		
		fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.fragment_lay, new HomeFragment(),
				HomeFragment.FRAGMENT_TAG).addToBackStack(null).commit();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.slide_menu_button:
			ItemFragment mItemFragment = (ItemFragment)fragmentManager.findFragmentByTag(ItemFragment.FRAGMENT_TAG);
			CheckOutFragment mCheckOutFragment = (CheckOutFragment)fragmentManager.findFragmentByTag(CheckOutFragment.FRAGMENT_TAG);
			if ((mItemFragment != null && mItemFragment.isVisible())
					||(mCheckOutFragment != null && mCheckOutFragment.isVisible())){
				super.onBackPressed();
			} else {
				top.startAnimation(top_view_left2right_animation);
			}
			break;
			
		case R.id.top_cart_button:
			if (!cart_lay.isSelected()) {
				home_lay.setSelected(false);
				
				menu_lay.setSelected(false);
				special_offers_lay.setSelected(false);
				
				reservation_lay.setSelected(false);
				gallery_lay.setSelected(false);
				
				contact_us_lay.setSelected(false);
				cart_lay.setSelected(true);
				
				fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.fragment_lay, new CartFragment(),
						CartFragment.FRAGMENT_TAG).addToBackStack(null).commit();
			}
			break;
			
		case R.id.transparent_view:
			top.bringToFront();
			top.startAnimation(top_view_right2left_animation);
			//left.startAnimation(left_view_right2left_animation);
			break;
			
		case R.id.home_lay:
			if (!home_lay.isSelected()) {
				home_lay.setSelected(true);
				
				menu_lay.setSelected(false);
				special_offers_lay.setSelected(false);
				
				reservation_lay.setSelected(false);
				gallery_lay.setSelected(false);
				review_lay.setSelected(false);
				
				contact_us_lay.setSelected(false);
				cart_lay.setSelected(false);
				
				transparent_view.performClick();
				fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.fragment_lay, new HomeFragment(),
						HomeFragment.FRAGMENT_TAG).addToBackStack(null).commit();
			} else {
				transparent_view.performClick();
			}
			break;
			
		case R.id.menu_lay:
			if (!menu_lay.isSelected()) {
				home_lay.setSelected(false);
				
				menu_lay.setSelected(true);
				special_offers_lay.setSelected(false);
				
				reservation_lay.setSelected(false);
				gallery_lay.setSelected(false);
				review_lay.setSelected(false);
				
				contact_us_lay.setSelected(false);
				cart_lay.setSelected(false);
				
				transparent_view.performClick();
				
				fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.fragment_lay, new MenuFragment(),
						MenuFragment.FRAGMENT_TAG).addToBackStack(null).commit();
			} else {
				transparent_view.performClick();
			}
			break;
			
		case R.id.special_offers_lay:
			if (!special_offers_lay.isSelected()) {
				home_lay.setSelected(false);
				
				menu_lay.setSelected(false);
				special_offers_lay.setSelected(true);
				
				reservation_lay.setSelected(false);
				gallery_lay.setSelected(false);
				review_lay.setSelected(false);
				
				contact_us_lay.setSelected(false);
				cart_lay.setSelected(false);
				
				transparent_view.performClick();
				
				fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.fragment_lay, new SpecialOfferFragment(),
						SpecialOfferFragment.FRAGMENT_TAG).addToBackStack(null).commit();
			} else {
				transparent_view.performClick();
			}
			break;
			
		case R.id.reservation_lay:
			if (sPref.getBoolean("is_login", false)) {
				if (!reservation_lay.isSelected()) {
					home_lay.setSelected(false);
					
					menu_lay.setSelected(false);
					special_offers_lay.setSelected(false);
					
					reservation_lay.setSelected(true);
					gallery_lay.setSelected(false);
					review_lay.setSelected(false);
					
					contact_us_lay.setSelected(false);
					cart_lay.setSelected(false);
					
					transparent_view.performClick();
					
					fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.fragment_lay, new ReservationFragment(),
							ReservationFragment.FRAGMENT_TAG).addToBackStack(null).commit();
				} else {
					transparent_view.performClick();
				}
				
			} else {
				ShowDialogSignIn();
			}
			break;
			
		case R.id.gallery_lay:
			if (!gallery_lay.isSelected()) {
				home_lay.setSelected(false);
				
				menu_lay.setSelected(false);
				special_offers_lay.setSelected(false);
				
				reservation_lay.setSelected(false);
				gallery_lay.setSelected(true);
				review_lay.setSelected(false);
				
				contact_us_lay.setSelected(false);
				cart_lay.setSelected(false);
				
				transparent_view.performClick();
				
				fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.fragment_lay, new GalleryFragment(),
						GalleryFragment.FRAGMENT_TAG).addToBackStack(null).commit();
			} else {
				transparent_view.performClick();
			}
			break;
			
		case R.id.review_lay:
			if (!review_lay.isSelected()) {
				home_lay.setSelected(false);
				
				menu_lay.setSelected(false);
				special_offers_lay.setSelected(false);
				
				reservation_lay.setSelected(false);
				gallery_lay.setSelected(false);
				review_lay.setSelected(true);
				
				contact_us_lay.setSelected(false);
				cart_lay.setSelected(false);
				
				transparent_view.performClick();
				
				fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.fragment_lay, new ReviewFragment(),
						ReviewFragment.FRAGMENT_TAG).addToBackStack(null).commit();
			} else {
				transparent_view.performClick();
			}
			break;
			
		case R.id.contact_us_lay:
			if (!contact_us_lay.isSelected()) {
				home_lay.setSelected(false);
				
				menu_lay.setSelected(false);
				special_offers_lay.setSelected(false);
				
				reservation_lay.setSelected(false);
				gallery_lay.setSelected(false);
				review_lay.setSelected(false);
				
				contact_us_lay.setSelected(true);
				cart_lay.setSelected(false);
				
				transparent_view.performClick();
				
				fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.fragment_lay, new ContactUsFragment(),
						ContactUsFragment.FRAGMENT_TAG).addToBackStack(null).commit();
			} else {
				transparent_view.performClick();
			}
			break;
			
		case R.id.cart_lay:
			if (!cart_lay.isSelected()) {
				home_lay.setSelected(false);
				
				menu_lay.setSelected(false);
				special_offers_lay.setSelected(false);
				
				reservation_lay.setSelected(false);
				gallery_lay.setSelected(false);
				review_lay.setSelected(false);
				
				contact_us_lay.setSelected(false);
				cart_lay.setSelected(true);
				
				transparent_view.performClick();
				
				fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.fragment_lay, new CartFragment(),
						CartFragment.FRAGMENT_TAG).addToBackStack(null).commit();
			} else {
				transparent_view.performClick();
			}
			break;
			
		default:
			break;
		}
	}

	MenuFragment new_ContactListFragment;
	SpecialOfferFragment new_LocationFragment;
	HomeFragment new_MyProfileFragment;

	@Override
	public void onBackPressed() {
		ItemFragment mItemFragment = (ItemFragment)fragmentManager.findFragmentByTag(ItemFragment.FRAGMENT_TAG);
		CheckOutFragment mCheckOutFragment = (CheckOutFragment)fragmentManager.findFragmentByTag(CheckOutFragment.FRAGMENT_TAG);
		if ((mItemFragment != null && mItemFragment.isVisible())
				||(mCheckOutFragment != null && mCheckOutFragment.isVisible())){
			super.onBackPressed();
		} else {
			finish();
		}
	}

	
	Dialog dialog_sign_in;
	
	public void ShowDialogSignIn(){
		dialog_sign_in = new Dialog(this);
		dialog_sign_in.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_sign_in.setContentView(R.layout.dialog_sign_in_2);
		dialog_sign_in.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dialog_sign_in.getWindow().setGravity( Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		
		dialog_sign_in.show();
		
		final EditText email_address, password;
		Button close_button, signup_button, submit_button;
		TextView forget_pass;
		forget_pass = (TextView)dialog_sign_in.findViewById(R.id.forget_pass);
		email_address = (EditText)dialog_sign_in.findViewById(R.id.email_address);
		password = (EditText)dialog_sign_in.findViewById(R.id.password);
		
		close_button = (Button)dialog_sign_in.findViewById(R.id.close_button);
		signup_button = (Button)dialog_sign_in.findViewById(R.id.signup_button);
		submit_button = (Button)dialog_sign_in.findViewById(R.id.submit_button);
		
		forget_pass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog_sign_in.dismiss();
			}
		});
		close_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog_sign_in.dismiss();
			}
		});
		signup_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog_sign_in.dismiss();
				ShowDialogSignUp();
			}
		});
		submit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (email_address.getText().toString().length() == 0 
						|| password.getText().toString().length() == 0 ) {
					
					Toast.makeText(MainActivity.this, "Please fill up the area!", Toast.LENGTH_SHORT).show();
				} else {
					if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_address.getText().toString()).matches()) {
						Toast.makeText(MainActivity.this, "Please insert correct email address.",
								Toast.LENGTH_SHORT).show();
					} else {
						HashMap<String, String> sign_in_data = new HashMap<String, String>();

						sign_in_data.put("email_address", email_address.getText().toString());
						sign_in_data.put("password", password.getText().toString());
						
						new SignInAsyncTask(MainActivity.this, sign_in_data).execute();
					}
				}
			}
		});
	}
	
	Dialog dialog_sign_up;
	
	public void ShowDialogSignUp(){
		dialog_sign_up = new Dialog(this);
		dialog_sign_up.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_sign_up.setContentView(R.layout.dialog_sign_up_2);
		dialog_sign_up.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dialog_sign_up.getWindow().setGravity( Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		
		dialog_sign_up.show();
		
		RelativeLayout country_layout;
		final TextView country;
		TextView term_and_condition_txt;
		
		final EditText first_name, last_name, email_address, contact_no,
			password, confirm_password, 
			address_1, address_2,
			city, county, post_code;
		
		final Button term_checkbox;
		Button close_button, clear_button, submit_button;
		TextView alrdy_hv_acnt;

		alrdy_hv_acnt = (TextView)dialog_sign_up.findViewById(R.id.alrdy_hv_acnt);
		country_layout = (RelativeLayout)dialog_sign_up.findViewById(R.id.country_layout);
		country = (TextView)dialog_sign_up.findViewById(R.id.country);
		
		term_and_condition_txt = (TextView)dialog_sign_up.findViewById(R.id.term_and_condition_txt);
		term_and_condition_txt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent termIntent = new Intent(MainActivity.this, TearmAndCondition.class);
				startActivity(termIntent);*/
			}
		});
		
		first_name = (EditText)dialog_sign_up.findViewById(R.id.first_name);
		last_name = (EditText)dialog_sign_up.findViewById(R.id.last_name);
		email_address = (EditText)dialog_sign_up.findViewById(R.id.email_address);
		contact_no = (EditText)dialog_sign_up.findViewById(R.id.contact_no);
		password = (EditText)dialog_sign_up.findViewById(R.id.password);
		confirm_password = (EditText)dialog_sign_up.findViewById(R.id.confirm_password);
		
		address_1 = (EditText)dialog_sign_up.findViewById(R.id.address_1);
		address_2 = (EditText)dialog_sign_up.findViewById(R.id.address_2);
		city = (EditText)dialog_sign_up.findViewById(R.id.city);
		county = (EditText)dialog_sign_up.findViewById(R.id.county);
		post_code = (EditText)dialog_sign_up.findViewById(R.id.post_code);
		
		close_button = (Button)dialog_sign_up.findViewById(R.id.close_button);
		clear_button = (Button)dialog_sign_up.findViewById(R.id.clear_button);
		submit_button = (Button)dialog_sign_up.findViewById(R.id.submit_button);
		term_checkbox = (Button)dialog_sign_up.findViewById(R.id.term_checkbox);
		
		term_checkbox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(term_checkbox.isSelected()){
					term_checkbox.setSelected(false);
				} else {
					term_checkbox.setSelected(true);
				}
			}
		});
		alrdy_hv_acnt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog_sign_up.dismiss();
			}
		});
		close_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog_sign_up.dismiss();
			}
		});
		
		country_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ShowDialogCountrySelection(country);
			}
		});
		
		clear_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				first_name.setText("");
				last_name.setText("");
				email_address.setText("");
				contact_no.setText("");
				password.setText("");
				confirm_password.setText("");
				address_1.setText("");
				address_2.setText("");
				city.setText("");
				county.setText("");
				post_code.setText("");
				term_checkbox.setSelected(false);
			}
		});
		
		submit_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (first_name.getText().toString().length() == 0 
						|| last_name.getText().toString().length() == 0 
						|| email_address.getText().toString().length() == 0 
						|| contact_no.getText().toString().length() < 0 
						|| password.getText().toString().length() == 0 
						|| confirm_password.getText().toString().length() == 0
						|| address_1.getText().toString().length() == 0 
						|| city.getText().toString().length() == 0 
						|| post_code.getText().toString().length() == 0
						|| !term_checkbox.isSelected()) {
					
					Toast.makeText(MainActivity.this, "Please fill up the area!", Toast.LENGTH_SHORT).show();
				} else {
					if (contact_no.getText().toString().length() < 11 && !contact_no.getText().toString().startsWith("0")) {
						Toast.makeText(MainActivity.this, "Please enter 11 digit phone number and start with 0.",
								Toast.LENGTH_SHORT).show();
					} else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_address.getText().toString()).matches()) {
						Toast.makeText(MainActivity.this, "Please insert correct email address.",
								Toast.LENGTH_SHORT).show();
					} else if (!password.getText().toString().equals(confirm_password.getText().toString())) {
						Toast.makeText(MainActivity.this, "Password do not match.",
								Toast.LENGTH_SHORT).show();
					} else {
						HashMap<String, String> sign_up_data = new HashMap<String, String>();

						sign_up_data.put("first_name", first_name.getText().toString());
						sign_up_data.put("last_name", last_name.getText().toString());
						sign_up_data.put("email_address", email_address.getText().toString());
						sign_up_data.put("contact_no", contact_no.getText().toString());
						sign_up_data.put("password", password.getText().toString());
						sign_up_data.put("address_1", address_1.getText().toString());
						sign_up_data.put("address_2", address_2.getText().toString());
						sign_up_data.put("city", city.getText().toString());
						sign_up_data.put("county", county.getText().toString());
						sign_up_data.put("post_code", post_code.getText().toString());
						sign_up_data.put("country", country.getText().toString());
						sign_up_data.put("term_checkbox", "agreed");
						
						new SignUpAsyncTask(MainActivity.this, sign_up_data).execute();
					}
				}
			}
		});
	}
	
	Dialog dialog_country_selection;
	String [] Country_array = {"United Kingdom", "Brazil", "USA"};
	
	public void ShowDialogCountrySelection(final TextView selection_country){
		dialog_country_selection = new Dialog(this);
		dialog_country_selection.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_country_selection.setContentView(R.layout.dialog_person_selection);
		dialog_country_selection.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dialog_country_selection.getWindow().setGravity( Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		
		dialog_country_selection.show();
		
		ListView list_person_selection = (ListView) dialog_country_selection.findViewById(R.id.list_person_selection);
		
        ArrayAdapter<String> person_adapter = new ArrayAdapter<String>(
                MainActivity.this, R.layout.spinner_text_view, Country_array);
        
        list_person_selection.setAdapter(person_adapter);
        list_person_selection.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				selection_country.setText(Country_array[position]);
				dialog_country_selection.dismiss();
			}
		});
	}
	
	public class SignUpAsyncTask extends AsyncTask<String, String, String> {
		
		Context context;
		HashMap<String, String> sign_up_data;
		
		public SignUpAsyncTask(Context context, HashMap<String, String> sign_up_data) {
			this.context = context;
			this.sign_up_data = sign_up_data;
		}
		
		@Override
		protected void onPreExecute() {
			MainActivity.loading_lay.bringToFront();
			MainActivity.loading_lay.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			String response = null;
			
			try {
				DefaultHttpClient hc = new DefaultHttpClient();
				ResponseHandler<String> res = new BasicResponseHandler();
				HttpPost postMethod = new HttpPost("signup");
				
				MultipartEntity entity = new MultipartEntity();
				
				entity.addPart("fname", 
						new org.apache.http.entity.mime.content.StringBody(sign_up_data.get("first_name")));

				entity.addPart("lname", 
						new org.apache.http.entity.mime.content.StringBody(sign_up_data.get("last_name")));

				entity.addPart("email", 
						new org.apache.http.entity.mime.content.StringBody(sign_up_data.get("email_address")));

				entity.addPart("contact_number", 
						new org.apache.http.entity.mime.content.StringBody(sign_up_data.get("contact_no")));
				
				entity.addPart("password", 
						new org.apache.http.entity.mime.content.StringBody(sign_up_data.get("password")));

				entity.addPart("b_address1", 
						new org.apache.http.entity.mime.content.StringBody(sign_up_data.get("address_1")));

				entity.addPart("b_address2", 
						new org.apache.http.entity.mime.content.StringBody(sign_up_data.get("address_2")));

				entity.addPart("b_state", 
						new org.apache.http.entity.mime.content.StringBody(sign_up_data.get("county")));

				entity.addPart("b_city", 
						new org.apache.http.entity.mime.content.StringBody(sign_up_data.get("city")));

				entity.addPart("b_postcode", 
						new org.apache.http.entity.mime.content.StringBody(sign_up_data.get("post_code")));

				entity.addPart("b_country", 
						new org.apache.http.entity.mime.content.StringBody(sign_up_data.get("country")));

				entity.addPart("terms_agreement", 
						new org.apache.http.entity.mime.content.StringBody(sign_up_data.get("term_checkbox")));
				
				postMethod.setEntity(entity);
				Log.v("post", "submitted");
				
				response = hc.execute(postMethod, res);
				
			} catch(Exception e){
				e.printStackTrace();
			}
			
			Log.v("response", "response = "+response);
			
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			MainActivity.loading_lay.setVisibility(View.INVISIBLE);
			dialog_sign_up.dismiss();
			if (result == null) {
				jsonParseWithUrlString(result);
			} else {
				ShowAlert("Fail");
			}
			super.onPostExecute(result);
		}
	}
	
	public class SignInAsyncTask extends AsyncTask<String, String, String> {
		
		Context context;
		HashMap<String, String> sign_in_data;
		
		public SignInAsyncTask(Context context, HashMap<String, String> sign_in_data) {
			this.context = context;
			this.sign_in_data = sign_in_data;
		}
		
		@Override
		protected void onPreExecute() {
			MainActivity.loading_lay.bringToFront();
			MainActivity.loading_lay.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			String response = null;
			
			try {
				DefaultHttpClient hc = new DefaultHttpClient();
				ResponseHandler<String> res = new BasicResponseHandler();
				HttpPost postMethod = new HttpPost("signin");
				
				MultipartEntity entity = new MultipartEntity();
				
				entity.addPart("email", 
						new org.apache.http.entity.mime.content.StringBody(sign_in_data.get("email_address")));
				
				entity.addPart("password", 
						new org.apache.http.entity.mime.content.StringBody(sign_in_data.get("password")));
				
				postMethod.setEntity(entity);
				Log.v("post", "submitted");
				
				response = hc.execute(postMethod, res);
				
			} catch(Exception e){
				e.printStackTrace();
			}
			
			Log.v("response", "response = "+response);
			
			return response;
		}
		
		@Override
		protected void onPostExecute(String result) {
			MainActivity.loading_lay.setVisibility(View.INVISIBLE);
			dialog_sign_in.dismiss();
			if (result == null) {
				jsonParseWithUrlString(result);
			} else {
				ShowAlert("Fail");
			}
			super.onPostExecute(result);
		}
	}
	
	public void jsonParseWithUrlString(String result) {
		
		try {
			JSONObject jObj = new JSONObject(result);
			String status = jObj.getString("status");
			
			if (status.equals("1")) {

				sPref.edit().putBoolean("is_login", true).commit();
				
				JSONObject subjObj = new JSONObject(jObj.getString("user_info"));
				
				sPref.edit().putString("first_name", subjObj.getString("first_name")).commit();
				sPref.edit().putString("last_name", subjObj.getString("last_name")).commit();
				sPref.edit().putString("email_address", subjObj.getString("email_address")).commit();
				sPref.edit().putString("contact_no", subjObj.getString("contact_no")).commit();
				sPref.edit().putString("address_1", subjObj.getString("address_1")).commit();
				sPref.edit().putString("address_2", subjObj.getString("address_2")).commit();
				sPref.edit().putString("city", subjObj.getString("city")).commit();
				sPref.edit().putString("county", subjObj.getString("county")).commit();
				sPref.edit().putString("post_code", subjObj.getString("post_code")).commit();
				sPref.edit().putString("country", subjObj.getString("country")).commit();
				sPref.edit().putString("term_checkbox", subjObj.getString("term_checkbox")).commit();
				
				transparent_view.performClick();
				
				fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.fragment_lay, new ReservationFragment(),
						ReservationFragment.FRAGMENT_TAG).addToBackStack(null).commit();
				
			} else {
				Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
				ShowAlert("Fail");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void ShowAlert(String message){
		AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
		alertbox.setTitle("Alert!");
		alertbox.setMessage(message);
		alertbox.setNeutralButton("OK",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						
					}

				});
		AlertDialog alertDialog = alertbox.create();
		alertDialog.show();
	}
}
