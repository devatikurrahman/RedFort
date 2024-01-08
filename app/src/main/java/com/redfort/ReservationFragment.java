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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class ReservationFragment extends Fragment implements OnClickListener {

	private View rootView;
	public static final String FRAGMENT_TAG = "TAG_ReservationFragment";
	
	SharedPreferences sPref;
	Button reserved_button;
	TextView number_of_person, time, date;
	RelativeLayout number_person_layout, time_lay, date_lay;
	
	EditText first_name, last_name, email, phone_number, message_ed;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState == null){
			rootView = inflater.inflate(R.layout.reservation_fragment, container, false);
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
		MainActivity.top_bar_title.setText("Reservation");
		MainActivity.slide_menu_button.setBackgroundResource(R.drawable.top_left_slide_button);
		sPref = getActivity().getSharedPreferences("RedFortPrefs", Context.MODE_PRIVATE);

		number_of_person =  (TextView)rootView.findViewById(R.id.number_of_person);
		time =  (TextView)rootView.findViewById(R.id.time);
		date =  (TextView)rootView.findViewById(R.id.date);

		number_person_layout = (RelativeLayout)rootView.findViewById(R.id.number_person_layout);
		time_lay = (RelativeLayout)rootView.findViewById(R.id.time_lay);
		date_lay = (RelativeLayout)rootView.findViewById(R.id.date_lay);

		number_person_layout.setOnClickListener(this);
		time_lay.setOnClickListener(this);
		date_lay.setOnClickListener(this);

		first_name = (EditText)rootView.findViewById(R.id.first_name);
		last_name = (EditText)rootView.findViewById(R.id.last_name);
		email = (EditText)rootView.findViewById(R.id.email);
		phone_number = (EditText)rootView.findViewById(R.id.phone_number);
		message_ed = (EditText)rootView.findViewById(R.id.message_ed);

		reserved_button = (Button)rootView.findViewById(R.id.reserved_button);
		reserved_button.setOnClickListener(this);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.number_person_layout:
			ShowDialogPersonSelection();
			break ;

		case R.id.time_lay:
			ShowDialogTimeSelection();
			break ;

		case R.id.date_lay:
			ShowDialogDateSelection();
			break ;

		case R.id.reserved_button:
			ShowDialogPersonSelection();
			break ;
			
		default:
			break;
		}
		
	}
	
	String [] persons_array = {
			"1 person", "2 person", "3 person", "4 person", "5 person", "6 person", "7 person",
			"8 person", "9 person", "10 person", "11 person", "12 person", "13 person", "14 person",
			"15 person", "16 person", "17 person", "18 person", "19 person", "20 person", "20+ person"
	};

	Dialog dialog_person_selection;
	
	public void ShowDialogPersonSelection(){
		dialog_person_selection = new Dialog(getActivity());
		dialog_person_selection.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_person_selection.setContentView(R.layout.dialog_person_selection);
		dialog_person_selection.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dialog_person_selection.getWindow().setGravity( Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		
		dialog_person_selection.show();
		
		ListView list_person_selection = (ListView) dialog_person_selection.findViewById(R.id.list_person_selection);
		
        ArrayAdapter<String> person_adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_text_view, persons_array);
        list_person_selection.setAdapter(person_adapter);
        
        list_person_selection.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				number_of_person.setText(persons_array[position]);
				dialog_person_selection.dismiss();
			}
		});
	}

	Dialog dialog_date_selection;
	
	public void ShowDialogDateSelection(){
		dialog_date_selection = new Dialog(getActivity());
		dialog_date_selection.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_date_selection.setContentView(R.layout.dialog_date_selection);
		dialog_date_selection.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dialog_date_selection.getWindow().setGravity( Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		
		dialog_date_selection.show();
		
		final android.widget.DatePicker datePicker = 
				(android.widget.DatePicker) dialog_date_selection.findViewById(R.id.datePicker);
		
		Button ok_button, cancel_button;
		
		ok_button = (Button) dialog_date_selection.findViewById(R.id.ok_button);
		cancel_button = (Button) dialog_date_selection.findViewById(R.id.cancel_button);

		ok_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog_date_selection.dismiss();
				String yourDate = String.format("%d", datePicker.getMonth()+1) + "-"
						+ String.format("%d", datePicker.getDayOfMonth()) + "-"
						+ String.format("%d", datePicker.getYear());
				
				date.setText(yourDate);
			}
		});
		cancel_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog_date_selection.dismiss();
			}
		});
	}

	Dialog dialog_time_selection;
	
	public void ShowDialogTimeSelection(){
		dialog_time_selection = new Dialog(getActivity());
		dialog_time_selection.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_time_selection.setContentView(R.layout.dialog_time_selection);
		dialog_time_selection.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dialog_time_selection.getWindow().setGravity( Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		
		dialog_time_selection.show();
		Button ok_button, cancel_button;
		ok_button = (Button) dialog_time_selection.findViewById(R.id.ok_button);
		cancel_button = (Button) dialog_time_selection.findViewById(R.id.cancel_button);
		
		final TimePicker timePicker = (TimePicker) dialog_time_selection.findViewById(R.id.datePicker);

		ok_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog_time_selection.dismiss();
				String yourTime = String.format("%d", timePicker.getCurrentHour()) + "-"
						+ String.format("%d", timePicker.getCurrentMinute());
				
				time.setText(yourTime);
			}
		});
		cancel_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog_time_selection.dismiss();
			}
		});
	}
	
}
