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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemFragment extends Fragment {

	private View rootView;
	public static final String FRAGMENT_TAG = "TAG_ItemFragment";
	ListView list_items;
	ItemsAdapter itemsAdapter;
	
	ArrayList<SubMenuClass> subMenuClassesArrayList = new ArrayList<SubMenuClass>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState == null){
			rootView = inflater.inflate(R.layout.items_fragment, container, false);
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
		MainActivity.top_bar_title.setText("Items");
		MainActivity.slide_menu_button.setBackgroundResource(R.drawable.top_back_button);
		list_items = (ListView) rootView.findViewById(R.id.list_items);
		
		itemsAdapter = new ItemsAdapter(getActivity());
		
		// for menu item click object
		//MenuFragment.staticMenuClass
		
		String url = MainActivity.serverApiFile.SUBCATEGORY_URL+MenuFragment.staticMenuClass.ID;
		new SubMenuAsynkTask(getActivity(), url , list_items).execute();
	}
	
	public class SubMenuAsynkTask extends AsyncTask<String, String, String> {

		Context context;
		String url;
		ListView list_view;
		
		public SubMenuAsynkTask (Context context, String url, ListView list_view) {
			this.context = context;
			this.url = url;
			this.list_view = list_view;
			
			Log.v("sub category url: ", ""+url);
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			MainActivity.loading_lay.setVisibility(View.VISIBLE);
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
			    InputStream json = context.getAssets().open("vegetable_side_dishes.txt");
			    BufferedReader in = new BufferedReader(new InputStreamReader(json));
			    
			    String str = in.readLine();
			    in.close();
			    
			    subMenuClassesArrayList.clear();
			    
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
					Log.v("jsonArray", "is JSONArray");
					for (int i = 0; i < jsonArray.length(); i++) {

						JSONObject jObject = jsonArray.getJSONObject(i);
						
						//Log.v("json", "sn : "+jObject.getString("sn"));
						SubMenuClass newSubMenuClass = new SubMenuClass();

						newSubMenuClass.sn = jObject.getString("sn");
						newSubMenuClass.category_id = jObject.getString("category_id");
						newSubMenuClass.manu_id = jObject.getString("manu_id");
						newSubMenuClass.description = jObject.getString("description");
						newSubMenuClass.extra_items = jObject.getString("extra_items");
						newSubMenuClass.extra_items_list = jObject.getString("extra_items_list");
						newSubMenuClass.hot = jObject.getString("hot");
						newSubMenuClass.manu_name = jObject.getString("manu_name");
						newSubMenuClass.mild = jObject.getString("mild");
						newSubMenuClass.nut = jObject.getString("nut");
						newSubMenuClass.popular = jObject.getString("popular");
						newSubMenuClass.price = jObject.getString("price");
						newSubMenuClass.price_collection = jObject.getString("price_collection");
						newSubMenuClass.priority = jObject.getString("priority");
						newSubMenuClass.short_name = jObject.getString("short_name");
						newSubMenuClass.submenu = jObject.getString("submenu");
						newSubMenuClass.sweetsour = jObject.getString("sweetsour");
						newSubMenuClass.types = jObject.getString("types");
						newSubMenuClass.vegetable = jObject.getString("vegetable");
						
						if (newSubMenuClass.submenu.equals("1")) {
							
							JSONArray subjsonArray = new JSONArray(jObject.getString("submenulist"));
							if (subjsonArray instanceof JSONArray) {

								Log.v("subjsonArray", "is JSONArray");
								for (int j = 0; j < subjsonArray.length(); j++) {
									
									SubMenuSubClass newSubMenuSubClass = new SubMenuSubClass();

									JSONObject subjObject = subjsonArray.getJSONObject(j);
									
									newSubMenuSubClass.sn = subjObject.getString("sn");
									newSubMenuSubClass.des = subjObject.getString("des");
									newSubMenuSubClass.menuid = subjObject.getString("menuid");
									newSubMenuSubClass.name = subjObject.getString("name");
									newSubMenuSubClass.price = subjObject.getString("price");
									newSubMenuSubClass.price_collection = subjObject.getString("price_collection");
									newSubMenuSubClass.shortname = subjObject.getString("shortname");
									newSubMenuSubClass.priority = subjObject.getString("priority");
									
									newSubMenuClass.mSubMenuSubClassesArrayList.add(newSubMenuSubClass);
								}
							}
						}
						
						subMenuClassesArrayList.add(newSubMenuClass);

					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			Collections.sort(subMenuClassesArrayList, new Comparator<SubMenuClass>() {

	            @Override
	            public int compare(SubMenuClass lhs, SubMenuClass rhs) {
	            	int o1 = Integer.parseInt(rhs.priority);
	            	int o2 = Integer.parseInt(lhs.priority);
	            	return(o1>o2 ? -1 : (o1==o2 ? 0 : 1));
	            }
	        });
			
			list_view.setAdapter(itemsAdapter);
		}
	}
	
	public class ItemsAdapter extends BaseAdapter {
		
		LayoutInflater inflater = null;
		Context context;
		
		public ItemsAdapter(Context context){
			this.context = context;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			return subMenuClassesArrayList.size();
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
			view = inflater.inflate(R.layout.list_items_view, null);
			
			TextView title, subtitle, description;
			Button add_to_cart;
			
			LinearLayout items_sub_list = (LinearLayout) view.findViewById(R.id.items_sub_list);
			
			final int tmp_position = position;

			title = (TextView) view.findViewById(R.id.title);
			subtitle = (TextView) view.findViewById(R.id.subtitle);
			description = (TextView) view.findViewById(R.id.description);
			add_to_cart = (Button) view.findViewById(R.id.add_to_cart);

			title.setText(subMenuClassesArrayList.get(position).manu_name);
			subtitle.setText("(£"+subMenuClassesArrayList.get(position).price+")");
			
			if (subMenuClassesArrayList.get(position).description == null
					|| subMenuClassesArrayList.get(position).description.isEmpty()
					|| subMenuClassesArrayList.get(position).description.equals("")) {
				
				description.setVisibility(View.GONE);
			} else {
				description.setText(subMenuClassesArrayList.get(position).description);
			}
			
			if (subMenuClassesArrayList.get(position).submenu.equals("1")) {

				items_sub_list.setVisibility(View.VISIBLE);
				add_to_cart.setVisibility(View.INVISIBLE);
				subtitle.setVisibility(View.INVISIBLE);

		        LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				for (int i = 0; i < subMenuClassesArrayList.get(position).mSubMenuSubClassesArrayList.size(); i++) {
					View sub_view = inflater.inflate(R.layout.list_items_sub_view, null);
					
					TextView sub_items_title, sub_items_subtitle, sub_items_description;
					Button sub_items_add_to_cart;
					
					sub_items_title = (TextView) sub_view.findViewById(R.id.sub_items_title);
					sub_items_subtitle = (TextView) sub_view.findViewById(R.id.sub_items_subtitle);
					sub_items_description = (TextView) sub_view.findViewById(R.id.sub_items_description);
					sub_items_add_to_cart = (Button) sub_view.findViewById(R.id.sub_items_add_to_cart);

					sub_items_title.setText(subMenuClassesArrayList.get(position).mSubMenuSubClassesArrayList.get(i).name);
					sub_items_subtitle.setText("(£"+subMenuClassesArrayList.get(position).mSubMenuSubClassesArrayList.get(i).price+")");
					
					if (subMenuClassesArrayList.get(position).mSubMenuSubClassesArrayList.get(i).des == null
							|| subMenuClassesArrayList.get(position).mSubMenuSubClassesArrayList.get(i).des.isEmpty()
							|| subMenuClassesArrayList.get(position).mSubMenuSubClassesArrayList.get(i).des.equals("")) {
						
						sub_items_description.setVisibility(View.GONE);
					} else {
						sub_items_description.setText(subMenuClassesArrayList.get(position).mSubMenuSubClassesArrayList.get(i).des);
					}
					
					final int sub_tmp_position = i;
					
					sub_items_add_to_cart.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Toast.makeText(getActivity(), "Added to cart!", Toast.LENGTH_SHORT).show();
							
							if (MainActivity.cart_array_list.size() <= 0 ) {
								CartClass mCartClass = new CartClass();

								mCartClass.sn = subMenuClassesArrayList.get(tmp_position).
										mSubMenuSubClassesArrayList.get(sub_tmp_position).sn;
								mCartClass.manu_id = subMenuClassesArrayList.get(tmp_position).
										mSubMenuSubClassesArrayList.get(sub_tmp_position).menuid;
								mCartClass.manu_name = subMenuClassesArrayList.get(tmp_position).manu_name +"-"+
										subMenuClassesArrayList.get(tmp_position).mSubMenuSubClassesArrayList
										.get(sub_tmp_position).name;
								mCartClass.price = subMenuClassesArrayList.get(tmp_position).
										mSubMenuSubClassesArrayList.get(sub_tmp_position).price;
								mCartClass.priceC = subMenuClassesArrayList.get(tmp_position).
										mSubMenuSubClassesArrayList.get(sub_tmp_position).price;
								mCartClass.qty = "1";
								mCartClass.restaurentid = "1";
								mCartClass.session_id = System.currentTimeMillis()+"";
								mCartClass.shortname = subMenuClassesArrayList.get(tmp_position).
										mSubMenuSubClassesArrayList.get(sub_tmp_position).shortname;
								mCartClass.types = subMenuClassesArrayList.get(tmp_position).types;
								
								MainActivity.cart_array_list.add(mCartClass);
							} else {
								boolean flag = false;
								for (int i = 0; i < MainActivity.cart_array_list.size(); i ++) {
									flag = false;
									if (subMenuClassesArrayList.get(tmp_position).
											mSubMenuSubClassesArrayList.get(sub_tmp_position).menuid
											.equals(MainActivity.cart_array_list.get(i).manu_id)
										&& subMenuClassesArrayList.get(tmp_position).
											mSubMenuSubClassesArrayList.get(sub_tmp_position).sn
											.equals(MainActivity.cart_array_list.get(i).sn)) {
										
										if (MainActivity.cart_array_list.get(i).qty == null
												|| MainActivity.cart_array_list.get(i).qty.isEmpty()
												|| MainActivity.cart_array_list.get(i).qty.equals("")
												|| MainActivity.cart_array_list.get(i).qty.equals("0")) {

											MainActivity.cart_array_list.get(i).qty = 1+"";
										} else {
											int count = Integer.parseInt(MainActivity.cart_array_list.get(i).qty);
											count++;
											MainActivity.cart_array_list.get(i).qty = count+"";
										}
										flag = true;
										break;
									}
								}
								if (!flag) {
									CartClass mCartClass = new CartClass();

									mCartClass.sn = subMenuClassesArrayList.get(tmp_position).
											mSubMenuSubClassesArrayList.get(sub_tmp_position).sn;
									mCartClass.manu_id = subMenuClassesArrayList.get(tmp_position).
											mSubMenuSubClassesArrayList.get(sub_tmp_position).menuid;
									mCartClass.manu_name = subMenuClassesArrayList.get(tmp_position).manu_name +"-"+
											subMenuClassesArrayList.get(tmp_position).mSubMenuSubClassesArrayList
											.get(sub_tmp_position).name;
									mCartClass.price = subMenuClassesArrayList.get(tmp_position).
											mSubMenuSubClassesArrayList.get(sub_tmp_position).price;
									mCartClass.priceC = subMenuClassesArrayList.get(tmp_position).
											mSubMenuSubClassesArrayList.get(sub_tmp_position).price;
									mCartClass.qty = "1";
									mCartClass.restaurentid = "1";
									mCartClass.session_id = System.currentTimeMillis()+"";
									mCartClass.shortname = subMenuClassesArrayList.get(tmp_position).
											mSubMenuSubClassesArrayList.get(sub_tmp_position).shortname;
									mCartClass.types = subMenuClassesArrayList.get(tmp_position).types;
									
									MainActivity.cart_array_list.add(mCartClass);
								}
							}
							
							MainActivity.top_cart_button.setText("("+MainActivity.cart_array_list.size()+")");
						}
					});
					
					items_sub_list.addView(sub_view);
				}
			} else {
				items_sub_list.setVisibility(View.GONE);
			}
			
			add_to_cart.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "Added to cart!", Toast.LENGTH_SHORT).show();
					
					if (MainActivity.cart_array_list.size() <= 0 ) {
						CartClass mCartClass = new CartClass();

						mCartClass.sn = subMenuClassesArrayList.get(tmp_position).sn;
						mCartClass.manu_id = subMenuClassesArrayList.get(tmp_position).manu_id;
						mCartClass.manu_name = subMenuClassesArrayList.get(tmp_position).manu_name;
						mCartClass.price = subMenuClassesArrayList.get(tmp_position).price;
						mCartClass.priceC = subMenuClassesArrayList.get(tmp_position).price;
						mCartClass.qty = "1";
						mCartClass.restaurentid = "1";
						mCartClass.session_id = System.currentTimeMillis()+"";
						mCartClass.shortname = subMenuClassesArrayList.get(tmp_position).short_name;
						mCartClass.types = subMenuClassesArrayList.get(tmp_position).types;
						
						MainActivity.cart_array_list.add(mCartClass);
					} else {
						boolean flag = false;
						for (int i = 0; i < MainActivity.cart_array_list.size(); i ++) {
							flag = false;
							if (subMenuClassesArrayList.get(tmp_position).manu_id
									.equals(MainActivity.cart_array_list.get(i).manu_id)
								&& subMenuClassesArrayList.get(tmp_position).sn
									.equals(MainActivity.cart_array_list.get(i).sn)) {
								
								if (MainActivity.cart_array_list.get(i).qty == null
										|| MainActivity.cart_array_list.get(i).qty.isEmpty()
										|| MainActivity.cart_array_list.get(i).qty.equals("")
										|| MainActivity.cart_array_list.get(i).qty.equals("0")) {

									MainActivity.cart_array_list.get(i).qty = 1+"";
								} else {
									int count = Integer.parseInt(MainActivity.cart_array_list.get(i).qty);
									count++;
									MainActivity.cart_array_list.get(i).qty = count+"";
								}
								flag = true;
								break;
							}
						}
						if (!flag) {
							CartClass mCartClass = new CartClass();

							mCartClass.sn = subMenuClassesArrayList.get(tmp_position).sn;
							mCartClass.manu_id = subMenuClassesArrayList.get(tmp_position).manu_id;
							mCartClass.manu_name = subMenuClassesArrayList.get(tmp_position).manu_name;
							mCartClass.price = subMenuClassesArrayList.get(tmp_position).price;
							mCartClass.priceC = subMenuClassesArrayList.get(tmp_position).price;
							mCartClass.qty = "1";
							mCartClass.restaurentid = "1";
							mCartClass.session_id = System.currentTimeMillis()+"";
							mCartClass.shortname = subMenuClassesArrayList.get(tmp_position).short_name;
							mCartClass.types = subMenuClassesArrayList.get(tmp_position).types;
							
							MainActivity.cart_array_list.add(mCartClass);
						}
					}
					
					MainActivity.top_cart_button.setText("("+MainActivity.cart_array_list.size()+")");
				}
			});
			
			return view;
		}
	}
}
