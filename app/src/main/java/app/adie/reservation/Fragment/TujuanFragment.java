package app.adie.reservation.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import app.adie.reservation.JSONParser;
import app.adie.reservation.R;
import app.adie.reservation.entity.Tujuan;
import app.adie.reservation.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import app.adie.reservation.view.adapter.TujuanAdapter;
import app.adie.reservation.view.widget.DividerItemDecorationPadding;
import app.adie.reservation.view.widget.LoadingIndicator;

import static app.adie.reservation.R.drawable.divider;
import static app.adie.reservation.entity.Tujuan.getInstance;

/**
 * Created by Adie on 15/04/2016.
 */
public class TujuanFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private boolean isProgress = false;
    private static final String TAG_SUCCESS = "status";
    private static final String TAG_PRODUCTS = "tujuan";
    JSONParser jParser = new JSONParser();
    private TujuanAdapter mAdapter;
    private static String kodejurusan;
    private StickyRecyclerHeadersDecoration mHeadersDecor;
    private LoadingIndicator mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String url;
    JSONArray products = null;

    public boolean setLoadingIndicator(LoadingIndicator mLoadingIndicator, boolean isShow) {
        if (isShow) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            return true;
        }
        mLoadingIndicator.setVisibility(View.GONE);
        return false;
    }
    public static TujuanFragment newInstance(String kodeJurusan) {
        TujuanFragment mFragment = new TujuanFragment();

        kodejurusan= kodeJurusan;
        return mFragment;
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
                getTujuan(true, kodejurusan);
            }
        }, 2000);


    }

    private void initViews(View view) {

        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        this.mLoadingIndicator = (LoadingIndicator) view.findViewById(R.id.loading_indicator);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        this.mAdapter = new TujuanAdapter();
        this.mHeadersDecor = new StickyRecyclerHeadersDecoration(this.mAdapter);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecorationPadding(getActivity(), divider));
        this.mRecyclerView.addItemDecoration(this.mHeadersDecor);
        this.mSwipeRefreshLayout.setOnRefreshListener(this);
        this.mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        this.mAdapter.registerAdapterDataObserver(new AdapterDataObserver() {
            public void onChanged() {
                TujuanFragment.this.mHeadersDecor.invalidateHeaders();
            }
        });
    }

    void startAnim(){
        getActivity().findViewById(R.id.loading_indicator).setVisibility(View.VISIBLE);
    }

    void stopAnim(){
        getActivity().findViewById(R.id.loading_indicator).setVisibility(View.GONE);
    }


    private void getTujuan(boolean progress, String kodejurusan) {


            if (progress) {
                setLoadingIndicator(this.mLoadingIndicator, true);

            }

            try {
                setLoadingIndicator(this.mLoadingIndicator, false);
                url = "http://10.0.2.2/kraka/api/tujuan.php?"+"kode_asal="+kodejurusan;
                JSONObject json = jParser.getJSONFromUrl(url);
                // Check your log cat for JSON reponse
                Log.d("All Products: ", kodejurusan);
                int success;
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    this.isProgress = setLoadingIndicator(this.mLoadingIndicator, false);
                    this.mSwipeRefreshLayout.setRefreshing(false);
                    if (this.mAdapter != null) {
                        this.mAdapter.clear();
                    }
                    ArrayList<Tujuan> list = getInstance(getActivity()).render(json);
                    for (int i = 0; i < list.size(); i++) {
                        Tujuan tuj = new Tujuan();
                        tuj.kota_tujuan = ((Tujuan) list.get(i)).kota_tujuan;
                        tuj.kode_asal = ((Tujuan) list.get(i)).kode_asal;
                        tuj.tujuan = ((Tujuan) list.get(i)).tujuan;
                        tuj.kode = ((Tujuan) list.get(i)).kode;
                        tuj.kode_tujuan = ((Tujuan) list.get(i)).kode_tujuan;
                        this.mAdapter.add(i, tuj);
                    }

                    this.mRecyclerView.setAdapter(this.mAdapter);

                }else{
                    showSnackbar(getView(), (int) R.string.drawer_close, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }



        if (!progress) {
            this.mSwipeRefreshLayout.setRefreshing(false);
        }

    }



    public void onRefresh() {
getTujuan(false, this.kodejurusan);
    }



}
