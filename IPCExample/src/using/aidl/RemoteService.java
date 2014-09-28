package using.aidl;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

public class RemoteService extends Service {
	private static final String TAG = "RemoteService";
	/**
	 * This is a list of callbacks that have been registered with the service.
	 * Note that this is package scoped (instead of private) so that it can be
	 * accessed more efficiently from inner classes.
	 */
	final RemoteCallbackList<IRemoteServiceCallback> mCallbacks = new RemoteCallbackList<IRemoteServiceCallback>();
	private int mValue=0;

	@Override
	public void onCreate() {
		

		// Display a notification about us starting.
		//showNotification();

		// While this service is running, it will continually increment a
		// number. Send the first message that is used to perform the
		// increment.
		mHandler.sendEmptyMessage(REPORT_MSG);
	}

	@Override
	public void onDestroy() {
		// Cancel the persistent notification.
	

		// Tell the user we stopped.
		Toast.makeText(this, "C",
				Toast.LENGTH_SHORT).show();

		// Unregister all callbacks.
		mCallbacks.kill();

		// Remove the next pending message to increment the counter, stopping
		// the increment loop.
		mHandler.removeMessages(REPORT_MSG);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// Select the interface to return. If your service only implements
		// a single interface, you can just return it here without checking
		// the Intent.
		
		
		Log.i(TAG, IRemoteService.class.getName());
		
		
		if (IRemoteService.class.getName().equals(intent.getAction())) {
			return mBinder;
		}
		if (ISecondary.class.getName().equals(intent.getAction())) {
			return mSecondaryBinder;
		}
		return null;
	}

	/**
	 * The IRemoteInterface is defined through IDL
	 */
	private final IRemoteService.Stub mBinder = new IRemoteService.Stub() {
		public void registerCallback(IRemoteServiceCallback cb) {
			if (cb != null)
				mCallbacks.register(cb);
		}

		public void unregisterCallback(IRemoteServiceCallback cb) {
			if (cb != null)
				mCallbacks.unregister(cb);
		}
	};

	/**
	 * A secondary interface to the service.
	 */
	private final ISecondary.Stub mSecondaryBinder = new ISecondary.Stub() {
		public int getPid() {
			return Process.myPid();
		}

		public void basicTypes(int anInt, long aLong, boolean aBoolean,
				float aFloat, double aDouble, String aString) {
		}
	};

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		Toast.makeText(this, "Task removed: " + rootIntent, Toast.LENGTH_LONG)
				.show();
	}

	private static final int REPORT_MSG = 1;

	/**
	 * Our Handler used to execute operations on the main thread. This is used
	 * to schedule increments of our value.
	 */
	private final Handler mHandler = new Handler() {
		

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			// It is time to bump the value!
			case REPORT_MSG: {
				// Up it goes.
				int value = ++mValue;

				// Broadcast to all clients the new value.
				final int N = mCallbacks.beginBroadcast();
				for (int i = 0; i < N; i++) {
					try {
						mCallbacks.getBroadcastItem(i).valueChanged(value);
					} catch (RemoteException e) {
						// The RemoteCallbackList will take care of removing
						// the dead object for us.
					}
				}
				mCallbacks.finishBroadcast();

				// Repeat every 1 second.
				sendMessageDelayed(obtainMessage(REPORT_MSG), 1 * 1000);
			}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};
}

