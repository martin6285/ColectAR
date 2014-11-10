package com.colectar.SQLite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ScannerInfoDataSource {
	  // Database fields
		private String TAG = "QR_APP";
	  private SQLiteDatabase database;
	  private ScannerInfoOpenHelper dbHelper;
	  private String[] allColumns = { ScannerInfoOpenHelper.COLUMN_ID,
	      ScannerInfoOpenHelper.COLUMN_HARVESTER,
	      ScannerInfoOpenHelper.COLUMN_INFO_READED};

	  public ScannerInfoDataSource(Context context) {
	    dbHelper = new ScannerInfoOpenHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public ScannerInfo createScannerInfo(String harvest, String info_readed) {
		 Log.i(TAG, "lalal");
	    ContentValues values = new ContentValues();
	    values.put(ScannerInfoOpenHelper.COLUMN_HARVESTER, harvest);
	    values.put(ScannerInfoOpenHelper.COLUMN_INFO_READED, info_readed);
	    long insertId = database.insert(ScannerInfoOpenHelper.SCANNERINFO_TABLE_NAME, null,
	        values);
	    Cursor cursor = database.query(ScannerInfoOpenHelper.SCANNERINFO_TABLE_NAME,
	        allColumns, ScannerInfoOpenHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    ScannerInfo newScannerInfo = cursorToScannerInfo(cursor);
	    cursor.close();
	    return newScannerInfo;
	  }

	  public void deleteScannerInfo(ScannerInfo ScannerInfo) {
	    long id = ScannerInfo.getId();
	    System.out.println("ScannerInfo deleted with id: " + id);
	    database.delete(ScannerInfoOpenHelper.SCANNERINFO_TABLE_NAME, ScannerInfoOpenHelper.COLUMN_ID
	        + " = " + id, null);
	  }

	  public List<ScannerInfo> getAllScannerInfo() {
	    List<ScannerInfo> ScannerInfos = new ArrayList<ScannerInfo>();

	    Cursor cursor = database.query(ScannerInfoOpenHelper.SCANNERINFO_TABLE_NAME,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      ScannerInfo ScannerInfo = cursorToScannerInfo(cursor);
	      ScannerInfos.add(ScannerInfo);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return ScannerInfos;
	  }

	  private ScannerInfo cursorToScannerInfo(Cursor cursor) {
	    ScannerInfo ScannerInfo = new ScannerInfo();
	    ScannerInfo.setId(cursor.getLong(0));
	    ScannerInfo.setHarvester(cursor.getString(1));
	    ScannerInfo.setInfo_readed(cursor.getString(2));
	    return ScannerInfo;
	  }
}
