package com.example.lostfoundapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CRUD {
    private SQLiteDatabase database;
    private LostFoundDatabase dbHelper;

    public CRUD(Context context) {
        dbHelper = new LostFoundDatabase(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addAdvertisement(Advertisement advertisement) {
        ContentValues values = new ContentValues();
        values.put(LostFoundDatabase.COLUMN_TYPE, advertisement.getType());
        values.put(LostFoundDatabase.COLUMN_NAME, advertisement.getName());
        values.put(LostFoundDatabase.COLUMN_PHONE, advertisement.getPhone());
        values.put(LostFoundDatabase.COLUMN_DESCRIPTION, advertisement.getDescription());
        values.put(LostFoundDatabase.COLUMN_DATE, advertisement.getDate());
        values.put(LostFoundDatabase.COLUMN_LOCATION, advertisement.getLocation());
        values.put(LostFoundDatabase.COLUMN_PLACE_NAME, advertisement.getPlaceName()); // Added place name
        long insertId = database.insert(LostFoundDatabase.TABLE_NAME, null, values);
        advertisement.setId(insertId);
    }

    public List<Advertisement> getAllAdvertisements() {
        List<Advertisement> advertisements = new ArrayList<>();
        Cursor cursor = database.query(LostFoundDatabase.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Advertisement ad = createAdvertisementFromCursor(cursor);
                if (ad != null) {
                    advertisements.add(ad);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return advertisements;
    }

    private Advertisement createAdvertisementFromCursor(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(LostFoundDatabase.COLUMN_ID);
        int typeIndex = cursor.getColumnIndex(LostFoundDatabase.COLUMN_TYPE);
        int nameIndex = cursor.getColumnIndex(LostFoundDatabase.COLUMN_NAME);
        int phoneIndex = cursor.getColumnIndex(LostFoundDatabase.COLUMN_PHONE);
        int descriptionIndex = cursor.getColumnIndex(LostFoundDatabase.COLUMN_DESCRIPTION);
        int dateIndex = cursor.getColumnIndex(LostFoundDatabase.COLUMN_DATE);
        int locationIndex = cursor.getColumnIndex(LostFoundDatabase.COLUMN_LOCATION);
        int placeNameIndex = cursor.getColumnIndex(LostFoundDatabase.COLUMN_PLACE_NAME);

        if (idIndex != -1 && typeIndex != -1 && nameIndex != -1 && phoneIndex != -1 &&
                descriptionIndex != -1 && dateIndex != -1 && locationIndex != -1 && placeNameIndex != -1) {
            return new Advertisement(
                    cursor.getLong(idIndex),
                    cursor.getString(typeIndex),
                    cursor.getString(nameIndex),
                    cursor.getString(phoneIndex),
                    cursor.getString(descriptionIndex),
                    cursor.getString(dateIndex),
                    cursor.getString(locationIndex),
                    cursor.getString(placeNameIndex) // Added place name
            );
        }
        return null;
    }

    public void deleteAdvertisement(long id) {
        database.delete(LostFoundDatabase.TABLE_NAME, LostFoundDatabase.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Advertisement getAdvertisement(long id) {
        Advertisement advertisement = null;
        Cursor cursor = database.query(LostFoundDatabase.TABLE_NAME, null,
                LostFoundDatabase.COLUMN_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            advertisement = createAdvertisementFromCursor(cursor);
            cursor.close();
        }
        return advertisement;
    }
}
