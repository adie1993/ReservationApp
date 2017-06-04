package app.adie.reservation.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Map.Entry;
import java.util.TreeMap;

import app.adie.reservation.JSONParser;
import app.adie.reservation.activity.KonfirmasiActivity;
import app.adie.reservation.activity.KursiActivity;
import app.adie.reservation.R;
import app.adie.reservation.entity.JamBer;
import app.adie.reservation.entity.Kursi;
import app.adie.reservation.entity.KursiDetil;
import app.adie.reservation.utils.StringUtils;
import app.adie.reservation.view.adapter.KursiAdapter;

public class KursiFragment extends BaseFragment implements OnItemClickListener, OnRefreshListener {
    private boolean isProgress = false;
    private GridView mGridView;
    private TextView mHarga;
    private Kursi mKursi;
    private TreeMap<String, Integer> mKursiHarga;
    private TreeMap<String, String> mKursiPilihan;
    private TreeMap<String, String> mKursiReserved;
    private TextView mPrimary;
    JSONParser jParser = new JSONParser();
    String url,urll;
    private static String tanggal,berangkat,tujuan,nama_pen,nam,emai,phon,jam,batas;
    private static int jadwal;
    private static int id;
    private static int jml_penumpang;
    private static int hargakursi,potongan;
    private JamBer mJam;
    private RelativeLayout mResumePrice;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTotal;

    public static KursiFragment newInstance(String tgl,String brgkt,String tuju,String nama_penum,String nama,String email,String phone,String ja,String ba, int id_jadwal, int harga,int id_armada,int jmlh,int ptong) {
        KursiFragment mFragment = new KursiFragment();
        tanggal = tgl;
        berangkat = brgkt;
        tujuan = tuju;
        nama_pen = nama_penum;
        nam = nama;
        emai = email;
        phon = phone;
        id= id_armada;
        jam = ja;
        batas = ba;
        jadwal = id_jadwal;
        jml_penumpang = jmlh;
        hargakursi = harga;
        potongan = ptong;
        return mFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        KursiActivity.activity.getSupportActionBar().setTitle("Pilih Kursi");
        this.mKursiHarga = new TreeMap();
        this.mKursiPilihan = new TreeMap();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kursi, null);
        initViews(view);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getKursiReservasi(true, tanggal, jadwal);
    }

    private void initViews(View view) {
        this.mResumePrice = (RelativeLayout) view.findViewById(R.id.resume_price);
        this.mHarga = (TextView) view.findViewById(R.id.harga);
        this.mTotal = (TextView) view.findViewById(R.id.total);
        this.mPrimary = (TextView) view.findViewById(R.id.primary);
        this.mGridView = (GridView) view.findViewById(R.id.gridview);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        this.mSwipeRefreshLayout.setOnRefreshListener(this);
        this.mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }



    private void getKursiReservasi(boolean refreshing, String tanggal, int jadwal) {
       url = "http://10.42.0.1/kraka/api/tes.php?"+"jadwal="+String.valueOf(jadwal)+"&tgl="+tanggal.toString();
        urll = "http://10.42.0.1/kraka/api/kursi.php?"+"id="+String.valueOf(id);

            JSONObject json = jParser.getJSONFromUrl(url);
            try {

                String success;
                success = json.getString("kursi");
                Log.d("All Products: ", success.toString());
                if (success!=null) {
                    this.mSwipeRefreshLayout.setRefreshing(false);
                    this.mKursiReserved = Kursi.getInstance(getActivity()).renderReserved(json);
                    Log.d("All Products: ", mKursiReserved.toString());

                    JSONObject jsson = jParser.getJSONFromUrl(urll);
                    String sukses;
                    sukses = jsson.getString("success");
                    Log.d("All Products: ", jsson.toString());
                    if (sukses.equals("1")){
                        this.mKursi = Kursi.getInstance(getActivity()).render(jsson, this.mKursiReserved);
                        this.mGridView.setNumColumns(this.mKursi.cols);
                        this.mGridView.setOnItemClickListener(this);
                        this.mGridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                        this.mGridView.setAdapter(new KursiAdapter(getActivity(), this.mKursi.kursiList));
                        for (int i = 0; i < this.mKursi.kursiList.size(); i++) {
                            if (this.mKursiPilihan.containsKey(((KursiDetil) this.mKursi.kursiList.get(i)).no)) {
                                this.mGridView.setItemChecked(i, true);
                            }
                        }
                        this.mTotal.setText(getResources().getString(R.string.total) + " " + StringUtils.toRupiahFormat(String.valueOf(totalHarga())));
                    }

                }else{
                    JSONObject jsson = jParser.getJSONFromUrl(urll);
                    String sukses;
                    sukses = jsson.getString("success");
                    Log.d("All Products: ", jsson.toString());
                    if (sukses.equals("1")){
                        this.mKursi = Kursi.getInstance(getActivity()).render(jsson, this.mKursiReserved);
                        this.mGridView.setNumColumns(this.mKursi.cols);
                        this.mGridView.setOnItemClickListener(this);
                        this.mGridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                        this.mGridView.setAdapter(new KursiAdapter(getActivity(), this.mKursi.kursiList));
                        for (int i = 0; i < this.mKursi.kursiList.size(); i++) {
                            if (this.mKursiPilihan.containsKey(((KursiDetil) this.mKursi.kursiList.get(i)).no)) {
                                this.mGridView.setItemChecked(i, true);
                            }
                        }
                        this.mTotal.setText(getResources().getString(R.string.total) + " " + StringUtils.toRupiahFormat(String.valueOf(totalHarga())));
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();

            }




    }



    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        KursiDetil seat = (KursiDetil) this.mKursi.kursiList.get(position);
        //Debug.i("Seat type: " + seat.type);
        if (this.mKursiPilihan.size() >= jml_penumpang && !this.mKursiPilihan.containsKey(seat.no)) {
            this.mGridView.setItemChecked(position, false);
            showSnackbar(getView(), (int) R.string.message_max_pessenger, false);

        }else if (seat.type.equals("S") || seat.type.equals("K") || seat.type.equals("B")) {
            this.mGridView.setItemChecked(position, false);
        } else if (this.mKursiPilihan.containsKey(seat.no)) {
            this.mKursiPilihan.remove(seat.no);
            this.mKursiHarga.remove(seat.no);

        } else {
            this.mKursiPilihan.put(seat.no, seat.no);
            this.mGridView.setItemChecked(position, true);
            int hargapotong;
            hargapotong = hargakursi - potongan;
            setHargaKursi(hargapotong);
            this.mKursiHarga.put(seat.no, hargapotong);
        }
        this.mTotal.setText(getResources().getString(R.string.total) + " " + StringUtils.toRupiahFormat(String.valueOf(totalHarga())));
    }

    public void onRefresh() {
        getKursiReservasi(false, tanggal, jadwal);
    }
@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_done, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if (this.mKursiPilihan.size() >= this.jml_penumpang) {
                   String seats = "";
                    for (String key : this.mKursiPilihan.keySet()) {
                       seats = seats.equals("") ? key : seats + ", " + key;
                    }
                    Intent intent;
                    intent = new Intent(getActivity(), KonfirmasiActivity.class);
                    intent.putExtra("id_jadwal", jadwal);
                    intent.putExtra("jam", jam);
                    intent.putExtra("batas", batas);
                    intent.putExtra("keberangkatan", berangkat);
                    intent.putExtra("tujuan", tujuan);
                    intent.putExtra("nama_penumpang", nama_pen);
                    intent.putExtra("jml_penumpang",jml_penumpang);
                    intent.putExtra("nama",nam);
                    intent.putExtra("email",emai);
                    intent.putExtra("phone",phon);
                    intent.putExtra("id_armada", id);
                    intent.putExtra("harga", hargakursi);
                    intent.putExtra("potong", potongan);
                    intent.putExtra("tanggal", tanggal);
                    intent.putExtra("seats", seats);
                    intent.putExtra("total",String.valueOf(totalHarga()));
                    startActivityForResult(intent, 30);
                }else {
                    showSnackbar(getView(), (int) R.string.message_passenger_less, false);
                }break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setHargaKursi(int harga) {
        this.mHarga.setText(getResources().getString(R.string.price_seat) + " " + StringUtils.toRupiahFormat(String.valueOf(harga)));
    }

    private int totalHarga() {
        int total_harga = 0;
        if (this.mKursiPilihan.size() <= 0) {
            this.mResumePrice.setVisibility(View.GONE);
            this.mResumePrice.animate().translationY(0.0f);
        } else {
            this.mResumePrice.setVisibility(View.VISIBLE);
            this.mResumePrice.animate().translationY(0.0f);
            for (Entry<String, Integer> entry : this.mKursiHarga.entrySet()) {
                total_harga = total_harga == 0 ? ((Integer) entry.getValue()).intValue() : total_harga + ((Integer) entry.getValue()).intValue();
            }
        }
        return total_harga;
    }
}
