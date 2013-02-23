package it.gmariotti.android.examples.dashclock.extensions.wht;

import java.util.ArrayList;

import android.util.Log;

public class MessageManager {

	private final static String TAG="MessageManager";
	
	private WhatsappExtension mWhtExtension;
	private static MessageManager sInstance;
	private SomethingOnScreenReceiver mReceiver;

	public static MessageManager getInstance(WhatsappExtension context) {
		if (sInstance == null) {
			sInstance = new MessageManager(context);
		}
		return sInstance;
	}

	public static MessageManager getInstance() {
		return sInstance;
	}

	private MessageManager(WhatsappExtension context) {
		mWhtExtension = context;
		mCount = 0;
		mMsgs = new ArrayList<MessageWht>();
	}

	
	private int mCount;
	private ArrayList<MessageWht> mMsgs;

	/**
	 * Notify for new Message
	 * @param msg
	 */
	public void notifyListener(MessageWht msg) {
		Log.d(TAG,"new Message");
		if (mWhtExtension != null){
			mCount++;
			mMsgs.add(msg);
			mWhtExtension.changeMessage();
		}
	}
	
	/**
	 * Reset counter and clear old messages
	 */
	public void clearMessages(){
		Log.d(TAG,"Clear Messages");
		mCount=0;
		mMsgs=new ArrayList<MessageWht>();
		if (mWhtExtension!=null)
			mWhtExtension.changeMessage();
	}

	public int getmCount() {
		return mCount;
	}

	public ArrayList<MessageWht> getmMsgs() {
		return mMsgs;
	}

	public SomethingOnScreenReceiver getmReceiver() {
		return mReceiver;
	}

	public void setmReceiver(SomethingOnScreenReceiver mReceiver) {
		this.mReceiver = mReceiver;
	}
	
	
}
