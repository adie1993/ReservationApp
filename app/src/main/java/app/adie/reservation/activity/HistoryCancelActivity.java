package app.adie.reservation.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.adie.reservation.Fragment.DialogBatalFragment;
import app.adie.reservation.JSONParser;
import app.adie.reservation.R;
import app.adie.reservation.SessionManager;
import app.adie.reservation.utils.DateUtils;
import app.adie.reservation.utils.StringUtils;
import app.adie.reservation.view.widget.Fab;

public class HistoryCancelActivity extends BaseActivity implements OnClickListener {
    private static String tgl;
    private static String asal,tujuan,nama,nodiuk,id_pemesanan,kodtik,poeayeuna,jamayeuna,iduser;
    private static String jam;
    private static String kode;
    private static String total;
    private int statusBarColor;
    private MaterialSheetFab materialSheetFab;
    Date tanggalberangkat;
    public Fragment mFragment;
    private int b=1;
    SessionManager session;
    private WebView myWebView;
    private Toolbar toolbar;
    private static int harga,uangkem,diskon;
    public static AppCompatActivity activity;
    private DialogBatalFragment mKetFragment;
    private static String url = "http://10.42.0.1/kraka/api/pembatalan.php";
    JSONParser jParser = new JSONParser();
    private EditText inputkode, inputharga, inputdiskon,inputtot,inputmetode,inputbatas,inputkeberangkatan,inputtuju,inptgl,inpjam,inppenum,inpkursi,inpuang;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_detail_cancel);
        Bundle extras = this.getIntent().getExtras();
        activity = HistoryCancelActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbarf);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pemesanan Batal");
        setupFab();

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        iduser = user.get(SessionManager.KEY_ID);

        inputkeberangkatan = (EditText) findViewById(R.id.inp_keb);
        inputtuju = (EditText) findViewById(R.id.inp_tujuan);
        myWebView = (WebView) findViewById(R.id.webviw);
        inptgl = (EditText)findViewById(R.id.inp_tggl);
        inpjam = (EditText)findViewById(R.id.inp_jam);
        inppenum = (EditText)findViewById(R.id.inp_penumpang);
        inputkode = (EditText) findViewById(R.id.inp_kodetiket);
        inputtot = (EditText) findViewById(R.id.inp_total);
        inpkursi = (EditText)findViewById(R.id.inp_kursi);
        inputharga = (EditText) findViewById(R.id.inp_harga);
        inputdiskon = (EditText) findViewById(R.id.inp_diskon);
        inpuang = (EditText) findViewById(R.id.inp_uang);
        materialSheetFab.showFab();

        if (extras != null) {
            this.tgl = extras.getString("tgl");
            this.asal = extras.getString("asal");
            this.tujuan = extras.getString("tujuan");
            this.nama = extras.getString("nama");
            this.nodiuk = extras.getString("no_seat");
            this.jam = extras.getString("jam");
            this.uangkem = extras.getInt("uangkem");
            this.kode = extras.getString("kode");
            this.kodtik = extras.getString("kodetiket");
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
            inputkode.setText(kodtik);
            inputharga.setText(StringUtils.toRupiahFormat(harga));
            inputdiskon.setText(StringUtils.toRupiahFormat(diskon));
            inputtot.setText(StringUtils.toRupiahFormat(total));
            inpuang.setText(StringUtils.toRupiahFormat(uangkem));


        }


        if (savedInstanceState == null) {
            this.mKetFragment = DialogBatalFragment.newInstance(kodtik);
            getSupportFragmentManager().beginTransaction().replace(R.id.containerterms, this.mKetFragment).commit();
            return;
        }
        this.mKetFragment = (DialogBatalFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mTermsFragment");

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
            Intent a = new Intent(HistoryCancelActivity.this, MainActivity.class);
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
        findViewById(R.id.fab_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v== findViewById(R.id.fab_cancel)){
            myWebView.setWebViewClient(new WebViewClient());

            myWebView.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                    //start download
                    DownloadPDF downloadPDF = new DownloadPDF();
                    downloadPDF.execute(url, userAgent, contentDisposition);
                }
            });
            Toast.makeText(getApplicationContext(), "Bukti Pembatalan berhasil di download harap install pdf viewer untuk melihat Bukti Pembatalan Anda" , Toast.LENGTH_LONG).show();
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            myWebView.loadUrl("http://10.42.0.1/kraka/api/buktipembatalan.php?id="+id_pemesanan);
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

    public void displayPdf(String filename)
    {
        try
        {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Krakaline E-TIket/" + filename);

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            startActivity(intent);
        }
        catch (Exception e)
        {
            Log.i("TAG", e.getMessage());
        }
    }
    private class DownloadPDF extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... sUrl) {
            try {
                URL url = new URL(sUrl[0]);

                File myDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS).toString() + "/Krakaline E-Tiket");

                // create the directory if it does not exist
                if (!myDir.exists()) myDir.mkdirs();

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.connect();

                //get filename from the contentDisposition
                String filename = null;
                Pattern p = Pattern.compile("\"([^\"]*)\"");
                Matcher m = p.matcher(sUrl[2]);
                while (m.find()) {
                    filename = m.group(1);
                }

                File outputFile = new File(myDir, filename);

                InputStream input = new BufferedInputStream(connection.getInputStream());
                OutputStream output = new FileOutputStream(outputFile);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }
                connection.disconnect();
                output.flush();
                output.close();
                input.close();

                displayPdf(filename);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }





}
