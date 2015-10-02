package istem.forestfire;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import istem.forestfire.R;

import java.util.HashMap;

public class CheckService {
	private Context mContext;
	private SessionManager session;
	private Integer opened_no;
	@SuppressWarnings("unused")
	private int no;
	@SuppressWarnings("unused")
	private Handler handler;
	@SuppressWarnings("unused")
	private String username;

	private String output_id = "";
	private String output_lat = "0";
	private String output_lgt = "0";
	private String output_name;
	private int note_id;
	private String output_forest_name = "";

	@SuppressWarnings("unused")
	public CheckService(Context context) {
		mContext = context;
		session = new SessionManager(mContext);
		HashMap<String, Integer> times = session.getTimesDetail();
		Log.i("check", "start");
		opened_no = 1;// times.get(SessionManager.KEY_OPEN);

	}

	public void startService(final int time) {
		if (opened_no == 1) {
			no = 1;
			session = new SessionManager(mContext);
			session.createTimesSession(opened_no + 1);
			handler = new Handler();
			Thread checkForNewReports = new Thread() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					try {
						sleep(300);
						checkForDetailReport();
						checkForQuickReport();
						// handler.postDelayed(this, time);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						// handler.postDelayed(this, time);
					}

				}

			};
			Log.i("aa3", output_id);
			checkForNewReports.start();
			// handler.postDelayed(checkForNewReports, time);

		}

	}

	public void checkForQuickReport() {
		NewReceiver connect = new NewReceiver(mContext,
				new AsyncResponseData() {
					@Override
					public void processFinish(String out) {

						try {
							Log.i("output_quick", out);
							JSONArray last_report = new JSONArray(out);
							JSONArray first_array = last_report.getJSONArray(0);

							output_id = first_array.getJSONObject(0).getString(
									"id");
							output_lat = first_array.getJSONObject(0)
									.getString("latitude");
							output_lgt = first_array.getJSONObject(0)
									.getString("longitude");
							output_forest_name = first_array.getJSONObject(0)
									.getString("forest_name");

							// check for validity of user
							JSONArray json = new JSONArray(out);
							JSONArray second_array = json.getJSONArray(1);
							JSONObject second_object = second_array
									.getJSONObject(0);
							String isvalid = second_object.getString("isvalid");

							if ((!output_id.equals(""))
									&& (isvalid.equals("t"))) {
								try {
									int newID = Integer.valueOf(output_id);
									int prevID = Integer.valueOf(session
											.getLastQuickReportId());
									if (newID > prevID) {
										showNotification("Quick Fire Reported",
												"Fire reported at "
														+ output_forest_name,
												output_lat, output_lgt);
										Log.i("aa", output_id);
										session.storeLastQuickReportId(
												String.valueOf(newID),
												output_lat, output_lgt);
										Log.i("aa1", output_id);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
		// connect.setPath("/forest_fire/getLastQuickReport.php");
		connect.setPath(SavedReportsActivity.LAST_QUICK_REPORT_PATH);
		connect.execute(new Void[0]);

	}

	public void checkForDetailReport() {
		NewReceiver connect = new NewReceiver(mContext,
				new AsyncResponseData() {
					@Override
					public void processFinish(String out) {

						try {
							JSONArray last_report = new JSONArray(out);
							JSONArray first_array = last_report.getJSONArray(0);

							output_id = first_array.getJSONObject(0).getString(
									"id");
							output_lat = first_array.getJSONObject(0)
									.getString("latitude");
							output_lgt = first_array.getJSONObject(0)
									.getString("longitude");
							output_name = first_array.getJSONObject(0)
									.getString("forest_name");

							// check the validity of user
							JSONArray json = new JSONArray(out);
							JSONArray second_array = json.getJSONArray(1);
							JSONObject second_object = second_array
									.getJSONObject(0);
							String isvalid = second_object.getString("isvalid");

							if ((!output_id.equals(""))
									&& (isvalid.equals("t"))) {
								try {
									int newID = Integer.valueOf(output_id);
									int prevID = Integer.valueOf(session
											.getLastDetailReportId());
									if (newID > prevID) {
										showNotification("Fire Reported",
												"Fire occured at "
														+ output_name,
												output_lat, output_lgt);
										session.storeLastDetailReportId(
												String.valueOf(newID),
												output_lat, output_lgt);
										Log.i("aa", output_id);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

		connect.setPath(SavedReportsActivity.LAST_DETAIL_REPORT_PATH);
		connect.execute(new Void[0]);

	}

	@SuppressWarnings({ "static-access", "deprecation" })
	private void showNotification(String title, String message, String lat,
			String lgt) {
		NotificationManager mgr = (NotificationManager) mContext
				.getSystemService(mContext.NOTIFICATION_SERVICE);
		int requestID = (int) System.currentTimeMillis();
		Notification note = new Notification(R.mipmap.ic_launcher,
				"Report Fire", System.currentTimeMillis());
		Intent intent = new Intent(mContext, ViewOnMapActivity.class);
		intent.putExtra("latitude", lat);
		intent.putExtra("longitude", lgt);
		if (title.contains("Quick")) {
			note_id = 1;
			intent.putExtra("type", "quick");
		} else {
			note_id = 2;
			intent.putExtra("type", "detail");
		}
		PendingIntent i = PendingIntent.getActivity(mContext, requestID,
				intent, 0);
		note.setLatestEventInfo(mContext, title, message, i);
		note.defaults |= Notification.DEFAULT_SOUND;
		mgr.notify(note_id, note);
	}

	public void stopService() {
		// handler.removeCallbacks(runnable);
	}

}
