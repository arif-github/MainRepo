package com.example.alarmmanager;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlarmDatabase {

	// DatabaseHandlerMedicine databaseHandlerMedicine;

	SQLiteDatabase database;
	private AlarmeHandler DatabaseHandler;

	public long insert(AlarmObject object) {

		ContentValues values = new ContentValues();

		values.put(AlarmeHandler.KEY_TIME, object.AlarmTime);
		values.put(AlarmeHandler.KEY_DAYS_MASK, object.daysMasked);
		values.put(AlarmeHandler.KEY_LABEL, object.label);
		values.put(AlarmeHandler.KEY_ON_OFF, object.OnOff);
		values.put(AlarmeHandler.KEY_REPEAT, object.repeat);

		long id = -1;

		try {
			id = database.insert(AlarmeHandler.TABLE_ALARM_DETAILS, null,
					values);
		} catch (Exception e) {
			e.printStackTrace();

		}

		return id;

	}

	public long update(AlarmObject object) {

		String whereClause = AlarmeHandler.KEY_ID + " = ?";

		String[] whereArgs = { object.alarmId + "" };

		ContentValues values = new ContentValues();

		values.put(AlarmeHandler.KEY_TIME, object.AlarmTime);
		values.put(AlarmeHandler.KEY_DAYS_MASK, object.daysMasked);
		values.put(AlarmeHandler.KEY_LABEL, object.label);
		values.put(AlarmeHandler.KEY_ON_OFF, object.OnOff);
		values.put(AlarmeHandler.KEY_REPEAT, object.repeat);

		long id = -1;

		try {
			id = database.update(AlarmeHandler.TABLE_ALARM_DETAILS, values,
					whereClause, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();

		}

		return id;

	}

	public long delete(long id) {

		String whereClause = AlarmeHandler.KEY_ID + " = ?";

		String[] whereArgs = { id + "" };

		long id2 = -1;

		try {
			id2 = database.delete(AlarmeHandler.TABLE_ALARM_DETAILS,
					whereClause, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();

		}

		return id2;

	}

	private AlarmObject cursorToObject(Cursor cursor) {

		AlarmObject obj = new AlarmObject();
		obj.alarmId = cursor.getLong(0);
		obj.AlarmTime = cursor.getString(1);
		obj.repeat = cursor.getInt(2);
		obj.daysMasked = cursor.getInt(3);
		obj.OnOff = cursor.getInt(4);
		obj.label = cursor.getString(5);

		return obj;

	}

	public List<AlarmObject> getAllAlarm() {

		List<AlarmObject> list = new ArrayList<AlarmObject>();

		String sql = "select * from " + AlarmeHandler.TABLE_ALARM_DETAILS

		+ " order by " + AlarmeHandler.KEY_ID;

		String[] selectionArgs = null;

		Cursor cursor = database.rawQuery(sql, selectionArgs);

		cursor.moveToFirst();

		Log.e("TAG", "count:cusrose" + cursor.getCount());

		while (!cursor.isAfterLast()) {

			list.add(cursorToObject(cursor));

			cursor.moveToNext();

		}
		cursor.close();

		return list;

	}

	public AlarmDatabase(Context context) {
		DatabaseHandler = new AlarmeHandler(context);

	}

	public void open() {
		database = DatabaseHandler.getWritableDatabase();

	}

	public void close() {
		database.close();

	}

}

class AlarmeHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	// private static final String DATABASE_NAME = "appdatabasemedicine.db";
	private static final String DATABASE_NAME = "appdatabaseAccel.db";

	// Medicine table name
	public static final String TABLE_ALARM_DETAILS = "accelerometer_details";

	// Medicine Table Columns names
	public static final String KEY_ID = "accelerometerId";
	public static final String KEY_TIME = "stepsCount";
	public static final String KEY_REPEAT = "accelerometerDuration";
	public static final String KEY_DAYS_MASK = "stepsDate";
	public static final String KEY_ON_OFF = "caloriesBurn";
	public static final String KEY_LABEL = "distanceCover";

	String CREATE_ALARM_DETAILS_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_ALARM_DETAILS + "(" + KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_TIME + " TEXT ,"
			+ KEY_REPEAT + " INTEGER," + KEY_DAYS_MASK + " INTEGER ,"
			+ KEY_ON_OFF + " INTEGER," + KEY_LABEL + " TEXT" + ")";

	public AlarmeHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_ALARM_DETAILS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM_DETAILS);

		// Create tables again
		onCreate(db);
	}

}
