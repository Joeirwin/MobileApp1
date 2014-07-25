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
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;

public class SingleActivityView extends ActionBarActivity {
	
	private DBAdapterTasks mDbHelper;
	int taskIDnum = 0;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_activity_view);
		
		Intent intent = getIntent();
		
		Bundle gt=getIntent().getExtras();
	    Bundle bundle = intent.getExtras();
	    final String taskID = bundle.getString("TaskID");
		taskIDnum = Integer.valueOf(taskID);
	    mDbHelper = new DBAdapterTasks(this);        
        mDbHelper.open();
        
        Cursor c = mDbHelper.getRow(taskIDnum);
        
        
        
        final TextView taskname = (TextView) findViewById(R.id.tnameView);
        final TextView taskDesc = (TextView) findViewById(R.id.tdescView);
        final TextView tCompDate = (TextView) findViewById(R.id.tcompDate);
        final TextView tAddInfo = (TextView) findViewById(R.id.taddView);
        
        taskname.setText(c.getString(c.getColumnIndex("taskname")));
        taskDesc.setText(c.getString(c.getColumnIndex("taskdesc")));
        tCompDate.setText(c.getString(c.getColumnIndex("taskcompdate")));
        tAddInfo.setText(c.getString(c.getColumnIndex("taskaddinfo")));
        
        
 
        
		
		
		

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
			
		
			
			
			
			
			
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.single_activity_view, menu);
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
			View rootView = inflater.inflate(
					R.layout.fragment_single_activity_view, container, false);
			return rootView;
		}
	}

}
