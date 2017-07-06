package app.adie.reservation.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.adie.reservation.JSONParser;
import app.adie.reservation.R;
import app.adie.reservation.SessionManager;
import app.adie.reservation.entity.Cancel;
import app.adie.reservation.entity.DisplayableItem;
import app.adie.reservation.entity.Habis;
import app.adie.reservation.entity.InProgress;
import app.adie.reservation.entity.Paid;
import app.adie.reservation.entity.Pesan;
import app.adie.reservation.entity.Rejected;
import app.adie.reservation.entity.Unpaid;
import app.adie.reservation.view.adapter.HistoryAdapter;
import app.adie.reservation.view.widget.DividerItemDecorationPadding;
import app.adie.reservation.view.widget.LoadingIndicator;

;

/**
 * Created by Adie on 18/05/2016.
 */
public class HistoryFragment extends BaseFragment implements OnRefreshListener {
    private ObservableScrollView mScrollView;
    private boolean isProgress = false;
    private Adapter mAdapter;
    private LoadingIndicator mLoadingIndicator;
    private Menu mMenu;
    SessionManager session;
    private FrameLayout mImg;
    private ArrayList<Paid> mPaids = new ArrayList();
    private ArrayList<InProgress> mInProg = new ArrayList();
    private ArrayList<Habis> mHabis = new ArrayList();
    private ArrayList<Cancel> mCancel = new ArrayList();
    private ArrayList<Rejected> mReject = new ArrayList();
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<Unpaid> mUnpaid = new ArrayList();
    private String url,id,urll,urlll,urllll,canc,rej;
    JSONParser jParser = new JSONParser();
    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history,container,false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mScrollView = (ObservableScrollView) view.findViewById(R.id.scrollView);

        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView, null);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mImg = (FrameLayout) getView().findViewById(R.id.frame);
        this.mLoadingIndicator = (LoadingIndicator) getView().findViewById(R.id.loading_indicator);
        this.mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        this.mSwipeRefreshLayout.setOnRefreshListener(this);
        this.mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        id = user.get(SessionManager.KEY_ID);

        getBooking(true, id);
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_filter, menu);
        this.mMenu = menu;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        ArrayList<DisplayableItem> lists = new ArrayList();
        int i;
        switch (item.getItemId()) {
            case R.id.action_all:
                getBooking(true, id);
                this.mSwipeRefreshLayout.setRefreshing(false);
                if (this.mRecyclerView != null) {
                    this.mRecyclerView.removeAllViews();
                }
                for (i = 0; i < this.mUnpaid.size(); i++) {
                    lists.add(getUnpaid(i));
                }
                for (i = 0; i < this.mPaids.size(); i++) {
                    lists.add(getPaid(i));
                }
                for (i = 0; i < this.mInProg.size(); i++) {
                    lists.add(getInProg(i));
                }
                for (i = 0; i < this.mCancel.size(); i++) {
                    lists.add(getCancel(i));
                }
                for (i = 0; i < this.mHabis.size(); i++) {
                    lists.add(getHabis(i));
                }
                for (i = 0; i < this.mReject.size(); i++) {
                    lists.add(getReject(i));
                }

                if (lists.size() == 0) {
                    this.mImg.setVisibility(View.VISIBLE);
                } else {
                    this.mImg.setVisibility(View.GONE);
                }
                setRecyclerView(lists);
                checkable(item);
                break;
            case R.id.action_paid:
                getBooking(true, id);
                this.mSwipeRefreshLayout.setRefreshing(false);
                if (this.mRecyclerView != null) {
                    this.mRecyclerView.removeAllViews();
                }
                for (i = 0; i < this.mPaids.size(); i++) {
                    lists.add(getPaid(i));
                }
                if (lists.size() == 0) {
                    this.mImg.setVisibility(View.VISIBLE);
                } else {
                    this.mImg.setVisibility(View.GONE);
                }
                setRecyclerView(lists);
                checkable(item);
                break;
            case R.id.action_unpaid:
                getBooking(true, id);
                this.mSwipeRefreshLayout.setRefreshing(false);
                if (this.mRecyclerView != null) {
                    this.mRecyclerView.removeAllViews();
                }
                for (i = 0; i < this.mUnpaid.size(); i++) {
                    lists.add(getUnpaid(i));
                }
                if (lists.size() == 0) {
                this.mImg.setVisibility(View.VISIBLE);
                } else {
                this.mImg.setVisibility(View.GONE);
                }
                setRecyclerView(lists);
                checkable(item);
                break;
            case R.id.action_progress:
                getBooking(true, id);
                this.mSwipeRefreshLayout.setRefreshing(false);
                if (this.mRecyclerView != null) {
                    this.mRecyclerView.removeAllViews();
                }
                for (i = 0; i < this.mInProg.size(); i++) {
                    lists.add(getInProg(i));
                }
                if (lists.size() == 0) {
                    this.mImg.setVisibility(View.VISIBLE);
                } else {
                    this.mImg.setVisibility(View.GONE);
                }
                setRecyclerView(lists);
                checkable(item);
                break;
            case R.id.action_cancel:
                getBooking(true, id);
                this.mSwipeRefreshLayout.setRefreshing(false);
                if (this.mRecyclerView != null) {
                    this.mRecyclerView.removeAllViews();
                }
                for (i = 0; i < this.mCancel.size(); i++) {
                    lists.add(getCancel(i));
                }
                if (lists.size() == 0) {
                    this.mImg.setVisibility(View.VISIBLE);
                } else {
                    this.mImg.setVisibility(View.GONE);
                }
                setRecyclerView(lists);
                checkable(item);
                break;
            case R.id.action_habis:
                getBooking(true, id);
                this.mSwipeRefreshLayout.setRefreshing(false);
                if (this.mRecyclerView != null) {
                    this.mRecyclerView.removeAllViews();
                }
                for (i = 0; i < this.mHabis.size(); i++) {
                    lists.add(getHabis(i));
                }
                if (lists.size() == 0) {
                    this.mImg.setVisibility(View.VISIBLE);
                } else {
                    this.mImg.setVisibility(View.GONE);
                }
                setRecyclerView(lists);
                checkable(item);
                break;
            case R.id.action_reject:
                getBooking(true, id);
                this.mSwipeRefreshLayout.setRefreshing(false);
                if (this.mRecyclerView != null) {
                    this.mRecyclerView.removeAllViews();
                }
                for (i = 0; i < this.mReject.size(); i++) {
                    lists.add(getReject(i));
                }
                if (lists.size() == 0) {
                    this.mImg.setVisibility(View.VISIBLE);
                } else {
                    this.mImg.setVisibility(View.GONE);
                }
                setRecyclerView(lists);
                checkable(item);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void getBooking(boolean progress,String id) {


        if (progress) {
            setLoadingIndicator(this.mLoadingIndicator, true);

        }

        try {
            setLoadingIndicator(this.mLoadingIndicator, false);
            url = "http://10.0.2.2/kraka/api/unpaid.php?"+"id_user="+id;
            JSONObject json = jParser.getJSONFromUrl(url);


            urll = "http://10.0.2.2/kraka/api/paid.php?"+"id_user="+id;
            JSONObject jsson = jParser.getJSONFromUrl(urll);


            urlll = "http://10.0.2.2/kraka/api/inprogress.php?"+"id_user="+id;
            JSONObject jssson = jParser.getJSONFromUrl(urlll);


            urllll = "http://10.0.2.2/kraka/api/habis.php?"+"id_user="+id;
            JSONObject jsssson = jParser.getJSONFromUrl(urllll);


            canc = "http://10.0.2.2/kraka/api/cancel.php?"+"id_user="+id;
            JSONObject cance = jParser.getJSONFromUrl(canc);



            rej = "http://10.0.2.2/kraka/api/dibatalkan.php?"+"id_user="+id;
            JSONObject reject = jParser.getJSONFromUrl(rej);
            String success;
            success = reject.getString("status");
            Log.d("Pending: ", success);


                ArrayList<DisplayableItem> lists = new ArrayList();
                HistoryFragment.this.mUnpaid = Pesan.getInstance(HistoryFragment.this.getActivity()).renderUnpaid(json);
                for (int i = 0; i < HistoryFragment.this.mUnpaid.size(); i++) {
                    lists.add(HistoryFragment.this.getUnpaid(i));
                }



                HistoryFragment.this.mPaids = Pesan.getInstance(HistoryFragment.this.getActivity()).renderPaid(jsson);
                for (int i = 0; i < HistoryFragment.this.mPaids.size(); i++) {
                    lists.add(HistoryFragment.this.getPaid(i));
                }


                HistoryFragment.this.mInProg = Pesan.getInstance(HistoryFragment.this.getActivity()).renderInProgress(jssson);
                for (int i = 0; i < HistoryFragment.this.mInProg.size(); i++) {
                    lists.add(HistoryFragment.this.getInProg(i));
                }

                HistoryFragment.this.mCancel = Pesan.getInstance(HistoryFragment.this.getActivity()).renderCancel(cance);
                for (int i = 0; i < HistoryFragment.this.mCancel.size(); i++) {
                lists.add(HistoryFragment.this.getCancel(i));
                }

                HistoryFragment.this.mHabis = Pesan.getInstance(HistoryFragment.this.getActivity()).renderHabis(jsssson);
                for (int i = 0; i < HistoryFragment.this.mHabis.size(); i++) {
                lists.add(HistoryFragment.this.getHabis(i));
                }

                HistoryFragment.this.mReject = Pesan.getInstance(HistoryFragment.this.getActivity()).renderReject(reject);
                for (int i = 0; i < HistoryFragment.this.mReject.size(); i++) {
                lists.add(HistoryFragment.this.getReject(i));
                }



                if (lists.size() == 0) {
                    HistoryFragment.this.mImg.setVisibility(View.VISIBLE);
                } else {
                    HistoryFragment.this.mImg.setVisibility(View.GONE);
                }
                HistoryFragment.this.setRecyclerView(lists);

            {

            }
        } catch (Exception e) {
            e.printStackTrace();

        }



        if (!progress) {
            this.mSwipeRefreshLayout.setRefreshing(false);
        }

    }

    private void checkable(MenuItem item) {
        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }
    }

    private Unpaid getUnpaid(int index) {
        return (Unpaid) this.mUnpaid.get(index);
    }
    private Paid getPaid(int index) {
        return (Paid) this.mPaids.get(index);
    }
    private InProgress getInProg(int index) {
        return (InProgress) this.mInProg.get(index);
    }
    private Cancel getCancel(int index) {
        return (Cancel) this.mCancel.get(index);
    }
    private Habis getHabis(int index) {
        return (Habis) this.mHabis.get(index);
    }
    private Rejected getReject(int index) {
        return (Rejected) this.mReject.get(index);
    }


    private void setRecyclerView(ArrayList<DisplayableItem> lists) {
        this.mAdapter = new HistoryAdapter(getActivity(), lists);
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    @Override
    public void onRefresh() {
        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        id = user.get(SessionManager.KEY_ID);
        getBooking(false,id);
    }
}
