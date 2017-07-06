package app.adie.reservation.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.adie.reservation.JSONParser;
import app.adie.reservation.activity.KonfirmasiActivity;
import app.adie.reservation.activity.PemesananActivity;
import app.adie.reservation.R;
import app.adie.reservation.SessionManager;
import app.adie.reservation.entity.AlertDialogAction;
import app.adie.reservation.utils.StringUtils;

public class KonfirmasiFragment extends BaseFragment implements OnClickListener {
    private static String tanggal,berangkat,tujuan,nama_pen,nam,emai,phon,jam,seat,tot,batas,iduser;
    private static int jadwal;
    private static int id;
    private static int jml_penumpang;
    private static int hargakursi,potongan,hargapotong;
    private TextView mTotal;
    private TextView mHarga;
    private TextView mKeberangkatan;
    private TextView mTujuan;
    private TextView mJumPenumpang;
    private TextView mTanggal;
    private TextView mJam;
    SessionManager session;
    private TextView mNomor;
    private TextView mDiskon;
    private TextView mNama;
    private TextView mEmail;
    private TextView mPhone;
    private TextView mNamaPenum;
    private Button btn_pesan;
    JSONParser jParser = new JSONParser();
    private static String url = "http://10.0.2.2/kraka/api/konfirmasipesan.php";
    private static String urll = "http://10.0.2.2/kraka/api/konfirmasi.php";



    public static KonfirmasiFragment newInstance(String tgl,String brgkt,String tuju,String nama_penum,String nama,String email,String phone,String ja,String ba,String shit,String total, int id_jadwal, int harga,int id_armada,int jmlh,int ptong ) {
        KonfirmasiFragment mFragment = new KonfirmasiFragment();
        tanggal = tgl;
        berangkat = brgkt;
        tujuan = tuju;
        nama_pen = nama_penum;
        nam = nama;
        emai = email;
        phon = phone;
        id= id_armada;
        batas = ba;
        jam = ja;
        jadwal = id_jadwal;
        jml_penumpang = jmlh;
        seat = shit;
        tot = total;
        hargakursi = harga;
        potongan = ptong;
        return mFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KonfirmasiActivity.activity.getSupportActionBar().setTitle("Konfirmasi Pesanan");
        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        iduser = user.get(SessionManager.KEY_ID);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_konfirmasi, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        this.btn_pesan = (Button) view.findViewById(R.id.button_pesan);
        btn_pesan.setOnClickListener(this);

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mKeberangkatan = (TextView) getView().findViewById(R.id.txtbrgkt);
        this.mTujuan = (TextView) getView().findViewById(R.id.txttujuan);
        this.mJumPenumpang = (TextView) getView().findViewById(R.id.txtpnpmg);
        this.mTanggal = (TextView) getView().findViewById(R.id.txttggl);
        this.mJam = (TextView) getView().findViewById(R.id.txtjambrgkt);
        this.mNomor = (TextView) getView().findViewById(R.id.txtnokursi);
        this.mHarga = (TextView) getView().findViewById(R.id.txthrga);
        this.mDiskon = (TextView) getView().findViewById(R.id.txtdiskon);
        this.mTotal = (TextView) getView().findViewById(R.id.txttotal);
        this.mNama = (TextView) getView().findViewById(R.id.txtnamapemesan);
        this.mEmail = (TextView) getView().findViewById(R.id.txtemailpmsan);
        this.mPhone = (TextView) getView().findViewById(R.id.txtnotlp);
        this.mNamaPenum = (TextView) getView().findViewById(R.id.txtnamapenumpang);


        hargapotong = hargakursi - potongan;
        mKeberangkatan.setText(berangkat);
        mTujuan.setText(tujuan);
        mJumPenumpang.setText(jml_penumpang+" Orang");
        mTanggal.setText(tanggal);
        mJam.setText(jam);
        mNomor.setText(seat);
        mHarga.setText(StringUtils.toRupiahFormat(hargapotong));
        mDiskon.setText(StringUtils.toRupiahFormat(potongan));
        mTotal.setText(StringUtils.toRupiahFormat(tot));
        mNama.setText(nam);
        mEmail.setText(emai);
        mPhone.setText(phon);
        mNamaPenum.setText(nama_pen);

    }



    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }



    private void postBook() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String jd = String.valueOf(jadwal);
        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("jadwal", String.valueOf(jadwal)));
        nvp.add(new BasicNameValuePair("tanggal", tanggal));
        nvp.add(new BasicNameValuePair("jumlah", String.valueOf(jml_penumpang)));
        nvp.add(new BasicNameValuePair("harga", String.valueOf(hargapotong)));
        nvp.add(new BasicNameValuePair("potongan", String.valueOf(potongan)));
        nvp.add(new BasicNameValuePair("total", tot));
        nvp.add(new BasicNameValuePair("id_user", iduser));

        JSONObject json = jParser.makeHttpRequest(url, "POST", nvp);
        try {
            String success;
            final String kode;
            String sucesss;
            success = json.getString("success");
            kode = json.getString("kode");
            Log.d("stat: ", success.toString());
            if (success.equals("1")) {
                List<NameValuePair> dwp = new ArrayList<NameValuePair>();
                dwp.add(new BasicNameValuePair("jadwal", String.valueOf(jadwal)));
                dwp.add(new BasicNameValuePair("nama", nama_pen));
                dwp.add(new BasicNameValuePair("jumlah", String.valueOf(jml_penumpang)));
                dwp.add(new BasicNameValuePair("no_seat", seat));
                dwp.add(new BasicNameValuePair("tanggal", tanggal));
                dwp.add(new BasicNameValuePair("id_user", iduser));

                JSONObject jsonn = jParser.makeHttpRequest(urll, "POST", dwp);
                KonfirmasiActivity.activity.getSupportActionBar().setTitle("");
                KonfirmasiActivity.activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                KonfirmasiActivity.newInstance();
                btn_pesan.setEnabled(false);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent;
                        intent = new Intent(getActivity(), PemesananActivity.class);
                        intent.putExtra("batas", batas);
                        intent.putExtra("kode", kode);
                        intent.putExtra("harga", hargapotong);
                        intent.putExtra("diskon", potongan);
                        intent.putExtra("tanggal", tanggal);
                        intent.putExtra("total", tot);
                        startActivityForResult(intent, 30);
                        getActivity().finish();
                    }
                }, 2000);

            }else{
                    showSnackbar(btn_pesan, (int) R.string.eror_pesan, false);

            }
        } catch (Exception e) {
            showSnackbar(btn_pesan, (int) R.string.eror_pesan, false);
        }


    }


    @Override
    public void onClick(View view) {
        showDialogTerm();

    }

    @Subscribe
    public void onEvent(AlertDialogAction event) {
        if (event.action.equals("POSITIVE") && event.type.equals("TERMS")) {
            try {
                postBook();
            } catch (Exception e) {

            }
        }
    }

    private void showDialogTerm() {

        AlertDialogFragment dialogFragment = AlertDialogFragment.newInstance();
        View layoutDialog = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_syarat_ketentuan, null);
        dialogFragment.init(getActivity(), AlertDialogFragment.DIALOG_CUSTOM_VIEW).setView(layoutDialog).setPositiveButton((int) R.string.action_agree).setNegativeButton((int) R.string.action_no).dialogKey("").dialogType("TERMS");
        dialogFragment.show(getFragmentManager(), "dialog");
    }
}
