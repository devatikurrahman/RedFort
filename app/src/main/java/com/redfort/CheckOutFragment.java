package com.redfort;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CheckOutFragment extends Fragment implements OnClickListener {

	private View rootView;
	public static final String FRAGMENT_TAG = "TAG_CheckOutFragment";
	
	SharedPreferences sPref;
	Button order_button;
	
	RelativeLayout country_layout;
	TextView country;
	
	EditText first_name, last_name, email_address, contact_no,
		password, confirm_password, 
		address_1, address_2,
		city, county, post_code;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState == null){
			rootView = inflater.inflate(R.layout.checkout_fragment, container, false);
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
		MainActivity.top_bar_title.setText("Checkout");
		MainActivity.slide_menu_button.setBackgroundResource(R.drawable.top_back_button);
		sPref = getActivity().getSharedPreferences("RedFortPrefs", Context.MODE_PRIVATE);
		
		first_name = (EditText)rootView.findViewById(R.id.first_name);
		last_name = (EditText)rootView.findViewById(R.id.last_name);
		email_address = (EditText)rootView.findViewById(R.id.email_address);
		contact_no = (EditText)rootView.findViewById(R.id.contact_no);
		password = (EditText)rootView.findViewById(R.id.password);
		confirm_password = (EditText)rootView.findViewById(R.id.confirm_password);
		
		address_1 = (EditText)rootView.findViewById(R.id.address_1);
		address_2 = (EditText)rootView.findViewById(R.id.address_2);
		city = (EditText)rootView.findViewById(R.id.city);
		county = (EditText)rootView.findViewById(R.id.county);
		post_code = (EditText)rootView.findViewById(R.id.post_code);
		
		country = (TextView) rootView.findViewById(R.id.country);
		country_layout = (RelativeLayout) rootView.findViewById(R.id.country_layout);
		country_layout.setOnClickListener(this);
		
		order_button = (Button) rootView.findViewById(R.id.order_button);
		order_button.setOnClickListener(this);
		
		first_name.setText(sPref.getString("first_name", ""));
		last_name.setText(sPref.getString("last_name", ""));
		email_address.setText(sPref.getString("email_address", ""));
		contact_no.setText(sPref.getString("contact_no", ""));
		address_1.setText(sPref.getString("address_1", ""));
		address_2.setText(sPref.getString("address_2", ""));
		city.setText(sPref.getString("city", ""));
		county.setText(sPref.getString("county", ""));
		post_code.setText(sPref.getString("post_code", ""));
		country.setText(sPref.getString("country", ""));
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.country_layout:
			ShowDialogCountrySelection();
			break;
			
		case R.id.order_button:
			if (first_name.getText().toString().length() == 0 
					|| last_name.getText().toString().length() == 0 
					|| email_address.getText().toString().length() == 0 
					|| contact_no.getText().toString().length() < 11 
					|| password.getText().toString().length() == 0 
					|| confirm_password.getText().toString().length() == 0
					|| address_1.getText().toString().length() == 0 
					|| city.getText().toString().length() == 0 
					|| post_code.getText().toString().length() == 0) {
				
				Toast.makeText(getActivity(), "Please fill up the area!", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		default:
			break;
		}
	}
	
	Dialog dialog_country_selection;
	String [] Country_array = {"United Kingdom", "Brazil", "USA"};
	
	public void ShowDialogCountrySelection(){
		dialog_country_selection = new Dialog(getActivity());
		dialog_country_selection.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_country_selection.setContentView(R.layout.dialog_person_selection);
		dialog_country_selection.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dialog_country_selection.getWindow().setGravity( Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		
		dialog_country_selection.show();
		
		ListView list_person_selection = (ListView) dialog_country_selection.findViewById(R.id.list_person_selection);
		
        ArrayAdapter<String> person_adapter = new ArrayAdapter<String>(
        		getActivity(), R.layout.spinner_text_view, Country_array);
        
        list_person_selection.setAdapter(person_adapter);
        list_person_selection.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				country.setText(Country_array[position]);
				dialog_country_selection.dismiss();
			}
		});
	}
}
