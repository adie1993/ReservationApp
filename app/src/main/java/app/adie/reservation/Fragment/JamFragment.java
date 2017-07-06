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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import app.adie.reservation.JSONParser;
import app.adie.reservation.R;
import app.adie.reservation.entity.JamBer;
import app.adie.reservation.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import app.adie.reservation.view.adapter.JamAdapter;
import app.adie.reservation.view.widget.DividerItemDecorationPadding;
import app.adie.reservation.view.widget.LoadingIndicator;

import static app.adie.reservation.R.drawable.divider;
import static app.adie.reservation.entity.JamBer.getInstance;

/**
 * Created by Adie on 15/04/2016.
 */
public class JamFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private boolean isProgress = false;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "JamBer";
    JSONParser jParser = new JSONParser();
    private JamAdapter mAdapter;
    private static String kodeJamBer;
    private static String tanggal;
    private static int jdw;
    private StickyRecyclerHeadersDecoration mHeadersDecor;
    private LoadingIndicator mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    String url;
    JSONArray products = null;

    public boolean setLoadingIndicator(LoadingIndicator mLoadingIndicator, boolean isShow) {
        if (isShow) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            return true;
        }
        mLoadingIndicator.setVisibility(View.GONE);
        return false;
    }
    public static JamFragment newInstance(String kode_tujuan, String tgl) {
        JamFragment mFragment = new JamFragment();
        kodeJamBer= kode_tujuan;
        tanggal = tgl;
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
                getJamBer(true, kodeJamBer, tanggal);
            }
        }, 2000);


    }

    private void initViews(View view) {

        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        this.mLoadingIndicator = (LoadingIndicator) view.findViewById(R.id.loading_indicator);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecorationPadding(getActivity(), divider));
        this.mSwipeRefreshLayout.setOnRefreshListener(this);
        this.mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

    }



    private void getJamBer(boolean progress, String kodeJamBer,String tanggal) {
        url = "http://10.0.2.2/kraka/api/jam.php?"+"kode="+kodeJamBer.toString()+"&tanggal="+tanggal.toString();


            if (progress) {
                setLoadingIndicator(this.mLoadingIndicator, true);

            }
            JSONObject json = jParser.getJSONFromUrl(url);
            try {
                setLoadingIndicator(this.mLoadingIndicator, false);

                String success;

                success = json.getString("success");

                Log.d("All a: ", tanggal.toString());

                if (success.equals("1")) {
                    this.isProgress = setLoadingIndicator(this.mLoadingIndicator, false);
                    this.mSwipeRefreshLayout.setRefreshing(false);

                    ArrayList<JamBer> list = getInstance(getActivity()).render(json);

                    this.mAdapter = new JamAdapter(getActivity(), list);
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
