package com.tapster.barMenu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class AddonDB {
	

		private static final String TAG = "DBAdapter";
		
		
		public static final String KEY_ID = "ID";
		public static final String KEY_RESTAURANT_ID = "Restaurant_ID";
		public static final String KEY_CATEGORY = "Category";
		public static final String KEY_NAME = "Name";
		public static final String KEY_PRICE = "Price";
		public static final String KEY_DESCRIPTION = "Description";
		public static final String KEY_DEPRECATED = "Deprecated";
		
		public static final int COL_ID = 0;
		public static final int COL_RESTAURANT_ID = 1;
		public static final int COL_CATEGORY = 2;
		public static final int COL_NAME = 3;
		public static final int COL_PRICE = 4;
		public static final int COL_DESCRIPTION = 5;
		public static final int COL_DEPRECATED = 6;
		
				
		public static final String[] ALL_KEYS = new String[] {KEY_ID, KEY_RESTAURANT_ID, KEY_CATEGORY, KEY_NAME, KEY_PRICE, KEY_DESCRIPTION, KEY_DEPRECATED};
		
		public static final String DATABASE_NAME = "theDB";
		public static final String DATABASE_TABLE = "additions";
		public static final int DATABASE_VERSION = 2;	
		
		private static final String DATABASE_CREATE_SQL = 
				"create table IF NOT EXISTS " + DATABASE_TABLE 
				+ " (" + KEY_ID + " integer primary key autoincrement, "
				+ KEY_RESTAURANT_ID + " integer, "
				+ KEY_CATEGORY + " integer not null, "
				+ KEY_NAME + " text not null, "
				+ KEY_PRICE + " real not null, "
				+  KEY_DESCRIPTION + " text, "
				+  KEY_DEPRECATED + " integer not null"
				+ ");";
		
		private final Context context;
		private DatabaseHelper2 myDBHelper;
		private SQLiteDatabase DB;

		public AddonDB(OrderConfirmDialog confirmDialog) {
			this.context = confirmDialog.getContext();
			myDBHelper = new DatabaseHelper2(context);
		}
		
		public AddonDB open() {
			DB = myDBHelper.getWritableDatabase();
			DB.execSQL(DATABASE_CREATE_SQL);
			return this;
		}

		public void close() {
			myDBHelper.close();
		}
		
		public long insertRow(int restaurantID, String category, String name, float price, String description, int deprecated) {
			
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_RESTAURANT_ID, restaurantID);
			initialValues.put(KEY_CATEGORY, category);
			initialValues.put(KEY_NAME, name);
			initialValues.put(KEY_PRICE, price);
			initialValues.put(KEY_DESCRIPTION, description);
			initialValues.put(KEY_DEPRECATED, deprecated);
			
			return DB.insert(DATABASE_TABLE, null, initialValues);
		}
		
		public boolean deleteRow(long rowId) {
			String where = KEY_ID + "=" + rowId;
			return DB.delete(DATABASE_TABLE, where, null) != 0;
		}
		
		public void deleteAll() {
			Cursor c = getAllRows();
			long rowId = c.getColumnIndexOrThrow(KEY_ID);
			if (c.moveToFirst()) {
				do {
					deleteRow(c.getLong((int) rowId));				
				} while (c.moveToNext());
			}
			c.close();
		}
		
		public Cursor getAllRows() {
			String where = null;
			Cursor c = 	DB.query(true, DATABASE_TABLE, ALL_KEYS, 
								where, null, null, null, null, null);
			if (c != null) {
				c.moveToFirst();
			}
			return c;
		}
		
		public Cursor getAllRowsOfCat(int category) {
			String where = "Category ='" + category+"'";
			Cursor c = 	DB.query(true, DATABASE_TABLE, ALL_KEYS, 
					where, null, null, null, null, null);
			if (c != null) {
				c.moveToFirst();
			}
			return c;
			
		}

		public Cursor getRow(long rowId) {
			String where = KEY_ID + "=" + rowId;
			Cursor c = 	DB.query(true, DATABASE_TABLE, ALL_KEYS, 
							where, null, null, null, null, null);
			if (c != null) {
				c.moveToFirst();
			}
			return c;
		}
		
		
		private static class DatabaseHelper2 extends SQLiteOpenHelper
		{
			DatabaseHelper2(Context context) {
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
				
				_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
				
				onCreate(_db);
			}
		}






}
