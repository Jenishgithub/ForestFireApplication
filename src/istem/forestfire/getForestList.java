package istem.forestfire;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hp on 6/22/2015.
 */
public class getForestList {
	private String str1;
	private Context mContext;

	public getForestList(Context mContext) {
		this.mContext = mContext;
	}

	public ArrayList<POI> getPointsFromJSON() {
		ArrayList<POI> localArrayList = new ArrayList<POI>();
		InputStream stream = null;
		int size = 0;
		try {
			stream = mContext.getAssets().open("forests.json");
			size = stream.available();
			byte[] buffer = new byte[size];
			stream.read(buffer);
			stream.close();
			String paramString = new String(buffer);

			JSONArray localJSONArray = new JSONObject(paramString)
					.getJSONArray("features");
			for (int i = 0;; i++) {
				if (i >= localJSONArray.length())
					return localArrayList;
				JSONObject localJSONObject = localJSONArray.getJSONObject(i);
				str1 = localJSONObject.getJSONObject("properties").optString(
						"NAME", "Unknown");
				if (localJSONObject.getJSONObject("geometry").getString("type")
						.equals("Point")) {
					localArrayList.add(new POI(Double
							.parseDouble(localJSONObject
									.getJSONObject("geometry")
									.getJSONArray("coordinates").getString(1)),
							Double.parseDouble(localJSONObject
									.getJSONObject("geometry")
									.getJSONArray("coordinates").getString(0)),
							str1));
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return localArrayList;
	}

	public List<String> getForestListfromArrayList(ArrayList<POI> poi) {
		List<String> all_forest = new ArrayList<String>();
		for (int i = 0; i < poi.size(); i++) {
			all_forest.add(poi.get(i).getForestName());
		}
		return all_forest;
	};
}
