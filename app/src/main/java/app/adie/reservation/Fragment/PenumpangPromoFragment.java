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
import android.widget.TextView;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.adie.reservation.activity.CalendarPromoActivity;
import app.adie.reservation.JSONParser;
import app.adie.reservation.activity.PenumpangTambahActivity;
import app.adie.reservation.R;
import app.adie.reservation.entity.AlertDialogAction;
import app.adie.reservation.entity.Penumpang;
import app.adie.reservation.entity.PromoPesan;
import app.adie.reservation.entity.SqliteHelper;
import app.adie.reservation.utils.DateUtils;
import app.adie.reservation.view.adapter.PenumpangPromoAdapter;
import app.adie.reservation.view.widget.DividerItemDecorationPadding;
import app.adie.reservation.view.widget.Fab;
import app.adie.reservation.view.widget.LoadingIndicator;

import static app.adie.reservation.R.drawable.divider;

/**
 * Created by Adie on 15/04/2016.
 */
public class PenumpangPromoFragment extends BaseFragment implements OnClickListener {

    private static PenumpangPromoFragment mInstance;

    private LoadingIndicator mLoadingIndicator;
    private static ArrayList<Penumpang> mCheckList;
    private List<Penumpang> mPenumpangs;
    private RecyclerView mRecyclerView;
    private int jmlPenumpang = 0;
    private static String tglpil,kode,asal,tuju;
    private static Date date;
    private SqliteHelper sqLiteHelper;

    private MaterialSheetFab materialSheetFab;
    private int statusBarColor;
    private PromoPesan mPromo;
    private TextView mTextPenumpang;
    private ArrayList<Penumpang> mPenumpangArrayList;
    private PenumpangPromoAdapter sPenumpangAdapter;
    JSONParser jsonParser = new JSONParser();
    public boolean setLoadingIndicator(LoadingIndicator mLoadingIndicator, boolean isShow) {
        if (isShow) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            return true;
        }
        mLoadingIndicator.setVisibility(View.GONE);
        return false;
    }
    public static PenumpangPromoFragment newInstance(Date tglpilih,String kod,String asl,String tuj) {
        PenumpangPromoFragment mFragment = new PenumpangPromoFragment();
        date=tglpilih;
        asal =asl;
        tuju = tuj;
        kode =kod;

        return mFragment;
    }
    public static synchronized PenumpangPromoFragment getInstance() {
        PenumpangPromoFragment mFragment = new PenumpangPromoFragment();
        synchronized (PenumpangPromoFragment.class) {
            if (mInstance == null) {
                mInstance = new PenumpangPromoFragment();
            }
            mFragment = mInstance;
        }
        return mFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_penumpang, null);
        initViews(view);
        this.mTextPenumpang.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_penumpang, 0, 0, 0);
        this.mTextPenumpang.setHint("Pilih Penumpang");
        CalendarPromoActivity.activity.getSupportActionBar().setTitle("Penumpang");
        return view;

    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_done, menu);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if (this.jmlPenumpang == 0) {
                    showSnackbar(this.mTextPenumpang, (int) R.string.message_no_passenger_selected, false);
                    break;
                }
                String namaPenumpang=mTextPenumpang.getText().toString();
                String tglupil = DateUtils.getStringDate(date);
                changeFragment(R.id.containerVieww, JamPromoFragment.newInstance(kode,tglupil,jmlPenumpang,namaPenumpang,asal,tuju));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(View view) {
        mCheckList= (ArrayList<Penumpang>) mPenumpangs;

        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        this.mLoadingIndicator = (LoadingIndicator) view.findViewById(R.id.loading_indicator);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mRecyclerView.addItemDecoration(new DividerItemDecorationPadding(getActivity(), divider));
        this.mTextPenumpang = (TextView)CalendarPromoActivity.activity.findViewById(R.id.txtpromo);
        setupFab(view);

        materialSheetFab.showFab();

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
        this.sPenumpangAdapter = new PenumpangPromoAdapter(getActivity(), this, (ArrayList<Penumpang>) this.mPenumpangArrayList);
        this.mRecyclerView.setAdapter(this.sPenumpangAdapter);
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

    public void getPassengerFromFragmentContainer(String text, int count, ArrayList<Penumpang> penumpangList) {
        this.mTextPenumpang.setText(text);
        switch (count) {
            case 1:
                this.mTextPenumpang.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_penumpang, 0, 0, 0);
                this.jmlPenumpang = 1;
                break;
            case 2:
                this.mTextPenumpang.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_penumpang2, 0, 0, 0);
                this.jmlPenumpang = 2;
                break;
            case 3:
                this.mTextPenumpang.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_penumpang3, 0, 0, 0);
                this.jmlPenumpang = 3;
                break;
            default:
                this.jmlPenumpang = 0;
                break;
        }
        this.mPenumpangs = penumpangList;
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
