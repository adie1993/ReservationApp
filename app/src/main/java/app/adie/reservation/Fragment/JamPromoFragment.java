package app.adie.reservation.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.adie.reservation.activity.CalendarPromoActivity;
import app.adie.reservation.JSONParser;
import app.adie.reservation.R;
import app.adie.reservation.SessionManager;
import app.adie.reservation.entity.JamPromo;
import app.adie.reservation.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import app.adie.reservation.view.adapter.JamPromoAdapter;
import app.adie.reservation.view.widget.DividerItemDecorationPadding;
import app.adie.reservation.view.widget.LoadingIndicator;

import static app.adie.reservation.R.drawable.divider;

/**
 * Created by Adie on 15/04/2016.
 */
public class JamPromoFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private boolean isProgress = false;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "JamBer";
    JSONParser jParser = new JSONParser();
    private JamPromoAdapter mAdapter;
    private static String kodeJamBer,nama_penum;
    private static JamPromoFragment mInstance;
    private static String tanggal,asal,tuju;
    private static int jmlah;
    private JamPromo mJam;
    SessionManager session;
    private StickyRecyclerHeadersDecoration mHeadersDecor;
    private LoadingIndicator mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    String id_user,url,urll,nama,phone,email;
    private  TextView txtpromo;
    JSONArray products = null;



    public boolean setLoadingIndicator(LoadingIndicator mLoadingIndicator, boolean isShow) {
        if (isShow) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            return true;
        }
        mLoadingIndicator.setVisibility(View.GONE);
        return false;
    }
    public static JamPromoFragment newInstance(String kode_tujuan, String tgl,int jml,String namapen,String asl,String tuj) {
        JamPromoFragment mFragment = new JamPromoFragment();
        kodeJamBer= kode_tujuan;
        tanggal = tgl;
        asal = asl;
        tuju = tuj;
        jmlah = jml;
        nama_penum = namapen;
        return mFragment;
    }
    public static synchronized JamPromoFragment getInstancee() {
        JamPromoFragment jamPromoFragment;
        synchronized (JamPromoFragment.class) {
            if (mInstance == null) {
                mInstance = new JamPromoFragment();
            }
            jamPromoFragment = mInstance;
        }
        return jamPromoFragment;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        nama = user.get(SessionManager.KEY_NAME);
        id_user = user.get(SessionManager.KEY_ID);
        email = user.get(SessionManager.KEY_EMAIL);
        phone = user.get(SessionManager.KEY_PHONE);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jurusan, null);
        initViews(view);


        //new TheTask().execute();
        //mAdapter.notifyDataSetChanged();

        return view;

    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setLoadingIndicator(this.mLoadingIndicator, true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getJamBer(true, kodeJamBer, tanggal);
            }
        }, 2000);


    }

    private void initViews(View view) {
        this.mJam = null;
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        this.mLoadingIndicator = (LoadingIndicator) view.findViewById(R.id.loading_indicator);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecorationPadding(getActivity(), divider));
        this.mSwipeRefreshLayout.setOnRefreshListener(this);
        this.mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        this.txtpromo = (TextView) CalendarPromoActivity.activity.findViewById(R.id.txtpromo);
        txtpromo.setText("");
        txtpromo.setHint("Pilih Jam Keberangkatan");
        this.txtpromo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.clock_on, 0, 0, 0);
        CalendarPromoActivity.activity.getSupportActionBar().setTitle("Jadwal Keberangkatan");
    }
    public void changeFragment(JamPromo mJamBer) {
        this.mJam = mJamBer;
        if(jmlah>mJam.kursi){
            showSnackbar(mLoadingIndicator, (int) R.string.eror_jml, false);
        }else{

           return;
        }
    }



    private void getJamBer(boolean progress, String kodeJamBer,String tanggal) {
        url = "http://10.42.0.1/kraka/api/jam.php?"+"kode="+kodeJamBer.toString()+"&tanggal="+tanggal.toString();






            if (progress) {
                setLoadingIndicator(this.mLoadingIndicator, true);

            }
            JSONObject json = jParser.getJSONFromUrl(url);


            try {
                setLoadingIndicator(this.mLoadingIndicator, false);

                String success;

                success = json.getString("success");

                Log.d("All Products: ", json.toString());

                if (success.equals("1")) {
                    this.isProgress = setLoadingIndicator(this.mLoadingIndicator, false);
                    this.mSwipeRefreshLayout.setRefreshing(false);

                    ArrayList<JamPromo> list = JamPromo.getInstance(getActivity()).render(json);

                    this.mAdapter = new JamPromoAdapter(getActivity(), list,asal,tuju,nama_penum,jmlah,nama,email,phone,tanggal,id_user);
                    this.mRecyclerView.setAdapter(this.mAdapter);
                    if(list.isEmpty()){
                        showSnackbar(getView(), (int) R.string.message_no_time, false);
                    }

                } else {

                        showSnackbar(getView(), (int) R.string.message_no_time, false);

                }
            } catch (Exception e) {
                e.printStackTrace();

            }


        if (!progress) {
            this.mSwipeRefreshLayout.setRefreshing(false);
        }

    }




    public void onRefresh() {
getJamBer(false, this.kodeJamBer, this.tanggal);
    }



}
