package istem.forestfire;

import istem.forestfire.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends Activity {

	ProgressBar p;
	int progressstatus = 0;
	Handler h = new Handler();
	@SuppressWarnings("unused")
	private TextView version;
	private boolean register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		SessionManager session = new SessionManager(SplashActivity.this);
		register = session.isUserRegistered();
		p = (ProgressBar) findViewById(R.id.progressBar1);
		version = (TextView) findViewById(R.id.version);
		Thread t = new Thread(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				while (progressstatus < 100) {
					progressstatus += 1;
					h.post(new Runnable() {
						public void run() {
							// TODO Auto-generated method stub
							p.setProgress(progressstatus);
						}
					});
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (progressstatus == 100) {
						if (register) {
							Intent i = new Intent(getApplicationContext(),
									MainActivity.class);
							startActivity(i);
							finish();
						} else {
							Intent i = new Intent(getApplicationContext(),
									RegisterActivity.class);
							startActivity(i);
							finish();
						}
					}
				} // while

			} // run
		} //
		);
		t.start();
	}
}
