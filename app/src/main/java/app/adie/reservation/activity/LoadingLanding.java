package app.adie.reservation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

import app.adie.reservation.R;

/**
 * Created by Adie on 07/05/2016.
 */
public class LoadingLanding extends Activity {
    private AnimatedCircleLoadingView animatedCircleLoadingView;
    private int statusBarColor;
    private int a =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadinglanding);
        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        startLoading();
        startPercentMockThread();
        if (Build.VERSION.SDK_INT >= 21) {
            this.statusBarColor = getWindow().getStatusBarColor();
            getWindow().setStatusBarColor(getResources().getColor(R.color.main));
        }
    }

    private void startLoading() {
        animatedCircleLoadingView.startDeterminate();
    }

    private void startPercentMockThread() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(600);
                    for (int i = 0; i <= 100; i++) {
                        Thread.sleep(30);
                        changePercent(i);

                    }

                    //resetLoading();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    private void changePercent(final int percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.setPercent(percent);
                if (percent==100){
                    animatedCircleLoadingView.stopOk();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(LoadingLanding.this, MainActivity.class);
                            intent.putExtra("ex", a);
                            startActivityForResult(intent, 20);
                            finish();
                        }
                    }, 3000);

                }
            }
        });
    }

    public void resetLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.resetLoading();
            }
        });
    }
}
