package app.adie.reservation.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import app.adie.reservation.R;
import app.adie.reservation.view.widget.LoadingIndicator;



public class BaseActivity extends AppCompatActivity {
    protected boolean isDrawer;
    protected boolean isLastActivities = false;
    protected DrawerLayout mDrawerLayout;
    protected SharedPreferences mSharedPreferences;
    Snackbar snackbar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }



    }


    public boolean isLastActivities() {
        return this.isLastActivities;
    }

    public void setIsLastActivities(boolean isLastActivities) {
        this.isLastActivities = isLastActivities;
    }


    public boolean isNetworkOn(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }



    public void addFragment(int continerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(continerViewId, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void removeFragment(int continerViewId) {
        if (getFragmentManager().findFragmentById(continerViewId) != null) {
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(continerViewId)).commit();
        }
    }

    public boolean setLoadingIndicator(LoadingIndicator mIndicator, boolean isShow) {
        if (isShow) {
            mIndicator.setVisibility(View.VISIBLE);
            return true;
        }
        mIndicator.setVisibility(View.GONE);
        return false;
    }
    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }
    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }
    public void showSnackbar(View view, String message, boolean length) {
        if (length) {
            snackbar=Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            View snackBarview = snackbar.getView();
            snackBarview.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
            snackbar.show();
        } else {
            snackbar=Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            View snackBarview = snackbar.getView();
            snackBarview.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
            snackbar.show();
        }
    }

    public void showSnackbar(View view, int message, boolean length) {

        if (length) {
            snackbar=Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            View snackBarview = snackbar.getView();
            snackBarview.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
            snackbar.show();
        } else {
            snackbar=Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            View snackBarview = snackbar.getView();
            snackBarview.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
            snackbar.show();
        }
    }

    public void showSnackbarAction(View view, String message, boolean length) {
        if (length) {
            Snackbar.make(view, message, 0).setAction("Action", null).show();
        } else {
            Snackbar.make(view, message, -1).setAction("Action", null).show();
        }
    }

    public void showToast(Context context, String message, boolean length) {
        if (length) {
            Toast.makeText(context, message, 1).show();
        } else {
            Toast.makeText(context, message, 0).show();
        }
    }

    public void showToast(Context context, int message, boolean length) {
        if (length) {
            Toast.makeText(context, message, 1).show();
        } else {
            Toast.makeText(context, message, 0).show();
        }
    }
    public void changeFragment(int continerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(continerViewId, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }






}
