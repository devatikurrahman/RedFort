package com.redfort;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpDialog extends DialogFragment implements OnClickListener {
	SharedPreferences sPref;
	Context context;
	View rootView;
	EditText first_name, last_name, email_address,phone_number,password,confirm_password;
	Button close_button, clear_button, submit_button;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = (Context) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState == null)
			rootView = inflater.inflate(R.layout.dialog_sign_up, container, true);

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView();
	}

	private void initView() {
		sPref = getActivity().getSharedPreferences("RedFortPrefs", Context.MODE_PRIVATE);
		
		first_name = (EditText)rootView.findViewById(R.id.first_name);
		last_name = (EditText)rootView.findViewById(R.id.last_name);
		email_address = (EditText)rootView.findViewById(R.id.email_address);
		phone_number = (EditText)rootView.findViewById(R.id.phone_number);
		password = (EditText)rootView.findViewById(R.id.password);
		confirm_password = (EditText)rootView.findViewById(R.id.confirm_password);
		
		close_button = (Button)rootView.findViewById(R.id.close_button);
		clear_button = (Button)rootView.findViewById(R.id.clear_button);
		submit_button = (Button)rootView.findViewById(R.id.submit_button);
		
		close_button.setOnClickListener(this);
		clear_button.setOnClickListener(this);
		submit_button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.close_button:
			dismiss();
			
			break;

		case R.id.clear_button:
			first_name.setText("");
			last_name.setText("");
			email_address.setText("");
			phone_number.setText("");
			password.setText("");
			confirm_password.setText("");
			break;

		case R.id.submit_button:
			if (first_name.getText().toString().length() == 0 
				|| last_name.getText().toString().length() == 0 
				|| email_address.getText().toString().length() == 0 
				|| phone_number.getText().toString().length() == 0 
				|| password.getText().toString().length() == 0 
				|| confirm_password.getText().toString().length() == 0) {
				
				Toast.makeText(getActivity(), "Please fill up the area!", Toast.LENGTH_SHORT).show();
			} else {
				sPref.edit().putBoolean("is_login", true).commit();
				dismiss();
				MainActivity.fragmentManager = getFragmentManager();
				MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_lay, new ReservationFragment(),
						ReservationFragment.FRAGMENT_TAG).addToBackStack(null).commit();
			}
			break;

		default:
			break;
		}
	}
}
