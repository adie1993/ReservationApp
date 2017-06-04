package app.adie.reservation.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

import app.adie.reservation.activity.PesanTiket;
import app.adie.reservation.R;
import app.adie.reservation.timessquare.CalendarPickerView;
import app.adie.reservation.timessquare.CalendarPickerView.OnDateSelectedListener;
import app.adie.reservation.utils.DateUtils;

/**
 * Created by Adie on 28/04/2016.
 */
public class CalendarFragment extends BaseFragment implements OnDateSelectedListener {
    private CalendarPickerView calendarPickerView;
    private static long maxDatee;
    private static long minDatee;
    private static boolean pickDatee = false;
    private static long selectedDatee;

    public static CalendarFragment newInstance(long selectedDate, boolean pick, long minDate, long maxDate) {
        CalendarFragment mFragment = new CalendarFragment();

        selectedDatee = selectedDate;
        pickDatee = pick;
        minDatee = minDate;
        maxDatee = maxDate;

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
        //this.calendarPickerView.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "sans-serif.ttf"));
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
            PesanTiket.getInstance().getDateFromFragmentContainer(date);

    }

    @Override
    public void onDateUnselected(Date date) {

    }
}
