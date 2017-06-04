package app.adie.reservation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.HashMap;

import app.adie.reservation.activity.Home;
import app.adie.reservation.entity.User;

@SuppressLint("CommitPrefEdits")
public class SessionManager {
	SharedPreferences pref;
	SharedPreferences.Editor editor;
	Context _context;
	int PRIVATE_MODE = 0;
	private static final String PREF_NAME = "Sesi";
	private static final String IS_LOGIN = "Is Logged In";
	public static final String KEY_NAME = "nama";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_ID = "id";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_ALAM = "alamat";
	private static final String KEY_NOTIFICATIONS = "notifications";
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	public void storeUser(User user) {
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_ID, user.getId());
		editor.putString(KEY_NAME, user.getName());
		editor.putString(KEY_EMAIL, user.getEmail());
		editor.putString(KEY_PHONE, user.getPhone());
		editor.putString(KEY_ALAM, user.getAlamat());
		editor.commit();

		Log.e("asda", "User is stored in shared preferences. "+user.getId() +", " + user.getName() + ", " + user.getEmail());
	}
	public void createSession(String nama){
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_NAME, nama);
		editor.commit();
	}

	public void checkLogin(){
		if(!this.isLoggedIn()){
			Intent i = new Intent(_context, Home.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
		}
		
	}

	public User getUser() {
		if (pref.getString(KEY_ID, null) != null) {
			String id, name, email,phone,alamat;
			int type;
			id = pref.getString(KEY_ID, null);
			name = pref.getString(KEY_NAME, null);
			email = pref.getString(KEY_EMAIL, null);
			phone = pref.getString(KEY_PHONE,null);
			alamat = pref.getString(KEY_ALAM,null);
			User user = new User(id, name, email,phone,alamat);
			return user;
		}
		return null;
	}

	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		user.put(KEY_ID, pref.getString(KEY_ID, null));
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));
		return user;
	}

	public void logoutUser(){
		editor.clear();
		editor.commit();
		
		Intent i = new Intent(_context, Home.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(i);
	}
	public void addNotification(String notification) {

		// get old notifications
		String oldNotifications = getNotifications();

		if (oldNotifications != null) {
			oldNotifications += "|" + notification;
		} else {
			oldNotifications = notification;
		}

		editor.putString(KEY_NOTIFICATIONS, oldNotifications);
		editor.commit();
	}

	public String getNotifications() {
		return pref.getString(KEY_NOTIFICATIONS, null);
	}
	
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);
	}
}
