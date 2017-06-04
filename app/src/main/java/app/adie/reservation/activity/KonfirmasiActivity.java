package app.adie.reservation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import app.adie.reservation.Fragment.KonfirmasiFragment;
import app.adie.reservation.R;
import app.adie.reservation.view.widget.LoadingIndicator;

public class KonfirmasiActivity extends BaseActivity {
    private static String tgl;
    private static String brgkt;
    private static String tuju;
    private static String nama_penum;
    private static String nama;
    private static String email;
    private static String phone;
    private static String jam;
    private static String batas;
    private static String seat;
    private static String total;
    private static int id_jadwal;
    private static int id_armada;
    private static int jml_penumpang;
    private static LoadingIndicator mLoadingIndicator;
    private Toolbar toolbar;
    private static int harga,potong;
    public static AppCompatActivity activity;
    private KonfirmasiFragment mKonfirmasiFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_detail);
        setIsLastActivities(true);
        Bundle extras = this.getIntent().getExtras();
        activity = KonfirmasiActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbaar);
        mLoadingIndicator = (LoadingIndicator) findViewById(R.id.loading_indicator);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setLoadingIndicator(mLoadingIndicator,false);
        if (extras != null) {
            this.tgl = extras.getString("tanggal");
            this.brgkt = extras.getString("keberangkatan");
            this.tuju = extras.getString("tujuan");
            this.nama_penum = extras.getString("nama_penumpang");
            this.nama = extras.getString("nama");
            this.email = extras.getString("email");
            this.phone = extras.getString("phone");
            this.jam = extras.getString("jam");
            this.batas = extras.getString("batas");
            this.seat = extras.getString("seats");
            this.total = extras.getString("total");
            this.id_jadwal = extras.getInt("id_jadwal");
            this.id_armada = extras.getInt("id_armada");
            this.jml_penumpang = extras.getInt("jml_penumpang");
            this.harga = extras.getInt("harga");
            this.potong = extras.getInt("potong");
        }

        if (savedInstanceState == null) {
            this.mKonfirmasiFragment = KonfirmasiFragment.newInstance(this.tgl,this.brgkt,this.tuju,this.nama_penum,this.nama,this.email,this.phone,this.jam,this.batas,this.seat,this.total, this.id_jadwal,this.harga,this.id_armada,this.jml_penumpang,this.potong);
            getSupportFragmentManager().beginTransaction().replace(R.id.containerVieww, this.mKonfirmasiFragment).commit();
            return;
        }
        this.mKonfirmasiFragment = (KonfirmasiFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mKonfirmasiFragment");

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
    public boolean setLoadingIndicator(LoadingIndicator mLoadingIndicator, boolean isShow) {
        if (isShow) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            return true;
        }
        mLoadingIndicator.setVisibility(View.GONE);
        return false;
    }

    public static void newInstance() {
        mLoadingIndicator.setVisibility(View.VISIBLE);

    }
}
