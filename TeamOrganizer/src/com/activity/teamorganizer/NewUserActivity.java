package com.activity.teamorganizer;

import java.util.Calendar;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class NewUserActivity extends ActionBarActivity {
	

	private DBAdapterUsers mDbHelper;
	
	//Database

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
			

			setupNewTaskButton();
	
			 mDbHelper = new DBAdapterUsers(this);
		     mDbHelper.open();
		}
	}

	
	
	private void setupNewTaskButton() {
		
		Button NewTaskButton = (Button) findViewById(R.id.btnSubmit);
		
		//click listener
		NewTaskButton .setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("submit Button", "submit was pressed");
				final TextView FName = (TextView) findViewById(R.id.txtFFname);
				
				if (FName.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
				}
				else
				{
				final TextView SName = (TextView) findViewById(R.id.txtFSname);
				final TextView UserNum = (TextView) findViewById(R.id.txtFPhone);
				final TextView UserEm = (TextView) findViewById(R.id.txtFEmail);
				mDbHelper.insertRow(FName.getText().toString(), SName.getText().toString(), UserNum.getText().toString(),UserEm.getText().toString());
				mDbHelper.close();
				Toast.makeText(NewUserActivity.this, "A new user was created", Toast.LENGTH_SHORT)
				.show();
				
				finish();
				
				}
				
			}
		});
		
	}	
	
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_user, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_new_user,
					container, false);
			return rootView;
		}
	}

}
