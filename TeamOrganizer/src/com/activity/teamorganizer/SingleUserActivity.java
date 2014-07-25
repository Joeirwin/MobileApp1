package com.activity.teamorganizer;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;

public class SingleUserActivity extends ActionBarActivity {

	private DBAdapterUsers mDbHelper;
	int userIDnum = 0;
	
	Button sendEmail;
	String msg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_user);
			
		Intent intent = getIntent();
		
		Bundle gt=getIntent().getExtras();
	    Bundle bundle = intent.getExtras();
	    final String usersID = bundle.getString("userID");
		userIDnum = Integer.valueOf(usersID);
	    mDbHelper = new DBAdapterUsers(this);        
        mDbHelper.open();
        
        Cursor c = mDbHelper.getRow(userIDnum);
       
        
        
        final TextView username = (TextView) findViewById(R.id.editTextUsername);
        final TextView userlname = (TextView) findViewById(R.id.editTextSurname);
        final TextView userpnum = (TextView) findViewById(R.id.editTextPnum);
        final TextView useremail = (TextView) findViewById(R.id.editTextEmail);
        
        username.setText(c.getString(c.getColumnIndex("userfname")));
        userlname.setText(c.getString(c.getColumnIndex("usersname")));
        userpnum.setText(c.getString(c.getColumnIndex("usernum")));
        useremail.setText(c.getString(c.getColumnIndex("useremail")));
        
		
	

        sendEmail = (Button) findViewById(R.id.btnEmailuser);
        sendEmail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				msg = "Sent from Team Orginizer";
				sendEmail(msg);
				
			}
		});
		
		
		

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	private void sendEmail(String msg) {
		
		String[] to = new String[]{"useremail"};
		String Subject =("Group Work");
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, Subject);
		emailIntent.putExtra(Intent.EXTRA_TEXT, msg);
		emailIntent.setType("message/rfc822");
		startActivity(Intent.createChooser(emailIntent, "Email"));	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.single_user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_single_user,
					container, false);
			return rootView;
		}
	}

}
