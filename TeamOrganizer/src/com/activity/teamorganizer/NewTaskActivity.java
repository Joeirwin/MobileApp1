package com.activity.teamorganizer;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
//import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
//import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

import java.text.DateFormat;
import java.util.Calendar;


public class NewTaskActivity extends ActionBarActivity implements OnClickListener  {
	
	
	
	/*DateFormat Format = DateFormat.getDateInstance();
	Calendar calendarEnd = Calendar.getInstance();
	Calendar calendarStart = Calendar.getInstance();
	Button btnStart;
	Button btnEnd;
	TextView labelEnd;*/
	private Button btnStart;
	private Button btnEnd;
	private Calendar cal;
	private int day;
	private int month;
	private int year;
//	private EditText txtStart;
	private EditText txtEnd;
	
	private DBAdapterTasks mDbHelper;
	
	//Database
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_task);

		setupNewTaskButton();

		btnEnd = (Button) findViewById(R.id.btnEndDate);
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		txtEnd = (EditText) findViewById(R.id.textViewEnd);
		btnEnd.setOnClickListener(this);
		
		//Database
		 mDbHelper = new DBAdapterTasks(this);
	     mDbHelper.open();
		
	}
	
	public void onClick(View v) {
		showDialog(0);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, datePickerListener, year, month, day);
	}
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			txtEnd.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
					+ selectedYear);
		}
	};
	
	
	private void setupNewTaskButton() {
		
		Button NewTaskButton = (Button) findViewById(R.id.btnNewTask);
		
		//click listener
		NewTaskButton .setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("New Task Button", "NewTaskButton was pressed");
				final TextView TaskName = (TextView) findViewById(R.id.editTaskName);
				
				if (TaskName.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "Please enter a task name", Toast.LENGTH_SHORT).show();
				}
				else
				{
				final TextView TaskDesc = (TextView) findViewById(R.id.fTaskDesc);
				final TextView TaskCompDate = (TextView) findViewById(R.id.textViewEnd);
				final TextView TaskAdInfo = (TextView) findViewById(R.id.fAdditInfo);
				mDbHelper.insertRow(TaskName.getText().toString(), TaskDesc.getText().toString(), TaskCompDate.getText().toString(),TaskAdInfo.getText().toString());
				mDbHelper.close();
				Toast.makeText(NewTaskActivity.this, "A new Task was created", Toast.LENGTH_SHORT)
				.show();
				
				finish();
				
				}
	
			}
		});
		
	}	
	

	//@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_task, menu);
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



}
