package istem.forestfire;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import istem.forestfire.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressLint("NewApi")
public class FileCache extends ActionBarActivity {
	private File cacheDir;
	private String file_name = "notification";
	Context mContext;
	String port = "";
	private JSONObject previous_json;
	private PrintWriter pw;

	public FileCache(Context context) {
		this.mContext = context;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					".iStem/.cache");
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	public File getFile(String filename) {
		File f = new File(cacheDir, filename);
		return f;
	}

	public File getdir() {
		return cacheDir;
	}

	public ArrayList<File> getallFiles(final String type) {
		File[] allfiles;
		ArrayList<File> allFiles_arrlist = new ArrayList<File>();
		File dir = cacheDir;
		FileFilter filter = new FileFilter() {
			public boolean accept(File file) {
				return file.getAbsolutePath().matches(".*\\." + type);
			}
		};

		// gets a list of files in the directory represented by this file. This
		// list is then filtered through a FilenameFilter and files with
		// matching names are as an array of files.
		allfiles = dir.listFiles(filter);
		for (int i = 0; i < allfiles.length; i++) {
			allFiles_arrlist.add(allfiles[i]);
		}
		return allFiles_arrlist;
	}

	public static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		reader.close();
		return sb.toString();
	}

	public static String getStringFromFile(String filePath) {
		File fl = new File(filePath);
		String ret = null;
		try {
			FileInputStream fin = new FileInputStream(fl);
			ret = convertStreamToString(fin);
			// Make sure you close all streams.
			fin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public String getFileHeader(File file) {
		String header = null;
		String json = getStringFromFile(file.getAbsolutePath());
		try {
			header = new JSONObject(json).getJSONObject("ForestFire")
					.getString("forest_name");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return header;
	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		for (File f : files)
			f.delete();
	}

	public void deletefile(String file_name) {
		File[] files = cacheDir.listFiles();
		for (File f : files)
			if (f.getName().equals(file_name))
				f.delete();
	}

	public void storeFile(String json) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			try {
				int i = 1;
				JSONObject report = new JSONObject(json);
				file_name = (report.getJSONObject("ForestFire"))
						.getString("UUID");
				File root = new File(cacheDir, file_name + ".txt");
				while (root.exists()) {
					// root.renameTo(new File(cacheDir,file_name+i+".txt"));
					root = new File(cacheDir, file_name + i + ".txt");
					i = i + 1;
				}

				// contents of detail report are initially stored in file before
				// uploading to the server
				FileWriter writer = new FileWriter(root);
				writer.append(json);
				writer.flush();
				writer.close();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
		}

	}

	public void toaster(String s) {
		Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
	}

	public boolean upload_status;

	@SuppressWarnings("deprecation")
	public void showerrordialog(String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
		alertDialog.setTitle("Error on Report of " + title);
		alertDialog.setMessage(message);
		alertDialog.setIcon(R.drawable.abc_btn_check_material);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int which) {

			}
		});
		alertDialog.show();
	}

	@SuppressWarnings("deprecation")
	public void showcreateddialog(String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
		alertDialog.setTitle("Success");
		alertDialog.setMessage(message);
		alertDialog.setIcon(R.drawable.abc_ab_share_pack_holo_light);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int which) {
				/*
				 * Intent intent=new
				 * Intent(getApplicationContext(),Reporting.class);
				 * startActivity(intent); finish();
				 */}
		});
		alertDialog.show();
	}

	public JSONArray getNotifications() {
		File notifyDir = new File(cacheDir + "/notification");
		if (!notifyDir.exists())
			notifyDir.mkdirs();
		File root = new File(notifyDir, file_name + ".txt");
		String allNotification = getStringFromFile(root.getPath());
		Log.i("notify", allNotification + "");
		JSONArray notificationArray = null;
		if (allNotification == null) {
			notificationArray = new JSONArray();
		} else {
			try {
				notificationArray = new JSONArray(allNotification);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return notificationArray;
	}

	public void storeNotification(String title, String message, int j,
			String state) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			try {
				Log.i("notify", "called");
				String currentdate = getDate();
				File notifyDir = new File(cacheDir + "/notification");
				if (!notifyDir.exists())
					notifyDir.mkdirs();
				File root = new File(notifyDir, file_name + ".txt");
				String allNotification = getStringFromFile(root.getPath());
				Log.i("notify", allNotification + "");
				JSONArray notificationArray;
				if (allNotification == null) {
					notificationArray = new JSONArray();
					JSONObject notification = new JSONObject();
					notification.put("id", j);
					notification.put("title", title);
					notification.put("message", message);
					notification.put("date", currentdate);
					notification.put("state", state);
					notificationArray.put(notification);
				} else {
					notificationArray = new JSONArray(allNotification);
					int i;
					for (i = 0; i < notificationArray.length(); i++) {
						if (notificationArray.getJSONObject(i).getInt("id") == j) {
							if (!notificationArray.getJSONObject(i)
									.getString("state").equals(state)) {
								notificationArray.getJSONObject(i).put("id", j);
								notificationArray.getJSONObject(i).put("title",
										title);
								notificationArray.getJSONObject(i).put(
										"message", message);
								notificationArray.getJSONObject(i).put("date",
										currentdate);
								notificationArray.getJSONObject(i).put("state",
										state);
								notificationArray.getJSONObject(i).put(
										"opened", false);
							}
						}
					}

					if (i == notificationArray.length()) {
						JSONObject notification = new JSONObject();
						notification.put("id", j);
						notification.put("title", title);
						notification.put("message", message);
						notification.put("date", currentdate);
						notification.put("state", state);
						notification.put("opened", false);
						notificationArray.put(notification);
					}
				}
				String newAllNotification = notificationArray.toString();
				try {
					FileWriter writer = new FileWriter(root);
					writer.append(newAllNotification);
					writer.flush();
					writer.close();
				} catch (IOException e) {
					Log.e("error", e.toString());
				}
			} catch (Exception e) {
				Log.e("error", e.toString());
				e.printStackTrace();
			}
		} else {
			Log.e("error", "error");
		}

	}

	public String getDate() {
		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		StringBuilder date;
		if (mMonth >= 9) {
			if (mDay >= 10) {
				date = new StringBuilder().append(mDay).append("/")
						.append(mMonth + 1).append("/").append(mYear);
			} else {
				date = new StringBuilder().append("0" + mDay).append("/")
						.append(mMonth + 1).append("/").append(mYear);
			}
		} else {
			if (mDay >= 10) {
				date = new StringBuilder().append(mDay).append("/")
						.append("0" + (mMonth + 1)).append("/").append(mYear);
			} else {
				date = new StringBuilder().append("0" + mDay).append("/")
						.append("0" + (mMonth + 1)).append("/").append(mYear);
			}
		}

		return date.toString();
	}

	public void replacenotification(JSONArray notesJSONArray) {
		PrintWriter pw = null;
		File notifyDir = new File(cacheDir + "/notification");
		if (!notifyDir.exists())
			notifyDir.mkdirs();
		File root = new File(notifyDir, file_name + ".txt");
		try {
			pw = new PrintWriter(root);
			pw.write(notesJSONArray.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		pw.close();
	}

	@SuppressWarnings("unused")
	public void deletenotification(String title, String message) {
		try {
			JSONArray jsonArray = getNotifications();
			JSONArray list = new JSONArray();
			int len = jsonArray.length();
			for (int i = 0; i < jsonArray.length(); i++) {
				if ((jsonArray.getJSONObject(i).getString("title")
						.equals(title))
						&& (jsonArray.getJSONObject(i).getString("message")
								.equals(message))) {
				} else {
					list.put(jsonArray.getJSONObject(i));
				}

			}
			replacenotification(list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getFileParameter(File file, String param) {
		String header = null;
		String json = getStringFromFile(file.getAbsolutePath());
		try {
			header = new JSONObject(json).getJSONObject("ForestFire")
					.getString(param);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return header;
	}

	public String getFileParameterfromName(String file_name, String param) {
		String header = null;
		File file = new File(cacheDir, file_name);
		String json = getStringFromFile(file.getAbsolutePath());
		try {
			header = new JSONObject(json).getJSONObject("ForestFire")
					.getString(param);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return header;

	}

	public void replaceFile(String new_data, String prev_file_name) {
		PrintWriter pw = null;
		JSONObject new_json;
		File notifyDir = new File(cacheDir, prev_file_name);
		if (!notifyDir.exists())
			notifyDir.mkdirs();
		String previous_data = getStringFromFile(notifyDir.getPath());
		try {
			previous_json = new JSONObject(previous_data);
			new_json = new JSONObject(new_data);
			previous_json.put("ForestFire",
					new_json.getJSONObject("ForestFire"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			pw = new PrintWriter(notifyDir);
			pw.write(previous_json.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		pw.close();

	}

	public ArrayList<POI> getAllPreviousReports() {
		ArrayList<POI> all_forest_data = new ArrayList<POI>();
		File recordedDir = new File(cacheDir, "records");
		if (!recordedDir.exists())
			recordedDir.mkdirs();
		File recordedFile = new File(recordedDir, "data.txt");
		String allData = getStringFromFile(recordedFile.getPath());
		try {
			if (allData != null) {
				JSONArray all_data_JSONArray = new JSONArray(allData);
				for (int i = 0; i < all_data_JSONArray.length(); i++) {
					JSONObject localjson = (JSONObject) all_data_JSONArray
							.getJSONObject(i);
					POI localPOI = new POI(Double.valueOf(localjson
							.getString("latitude")), Double.valueOf(localjson
							.getString("longitude")),
							localjson.getString("forest_name"));
					localPOI.setArea(localjson.getString("area"));
					localPOI.setCause(localjson.getString("cause"));
					localPOI.setDensity(localjson.getString("density"));
					localPOI.setFireSize(localjson.getString("fire_size"));
					localPOI.setforest_fire_type(localjson
							.getString("fire_type"));
					localPOI.setForestFireDate(localjson.getString("fire_date"));
					localPOI.setId(localjson.getString("id"));
					localPOI.setNoOfAffectedHouses(localjson
							.getString("affected_houses"));
					localPOI.setNoOfAffectedPeoples(localjson
							.getString("affected_people"));
					localPOI.setRegeneration(localjson
							.getString("regeneration"));
					localPOI.setStatus(localjson.getString("status"));

					localPOI.setHowExtinguished(localjson
							.getString("how_extinguished"));
					localPOI.setBurntAreaFeature(localjson
							.getString("burnt_area_feature"));
					localPOI.setOtherInstitutions(localjson
							.getString("other_institutions"));
					localPOI.setNoOfTreesBurnt(localjson
							.getString("no_of_trees_burnt"));
					localPOI.setDistanceFromFire(localjson
							.getString("distance_from_fire"));
					localPOI.setFireExtinguishTime(localjson
							.getString("fire_extinguish_time"));
					localPOI.setCfugArrivalTime(localjson
							.getString("cfug_arrival_time"));
					localPOI.setImpactInWater(localjson
							.getString("impact_in_water"));
					localPOI.setFireStartFrom(localjson
							.getString("fire_start_from"));
					localPOI.setAreaDamaged(localjson.getString("area_damaged"));
					localPOI.setOtherRemarks(localjson
							.getString("other_remarks"));
					localPOI.setHowNotExtinguished(localjson
							.getString("how_not_extinguished"));
					localPOI.setdamageOfNtft(localjson
							.getString("damage_of_ntft"));
					localPOI.setLessonForFuture(localjson
							.getString("lesson_for_future"));
					localPOI.setUseOfWater(localjson.getString("use_of_water"));
					localPOI.setDamageOfWildLife(localjson
							.getString("damage_of_wildlife"));
					localPOI.setFireTime(localjson.getString("fire_time"));
					localPOI.setNoCFUGPeopelMobilized(localjson
							.getString("no_cfug_people_mobilized"));
					localPOI.setTriggers(localjson.getString("triggers"));
					localPOI.setimpactInSoil(localjson
							.getString("impact_in_soil"));
					localPOI.setEquipmentsBrought(localjson
							.getString("equipments_brought"));
					Log.i("localPOI", localPOI.getArea());
					all_forest_data.add(localPOI);
				}
			}
		} catch (JSONException e) {
			Log.e("rec_err", e.toString());
			e.printStackTrace();
		}
		Log.i("no_of_records", all_forest_data.size() + "");
		return all_forest_data;
	}

	public boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	@SuppressWarnings("unused")
	public void renewAllReportsData(String output) {
		File recordedDir = new File(cacheDir, "records");
		if (!recordedDir.exists())
			recordedDir.mkdirs();
		File recordedFile = new File(recordedDir, "data.txt");
		try {
			JSONArray check_data = new JSONArray(output);
			try {
				pw = new PrintWriter(recordedFile);
				pw.write(output);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			pw.close();
		} catch (JSONException e) {
			toaster("Error Data Download");
			e.printStackTrace();
		}

	}
}