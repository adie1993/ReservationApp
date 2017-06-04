package app.adie.reservation.Fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.adie.reservation.R;

/**
 * Created by Adie on 28/04/2016.
 */
public class CalendarLandingFragment extends BaseFragment {
    private CalendarFragment mFragment;
    private static long maxDatee;
    private static long minDatee;
    private static boolean pickDatee = false;
    private static long selectedDatee;
    public static CalendarLandingFragment newInstance(long selectedDate, boolean pick, long minDate, long maxDate) {
        CalendarLandingFragment mFragment = new CalendarLandingFragment();

        selectedDatee = selectedDate;
        pickDatee = pick;
        minDatee = minDate;
        maxDatee = maxDate;

        return mFragment;
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_landing, null);

        new CountDownTimer(500, 100) {
            public void onTick(long l) {
            }

            public void onFinish() {
                cancel();
                CalendarLandingFragment.this.mFragment = CalendarFragment.newInstance(CalendarLandingFragment.this.selectedDatee, CalendarLandingFragment.this.pickDatee, CalendarLandingFragment.this.minDatee, CalendarLandingFragment.this.maxDatee);
                CalendarLandingFragment.this.getFragmentManager().beginTransaction().replace(R.id.content, CalendarLandingFragment.this.mFragment).commit();
            }
        }.start();
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
