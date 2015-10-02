package istem.forestfire;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import istem.forestfire.R;
import java.util.Iterator;

/**
 * Created by Hp on 8/25/2015.
 */

public class RegisterActivity extends ActionBarActivity {
	private EditText reg_name;
	private EditText reg_phone;
	private EditText reg_email;
	private Toast toast;
	private Button reg_btn;
	private String reg_imei_no;
	private JSONObject data = new JSONObject();
	private EditText reg_address;

	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon);
		getSupportActionBar().setTitle("Register User");
		toast = Toast.makeText(RegisterActivity.this, "", Toast.LENGTH_SHORT);

		reg_name = (EditText) findViewById(R.id.reg_name);
		reg_phone = (EditText) findViewById(R.id.reg_phone);
		reg_email = (EditText) findViewById(R.id.reg_email);
		reg_address = (EditText) findViewById(R.id.reg_address);
		reg_btn = (Button) findViewById(R.id.reg_btn);

		try {
			// TelephonyManager tm = (TelephonyManager)
			// getSystemService(TELEPHONY_SERVICE);
			// reg_imei_no = tm.getDeviceId();

			// get the wifi mac address of device instead of IMEI no.
			WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo winFo = wifiManager.getConnectionInfo();
			reg_imei_no = winFo.getMacAddress();

		} catch (Exception e) {
			e.printStackTrace();
		}

		reg_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!reg_name.getText().toString().equals("")) {

					Log.i("phone", reg_phone.getText().toString());
					if (!reg_phone.getText().toString().equals("")) {
						JSONObject information = new JSONObject();
						try {
							information.put("fullname", reg_name.getText()
									.toString());
							information.put("phone_no", reg_phone.getText()
									.toString());
							information.put("email", reg_email.getText()
									.toString());
							information.put("address", reg_address.getText()
									.toString());
							information.put("imei_no", reg_imei_no);
							data.put("register", information);
							Log.i("data", data.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Receiver connect = new Receiver(RegisterActivity.this,
								new AsyncResponseData() {
									@Override
									public void processFinish(String output) {
										Log.i("output", output);
										JSONObject result = null;
										try {
											result = new JSONObject(output);
										} catch (JSONException e) {
											e.printStackTrace();
										}
										if (result.has("status")) {
											try {
												if (result.getString("status")
														.equals("success")) {
													new SessionManager(
															RegisterActivity.this)
															.UserIsRegistered();
													showSuccessDialog();
												} else if (result.getString(
														"status")
														.equals("fail")) {
													if (result.has("message")) {
														toaster(result
																.getString("message"));
														if (result
																.getString(
																		"message")
																.equals("IMEI Already Registered")) {
															new SessionManager(
																	RegisterActivity.this)
																	.UserIsRegistered();
															startActivity(new Intent(
																	RegisterActivity.this,
																	MainActivity.class));
															finish();
														}
													} else {
														toaster("Registration Failed");
													}
												}
											} catch (JSONException e) {
												e.printStackTrace();
											}
										}

									}

								});

						if (connect.haveNetworkConnection()) {
							connect.setPath(SavedReportsActivity.REGISTER_USER_PATH);
							try {
								JSONObject forestFire = data
										.getJSONObject("register");
								Iterator<String> keys = forestFire.keys();
								while (keys.hasNext()) {
									String currentDynamicKey = (String) keys
											.next();
									connect.addNameValuePairs(
											currentDynamicKey,
											forestFire
													.getString(currentDynamicKey));
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							connect.execute(new Void[0]);
						} else {
							toaster("Needs Internet Connection");
						}
					} else {
						toaster("Phone No Needed");
					}
				} else {
					toaster("Name Field Empty");
				}

			}

		});

	}

	private void showSuccessDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				RegisterActivity.this);
		alertDialogBuilder.setTitle("Registering User");
		alertDialogBuilder.setMessage("User Registration Successful")
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						startActivity(new Intent(RegisterActivity.this,
								MainActivity.class));
						RegisterActivity.this.finish();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	private void toaster(String message) {
		toast.setText(message);
		toast.show();
	}
}
