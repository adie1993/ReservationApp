package app.adie.reservation.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.adie.reservation.R;
import app.adie.reservation.app.EndPoints;
import app.adie.reservation.app.MyApplication;

/**
 * Created by Adie on 11/05/2016.
 */
public class ChatTambahActivity extends AppCompatActivity implements OnClickListener {
    private String TAG = ChatTambahActivity.class.getSimpleName();
    private Button mButtonBatal;
    private Button mButtonTambah;
    private LinearLayout mLayoutButton;
    private EditText mJudul;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_chat_tambah);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        this.mButtonBatal = (Button) findViewById(R.id.batal);
        this.mButtonTambah = (Button) findViewById(R.id.tambah);
        this.mJudul = (EditText) findViewById(R.id.judul);
        this.mLayoutButton = (LinearLayout) findViewById(R.id.button);
        this.mButtonBatal.setOnClickListener(this);
        this.mButtonTambah.setOnClickListener(this);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.batal:
                onBackPressed();
                return;
            case R.id.tambah:
                if (isDataValid()) {
                    this.mJudul.setEnabled(false);
                    this.mLayoutButton.setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tambahChat();
                        }
                    }, 1500);
                    return;
                }

                return;
            default:
                return;
        }
    }

    private void tambahChat() {

        final String selfUserId = MyApplication.getInstance().getPrefManager().getUser().getId();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.INSERTCHAT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        // user successfully logged in

                        startActivity(new Intent(getApplicationContext(), ChatActivity.class));
                        finish();

                    } else {
                        // login error - simply toast the message
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", selfUserId);
                params.put("judul", mJudul.getText().toString());

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);


        Intent intent = new Intent();
        setResult(-1, intent);
        finish();
    }

    public boolean isDataValid() {
        boolean isJudulValid;
        if (getJudul().isEmpty()) {
            isJudulValid = false;
        } else {
            isJudulValid = true;
        }

        if (isJudulValid ) {
            return true;
        }
        return false;
    }
    private String getJudul() {
        return this.mJudul.getText().toString().trim();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onBackPressed(){
        Intent chat = new Intent(ChatTambahActivity.this, ChatActivity.class);
        startActivity(chat);

    }
}
