package com.activity.teamorganizer;

import java.util.ArrayList;

import com.activity.teamorganizer.TasksActivity.PlaceholderFragment;

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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.os.Build;

public class UserActivity extends ActionBarActivity {
	
	private DBAdapterUsers mDbHelper;
	private ArrayList<String> UserList = new ArrayList<String>();
	private CharSequence[] UserItems = new CharSequence[0];
	
	Intent intent = getIntent();
	private Cursor c;

		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
			
		}
			
			setupAddTaskButton();
			setUpDeleteUserButton();
			setUpOnClickList();
			
			//DATABASE POPULATE
			 mDbHelper = new DBAdapterUsers(this);        
		     mDbHelper.open();
		  putInList();
		

		
	}

	



	private void setupAddTaskButton() {
		
		Button AddTaskButton = (Button) findViewById(R.id.BtnSignUp);
		
		//click listener
		AddTaskButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("User Button", "Add User Button was pressed");
				
				//Add task Toast
				Toast.makeText(UserActivity.this, "Add User", Toast.LENGTH_SHORT)
				.show();
				startActivity(new Intent(UserActivity.this, NewUserActivity.class));
				
				
			}
		});
	
	}
	
	private void setUpDeleteUserButton() {
		
		Button btDelete = (Button) findViewById(R.id.btnRemoveUser);
	    btDelete.setOnClickListener(new OnClickListener()
	    {
	    		@Override
				public void onClick(View v) {      		
	    				showDialog(0);        	
	    	}
	    });
	    //  putInList();
		}
	

	private void putInList() {
		Cursor c = mDbHelper.getAllRows();
		
		//Cursor life time
		startManagingCursor(c);
		
		//Cursor map to view fields
		String [] fieldNames = new String[]
				{
				DBAdapterUsers.KEY_ROWID, 
				DBAdapterUsers.KEY_USERFNAME,
				DBAdapterUsers.KEY_USERSNAME,
				DBAdapterUsers.KEY_USERNUM, 
				DBAdapterUsers.KEY_USEREMAIL		
				};
		int [] getIds = new int []
				{	
				R.id.textViewUserID,
				R.id.textViewFname,
				R.id.textViewSname,
				R.id.textViewNum,
				R.id.textviewEmail
				};
				
		
		SimpleCursorAdapter cAdapter = new SimpleCursorAdapter
				(this, R.layout.user_layout, c, fieldNames,getIds);
		
		ListView UserList = (ListView) findViewById(R.id.listViewUsers);
		UserList.setAdapter(cAdapter);
	}
		

	private void setUpOnClickList() {
		ListView list = (ListView) findViewById(R.id.listViewUsers);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			
				Cursor c = mDbHelper.getRow(id);
				if (c.moveToFirst()){
					String idDB = c.getString(DBAdapterUsers.COL_ROWID);
					String message = "ID = " + idDB;
				
					Bundle basket= new Bundle();
					basket.putString("userID", idDB);
					Intent i = new Intent(UserActivity.this, SingleUserActivity.class);
					i.putExtras(basket);
					startActivity(i);
					
					
					//Toast.makeText(UserActivity.this, message, Toast.LENGTH_SHORT).show();
				
				}
				
				c.close();		 
				}
			});
	}
	

	    @Override
    protected Dialog onCreateDialog(int id){
    	switch (id){
    	case 0:
    	UserItems = UserList.toArray(new CharSequence[UserList.size()]);
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Select the user you woud like to remove");
    	builder.setItems(UserItems, new DialogInterface.OnClickListener(){
    		public void onClick(DialogInterface dialog, int item){
    		mDbHelper.deleteTask(UserItems[item]);
    		
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
    		UserList.clear();
    		Cursor c = mDbHelper.getAllRows();
        	startManagingCursor(c);
        	c.moveToFirst();
        	for(int i = 0; i<c.getCount(); i++){
        		UserList.add(c.getString(c.getColumnIndex("userfname")));
        		c.moveToNext();
        	}    	
        	UserItems = UserList.toArray(new CharSequence[UserList.size()]);
            AlertDialog ad = (AlertDialog) dialog; 
            
            ad.getListView().setAdapter(new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, UserList));  
            
    	}
    
    	
 
    }
	
	
	
	
	
	
	

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
    	int id = item.getItemId();
    	 switch(item.getItemId()){
		 case R.id.mainmenu_Task: Intent i = new Intent(UserActivity.this, TasksActivity.class);
								 startActivity(i);
								 break;
		 case R.id.mainmenu_Users: Intent i1 = new Intent(UserActivity.this, UserActivity.class);
		 							startActivity(i1);
		 							break;
			
		 		
		 }
		 return true;
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
			View rootView = inflater.inflate(R.layout.fragment_user, container,
					false);
			return rootView;
		}
	}

}
