package istem.forestfire;

import istem.forestfire.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Hp on 6/17/2015.
 */
public class QuickReportActivity extends ActionBarActivity {
	@SuppressWarnings("unused")
	private EditText forest_name;
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 0;
	private DatePickerDialog.OnDateSetListener mDateSetListener;
	private RadioGroup rg_current_status;
	private RadioGroup rg_fire_type;
	private RadioGroup rg_fire_size;
	private Button save_report;
	private JSONObject reporting;
	private RadioButton current_status;
	private RadioButton fire_size;
	private RadioButton fire_type;
	private JSONObject forest_fire;
	private Toast toast;
	private String device_id;
	WifiManager wifiManager;
	WifiInfo winFo;
	String reg_imei_no;

	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quick_report);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon);

		toast = Toast
				.makeText(QuickReportActivity.this, "", Toast.LENGTH_SHORT);

		rg_current_status = (RadioGroup) findViewById(R.id.rg_current_status);
		rg_fire_size = (RadioGroup) findViewById(R.id.rg_fire_size);
		rg_fire_type = (RadioGroup) findViewById(R.id.rg_fire_type);

		current_status = (RadioButton) findViewById(R.id.cs_just_started);
		fire_size = (RadioButton) findViewById(R.id.fs_big);
		fire_type = (RadioButton) findViewById(R.id.ft_crown_fire);

		rg_current_status
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup radioGroup, int i) {
						current_status = (RadioButton) findViewById(i);
					}
				});

		rg_fire_size
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup radioGroup, int i) {
						fire_size = (RadioButton) findViewById(i);
					}
				});

		rg_fire_type
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup radioGroup, int i) {
						fire_type = (RadioButton) findViewById(i);
					}
				});

		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		winFo = wifiManager.getConnectionInfo();
		reg_imei_no = winFo.getMacAddress();

		reporting = new JSONObject();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			try {
				if (extras.containsKey("latitude")
						&& extras.containsKey("longitude")
						&& extras.containsKey("forest_name")) {
					reporting.put("latitude", extras.getDouble("latitude"));
					reporting.put("longitude", extras.getDouble("longitude"));
					reporting.put("forest_name",
							extras.getString("forest_name"));
					Log.i("forest_name", extras.getString("forest_name"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {

			// get the wifi mac address of the device instead of imei no
			WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo wInfo = wifiManager.getConnectionInfo();
			device_id = wInfo.getMacAddress();

		} catch (Exception e) {
			e.printStackTrace();
		}

		save_report = (Button) findViewById(R.id.save_report);
		save_report.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (reg_imei_no != null) {
					JSONObject userIMEI = new JSONObject();
					try {
						userIMEI.put("imei_no", reg_imei_no);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Receiver connect = new Receiver(QuickReportActivity.this,
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

											reporting.put("current_status",
													current_status.getText()
															.toString());
											reporting.put("fire_size",
													fire_size.getText()
															.toString());
											reporting.put("fire_type",
													fire_type.getText()
															.toString());
											reporting.put("fire_date",
													getCurrentDate());
											reporting.put("imei_no", device_id);
											forest_fire = new JSONObject().put(
													"ForestFire", reporting);
											Log.i("quick",
													forest_fire.toString());

											postQuickReport(forest_fire);
											toaster("Reporting Successful");
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
						connect.setPath(SavedReportsActivity.CHECK_VALID_USER_PATH);
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

	private void postQuickReport(JSONObject data) {
		Receiver connect = new Receiver(QuickReportActivity.this,
				new AsyncResponseData() {
					@Override
					public void processFinish(String output) {
						Log.i("quick_report_return", output);
						try {
							JSONObject status = new JSONObject(output);
							if (status.getString("status").equals("success")) {
								toaster("Reporting Successful");
								showNotificationMessage("Quick Report",
										"Reporting Successful");
								finish();
							} else {
								showNotificationMessage("Quick Report",
										"Reporting Failed");
								toaster("Reporting Failed");
							}
						} catch (JSONException e) {
							toaster("Reporting Failed");
							e.printStackTrace();
						}
					}
				});

		if (connect.haveNetworkConnection()) {
			connect.setPath(SavedReportsActivity.QUICK_REPORT_PATH);
			Log.i("data_quick", data.toString());
			try {
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

	@SuppressWarnings("deprecation")
	private void showNotificationMessage(String title, String message) {
		NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		int requestID = (int) System.currentTimeMillis();
		Notification note = new Notification(R.mipmap.ic_launcher,
				"Report Fire", System.currentTimeMillis());
		Intent intent = new Intent(QuickReportActivity.this,
				ReportActivity.class);
		PendingIntent i = PendingIntent.getActivity(QuickReportActivity.this,
				requestID, intent, 0);
		note.setLatestEventInfo(QuickReportActivity.this, title, message, i);
		mgr.notify(3, note);
	}

	private void toaster(String s) {
		toast.setText(s);
		toast.show();
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	@SuppressLint("SimpleDateFormat")
	public String getCurrentDate() {
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));
		String currentDate = dateFormatGmt.format(new Date());
		return currentDate.toString();
	}

	@SuppressWarnings({ "unused", "deprecation" })
	private void showNotification(String title, String message) {
		NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		int requestID = (int) System.currentTimeMillis();
		Notification note = new Notification(R.mipmap.ic_launcher,
				"Report Fire", System.currentTimeMillis());
		Intent intent = new Intent(QuickReportActivity.this, MainActivity.class);
		PendingIntent i = PendingIntent.getActivity(QuickReportActivity.this,
				requestID, intent, 0);
		note.setLatestEventInfo(QuickReportActivity.this, title, message, i);
		note.defaults |= Notification.DEFAULT_SOUND;
		mgr.notify(3, note);
	}

}
