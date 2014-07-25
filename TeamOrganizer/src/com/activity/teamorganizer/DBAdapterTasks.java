package com.activity.teamorganizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;

public class DBAdapterTasks {


	private static final String TAG = "DBAdapterTasks";
	
	// DB Fields
	public static final String KEY_ROWID = "_id";
	public static final int COL_ROWID = 0;

	// Setup your fields
	public static final String KEY_TASKNAME = "taskname";
	public static final String KEY_TASKDESC = "taskdesc";
	public static final String KEY_TASKCOMPDATE = "taskcompdate";
	public static final String KEY_TASKADDINFO = "taskaddinfo";
	
	// Setup field numbers
	public static final int COL_TASKNAME = 1;
	public static final int COL_TASKDESC = 2;
	public static final int COL_TASKCOMPDATE = 3;
	public static final int COL_TASKADDINFO = 4;

	
	public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_TASKNAME, KEY_TASKDESC, KEY_TASKCOMPDATE, KEY_TASKADDINFO};
	
	
	public static final String DATABASE_NAME = "MyDb";
	public static final String DATABASE_TABLE = "mainTable";

	public static final int DATABASE_VERSION = 2;	
	
	private static final String DATABASE_CREATE_SQL = 
			"create table " + DATABASE_TABLE 
			+ " (" + KEY_ROWID + " integer primary key autoincrement, "
			
	
			+ KEY_TASKNAME + " text not null, "
			+ KEY_TASKDESC + " test not null, "
			+ KEY_TASKCOMPDATE + " string not null, "
			+ KEY_TASKADDINFO + " text not null"
			
			
			+ ");";
	

	private final Context context;
	
	private DatabaseHelper myDBHelper;
	private SQLiteDatabase db;

	public DBAdapterTasks(Context ctx) {
		this.context = ctx;
		myDBHelper = new DatabaseHelper(context);
	}
	
	// Open the database connection.
	public DBAdapterTasks open() {
		db = myDBHelper.getWritableDatabase();
		return this;
	}
	
	// Close the database connection.
	public void close() {
		myDBHelper.close();
	}
	
	// Add a new set of values to the database.
	public long insertRow(String TName, String TDesc, String TCDate, String TAdInfo) {
		
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TASKNAME, TName);
		initialValues.put(KEY_TASKDESC, TDesc);
		initialValues.put(KEY_TASKCOMPDATE, TCDate);
		initialValues.put(KEY_TASKADDINFO, TAdInfo);
		
		// Insert it into the database.
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	// Delete a row from the database
	public boolean deleteRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		return db.delete(DATABASE_TABLE, where, null) != 0;
	}
	 //delete event and items related
    public boolean deleteTask(CharSequence taskName) {
    	Cursor c = db.query(DATABASE_TABLE, new String[]{KEY_ROWID},KEY_TASKNAME + "='" + taskName +"'", null, null, null, null, null);
    	if(c!=null){
    		c.moveToFirst();
    	}
    	String id = c.getString(c.getColumnIndex("_id"));
    	db.delete(DATABASE_TABLE, KEY_ROWID + "='" + id +"'", null);
        return db.delete(DATABASE_TABLE, KEY_TASKNAME + "='" + taskName +"'", null) > 0;
    }
	
	
	public void deleteAll() {
		Cursor c = getAllRows();
		long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
		if (c.moveToFirst()) {
			do {
				deleteRow(c.getLong((int) rowId));				
			} while (c.moveToNext());
		}
		c.close();
	}
	
	// Return all data in the database.
	public Cursor getAllRows() {
		String where = null;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
							where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	// Get a specific row (by rowId)
	public Cursor getRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
						where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	

	public boolean updateRow(long rowId, String TName, String TDesc, String TCDate, String TAdInfo) {
		String where = KEY_ROWID + "=" + rowId;

	
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_TASKNAME, TName);
		newValues.put(KEY_TASKDESC, TDesc);
		newValues.put(KEY_TASKCOMPDATE, TCDate);
		newValues.put(KEY_TASKADDINFO, TAdInfo);
		
		// Insert it into the database.
		return db.update(DATABASE_TABLE, newValues, where, null) != 0;
	}
	
	
	
	/**
	 * Private class which handles database creation and upgrading.
	 * Used to handle low-level database access.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE_SQL);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading application's database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data!");
			
			// Destroy old database:
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			
			// Recreate new database:
			onCreate(_db);
		}
	}
}
