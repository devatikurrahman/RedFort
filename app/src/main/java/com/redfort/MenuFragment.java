package com.redfort;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MenuFragment extends Fragment implements OnClickListener {

	private View rootView;
	public static final String FRAGMENT_TAG = "TAG_MenuFragment";
	
	ListView menu_list;
	MenuListAdapter menuListAdapter;
	
	ArrayList<MenuClass> menuClassesArrayList = new ArrayList<MenuClass>();
	SharedPreferences sPref;
	
	public static MenuClass staticMenuClass;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState == null){
			rootView = inflater.inflate(R.layout.menu_fragment, container, false);
		}
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (savedInstanceState == null) 
			Initialize();
			
	}
	
	public void Initialize(){
		MainActivity.top_bar_title.setText("Menu");
		MainActivity.slide_menu_button.setBackgroundResource(R.drawable.top_left_slide_button);
		sPref = getActivity().getSharedPreferences("RedFortPrefs", Context.MODE_PRIVATE);
		menu_list = (ListView) rootView.findViewById(R.id.menu_list);
		
		if(menuClassesArrayList.size() <= 0) {
			
			menuListAdapter = new MenuListAdapter(getActivity());
			MainActivity.loading_lay.setVisibility(View.VISIBLE);
		
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//menu_list.setAdapter(menuListAdapter);
					new MenuAsynkTask(getActivity(), MainActivity.serverApiFile.CATEGORY_URL, menu_list).execute();
				}
			}, 450);
			
		}
		else {
			MainActivity.loading_lay.setVisibility(View.GONE);
			menu_list.setAdapter(menuListAdapter);
		}
		
		menu_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				staticMenuClass = menuClassesArrayList.get(position);
				getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_lay, new ItemFragment(),
						ItemFragment.FRAGMENT_TAG).addToBackStack(null).commit();
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		default:
			break;
		}
	}
	
	public class MenuAsynkTask extends AsyncTask<String, String, String> {

		Context context;
		String url;
		ListView list_view;
		
		public MenuAsynkTask (Context context, String url, ListView list_view) {
			this.context = context;
			this.url = url;
			this.list_view = list_view;
			
			Log.v("category url: ", ""+url);
			
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
			Log.v("result", "result : "+result);
			
			/*try {
			    InputStream json = context.getAssets().open("redfort_category.txt");
			    BufferedReader in = new BufferedReader(new InputStreamReader(json));
			    
			    String str = in.readLine();
			    in.close();
			    
			    menuClassesArrayList.clear();
			    
			    jsonParseWithUrlString(str);
			    
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			
			jsonParseWithUrlString(result);
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
						
						Log.v("json", "ID : "+jObject.getString("ID"));
						MenuClass newMenuClass = new MenuClass();
						
						newMenuClass.ID = jObject.getString("ID");
						newMenuClass.category = jObject.getString("category");
						newMenuClass.hinds = jObject.getString("hinds");
						newMenuClass.notes = jObject.getString("notes");
						newMenuClass.priority = jObject.getString("priority");
						newMenuClass.restaurentid = jObject.getString("restaurentid");
						newMenuClass.root_category = jObject.getString("root_category");
						
						menuClassesArrayList.add(newMenuClass);

					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Collections.sort(menuClassesArrayList, new Comparator<MenuClass>() {

	            @Override
	            public int compare(MenuClass lhs, MenuClass rhs) {
	            	int o1 = Integer.parseInt(rhs.priority);
	            	int o2 = Integer.parseInt(lhs.priority);
	            	return(o1>o2 ? -1 : (o1==o2 ? 0 : 1));
	                //lhs.priority.compareTo(rhs.priority);
	            }
	        });

			list_view.setAdapter(menuListAdapter);
		}
	}
	
	public class MenuListAdapter extends BaseAdapter {
		
		LayoutInflater inflater = null;
		Context context;
		
		public MenuListAdapter(Context context){
			this.context = context;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			return menuClassesArrayList.size();
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
			view = inflater.inflate(R.layout.list_menu_view, null);
			final int tmpposition = position;
			
			TextView title, subtitle;
			title = (TextView) view.findViewById(R.id.title);
			subtitle = (TextView) view.findViewById(R.id.subtitle);

			title.setText(menuClassesArrayList.get(position).category);
			if (menuClassesArrayList.get(position).hinds.isEmpty()) {
				subtitle.setVisibility(View.GONE);
			} else {
				subtitle.setText(menuClassesArrayList.get(position).hinds);
			}
			
			return view;
		}
	}
}
