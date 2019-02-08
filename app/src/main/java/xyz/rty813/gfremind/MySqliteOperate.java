package xyz.rty813.gfremind;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MySqliteOperate {
    private MySqliteHelper helper;

    public MySqliteOperate(Context context) {
        helper = new MySqliteHelper(context);
    }

    public void insert(String tableName, ContentValues contentValues) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.insertOrThrow(tableName, null, contentValues);
            db.setTransactionSuccessful();
        } catch (SQLiteConstraintException e) {
            String packageName = contentValues.getAsString("package");
//            contentValues.remove("package");
            db.update(tableName, contentValues, "package=?", new String[]{packageName});
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Map<String, String>> queryNotification() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("notification", new String[]{"package", "title", "content", "time"}, null, null, null, null, "time");
        ArrayList<Map<String, String>> result = new ArrayList<>(cursor.getCount());
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<>();
                map.put("package", cursor.getString(0));
                map.put("title", cursor.getString(1));
                map.put("content", cursor.getString(2));
                map.put("time", cursor.getString(3));
                result.add(map);
            }
        }
        Collections.reverse(result);
        return result;
    }

    public ArrayList<Map<String, String>> querySetting() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("setting", new String[]{"package", "keywords"}, null, null, null, null, null);
        ArrayList<Map<String, String>> result = new ArrayList<>(cursor.getCount());
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<>();
                map.put("package", cursor.getString(0));
                map.put("keywords", cursor.getString(1));
                result.add(map);
            }
        }
        return result;
    }
}
