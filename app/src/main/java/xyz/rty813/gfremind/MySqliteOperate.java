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

class MySqliteOperate {
    static Map<String, SettingModel> settingMap;
    private static MySqliteHelper helper = null;

    static void init(Context context) {
        if (helper == null) {
            helper = new MySqliteHelper(context);
        }
        settingMap = querySetting();
    }

    static boolean insert(String tableName, ContentValues contentValues) {
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
            return false;
        } finally {
            db.endTransaction();
        }
        return true;
    }

    static ArrayList<Map<String, String>> queryNotification() {
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
        cursor.close();
        return result;
    }

    static ArrayList<Map<String, String>> queryDetail(String packageName) {
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
        cursor.close();
        return result;
    }

    static void remove(String tableName, String packageName) {
        SQLiteDatabase db = helper.getReadableDatabase();
        db.beginTransaction();
        db.delete(tableName, "package=?", new String[]{packageName});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    static Map<String, SettingModel> querySetting() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("setting", new String[]{"package", "enable", "keywords", "hideMain", "hideSub", "exclude"},
                null, null, null, null, null);
        Map<String, SettingModel> result = new HashMap<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                result.put(cursor.getString(0), new SettingModel(
                        cursor.getInt(1) == 1,
                        cursor.getString(2),
                        cursor.getInt(3) == 1,
                        cursor.getInt(4) == 1,
                        cursor.getString(5)
                ));
            }
        }
        cursor.close();
        return result;
    }

    static SettingModel querySetting(String packageName) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("setting", new String[]{"enable", "keywords", "hideMain", "hideSub", "exclude"},
                "package=?", new String[]{packageName}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            SettingModel model = new SettingModel(
                    cursor.getInt(0) == 1,
                    cursor.getString(1),
                    cursor.getInt(2) == 1,
                    cursor.getInt(3) == 1,
                    cursor.getString(4)
            );
            cursor.close();
            return model;
        }
        else {
            cursor.close();
            return null;
        }
    }
}
