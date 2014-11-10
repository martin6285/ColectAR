package com.colectar.ftp;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import android.os.AsyncTask;
import android.util.Log;

public class FtpConnection {

	public static final String FTP_HOST = "ftp.studybuenosaires.com.ar";//"190.228.29.71:21";
	public static final String FTP_USER = "colectar.studybuenosaires.com.ar";
	public static final String FTP_PASS = "ColectAR123";
	private static final String LOG_TAG = "QR_APP";
	
	public void sendFileForFTP(final String path, final String fileName){


		new Thread(new Runnable(){
		    @Override
		    public void run() {
		        try {
		        	FTPClient con = null;

		            try
		            {
		                con = new FTPClient();
		                Log.i(LOG_TAG, "Antes de conectar");
		                con.connect(FTP_HOST);

		                Log.i(LOG_TAG, "Conectó");
		                if (con.login(FTP_USER, FTP_PASS))
		                {
		                	Log.i(LOG_TAG, "Login correcto");
		                	con.enterLocalPassiveMode(); // important!
		                    con.setFileType(FTP.BINARY_FILE_TYPE);
		                    String data = path + fileName; //"/sdcard/vivekm4a.m4a";

		                    FileInputStream in = new FileInputStream(new File(data));
		                    boolean result = con.storeFile("/" + fileName, in); //"/vivekm4a.m4a", in);
		                    in.close();
		                    if (result) Log.i(LOG_TAG, "upload result");
		                    con.logout();
		                    con.disconnect();
		                }
		            }
		            catch (Exception e)
		            {
		                e.printStackTrace();
		                Log.e(LOG_TAG, "Error FTPConnection. " + e.getMessage());
		            }

		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		    }
		}).start();
		
		

    }
}
