package net.janebeta7.android;

import java.net.InetSocketAddress;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;


/*osclib*/



import com.relivethefuture.osc.data.OscMessage;
import com.relivethefuture.osc.transport.OscClient;

public class ColorOSC extends Activity {

	public static final int DEFAULT_OSC_PORT = 8000;
	protected static final int EDIT_PREFS = 1;
	private OscClient sender;

	private String oscAddress = "127.0.0.1";
	private int oscPort = DEFAULT_OSC_PORT;
	private String oscMsgPath = "/test/count";
	private int timeout = 100;
	private int curCount = 0;
	private boolean isServiceStarted = false;
	private	ImageAdapter imageAdapter;


	/** Called when the activity iiis first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/*
		 * This populates the default values from the preferences XML file. See
		 * {@link DefaultValues} for more details.
		 */
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		//reload prefs
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
		try {
			oscPort = Integer.parseInt(p.getString("pref_osc_port", String.valueOf(oscPort)));
		}
		catch (NumberFormatException e) {
			Toast.makeText(this, "Invalid port in preferences", Toast.LENGTH_LONG);
		}
		try {
			timeout = Integer.parseInt(p.getString("pref_timeout", String.valueOf(timeout)));
		}
		catch (NumberFormatException e) {
			Toast.makeText(this, "Invalid timeout in preferences", Toast.LENGTH_LONG);
		}
		oscAddress = p.getString("pref_osc_addr", oscAddress);
		//oscMsgPath  = p.getString("pref_osc_msg", oscMsgPath);



		//start the osc client
		if (sender == null) {
			sender = new OscClient(true); 
			InetSocketAddress addr = new InetSocketAddress(oscAddress, oscPort);
			sender.connect(addr);
		}

		GridView gridview = (GridView) findViewById(R.id.GridViewPalettes);
		imageAdapter = new ImageAdapter(this);
		gridview.setAdapter(imageAdapter);

	}
	public void sendData(){
		Log.d("ColorOSC", "sendData:" );
	}
	protected void onHandleIntent(Intent intent) {
		Toast.makeText(this, "OSCTester service starting", Toast.LENGTH_SHORT).show();

		curCount = 0;
		while (curCount++ < timeout) {
			//send a test osc message
			if (sender != null) {
				OscMessage m = new OscMessage(oscMsgPath);
				m.addArgument(curCount);
				try {
					sender.sendPacket(m);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}


			// For our sample, we just sleep for 2 seconds.
			/*long endTime = System.currentTimeMillis() + 2*1000;
			while (System.currentTimeMillis() < endTime) {
				synchronized (this) {
					try {
						wait(endTime - System.currentTimeMillis());
					} catch (Exception e) {
					}
				}
			}*/
		}
	}
	// send an osc message
	public void send(String msg) {
		// @+id/GridViewPalettes
		// Call getDrawable to get the image
		// button.setBackgroundResource(R.drawable.connect_off);
		// button.setBackgroundResource(R.drawable.icon);
		OscMessage msg_ = new OscMessage(msg);
		if (msg_ != null) {
			try {
				sender.sendPacket(msg_);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.app_menu, menu);
		return true;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		// See which child activity is calling us back.
		switch (requestCode) {
		case EDIT_PREFS:
			//reload prefs
			SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
			try {
				oscPort = Integer.parseInt(p.getString("pref_osc_port", String.valueOf(DEFAULT_OSC_PORT)));
			}
			catch (NumberFormatException e) {
				Toast.makeText(this, "Invalid port in preferences", Toast.LENGTH_LONG);
			}


			break;
		} 
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.settings:
			System.out.println("settings");
			Intent intent = new Intent(this, OSCTesterClientPreferences.class);
			startActivityForResult(intent, EDIT_PREFS);
			return true;
		case R.id.disconnect:
			Toast.makeText(this, "disconnect", Toast.LENGTH_SHORT).show(); 
			return true;
		case R.id.connect:
			Toast.makeText(this, "connect on:"+ oscPort +"to"+oscAddress, Toast.LENGTH_SHORT).show(); 
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	@Override
	public void onDestroy() {
		if (sender != null) {
			sender.disconnect();
			sender = null;
		}
		Toast.makeText(this, "OSCTester service done", Toast.LENGTH_SHORT).show(); 
		super.onDestroy();
	}

}
