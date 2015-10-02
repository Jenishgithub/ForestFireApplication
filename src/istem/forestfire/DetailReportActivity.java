package istem.forestfire;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import istem.forestfire.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by Hp on 6/17/2015.
 */
public class DetailReportActivity extends ActionBarActivity {

	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
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
	@SuppressWarnings("unused")
	private String prev_forest_name;
	private String prev_forest_fire_date;
	private String prev_forest_fire_type;
	private String prev_forest_fire_status;
	private String prev_forest_fire_size;
	private String prev_file_name = "";
	private RadioGroup rg_regeneration;
	private RadioButton regenration_option;
	private EditText area_affected;
	private EditText cause_of_fire;
	private String prev_forest_fire_cause;
	private String prev_area_affected;
	private int mhour;
	private int mminute;
	private TimePickerDialog.OnTimeSetListener mTimeSetListener;
	private EditText start_date;
	private EditText no_of_houses_affected;
	private EditText no_of_peoples_affected;
	private String prev_no_of_houses_affected;
	private String prev_no_of_peoples_affected;
	private String prev_regneration_option;
	private RadioGroup rg_forest_density;
	private RadioButton forest_density;
	private String prev_forest_density;
	private ArrayList<POI> forest_data = new ArrayList<POI>();
	private List<String> forest_name_list = new ArrayList<String>();
	private RadioGroup rg_start_time;
	private RadioButton start_time;
	private RadioGroup rg_arrival_time;
	private RadioButton arrival_time;
	private RadioGroup rg_fire_extinguish_time;
	private RadioButton fire_extinguish_time;
	private RadioGroup rg_no_mobilized;
	private RadioButton no_mobilized;
	private RadioGroup rg_use_of_water;
	private RadioButton use_of_water;
	private RadioButton area_damaged;
	private RadioGroup rg_area_damaged;
	private RadioGroup rg_burnt_area_feature;
	private RadioButton burnt_area_feature;
	private RadioButton fire_start_from;
	private RadioGroup rg_fire_start_from;
	private EditText fire_start_from_others;
	private RadioGroup rg_distance_from_fire;
	private RadioButton distance_from_fire;
	private RadioGroup rg_no_of_trees_burnt;
	private RadioButton no_of_trees_burnt;
	private EditText damage_of_wildlife;
	private EditText damage_of_ntft;
	private EditText impact_in_water;
	private EditText impact_in_soil;
	private EditText how_not_extinguished;
	private EditText how_extinguished;
	private EditText lesson_for_future;
	private EditText other_remarks;
	private CheckBox other_institution_dfo;
	private CheckBox equipment_long_stick;
	private CheckBox other_institution_police;
	private CheckBox trigger_human_cause;
	private CheckBox trigger_dry_litters;
	private CheckBox equipment_kuto_kodalo;
	private CheckBox trigger_steep_slope;
	private CheckBox other_institution_army;
	private CheckBox equipment_tree_branch;
	private CheckBox trigger_dry_pine;
	private CheckBox equipment_water;
	private CheckBox trigger_strong_wind;
	private ArrayList<String> other_institutions = new ArrayList<String>();
	private ArrayList<String> triggers = new ArrayList<String>();
	private ArrayList<String> equipment_brought = new ArrayList<String>();
	private String other_institutions_string = "";
	private String triggers_string = "";
	private String equipments_string = "";
	private CheckBox equipment_improved_equipment;
	private String imei_no;

	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_report);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon);

		// forest_name=(AutoCompleteTextView)findViewById(R.id.dt_forest_name);
		start_date = (EditText) findViewById(R.id.dt_start_date);
		area_affected = (EditText) findViewById(R.id.dt_area_affected);
		no_of_houses_affected = (EditText) findViewById(R.id.dt_no_of_houses_affected);
		no_of_peoples_affected = (EditText) findViewById(R.id.dt_no_of_peoples_affected);
		cause_of_fire = (EditText) findViewById(R.id.dt_cause_of_fire);

		damage_of_wildlife = (EditText) findViewById(R.id.dt_damage_of_wildlife);
		damage_of_ntft = (EditText) findViewById(R.id.dt_damage_of_ntft);
		impact_in_water = (EditText) findViewById(R.id.dt_potential_impact_in_water);
		impact_in_soil = (EditText) findViewById(R.id.dt_potential_impact_in_soil);
		how_extinguished = (EditText) findViewById(R.id.dt_how_extinguished);
		how_not_extinguished = (EditText) findViewById(R.id.dt_how_not_extinguished);
		lesson_for_future = (EditText) findViewById(R.id.dt_lesson_for_future);
		other_remarks = (EditText) findViewById(R.id.dt_other_remarks);

		getForestList all_forest = new getForestList(DetailReportActivity.this);
		this.forest_data = all_forest.getPointsFromJSON();
		this.forest_name_list = all_forest
				.getForestListfromArrayList(forest_data);

		ArrayAdapter<String> forest_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, forest_name_list) {
			@Override
			public Filter getFilter() {
				return super.getFilter();
			}

		};
		// forest_name.setAdapter(forest_adapter);

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mhour = c.get(Calendar.HOUR_OF_DAY);
		mminute = c.get(Calendar.MINUTE);
		updateDisplay();
		// updateTimeDisplay();
		mDateSetListener = new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				updateDisplay();
			}
		};

		start_date.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View view) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		rg_current_status = (RadioGroup) findViewById(R.id.rg_dt_current_status);
		rg_fire_size = (RadioGroup) findViewById(R.id.rg_dt_fire_size);
		rg_fire_type = (RadioGroup) findViewById(R.id.rg_dt_fire_type);
		rg_regeneration = (RadioGroup) findViewById(R.id.rg_dt_regneration);
		rg_forest_density = (RadioGroup) findViewById(R.id.rg_density);
		rg_start_time = (RadioGroup) findViewById(R.id.rg_dt_start_time);
		rg_arrival_time = (RadioGroup) findViewById(R.id.rg_dt_dfug_arrival_time);
		rg_fire_extinguish_time = (RadioGroup) findViewById(R.id.rg_dt_extinguish_time);
		rg_no_mobilized = (RadioGroup) findViewById(R.id.rg_dt_no_mobilized);
		rg_use_of_water = (RadioGroup) findViewById(R.id.rg_dt_use_of_water);
		rg_area_damaged = (RadioGroup) findViewById(R.id.rg_dt_area_damaged);
		rg_burnt_area_feature = (RadioGroup) findViewById(R.id.dt_feature_of_burn_area);
		rg_fire_start_from = (RadioGroup) findViewById(R.id.rg_dt_fire_started);
		rg_distance_from_fire = (RadioGroup) findViewById(R.id.rg_dt_distance_from_fire);
		rg_no_of_trees_burnt = (RadioGroup) findViewById(R.id.rg_dt_no_of_trees_burnt);

		current_status = (RadioButton) findViewById(R.id.dt_just_started);
		fire_size = (RadioButton) findViewById(R.id.dt_big);
		fire_type = (RadioButton) findViewById(R.id.dt_crown_fire);
		regenration_option = (RadioButton) findViewById(R.id.dt_yes);
		forest_density = (RadioButton) findViewById(R.id.dt_high_density);
		start_time = (RadioButton) findViewById(R.id.dt_morning_hour);
		arrival_time = (RadioButton) findViewById(R.id.dt_arrival_immediately);
		fire_extinguish_time = (RadioButton) findViewById(R.id.dt_extinguish_immediately);
		no_mobilized = (RadioButton) findViewById(R.id.dt_less_than_20);
		use_of_water = (RadioButton) findViewById(R.id.dt_water_yes);
		area_damaged = (RadioButton) findViewById(R.id.dt_area_less_1);
		burnt_area_feature = (RadioButton) findViewById(R.id.dt_feature_slope);
		fire_start_from = (RadioButton) findViewById(R.id.dt_start_near_road);
		distance_from_fire = (RadioButton) findViewById(R.id.dt_distance_less_15);
		no_of_trees_burnt = (RadioButton) findViewById(R.id.dt_tree_less_50);

		TelephonyManager telephoneManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		final String device_id = telephoneManager.getDeviceId();

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

		rg_regeneration
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int i) {
						regenration_option = (RadioButton) findViewById(i);
					}
				});

		rg_forest_density
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int i) {
						forest_density = (RadioButton) findViewById(i);
					}
				});

		rg_start_time
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int i) {
						start_time = (RadioButton) findViewById(i);
					}
				});

		rg_arrival_time
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int i) {
						arrival_time = (RadioButton) findViewById(i);
					}
				});

		rg_fire_extinguish_time
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int i) {
						fire_extinguish_time = (RadioButton) findViewById(i);
					}
				});

		rg_no_mobilized
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int i) {
						no_mobilized = (RadioButton) findViewById(i);
					}
				});

		rg_use_of_water
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int i) {
						use_of_water = (RadioButton) findViewById(i);
					}
				});

		rg_area_damaged
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int i) {
						area_damaged = (RadioButton) findViewById(i);
					}
				});

		rg_burnt_area_feature
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int i) {
						burnt_area_feature = (RadioButton) findViewById(i);
					}
				});

		fire_start_from_others = (EditText) findViewById(R.id.dt_start_others_checked);
		rg_fire_start_from
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int i) {
						if (i == R.id.dt_start_others) {
							fire_start_from_others.setVisibility(View.VISIBLE);
						} else {
							fire_start_from_others.setVisibility(View.GONE);
						}
						fire_start_from = (RadioButton) findViewById(i);
					}
				});

		rg_distance_from_fire
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int i) {
						distance_from_fire = (RadioButton) findViewById(i);
					}
				});

		rg_no_of_trees_burnt
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int i) {
						no_of_trees_burnt = (RadioButton) findViewById(i);
					}
				});

		other_institution_dfo = (CheckBox) findViewById(R.id.dt_dfo);
		other_institution_police = (CheckBox) findViewById(R.id.dt_police);
		other_institution_army = (CheckBox) findViewById(R.id.dt_army);

		trigger_dry_pine = (CheckBox) findViewById(R.id.dt_trigger_dry_pine);
		trigger_dry_litters = (CheckBox) findViewById(R.id.dt_trigger_dry_litters);
		trigger_steep_slope = (CheckBox) findViewById(R.id.dt_trigger_steep_slope);
		trigger_strong_wind = (CheckBox) findViewById(R.id.dt_trigger_strong_wind);
		trigger_human_cause = (CheckBox) findViewById(R.id.dt_trigger_human_cause);

		equipment_long_stick = (CheckBox) findViewById(R.id.dt_long_stick);
		equipment_water = (CheckBox) findViewById(R.id.dt_water);
		equipment_tree_branch = (CheckBox) findViewById(R.id.dt_tree_branch);
		equipment_kuto_kodalo = (CheckBox) findViewById(R.id.dt_kuto_kodalo);
		equipment_improved_equipment = (CheckBox) findViewById(R.id.dt_improved_eq);

		reporting = new JSONObject();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			try {
				if (extras.containsKey("latitude")
						&& extras.containsKey("longitude")) {
					reporting.put("latitude", extras.getDouble("latitude"));
					reporting.put("longitude", extras.getDouble("longitude"));
					Log.i("forest_name", extras.getString("forest_name"));
					reporting.put("forest_name",
							extras.getString("forest_name"));
				}
				if (extras.containsKey("file_name")) {
					FileCache fileCache = new FileCache(
							DetailReportActivity.this);
					reporting.put("forest_name", fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"forest_name"));
					reporting.put(
							"latitude",
							fileCache.getFileParameterfromName(
									extras.getString("file_name"), "latitude"));
					reporting
							.put("longitude", fileCache
									.getFileParameterfromName(
											extras.getString("file_name"),
											"longitude"));
					// forest_name.setText(prev_forest_name);

					prev_forest_fire_date = fileCache.getFileParameterfromName(
							extras.getString("file_name"), "fire_date");
					if (!prev_forest_fire_date.equals("")) {
						if (prev_forest_fire_date.contains(" ")) {
							String[] pre_dates = prev_forest_fire_date
									.split(" ");
							if (pre_dates.length == 2) {
								start_date.setText(pre_dates[0]);
								// start_time.setText(pre_dates[1]);
							} else if (pre_dates.length == 1) {
								start_date.setText(pre_dates[0]);
							}
						} else {
							if (prev_forest_fire_date.contains(":")) {
								// start_time.setText(prev_forest_fire_date);
							} else
								start_date.setText(prev_forest_fire_date);
						}
					}
					prev_forest_fire_cause = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"cause_of_fire");
					cause_of_fire.setText(prev_forest_fire_cause);

					prev_no_of_houses_affected = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"no_of_houses_affected");
					no_of_houses_affected.setText(prev_no_of_houses_affected);

					prev_no_of_peoples_affected = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"no_of_peoples_affected");
					no_of_peoples_affected.setText(prev_no_of_peoples_affected);

					prev_area_affected = fileCache.getFileParameterfromName(
							extras.getString("file_name"), "area_affected");
					area_affected.setText(prev_area_affected);
					//

					String prev_damage_of_wildlife = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"damage_of_wildlife");
					damage_of_wildlife.setText(prev_damage_of_wildlife);

					String prev_damage_of_ntft = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"damage_of_ntft");
					damage_of_ntft.setText(prev_damage_of_ntft);

					String prev_impact_in_water = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"impact_in_water");
					impact_in_water.setText(prev_impact_in_water);

					String prev_impact_in_soil = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"impact_in_soil");
					impact_in_soil.setText(prev_impact_in_soil);

					String prev_how_extinguished = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"how_extinguished");
					how_extinguished.setText(prev_how_extinguished);

					String prev_how_not_extinguished = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"how_not_extinguished");
					how_not_extinguished.setText(prev_how_not_extinguished);

					String prev_lesson_for_future = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"lesson_for_future");
					lesson_for_future.setText(prev_lesson_for_future);

					String prev_other_remarks = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"other_remarks");
					other_remarks.setText(prev_other_remarks);

					prev_forest_density = fileCache.getFileParameterfromName(
							extras.getString("file_name"), "forest_density");
					if (prev_forest_density.equalsIgnoreCase("High")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_high_density);
						rb.setChecked(true);
					} else if (prev_forest_density.equalsIgnoreCase("Medium")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_medium_density);
						rb.setChecked(true);
					} else if (prev_forest_density.equalsIgnoreCase("Low")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_low_density);
						rb.setChecked(true);
					}

					prev_forest_fire_type = fileCache.getFileParameterfromName(
							extras.getString("file_name"), "fire_type");
					if (prev_forest_fire_type.equalsIgnoreCase("Bush Fire")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_bush_fire);
						rb.setChecked(true);
					} else if (prev_forest_fire_type
							.equalsIgnoreCase("Ground Fire")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_ground_fire);
						rb.setChecked(true);
					} else if (prev_forest_fire_type
							.equalsIgnoreCase("Crown Fire")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_crown_fire);
						rb.setChecked(true);
					}

					prev_forest_fire_status = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"current_status");
					if (prev_forest_fire_status
							.equalsIgnoreCase("Just Started")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_just_started);
						rb.setChecked(true);
					} else if (prev_forest_fire_status
							.equalsIgnoreCase("Under Control")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_under_control);
						rb.setChecked(true);
					} else if (prev_forest_fire_status
							.equalsIgnoreCase("Expanding")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_expanding);
						rb.setChecked(true);
					}

					prev_forest_fire_size = fileCache.getFileParameterfromName(
							extras.getString("file_name"), "fire_size");
					if (prev_forest_fire_size.equalsIgnoreCase("Big")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_big);
						rb.setChecked(true);
					} else if (prev_forest_fire_size.equalsIgnoreCase("Medium")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_medium);
						rb.setChecked(true);
					} else if (prev_forest_fire_size.equalsIgnoreCase("Small")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_small);
						rb.setChecked(true);
					}

					prev_regneration_option = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"regeneration");
					if (prev_regneration_option.equalsIgnoreCase("YES")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_yes);
						rb.setChecked(true);
					} else if (prev_regneration_option.equalsIgnoreCase("NO")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_no);
						rb.setChecked(true);
					}

					String prev_start_time = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"), "fire_time");
					if (prev_start_time.equalsIgnoreCase("Morning Hour")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_morning_hour);
						rb.setChecked(true);
					} else if (prev_start_time.equalsIgnoreCase("Day Hour")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_day_hour);
						rb.setChecked(true);
					} else if (prev_start_time.equalsIgnoreCase("Evening Hour")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_evening_hour);
						rb.setChecked(true);
					} else if (prev_start_time.equalsIgnoreCase("Night Hour")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_night_hour);
						rb.setChecked(true);
					}

					String prev_arrival_time = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"cfug_arrival_time");
					if (prev_arrival_time.equalsIgnoreCase("Immediately")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_arrival_immediately);
						rb.setChecked(true);
					} else if (prev_arrival_time
							.equalsIgnoreCase("After 3 Hour")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_arrival_after_3_hr);
						rb.setChecked(true);
					} else if (prev_arrival_time.equalsIgnoreCase("Lately")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_arrival_lately);
						rb.setChecked(true);
					}

					String prev_extinguish_time = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"fire_extinguish_time");
					if (prev_extinguish_time.equalsIgnoreCase("Immediately")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_extinguish_immediately);
						rb.setChecked(true);
					} else if (prev_extinguish_time
							.equalsIgnoreCase("Within 3 hours")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_extinguish_within_3_hour);
						rb.setChecked(true);
					} else if (prev_extinguish_time
							.equalsIgnoreCase("Within 24 hours")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_extinguish_within_24_hr);
						rb.setChecked(true);
					} else if (prev_extinguish_time
							.equalsIgnoreCase("More than 1 day")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_extinguish_more_than_1_day);
						rb.setChecked(true);
					}

					String prev_no_mobilized = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"no_cfug_people_mobilized");
					if (prev_no_mobilized.equalsIgnoreCase("Less than 20")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_less_than_20);
						rb.setChecked(true);
					} else if (prev_no_mobilized.equalsIgnoreCase("20-50")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_20_50);
						rb.setChecked(true);
					} else if (prev_no_mobilized.equalsIgnoreCase("50-100")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_50_100);
						rb.setChecked(true);
					} else if (prev_no_mobilized
							.equalsIgnoreCase("More than 100")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_more_than_100);
						rb.setChecked(true);
					}

					String prev_use_of_water = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"use_of_water");
					if (prev_use_of_water.equalsIgnoreCase("YES")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_water_yes);
						rb.setChecked(true);
					} else if (prev_use_of_water.equalsIgnoreCase("NO")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_water_no);
						rb.setChecked(true);
					}

					String prev_area_damaged = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"area_damaged");
					if (prev_area_damaged.equalsIgnoreCase("Less than 1 ha")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_area_less_1);
						rb.setChecked(true);
					} else if (prev_area_damaged.equalsIgnoreCase("1-5 Ha")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_area_1_5);
						rb.setChecked(true);
					} else if (prev_area_damaged.equalsIgnoreCase("5-10 Ha")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_area_5_10);
						rb.setChecked(true);
					} else if (prev_area_damaged
							.equalsIgnoreCase("More than 10 Ha")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_area_more_10);
						rb.setChecked(true);
					}

					String prev_area_feature = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"burnt_area_feature");
					if (prev_area_feature.equalsIgnoreCase("Slope")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_feature_slope);
						rb.setChecked(true);
					} else if (prev_area_feature.equalsIgnoreCase("Flat")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_feature_flat);
						rb.setChecked(true);
					}

					String prev_fire_start_from = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"fire_start_from");
					if (prev_fire_start_from.equalsIgnoreCase("Near Road")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_start_near_road);
						rb.setChecked(true);
					} else if (prev_fire_start_from
							.equalsIgnoreCase("Farm Land")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_start_farm_land);
						rb.setChecked(true);
					} else if (prev_fire_start_from.equalsIgnoreCase("Village")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_start_village);
						rb.setChecked(true);
					} else if (prev_fire_start_from.equalsIgnoreCase("Others")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_start_others);
						fire_start_from_others.setVisibility(View.VISIBLE);
						fire_start_from_others.setText(fileCache
								.getFileParameterfromName(
										extras.getString("file_name"),
										"fire_start_from_other"));
						rb.setChecked(true);
					}

					String prev_distance_from_fire = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"distance_from_fire");
					if (prev_distance_from_fire
							.equalsIgnoreCase("Less than 15 min")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_distance_less_15);
						rb.setChecked(true);
					} else if (prev_distance_from_fire
							.equalsIgnoreCase("15-30 min")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_distance_15_30);
						rb.setChecked(true);
					} else if (prev_distance_from_fire
							.equalsIgnoreCase("30-60 min")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_distance_30_60);
						rb.setChecked(true);
					} else if (prev_distance_from_fire
							.equalsIgnoreCase("More than 1 hr")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_distance_more_1);
						rb.setChecked(true);
					}

					String prev_no_of_trees_burnt = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"no_of_trees_burnt");
					if (prev_no_of_trees_burnt.equalsIgnoreCase("Less than 50")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_tree_less_50);
						rb.setChecked(true);
					} else if (prev_no_of_trees_burnt
							.equalsIgnoreCase("50-100")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_tree_50_100);
						rb.setChecked(true);
					} else if (prev_no_of_trees_burnt
							.equalsIgnoreCase("100-500")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_tree_100_500);
						rb.setChecked(true);
					} else if (prev_no_of_trees_burnt
							.equalsIgnoreCase("More than 500")) {
						RadioButton rb = (RadioButton) findViewById(R.id.dt_tree_more_500);
						rb.setChecked(true);
					}

					String prev_triggers = fileCache.getFileParameterfromName(
							extras.getString("file_name"), "triggers");
					String[] prev_triggers_list = prev_triggers.split(",");
					ArrayList<String> prev_triggers_arraylist = convertListtoArrayList(prev_triggers_list);
					if (prev_triggers_arraylist.contains("Dry Pine Needle"))
						trigger_dry_pine.setChecked(true);
					if (prev_triggers_arraylist.contains("Dry Litters"))
						trigger_dry_litters.setChecked(true);
					if (prev_triggers_arraylist.contains("Steep Slope"))
						trigger_steep_slope.setChecked(true);
					if (prev_triggers_arraylist.contains("Strong Wind"))
						trigger_strong_wind.setChecked(true);
					if (prev_triggers_arraylist.contains("Human Cause"))
						trigger_human_cause.setChecked(true);

					String prev_equipments = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"equipments_brought");
					Log.i("equipments_string_edit", prev_equipments);
					String[] prev_equipments_list = prev_equipments.split(",");
					Log.i("equipments_edit_size", prev_equipments_list.length
							+ "");
					ArrayList<String> prev_equipments_arraylist = convertListtoArrayList(prev_equipments_list);
					if (prev_equipments_arraylist.contains("Kuto Kodalo"))
						equipment_kuto_kodalo.setChecked(true);
					if (prev_equipments_arraylist.contains("Long Stick"))
						equipment_long_stick.setChecked(true);
					if (prev_equipments_arraylist.contains("Tree Branch"))
						equipment_tree_branch.setChecked(true);
					if (prev_equipments_arraylist.contains("Water"))
						equipment_water.setChecked(true);
					if (prev_equipments_arraylist
							.contains("Improved Equipment"))
						equipment_improved_equipment.setChecked(true);

					String prev_institutions = fileCache
							.getFileParameterfromName(
									extras.getString("file_name"),
									"other_institutions");
					String[] prev_institutions_list = prev_institutions
							.split(",");
					ArrayList<String> prev_institutions_arrayList = convertListtoArrayList(prev_institutions_list);
					if (prev_institutions_arrayList.contains("DFO"))
						other_institution_dfo.setChecked(true);
					if (prev_institutions_arrayList.contains("Police"))
						other_institution_police.setChecked(true);
					if (prev_institutions_arrayList.contains("Army"))
						other_institution_army.setChecked(true);

					prev_file_name = extras.getString("file_name");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		save_report = (Button) findViewById(R.id.save_report);
		save_report.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try {

					// Other institutions
					try {

						// get mac address of the device instead of imei no
						WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
						WifiInfo winFo = wifiManager.getConnectionInfo();
						imei_no = winFo.getMacAddress();

					} catch (Exception e) {
						e.printStackTrace();
					}

					if (other_institution_army.isChecked())
						other_institutions.add("Army");
					if (other_institution_dfo.isChecked())
						other_institutions.add("DFO");
					if (other_institution_police.isChecked())
						other_institutions.add("Police");

					for (int i = 0; i < other_institutions.size(); i++) {
						if (i == other_institutions.size() - 1)
							other_institutions_string = other_institutions_string
									+ other_institutions.get(i);
						else
							other_institutions_string = other_institutions_string
									+ other_institutions.get(i) + ",";
					}

					if (trigger_dry_pine.isChecked())
						triggers.add("Dry Pine Needle");
					if (trigger_dry_litters.isChecked())
						triggers.add("Dry Litters");
					if (trigger_steep_slope.isChecked())
						triggers.add("Steep Slope");
					if (trigger_strong_wind.isChecked())
						triggers.add("Strong Wind");
					if (trigger_human_cause.isChecked())
						triggers.add("Human Cause");
					for (int i = 0; i < triggers.size(); i++) {
						if (i == triggers.size() - 1)
							triggers_string = triggers_string + triggers.get(i);
						else
							triggers_string = triggers_string + triggers.get(i)
									+ ",";
					}

					if (equipment_kuto_kodalo.isChecked())
						equipment_brought.add("Kuto Kodalo");
					if (equipment_long_stick.isChecked())
						equipment_brought.add("Long Stick");
					if (equipment_tree_branch.isChecked())
						equipment_brought.add("Tree Branch");
					if (equipment_water.isChecked())
						equipment_brought.add("Water");
					if (equipment_improved_equipment.isChecked())
						equipment_brought.add("Improved Equipment");
					for (int i = 0; i < equipment_brought.size(); i++) {
						if (i == equipment_brought.size() - 1)
							equipments_string = equipments_string
									+ equipment_brought.get(i);
						else
							equipments_string = equipments_string
									+ equipment_brought.get(i) + ",";
					}

					Log.i("equipments_string", equipments_string);

					if (!start_date.getText().toString().equals("")) {

						if (prev_file_name.equals(""))
							reporting.put("UUID", UUID.randomUUID().toString());
						reporting.put("fire_date", start_date.getText()
								.toString());

						reporting.put("current_status", current_status
								.getText().toString());
						reporting.put("fire_size", fire_size.getText()
								.toString());
						reporting.put("no_of_houses_affected",
								no_of_houses_affected.getText().toString());
						reporting.put("no_of_peoples_affected",
								no_of_peoples_affected.getText().toString());
						reporting.put("area_affected", area_affected.getText()
								.toString());
						reporting.put("cause_of_fire", cause_of_fire.getText()
								.toString());
						reporting.put("fire_type", fire_type.getText()
								.toString());
						reporting.put("forest_density", forest_density
								.getText().toString());
						reporting.put("fire_time", start_time.getText()
								.toString());
						reporting.put("cfug_arrival_time", arrival_time
								.getText().toString());
						reporting.put("fire_extinguish_time",
								fire_extinguish_time.getText().toString());
						reporting.put("no_cfug_people_mobilized", no_mobilized
								.getText().toString());
						reporting.put("area_damaged", area_damaged.getText()
								.toString());
						reporting.put("use_of_water", use_of_water.getText()
								.toString());
						reporting.put("burnt_area_feature", burnt_area_feature
								.getText().toString());
						reporting.put("fire_start_from", fire_start_from
								.getText().toString());
						if (fire_start_from_others.getVisibility() == View.VISIBLE) {
							reporting
									.put("fire_start_from_other",
											fire_start_from_others.getText()
													.toString());
						}
						reporting.put("distance_from_fire", distance_from_fire
								.getText().toString());
						reporting.put("no_of_trees_burnt", no_of_trees_burnt
								.getText().toString());

						reporting.put("damage_of_wildlife", damage_of_wildlife
								.getText().toString());
						reporting.put("damage_of_ntft", damage_of_ntft
								.getText().toString());
						reporting.put("impact_in_water", impact_in_water
								.getText().toString());
						reporting.put("impact_in_soil", impact_in_soil
								.getText().toString());
						reporting.put("how_extinguished", how_extinguished
								.getText().toString());
						reporting.put("how_not_extinguished",
								how_not_extinguished.getText().toString());
						reporting.put("lesson_for_future", lesson_for_future
								.getText().toString());
						reporting.put("other_remarks", other_remarks.getText()
								.toString());

						reporting.put("other_institutions",
								other_institutions_string);
						reporting.put("triggers", triggers_string);
						reporting.put("equipments_brought", equipments_string);

						reporting.put("regeneration", regenration_option
								.getText().toString());
						reporting.put("imei_no", imei_no);
						forest_fire = new JSONObject().put("ForestFire",
								reporting);

						Log.i("saved_data", forest_fire.toString());

						if (prev_file_name.equals(""))

							// contents of detail_report are initially stored to
							// file as jsonObject before uploading into the
							// server
							new FileCache(DetailReportActivity.this)
									.storeFile(forest_fire.toString());
						else
							new FileCache(DetailReportActivity.this)
									.replaceFile(forest_fire.toString(),
											prev_file_name);

						Intent intent = new Intent(DetailReportActivity.this,
								SavedReportsActivity.class);
						intent.putExtra("new_report", "yes");
						startActivity(intent);
						finish();
					} else {
						Toast.makeText(DetailReportActivity.this,
								"Date is Needed", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private ArrayList<String> convertListtoArrayList(String[] list) {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (int i = 0; i < list.length; i++) {
			arrayList.add(list[i]);
		}
		return arrayList;
	}

	/*
	 * private void updateTimeDisplay() { start_time.setText( new
	 * StringBuilder() .append(pad(mhour)).append(":") .append(pad(mminute)));
	 * 
	 * }
	 */

	@SuppressWarnings("unused")
	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);

	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mhour, mminute,
					false);
		}
		return null;
	}

	private void updateDisplay() {
		if (mMonth >= 9) {
			if (mDay >= 10) {
				start_date.setText(new StringBuilder().append(mYear)
						.append("-").append(mMonth + 1).append("-")
						.append(mDay));
			} else {
				start_date.setText(new StringBuilder().append(mYear)
						.append("-").append(mMonth + 1).append("-")
						.append("0" + mDay));
			}
		} else {
			if (mDay >= 10) {
				start_date.setText(new StringBuilder().append(mYear)
						.append("-").append("0" + (mMonth + 1)).append("-")
						.append(mDay));
			} else {
				start_date.setText(new StringBuilder().append(mYear)
						.append("-").append("0" + (mMonth + 1)).append("-")
						.append("0" + mDay));
			}

		}

	}

}
