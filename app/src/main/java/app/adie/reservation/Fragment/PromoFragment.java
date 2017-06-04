package app.adie.reservation.Fragment;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

import org.json.JSONObject;

import java.util.ArrayList;

import app.adie.reservation.JSONParser;
import app.adie.reservation.R;
import app.adie.reservation.entity.PromoTiket;
import app.adie.reservation.view.adapter.PromoAdapter;
import app.adie.reservation.view.widget.DividerItemDecorationPadding;
import app.adie.reservation.view.widget.LoadingIndicator;

/**
 * Created by Adie on 01/06/2016.
 */
public class PromoFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private ObservableScrollView mScrollView;
    private boolean isProgress = false;
    private Adapter mAdapter;
    private LoadingIndicator mLoadingIndicator;
    JSONParser jParser = new JSONParser();
    private String url = "http://10.42.0.1/kraka/api/promo.php";
    private FrameLayout mImg;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static PromoFragment newInstance() {
        return new PromoFragment();
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
        this.mLoadingIndicator = (LoadingIndicator) getView().findViewById(R.id.loading_indicator);
        this.mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecorationPadding(getActivity(), R.drawable.divider));
        this.mSwipeRefreshLayout.setOnRefreshListener(this);
        this.mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        getPromo(true);
    }

    private void getPromo(boolean progress) {
        this.mImg.setVisibility(View.GONE);

        if (progress) {
            setLoadingIndicator(this.mLoadingIndicator, true);

        }

        try {
            setLoadingIndicator(this.mLoadingIndicator, false);
            JSONObject json = jParser.getJSONFromUrl(url);
            Log.d("Execute : ", json.toString());
            String success;
            success = json.getString("status");
            if (success.equals("1")) {
                this.isProgress = setLoadingIndicator(this.mLoadingIndicator, false);
                this.mSwipeRefreshLayout.setRefreshing(false);

                ArrayList<PromoTiket> lists = PromoTiket.getInstance(getActivity()).render(json);

                this.mAdapter = new PromoAdapter(getActivity(), lists);
                this.mRecyclerView.setAdapter(this.mAdapter);

            }else{
                this.mImg.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }


        if (!progress) {
            this.mSwipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void onRefresh() {
    getPromo(false);
    }
}
