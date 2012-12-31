package net.janebeta7.android;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.openintents.intents.FileManagerIntents;



import net.janebeta7.android.R;

import com.relivethefuture.osc.data.BasicOscListener;
import com.relivethefuture.osc.data.OscMessage;
import com.relivethefuture.osc.transport.OscServer;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;



public class ColorOSC extends Activity {

	/*-------------------------------------------------------- */
	public static final int DEFAULT_OSC_PORT = 55555;
	protected static final int EDIT_PREFS = 1;
	private static final int REQUEST_LOAD = 0;
	private static final String SelectionMode = null;
	private static final int PICK_FOLDER_RESULT_CODE = 0;
	private static final int PICK_FILE_RESULT_CODE = 0;
	private static final int REQUEST_CODE_PICK_FILE_OR_DIRECTORY = 0;
	private OscServer server;
	private int oscPort = DEFAULT_OSC_PORT;
	private static final String filePath ="";

	private static ImageAdapter imageAdapter;
	private boolean isServiceStarted = false;
	protected EditText mEditText;
	private String folderPath = "sdcard/aColorOSC"; //si no lo encuentra buscar otro por defecto
	private int contFiles;

	/*-------------------------------------------------------- */

	@Override
	public void onDestroy() {
		stopTestClient();

		super.onDestroy();
	}

	/***
	 * This just starts the OSCTesterClient service.
	 * 
	 * In a real application you don't need this because presumably the user has
	 * already started their own OSC client either on the phone or on a separate
	 * device (e.g. a laptop connected via WiFi).
	 */
	private void startTestClient() {
		Log.d("ColorOSC", "startTestClient:");
		Intent intent = new Intent(this, OSCClientService.class);
		startService(intent);
	}

	private void stopTestClient() {
		Log.d("ColorOSC", "stopTestClient:");
		Intent intent = new Intent(this, OSCClientService.class);
		stopService(intent);
	}

	public static int getColor() {
		int numColors = imageAdapter.getColor();
		return numColors;
	}

	/**
	 * Called when the activity is first created. The rest of this code is just
	 * for demonstration.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/* desbloqueamos la pantalla en el emulador o devide de block */
		KeyguardManager mKeyGuardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		mKeyGuardManager.newKeyguardLock("ColorOSC").disableKeyguard();
		/*if (isMediaMounted()) initImages();
		else pickDirectory();*/
		initImages();
		

	}
	private boolean isMediaMounted(){
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;

		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		} else {
			return false;
		}
		
	}
	private void initImages(){
		/* visualizamos la grilla de colores desde el folder especificado*/
		GridView gridview = (GridView) findViewById(R.id.GridViewPalettes);
		final List<String> SD = ReadSDCard();
		imageAdapter = new ImageAdapter(this,SD);
		gridview.setAdapter(imageAdapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent,
	          View v, int position, long id) {
	         String imageInSD = SD.get(position);
	         Log.d("onClick", "imageInSD [" + imageInSD + "]");
	         imageAdapter.setImageGrande(imageInSD);
	         imageAdapter.processColors(imageInSD);
	         
	        }
	    });
		
		
		/*Log.d("onClick", "position [" + position + "]");
		imgView.setImageResource(mThumbIds[position]); //display image screen
	
		final AssetManager mgr = mContext.getAssets();
		displayFiles(mgr,"set");
		
		BitmapFactory.Options bfoOptions = new BitmapFactory.Options(); 
		bfoOptions.inScaled = false; 
		
		processColors(position);*/
		//Log.d("ImageAdapter", "numColors:" + pixColors.size());
		
	}
	private List<String> ReadSDCard()
	{
		contFiles =0;
	 List<String> tFileList = new ArrayList<String>();

	 //It have to be matched with the directory in SDCard
	 File f = new File(folderPath);

	 File[] files=f.listFiles();
	 Log.d("janebeta7", "ReadSDCard >" + files.length + "");
	 for(int i=0; i<files.length; i++)
	 {
	  File file = files[i];
	  String curFile=file.getPath();
	  String curName=file.getName();
	  String ext=curFile.substring(curFile.lastIndexOf(".")+1, 
	    curFile.length()).toLowerCase();
	  int namePoint=curName.lastIndexOf("_"); //prevenir que lea archivos que empiecen por . (ocultos)
	  if((ext.equals("jpg")||ext.equals("gif")||ext.equals("png")) && (namePoint != 1) && (file.canRead()))
	  {
		  tFileList.add(file.getPath());
		  Log.d("janebeta7", "ReadSDCard >curFile" + curFile + "");
		  contFiles ++;
	  }
	 }
	 Log.d("janebeta7", "ReadSDCard >contFiles" + contFiles + "");
	 return tFileList;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// See which child activity is calling us back.
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("janebeta7", "resultCode [" + requestCode + "]");
		switch (requestCode) {
		case EDIT_PREFS:
			// reload prefs
			SharedPreferences p = PreferenceManager
					.getDefaultSharedPreferences(this);
			try {
				oscPort = Integer.parseInt(p.getString("pref_osc_port",
						String.valueOf(DEFAULT_OSC_PORT)));
			} catch (NumberFormatException e) {
				Toast.makeText(this, "Invalid port in preferences",
						Toast.LENGTH_LONG);
			}

			break;
		case REQUEST_CODE_PICK_FILE_OR_DIRECTORY:
			if (resultCode == RESULT_OK && data != null) {
				Toast.makeText(this, "data"+data,
						Toast.LENGTH_LONG);
				// obtain the folderPath
				Uri fileUri = data.getData();
				if (fileUri != null) {
					 folderPath = fileUri.getPath();
					if (folderPath != null) {
						//mEditText.setText(filePath);
						Log.d("janebeta7", "folderPath [" + folderPath + "]");
						initImages();

					}
					else
					{
						Log.d("janebeta7", "NO FOLDER PATH FOUND- CHOOSE ANOTHER FOLDER");
					}
				}
			}
			break;
        }
	}

	/*------------------------ CREAMOS ACCIONES DE BOTON MENU-----------------------------------*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.app_menu, menu);
		return true;
	}
	 /**
     * Opens the file manager to pick a directory.
     */
    private void pickDirectory() {
		
		
		// Note the different intent: PICK_DIRECTORY
		Intent intent = new Intent(FileManagerIntents.ACTION_PICK_DIRECTORY);
		
		// Construct URI from file name.
		File file = new File(folderPath);
		intent.setData(Uri.fromFile(file));
		
		// Set fancy title and button (optional)
		intent.putExtra(FileManagerIntents.EXTRA_TITLE, "select a folder");
		intent.putExtra(FileManagerIntents.EXTRA_BUTTON_TEXT, "select");
		
		try {
	
			startActivityForResult(intent, REQUEST_CODE_PICK_FILE_OR_DIRECTORY);
		} catch (ActivityNotFoundException e) {
			// No compatible file manager was found.
			Toast.makeText(this,"No file manager installed", 
					Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.settings:
			System.out.println("settings");
			Intent intent2 = new Intent(this, aColorPreferences.class);
			startActivityForResult(intent2, EDIT_PREFS);
			return true;
		case R.id.info:
			Toast.makeText(this, "info", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.load:

			pickDirectory();
			
			return true;
		case R.id.connect:

			if (!isServiceStarted) {
				startTestClient();
				((MenuItem) item).setTitle("disconnect");
				Toast.makeText(this, "connect", Toast.LENGTH_SHORT).show();
			} else {
				stopTestClient();
				((MenuItem) item).setTitle("connect");
				Toast.makeText(this, "disconnect", Toast.LENGTH_SHORT).show();
			}
			isServiceStarted = !isServiceStarted;
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
