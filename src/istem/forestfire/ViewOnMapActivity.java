package istem.forestfire;

import static android.view.View.GONE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.rendertheme.AssetsRenderTheme;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapDataStore;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Hp on 6/19/2015.
 */
@SuppressLint("ShowToast")
public class ViewOnMapActivity extends ActionBarActivity {
	private MapView mv;
	private ArrayList<TileCache> tileCache = new ArrayList<TileCache>();
	private TileRendererLayer tileRendererLayer;
	private String file_url = DownloadZipfile.file_url;
	private Toast toast;
	private FileCache fileCache;
	private ArrayList<POI> previous_reports = new ArrayList<POI>();
	private TappableMarker all_marker;
	private ScrollView click_view;
	private EditText vw_forest_name;
	private EditText vw_area_affected;
	private EditText vw_cause_of_fire;
	private EditText vw_density;
	private EditText vw_dt_current_status;
	private EditText vw_dt_fire_size;
	private EditText vw_dt_fire_type;
	private EditText vw_no_of_houses_affected;
	private EditText vw_no_of_peoples_affected;
	private EditText vw_start_date;
	private EditText vw_dt_regneration;
	private ImageButton refresh_loc;
	private AutoCompleteTextView autofill_forest;
	private ArrayList<POI> forest_data = new ArrayList<POI>();
	private List<String> forest_name = new ArrayList<String>();
	private TileRendererLayer tileRendererLayer2;
	private TappableMarker my_marker;
	private Button reports;
	private ArrayList<POI> sortedReports = new ArrayList<POI>();
	private EditText vw_how_extinguished;
	private EditText vw_cfug_arrival_time;
	private EditText vw_fire_extinguish_time;
	private EditText vw_distance_from_fire;
	private EditText vw_no_of_trees_burnt;
	private EditText vw_other_institutions;
	private EditText vw_potential_impact_in_water;
	private EditText vw_area_damaged;
	private EditText vw_burnt_area_feature;
	private EditText vw_potential_impact_in_soil;
	private EditText vw_fire_start_from;
	private EditText vw_other_remarks;
	private EditText vw_how_not_extinguished;
	private EditText vw_damage_of_ntft;
	private EditText vw_fire_time;
	private EditText vw_lesson_for_future;
	private EditText vw_triggers;
	private EditText vw_use_of_water;
	private EditText vw_damage_of_wildlife;
	private EditText vw_equipments_brought;
	private EditText vw_no_cfug_people_mobilized;
	private TextView vw_damage_of_wildlife_title;
	private TextView vw_lesson_for_future_title;
	private TextView vw_potential_impact_in_water_title;
	private TextView vw_damage_of_ntft_title;
	private TextView vw_potential_impact_in_soil_title;
	private TextView vw_other_remarks_title;
	private TextView vw_how_not_extinguished_title;
	private TextView vw_how_extinguished_title;
	LayoutInflater layoutInflater;
	ListView lvNewReports;
	List<ForestNameDate> forestNameDate;
	ForestNameDateAdapter forestNameDateAdapter;
	public static boolean isReportsClicked = false;
	PopupWindow popupWindow;
	View popupView;
	AlertDialog alertDialogReport;

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidGraphicFactory.createInstance(this.getApplication());
		setContentView(R.layout.activity_map);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon);
		getSupportActionBar().setTitle("View Reports");

		toast = Toast.makeText(ViewOnMapActivity.this, "", Toast.LENGTH_SHORT);
		click_view = (ScrollView) findViewById(R.id.click_view);
		vw_forest_name = (EditText) findViewById(R.id.vw_forest_name);
		vw_area_affected = (EditText) findViewById(R.id.vw_area_affected);
		vw_cause_of_fire = (EditText) findViewById(R.id.vw_cause_of_fire);
		vw_density = (EditText) findViewById(R.id.vw_density);
		vw_dt_current_status = (EditText) findViewById(R.id.vw_dt_current_status);
		vw_dt_fire_size = (EditText) findViewById(R.id.vw_dt_fire_size);
		vw_dt_fire_type = (EditText) findViewById(R.id.vw_dt_fire_type);
		vw_dt_regneration = (EditText) findViewById(R.id.vw_dt_regneration);
		vw_no_of_houses_affected = (EditText) findViewById(R.id.vw_no_of_houses_affected);
		vw_no_of_peoples_affected = (EditText) findViewById(R.id.vw_no_of_peoples_affected);
		vw_start_date = (EditText) findViewById(R.id.vw_start_date);

		vw_burnt_area_feature = (EditText) findViewById(R.id.vw_burnt_area_feature);
		vw_other_institutions = (EditText) findViewById(R.id.vw_other_institutions);
		vw_no_of_trees_burnt = (EditText) findViewById(R.id.vw_no_of_trees_burnt);
		vw_distance_from_fire = (EditText) findViewById(R.id.vw_distance_from_fire);
		vw_fire_extinguish_time = (EditText) findViewById(R.id.vw_fire_extinguish_time);
		vw_cfug_arrival_time = (EditText) findViewById(R.id.vw_cfug_arrival_time);

		vw_fire_start_from = (EditText) findViewById(R.id.vw_fire_start_from);
		vw_area_damaged = (EditText) findViewById(R.id.vw_area_damaged);
		vw_use_of_water = (EditText) findViewById(R.id.vw_use_of_water);
		vw_fire_time = (EditText) findViewById(R.id.vw_fire_time);
		vw_no_cfug_people_mobilized = (EditText) findViewById(R.id.vw_no_cfug_people_mobilized);
		vw_triggers = (EditText) findViewById(R.id.vw_triggers);
		vw_equipments_brought = (EditText) findViewById(R.id.vw_equipments_brought);

		vw_damage_of_wildlife = (EditText) findViewById(R.id.vw_damage_of_wildlife);
		vw_damage_of_ntft = (EditText) findViewById(R.id.vw_damage_of_ntft);
		vw_potential_impact_in_water = (EditText) findViewById(R.id.vw_potential_impact_in_water);
		vw_potential_impact_in_soil = (EditText) findViewById(R.id.vw_potential_impact_in_soil);
		vw_how_extinguished = (EditText) findViewById(R.id.vw_how_extinguished);
		vw_how_not_extinguished = (EditText) findViewById(R.id.vw_how_not_extinguished);
		vw_other_remarks = (EditText) findViewById(R.id.vw_other_remarks);
		vw_lesson_for_future = (EditText) findViewById(R.id.vw_lesson_for_future);

		vw_damage_of_wildlife_title = (TextView) findViewById(R.id.vw_damage_of_wildlife_title);
		vw_damage_of_ntft_title = (TextView) findViewById(R.id.vw_damage_of_ntft_title);
		vw_potential_impact_in_water_title = (TextView) findViewById(R.id.vw_potential_impact_in_water_title);
		vw_potential_impact_in_soil_title = (TextView) findViewById(R.id.vw_potential_impact_in_soil_title);
		vw_how_extinguished_title = (TextView) findViewById(R.id.vw_how_extinguished_title);
		vw_how_not_extinguished_title = (TextView) findViewById(R.id.vw_how_not_extinguished_title);
		vw_other_remarks_title = (TextView) findViewById(R.id.vw_other_remarks_title);
		vw_lesson_for_future_title = (TextView) findViewById(R.id.vw_lesson_for_future_title);

		reports = (Button) findViewById(R.id.reports_quick);

		layoutInflater = (LayoutInflater) getBaseContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		popupView = layoutInflater.inflate(R.layout.newreports, null);
		lvNewReports = (ListView) popupView.findViewById(R.id.lvNewReports);
		reports.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// view.showContextMenu();
				if (isReportsClicked == false) {
					fileCache = new FileCache(ViewOnMapActivity.this);
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							ViewOnMapActivity.this);

					// set title
					alertDialogBuilder.setTitle("Reports");

					// set dialog message
					alertDialogBuilder
							// .setMessage("Choose!")
							.setCancelable(false)
							.setPositiveButton("Check new reports",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {

											if (fileCache
													.haveNetworkConnection()) {
												Log.i("from", "database");
												getPreviousReportsFromdatabase();

											} else {
												toaster("No internet connection");
											}
										}
									})
							.setNegativeButton("View Reports",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {

											previous_reports = fileCache
													.getAllPreviousReports();
											if (previous_reports != null) {
												showOnMap();
											}
											showPopupWindow();

										}
									});

					alertDialogReport = alertDialogBuilder.create();

					alertDialogReport.show();

					alertDialogReport.setCancelable(true);
					alertDialogBuilder.setCancelable(true);

				} else if (isReportsClicked == true && popupWindow != null
						&& popupWindow.isShowing()) {
					popupWindow.dismiss();
					isReportsClicked = false;
				}
			}
		});

		autofill_forest = (AutoCompleteTextView) findViewById(R.id.view_auto_search);

		getForestList all_forest = new getForestList(ViewOnMapActivity.this);
		this.forest_data = all_forest.getPointsFromJSON();
		this.forest_name = all_forest.getForestListfromArrayList(forest_data);

		ArrayAdapter<String> forest_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, forest_name) {
			@Override
			public Filter getFilter() {
				return super.getFilter();
			}

		};
		autofill_forest.setAdapter(forest_adapter);

		autofill_forest
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long l) {
						int index = forest_name.indexOf(autofill_forest
								.getText().toString());
						mv.getModel().mapViewPosition.setCenter(forest_data
								.get(index).getLatLng());
						mv.getModel().mapViewPosition.setZoomLevel((byte) 16);
						setKeyboardVisibility(false);
					}
				});

		refresh_loc = (ImageButton) findViewById(R.id.view_refresh_loc);
		mv = (MapView) findViewById(R.id.mapView);
		mv.setClickable(true);
		mv.setBuiltInZoomControls(true);
		this.mv.getMapZoomControls().setZoomLevelMax((byte) 20);
		this.tileCache.add(AndroidUtil.createTileCache(this, "mapcache",
				mv.getModel().displayModel.getTileSize(), 1f,
				this.mv.getModel().frameBufferModel.getOverdrawFactor()));
		this.tileCache.add(AndroidUtil.createTileCache(this, "mapcache1",
				mv.getModel().displayModel.getTileSize(), 1f,
				this.mv.getModel().frameBufferModel.getOverdrawFactor()));

	}

	private ArrayList<POI> sortPOI(ArrayList<POI> previous_reports) {
		ArrayList<POI> sort = new ArrayList<POI>();
		sort = previous_reports;

		Collections.sort(sort);
		return sort;
	}

	@SuppressWarnings("unused")
	private int compareTime(String fireTime, String fireTime1) {
		int time1 = getTimeValue(fireTime);
		int time2 = getTimeValue(fireTime1);

		if (time1 < time2)
			return -1;
		else if (time1 == time2)
			return 0;
		else if (time1 > time2)
			return 1;
		return 0;
	}

	private int getTimeValue(String fireTime) {
		if (fireTime.equalsIgnoreCase("Morning Hour"))
			return 1;
		else if (fireTime.equalsIgnoreCase("Day Hour"))
			return 2;
		else if (fireTime.equalsIgnoreCase("Evening Hour"))
			return 3;
		else if (fireTime.equalsIgnoreCase("Night Hour"))
			return 4;
		return 0;
	}

	public void setKeyboardVisibility(boolean show) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (show) {
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		} else {
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

	private void loadMap() {
		HashMap<String, String> old_location = new SessionManager(
				getApplicationContext()).getLocationDetails();
		final String old_lat = old_location.get(SessionManager.KEY_LAT);
		final String old_lgt = old_location.get(SessionManager.KEY_LGT);
		final int old_zoom = new SessionManager(ViewOnMapActivity.this)
				.getZoomLevel();
		mv.getModel().mapViewPosition.setZoomLevel((byte) 14);
		mv.getModel().mapViewPosition.setCenter(new LatLong(Double
				.valueOf(old_lat), Double.valueOf(old_lgt)));

		MapDataStore mapDataStore = new MapFile(ReportActivity.getMapFile());
		tileRendererLayer = new TileRendererLayer(tileCache.get(0),
				mapDataStore, mv.getModel().mapViewPosition, false, true,
				AndroidGraphicFactory.INSTANCE) {
			@Override
			public boolean onLongPress(LatLong tapLatLong, Point layerXY,
					Point tapXY) {
				click_view.setVisibility(GONE);
				return super.onLongPress(tapLatLong, layerXY, tapXY);
			}

			@Override
			public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
				click_view.setVisibility(GONE);
				return super.onTap(tapLatLong, layerXY, tapXY);
			}
		};
		tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
		mv.getLayerManager().getLayers().add(tileRendererLayer);
		MapDataStore mapDataStore2 = new MapFile(ReportActivity.getMapFile2());

		tileRendererLayer2 = new TileRendererLayer(this.tileCache.get(1),
				mapDataStore2, mv.getModel().mapViewPosition, true, true,
				AndroidGraphicFactory.INSTANCE) {
			@Override
			public boolean onLongPress(LatLong tapLatLong, Point layerXY,
					Point tapXY) {
				click_view.setVisibility(GONE);
				return super.onLongPress(tapLatLong, layerXY, tapXY);
			}

			@Override
			public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
				click_view.setVisibility(GONE);
				return super.onTap(tapLatLong, layerXY, tapXY);
			}
		};
		try {
			XmlRenderTheme renderTheme = new AssetsRenderTheme(this, "",
					"myrenderer.xml");
			tileRendererLayer2.setXmlRenderTheme(renderTheme);
		} catch (IOException e) {
			tileRendererLayer2
					.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
			e.printStackTrace();
		}
		mv.getLayerManager().getLayers().add(tileRendererLayer2);

		this.mv.getModel().mapViewPosition.setCenter(new LatLong(Double
				.valueOf(old_lat), Double.valueOf(old_lgt)));
		this.mv.getModel().mapViewPosition.setZoomLevel((byte) old_zoom);

		refresh_loc.setOnClickListener(new View.OnClickListener() {
			public GPSTracker gps_conn;

			@Override
			public void onClick(View view) {
				gps_conn = new GPSTracker(ViewOnMapActivity.this);
				if (gps_conn.canGetLocation()) {
					if (gps_conn.getLatitude() == 0
							&& gps_conn.getLongitude() == 0)
						toaster("Waiting For Location");
					else
						mv.getModel().mapViewPosition.setCenter(new LatLong(
								gps_conn.getLatitude(), gps_conn.getLongitude()));
				} else {
					toaster("Waiting For Location");
				}

			}
		});

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras.containsKey("latitude")
					&& extras.containsKey("longitude")) {
				mv.getModel().mapViewPosition.setZoomLevel((byte) 16);
				mv.getModel().mapViewPosition.setCenter(new LatLong(Double
						.valueOf(extras.getString("latitude")), Double
						.valueOf(extras.getString("longitude"))));
				this.my_marker = new TappableMarker(ViewOnMapActivity.this,
						R.mipmap.markericon, new LatLong(Double.valueOf(extras
								.getString("latitude")), Double.valueOf(extras
								.getString("longitude"))));
				mv.getLayerManager().getLayers().add(my_marker);

			}
		}
		toaster2("Long Press on Marker to View Details");

	}

	private void toaster2(String s) {
		Toast.makeText(ViewOnMapActivity.this, s, Toast.LENGTH_LONG).show();
		Toast.makeText(ViewOnMapActivity.this, s, Toast.LENGTH_LONG).show();

	}

	private void showOnMap() {
		Log.i("forest_fire", previous_reports.size() + "");
		for (int i = 0; i < previous_reports.size(); i++) {
			createReportsMarker(previous_reports.get(i).getLatLng(),
					previous_reports.get(i));
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (new File(ReportActivity.MAPFILE).exists()
				&& new File(ReportActivity.MAPFILE1).exists()) {
			this.mv.getLayerManager().getLayers()
					.remove(this.tileRendererLayer);
			this.mv.getLayerManager().getLayers()
					.remove(this.tileRendererLayer2);
			this.tileRendererLayer.onDestroy();
			this.tileRendererLayer2.onDestroy();
			this.tileRendererLayer2.onDestroy();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.tileCache.get(0).destroy();
		this.tileCache.get(1).destroy();
		this.mv.getModel().mapViewPosition.destroy();
		this.mv.destroy();
		AndroidGraphicFactory.clearResourceMemoryCache();
	}

	private void createReportsMarker(LatLong latLng, final POI localPOI) {
		this.all_marker = new TappableMarker(ViewOnMapActivity.this,
				R.drawable.red, latLng, "") {
			@Override
			public boolean onLongPress(LatLong tapLatLong, Point layerXY,
					Point tapXY) {
				double centerX = layerXY.x + getHorizontalOffset();
				double centerY = layerXY.y + getVerticalOffset();

				double radiusX = (getBitmap().getWidth() / 2) * 1.1;
				double radiusY = (getBitmap().getHeight() / 2) * 1.1;

				double distX = Math.abs(centerX - tapXY.x);
				double distY = Math.abs(centerY - tapXY.y);

				if (distX < radiusX && distY < radiusY) {
					vw_forest_name.setText(localPOI.getForestName());
					vw_area_affected.setText(localPOI.getArea());
					vw_cause_of_fire.setText(localPOI.getCause());
					vw_density.setText(localPOI.getDensity());
					vw_dt_current_status.setText(localPOI.getStatus());
					vw_dt_fire_size.setText(localPOI.getFireSize());
					vw_dt_fire_type.setText(localPOI.getforest_fire_type());
					vw_no_of_houses_affected.setText(localPOI
							.getNoOfAffectedHouses());
					vw_no_of_peoples_affected.setText(localPOI
							.getNoOfAffectedPeoples());

					if (localPOI.getHowExtinguished().equals("")) {
						vw_how_extinguished.setVisibility(View.GONE);
						vw_how_extinguished_title.setVisibility(View.GONE);
					} else {
						vw_how_extinguished.setText(localPOI
								.getHowExtinguished());
						vw_how_extinguished.setVisibility(View.VISIBLE);
						vw_how_extinguished_title.setVisibility(View.VISIBLE);

					}
					if (localPOI.getHowNotExtinguished().equals("")) {
						vw_how_not_extinguished.setVisibility(View.GONE);
						vw_how_not_extinguished_title.setVisibility(View.GONE);
					} else {
						vw_how_not_extinguished.setText(localPOI
								.getHowNotExtinguished());
						vw_how_not_extinguished.setVisibility(View.VISIBLE);
						vw_how_not_extinguished_title
								.setVisibility(View.VISIBLE);
					}
					if (localPOI.getDamageOfWildLife().equals("")) {
						vw_damage_of_wildlife.setVisibility(View.GONE);
						vw_damage_of_wildlife_title.setVisibility(View.GONE);
					} else {
						vw_damage_of_wildlife.setText(localPOI
								.getDamageOfWildLife());
						vw_damage_of_wildlife.setVisibility(View.VISIBLE);
						vw_damage_of_wildlife_title.setVisibility(View.VISIBLE);
					}

					if (localPOI.getdamageOfNtft().equals("")) {
						vw_damage_of_ntft.setVisibility(View.GONE);
						vw_damage_of_ntft_title.setVisibility(View.GONE);
					} else {
						vw_damage_of_ntft.setText(localPOI.getdamageOfNtft());
						vw_damage_of_ntft.setVisibility(View.VISIBLE);
						vw_damage_of_ntft_title.setVisibility(View.VISIBLE);
					}

					if (localPOI.getOtherRemarks().equals("")) {
						vw_other_remarks.setVisibility(View.GONE);
						vw_other_remarks_title.setVisibility(View.GONE);
					} else {
						vw_other_remarks.setText(localPOI.getOtherRemarks());
						vw_other_remarks.setVisibility(View.VISIBLE);
						vw_other_remarks_title.setVisibility(View.VISIBLE);
					}

					if (localPOI.getLessonForFuture().equals("")) {
						vw_lesson_for_future.setVisibility(View.GONE);
						vw_lesson_for_future_title.setVisibility(View.GONE);
					} else {
						vw_lesson_for_future.setText(localPOI
								.getLessonForFuture());
						vw_lesson_for_future.setVisibility(View.VISIBLE);
						vw_lesson_for_future_title.setVisibility(View.VISIBLE);
					}

					if (localPOI.getImpactInWater().equals("")) {
						vw_potential_impact_in_water.setVisibility(View.GONE);
						vw_potential_impact_in_water_title
								.setVisibility(View.GONE);
					} else {
						vw_potential_impact_in_water.setText(localPOI
								.getImpactInWater());
						vw_potential_impact_in_water
								.setVisibility(View.VISIBLE);
						vw_potential_impact_in_water_title
								.setVisibility(View.VISIBLE);
					}

					if (localPOI.getimpactInSoil().equals("")) {
						vw_potential_impact_in_soil.setVisibility(View.GONE);
						vw_potential_impact_in_soil_title
								.setVisibility(View.GONE);
					} else {
						vw_potential_impact_in_soil.setText(localPOI
								.getimpactInSoil());
						vw_potential_impact_in_soil.setVisibility(View.VISIBLE);
						vw_potential_impact_in_soil_title
								.setVisibility(View.VISIBLE);
					}

					vw_cfug_arrival_time.setText(localPOI.getCfugArrivalTime());
					vw_fire_extinguish_time.setText(localPOI
							.getFireExtinguishTime());
					vw_distance_from_fire.setText(localPOI
							.getDistanceFromFire());
					vw_no_of_trees_burnt.setText(localPOI.getNoOfTreesBurnt());
					vw_other_institutions.setText(localPOI
							.getOtherInstitutions());
					vw_area_damaged.setText(localPOI.getAreaDamaged());
					vw_burnt_area_feature.setText(localPOI
							.getBurntAreaFeature());
					vw_fire_start_from.setText(localPOI.getFireStartFrom());
					vw_fire_time.setText(localPOI.getFireTime());
					vw_triggers.setText(localPOI.getTriggers());
					vw_use_of_water.setText(localPOI.getUseOfWater());
					vw_equipments_brought.setText(localPOI
							.getEquipmentsBrought());
					vw_no_cfug_people_mobilized.setText(localPOI
							.getNoCFUGPeopelMobilized());

					if (!localPOI.getForestFireDate().equals("null"))
						vw_start_date.setText(localPOI.getForestFireDate());
					vw_dt_regneration.setText(localPOI.getRegeneration());
					click_view.setVisibility(View.VISIBLE);
					return true;
				}
				return false;
			}

			@Override
			public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
				double centerX = layerXY.x + getHorizontalOffset();
				double centerY = layerXY.y + getVerticalOffset();

				double radiusX = (getBitmap().getWidth() / 2) * 1.1;
				double radiusY = (getBitmap().getHeight() / 2) * 1.1;

				double distX = Math.abs(centerX - tapXY.x);
				double distY = Math.abs(centerY - tapXY.y);

				if (distX < radiusX && distY < radiusY) {
					toaster(localPOI.getForestName());
					return true;
				}
				return false;
			}
		};
		mv.getLayerManager().getLayers().add(all_marker);

	}

	private void getPreviousReportsFromdatabase() {
		StringReceiver connect = new StringReceiver(ViewOnMapActivity.this,
				new AsyncResponseData() {

					public void processFinish(String output) {
						try {
							if (!new JSONArray(output).getJSONObject(0).has(
									"error")) {
								fileCache.renewAllReportsData(output);

								previous_reports = fileCache
										.getAllPreviousReports();
								if (previous_reports != null) {
									showOnMap();

								}
							} else {
								previous_reports = fileCache
										.getAllPreviousReports();
								if (previous_reports != null) {
									showOnMap();
								}

							}
						} catch (JSONException e) {
							toaster("Error Downoading Data");

							previous_reports = fileCache
									.getAllPreviousReports();
							if (previous_reports != null) {
								showOnMap();
							}
						}
						showOnMap();
						showPopupWindow();
					}
				});
		connect.setPath(SavedReportsActivity.RECEIVE_PREVIOUS_REPORT_PATH);
		connect.execute(new Void[0]);
	}

	@SuppressWarnings("unused")
	private File getMapFile() {
		File file = new File(ReportActivity.MAPFILE);
		return file;
	}

	protected void onStart() {
		super.onStart();
		if (new File(ReportActivity.MAPFILE).exists()
				&& new File(ReportActivity.MAPFILE1).exists()) {
			loadMap();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Do You Want To Download Files Now?").setTitle(
					"Download Support Files");
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							DownloadZipfile mew = new DownloadZipfile(
									ViewOnMapActivity.this,
									new AsyncResponse() {
										@Override
										public void processFinish(Boolean output) {
											if (output)
												loadMap();
											else
												toaster("Error");
										}
									});
							mew.execute(file_url);
						}
					});
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							finish();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}
	}

	private void toaster(String message) {
		toast.setText(message);
		toast.show();
	}

	@Override
	public void onBackPressed() {
		if (isReportsClicked == true && popupWindow != null
				&& popupWindow.isShowing()) {
			popupWindow.dismiss();
			isReportsClicked = false;
		} else {
			new SessionManager(ViewOnMapActivity.this)
					.createLocationSession(
							String.valueOf(mv.getModel().mapViewPosition
									.getCenter().latitude),
							String.valueOf(mv.getModel().mapViewPosition
									.getCenter().longitude));
			new SessionManager(ViewOnMapActivity.this).storeZoomLevel(mv
					.getModel().mapViewPosition.getZoomLevel());
			super.onBackPressed();
		}

	}

	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	public void showPopupWindow() {
		sortedReports = sortPOI(previous_reports);
		forestNameDate = new ArrayList<ForestNameDate>();
		if (sortedReports.size() > 0) {
			int i = 0;
			while (i < sortedReports.size()) {
				forestNameDate.add(new ForestNameDate(sortedReports.get(i)
						.getForestName(), "  "
						+ sortedReports.get(i).getForestFireDate()));
				i++;
				if (i == 15)
					break;
			}
		}
		popupWindow = new PopupWindow(popupView, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);

		forestNameDateAdapter = new ForestNameDateAdapter(
				getApplicationContext(), R.layout.forestnamedateadapter,
				forestNameDate, mv, sortedReports, popupWindow);

		lvNewReports.setAdapter(forestNameDateAdapter);

		popupWindow.showAtLocation(reports, Gravity.CENTER, 0, 0);

		isReportsClicked = true;
	}
}