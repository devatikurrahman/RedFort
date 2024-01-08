package com.redfort;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CartFragment extends Fragment implements OnClickListener {

	private View rootView;
	public static final String FRAGMENT_TAG = "TAG_CartFragment";
	ListView list_cart;
	CartAdapter cartAdapter;
	Button check_out_button;
	TextView total_ammount;
	SharedPreferences sPref;
	float total_order = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState == null){
			rootView = inflater.inflate(R.layout.cart_fragment, container, false);
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
		sPref = getActivity().getSharedPreferences("RedFortPrefs", Context.MODE_PRIVATE);
		MainActivity.top_bar_title.setText("Cart");
		MainActivity.slide_menu_button.setBackgroundResource(R.drawable.top_left_slide_button);
		list_cart = (ListView) rootView.findViewById(R.id.list_cart);
		
		cartAdapter = new CartAdapter(getActivity());
		MainActivity.loading_lay.setVisibility(View.VISIBLE);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				MainActivity.loading_lay.setVisibility(View.GONE);
				list_cart.setAdapter(cartAdapter);
			}
		}, 450);
		check_out_button = (Button) rootView.findViewById(R.id.check_out_button);
		check_out_button.setOnClickListener(this);
		
		total_ammount = (TextView) rootView.findViewById(R.id.total_ammount);
		
		float sum = 0;
		for (int i = 0; i < MainActivity.cart_array_list.size(); i++) {
			if (MainActivity.cart_array_list.get(i).qty == null
					|| MainActivity.cart_array_list.get(i).qty.isEmpty()
					|| MainActivity.cart_array_list.get(i).qty.equals("")
					|| MainActivity.cart_array_list.get(i).qty.equals("0")) {

				sum += Float.parseFloat(MainActivity.cart_array_list.get(i).price);
			} else {
				float tmp_sum = Float.parseFloat((MainActivity.cart_array_list.get(i).price))
						*(Integer.parseInt(MainActivity.cart_array_list.get(i).qty));
				sum += tmp_sum;
			}
		}

		
		if (MainActivity.cart_array_list.size() <= 0) {
			check_out_button.setVisibility(View.INVISIBLE);
		} else {
			check_out_button.setVisibility(View.VISIBLE);
		}
		
		total_order = 0;
		
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		total_ammount.setText("Total "+MainActivity.cart_array_list.size()
				+" items - £" +decimalFormat.format(sum));
		total_order = sum;
		
		new DeliveryFeeAsynkTask(getActivity(), "").execute();
	}
	
	public class CartAdapter extends BaseAdapter {
		
		LayoutInflater inflater = null;
		Context context;
		
		public CartAdapter(Context context){
			this.context = context;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			return MainActivity.cart_array_list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			view = inflater.inflate(R.layout.list_cart_view, null);
			
			final int tmp_position = position;

			TextView title = (TextView) view.findViewById(R.id.title);
			final TextView individual_price = (TextView) view.findViewById(R.id.individual_price);
			final TextView count_txt = (TextView) view.findViewById(R.id.count_txt);
			Button cross_button = (Button) view.findViewById(R.id.cross_button);
			
			Button plus_button = (Button) view.findViewById(R.id.plus_button);
			Button minus_button = (Button) view.findViewById(R.id.minus_button);

			title.setText(MainActivity.cart_array_list.get(position).manu_name);
			
			if (MainActivity.cart_array_list.get(position).qty == null
					|| MainActivity.cart_array_list.get(position).qty.isEmpty()
					|| MainActivity.cart_array_list.get(position).qty.equals("")
					|| MainActivity.cart_array_list.get(position).qty.equals("0")) {
				
				count_txt.setText("1");
				individual_price.setText("( £"+MainActivity.cart_array_list.get(position).price+")");
			} else {
				count_txt.setText(MainActivity.cart_array_list.get(position).qty);
				
				int count = Integer.parseInt(count_txt.getText().toString());
				float individual_sum = Float.parseFloat(MainActivity.cart_array_list.get(position).price);
				individual_sum = individual_sum*count;
				DecimalFormat decimalFormat = new DecimalFormat("#.##");
				
				individual_price.setText("( £"+decimalFormat.format(individual_sum)+")");
			}
			
			cross_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MainActivity.cart_array_list.remove(MainActivity.cart_array_list.get(tmp_position));
					cartAdapter.notifyDataSetChanged();
					float sum = 0;
					for (int i = 0; i < MainActivity.cart_array_list.size(); i++) {
						if (MainActivity.cart_array_list.get(i).qty == null
								|| MainActivity.cart_array_list.get(i).qty.isEmpty()
								|| MainActivity.cart_array_list.get(i).qty.equals("")
								|| MainActivity.cart_array_list.get(i).qty.equals("0")) {

							sum += Float.parseFloat(MainActivity.cart_array_list.get(i).price);
						} else {
							float tmp_sum = Float.parseFloat((MainActivity.cart_array_list.get(i).price))
									*(Integer.parseInt(MainActivity.cart_array_list.get(i).qty));
							sum += tmp_sum;
						}
					}

					DecimalFormat decimalFormat = new DecimalFormat("#.##");
					total_ammount.setText("Total "+MainActivity.cart_array_list.size()
							+" items - £" +decimalFormat.format(sum));
					total_order = sum;
					
					if (MainActivity.cart_array_list.size() <= 0) {
						check_out_button.setVisibility(View.INVISIBLE);
					}
					
					MainActivity.top_cart_button.setText("("+MainActivity.cart_array_list.size()+")");
				}
			});
			
			plus_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (Integer.parseInt(count_txt.getText().toString()) <= 99) {
						int tmp_count = Integer.parseInt(MainActivity.cart_array_list.get(tmp_position).qty);
						tmp_count ++;
						MainActivity.cart_array_list.get(tmp_position).qty = ""+tmp_count;
						
						float sum = 0;
						for (int i = 0; i < MainActivity.cart_array_list.size(); i++) {
							if (MainActivity.cart_array_list.get(i).qty == null
									|| MainActivity.cart_array_list.get(i).qty.isEmpty()
									|| MainActivity.cart_array_list.get(i).qty.equals("")
									|| MainActivity.cart_array_list.get(i).qty.equals("0")) {

								sum += Float.parseFloat(MainActivity.cart_array_list.get(i).price);
							} else {
								float tmp_sum = Float.parseFloat((MainActivity.cart_array_list.get(i).price))
										*(Integer.parseInt(MainActivity.cart_array_list.get(i).qty));
								sum += tmp_sum;
							}
						}
						
						if (MainActivity.cart_array_list.get(tmp_position).qty == null
								|| MainActivity.cart_array_list.get(tmp_position).qty.isEmpty()
								|| MainActivity.cart_array_list.get(tmp_position).qty.equals("")
								|| MainActivity.cart_array_list.get(tmp_position).qty.equals("0")) {
							
							count_txt.setText("1");
							individual_price.setText("( £"+MainActivity.cart_array_list.get(tmp_position).price+")");
						} else {
							count_txt.setText(MainActivity.cart_array_list.get(tmp_position).qty);
							
							int count = Integer.parseInt(count_txt.getText().toString());
							float individual_sum = Float.parseFloat(MainActivity.cart_array_list.get(tmp_position).price);
							individual_sum = individual_sum*count;
							DecimalFormat decimalFormat = new DecimalFormat("#.##");
							
							individual_price.setText("( £"+decimalFormat.format(individual_sum)+")");
						}

						cartAdapter.notifyDataSetChanged();

						DecimalFormat decimalFormat = new DecimalFormat("#.##");
						total_ammount.setText("Total "+MainActivity.cart_array_list.size()
								+" items - £" +decimalFormat.format(sum));
						total_order = sum;
						
						MainActivity.top_cart_button.setText("("+MainActivity.cart_array_list.size()+")");
					}
				}
			});

			minus_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (Integer.parseInt(count_txt.getText().toString()) > 1) {
						int tmp_count = Integer.parseInt(MainActivity.cart_array_list.get(tmp_position).qty);
						tmp_count --;
						MainActivity.cart_array_list.get(tmp_position).qty = ""+tmp_count;
						
						float sum = 0;
						for (int i = 0; i < MainActivity.cart_array_list.size(); i++) {
							if (MainActivity.cart_array_list.get(i).qty == null
									|| MainActivity.cart_array_list.get(i).qty.isEmpty()
									|| MainActivity.cart_array_list.get(i).qty.equals("")
									|| MainActivity.cart_array_list.get(i).qty.equals("0")) {

								sum += Float.parseFloat(MainActivity.cart_array_list.get(i).price);
							} else {
								float tmp_sum = Float.parseFloat((MainActivity.cart_array_list.get(i).price))
										*(Integer.parseInt(MainActivity.cart_array_list.get(i).qty));
								sum += tmp_sum;
							}
						}
						
						if (MainActivity.cart_array_list.get(tmp_position).qty == null
								|| MainActivity.cart_array_list.get(tmp_position).qty.isEmpty()
								|| MainActivity.cart_array_list.get(tmp_position).qty.equals("")
								|| MainActivity.cart_array_list.get(tmp_position).qty.equals("0")) {
							
							count_txt.setText("1");
							individual_price.setText("( £"+MainActivity.cart_array_list.get(tmp_position).price+")");
						} else {
							count_txt.setText(MainActivity.cart_array_list.get(tmp_position).qty);
							
							int count = Integer.parseInt(count_txt.getText().toString());
							float individual_sum = Float.parseFloat(MainActivity.cart_array_list.get(tmp_position).price);
							individual_sum = individual_sum*count;
							DecimalFormat decimalFormat = new DecimalFormat("#.##");
							
							individual_price.setText("( £"+decimalFormat.format(individual_sum)+")");
						}
						
						cartAdapter.notifyDataSetChanged();
						DecimalFormat decimalFormat = new DecimalFormat("#.##");
						total_ammount.setText("Total "+MainActivity.cart_array_list.size()
								+" items - £" +decimalFormat.format(sum));
						total_order = sum;
						
						MainActivity.top_cart_button.setText("("+MainActivity.cart_array_list.size()+")");
					}
				}
			});

			return view;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.check_out_button:
			if (MainActivity.cart_array_list.size()>0) {
				ShowDialogDeliveryOption();
			}
			break;

		default:
			break;
		}
	}
	
	Dialog dialog_delivery_option;
	
	public void ShowDialogDeliveryOption(){
		dialog_delivery_option = new Dialog(getActivity());
		dialog_delivery_option.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog_delivery_option.setContentView(R.layout.dialog_delivery_option);
		dialog_delivery_option.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		dialog_delivery_option.getWindow().setGravity( Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		
		dialog_delivery_option.show();
		
		LinearLayout home_delivery_lay, collection_lay;
		final Button home_delivery_button, collection_button, ok_button, cancel_button;
		final Spinner delivery_area;
        
		home_delivery_lay = (LinearLayout) dialog_delivery_option.findViewById(R.id.home_delivery_lay);
		collection_lay = (LinearLayout) dialog_delivery_option.findViewById(R.id.collection_lay);
		home_delivery_button = (Button) dialog_delivery_option.findViewById(R.id.home_delivery_button);
		collection_button = (Button) dialog_delivery_option.findViewById(R.id.collection_button);
		ok_button = (Button) dialog_delivery_option.findViewById(R.id.ok_button);
		cancel_button = (Button) dialog_delivery_option.findViewById(R.id.cancel_button);
		
		home_delivery_button.setSelected(true);
		collection_lay.setSelected(false);
		
		delivery_area = (Spinner) dialog_delivery_option.findViewById(R.id.delivery_area);
        ArrayAdapter<String> delivery_area_adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_text_view, mDeliveryAreaNamesList);
        delivery_area.setAdapter(delivery_area_adapter);
        
        delivery_area.setVisibility(View.VISIBLE);
		
		home_delivery_lay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				home_delivery_button.performClick();
			}
		});
		
		collection_lay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				collection_button.performClick();
			}
		});
		
		home_delivery_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (home_delivery_button.isSelected()) {
					home_delivery_button.setSelected(false);
			        delivery_area.setVisibility(View.GONE);
					collection_button.setSelected(true);
				} else {
					home_delivery_button.setSelected(true);
			        delivery_area.setVisibility(View.VISIBLE);
					collection_button.setSelected(false);
				}
			}
		});
		
		collection_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (home_delivery_button.isSelected()) {
					home_delivery_button.setSelected(false);
			        delivery_area.setVisibility(View.GONE);
					collection_button.setSelected(true);
				} else {
					home_delivery_button.setSelected(true);
			        delivery_area.setVisibility(View.VISIBLE);
					collection_button.setSelected(false);
				}
			}
		});
		
		ok_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog_delivery_option.dismiss();
				if (home_delivery_button.isSelected() && !collection_button.isSelected()) {
					float min_order = Float.parseFloat(mDeliveryClassesArrayList.get(delivery_area.getSelectedItemPosition()).minorder);
					Log.v("min_order", "min_order = "+min_order);
					if (total_order < min_order) {
						ShowAlert("Delivery amount must be greater then "+min_order);
					} else {
						if (sPref.getBoolean("is_login", false)) {
							getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_lay, new CheckOutFragment(),
									CheckOutFragment.FRAGMENT_TAG).addToBackStack(null).commit();
						} else {
							ShowDialogSignIn();
						}
					}
				}
			}
		});
		
		cancel_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog_delivery_option.dismiss();
			}
		});
	}
	
	public void ShowAlert(String message){
		AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());
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

	public ArrayList<DeliveryClass> mDeliveryClassesArrayList = new ArrayList<DeliveryClass>();
	public ArrayList<String> mDeliveryAreaNamesList = new ArrayList<String>();
	
	public class DeliveryFeeAsynkTask extends AsyncTask<String, String, String> {

		Context context;
		String url;
		
		public DeliveryFeeAsynkTask (Context context, String url) {
			this.context = context;
			this.url = url;
			
			Log.v("delivery url: ", ""+url);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			String responseString = null;
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			try {
				response = httpclient.execute(new HttpGet(url));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			} catch (Exception e) {
				//e.printStackTrace();
			}
			return responseString;
		}
		
		@Override
		protected void onPostExecute(String result) {
			Log.v("result", "result : "+result);
			
			try {
			    InputStream json = context.getAssets().open("deliveryfee.txt");
			    BufferedReader in = new BufferedReader(new InputStreamReader(json));
			    
			    String str = in.readLine();
			    in.close();

			    mDeliveryClassesArrayList.clear();
			    mDeliveryAreaNamesList.clear();
			    
			    jsonParseWithUrlString(str);
			    
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//jsonParseWithUrlString(result);
			MainActivity.loading_lay.setVisibility(View.GONE);
			super.onPostExecute(result);
		}
		
		public void jsonParseWithUrlString(String result) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				if (jsonArray instanceof JSONArray) {
					Log.v("json", "is JSONArray");
					for (int i = 0; i < jsonArray.length(); i++) {

						JSONObject jObject = jsonArray.getJSONObject(i);
						
						DeliveryClass neDeliveryClass = new DeliveryClass();
						
						Log.v("area", jObject.getString("area"));
						
						neDeliveryClass.area = jObject.getString("area");
						neDeliveryClass.deliverfee = jObject.getString("deliverfee");
						neDeliveryClass.minorder = jObject.getString("minorder");
						neDeliveryClass.rid = jObject.getString("rid");
						neDeliveryClass.sn = jObject.getString("sn");
						
						mDeliveryClassesArrayList.add(neDeliveryClass);
						mDeliveryAreaNamesList.add(neDeliveryClass.area);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	Dialog dialog_sign_in;
	
	public void ShowDialogSignIn(){
		dialog_sign_in = new Dialog(getActivity());
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
					
					Toast.makeText(getActivity(), "Please fill up the area!", Toast.LENGTH_SHORT).show();
				} else {
					if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_address.getText().toString()).matches()) {
						Toast.makeText(getActivity(), "Please insert correct email address.",
								Toast.LENGTH_SHORT).show();
					} else {
						sPref.edit().putBoolean("is_login", true).commit();
						dialog_sign_in.dismiss();

						sPref.edit().putString("first_name", "first_name").commit();
						sPref.edit().putString("last_name", "last_name").commit();
						sPref.edit().putString("email_address", "email_address").commit();
						sPref.edit().putString("contact_no", "contact_no").commit();
						sPref.edit().putString("address_1", "address_1").commit();
						sPref.edit().putString("address_2", "address_2").commit();
						sPref.edit().putString("city", "city").commit();
						sPref.edit().putString("county", "county").commit();
						sPref.edit().putString("post_code", "post_code").commit();
						sPref.edit().putString("country", "country").commit();
						
						HashMap<String, String> sign_in_data = new HashMap<String, String>();

						sign_in_data.put("email_address", email_address.getText().toString());
						sign_in_data.put("password", password.getText().toString());
						
						new SignInAsyncTask(getActivity(), sign_in_data).execute();
					}
				}
			}
		});
	}
	
	Dialog dialog_sign_up;
	
	public void ShowDialogSignUp(){
		dialog_sign_up = new Dialog(getActivity());
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
					
					Toast.makeText(getActivity(), "Please fill up the area!", Toast.LENGTH_SHORT).show();
				} else {
					if (contact_no.getText().toString().length() < 11 && !contact_no.getText().toString().startsWith("0")) {
						Toast.makeText(getActivity(), "Please enter 11 digit phone number and start with 0.",
								Toast.LENGTH_SHORT).show();
					} else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_address.getText().toString()).matches()) {
						Toast.makeText(getActivity(), "Please insert correct email address.",
								Toast.LENGTH_SHORT).show();
					} else if (!password.getText().toString().equals(confirm_password.getText().toString())) {
						Toast.makeText(getActivity(), "Password do not match.",
								Toast.LENGTH_SHORT).show();
					} else {
						sPref.edit().putBoolean("is_login", true).commit();
						dialog_sign_up.dismiss();

						sPref.edit().putString("first_name", first_name.getText().toString()).commit();
						sPref.edit().putString("last_name", last_name.getText().toString()).commit();
						sPref.edit().putString("email_address", email_address.getText().toString()).commit();
						sPref.edit().putString("contact_no", contact_no.getText().toString()).commit();
						sPref.edit().putString("address_1", address_1.getText().toString()).commit();
						sPref.edit().putString("address_2", address_2.getText().toString()).commit();
						sPref.edit().putString("city", city.getText().toString()).commit();
						sPref.edit().putString("county", county.getText().toString()).commit();
						sPref.edit().putString("post_code", post_code.getText().toString()).commit();
						sPref.edit().putString("country", country.getText().toString()).commit();
						sPref.edit().putString("term_checkbox", "agreed").commit();
						
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
						
						new SignUpAsyncTask(getActivity(), sign_up_data).execute();
					}
					/*sPref.edit().putBoolean("is_login", true).commit();
					dialog_sign_up.dismiss();

					sPref.edit().putString("first_name", first_name.getText().toString()).commit();
					sPref.edit().putString("last_name", last_name.getText().toString()).commit();
					sPref.edit().putString("email_address", email_address.getText().toString()).commit();
					sPref.edit().putString("contact_no", contact_no.getText().toString()).commit();
					sPref.edit().putString("address_1", address_1.getText().toString()).commit();
					sPref.edit().putString("address_2", address_2.getText().toString()).commit();
					sPref.edit().putString("city", city.getText().toString()).commit();
					sPref.edit().putString("county", county.getText().toString()).commit();
					sPref.edit().putString("post_code", post_code.getText().toString()).commit();
					sPref.edit().putString("country", country.getText().toString()).commit();*/
				}
			}
		});
	}
	
	Dialog dialog_country_selection;
	String [] Country_array = {"United Kingdom", "Brazil", "USA"};
	
	public void ShowDialogCountrySelection(final TextView selection_country){
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
			
			getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_lay, new CheckOutFragment(),
					CheckOutFragment.FRAGMENT_TAG).addToBackStack(null).commit();
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
			
			getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_lay, new CheckOutFragment(),
					CheckOutFragment.FRAGMENT_TAG).addToBackStack(null).commit();
			super.onPostExecute(result);
		}
	}
}
