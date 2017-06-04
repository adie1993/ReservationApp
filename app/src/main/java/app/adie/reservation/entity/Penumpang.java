package app.adie.reservation.entity;

import android.content.Context;

import java.io.Serializable;

public class Penumpang implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static Context mContext = null;
	private static Penumpang mInstance = null;
	private String name;
	public int id;
	private SqliteHelper sqlitehelper;
	private String emailId;

	private boolean isSelected;

	public Penumpang() {

	}

	public Penumpang(Context context) {
		mContext = context;
	}

	public static synchronized Penumpang getInstance() {
		Penumpang penumpang;
		synchronized (Penumpang.class) {
			if (mInstance == null) {
				mInstance = new Penumpang();
			}
			penumpang = mInstance;
		}
		return penumpang;
	}


	public Penumpang(int id,String name, boolean isSelected) {
		this.id = id;
		this.name = name;
		this.emailId = emailId;
		this.isSelected = isSelected;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public boolean isSelected() {
		return isSelected;
	}



	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public int getId() {
		return id;
	}



}
