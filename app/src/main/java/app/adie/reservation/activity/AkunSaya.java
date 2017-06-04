package app.adie.reservation.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.adie.reservation.JSONParser;
import app.adie.reservation.R;
import app.adie.reservation.SessionManager;
import app.adie.reservation.app.MyApplication;
import app.adie.reservation.entity.User;
import app.adie.reservation.utils.ConnectionDetector;

public class AkunSaya extends BaseActivity {

    private Toolbar toolbar;
    private EditText inputUsername,inputName, inputEmail, inputAlamat,inputPhone;
    private TextInputLayout inputLayoutUsername,inputLayoutName, inputLayoutEmail, inputLayoutAlamat,inputLayoutPhone;
    private Button btnUpdate;
    ProgressDialog pDialog;
    String nama,email;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    JSONParser jsonParser = new JSONParser();
    SessionManager session;
    private static String url = "http://10.42.0.1/kraka/api/ubahakun.php";
    private static String urlloc = "http://10.42.0.1/kraka/api/ubahakun.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akunsaya);

        toolbar = (Toolbar) findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Akun Saya");
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        session = new SessionManager(getApplicationContext());
        inputLayoutAlamat= (TextInputLayout) findViewById(R.id.input_layout_alamat);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);

        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);

        inputAlamat = (EditText) findViewById(R.id.input_alamat);
        inputName = (EditText) findViewById(R.id.input_name);
        inputEmail = (EditText) findViewById(R.id.input_email);

        inputPhone = (EditText) findViewById(R.id.input_phone);
        btnUpdate = (Button) findViewById(R.id.btn_update);

        inputAlamat.addTextChangedListener(new MyTextWatcher(inputAlamat));
        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPhone.addTextChangedListener(new MyTextWatcher((inputPhone)));

        String nama = MyApplication.getInstance().getPrefManager().getUser().getName();
        String email = MyApplication.getInstance().getPrefManager().getUser().getEmail();
        String phone = MyApplication.getInstance().getPrefManager().getUser().getPhone();
        String alamat = MyApplication.getInstance().getPrefManager().getUser().getAlamat();
        inputName.setText(nama);
        inputEmail.setText(email);
        inputPhone.setText(phone);
        inputAlamat.setText(alamat);
        btnUpdate.setVisibility(View.GONE);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                updateVisible();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void updateVisible() {
        getSupportActionBar().setTitle("Ubah Detail Akun");
        this.btnUpdate.setVisibility(View.VISIBLE);
        this.inputName.setEnabled(true);
        this.inputName.setFocusable(true);
        this.inputEmail.setEnabled(true);
        this.inputEmail.setFocusable(true);
        this.inputPhone.setEnabled(true);
        this.inputPhone.setFocusable(true);
        this.inputAlamat.setEnabled(true);
        this.inputAlamat.setFocusable(true);
    }

    private void updateInvisible() {
        getSupportActionBar().setTitle("Akun Saya");
        this.btnUpdate.setVisibility(View.GONE);
        this.inputName.setEnabled(false);
        this.inputName.setFocusable(false);
        this.inputEmail.setEnabled(false);
        this.inputEmail.setFocusable(false);
        this.inputPhone.setEnabled(false);
        this.inputPhone.setFocusable(false);
        this.inputAlamat.setEnabled(false);
        this.inputAlamat.setFocusable(false);

    }
    public void showSnackbarr(View view, int message, boolean length) {

        if (length) {
            snackbar= Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            View snackBarview = snackbar.getView();
            snackBarview.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
            snackbar.show();
        } else {
            snackbar=Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            View snackBarview = snackbar.getView();
            snackBarview.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
            snackbar.show();
        }
    }
    private void submitForm() {

        if (!validateName()) {
            return;
        }
        if (!validateAlamat()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }


        if (!validatePhone()) {
            return;
        }
        if (isInternetPresent) {
            new Update().execute();
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            showSnackbar(btnUpdate, (int) R.string.message_no_network_connection, true);
        }

    }

    public class Update extends AsyncTask<String, String, String> {

        String success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AkunSaya.this);
            pDialog.setMessage("Mohon menunggu");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String id = MyApplication.getInstance().getPrefManager().getUser().getId();
            String strnama = inputName.getText().toString();
            String stremail = inputEmail.getText().toString();
            String strphone = inputPhone.getText().toString();
            String stralam = inputAlamat.getText().toString();
            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("id", id));
            nvp.add(new BasicNameValuePair("nama", strnama));
            nvp.add(new BasicNameValuePair("email", stremail));
            nvp.add(new BasicNameValuePair("alamat", stralam));
            nvp.add(new BasicNameValuePair("phone", strphone));

            JSONObject json = jsonParser.makeHttpRequest(url, "POST", nvp);
            try {
                success = json.getString("success");
                if(success.equals("1")){

                    User user = new User(id,
                            strnama,
                            stremail,
                            strphone,stralam);
                    session.storeUser(user);

                }else if(success.equals("0")){
                    showSnackbar(btnUpdate, (int) R.string.eror_pesan, false);
                }

            } catch (Exception e) {
               // showSnackbar(btnSignUp, (int) R.string.eror_login, false);
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if (success.equals("1")) {
                showSnackbarr(btnUpdate, (int) R.string.edit_akun, false);
                updateInvisible();

            } else if (success.equals("0")){
                showSnackbar(btnUpdate, (int) R.string.eror_pesan, false);
            }else if(success.equals("3")){
                showSnackbar(btnUpdate, (int) R.string.eror_reg, false);
            }
        }

    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateAlamat() {
        if (inputAlamat.getText().toString().trim().isEmpty()) {
            inputLayoutAlamat.setError(getString(R.string.err_msg_alm));
            requestFocus(inputAlamat);
            return false;
        } else {
            inputLayoutAlamat.setErrorEnabled(false);
        }

        return true;
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


    private boolean validatePhone() {
        if (inputPhone.getText().toString().trim().isEmpty()) {
            inputLayoutPhone.setError(getString(R.string.err_msg_phone));
            requestFocus(inputPhone);
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
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
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_phone:
                    validatePhone();
                    break;
                case R.id.input_alamat:
                    validateAlamat();
                    break;
            }
        }
    }

}
