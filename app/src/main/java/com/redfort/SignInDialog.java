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
import android.widget.TextView;
import android.widget.Toast;

public class SignInDialog extends DialogFragment implements OnClickListener {
	
	SharedPreferences sPref;
	Context context;
	View rootView;
	EditText email_address, password;
	Button close_button, signup_button, submit_button;
	TextView forget_pass;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = (Context) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState == null)
			rootView = inflater.inflate(R.layout.dialog_sign_in, container, true);
		
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
		forget_pass = (TextView)rootView.findViewById(R.id.forget_pass);
		email_address = (EditText)rootView.findViewById(R.id.email_address);
		password = (EditText)rootView.findViewById(R.id.password);
		
		close_button = (Button)rootView.findViewById(R.id.close_button);
		signup_button = (Button)rootView.findViewById(R.id.signup_button);
		submit_button = (Button)rootView.findViewById(R.id.submit_button);
		
		forget_pass.setOnClickListener(this);
		close_button.setOnClickListener(this);
		signup_button.setOnClickListener(this);
		submit_button.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.close_button:
			dismiss();
			
			break;

		case R.id.signup_button:
			dismiss();
			SignUpDialog d = new SignUpDialog();
			MainActivity.fragmentManager.beginTransaction().add(d, "SignUp").commit();
			
			break;

		case R.id.submit_button:
			if (email_address.getText().toString().length() == 0 
					|| password.getText().toString().length() == 0 ) {
				
				Toast.makeText(getActivity(), "Please fill up the area!", Toast.LENGTH_SHORT).show();
			} else {
				sPref.edit().putBoolean("is_login", true).commit();
				dismiss();
				MainActivity.fragmentManager = getFragmentManager();
				MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_lay, new ReservationFragment(),
						ReservationFragment.FRAGMENT_TAG).addToBackStack(null).commit();
			}
			break;

		case R.id.forget_pass:
			dismiss();
			break;

		default:
			break;
		}
	}
}
