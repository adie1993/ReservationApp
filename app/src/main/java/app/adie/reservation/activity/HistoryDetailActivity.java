package app.adie.reservation.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import java.util.Date;

import app.adie.reservation.Fragment.DialogPembayaranFragment;
import app.adie.reservation.R;
import app.adie.reservation.utils.DateUtils;
import app.adie.reservation.utils.StringUtils;
import app.adie.reservation.view.widget.Fab;

public class HistoryDetailActivity extends BaseActivity implements OnClickListener {
    private static String tgl;
    private static String asal,tujuan,nama,nodiuk,id_pemesanan;
    private static String jam;
    private static String batas;
    private static String kode;
    private static String total;
    private int statusBarColor;
    private MaterialSheetFab materialSheetFab;
    Date tanggalberangkat;
    private int b=1;
    private Toolbar toolbar;
    private static int harga,diskon;
    public static AppCompatActivity activity;
    private DialogPembayaranFragment mKetFragment;
    private EditText inputkode, inputharga, inputdiskon,inputtot,inputmetode,inputbatas,inputkeberangkatan,inputtuju,inptgl,inpjam,inppenum,inpkursi;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.fragment_detail);
        setIsLastActivities(true);
        Bundle extras = this.getIntent().getExtras();
        activity = HistoryDetailActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbarf);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pemesanan");
        setupFab();
        inputkeberangkatan = (EditText) findViewById(R.id.inp_keb);
        inputtuju = (EditText) findViewById(R.id.inp_tujuan);
        inptgl = (EditText)findViewById(R.id.inp_tggl);
        inpjam = (EditText)findViewById(R.id.inp_jam);
        inppenum = (EditText)findViewById(R.id.inp_penumpang);
        inputkode = (EditText) findViewById(R.id.inp_kodebooking);
        inputharga = (EditText) findViewById(R.id.inp_harga);
        inputdiskon = (EditText) findViewById(R.id.inp_diskon);
        inputtot = (EditText) findViewById(R.id.inp_total);
        inputbatas = (EditText) findViewById(R.id.inp_batas);
        inpkursi = (EditText)findViewById(R.id.inp_kursi);
        materialSheetFab.showFab();

        if (extras != null) {
            this.tgl = extras.getString("tgl");
            this.asal = extras.getString("asal");
            this.tujuan = extras.getString("tujuan");
            this.nama = extras.getString("nama");
            this.nodiuk = extras.getString("no_seat");
            this.batas = extras.getString("batas");
            this.jam = extras.getString("jam");
            this.kode = extras.getString("kode");
            this.total = extras.getString("total");
            this.harga = extras.getInt("harga");
            this.diskon = extras.getInt("diskon");
            this.id_pemesanan = extras.getString("id_pem");
            tanggalberangkat = DateUtils.stringToDate(tgl);
            inputkeberangkatan.setText(asal);
            inputtuju.setText(tujuan);
            inptgl.setText(DateUtils.getDateFormatCard(tanggalberangkat));
            inpkursi.setText(nodiuk);
            inpjam.setText(jam);
            inppenum.setText(nama);
            inputkode.setText(kode);
            inputharga.setText(StringUtils.toRupiahFormat(harga));
            inputtot.setText(StringUtils.toRupiahFormat(total));
            inputdiskon.setText(StringUtils.toRupiahFormat(diskon));
            inputbatas.setText(tgl+" "+batas);
        }

        if (savedInstanceState == null) {
            this.mKetFragment = DialogPembayaranFragment.newInstance(tgl,batas);
            getSupportFragmentManager().beginTransaction().replace(R.id.containerterms, this.mKetFragment).commit();
            return;
        }
        this.mKetFragment = (DialogPembayaranFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mTermsFragment");

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
        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
        } else {
            Intent a = new Intent(HistoryDetailActivity.this, MainActivity.class);
            a.putExtra("get", b);
            startActivityForResult(a, 20);
            finish();
        }


    }
    private void setupFab() {

        Fab fab = (Fab) findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.background_card);
        int fabColor = getResources().getColor(R.color.main);

        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        // Set material sheet event listener
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Save current status bar color
                statusBarColor = getStatusBarColor();
                // Set darker status bar color to match the dim overlay
                setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            @Override
            public void onHideSheet() {
                // Restore status bar color
                setStatusBarColor(statusBarColor);
            }
        });

        // Set material sheet item click listeners
        findViewById(R.id.fab_konfirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v== findViewById(R.id.fab_konfirm)){
            Intent a = new Intent(HistoryDetailActivity.this, PembayaranActivity.class);
            a.putExtra("id_pemesan", id_pemesanan);
            a.putExtra("kode", kode);
            a.putExtra("tgl", tgl);
            a.putExtra("batas",batas);
            a.putExtra("harga", harga);
            a.putExtra("diskon", diskon);
            a.putExtra("total", total);
            startActivityForResult(a, 20);
            materialSheetFab.hideSheet();
        }
    }
    private int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getWindow().getStatusBarColor();
        }
        return 0;
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }
}
