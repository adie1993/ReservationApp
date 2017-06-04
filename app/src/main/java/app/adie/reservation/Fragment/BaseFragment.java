package app.adie.reservation.Fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import app.adie.reservation.activity.BaseActivity;
import app.adie.reservation.view.widget.LoadingIndicator;

public class BaseFragment extends Fragment {





    public boolean setLoadingIndicator(LoadingIndicator mIndicator, boolean isShow) {
        return ((BaseActivity) getActivity()).setLoadingIndicator(mIndicator, isShow);
    }

    public void showSnackbar(View view, String message, boolean length) {
        ((BaseActivity) getActivity()).showSnackbar(view, message, length);
    }

    public void showSnackbar(View view, int message, boolean length) {
        ((BaseActivity) getActivity()).showSnackbar(view, message, length);
    }

    public void showToast(Context context, int message, boolean length) {
        ((BaseActivity) getActivity()).showToast(context, message, length);
    }

    public void showToast(Context context, String message, boolean length) {
        ((BaseActivity) getActivity()).showToast(context, message, length);
    }

    public void changeFragment(int continerViewId, Fragment fragment) {
        ((BaseActivity) getActivity()).changeFragment(continerViewId, fragment);
    }


    public void addFragment(int continerViewId, Fragment fragment) {
        ((BaseActivity) getActivity()).addFragment(continerViewId, fragment);
    }

    public void removeFragment(int continerViewId) {
        ((BaseActivity) getActivity()).removeFragment(continerViewId);
    }

    public boolean isLastActivities() {
        return ((BaseActivity) getActivity()).isLastActivities();
    }

    public void setIsLastActivities(boolean isLastActivities) {
        ((BaseActivity) getActivity()).setIsLastActivities(isLastActivities);
    }



}
