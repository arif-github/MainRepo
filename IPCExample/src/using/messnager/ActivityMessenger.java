package using.messnager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ipcexample.R;

public class ActivityMessenger extends Activity {
	/** Messenger for communicating with the service. */
	Messenger mService = null;

	/** Flag indicating whether we have called bind on the service. */
	boolean mBound;
	final Messenger mMessenger = new Messenger(new IncomingHandler());
	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case MessengerService.MSG_REPLAY_ME:
				Toast.makeText(getApplicationContext(),
						"Get Replay From Service:" + msg.arg1,
						Toast.LENGTH_SHORT).show();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	/**
	 * Class for interacting with the main interface of the service.
	 */
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			// This is called when the connection with the service has been
			// established, giving us the object we can use to
			// interact with the service. We are communicating with the
			// service using a Messenger, so here we get a client-side
			// representation of that from the raw IBinder object.
			mService = new Messenger(service);
			
			Message msg = Message
					.obtain(null, MessengerService.MSG_REGISTER_CLIENT);
			msg.replyTo=mMessenger;
			try {
				mService.send(msg);
				// mService.

			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			mBound = true;
		}

		public void onServiceDisconnected(ComponentName className) {
			// This is called when the connection with the service has been
			// unexpectedly disconnected -- that is, its process crashed.
			 try {
                 Message msg = Message.obtain(null,
                         MessengerService.MSG_UNREGISTER_CLIENT);
                 msg.replyTo = mMessenger;
                 mService.send(msg);
             } catch (RemoteException e) {
                 // There is nothing special we need to do if the service
                 // has crashed.
             }

			mService = null;
			mBound = false;
		}
	};

	public void sayHello(View v) {
		if (!mBound)
			return;
		// Create and send a message to the service, using a supported 'what'
		// value
		Message msg = Message
				.obtain(null, MessengerService.MSG_REPLAY_ME);
		try {
			mService.send(msg);

			// mService.

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_binding);

		TextView button;
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sayHello(v);
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
		// Bind to the service
		bindService(new Intent(this, MessengerService.class), mConnection,
				Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		// Unbind from the service
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
	}
}
