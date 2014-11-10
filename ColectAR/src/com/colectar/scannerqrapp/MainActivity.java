package com.colectar.scannerqrapp;

import com.colectar.scannerqrapp.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

//import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

public class MainActivity extends Activity implements OnClickListener{

	private Button scanBtn, sendBtn;
	private TextView tittleTxt;
	private Context context;
	private static final String LOG_TAG = "QR_APP";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        context = this;
        scanBtn = (Button)findViewById(R.id.scan_main_button);
        sendBtn = (Button)findViewById(R.id.send_main_button);
        tittleTxt = (TextView)findViewById(R.id.menu_tittle);
        
        scanBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, (android.view.Menu) menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



	public void readInternalStorageOption() {
	    try {
	        Intent intent = new Intent();
	        intent.setAction(Intent.ACTION_VIEW);
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

	        FileManager fm = new FileManager();
	        fm.sendFile(this);
	        
	        /*String filePath = getApplication().getFilesDir().getAbsolutePath()
	                + File.separator;
	        String file = getFileName();
	        Log.i(LOG_TAG, "El archivo está en:" +  filePath + file);
	        File f = new File(filePath + file);
	        if (f.exists()) {
	        	Log.i(LOG_TAG, "El archivo existe!");
	        	FtpConnection conn = new FtpConnection();
	        	Log.i(LOG_TAG, "intenta enviar FTP");
	        	conn.sendFileForFTP(filePath, file);
	        	Uri internal = Uri.fromFile(f);
	            intent.setDataAndType(internal, "text");
	            startActivity(intent);
	        }*/
	        
	    } catch (Exception ex) {
	    	Log.e(LOG_TAG, "Error in readFile! " + ex.getMessage());
	    }
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.scan_main_button){

			Intent i = new Intent(context, ScanActivity.class );
	        startActivity(i);
		 }
		if(v.getId()==R.id.send_main_button){

			//Intent i = new Intent(context, DataActivity.class );
	        //startActivity(i);
	        readInternalStorageOption();
		 }
		
	}
}
