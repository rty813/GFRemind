package xyz.rty813.gfremind;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 11;
    private static final String DB_NAME = "myTest.db";

    public MySqliteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists notification (id integer primary key autoincrement, package text unique not null, title text, content text, time text)";
        sqLiteDatabase.execSQL(sql);
        sql = "create table if not exists detail (id integer primary key autoincrement, package text not null, title text, content text, time text)";
        sqLiteDatabase.execSQL(sql);
        sql = "create table if not exists setting (id integer primary key autoincrement, package text unique not null, enable integer, keywords text, hideMain integer, exclude text, hideSub integer)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        String sql = "DROP TABLE IF EXISTS notification";
//        sqLiteDatabase.execSQL(sql);
        String sql = "DROP TABLE IF EXISTS detail";
        sqLiteDatabase.execSQL(sql);
//        sql = "DROP TABLE IF EXISTS setting";
//        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
