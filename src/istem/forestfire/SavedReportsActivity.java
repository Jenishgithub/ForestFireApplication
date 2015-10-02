package istem.forestfire;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import istem.forestfire.R;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Hp on 6/18/2015.
 */
@SuppressLint("ShowToast")
public class SavedReportsActivity extends ActionBarActivity {
	private FileCache fileCache;
	private ArrayList<File> forest_files_list = new ArrayList<File>();
	private ArrayList<String> forest_names_list = new ArrayList<String>();
	private ArrayList<String> forest_start_time = new ArrayList<String>();
	private HashMap<String, Object> hm;
	List<HashMap<String, Object>> aList = new ArrayList<HashMap<String, Object>>();
	private SimpleAdapter adapter;
	private ListView listView;
	private ImageButton delete_report;
	private Toast toast;
	private ImageButton edit_report;
	private ImageButton upload_report;
	private Button upload_all;
	private String path;
	private String host = StringReceiver.host;
	private ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
	private Button new_report;
	@SuppressWarnings("unused")
	private ArrayList<BasicNameValuePair> newNameValuePairs = new ArrayList<BasicNameValuePair>();
	@SuppressWarnings("unused")
	private JSONObject userIMEI;
	WifiManager wifiManager;
	WifiInfo winFo;
	String reg_imei_no;
	public static String DETAIL_REPORT_PATH = "/forest_fire/detail_report.php";
	public static String CHECK_VALID_USER_PATH = "/forest_fire/checkuservalid.php";
	public static String QUICK_REPORT_PATH = "/forest_fire/quick_report.php";
	public static String LAST_DETAIL_REPORT_PATH = "/forest_fire/getLastDetailReport2.php";
	public static String LAST_QUICK_REPORT_PATH = "/forest_fire/getLastQuickReport2.php";
	public static String REGISTER_USER_PATH = "/forest_fire/register.php";
	public static String RECEIVE_PREVIOUS_REPORT_PATH = "/forest_fire/receive_previous_report.php";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_reports);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon);
		getSupportActionBar().setTitle("Saved Reports");

		path = DETAIL_REPORT_PATH;
		toast = Toast.makeText(SavedReportsActivity.this, "",
				Toast.LENGTH_SHORT);
		listView = (ListView) findViewById(R.id.ListView);
		fileCache = new FileCache(SavedReportsActivity.this);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		new_report = (Button) findViewById(R.id.new_button);
		new_report.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SavedReportsActivity.this,
						ReportActivity.class);
				startActivity(intent);
				// finish();
			}
		});
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras.containsKey("new_report")) {
				new_report.setVisibility(View.VISIBLE);
			}
		}
		upload_all = (Button) findViewById(R.id.upload_all);

		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		winFo = wifiManager.getConnectionInfo();
		reg_imei_no = winFo.getMacAddress();

		forest_files_list = fileCache.getallFiles("txt");
		for (int i = 0; i < forest_files_list.size(); i++) {
			forest_names_list.add(fileCache.getFileParameter(
					forest_files_list.get(i), "forest_name"));
			forest_start_time.add(fileCache.getFileParameter(
					forest_files_list.get(i), "fire_date"));
		}

		for (int i = 0; i < forest_files_list.size(); i++) {
			hm = new HashMap<String, Object>();
			hm.put("name", forest_names_list.get(i));
			hm.put("date", forest_start_time.get(i));
			Log.i("data", forest_names_list.get(i));
			aList.add(hm);
		}
		String[] from = { "name", "date" };
		int[] to = { R.id.group_title, R.id.group_start_date };

		adapter = new SimpleAdapter(SavedReportsActivity.this, aList,
				R.layout.list_group, from, to) {
			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {
				final View row = super.getView(position, convertView, parent);

				delete_report = (ImageButton) row
						.findViewById(R.id.group_delete);
				delete_report.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								SavedReportsActivity.this);
						builder.setTitle("Confirm Delete");
						builder.setMessage("Are You Sure You want To Delet This File");
						builder.setIcon(android.R.drawable.ic_menu_help);
						builder.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialogInterface,
											int i) {
										fileCache.deletefile(forest_files_list
												.get(position).getName());
										removeAllFromPosition(position);
										toaster("Deleted");
									}
								});

						builder.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialogInterface,
											int i) {
										toaster("Deletion Cancelled");
									}
								});

						builder.show();
					}
				});

				edit_report = (ImageButton) row.findViewById(R.id.group_edit);
				edit_report.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(SavedReportsActivity.this,
								DetailReportActivity.class);
						Log.i("editing", forest_files_list.get(position)
								.getName());
						intent.putExtra("file_name",
								forest_files_list.get(position).getName());
						startActivity(intent);
					}
				});

				upload_report = (ImageButton) row
						.findViewById(R.id.group_upload);
				upload_report.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// check if user posting report is valid user or not
						// before uploading

						if (reg_imei_no != null) {
							JSONObject userIMEI = new JSONObject();
							try {
								userIMEI.put("imei_no", reg_imei_no);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Receiver connect = new Receiver(
									SavedReportsActivity.this,
									new AsyncResponseData() {
										@Override
										public void processFinish(String output) {
											Log.i("crossover", output);
											Log.d("crossover", "user is:"
													+ output);
											try {
												JSONObject status = new JSONObject(
														output);
												if (status.getString("user")
														.equals("valid")) {

													postDetailRepoort(
															forest_files_list
																	.get(position),
															position);

													// finish();
												} else {
													toaster("Sorry! You are not valid user");
												}
											} catch (JSONException e) {
												e.printStackTrace();
											}
										}
									});

							if (connect.haveNetworkConnection()) {
								connect.setPath(CHECK_VALID_USER_PATH);
								try {

									Iterator<String> keys = userIMEI.keys();
									while (keys.hasNext()) {
										String currentDynamicKey = (String) keys
												.next();
										connect.addNameValuePairs(
												currentDynamicKey,
												userIMEI.getString(currentDynamicKey));
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
								connect.execute(new Void[0]);
							} else {
								toaster("Needs internet connection");
							}
						} else {
							toaster("Your device IMEI is not available");

						}

					}
				});
				return row;
			}
		};
		listView.setAdapter(adapter);

		upload_all.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("unused")
			public ProgressDialog mProgressDialog;

			@Override
			public void onClick(View view) {

				// check here if user is valid or not before posting reports
				if (reg_imei_no != null) {
					JSONObject userIMEI = new JSONObject();
					try {
						userIMEI.put("imei_no", reg_imei_no);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Receiver connect = new Receiver(SavedReportsActivity.this,
							new AsyncResponseData() {
								@Override
								public void processFinish(String output) {
									Log.i("crossover", output);
									Log.d("crossover", "user is:" + output);
									try {
										JSONObject status = new JSONObject(
												output);
										if (status.getString("user").equals(
												"valid")) {
											if (!forest_files_list.isEmpty()) {

												for (int i = forest_files_list
														.size() - 1; i >= 0; i--) {

													if (fileCache
															.haveNetworkConnection()) {
														toaster("Please Wait While Uploading");
														if (detailreport(forest_files_list
																.get(i))) {
															Log.i("uploading",
																	forest_files_list
																			.get(i)
																			.getName());
															fileCache
																	.deletefile(forest_files_list
																			.get(i)
																			.getName());
															removeAllFromPosition(i);
															if (i == 0) {
																toaster("Upload Successful");
															}
														} else {
															toaster("Upload Failed");
															break;
														}
													} else {
														toaster("No working Internet Connection");
													}
												}
												toaster("Reporting Successful");
											} else {
												toaster("No saved files..");
											}
											// finish();
										} else {
											toaster("Sorry! You are not valid user");
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							});

					if (connect.haveNetworkConnection()) {
						connect.setPath(CHECK_VALID_USER_PATH);
						try {

							Iterator<String> keys = userIMEI.keys();
							while (keys.hasNext()) {
								String currentDynamicKey = (String) keys.next();
								connect.addNameValuePairs(currentDynamicKey,
										userIMEI.getString(currentDynamicKey));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						connect.execute(new Void[0]);
					} else {
						toaster("Needs internet connection");
					}
				} else {
					toaster("Your device IMEI is not available");

				}

			}
		});
	}

	@SuppressWarnings({ "static-access", "unused" })
	private boolean detailreport(File file) {

		boolean upload_status = false;
		String str1 = host + path;
		DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
		HttpPost localHttpPost = new HttpPost(str1);
		try {
			String allData = fileCache.getStringFromFile(file.getPath());
			JSONObject data = new JSONObject(allData);
			JSONObject forestFire = data.getJSONObject("ForestFire");
			Iterator<String> keys = forestFire.keys();
			while (keys.hasNext()) {
				String currentDynamicKey = (String) keys.next();
				nameValuePairs.add(new BasicNameValuePair(currentDynamicKey,
						forestFire.getString(currentDynamicKey)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			localHttpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			String str2 = EntityUtils.toString(localDefaultHttpClient.execute(
					localHttpPost).getEntity());
			Log.i("all_report_data", str2);
			if (new JSONObject(str2).getString("status").equals("success"))
				return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("static-access")
	private void postDetailRepoort(final File file, final int position) {
		Receiver connect = new Receiver(SavedReportsActivity.this,
				new AsyncResponseData() {
					@Override
					public void processFinish(String output) {
						Log.i("quick_report_return", output);
						try {
							JSONObject status = new JSONObject(output);
							if (status.getString("status").equals("success")) {

								// after saved reports are uploaded to the
								// server, they are deleted from the file list
								fileCache.deletefile(file.getName());
								removeAllFromPosition(position);
								toaster("Reporting Successful");
								// finish();
							} else {
								toaster("Reporting Failed");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

		if (connect.haveNetworkConnection()) {
			connect.setPath(DETAIL_REPORT_PATH);
			try {
				String allData = fileCache.getStringFromFile(file.getPath());

				JSONObject data = new JSONObject(allData);
				JSONObject forestFire = data.getJSONObject("ForestFire");
				Iterator<String> keys = forestFire.keys();
				while (keys.hasNext()) {
					String currentDynamicKey = (String) keys.next();
					connect.addNameValuePairs(currentDynamicKey,
							forestFire.getString(currentDynamicKey));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			connect.execute(new Void[0]);
		} else {
			toaster("Needs Internet Connection");
		}
	}

	private void removeAllFromPosition(int position) {
		forest_files_list.remove(position);
		forest_names_list.remove(position);
		forest_start_time.remove(position);
		aList.remove(position);
		adapter.notifyDataSetChanged();
	}

	public void toaster(String string) {
		toast.setText(string);
		toast.show();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(SavedReportsActivity.this,
				MainActivity.class);
		startActivity(intent);

	}
}
