package com.colectar.scannerqrapp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.colectar.SQLite.ScannerInfo;
import com.colectar.ftp.FtpConnection;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class FileManager {

	private static final String LOG_TAG = "QR_APP";
	private Context context;

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public static File getPodCastsStorageDir(String fileName) {
	    // Get the directory for the user's public pictures directory.
	    File file = new File(Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PODCASTS), fileName);
	    if (!file.mkdirs()) {
	        Log.e(LOG_TAG, "Directory not created");
	    }
	    return file;
	}

	
	public void writeFile(String fileName, String harvest, String info_readed){
		//File file = getPodCastsStorageDir(fileName);
		final String        packageName     = "com.scannerQrApp";
	    final String        folderpath      = Environment.getDataDirectory().getAbsolutePath() + "/" + packageName + "/files/";
	    File                folder          = new File(folderpath);
	    File                file            = null;
	    File                file2            = null;
	    FileOutputStream    fOut            = null;
	    FileOutputStream    fOut2            = null;

	    Log.i(LOG_TAG, "Test writeFile");
	    Log.i(LOG_TAG, "packageName:" + packageName + " - folderpath:" + folderpath );
	    Log.i(LOG_TAG, "fileName:" + fileName + " - harvest:" + harvest + " - info_readed:" + info_readed );

	      
	    try {
	        try {
	        	Log.i(LOG_TAG, "isExternalStorageReadable()= "+ isExternalStorageReadable());
	        	Log.i(LOG_TAG, "isExternalStorageWritable()= "+ isExternalStorageWritable());
	        	
	        	File filex = new File(context.getFilesDir(), fileName);
	        	
	        } catch (Exception e) {
	            Log.e(LOG_TAG, "Error in writeFile. "+ e.getMessage());
	        }

	    } finally {
	        if (fOut != null) {
	            try {
	                fOut.flush();
	                //fOut2.flush();
	                Log.i(LOG_TAG, "Flush...");
	                fOut.close();
	                //fOut2.close();
	                Log.i(LOG_TAG, "Close...");
	            } catch (IOException e) {
	                Log.e(LOG_TAG, "Error on close File. "+ e.getMessage());
	            }
	        }
	    }
	}

	private String getFileName(Activity act){

		String fileName = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date(); 
		String mask = sdf.format(date);

		TelephonyManager tm = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
		// get IMEI
		String imei = tm.getDeviceId();
		//String phone = tm.getLine1Number();

		
		//String path = Environment.getExternalStorageDirectory().toString()+"/Pictures";
		String filePath = act.getApplication().getFilesDir().getAbsolutePath()
                + File.separator;
		Log.i(LOG_TAG, "Path: " + filePath);
		
		fileName = imei + "_" + mask + "_";
		
		File f = new File(filePath);
		File file[] = f.listFiles();
		Log.i(LOG_TAG, "Size: "+ file.length);

		//Busco el archivo con el ultimo incremento
		int increment = 1;
		for (int i=0; i < file.length; i++)
		{
		    Log.i(LOG_TAG, "FileName:" + file[i].getName());
		    if (file[i].getName().equals(fileName + (increment + 1)) ){
		    	increment++;
		    }
		}
		String incrementPad = "0000" + increment;
		incrementPad = incrementPad.substring( ("" + increment).length() - 1, incrementPad.length() - 1); 
		
		return imei + "_" + mask + "_" + incrementPad + ".csv";

	}
	
	private void write(Activity act, String fileName, String line){
		try {
	        FileOutputStream fos = act.openFileOutput(fileName,
	                Context.MODE_APPEND | Context.MODE_WORLD_READABLE);
	        fos.write(line.getBytes());
	        fos.flush();
	        fos.close();
	    } catch (Exception e) {
	    	Log.e(LOG_TAG, "Error in writeFile." + e.getMessage());
	    }
	}
	
	public void writeFile(Activity act, String harvest, String info_readed){
		String fileName = getFileName(act);
	    String line = harvest + "\t" + info_readed + "</br>";
	    write(act, fileName, line);
		
	}
	
	public void sendFile(Activity act){

		try {
	    	String filePath = act.getApplication().getFilesDir().getAbsolutePath()
	                + File.separator;
	        String file = getFileName(act);
	        Log.i(LOG_TAG, "El archivo está en:" +  filePath + file);
	        File f = new File(filePath + file);
	        if (f.exists()) {
	        	Log.i(LOG_TAG, "El archivo existe!");
	        	FtpConnection conn = new FtpConnection();
	        	Log.i(LOG_TAG, "intenta enviar FTP");
	        	conn.sendFileForFTP(filePath, file);
	        	
	        	//get increment number
	        	int increment = Integer.valueOf( file.substring( file.length() - 5, file.length() - 4 ) );
	        	Log.i(LOG_TAG, "get increment number:" + increment);
	        	//Increment file's number
	        	file = file.substring( 0, file.length() - 5) + (increment + 1) ;
	        	Log.i(LOG_TAG, "Increment file's number:" + file);
	        	//Create a new file empty
	        	write(act, file, "");
	        	Log.i(LOG_TAG, "new file created");
	        }
	    } catch (Exception e) {
	    	Log.e(LOG_TAG, "Error in sendFile... " + e.getMessage());
	    }
		
	}
	
	
}
