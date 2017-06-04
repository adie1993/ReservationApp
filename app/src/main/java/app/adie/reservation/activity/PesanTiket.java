package app.adie.reservation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import app.adie.reservation.BuildConfig;
import app.adie.reservation.CustomAnimator;
import app.adie.reservation.Fragment.BaseFragment;
import app.adie.reservation.Fragment.CalendarLandingFragment;
import app.adie.reservation.Fragment.JamFragment;
import app.adie.reservation.Fragment.JurusanFragment;
import app.adie.reservation.Fragment.PenumpangFragment;
import app.adie.reservation.Fragment.TujuanFragment;
import app.adie.reservation.JSONParser;
import app.adie.reservation.R;
import app.adie.reservation.SessionManager;
import app.adie.reservation.entity.JamBer;
import app.adie.reservation.entity.Jurusan;
import app.adie.reservation.entity.Penumpang;
import app.adie.reservation.entity.Tujuan;
import app.adie.reservation.utils.DateUtils;


public class PesanTiket extends BaseFragment implements OnClickListener,OnTouchListener {
    private static PesanTiket mInstance;
    private ScrollView mScrollView;
    private RelativeLayout mMainContainer;
    private FrameLayout mEditModeContainer;
    private int jmlPenumpang;
    private FrameLayout mEditFragmentContainer;
    private FrameLayout mEditModeContainertwo;
    private FrameLayout mEditFragmenttwo;
    private LinearLayout mFirstGroup, mSecondGroup, mThirdGroup;
    private boolean isAniamted;
    private Button mSearch;
    private View mFirstSpacer;
    private View mSecondSpacer;
    private View mThirdSpacer;
    private ActionMode mActionMode;
    private int mHalfHeight;
    private CustomAnimator animator;
    private TextView mDari;
    private TextView mTujuan;
    private TextView mDateFrom;
    private TextView mDateTo;
    private static String url = "http://10.42.0.1/kraka/api/cekuser.php";
    private TextView mFinalVisibleView;
    private Register regis = new Register();
    private boolean isActionMode = false;
    SessionManager session;
    private Jurusan mJurusan;
    private JamBer mJam;
    private Tujuan gTujuan;
    JSONParser jParser = new JSONParser();
    private TextView mPenumpang;
    private List<Penumpang> mPenumpangs;
    private Date TglBrgkt;
    private TextView mTggl;
    private long minDate;
    private long maxDate;
    private  TextView jam;
    private ObservableScrollView mScroll;
    String email, nama, phone,wak,id_user;
    public static PesanTiket newInstance() {
        mInstance = new PesanTiket();
        return mInstance;
    }


    public static synchronized PesanTiket getInstance() {
        PesanTiket pesanTiket;
        synchronized (PesanTiket.class) {
            if (mInstance == null) {
                mInstance = new PesanTiket();
            }
            pesanTiket = mInstance;
        }
        return pesanTiket;
    }





    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.animate_fragmet,null);

        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                mEditModeContainer.setTranslationY(view.getHeight());
                mEditModeContainer.setAlpha(0f);
                animator.setEditModeHalfHeight(view.getHeight());
                animator.setDuration(500);
            }
        });
        MainActivity.activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MainActivity.mDrawerToggle.syncState();
        initViews(view);

        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mScroll = (ObservableScrollView) view.findViewById(R.id.scrollView);

        MaterialViewPagerHelper.registerScrollView(getActivity(), mScroll, null);
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        session = new SessionManager(getActivity().getApplicationContext());

        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        id_user = user.get(SessionManager.KEY_ID);
        phone = user.get(SessionManager.KEY_PHONE);
        email = user.get(SessionManager.KEY_EMAIL);
        nama = user.get(SessionManager.KEY_NAME);
    }

    private void initViews(View view) {
        mMainContainer = (RelativeLayout) view.findViewById(R.id.main_container);

        animator = new CustomAnimator();
        mDari = (TextView) view.findViewById(R.id.textpilih);
        mFirstSpacer = view.findViewById(R.id.first_spacer);
        mSecondSpacer = view.findViewById(R.id.second_spacer);
        mThirdSpacer = view.findViewById(R.id.third_spacer);
        this.mDari.setOnClickListener(this);
        this.mJurusan = null;
        this.gTujuan = null;
        mSearch = (Button) view.findViewById(R.id.button_search);
        mSearch.setOnClickListener(this);
        Date date = new Date();
        long dateto = 2685584;
        wak = DateUtils.getDateNow(date);
        minDate = DateUtils.dateToLong(date);
        maxDate = dateto+minDate;
        Log.e("error", "val=" + wak );
        mTujuan = (TextView) view.findViewById(R.id.txttujuan);
        this.mTujuan.setOnClickListener(this);
        mFirstGroup = (LinearLayout) view.findViewById(R.id.first_group_container);
        mSecondGroup = (LinearLayout) view.findViewById(R.id.second_group_container);
        mThirdGroup = (LinearLayout) view.findViewById(R.id.third_group_container);
        this.mPenumpang = (TextView) view.findViewById(R.id.txtpenumpang);
        this.mPenumpang.setOnClickListener(this);
        jam = (TextView) view.findViewById(R.id.txtjam);
        this.jam.setOnClickListener(this);
        this.mScrollView = (ScrollView)view.findViewById(R.id.normal_mode_container);

        mEditModeContainer = (FrameLayout) view.findViewById(R.id.edit_mode_container);
        mEditFragmentContainer = (FrameLayout) view.findViewById(R.id.edit_mode_fragment_container);

        mTggl = (TextView)view.findViewById(R.id.txttanggal);
        this.mTggl.setOnClickListener(this);




    }



    public void onResume() {
        super.onResume();
        setDate();
        checkStatusList();
    }
    public void onPause() {
        super.onPause();
    }
    public void popBackStack() {
        this.mScrollView.setOnTouchListener(null);
       // TabFragment.tabLayout.setVisibility(View.VISIBLE);
        if (this.isActionMode) {
            onDestroyActionMode(this.mActionMode);
        }
        getFragmentManager().popBackStack();
    }
    private void setDate() {

        if (this.TglBrgkt == null) {
            this.TglBrgkt = DateUtils.longToDate(minDate);
        }
        this.mTggl.setText(DateUtils.getDateFormatCard(this.TglBrgkt));
    }
    public void getJurusanFromFragmentContainer(Jurusan jurusan) {
        mTujuan.setText(BuildConfig.FLAVOR);
        mJurusan = jurusan;
        mDari.setText(jurusan.nama);
        MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(true);
        popBackStack();
    }
    public void getTujuanFromFragmentContainer(Tujuan tujuan) {

        gTujuan = tujuan;
        mTujuan.setText(tujuan.tujuan);
        MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(true);
        mJam = null;
        jam.setText("");
        popBackStack();
    }
    public void getDateFromFragmentContainer(Date date) {

        TglBrgkt = date;
        mTggl.setText(DateUtils.getDateFormatCard(date));
        MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(true);
        jam.setText("");
        popBackStack();
    }
    public void getJamFromFragmentContainer(JamBer mJamBer) {
        this.mJam = mJamBer;
        jam.setText("Pukul " + mJamBer.jam);
        MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(true);
        popBackStack();
    }
    public void getPassengerFromFragmentContainer(String text, int count, ArrayList<Penumpang> penumpangList) {
        this.mPenumpang.setText(text);
        switch (count) {
            case 1:
                this.mPenumpang.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_penumpang, 0, 0, 0);
                this.jmlPenumpang = 1;
                break;
            case 2:
                this.mPenumpang.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_penumpang2, 0, 0, 0);
                this.jmlPenumpang = 2;
                break;
            case 3:
                this.mPenumpang.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_penumpang3, 0, 0, 0);
                this.jmlPenumpang = 3;
                break;
            default:
                this.jmlPenumpang = 0;
                break;
        }
        this.mPenumpangs = penumpangList;
    }




    private void checkSelected(TextView tv, boolean isActive) {
        tv.setEnabled(isActive);
    }
    private void startAnimation(TextView tv, int group) {
        switch (group) {
            case 1 :
                this.animator.setAnimatorViews(this.mMainContainer, tv, this.mFirstGroup, Arrays.asList(new View[]{ this.mFirstSpacer,this.mSecondGroup,this.mSecondSpacer,this.mThirdGroup, this.mThirdSpacer,this.mSearch}),null,this.mEditModeContainer, Arrays.asList(new View[0]));
                break;
            case 2 :
                this.animator.setAnimatorViews(this.mMainContainer, tv, this.mFirstGroup, Arrays.asList(new View[]{this.mFirstSpacer,this.mSecondGroup,this.mSecondSpacer,  this.mThirdGroup, this.mThirdSpacer, this.mSearch}),null, this.mEditModeContainer, Arrays.asList(new View[0]));
                break;
            case 3 :
                this.animator.setAnimatorViews(this.mMainContainer, tv, this.mSecondGroup, Arrays.asList(new View[]{this.mThirdGroup,this.mThirdSpacer,this.mSearch}),this.mSecondSpacer, this.mEditModeContainer, Arrays.asList(new View[]{this.mFirstGroup, this.mFirstSpacer}));
                break;
            case 4 :
                this.animator.setAnimatorViews(this.mMainContainer, tv, this.mSecondGroup, Arrays.asList(new View[]{this.mThirdGroup,this.mThirdSpacer,this.mSearch}),this.mSecondSpacer, this.mEditModeContainer, Arrays.asList(new View[]{this.mFirstGroup, this.mFirstSpacer}));
                break;
            case 5 :
                this.animator.setAnimatorViews(this.mMainContainer, tv, this.mThirdGroup, Arrays.asList(new View[]{this.mSearch}),this.mThirdSpacer, this.mEditModeContainer, Arrays.asList(new View[]{this.mFirstGroup, this.mFirstSpacer,this.mSecondGroup,this.mSecondSpacer}));
                break;
        }
        this.animator.prepareAnimation();
        this.animator.start();
    }
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    public void onClick(View view) {
        this.mScrollView.setOnTouchListener(this);
        switch (view.getId()) {
            case R.id.textpilih :
                checkSelected(this.mDari, false);
                removeFragment(R.id.edit_mode_fragment_container);
                MainActivity.changeFragment(R.id.edit_mode_fragment_container, JurusanFragment.newInstance(), true);
                startAnimation(this.mDari, 1);
                if (this.mDari.getText().toString().equals(BuildConfig.FLAVOR)) {
                    this.mDari.setHint("Dari");
                }
                this.isActionMode = true;
                return;
            case R.id.txttujuan :
                checkSelected(this.mTujuan, false);
                removeFragment(R.id.edit_mode_fragment_container);
                MainActivity.changeFragment(R.id.edit_mode_fragment_container, TujuanFragment.newInstance(this.mJurusan.kode),true);
                startAnimation(this.mTujuan, 2);
                if (this.mTujuan.getText().toString().equals(BuildConfig.FLAVOR)) {
                    this.mTujuan.setHint("Tujuan");
                }
                this.isActionMode = true;
                return;
            case R.id.txttanggal :
                checkSelected(this.mTggl, false);
                removeFragment(R.id.edit_mode_fragment_container);
                MainActivity.changeFragment(R.id.edit_mode_fragment_container, CalendarLandingFragment.newInstance(DateUtils.dateToLong(this.TglBrgkt), true, minDate, maxDate),true);
                startAnimation(this.mTggl, 3);
                this.isActionMode = true;
                return;
            case R.id.txtjam :
                checkSelected(this.jam, false);
                removeFragment(R.id.edit_mode_fragment_container);
                MainActivity.changeFragment(R.id.edit_mode_fragment_container, JamFragment.newInstance(this.gTujuan.kode,DateUtils.getStringDate(this.TglBrgkt)),true);
                startAnimation(this.jam, 4);
                if (this.jam.getText().toString().equals(BuildConfig.FLAVOR)) {
                    this.jam.setHint("Pilih Jam Keberangkatan");
                }
                this.isActionMode = true;
                return;
            case R.id.txtpenumpang :
                checkSelected(this.mPenumpang, false);
                removeFragment(R.id.edit_mode_fragment_container);
                MainActivity.changeFragment(R.id.edit_mode_fragment_container, PenumpangFragment.newInstance(this.mPenumpangs),true);
                startAnimation(this.mPenumpang, 5);
                if (this.mPenumpang.getText().toString().equals(BuildConfig.FLAVOR)) {
                    this.mPenumpang.setHint("Pilih Penumpang");
                }
                this.isActionMode = true;
                return;
            case R.id.button_search :

                    if(searchChecklist()){
                        searchTicket();
                        return;
                    }

                return;
            default:
                return;
        }
    }

    private void searchTicket() {
        Intent intent = new Intent(getActivity(), KursiActivity.class);
        intent.putExtra("id_jadwal", mJam.id_jadwal);
        intent.putExtra("jam", mJam.jam);
        intent.putExtra("batas", mJam.batas);
        intent.putExtra("keberangkatan", mJurusan.nama);
        intent.putExtra("tujuan", gTujuan.tujuan);
        intent.putExtra("nama_penumpang", mPenumpang.getText().toString());
        intent.putExtra("jml_penumpang",jmlPenumpang);
        intent.putExtra("nama",nama);
        intent.putExtra("email",email);
        intent.putExtra("phone",phone);
        intent.putExtra("id_armada", mJam.id_armada);
        intent.putExtra("harga", mJam.harga_tiket);
        intent.putExtra("potong", mJam.potongan);
        intent.putExtra("tanggal", DateUtils.getStringDate(this.TglBrgkt));

        startActivityForResult(intent, 20);
    }


    private void checkStatusList() {
        if (this.mJurusan != null) {
            this.mTujuan.setEnabled(true);
            this.mDari.setBackgroundResource(R.color.colorCardBackground);
            this.mTujuan.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tujuan_on, 0, 0, 0);
        } else {
            this.mTujuan.setEnabled(false);
            this.mTujuan.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tujuan, 0, 0, 0);
        }
        if (this.gTujuan != null) {
             this.jam.setEnabled(true);
            this.mTujuan.setBackgroundResource(R.color.colorCardBackground);
             this.jam.setCompoundDrawablesWithIntrinsicBounds(R.drawable.clock_on, 0, 0, 0);
        } else {
            this.jam.setEnabled(false);
            this.jam.setCompoundDrawablesWithIntrinsicBounds(R.drawable.clockgray, 0, 0, 0);
        }
        if(this.mJam != null){
            this.jam.setBackgroundResource(R.color.colorCardBackground);
        }
        if (this.jmlPenumpang != 0) {
            this.mPenumpang.setBackgroundResource(R.color.colorCardBackground);
        }

    }

    private boolean searchChecklist() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        this.mScrollView.setOnTouchListener(null);
        if (this.mJurusan == null) {
            YoYo.with(Techniques.Shake).playOn(getActivity().findViewById(R.id.textpilih));
            this.mDari.setBackgroundResource(R.color.colorCardErrorBackground);
            return false;
        } else if (this.gTujuan == null) {
            YoYo.with(Techniques.Shake).playOn(getActivity().findViewById(R.id.txttujuan));
            this.mTujuan.setBackgroundResource(R.color.colorCardErrorBackground);
            return false;
        } else if (this.mJam == null) {
            YoYo.with(Techniques.Shake).playOn(getActivity().findViewById(R.id.txtjam));
            this.jam.setBackgroundResource(R.color.colorCardErrorBackground);
            return false;
        } else if (this.jmlPenumpang > mJam.kursi) {
            YoYo.with(Techniques.Shake).playOn(getActivity().findViewById(R.id.txtpenumpang));
            this.mPenumpang.setBackgroundResource(R.color.colorCardErrorBackground);
                showSnackbar(mSearch, (int) R.string.eror_jml, false);
         return false;

        } else if (this.jmlPenumpang == 0) {
            YoYo.with(Techniques.Shake).playOn(getActivity().findViewById(R.id.txtpenumpang));
            this.mPenumpang.setBackgroundResource(R.color.colorCardErrorBackground);
            return false;
        }else if(mJam!=null){

            try {
                String tgl = DateUtils.getStringDate(TglBrgkt);
                List<NameValuePair> nvp = new ArrayList<NameValuePair>();
                nvp.add(new BasicNameValuePair("id_user", id_user));
                nvp.add(new BasicNameValuePair("id_jadwal", String.valueOf(mJam.id_jadwal)));
                nvp.add(new BasicNameValuePair("tanggal", tgl));
                JSONObject json = jParser.makeHttpRequest(url, "GET", nvp);
                int jumlah = json.getInt("jumlah");
                Log.d("tangal: ", tgl);
                Log.d("stat: ", String.valueOf(jumlah));
                if(jumlah>=2){
                    YoYo.with(Techniques.Shake).playOn(getActivity().findViewById(R.id.txtjam));
                    this.jam.setBackgroundResource(R.color.colorCardErrorBackground);
                    showSnackbar(mSearch, (int) R.string.eror_pesan_lebih, false);
                    return false;
                }else{
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        } else {

            return false;
        }

    }
    public void onDestroyActionMode(ActionMode actionMode) {
        destroyActionMode(actionMode);
    }

    private void destroyActionMode(ActionMode actionMode) {

        if (!this.animator.isStarted()) {
            this.animator.prepareRevert();
        }
        this.animator.start();
        checkStatusList();
        checkSelected(this.mDari, true);
        checkSelected(this.mTggl, true);
        checkSelected(this.mPenumpang, true);
        this.isActionMode = false;
        popBackStack();
    }

}


