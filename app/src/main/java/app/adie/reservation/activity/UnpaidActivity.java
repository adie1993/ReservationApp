package app.adie.reservation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import app.adie.reservation.Fragment.TermsFragment;
import app.adie.reservation.R;
import app.adie.reservation.utils.StringUtils;

public class UnpaidActivity extends BaseActivity {
    private static String tgl;

    private static String jam;
    private static String batas;
    private static String kode;
    private static String total;
    private int b=1;
    private Toolbar toolbar;
    private static int harga;
    public static AppCompatActivity activity;
    private TermsFragment mTermsFragment;
    private EditText inputkode, inputharga, inputdiskon,inputtot,inputmetode,inputbatas;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.fragment_berhasil);
        setIsLastActivities(true);
        Bundle extras = this.getIntent().getExtras();
        activity = UnpaidActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbarf);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pemesanan Berhasil");

        inputkode = (EditText) findViewById(R.id.inp_kodebooking);
        inputharga = (EditText) findViewById(R.id.inp_harga);
        inputdiskon = (EditText) findViewById(R.id.inp_diskon);
        inputtot = (EditText) findViewById(R.id.inp_total);
        inputbatas = (EditText) findViewById(R.id.inp_batas);

        if (extras != null) {
            this.tgl = extras.getString("tanggal");
            this.batas = extras.getString("batas");
            this.kode = extras.getString("kode");
            this.total = extras.getString("total");
            this.harga = extras.getInt("harga");

            inputkode.setText(kode);
            inputharga.setText(StringUtils.toRupiahFormat(harga));
            inputtot.setText(StringUtils.toRupiahFormat(total));
            inputdiskon.setText(StringUtils.toRupiahFormat(0));
            inputbatas.setText(tgl+" "+batas);
        }

        if (savedInstanceState == null) {
            this.mTermsFragment = TermsFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.containerterms, this.mTermsFragment).commit();
            return;
        }
        this.mTermsFragment = (TermsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mTermsFragment");

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
    @Override
    public void onBackPressed() {
        Intent a = new Intent(UnpaidActivity.this, MainActivity.class);
        a.putExtra("get", b);
        startActivityForResult(a, 20);
        finish();

    }
}
