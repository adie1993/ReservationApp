package app.adie.reservation.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import app.adie.reservation.entity.Sopir;
import app.adie.reservation.JSONParser;
import app.adie.reservation.R;
import app.adie.reservation.entity.Jurusan;
import app.adie.reservation.entity.Sopir;
import app.adie.reservation.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import app.adie.reservation.view.adapter.JurusanAdapter;
import app.adie.reservation.view.adapter.SopirAdapter;
import app.adie.reservation.view.widget.DividerItemDecorationPadding;
import app.adie.reservation.view.widget.LoadingIndicator;

import static app.adie.reservation.R.drawable.divider;
import static app.adie.reservation.entity.Sopir.getInstance;

/**
 * Created by Adie on 15/04/2016.
 */
public class SopirFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private boolean isProgress = false;
    private static final String TAG_SUCCESS = "status";
    private static final String TAG_PRODUCTS = "Jurusan";
    JSONParser jParser = new JSONParser();
    private SopirAdapter mAdapter;
    private StickyRecyclerHeadersDecoration mHeadersDecor;
    private LoadingIndicator mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private FrameLayout mImg;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String url_all_products = "http://10.42.0.1/kraka/api/sopirapi.php";
    JSONArray products = null;
    private ObservableScrollView mScrollView;

    public boolean setLoadingIndicator(LoadingIndicator mLoadingIndicator, boolean isShow) {
        if (isShow) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            return true;
        }
        mLoadingIndicator.setVisibility(View.GONE);
        return false;
    }
    public static SopirFragment newInstance() {
        return new SopirFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_promo,null);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mScrollView = (ObservableScrollView) view.findViewById(R.id.scrollView);

        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView, null);
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.mImg = (FrameLayout) getView().findViewById(R.id.frame);
        this.mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        this.mLoadingIndicator = (LoadingIndicator) getView().findViewById(R.id.loading_indicator);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        this.mAdapter = new SopirAdapter();
        this.mHeadersDecor = new StickyRecyclerHeadersDecoration(this.mAdapter);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecorationPadding(getActivity(), divider));
        this.mRecyclerView.addItemDecoration(this.mHeadersDecor);
        this.mSwipeRefreshLayout.setOnRefreshListener(this);
        this.mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        this.mAdapter.registerAdapterDataObserver(new AdapterDataObserver() {
            public void onChanged() {
                SopirFragment.this.mHeadersDecor.invalidateHeaders();
            }
        });
        setLoadingIndicator(this.mLoadingIndicator, true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSopir(true);
            }
        }, 2000);

    }

    private void initViews(View view) {

    }

    void startAnim(){
        getActivity().findViewById(R.id.loading_indicator).setVisibility(View.VISIBLE);
    }

    void stopAnim(){
        getActivity().findViewById(R.id.loading_indicator).setVisibility(View.GONE);
    }


    private void getSopir(boolean progress) {
        this.mImg.setVisibility(View.GONE);

            if (progress) {
                setLoadingIndicator(this.mLoadingIndicator, true);

            }

            try {
                setLoadingIndicator(this.mLoadingIndicator, false);
                JSONObject json = jParser.getJSONFromUrl(url_all_products);
                Log.d("Execute : ", json.toString());
                int success;
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    this.isProgress = setLoadingIndicator(this.mLoadingIndicator, false);
                    this.mSwipeRefreshLayout.setRefreshing(false);
                    if (this.mAdapter != null) {
                        this.mAdapter.clear();
                    }
                    ArrayList<Sopir> lists = getInstance(getActivity()).render(json);
                    for (int i = 0; i < lists.size(); i++) {
                        Sopir jur = new Sopir();
                        jur.alamat = ((Sopir) lists.get(i)).alamat;
                        jur.nama=((Sopir) lists.get(i)).nama;
                        this.mAdapter.add(i, jur);
                    }

                    this.mRecyclerView.setAdapter(this.mAdapter);

                }else{
                    showSnackbar(getView(), (int) R.string.message_no_network_connection, false);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }


        if (!progress) {
            this.mSwipeRefreshLayout.setRefreshing(false);
        }

    }



    public void onRefresh() {
getSopir(false);
    }



}
