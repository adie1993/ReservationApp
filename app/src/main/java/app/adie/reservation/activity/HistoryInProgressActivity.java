package app.adie.reservation.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.net.URL;
import java.util.Date;

import app.adie.reservation.Fragment.DialogInProgressFragment;
import app.adie.reservation.R;
import app.adie.reservation.utils.DateUtils;
import app.adie.reservation.utils.StringUtils;

public class HistoryInProgressActivity extends BaseActivity  {
    private static String tgl;
    private static String asal,tujuan,nama,nodiuk,id_pemesanan;
    private static String jam;
    private static String bukti;
    private static String kode;
    private static String total;
    Date tanggalberangkat;
    private int b=1;
    private Toolbar toolbar;
    private static int harga,diskon;
    private ImageView img;
    public static AppCompatActivity activity;
    private DialogInProgressFragment mKetFragment;
    private EditText inputkode, inputharga, inputdiskon,inputtot,inputmetode,inputbatas,inputkeberangkatan,inputtuju,inptgl,inpjam,inppenum,inpkursi;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.fragment_detail_inprogress);
        setIsLastActivities(true);
        Bundle extras = this.getIntent().getExtras();
        activity = HistoryInProgressActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbarf);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pemesanan");

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
        img = (ImageView)findViewById(R.id.imageView);
        if (extras != null) {
            this.tgl = extras.getString("tgl");
            this.asal = extras.getString("asal");
            this.tujuan = extras.getString("tujuan");
            this.nama = extras.getString("nama");
            this.nodiuk = extras.getString("no_seat");
            this.bukti = extras.getString("bukti");
            this.jam = extras.getString("jam");
            this.kode = extras.getString("kode");
            this.total = extras.getString("total");
            this.harga = extras.getInt("harga");
            this.diskon = extras.getInt("diskon");
            this.id_pemesanan = extras.getString("id_pem");
            String gambar = "http://10.42.0.1/kraka/"+bukti;
            Glide.with(getApplicationContext()).load(gambar).thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(img);

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
        }
        Log.d("bukti: ", bukti);
        if (savedInstanceState == null) {
            this.mKetFragment = DialogInProgressFragment.newInstance(kode);
            getSupportFragmentManager().beginTransaction().replace(R.id.containerterms, this.mKetFragment).commit();
            return;
        }
        this.mKetFragment = (DialogInProgressFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mTermsFragment");

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
        Intent a = new Intent(HistoryInProgressActivity.this, MainActivity.class);
        a.putExtra("get", b);
        startActivityForResult(a, 20);
        finish();

    }


}
