package net.janebeta7.android;

import java.net.InetSocketAddress;

import com.relivethefuture.osc.data.OscMessage;
import com.relivethefuture.osc.transport.OscClient;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import android.os.Process;
import android.preference.PreferenceManager;

/***
 * This service runs in the background and sends a test OSC message 
 * to a specified address every 2 seconds.
 * 
 * Use it to test your OSC server app to make sure it can receive OSC messages
 * correctly. Do this by calling startService() (see OSCSampleServer for details)
 * either from your app (not recommended) or by starting the service via a separate app.
 * 
 * We will assume that the user will start their own OSC Client that sends messages,
 * so you only need to implement the OSC Server and keep listening until something comes to you!
 * This is merely to test your app's response to the OSC message.
 * 
 * @author odbol
 *
 */
public class OSCClientService extends IntentService {
	public OSCClientService() {
		super("OSCTesterClientServiceThread");
	}

	private String oscAddress = "192.168.0.192";
	private int oscPort = ColorOSC.DEFAULT_OSC_PORT;
	private String oscMsgPath = "/acolor";
	private OscClient sender;

	

	@Override
	public void onCreate() {
		super.onCreate();
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
		
		oscAddress = p.getString("pref_osc_addr", oscAddress);
	



		//start the osc client
		if (sender == null) {
			sender = new OscClient(true); 
			InetSocketAddress addr = new InetSocketAddress(oscAddress, oscPort);
			sender.connect(addr);
		}

		//add to foreground
		
	   /* Notification notification = new Notification(R.drawable.icon, getText(R.string.ticker_text),
	            System.currentTimeMillis());
	    Intent notificationIntent = new Intent(this, ColorOSC.class);
	    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
	    notification.setLatestEventInfo(this, getText(R.string.notification_title),
	            getText("notificacion"), pendingIntent);
	    startForeground(ONGOING_NOTIFICATION, notification);*/
		
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Toast.makeText(this, "OSCTester service starting", Toast.LENGTH_SHORT).show();
		Log.d("ColorOSC", "OSCTester service starting:");
		
		while (sender != null) {
			//send a test osc message
			
				OscMessage m = new OscMessage(oscMsgPath);
				m.addArgument(ColorOSC.getColor());
				try {
					sender.sendPacket(m);
				} catch (Exception e) {
					e.printStackTrace();
				}
			


			// For our sample, we just sleep for 2 seconds.
			long endTime = System.currentTimeMillis() +50;
			while (System.currentTimeMillis() < endTime) {
				synchronized (this) {
					try {
						wait(endTime - System.currentTimeMillis());
					} catch (Exception e) {
					}
				}
			}
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