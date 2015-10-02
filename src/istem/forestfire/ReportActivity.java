package istem.forestfire;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
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
import istem.forestfire.R;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Hp on 6/16/2015.
 */
@SuppressLint("ShowToast")
public class ReportActivity extends ActionBarActivity {
	private MapView mv;
	private ArrayList<TileCache> tileCache = new ArrayList<TileCache>();
	private TileRendererLayer tileRendererLayer;
	public static String MAPFILE = Environment.getExternalStorageDirectory()
			.getPath() + "/.iStem/nepal/nepal.map";
	public static String MAPFILE1 = Environment.getExternalStorageDirectory()
			.getPath() + "/.iStem/nepal/all_layers2.map";
	public static String MAPFILE2 = Environment.getExternalStorageDirectory()
			.getPath() + "/.iStem/nepal/road_forest2.map";
	@SuppressWarnings("unused")
	private TappableMarker endmarker, green_marker, usermarker, blue_marker,
			red_marker;
	private GPSTracker gps_conn;
	private ProgressDialog mProgressDialog;
	private LinearLayout report_layout;
	private ImageButton refresh_gps_loc;
	private Toast toast;
	private Button quick_report;
	private Button detail_report;
	private AutoCompleteTextView autofill_forest;
	private ArrayList<POI> forest_data;
	private List<String> forest_name;
	@SuppressWarnings("unused")
	private ImageButton search;
	private TileRendererLayer tileRendererLayer2;
	protected SharedPreferences sharedPreferences;
	@SuppressWarnings("unused")
	private TileRendererLayer tileRendererLayer3;
	private String selected_forest_name = "";
	private String imei_no;
	public static String file_url = "http://atnepal.com/forest_fire/fire.zip";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidGraphicFactory.createInstance(this.getApplication());
		setContentView(R.layout.activity_reports);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon);
		getSupportActionBar().setTitle("New Report");
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		toast = Toast.makeText(ReportActivity.this, "", Toast.LENGTH_LONG);
		toaster("Long Press to Choose Location");
		report_layout = (LinearLayout) findViewById(R.id.report_layout);
		mv = (MapView) findViewById(R.id.map);
		mv.setClickable(true);
		mv.setBuiltInZoomControls(true);
		/*
		 * this.tileCache.add(AndroidUtil.createTileCache(this, "mapcache1",
		 * mv.getModel().displayModel.getTileSize(), 0.5f,
		 * mv.getModel().frameBufferModel.getOverdrawFactor()));
		 */
		this.mv.getMapZoomControls().setZoomLevelMin((byte) 10);
		this.mv.getMapZoomControls().setZoomLevelMax((byte) 20);
		this.tileCache.add(AndroidUtil.createTileCache(this, "mapcache",
				mv.getModel().displayModel.getTileSize(), 1f,
				this.mv.getModel().frameBufferModel.getOverdrawFactor()));
		this.tileCache.add(AndroidUtil.createTileCache(this, "mapcache2",
				mv.getModel().displayModel.getTileSize(), 1f,
				this.mv.getModel().frameBufferModel.getOverdrawFactor()));
		// this.tileCache.add(AndroidUtil.createTileCache(this, "mapcache3",
		// mv.getModel().displayModel.getTileSize(), 1f,
		// this.mv.getModel().frameBufferModel.getOverdrawFactor()));

		autofill_forest = (AutoCompleteTextView) findViewById(R.id.search_autocomplete);

		getForestList all_forest = new getForestList(ReportActivity.this);
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
						mv.getModel().mapViewPosition.setZoomLevel((byte) 15);
						if (green_marker != null) {
							mv.getLayerManager().getLayers()
									.remove(green_marker);
							green_marker = null;
						}
						selected_forest_name = autofill_forest.getText()
								.toString();
						setKeyboardVisibility(false);
					}
				});

		refresh_gps_loc = (ImageButton) findViewById(R.id.refresh_gps_loc);
		refresh_gps_loc.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				gps_conn = new GPSTracker(ReportActivity.this);
				if (gps_conn.canGetLocation() && gps_conn.getLocation() != null) {
					if (gps_conn.getLatitude() != 0
							&& gps_conn.getLongitude() != 0) {
						createUserMarker(gps_conn.getLatitude(),
								gps_conn.getLongitude());
						if (green_marker != null) {
							mv.getLayerManager().getLayers()
									.remove(green_marker);
							green_marker = null;
							report_layout.setVisibility(View.GONE);
						}
					}
				} else {
					toaster("Searching For Location");
				}
			}
		});

		quick_report = (Button) findViewById(R.id.quick_report);
		quick_report.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (new NetworkCheck(ReportActivity.this)
						.haveNetworkConnection()) {
					if (green_marker != null) {
						String other_forest_name = autofill_forest.getText()
								.toString();
						if (forest_name.contains(other_forest_name)) {
							if (!selected_forest_name.equals("")) {
								if (selected_forest_name
										.equals(other_forest_name)) {
									new SessionManager(ReportActivity.this).createLocationSession(
											String.valueOf(mv.getModel().mapViewPosition
													.getCenter().latitude),
											String.valueOf(mv.getModel().mapViewPosition
													.getCenter().longitude));
									new SessionManager(ReportActivity.this)
											.storeZoomLevel(mv.getModel().mapViewPosition
													.getZoomLevel());

									Intent intent = new Intent(
											ReportActivity.this,
											QuickReportActivity.class);
									intent.putExtra("latitude",
											green_marker.getLatLong().latitude);
									intent.putExtra("longitude",
											green_marker.getLatLong().longitude);
									intent.putExtra("forest_name",
											selected_forest_name);
									startActivity(intent);
								} else {
									toaster("Incorrect Forest Location");
								}
							} else {
								toaster("Search/Choose Forest");
							}
						} else {
							toaster("Forest Name Does Not Exists");
						}
					} else {
						toaster("Please Choose Location First");
					}
				} else {
					toaster("No Internet Connection");
				}
			}
		});

		detail_report = (Button) findViewById(R.id.detail_report);
		detail_report.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (green_marker != null) {
					String other_forest_name = autofill_forest.getText()
							.toString();
					if (forest_name.contains(other_forest_name)) {
						if (!selected_forest_name.equals("")) {
							if (selected_forest_name.equals(other_forest_name)) {
								new SessionManager(ReportActivity.this).createLocationSession(
										String.valueOf(mv.getModel().mapViewPosition
												.getCenter().latitude),
										String.valueOf(mv.getModel().mapViewPosition
												.getCenter().longitude));
								new SessionManager(ReportActivity.this)
										.storeZoomLevel(mv.getModel().mapViewPosition
												.getZoomLevel());

								Intent intent = new Intent(ReportActivity.this,
										DetailReportActivity.class);
								intent.putExtra("latitude",
										green_marker.getLatLong().latitude);
								intent.putExtra("longitude",
										green_marker.getLatLong().longitude);
								intent.putExtra("forest_name",
										selected_forest_name);
								intent.putExtra("imei_no", imei_no);
								startActivity(intent);
							} else {
								toaster("Incorrect Forest Location");
							}
						} else {
							toaster("Search/Choose Forest");
						}
					} else {
						toaster("Forest Name Does Not Exists");
					}
				} else {
					toaster("Please Choose Location First");
				}
			}
		});

	}

	public void setKeyboardVisibility(boolean show) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (show) {
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		} else {
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		}
	}

	public void onBackPressed() {
		new SessionManager(ReportActivity.this)
				.createLocationSession(
						String.valueOf(mv.getModel().mapViewPosition
								.getCenter().latitude),
						String.valueOf(mv.getModel().mapViewPosition
								.getCenter().longitude));
		new SessionManager(ReportActivity.this)
				.storeZoomLevel(mv.getModel().mapViewPosition.getZoomLevel());
		super.onBackPressed();
		/*
		 * Intent intent=new Intent(ReportActivity.this,MainActivity.class);
		 * startActivity(intent);
		 */

	}

	public static File getMapFile() {
		File file = new File(MAPFILE);
		return file;
	}

	public static File getMapFile2() {
		File file = new File(MAPFILE1);
		return file;
	}

	public static File getMapFile3() {
		File file = new File(MAPFILE2);
		return file;
	}

	public void toaster(String s) {
		toast.setText(s);
		toast.show();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (new File(MAPFILE).exists() && new File(MAPFILE1).exists()) {
			this.mv.getLayerManager().getLayers()
					.remove(this.tileRendererLayer);
			this.mv.getLayerManager().getLayers()
					.remove(this.tileRendererLayer2);
			// this.mv.getLayerManager().getLayers().remove(this.tileRendererLayer3);
			this.tileRendererLayer.onDestroy();
			this.tileRendererLayer2.onDestroy();
			// this.tileRendererLayer3.onDestroy();

		} else {
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (new File(MAPFILE).exists() && new File(MAPFILE1).exists()) {
			this.tileCache.get(0).destroy();
			this.tileCache.get(1).destroy();
			// this.tileCache.get(2).destroy();
			this.mv.getModel().mapViewPosition.destroy();
			this.mv.destroy();
			AndroidGraphicFactory.clearResourceMemoryCache();

		} else {
		}
	}

	private void loadMap() {
		HashMap<String, String> old_location = new SessionManager(
				getApplicationContext()).getLocationDetails();
		final String old_lat = old_location.get(SessionManager.KEY_LAT);
		final String old_lgt = old_location.get(SessionManager.KEY_LGT);
		@SuppressWarnings("unused")
		final int old_zoom = new SessionManager(ReportActivity.this)
				.getZoomLevel();
		mv.getModel().mapViewPosition.setZoomLevel((byte) 16);
		MapDataStore mapDataStore = new MapFile(getMapFile());
		// mv.getModel().mapViewPosition.setCenter(new
		// LatLong(Double.valueOf(old_lat),Double.valueOf(old_lgt)));
		tileRendererLayer = new TileRendererLayer(tileCache.get(0),
				mapDataStore, mv.getModel().mapViewPosition, true, true,
				AndroidGraphicFactory.INSTANCE) {
			@Override
			public boolean onLongPress(LatLong tapLatLong, Point layerXY,
					Point tapXY) {
				createlocationMarker(tapLatLong);
				report_layout.setVisibility(View.VISIBLE);
				return super.onLongPress(tapLatLong, layerXY, tapXY);

			}
		};
		tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);
		mv.getLayerManager().getLayers().add(tileRendererLayer);

		MapDataStore mapDataStore2 = new MapFile(getMapFile2());

		tileRendererLayer2 = new TileRendererLayer(this.tileCache.get(1),
				mapDataStore2, mv.getModel().mapViewPosition, true, true,
				AndroidGraphicFactory.INSTANCE) {
			@Override
			public boolean onLongPress(LatLong tapLatLong, Point layerXY,
					Point tapXY) {
				createlocationMarker(tapLatLong);
				report_layout.setVisibility(View.VISIBLE);
				return super.onLongPress(tapLatLong, layerXY, tapXY);

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

		gps_conn = new GPSTracker(getApplicationContext());
		createUserMarker(gps_conn.getLatitude(), gps_conn.getLongitude());
		this.mv.getModel().mapViewPosition.setCenter(new LatLong(
				27.39365740368, 85.232818810));
		this.mv.getModel().mapViewPosition.setZoomLevel((byte) 14);

		if (green_marker != null) {
			mv.getLayerManager().getLayers().remove(green_marker);
			green_marker = null;
			report_layout.setVisibility(View.GONE);
		}

		if (old_lat != null || old_lgt != null)
			createOldLocationMarker(new LatLong(Double.valueOf(old_lat),
					Double.valueOf(old_lgt)));

	}

	protected void onStart() {
		super.onStart();
		if (new File(MAPFILE).exists() && new File(MAPFILE1).exists()) {
			loadMap();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Do You Want To Download Files Now?").setTitle(
					"Download Support Files");
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							DownloadZipfile mew = new DownloadZipfile();
							mew.execute(file_url);
						}
					});
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// dialog.cancel();
							startActivity(new Intent(ReportActivity.this,
									MainActivity.class));
						}
					});
			AlertDialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}
	}

	private void createOldLocationMarker(LatLong latLong) {
		if (red_marker != null)
			mv.getLayerManager().getLayers().remove(red_marker);
		this.red_marker = new TappableMarker(ReportActivity.this,
				R.drawable.red, latLong, "");
		mv.getLayerManager().getLayers().add(red_marker);

	}

	@SuppressWarnings("unused")
	private void createPreviousLocationMarker(LatLong latLng,
			final String string) {
		this.blue_marker = new TappableMarker(ReportActivity.this,
				R.drawable.blue, latLng, "") {
			public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
				double centerX = layerXY.x + getHorizontalOffset();
				double centerY = layerXY.y + getVerticalOffset();

				double radiusX = (getBitmap().getWidth() / 2) * 1.1;
				double radiusY = (getBitmap().getHeight() / 2) * 1.1;

				double distX = Math.abs(centerX - tapXY.x);
				double distY = Math.abs(centerY - tapXY.y);

				if (distX < radiusX && distY < radiusY) {
					// toaster(string);
					return true;
				}
				return false;
			}
		};
		mv.getLayerManager().getLayers().add(blue_marker);

	}

	private void createUserMarker(double paramDouble1, double paramDouble2) {
		final LatLong localLatLong = new LatLong(paramDouble1, paramDouble2);
		if (usermarker != null)
			mv.getLayerManager().getLayers().remove(usermarker);
		this.usermarker = new TappableMarker(ReportActivity.this,
				R.mipmap.markericon, localLatLong) {
			public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
				/*
				 * mv.getModel().mapViewPosition.setCenter(tapLatLong);
				 * mv.getModel().mapViewPosition.setZoomLevel((byte) 16);
				 * toaster("Your Position");
				 */return super.onTap(tapLatLong, layerXY, tapXY);
			}

		};
		mv.getLayerManager().getLayers().add(usermarker);
		this.mv.getModel().mapViewPosition.setCenter(localLatLong);
	}

	protected void createlocationMarker(LatLong tapLatLong) {
		if (green_marker != null)
			mv.getLayerManager().getLayers().remove(green_marker);
		this.green_marker = new TappableMarker(ReportActivity.this,
				R.drawable.green, tapLatLong, "") {
			public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
				return super.onTap(tapLatLong, layerXY, tapXY);
			}

		};
		mv.getLayerManager().getLayers().add(green_marker);
	}

	String unzipLocation = Environment.getExternalStorageDirectory().getPath()
			+ "/";
	String StorezipFileLocation = Environment.getExternalStorageDirectory()
			.getPath() + "/DownloadedZip";
	String DirectoryName = Environment.getExternalStorageDirectory().getPath()
			+ "/";

	// -This is method is used for Download Zip file from server and store in
	// Desire location.
	class DownloadZipfile extends AsyncTask<String, String, String> {
		String result = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(ReportActivity.this);
			mProgressDialog.setMessage("Downloading...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... aurl) {
			int count;
			try {
				URL url = new URL(aurl[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();
				int lenghtOfFile = conexion.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(StorezipFileLocation);
				byte data[] = new byte[1024];
				long total = 0;
				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}
				output.close();
				input.close();
				result = "true";
			} catch (Exception e) {
				result = "false";
			}
			return null;
		}

		protected void onProgressUpdate(String... progress) {
			Log.d("ANDRO_ASYNC", progress[0]);
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(String unused) {
			mProgressDialog.dismiss();
			if (result.equalsIgnoreCase("true")) {
				try {
					unzip();
				} catch (IOException e) {
					Log.e("unzip_error", e.toString());
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
			}
		}
	}

	public void unzip() throws IOException {
		mProgressDialog = new ProgressDialog(ReportActivity.this);
		mProgressDialog.setMessage("Please Wait...");
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
		new UnZipTask().execute(StorezipFileLocation, DirectoryName);
	}

	private class UnZipTask extends AsyncTask<String, Void, Boolean> {
		@SuppressWarnings("rawtypes")
		@Override
		protected Boolean doInBackground(String... params) {
			Log.i("unzip", "start");
			String filePath = params[0];
			String destinationPath = params[1];
			File archive = new File(filePath);
			try {
				ZipFile zipfile = new ZipFile(archive);
				for (Enumeration e = zipfile.entries(); e.hasMoreElements();) {
					ZipEntry entry = (ZipEntry) e.nextElement();
					unzipEntry(zipfile, entry, destinationPath);
				}
				UnzipUtil d = new UnzipUtil(StorezipFileLocation, DirectoryName);
				d.unzip();
			} catch (Exception e) {
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mProgressDialog.dismiss();
			loadMap();

		}

		private void unzipEntry(ZipFile zipfile, ZipEntry entry,
				String outputDir) throws IOException {
			if (entry.isDirectory()) {
				createDir(new File(outputDir, entry.getName()));
				return;
			}
			File outputFile = new File(outputDir, entry.getName());
			if (!outputFile.getParentFile().exists()) {
				createDir(outputFile.getParentFile());
			}
			// Log.v("", "Extracting: " + entry);
			BufferedInputStream inputStream = new BufferedInputStream(
					zipfile.getInputStream(entry));
			BufferedOutputStream outputStream = new BufferedOutputStream(
					new FileOutputStream(outputFile));
			try {
			} finally {
				outputStream.flush();
				outputStream.close();
				inputStream.close();
			}
		}

		private void createDir(File dir) {
			if (dir.exists()) {
				return;
			}
			if (!dir.mkdirs()) {
				throw new RuntimeException("Can not create dir " + dir);
			}
		}
	}

}
