package app.adie.reservation.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import app.adie.reservation.activity.CalendarPromoActivity;
import app.adie.reservation.R;
import app.adie.reservation.timessquare.CalendarPickerView;
import app.adie.reservation.timessquare.CalendarPickerView.OnDateSelectedListener;
import app.adie.reservation.utils.DateUtils;

/**
 * Created by Adie on 28/04/2016.
 */
public class CalendarPromoFragment extends BaseFragment implements OnDateSelectedListener {
    private CalendarPickerView calendarPickerView;
    private static long maxDatee;
    private static String kode,asal,tuju;
    private static long minDatee;
    private static boolean pickDatee = false;
    private static long selectedDatee;
    private TextView txtpromo;

    public static CalendarPromoFragment newInstance(long selectedDate, boolean pick, long minDate, long maxDate,String kod,String asl,String tuj) {
        CalendarPromoFragment mFragment = new CalendarPromoFragment();

        selectedDatee = selectedDate;
        pickDatee = pick;
        minDatee = minDate;
        maxDatee = maxDate;
        kode = kod;
        asal = asl;
        tuju=tuj;
        return mFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, null);
        initViews(view);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initViews(View view) {
        this.calendarPickerView = (CalendarPickerView) view.findViewById(R.id.calendar_view);
        this.calendarPickerView.setOnDateSelectedListener(this);
        this.txtpromo = (TextView) CalendarPromoActivity.activity.findViewById(R.id.txtpromo);
        txtpromo.setText("");
        txtpromo.setHint("Pilih Tanggal Keberangkatan");
        this.txtpromo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0);
        CalendarPromoActivity.activity.getSupportActionBar().setTitle("Tanggal Berangkat");
        calendarInit();
    }

    public void onResume() {
        super.onResume();

    }

    private void calendarInit() {
        try {
            if (this.minDatee == 0) {
                this.calendarPickerView.init(DateUtils.today(), DateUtils.longToDate(this.maxDatee)).withSelectedDate(DateUtils.longToDate(this.selectedDatee)).setShortWeekdays(DateUtils.weekDayLabelsShort());
            }
            this.calendarPickerView.init(DateUtils.longToDate(this.minDatee), DateUtils.longToDate(this.maxDatee)).setShortWeekdays(DateUtils.weekDayLabelsShort()).withSelectedDate(DateUtils.longToDate(this.selectedDatee));
        } catch (Exception e) {

        }
    }



    @Override
    public void onDateSelected(Date date) {

        changeFragment(R.id.containerVieww, PenumpangPromoFragment.newInstance(date, kode,asal,tuju));

    }

    @Override
    public void onDateUnselected(Date date) {

    }
    public void changeFragment(int continerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(continerViewId, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
