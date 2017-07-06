package app.adie.reservation.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.adie.reservation.JSONParser;
import app.adie.reservation.R;
import app.adie.reservation.SessionManager;

public class LupaPass extends BaseActivity {

    private Toolbar toolbar;
    private EditText inputEmail;
    private TextInputLayout inputLayoutEmail;
    private Button btnconfirm;
    private TextView txtemail;
    ProgressDialog pDialog;
    String nama,email;
    private String TAG = LupaPass.class.getSimpleName();
    FrameLayout frame;
    JSONParser jsonParser = new JSONParser();
    SessionManager session;
    private static String url = "http://10.0.2.2/kraka/api/fpass.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa);

        toolbar = (Toolbar) findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reset Password");



        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);

        inputEmail = (EditText) findViewById(R.id.input_email);
        frame = (FrameLayout) findViewById(R.id.framelupa);
        txtemail = (TextView)findViewById(R.id.txtemail);
        btnconfirm = (Button) findViewById(R.id.btn_confirm);

        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        frame.setVisibility(View.GONE);

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });


    }

    /**
     * Validating form
     */
    private void submitForm() {

        if (!validateEmail()) {
            return;
        }

        new lupaexec().execute();
    }

    public class lupaexec extends AsyncTask<String, String, String> {

        String success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LupaPass.this);
            pDialog.setMessage("Mohon Tunggu");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String stremail = inputEmail.getText().toString();

            List<NameValuePair> nvp = new ArrayList<NameValuePair>();

            nvp.add(new BasicNameValuePair("email", stremail));


            JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
            try {
                success = json.getString("success");

            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
                runOnUiThread(new Runnable(){
                    public void run() {
                        showSnackbar(btnconfirm, (int) R.string.eror_login, false);
                    }
                });

            }

            return null;
        }

        protected void onPostExecute(String params) {
            pDialog.dismiss();

            if (success.equals("1")) {
                frame.setVisibility(View.VISIBLE);
                txtemail.setText(inputEmail.getText().toString());
            } else{
                showSnackbar(btnconfirm, (int) R.string.eror_lupa, false);
            }
        }

    }




    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }



    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

                case R.id.input_email:
                    validateEmail();
                    break;

            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
