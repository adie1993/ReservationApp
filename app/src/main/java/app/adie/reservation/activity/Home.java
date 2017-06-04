package app.adie.reservation.activity;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import app.adie.reservation.JSONParser;
import app.adie.reservation.R;
import app.adie.reservation.SessionManager;

public class Home extends BaseActivity  {

	Button daftar, login;
	Intent a;
	private int statusBarColor;
	private int NavigationBarColor;
	String url, success,plus;
	JSONParser jParser = new JSONParser();
 	SessionManager session;

	private int backButtonCount;
	ProgressDialog pDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		if (Build.VERSION.SDK_INT >= 21) {
			this.statusBarColor = getWindow().getStatusBarColor();
			getWindow().setStatusBarColor(getResources().getColor(R.color.main));
		}
		if (Build.VERSION.SDK_INT >= 21) {
			this.statusBarColor = getWindow().getNavigationBarColor();
			getWindow().setNavigationBarColor(getResources().getColor(R.color.main));
		}
		pDialog = new ProgressDialog(Home.this);
		session = new SessionManager(getApplicationContext());
		Toast.makeText(getApplicationContext(),
				"Selamat Datang di Krakaline Shuttle App" , Toast.LENGTH_SHORT)
				.show();

		daftar = (Button) findViewById(R.id.btn_register);
		login = (Button) findViewById(R.id.btn_login);





		daftar.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				Intent daftar = new Intent(Home.this, Register.class);
				startActivity(daftar);

			}
		});
		login.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				Intent login = new Intent(Home.this, SignIn.class);
				startActivity(login);

			}
		});


}












	@Override
	public void onBackPressed() {
		if(backButtonCount >= 1)
		{
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			//finish();
		}
		else
		{
			Toast.makeText(this, "Yekan tombol back sekali lagi untuk keluar dari aplikasi.", Toast.LENGTH_SHORT).show();
			backButtonCount++;
		}
	}
}
