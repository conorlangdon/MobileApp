package com.example.conor.mobilecoursework;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

public class MeetingDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 14;
    private static final String DATABASE_NAME = "meetings.db";
    public static final String TABLE_MEETINGS = "meetings";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ATTENDEES = "numberOfAttendees";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LON = "lon";

    public MeetingDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    // Creates the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_MEETINGS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DATE + " DATETIME, "
                + COLUMN_ATTENDEES + " TEXT, "
                + COLUMN_NOTES + " TEXT, "
                + COLUMN_LAT  + " DOUBLE, "
                + COLUMN_LON + " DOUBLE " + ")";
        db.execSQL(query);
    }

    // If new version of database is specified then the old database is deleted and new is created with this method.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEETINGS);
        onCreate(db);
    }

    //Add a new row to the database

    public boolean addMeeting(Meeting meeting) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, meeting.getDateTime());
        values.put(COLUMN_ATTENDEES, meeting.getAttendees());
        values.put(COLUMN_NOTES, meeting.getNotes());
        values.put(COLUMN_LAT, meeting.getLat());
        values.put(COLUMN_LON, meeting.getLon());
        SQLiteDatabase db = getWritableDatabase();
        // A check to perform if the data was inserted correctly to the database
        long check = db.insert(TABLE_MEETINGS, null, values);

        // Check to see if the data was added.
        if(check == -1) {
            return false;
        } else {
            return true;
        }
    }

    //Get a list of all locations in the database for listview

    public Cursor getListOfMeetings() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_MEETINGS, null);
        return data;
    }

    //
    public Cursor getDatabaseById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_MEETINGS + " WHERE " + COLUMN_ID + " = " +  id , null);
        return data;
    }

    // Method to add an attendee into the database
    public void addAttendee(Meeting m, String attendee, int columnId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DATE, m.getDateTime());
        contentValues.put(COLUMN_ATTENDEES, m.getAttendees() + attendee);
        contentValues.put(COLUMN_NOTES, m.getNotes());
        contentValues.put(COLUMN_LAT, m.getLat());
        contentValues.put(COLUMN_LON, m.getLon());
        db.update(TABLE_MEETINGS, contentValues, "ID=" + columnId, null);
    }

}
