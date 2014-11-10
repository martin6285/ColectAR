package com.colectar.scannerqrapp;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.colectar.SQLite.ScannerInfo;
import com.colectar.SQLite.ScannerInfoDataSource;
import com.colectar.ftp.FtpConnection;
import com.colectar.scannerqrapp.R;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.app.ListActivity;

//import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

public class DataActivity extends Activity implements OnClickListener{

	private TextView dataInfoTxt;
	private ScannerInfoDataSource datasource;
	private Context context;
	private static final String LOG_TAG = "QR_APP";
	public static String textContent;

	public static String getTextContent() {
		return textContent;
	}
	public static void setTextContent(String textContent) {
		DataActivity.textContent = textContent;
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        
        context = this;
        /*datasource = new ScannerInfoDataSource(this);
        datasource.open();

        List<ScannerInfo> values = datasource.getAllScannerInfo();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<ScannerInfo> adapter = new ArrayAdapter<ScannerInfo>(this,
            android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);*/
        dataInfoTxt = (TextView)findViewById(R.id.data_info);

        dataInfoTxt.setText(getTextContent());
        
        dataInfoTxt.setOnClickListener(this);
        
        Log.i(LOG_TAG, "Presenta DataActivity");
        
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


	@Override
	public void onClick(View v) {
		this.finish();
	}
	
}
