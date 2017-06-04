package app.adie.reservation.activity;

/**
 * Created by Adie on 28/04/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import app.adie.reservation.R;
import app.adie.reservation.SessionManager;

public class SplashScreenActivity extends BaseActivity {
    SessionManager session;
    private CountDownTimer mCountDownTimer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        this.mCountDownTimer = new CountDownTimer(1000, 2000) {
            public void onTick(long l) {
            }

            public void onFinish() {
                session = new SessionManager(getApplicationContext());


                session.checkLogin();

                if(session.isLoggedIn()) {
                    Intent a = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(a);
                    finish();
                }

            }
        }.start();
    }


}
