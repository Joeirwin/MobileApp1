package com.activity.teamorganizer;

import java.util.ArrayList;



import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class TasksActivity extends ActionBarActivity {
	
	private DBAdapterTasks mDbHelper;
	private ArrayList<String> TaskList = new ArrayList<String>();
	private CharSequence[] TaskItems = new CharSequence[0];
	
	Intent intent = getIntent();
	private Cursor c;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasks);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		setupAddTaskButton();
		setUpDeleteTaskButton();
		setUpOnClickList();
		
		//DATABASE POPULATE
		 mDbHelper = new DBAdapterTasks(this);        
	     mDbHelper.open();
	     putInList();
	     
	}
		



	private void setupAddTaskButton() {
		
		Button AddTaskButton = (Button) findViewById(R.id.BtAddTasks);
		
		//click listener
		AddTaskButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("Task Button", "AddTaskButton was pressed");
				
				//Add task Toast
				Toast.makeText(TasksActivity.this, "Add Task", Toast.LENGTH_SHORT)
				.show();
				
				startActivity(new Intent(TasksActivity.this, NewTaskActivity.class));
				
				
			}
		});
	
	}
	private void setUpDeleteTaskButton() {
	
	Button btDelete = (Button) findViewById(R.id.btndelete);
    btDelete.setOnClickListener(new OnClickListener()
    {
    		@Override
			public void onClick(View v) {      		
    				showDialog(0);        	
    	}
    });
    //  putInList();
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
			View rootView = inflater.inflate(R.layout.fragment_events,
					container, false);
			return rootView;
		}
	}
	

	private void putInList() {
		Cursor c = mDbHelper.getAllRows();
		
		//Cursor life time
		startManagingCursor(c);
		
		//Cursor map to view fields
		String [] fieldNames = new String[]
				{
				DBAdapterTasks.KEY_ROWID, 
				DBAdapterTasks.KEY_TASKNAME,
				DBAdapterTasks.KEY_TASKDESC, 
				DBAdapterTasks.KEY_TASKCOMPDATE, 
				DBAdapterTasks.KEY_TASKADDINFO			
				};
		int [] getIds = new int []
				{	
				R.id.taskId, 
				R.id.taskName, 
				R.id.taskdesc,
				R.id.compDate, 
				R.id.taskAddInfo 
				};
				
		
		SimpleCursorAdapter cAdapter = new SimpleCursorAdapter
				(this, R.layout.task_layout, c, fieldNames,getIds);
		
		ListView taskList = (ListView) findViewById(R.id.listViewTasks);
		taskList.setAdapter(cAdapter);
		
		
		
	}
	private void setUpOnClickList() {
		ListView list = (ListView) findViewById(R.id.listViewTasks);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			
				Cursor c = mDbHelper.getRow(id);
				if (c.moveToFirst()){
					String idDB = c.getString(DBAdapterTasks.COL_ROWID);
					String message = "ID = " + idDB;
				
					Bundle basket= new Bundle();
					basket.putString("TaskID", idDB);
					Intent i = new Intent(TasksActivity.this, SingleActivityView.class);
					i.putExtras(basket);
					startActivity(i);
					
					
				//	Toast.makeText(TasksActivity.this, message, Toast.LENGTH_SHORT).show();
				

				//startActivity(new Intent(TasksActivity.this, SingleActivityView.class));
	
				}
				
				c.close();		 
				}
			});
	}
	
   
	
	
	    @Override
    protected Dialog onCreateDialog(int id){
    	switch (id){
    	case 0:
    	TaskItems = TaskList.toArray(new CharSequence[TaskList.size()]);
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Select the task you want to delete");
    	builder.setItems(TaskItems, new DialogInterface.OnClickListener(){
    		public void onClick(DialogInterface dialog, int item){
    		mDbHelper.deleteTask(TaskItems[item]);
    		
    		//refresh activity
    			Intent intent = getIntent();
    		    finish();
    		    startActivity(intent); 	
    		}
    	});
    	AlertDialog alert = builder.create();
    	return alert;
    	}
		return null;	
    	
    }
    @Override
    protected void onPrepareDialog(int id, Dialog dialog){
    	switch (id){
    	case 0:
    		TaskList.clear();
    		Cursor c = mDbHelper.getAllRows();
        	startManagingCursor(c);
        	c.moveToFirst();
        	for(int i = 0; i<c.getCount(); i++){
        		TaskList.add(c.getString(c.getColumnIndex("taskname")));
        		c.moveToNext();
        	}    	
        	TaskItems = TaskList.toArray(new CharSequence[TaskList.size()]);
            AlertDialog ad = (AlertDialog) dialog; 
            
            ad.getListView().setAdapter(new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, TaskList));  
            
    	}
    
    	
 
    }
    
  //  public void onClick_Delete(View v) {
  //  	mDbHelper.deleteAll();
//	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	// Inflate the menu; this adds items to the action bar if it is present.
    	getMenuInflater().inflate(R.menu.events, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Handle action bar item clicks here. The action bar will
    	// automatically handle clicks on the Home/Up button, so long
    	// as you specify a parent activity in AndroidManifest.xml.
    	 switch(item.getItemId()){
		 case R.id.mainmenu_Task: Intent i = new Intent(TasksActivity.this, TasksActivity.class);
								 startActivity(i);
								 break;
		 case R.id.mainmenu_Users: Intent i1 = new Intent(TasksActivity.this, UserActivity.class);
		 							startActivity(i1);
		 							break;
			
		 		
		 }
		 return true;
    	}
}
