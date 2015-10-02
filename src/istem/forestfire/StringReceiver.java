package istem.forestfire;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StringReceiver extends AsyncTask<Void, Void, String> {
	public static String host = "http://atnepal.com";

	private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	private String path;
	private Context mContext = null;
	private String data;
	private String message = "Connecting...";

	ProgressDialog mProgressDialog;
	public AsyncResponseData delegate = null;

	public StringReceiver() {

	}

	public StringReceiver(Context context) {
		this.mContext = context;
		this.mProgressDialog = new ProgressDialog(mContext);
	}

	public StringReceiver(Context context, AsyncResponseData response) {
		this.mContext = context;
		this.mProgressDialog = new ProgressDialog(mContext);
		this.delegate = (AsyncResponseData) response;
	}

	public void setString(String data) {
		this.data = data;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@SuppressWarnings("static-access")
	protected String doInBackground(Void[] paramArrayOfVoid) {
		try {
			String str1 = this.host + this.path;
			DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
			/*
			 * HttpPost localHttpPost = new HttpPost(str1);
			 * localHttpPost.setHeader("Content-Type","application/json");
			 * localHttpPost.setEntity(new StringEntity(data)); String str2 =
			 * EntityUtils
			 * .toString(localDefaultHttpClient.execute(localHttpPost)
			 * .getEntity());
			 */
			HttpPost localHttpPost = new HttpPost(str1);
			nameValuePairs.add(new BasicNameValuePair("json", data));
			localHttpPost.setEntity(new UrlEncodedFormEntity(
					this.nameValuePairs));
			String str2 = EntityUtils.toString(localDefaultHttpClient.execute(
					localHttpPost).getEntity());

			return str2;
		} catch (Exception localException) {
			Log.i("server", localException.toString());
			String server_error = "Server Under Maintainance.Please TryAgain Later";
			String str3 = null;
			try {
				str3 = "["
						+ new JSONObject().put("error", "server_fail")
								.put("message", server_error).toString() + "]";
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return str3;
		}
	}

	@SuppressWarnings("static-access")
	public String getHost() {
		return this.host;
	}

	public List<NameValuePair> getNameValuePairs() {
		return this.nameValuePairs;
	}

	public String getPath() {
		return this.path;
	}

	protected void onPostExecute(String result) {
		delegate.processFinish(result);
		Log.i("pd", "end");
		if (mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		// super.onPostExecute(paramString);
	}

	protected void onProgressUpdate(Void... values) {
		// super.onProgressUpdate(values);
	}

	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(message);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("Loading data...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setInverseBackgroundForced(true);
		mProgressDialog.show();

	}

	@SuppressWarnings("static-access")
	public void setHost(String paramString) {
		this.host = paramString;
	}

	public void setPath(String paramString) {
		this.path = paramString;
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

}
