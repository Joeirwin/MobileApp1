package com.activity.teamorganizer;



import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;

public class DBAdapterUsers {
	


	
		private static final String TAG = "DBAdapterUsers";

		public static final String KEY_ROWID = "_id";
		public static final int COL_ROWID = 0;

		public static final String KEY_USERFNAME = "userfname";
		public static final String KEY_USERSNAME = "usersname";
		public static final String KEY_USERNUM = "usernum";
		public static final String KEY_USEREMAIL = "useremail";
		

		public static final int COL_USERFNAME = 1;
		public static final int COL_USERSNAME = 2;
		public static final int COL_USERNUM = 3;
		public static final int COL_USEREMAIL = 4;

		
		public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_USERFNAME, KEY_USERSNAME, KEY_USERNUM, KEY_USEREMAIL};
		
		// DB info: it's name, and the table we are using (just one).
		public static final String DATABASE_NAME = "Db";
		public static final String DATABASE_TABLE = "mainTable";
		// Track DB version if a new version of your app changes the format.
		public static final int DATABASE_VERSION = 2;	
		
		private static final String DATABASE_CREATE_SQL = 
				"create table " + DATABASE_TABLE 
				+ " (" + KEY_ROWID + " integer primary key autoincrement, "
			
				+ KEY_USERFNAME + " text not null, "
				+ KEY_USERSNAME + " test not null, "
				+ KEY_USERNUM + " string not null, "
				+ KEY_USEREMAIL + " text not null"
				
			
				+ ");";
		

		private final Context context;
		
		private DatabaseHelper myDBHelper;
		private SQLiteDatabase db;

		
		public DBAdapterUsers(Context ctx) {
			this.context = ctx;
			myDBHelper = new DatabaseHelper(context);
		}
		
		
		public DBAdapterUsers open() {
			db = myDBHelper.getWritableDatabase();
			return this;
		}
	
		public void close() {
			myDBHelper.close();
		}
		
		
		public long insertRow(String UFName, String USName, String UNum, String UEmail) {
			
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_USERFNAME, UFName);
			initialValues.put(KEY_USERSNAME, USName);
			initialValues.put(KEY_USERNUM, UNum);
			initialValues.put(KEY_USEREMAIL, UEmail);
			
			// Insert it into the database.
			return db.insert(DATABASE_TABLE, null, initialValues);
		}
		
		// Delete a row from the database
		public boolean deleteRow(long rowId) {
			String where = KEY_ROWID + "=" + rowId;
			return db.delete(DATABASE_TABLE, where, null) != 0;
		}
		 //delete event and item
	    public boolean deleteTask(CharSequence UFName) {
	    	Cursor c = db.query(DATABASE_TABLE, new String[]{KEY_ROWID},KEY_USERFNAME + "='" + UFName +"'", null, null, null, null, null);
	    	if(c!=null){
	    		c.moveToFirst();
	    	}
	    	String id = c.getString(c.getColumnIndex("_id"));
	    	db.delete(DATABASE_TABLE, KEY_ROWID + "='" + id +"'", null);
	        return db.delete(DATABASE_TABLE, KEY_USERFNAME + "='" + UFName +"'", null) > 0;
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
		
		// Change an existing row to be equal to new data.
		public boolean updateRow(long rowId, String UFName, String USName, String UNum, String UEmail) {
			String where = KEY_ROWID + "=" + rowId;

	
			ContentValues newValues = new ContentValues();
			newValues.put(KEY_USERFNAME, UFName);
			newValues.put(KEY_USERSNAME, USName);
			newValues.put(KEY_USERNUM, UNum);
			newValues.put(KEY_USEREMAIL, UEmail);
			
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



