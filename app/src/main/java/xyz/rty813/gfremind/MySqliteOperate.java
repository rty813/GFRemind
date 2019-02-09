package xyz.rty813.gfremind;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MySqliteOperate {
    private static MySqliteHelper helper;
    public static Map<String, String> keywordsMap;

    public MySqliteOperate(Context context) {
        helper = new MySqliteHelper(context);
        keywordsMap = querySetting();
    }

    public static void insert(String tableName, ContentValues contentValues) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.insertOrThrow(tableName, null, contentValues);
            db.setTransactionSuccessful();
        } catch (SQLiteConstraintException e) {
            String packageName = contentValues.getAsString("package");
            db.update(tableName, contentValues, "package=?", new String[]{packageName});
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public static ArrayList<Map<String, String>> queryNotification() {
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

    public static ArrayList<Map<String, String>> queryDetail(String packageName) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("detail", new String[]{"package", "title", "content", "time"},
                "package=?", new String[]{packageName}, null, null, "time");
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

    public static void remove(String tableName, String packageName) {
        SQLiteDatabase db = helper.getReadableDatabase();
        db.beginTransaction();
        db.delete(tableName, "package=?", new String[]{packageName});
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    public static Map<String, String> querySetting() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("setting", new String[]{"package", "keywords"}, null, null, null, null, null);
        Map<String, String> result = new HashMap<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                result.put(cursor.getString(0), cursor.getString(1));
            }
        }
        cursor.close();
        return result;
    }

    public static String querySetting(String packageName) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("setting", new String[]{"keywords"}, "package=?",
                new String[]{packageName}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String result = cursor.getString(0);
            cursor.close();
            return result;
        }
        else {
            cursor.close();
            return null;
        }
    }
}
