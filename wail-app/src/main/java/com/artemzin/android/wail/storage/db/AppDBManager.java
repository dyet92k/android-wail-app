package com.artemzin.android.wail.storage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDBManager extends SQLiteOpenHelper {

    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "WAIL_DB";

    private static volatile AppDBManager instance;
    private Context context;

    private AppDBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public static AppDBManager getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDBManager.class) {
                if (instance == null) {
                    instance = new AppDBManager(context.getApplicationContext());
                }
            }
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TracksDBHelper.TableInfo.CREATE_TABLE_QUERY);
        db.execSQL(PlayersDBHelper.TableInfo.CREATE_TABLE_QUERY);
        db.execSQL(LovedTracksDBHelper.TableInfo.CREATE_TABLE_QUERY);
        db.execSQL(IgnoredPlayersDBHelper.TableInfo.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1: // Upgrade from 1 to 2
                db.execSQL(LovedTracksDBHelper.TableInfo.CREATE_TABLE_QUERY);
            case 2: // Upgrade from 2 to 3
                db.execSQL(IgnoredPlayersDBHelper.TableInfo.CREATE_TABLE_QUERY);
        }
    }

    @Override
    public synchronized void close() {
        super.close();
        instance = null;
    }

    public static Boolean convertIntegerToBoolean(int value) {
        if (value < 0) return false;
        if (value > 0) return true;
        else return null;
    }

    public static int convertBooleanToInteger(Boolean value) {
        if (value == null) return 0;
        return value ? 1 : -1;
    }

    public void clearAll() {
        PlayersDBHelper.getInstance(context).removeAll();
        TracksDBHelper.getInstance(context).deleteAll();
        LovedTracksDBHelper.getInstance(context).deleteAll();
        IgnoredPlayersDBHelper.getInstance(context).deleteAll();
    }
}
