package app.adie.reservation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import app.adie.reservation.Fragment.KursiFragment;
import app.adie.reservation.R;

public class KursiActivity extends BaseActivity {
    private static String tgl;
    private static String brgkt;
    private static String tuju;
    private static String nama_penum;
    private static String nama;
    private static String email;
    private static String phone;
    private static String jam;
    private static String batas;
    private static int id_jadwal;
    private static int id_armada;
    private static int jml_penumpang;
    private Toolbar toolbar;
    private static int harga,potong;
    public static AppCompatActivity activity;
    private KursiFragment mKursiFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_kursi);
        setIsLastActivities(true);
        Bundle extras = this.getIntent().getExtras();
        activity = KursiActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbaar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            this.id_jadwal = extras.getInt("id_jadwal");
            this.id_armada = extras.getInt("id_armada");
            this.jml_penumpang = extras.getInt("jml_penumpang");
            this.harga = extras.getInt("harga");
            this.potong = extras.getInt("potong");
        }

        if (savedInstanceState == null) {
            this.mKursiFragment = KursiFragment.newInstance(this.tgl,this.brgkt,this.tuju,this.nama_penum,this.nama,this.email,this.phone,this.jam,this.batas, this.id_jadwal,this.harga,this.id_armada,this.jml_penumpang,this.potong);
            getSupportFragmentManager().beginTransaction().replace(R.id.containerVieww, this.mKursiFragment).commit();
            return;
        }
        this.mKursiFragment = (KursiFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mKursiFragment");

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
