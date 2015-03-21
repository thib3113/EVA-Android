package fr.thib3113.eva;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thibaut on 21/03/2015.
 */
public class MySql extends SQLiteOpenHelper {

    static public int BASE_VERSION = 1;
    static public String BASE_NAME = "configs.db";

    public MySql(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
