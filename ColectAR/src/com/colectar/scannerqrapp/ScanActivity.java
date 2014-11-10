package com.colectar.scannerqrapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.colectar.SQLite.ScannerInfoDataSource;
import com.colectar.scannerqrapp.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ScanActivity extends Activity implements OnClickListener{

	private Button scanBtn;
	private TextView harvestDescTxt, harvestTxt, infoQRDescTxt, infoQRTxt;
	private int lecturas = 0;
	private Context context;
	private static final String LOG_TAG = "QR_APP";
	private ScannerInfoDataSource datasource;
	private final static int LECTURA1 = 1;
	private final static int LECTURA2 = 2;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        
        context = this;
        scanBtn = (Button)findViewById(R.id.scan_ok_button);
        harvestDescTxt = (TextView)findViewById(R.id.scan_harvestDesc);
        harvestTxt = (TextView)findViewById(R.id.scan_harvest);
        infoQRDescTxt = (TextView)findViewById(R.id.scan_infoQRDesc);
        infoQRTxt = (TextView)findViewById(R.id.scan_infoQR);

        datasource = new ScannerInfoDataSource(this);
        //datasource.open();
        
        //Toast.makeText(context, "Lea el código del cosechador", Toast.LENGTH_LONG).show();
		
		
		lecturas = 0;
		DataActivity.setTextContent("Lea la Etiqueta del Cosechador");
		//Intent i = new Intent(context, DataActivity.class );
        //startActivity(i);
        Intent i = new Intent(context, DataActivity.class);
        //i.putExtra(...);    //if you need to pass parameters
        startActivityForResult(i, LECTURA1);
		
       
        
        scanBtn.setOnClickListener(this);
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
		Log.i(LOG_TAG, "onClick - v.getId():[" + v.getId() + "]  - R.id.scan_ok_button:[" + R.id.scan_ok_button + "]");
		if(v.getId()==R.id.scan_ok_button){
			Log.i(LOG_TAG, "Entra al evento");
			sendInformation();
			this.finish();
		}
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//retrieve scan result
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanningResult != null) {
			//we have a result
			String scanContent = scanningResult.getContents();
			String scanFormat = scanningResult.getFormatName();
			String msg = "";
			int lectura = 0;

			if (lecturas < 2) {
				if (lecturas == 0) {
					//harvestDescTxt.setText("FORMAT 1ra Lectura: " + scanFormat);
					//verifico que sea un EAN13
					Log.i(LOG_TAG, "1ra Leida FORMATO:[" + scanFormat + "]");
					if (scanFormat!= null){
						if (scanFormat.equals("EAN13") || scanFormat.equals("UPC_A") ){
							harvestTxt.setText(scanContent);
							Log.i(LOG_TAG, "1ra Leida:[" + scanContent + "]");
							msg = "Lea el c\u00F3digo QR de la variedad.";
							lectura = LECTURA2;
							lecturas = 1;
						} else {
							msg = "Debe leer un c\u00F3digo de etiqueta con el formato EAN13.\nLea la Etiqueta del Cosechador";
							lectura = LECTURA1;
							lecturas = 0;
						}
					} else {
						msg = "Hubo un error en la lectura.\nLea nuevamente la Etiqueta del Cosechador";
						lectura = LECTURA1;
						lecturas = 0;
					}

					DataActivity.setTextContent(msg);
					Intent i = new Intent(context, DataActivity.class);
			        startActivityForResult(i, lectura);

					
				} else {
					//infoQRDescTxt.setText("FORMAT 2da Lectura: " + scanFormat);
					Log.i(LOG_TAG, "2da Leida FORMATO:[" + scanFormat + "]");
					if (scanFormat!= null){
						if (scanFormat.equals("QR_CODE")){
							infoQRTxt.setText(scanContent);
							Log.i(LOG_TAG, "2da Leida:[" + scanContent + "]");
							lecturas = 2;
						} else {
							msg = "Debe leer un c\u00F3digo QR.\nLea el c\u00F3digo QR de la variedad.";
							lectura = LECTURA2;
							lecturas = 1;
							DataActivity.setTextContent(msg);
							Intent i = new Intent(context, DataActivity.class);
					        startActivityForResult(i, lectura);
						}
					} else {
						msg = "Hubo un error en la lectura.\nLea nuevamente el c\u00F3digo QR de la variedad.";
						lectura = LECTURA2;
						lecturas = 1;
						DataActivity.setTextContent(msg);
						Intent i = new Intent(context, DataActivity.class);
				        startActivityForResult(i, lectura);
					}

				}
			}
			
		} else {
		    Log.i(LOG_TAG, "No se encontraron datos");
		    IntentIntegrator scanIntegrator = null;
		    switch(requestCode) {
	        case LECTURA1:
	            //you just got back from activity B - deal with resultCode
	            //use data.getExtra(...) to retrieve the returned data
	        	 //scan
	          	scanIntegrator = new IntentIntegrator(this);
	            scanIntegrator.initiateScan();
	            break;
	        case LECTURA2:
	            //you just got back from activity C - deal with resultCode
	        	scanIntegrator = new IntentIntegrator(this);
	            scanIntegrator.initiateScan();
	            break;
			}
		}
	}

	private void sendInformation() {
		if (harvestTxt.getText().length() > 0 &&
			infoQRTxt.getText().length() > 0){
			/*datasource.open();
			datasource.createScannerInfo(harvestTxt.getText().toString(), infoQRTxt.getText().toString());
			datasource.close();*/
			FileManager fm = new FileManager();

			Toast.makeText(getApplicationContext(),
		            "Llama a writeFile!", Toast.LENGTH_SHORT).show();

			//String fileName = getFileName();
			fm.writeFile(this, harvestTxt.getText().toString(), infoQRTxt.getText().toString());
			this.finish();
			
		} else {
			Toast.makeText(context, "No se tiene la informaci\u00F3n necesaria", Toast.LENGTH_LONG).show();
		}
	
	}
	
	public boolean isSdReadable() {

	    boolean mExternalStorageAvailable = false;
	    try {
	        String state = Environment.getExternalStorageState();

	        if (Environment.MEDIA_MOUNTED.equals(state)) {
	            // We can read and write the media
	            mExternalStorageAvailable = true;
	            Log.i("isSdReadable", "External storage card is readable.");
	        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	            // We can only read the media
	            Log.i("isSdReadable", "External storage card is readable.");
	            mExternalStorageAvailable = true;
	        } else {
	            // Something else is wrong. It may be one of many other
	            // states, but all we need to know is we can neither read nor
	            // write
	            mExternalStorageAvailable = false;
	        }
	    } catch (Exception ex) {

	    }
	    return mExternalStorageAvailable;
	}
	
}
