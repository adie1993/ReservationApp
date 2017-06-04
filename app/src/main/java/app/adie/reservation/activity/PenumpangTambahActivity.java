package app.adie.reservation.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.List;

import app.adie.reservation.R;
import app.adie.reservation.activity.BaseActivity;
import app.adie.reservation.entity.Penumpang;
import app.adie.reservation.entity.SqliteHelper;

/**
 * Created by Adie on 11/05/2016.
 */
public class PenumpangTambahActivity extends BaseActivity implements OnClickListener{
    private Button mButtonBatal;
    private Button mButtonTambah;
    private EditText mEmail;
    private LinearLayout mLayoutButton;
    private EditText mNamaDepan;
    private EditText mNomorHp;
    private SqliteHelper sqLiteHelper;
    private List<Penumpang> mPenumpangs;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_penumpang_tambah);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        sqLiteHelper = new SqliteHelper(this);
        this.mButtonBatal = (Button) findViewById(R.id.batal);
        this.mButtonTambah = (Button) findViewById(R.id.tambah);
        this.mNamaDepan = (EditText) findViewById(R.id.nama_depan);
        this.mNomorHp = (EditText) findViewById(R.id.input_phone);
        this.mLayoutButton = (LinearLayout) findViewById(R.id.button);
        this.mButtonBatal.setOnClickListener(this);
        this.mButtonTambah.setOnClickListener(this);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.batal:
                onBackPressed();
                finish();
                return;
            case R.id.tambah:
                if (isDataValid()) {
                    this.mNamaDepan.setEnabled(false);
                    this.mNomorHp.setEnabled(false);
                    this.mLayoutButton.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tambahPenumpang();
                        }
                    }, 1500);
                    return;
                }
                showSnackbar(getCurrentFocus(), (int) R.string.message_form_not_complete, false);
                return;
            default:
                return;
        }
    }

    private void tambahPenumpang() {
        sqLiteHelper.tambahdatauser(mNamaDepan.getText().toString());
        Intent intent = new Intent();
        setResult(-1, intent);
        finish();
    }

    public boolean isDataValid() {
        boolean isNamaDepanValid;
        if (getNamaDepan().isEmpty()) {
            isNamaDepanValid = false;
        } else {
            isNamaDepanValid = true;
        }
        boolean isNomorValid;
        if (getNomorHp().isEmpty()) {
            isNomorValid = false;
        } else {
            isNomorValid = true;
        }
        if (isNamaDepanValid && isNomorValid) {
            return true;
        }
        return false;
    }
    private String getNamaDepan() {
        return this.mNamaDepan.getText().toString().trim();
    }

    private String getNomorHp() {
        return this.mNomorHp.getText().toString().trim();
    }

}
