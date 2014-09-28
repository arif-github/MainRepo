package using.messnager;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

public class MessengerService extends Service {
	/** Command to the service to display a message */
	static final int MSG_SAY_HELLO = 1;
	static final int MSG_REPLAY_ME = 2;
	static final int MSG_REGISTER_CLIENT = 3;

	/**
	 * Command to the service to unregister a client, ot stop receiving
	 * callbacks from the service. The Message's replyTo field must be a
	 * Messenger of the client as previously given with MSG_REGISTER_CLIENT.
	 */
	static final int MSG_UNREGISTER_CLIENT = 4;

	/**
	 * Handler of incoming messages from clients.
	 */
	private int mVa = 0;

	class IncomingHandler extends Handler {
		;

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SAY_HELLO:
				Toast.makeText(getApplicationContext(), "hello!",
						Toast.LENGTH_SHORT).show();
				break;
			case MSG_REGISTER_CLIENT:
				mClinet = msg.replyTo;
				break;
			case MSG_UNREGISTER_CLIENT:
				mClinet = null;
				break;
			case MSG_REPLAY_ME:
				
				Toast.makeText(getApplicationContext(), "Fet Request from Clinet",
						Toast.LENGTH_SHORT).show();

				Message msg2 = Message.obtain(null,
						MessengerService.MSG_REPLAY_ME, mVa++, 0);

				try {
					mClinet.send(msg2);

				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	final Messenger mMessenger = new Messenger(new IncomingHandler());
	Messenger mClinet;// = new Messenger(new IncomingHandler());

	/**
	 * When binding to the service, we return an interface to our messenger for
	 * sending messages to the service.
	 */
	@Override
	public IBinder onBind(Intent intent) {
		Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT)
				.show();
		return mMessenger.getBinder();
	}
}
