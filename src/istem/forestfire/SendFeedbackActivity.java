package istem.forestfire;

import istem.forestfire.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Hp on 6/22/2015.
 */
public class SendFeedbackActivity extends ActionBarActivity
{
    private EditText message;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon);

        toast= Toast.makeText(SendFeedbackActivity.this,"",Toast.LENGTH_SHORT);
        message=(EditText)findViewById(R.id.fb_message);

    }

    public void btnFeedbackOnClick(View v) {
        if(!message.getText().toString().equals("")) {
            final Intent _Intent = new Intent(android.content.Intent.ACTION_SEND);
            _Intent.setData(Uri.parse("mailto:"));
            _Intent.setType("text/html");
            _Intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{" info@crossovernepal.com"});
            _Intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Report Forest Fire Feedback");
            _Intent.putExtra(android.content.Intent.EXTRA_TEXT, message.getText().toString());
            startActivity(Intent.createChooser(_Intent, "Send Feedback"));
        }
        else
        {
            toaster("Write Some Feedback First");
        }
    }

    private void toaster(String message) {
        toast.setText(message);
        toast.show();
    }

}
