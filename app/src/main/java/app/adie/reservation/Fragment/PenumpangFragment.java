package app.adie.reservation.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import app.adie.reservation.JSONParser;
import app.adie.reservation.activity.ChatTambahActivity;
import app.adie.reservation.activity.KonfirmasiActivity;
import app.adie.reservation.activity.KursiActivity;
import app.adie.reservation.activity.PenumpangTambahActivity;
import app.adie.reservation.R;
import app.adie.reservation.entity.AlertDialogAction;
import app.adie.reservation.entity.Penumpang;
import app.adie.reservation.entity.SqliteHelper;
import app.adie.reservation.view.adapter.PenumpangAdapter;
import app.adie.reservation.view.widget.DividerItemDecorationPadding;
import app.adie.reservation.view.widget.Fab;
import app.adie.reservation.view.widget.LoadingIndicator;

import static app.adie.reservation.R.drawable.divider;

/**
 * Created by Adie on 15/04/2016.
 */
public class PenumpangFragment extends BaseFragment implements OnClickListener {

    private static PenumpangFragment mInstance;

    private MaterialSheetFab materialSheetFab;
    private int statusBarColor;
    private LoadingIndicator mLoadingIndicator;
    private static ArrayList<Penumpang> mCheckList;
    private RecyclerView mRecyclerView;
    private SqliteHelper sqLiteHelper;
    private ArrayList<Penumpang> mPenumpangArrayList;
    private PenumpangAdapter sPenumpangAdapter;
    JSONParser jsonParser = new JSONParser();
    public boolean setLoadingIndicator(LoadingIndicator mLoadingIndicator, boolean isShow) {
        if (isShow) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            return true;
        }
        mLoadingIndicator.setVisibility(View.GONE);
        return false;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    public static PenumpangFragment newInstance(List<Penumpang> mPenumpangs) {
        PenumpangFragment mFragment = new PenumpangFragment();
        mCheckList= (ArrayList<Penumpang>) mPenumpangs;
        return mFragment;
    }
    public static synchronized PenumpangFragment getInstance() {
        PenumpangFragment mFragment = new PenumpangFragment();
        synchronized (PenumpangFragment.class) {
            if (mInstance == null) {
                mInstance = new PenumpangFragment();
            }
            mFragment = mInstance;
        }
        return mFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_penumpang, null);
        initViews(view);


        return view;

    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setLoadingIndicator(this.mLoadingIndicator, true);
        sqLiteHelper = new SqliteHelper(getActivity());
        sqLiteHelper.getAllPenumpangs();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getPenumpang();
            }
        }, 2000);

    }

    private void initViews(View view) {

        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        this.mLoadingIndicator = (LoadingIndicator) view.findViewById(R.id.loading_indicator);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecorationPadding(getActivity(), divider));
        setupFab(view);

        materialSheetFab.showFab();


    }
    private void setupFab(View view) {

        Fab fab = (Fab) view.findViewById(R.id.fab);
        View sheetView = view.findViewById(R.id.fab_sheet);
        View overlay = view.findViewById(R.id.overlay);
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
        view.findViewById(R.id.button_tambah).setOnClickListener(this);

    }


    private int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getActivity().getWindow().getStatusBarColor();
        }
        return 0;
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(color);
        }
    }

    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }
    private void getPenumpang() {
        setLoadingIndicator(mLoadingIndicator, false);
        sqLiteHelper.getAllPenumpangs();
        Log.d("All Products: ", sqLiteHelper.getAllPenumpangs().toString());
        this.mPenumpangArrayList = sqLiteHelper.getAllPenumpangs();
        if (this.mCheckList != null) {
            for (int i = 0; i < this.mCheckList.size(); i++) {
                if (((Penumpang) this.mCheckList.get(i)).isSelected() != ((Penumpang) this.mPenumpangArrayList.get(i)).isSelected()) {
                    ((Penumpang) this.mPenumpangArrayList.get(i)).setIsSelected(((Penumpang) this.mCheckList.get(i)).isSelected());
                }
            }
        }
        this.sPenumpangAdapter = new PenumpangAdapter(getActivity(), this, (ArrayList<Penumpang>) this.mPenumpangArrayList);
        this.mRecyclerView.setAdapter(this.sPenumpangAdapter);
    }


    public void onClick(View view) {


        if (view== view.findViewById(R.id.button_tambah)){

            startActivity(new Intent(getActivity(),PenumpangTambahActivity.class));


            materialSheetFab.hideSheet();
        }
    }

    @Subscribe
    public void onEvent(AlertDialogAction event) {
        if (event.action.equals("POSITIVE") && event.type.equals("DELETE_PENUMPANG")) {
            try {
               sqLiteHelper.hapus_data(event.key);
                this.sPenumpangAdapter.removeItem(event.key);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_done, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && data != null) {
            getActivity();
            if (resultCode == -1) {
                getPenumpang();
            }
        }
    }

}
