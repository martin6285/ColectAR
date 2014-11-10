package com.colectar.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScannerInfoOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "scannerQRApp";
    public static final String SCANNERINFO_TABLE_NAME = "scannerInfo";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_HARVESTER = "harvester";
    public static final String COLUMN_INFO_READED = "info_readed";
    private static final String SCANNERINFO_TABLE_CREATE =
                "CREATE TABLE " + SCANNERINFO_TABLE_NAME + " (" 
                + COLUMN_ID 
                + " integer primary key autoincrement, "  
                + COLUMN_HARVESTER + " text not null,"
                + COLUMN_INFO_READED + " text not null);";
    ScannerInfoOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SCANNERINFO_TABLE_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}
