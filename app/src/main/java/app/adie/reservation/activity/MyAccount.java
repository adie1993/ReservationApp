package app.adie.reservation.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;

import app.adie.reservation.R;
import app.adie.reservation.SessionManager;
import app.adie.reservation.entity.Jurusan;

public class MyAccount extends Activity {
	private static MyAccount mInstance;
	Button logout;
	Jurusan mJurusan;
	SessionManager session;
	TextView status;
	JSONArray contacts = null;
	String email, nama, id;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_konfirmasi);



	}

}
