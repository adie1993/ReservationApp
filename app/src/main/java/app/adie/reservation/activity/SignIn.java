package app.adie.reservation.activity;



import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.adie.reservation.JSONParser;
import app.adie.reservation.R;
import app.adie.reservation.SessionManager;
import app.adie.reservation.app.MyApplication;
import app.adie.reservation.entity.User;
import app.adie.reservation.utils.ConnectionDetector;

public class SignIn extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
	private static final String TAG_SUCCESS = "success";
	static GoogleApiClient mGoogleApiClient;
	private static final int RC_SIGN_IN = 9001;
	private EditText inputuname, inputpassword;
	private TextInputLayout inputLayoutuname, inputLayoutPassword;
	Button login, loginplus;
	Intent a;
	private int statusBarColor;
	private int NavigationBarColor;
	String url, success,plus;
	JSONParser jParser = new JSONParser();
 	SessionManager session;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	TextView lupa;
	ProgressDialog pDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);
		if (Build.VERSION.SDK_INT >= 21) {
			this.statusBarColor = getWindow().getStatusBarColor();
			getWindow().setStatusBarColor(getResources().getColor(R.color.main));
		}
		cd = new ConnectionDetector(getApplicationContext());
		pDialog = new ProgressDialog(SignIn.this);
		session = new SessionManager(getApplicationContext());

		loginplus = (Button) findViewById(R.id.btn_loginplus);

		login = (Button) findViewById(R.id.btn_login);
		inputLayoutuname = (TextInputLayout) findViewById(R.id.input_layout_uname);
		inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
		inputuname = (EditText) findViewById(R.id.input_uname);
		inputpassword = (EditText) findViewById(R.id.input_password);
		inputuname.addTextChangedListener(new MyTextWatcher(inputuname));
		inputpassword.addTextChangedListener(new MyTextWatcher(inputpassword));
		pDialog.dismiss();

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this, this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();
		lupa=(TextView) findViewById(R.id.lupa);
		loginplus.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				if (isInternetPresent) {
					pDialog = new ProgressDialog(SignIn.this);
					pDialog.setMessage("Signing In");
					pDialog.setIndeterminate(false);
					pDialog.setCancelable(false);
					pDialog.show();
					signIn();
				} else {
					// Internet connection is not present
					// Ask user to connect to Internet
					showSnackbar(login, (int) R.string.message_no_network_connection, true);
				}

			}
		});
		isInternetPresent = cd.isConnectingToInternet();

		lupa.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				Intent lupa = new Intent(SignIn.this, LupaPass.class);
				startActivity(lupa);

			}
		});
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				url = "http://10.0.2.2/kraka/api/login.php?" + "username="
						+ inputuname.getText().toString() + "&password="
						+ inputpassword.getText().toString();
				if (!validateUName()) {
					return;
				}

				if (!validatePassword()) {
					return;
				}
				if (inputuname.getText().toString().trim().length() > 0
						&& inputpassword.getText().toString().trim().length() > 0)
				{
					if (isInternetPresent) {
						new Masuk().execute();
					} else {
						// Internet connection is not present
						// Ask user to connect to Internet
						showSnackbar(login, (int) R.string.message_no_network_connection, true);
					}

				}

			}
		});
	}


	private void signIn() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}
	public static void signOut() {
		Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
				new ResultCallback<Status>() {
					@Override
					public void onResult(Status status) {

					}
				});
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		pDialog.dismiss();

		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			handleSignInResult(result);
		}
	}
	private void handleSignInResult(GoogleSignInResult result) {
		if (result.isSuccess()) {
			GoogleSignInAccount acct = result.getSignInAccount();
			String emaiil = acct.getEmail();
			String namaa = acct.getDisplayName();
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
				plus = "http://10.0.2.2/kraka/api/loginGplus.php?" + "email="+ emaiil;

			try {
					int successs;

					JSONObject jsonn = jParser.getJSONFromUrl(plus);
					successs = jsonn.getInt(TAG_SUCCESS);
					Log.e("error", "nilai sukses=" + successs);

					if (successs==1) {

						JSONArray hasil = jsonn.getJSONArray("loginp");

						for (int i = 0; i < hasil.length(); i++) {

							JSONObject c = hasil.getJSONObject(i);

							String id = c.getString("id").trim();
							String nama = c.getString("nama").trim();
							String email = c.getString("email").trim();
							String phone = c.getString("phone").trim();
							String alamat = c.getString("alamat").trim();
							User user = new User(c.getString("id"),
									c.getString("nama"),
									c.getString("email"),
									c.getString("phone"),
									c.getString("alamat"));
							session.storeUser(user);

							Log.e("ok", " ambil data");
						}
						signOut();
						a = new Intent(SignIn.this, LoadingLanding.class);
						startActivity(a);
						finish();
					} else {

						Intent intent = new Intent(SignIn.this, Register.class);
						intent.putExtra("email", emaiil);
						intent.putExtra("nama", namaa);
						setResult(-1, intent);
						startActivityForResult(intent, 99);
					}

				} catch (Exception e) {
					// TODO: handle exception
					showSnackbar(loginplus, (int) R.string.message_no_network_connection, false);
				}

		} else {
			//showSnackbar(loginplus, (int) R.string.message_no_network_connection, false);
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}

	public class Masuk extends AsyncTask<String, String, String>
	{
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
		ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(SignIn.this);
			pDialog.setMessage("Signing In");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			JSONParser jParser = new JSONParser();

			JSONObject json = jParser.getJSONFromUrl(url);

			try {
				success = json.getString("success");

				Log.e("error", "nilai sukses=" + success);

				JSONArray hasil = json.getJSONArray("login");

				if (success.equals("1")) {

					for (int i = 0; i < hasil.length(); i++) {

						JSONObject c = hasil.getJSONObject(i);

						String id = c.getString("id").trim();
						String nama = c.getString("nama").trim();
						String email = c.getString("email").trim();
						String phone = c.getString("phone").trim();
						User user = new User(c.getString("id"),
								c.getString("nama"),
								c.getString("email"),
								c.getString("phone"),
								c.getString("alamat"));
						session.storeUser(user);

						Log.e("ok", " ambil data");

					}
				} else {
					showSnackbar(login, (int) R.string.eror_login, false);
				}

			} catch (Exception e) {
				// TODO: handle exception
				showSnackbar(login, (int) R.string.message_no_network_connection, false);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (success.equals("1")) {
				a = new Intent(SignIn.this, LoadingLanding.class);
				startActivity(a);
				finish();
			}else{
				showSnackbar(login, (int) R.string.eror_login, false);
			}

		}
		
	}
	private boolean validateUName() {
		if (inputuname.getText().toString().trim().isEmpty()) {
			inputLayoutuname.setError(getString(R.string.err_msg_uname));
			requestFocus(inputuname);
			return false;
		} else {
			inputLayoutuname.setErrorEnabled(false);
		}

		return true;
	}

	private boolean validatePassword() {
		if (inputpassword.getText().toString().trim().isEmpty()) {
			inputLayoutPassword.setError(getString(R.string.err_msg_password));
			requestFocus(inputpassword);
			return false;
		} else {
			inputLayoutPassword.setErrorEnabled(false);
		}

		return true;
	}



	private void requestFocus(View view) {
		if (view.requestFocus()) {
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}
	}

	private class MyTextWatcher implements TextWatcher {

		private View view;

		private MyTextWatcher(View view) {
			this.view = view;
		}

		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}

		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		}

		public void afterTextChanged(Editable editable) {
			switch (view.getId()) {
				case R.id.input_uname:
					validateUName();
					break;
				case R.id.input_password:
					validatePassword();
					break;
			}
		}
	}

}
