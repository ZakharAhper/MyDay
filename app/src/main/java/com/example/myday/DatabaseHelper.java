package com.example.myday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_day.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ENTRIES = "entries";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_MOOD = "mood";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_ENTRIES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_SCORE + " INTEGER NOT NULL, "
                + COLUMN_MOOD + " TEXT NOT NULL, "
                + COLUMN_COMMENT + " TEXT, "
                + COLUMN_DATE + " TEXT NOT NULL"
                + ")";

        db.execSQL(createTable);
    }

    public boolean addEntry(int score, String mood, String comment, String date) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE, score);
        values.put(COLUMN_MOOD, mood);
        values.put(COLUMN_COMMENT, comment);
        values.put(COLUMN_DATE, date);

        long result = db.insert(TABLE_ENTRIES, null, values);
        db.close();

        return result != -1;
    }

    public boolean updateEntry(int id, int score, String mood, String comment) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE, score);
        values.put(COLUMN_MOOD, mood);
        values.put(COLUMN_COMMENT, comment);

        int result = db.update(
                TABLE_ENTRIES,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return result > 0;
    }

    public boolean hasEntryForDate(String datePrefix) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_ENTRIES,
                new String[]{COLUMN_ID},
                COLUMN_DATE + " LIKE ?",
                new String[]{datePrefix + "%"},
                null,
                null,
                null
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }

    public int getScoreByDate(String datePrefix) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_ENTRIES,
                new String[]{COLUMN_SCORE},
                COLUMN_DATE + " LIKE ?",
                new String[]{datePrefix + "%"},
                null,
                null,
                null
        );

        int score = 0;

        if (cursor.moveToFirst()) {
            score = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE));
        }

        cursor.close();
        db.close();

        return score;
    }

    public ArrayList<DayEntry> getAllEntries() {
        ArrayList<DayEntry> entries = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_ENTRIES,
                null,
                null,
                null,
                null,
                null,
                COLUMN_ID + " DESC"
        );

        fillEntriesFromCursor(entries, cursor);

        cursor.close();
        db.close();

        return entries;
    }

    public ArrayList<DayEntry> getEntriesByDate(String datePrefix) {
        ArrayList<DayEntry> entries = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_ENTRIES,
                null,
                COLUMN_DATE + " LIKE ?",
                new String[]{datePrefix + "%"},
                null,
                null,
                COLUMN_ID + " DESC"
        );

        fillEntriesFromCursor(entries, cursor);

        cursor.close();
        db.close();

        return entries;
    }

    private void fillEntriesFromCursor(ArrayList<DayEntry> entries, Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE));
                String mood = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOOD));
                String comment = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMMENT));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));

                entries.add(new DayEntry(id, score, mood, comment, date));
            } while (cursor.moveToNext());
        }
    }

    public int getEntriesCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ENTRIES, null);

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }

    public double getAverageScore() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(" + COLUMN_SCORE + ") FROM " + TABLE_ENTRIES, null);

        double average = 0;

        if (cursor.moveToFirst()) {
            average = cursor.getDouble(0);
        }

        cursor.close();
        db.close();

        return average;
    }

    public int getMinScore() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MIN(" + COLUMN_SCORE + ") FROM " + TABLE_ENTRIES, null);

        int min = 0;

        if (cursor.moveToFirst()) {
            min = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return min;
    }

    public int getMaxScore() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(" + COLUMN_SCORE + ") FROM " + TABLE_ENTRIES, null);

        int max = 0;

        if (cursor.moveToFirst()) {
            max = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return max;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        onCreate(db);
    }
}