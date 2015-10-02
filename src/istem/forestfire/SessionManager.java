package istem.forestfire;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager {
	public static final String KEY_OPEN = "open";
	private static final String KEY_VIEW_OPEN = "view_open";
	private static final String KEY_ZOOM = "map_zoom";
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor, editor2;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Shared pref file name
	private static final String PREF_NAME = "Report Fire";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";
	private static final String IS_FIRST = "First";
	public static final String IS_CHECKED = "IsCheckedIn";
	static final String KEY_Time = "time";

	// User name (make variable public to access from outside)
	public static final String KEY_NAME = "name";
	public static final String KEY_LGT = "longitude";
	public static final String KEY_LAT = "latitude";
	public static final String KEY_REP_LGT = "longitude";
	public static final String KEY_REP_LAT = "latitude";

	public static final String KEY_DETAIL_REP_ID = "detail_id";
	public static final String KEY_QUICK_REP_ID = "quick_id";
	public static final String KEY_REGISTER = "register";

	// Constructor
	@SuppressLint("CommitPrefEdits")
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
		editor2 = pref.edit();
	}

	/**
	 * Create login session
	 * */

	public void storeLastDetailReportId(String id, String lat, String lgt) {
		editor2.putString(KEY_DETAIL_REP_ID, id);
		editor2.commit();
	}

	public String getLastDetailReportId() {
		return pref.getString(KEY_DETAIL_REP_ID, "0");
	}

	public void storeLastQuickReportId(String id, String lat, String lgt) {
		editor2.putString(KEY_QUICK_REP_ID, id);
		editor2.commit();
	}

	public String getLastQuickReportId() {
		return pref.getString(KEY_QUICK_REP_ID, "0");
	}

	public void resetSettings() {
		editor2.clear();
		editor2.commit();
	}

	public void is_first(boolean state) {
		// Storing login value as TRUE
		editor.putBoolean(IS_FIRST, state);
		editor.commit();
	}

	public void createTimesSession(int opened) {
		editor.putInt(KEY_OPEN, opened);
		editor.commit();
	}

	public void storeZoomLevel(int zoom) {
		editor.putInt(KEY_ZOOM, 16);
		editor.commit();
	}

	public int getZoomLevel() {
		return pref.getInt(KEY_ZOOM, 16);
	}

	public void createLocationSession(String lat, String lgt) {
		// Storing name in pref
		editor.putString(KEY_LAT, lat);
		editor.putString(KEY_LGT, lgt);
		// commit changes
		editor.commit();
	}

	public boolean checksetting() {
		if (!this.isSettingCalled()) {
			return false;
		} else {
			return true;
		}
	}

	public boolean checkLogin() {
		// Check login status
		if (!this.isLoggedIn()) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, Integer> getTimesDetail() {

		HashMap<String, Integer> times = new HashMap<String, Integer>();

		times.put(KEY_OPEN, pref.getInt(KEY_OPEN, 0));

		return times;
	}

	public String getReportLatitude() {
		return pref.getString(KEY_REP_LAT, "");
	}

	public String getReportLongitude() {
		return pref.getString(KEY_REP_LGT, "");
	}

	public HashMap<String, String> getLocationDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_LAT, pref.getString(KEY_LAT, "27.4703656"));

		// user email id
		user.put(KEY_LGT, pref.getString(KEY_LGT, "85.081478"));
		// return user
		return user;
	}

	public HashMap<String, String> getTimeDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_Time, pref.getString(KEY_Time, null));
		// return user
		return user;
	}

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}

	public boolean isSettingCalled() {
		return pref.getBoolean(IS_CHECKED, false);
	}

	public void setViewOpened(boolean view_opened) {
		editor2.putBoolean(KEY_VIEW_OPEN, false);
		editor2.commit();
	}

	public boolean isUserRegistered() {
		return pref.getBoolean(KEY_REGISTER, false);
	}

	public void UserIsRegistered() {
		editor.putBoolean(KEY_REGISTER, true);
		editor.commit();
	}
}
