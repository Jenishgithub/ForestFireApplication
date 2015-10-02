package istem.forestfire;

import istem.forestfire.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Hp on 6/22/2015.
 */
public class AboutActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.icon);
		getSupportActionBar().setTitle("About App");

		Button feedback = (Button) findViewById(R.id.feedback);
		feedback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(AboutActivity.this,
						SendFeedbackActivity.class);
				startActivity(intent);
			}
		});

		Button precaution = (Button) findViewById(R.id.btn_precautions);
		precaution.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(AboutActivity.this,
						Precautions.class);
				startActivity(intent);
			}
		});

	}

}
