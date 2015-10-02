package istem.forestfire;

import istem.forestfire.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import android.widget.ImageButton;

public class MainActivity extends ActionBarActivity {

	private ImageButton report, allreport, viewonmap, about;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon);

		try {
			SessionManager session = new SessionManager(MainActivity.this);
			session.createTimesSession(1);
			new CheckService(MainActivity.this).startService(10000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		report = (ImageButton) findViewById(R.id.report);
		allreport = (ImageButton) findViewById(R.id.allreport);
		viewonmap = (ImageButton) findViewById(R.id.viewmap);
		about = (ImageButton) findViewById(R.id.about1);

		report.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent;
				intent = new Intent(MainActivity.this, ReportActivity.class);
				startActivity(intent);
				// finish();
			}
		});

		allreport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent;
				intent = new Intent(MainActivity.this,
						SavedReportsActivity.class);
				startActivity(intent);
				// finish();

			}
		});

		viewonmap.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this,
						ViewOnMapActivity.class);
				startActivity(intent);

			}
		});

		about.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this,
						AboutActivity.class);
				startActivity(intent);
			}
		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(Intent.ACTION_MAIN);

		intent.addCategory(Intent.CATEGORY_HOME);

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(intent);
	}
}
