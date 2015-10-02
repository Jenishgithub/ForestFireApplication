package istem.forestfire;

import java.util.ArrayList;
import java.util.List;
import org.mapsforge.map.android.view.MapView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ForestNameDateAdapter extends ArrayAdapter<ForestNameDate> {
	Context mContext;
	List<ForestNameDate> forestNameDate = new ArrayList<ForestNameDate>();
	MapView mv;
	ArrayList<POI> sortedReports = new ArrayList<POI>();
	PopupWindow popupWindow;
	LinearLayout llforestFireList;

	public ForestNameDateAdapter(Context context, int resource,
			List<ForestNameDate> objects, MapView mv,
			ArrayList<POI> sortedReports, PopupWindow popupWindow) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		mContext = context;
		forestNameDate = objects;
		this.mv = mv;
		this.sortedReports = sortedReports;
		this.popupWindow = popupWindow;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View v = convertView;

		LayoutInflater li = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = li.inflate(R.layout.forestnamedateadapter, null);
		ForestNameDate singleForestName = forestNameDate.get(position);
		TextView tvForestNameFirst = (TextView) v
				.findViewById(R.id.tvForestNameFirst);
		TextView tvForestDateSecond = (TextView) v
				.findViewById(R.id.tvForestDateSecond);
		llforestFireList = (LinearLayout) v.findViewById(R.id.llforestFireList);

		tvForestNameFirst.setText(singleForestName.forestName);
		tvForestDateSecond.setText(singleForestName.forestFireDate);
		llforestFireList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				llforestFireList = (LinearLayout) v
						.findViewById(R.id.llforestFireList);

				mv.getModel().mapViewPosition.setCenter(sortedReports.get(
						position).getLatLng());
				mv.getModel().mapViewPosition.setZoomLevel((byte) 15);
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					ViewOnMapActivity.isReportsClicked = false;
				}

			}
		});

		return v;
	}
}
