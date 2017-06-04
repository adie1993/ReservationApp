package app.adie.reservation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Date;

import app.adie.reservation.Fragment.CalendarPromoFragment;
import app.adie.reservation.Fragment.KursiFragment;
import app.adie.reservation.R;
import app.adie.reservation.utils.DateUtils;

public class CalendarPromoActivity extends BaseActivity {
    private static String tgl;
    private static String brgkt;
    private static String tuju;
    private static String kode;
    private CalendarPromoFragment mFragment;
    private Toolbar toolbar;
    private static long maxDate;
    private static long minDate;
    private static boolean pickDate = false;
    private static long selectedDate;
    public static AppCompatActivity activity;
    private KursiFragment mKursiFragment;
    private TextView txtpromo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_calendar_promo);
        setIsLastActivities(true);
        Bundle extras = this.getIntent().getExtras();
        activity = CalendarPromoActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbaar);
        txtpromo = (TextView) findViewById(R.id.txtpromo);
        txtpromo.setHint("Pilih Tanggal Keberangkatan");
        txtpromo.setText("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        if (extras != null) {
            this.tgl = extras.getString("tgl_akhir");
            this.brgkt = extras.getString("asal");
            this.tuju = extras.getString("tujuan");
            this.kode = extras.getString("kode");
            Date date = new Date();

            Date tgl_akh = DateUtils.stringToDate(tgl);
            minDate = DateUtils.dateToLong(date);

            selectedDate = DateUtils.dateToLong(date);
            maxDate = DateUtils.dateToLong(tgl_akh);
        }

        if (savedInstanceState == null) {
            this.mFragment = CalendarPromoFragment.newInstance(this.selectedDate, this.pickDate, this.minDate, this.maxDate,this.kode,this.brgkt,this.tuju);
            getSupportFragmentManager().beginTransaction().replace(R.id.containerVieww, this.mFragment).commit();

            return;
        }
        this.mFragment = (CalendarPromoFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mCalendarFragment");

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
