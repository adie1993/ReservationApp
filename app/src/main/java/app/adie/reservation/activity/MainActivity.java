package app.adie.reservation.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.adie.reservation.Fragment.AlertDialogFragment;
import app.adie.reservation.Fragment.HistoryFragment;
import app.adie.reservation.Fragment.PromoFragment;
import app.adie.reservation.Fragment.SopirFragment;
import app.adie.reservation.JSONParser;
import app.adie.reservation.R;
import app.adie.reservation.SessionManager;
import app.adie.reservation.app.Config;
import app.adie.reservation.app.EndPoints;
import app.adie.reservation.app.MyApplication;
import app.adie.reservation.entity.ChatRoom;
import app.adie.reservation.entity.Message;
import app.adie.reservation.entity.SqliteHelper;
import app.adie.reservation.gcm.GcmIntentService;
import app.adie.reservation.gcm.NotificationUtils;
import app.adie.reservation.utils.ConnectionDetector;
import app.adie.reservation.utils.DateUtils;

;

public class MainActivity extends BaseActivity {
    public static DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    public static FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    SessionManager session;
    private static MaterialViewPager mViewPager;
    private String TAG = MainActivity.class.getSimpleName();
    private Toolbar toolbar;
    public static AppCompatActivity activity;
    static Context context;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public static ActionBarDrawerToggle mDrawerToggle;
    private SqliteHelper sqLiteHelper;
    private ArrayList<ChatRoom> chatRoomArrayList;
    private int a,b;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private static String url = "http://10.42.0.1/kraka/api/updatestat.php";
    JSONArray contacts = null;
    String email, nama, id,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cd = new ConnectionDetector(getApplicationContext());

        setTitle("");
        sqLiteHelper = new SqliteHelper(this);
        context = getApplicationContext();
        activity = MainActivity.this;
        Bundle extras = getIntent().getExtras();
        session = new SessionManager(getApplicationContext());
        DateUtils.setMonthsName(DateUtils.monthLabels());
        DateUtils.setShortMonthsName(DateUtils.monthLabelsShort());
        DateUtils.setWeekdaysName(DateUtils.weekDayLabels());
        DateUtils.setShortWeekdaysName(DateUtils.weekDayLabelsShort());
        sqLiteHelper.BuatTabel();
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();
        phone = user.get(SessionManager.KEY_PHONE);
        email = user.get(SessionManager.KEY_EMAIL);
        nama = user.get(SessionManager.KEY_NAME);
        id = user.get(SessionManager.KEY_ID);
        if (extras != null){
            this.a = extras.getInt("ex");
            this.b = extras.getInt("get");
        }
        if(a==1){
            sqLiteHelper.tambahdatauser(nama);

        }
View parentlayout = findViewById(R.id.materialViewPager);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url);

            String success;
            try {
                success = json.getString("success");
                Log.d("Tes: ", success);
            } catch (JSONException e) {

            }
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            showSnackbar(parentlayout, (int) R.string.message_no_network_connection, true);
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    subscribeToGlobalTopic();

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL
                    Log.e(TAG, "GCM registration id is sent to our server");

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    handlePushNotification(intent);
                }
            }
        };
        chatRoomArrayList = new ArrayList<>();
        if (checkPlayServices()) {
            registerGCM();
            fetchChatRooms();
        }

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        toolbar = mViewPager.getToolbar();
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

            mNavigationView = (NavigationView) findViewById(R.id.shitstuff);


        View headerLayout = mNavigationView.inflateHeaderView(R.layout.nav_header);

        TextView txthelo = (TextView) headerLayout.findViewById(R.id.helo);
        txthelo.setText("Selamat Datang "+ nama);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }
        mFragmentManager = getSupportFragmentManager();


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                if (menuItem.getItemId() == R.id.akun_saya) {
                    Intent chat = new Intent(MainActivity.this, AkunSaya.class);
                    startActivity(chat);


                }
                if (menuItem.getItemId() == R.id.keluhan) {
                    Intent chat = new Intent(MainActivity.this, ChatActivity.class);
                    startActivity(chat);


                }
                if (menuItem.getItemId() == R.id.nav_item_logout) {
                    sqLiteHelper.HapusTabel();
                    session.logoutUser();
                   
                    finish();

                }
                return false;
            }

        });


        showToolbar();
        MainActivity.activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

                mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.drawer_open,R.string.drawer_close);


                mDrawerLayout.setDrawerListener(mDrawerToggle);
                mDrawerToggle.syncState();
        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 3) {
                    case 0:
                        return PesanTiket.newInstance();
                    case 1:
                        return HistoryFragment.newInstance();
                    case 2:
                        return PromoFragment.newInstance();

                    default:
                        return PesanTiket.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 3) {
                    case 0:
                        return "Pesan Tiket";
                    case 1:
                        return "Pesanan";
                    case 2:
                        return "Promo";


                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.colorPrimary,
                                "http://10.42.0.1/kraka/api/wel.png");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.main,
                                "http://10.42.0.1/kraka/api/tess.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.colorAccent,
                                "http://10.42.0.1/kraka/api/ff.png");




                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
        if(b==1){
            mViewPager.getViewPager().setCurrentItem(1,true);
        }
        if(b==2){
            mViewPager.getViewPager().setCurrentItem(1,true);
            AlertDialogFragment dialogFragment = AlertDialogFragment.newInstance();
            View layoutDialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_pembayaran_sukses, null);
            dialogFragment.init(MainActivity.this, AlertDialogFragment.DIALOG_CUSTOM_VIEW).setView(layoutDialog).setPositiveButton((int) R.string.add_button_text).dialogKey("").dialogType("ALERT");
            dialogFragment.show(getSupportFragmentManager(), "dialog");
        }
        if(b==3){
            mViewPager.getViewPager().setCurrentItem(1,true);
            AlertDialogFragment dialogFragment = AlertDialogFragment.newInstance();
            View layoutDialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_pembayaran_cancel, null);
            dialogFragment.init(MainActivity.this, AlertDialogFragment.DIALOG_CUSTOM_VIEW).setView(layoutDialog).setPositiveButton((int) R.string.add_button_text).dialogKey("").dialogType("ALERT");
            dialogFragment.show(getSupportFragmentManager(), "dialog");
        }

        View logo = findViewById(R.id.logo_white);
        if (logo != null)
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();

                }
            });
    }

    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);

        // if the push is of chat room message
        // simply update the UI unread messages count
        if (type == Config.PUSH_TYPE_CHATROOM) {
            Message message = (Message) intent.getSerializableExtra("message");
            String chatRoomId = intent.getStringExtra("chat_room_id");

            if (message != null && chatRoomId != null) {
                updateRow(chatRoomId, message);
            }
        } else if (type == Config.PUSH_TYPE_USER) {
            // push belongs to user alone
            // just showing the message in a toast
            Message message = (Message) intent.getSerializableExtra("message");
            Toast.makeText(getApplicationContext(), message.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    private void updateRow(String chatRoomId, Message message) {
        for (ChatRoom cr : chatRoomArrayList) {
            if (cr.getId().equals(chatRoomId)) {
                int index = chatRoomArrayList.indexOf(cr);
                cr.setLastMessage(message.getMessage());
                cr.setUnreadCount(cr.getUnreadCount() + 1);
                chatRoomArrayList.remove(index);
                chatRoomArrayList.add(index, cr);
                break;
            }
        }

    }
    private void fetchChatRooms() {
        String selfUserId = MyApplication.getInstance().getPrefManager().getUser().getId();
        String endPoint = EndPoints.CHAT_ROOMS.replace("_ID_", selfUserId);




        Log.e(TAG, "endpointchat: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {

                    JSONObject obj = new JSONObject(response);
                    // check for error flag
                    if (obj.getBoolean("error") == false) {


                        JSONArray chatRoomsArray = obj.getJSONArray("chat_rooms");
                        for (int i = 0; i < chatRoomsArray.length(); i++) {
                            JSONObject chatRoomsObj = (JSONObject) chatRoomsArray.get(i);
                            ChatRoom cr = new ChatRoom();
                            cr.setId(chatRoomsObj.getString("chat_room_id"));
                            cr.setName(chatRoomsObj.getString("name"));


                            cr.setTimestamp(chatRoomsObj.getString("created_at"));
                            if(chatRoomsObj.getInt("status")>0){
                                cr.setLastMessage(chatRoomsObj.getString("message"));
                                cr.setUnreadCount(chatRoomsObj.getInt("status"));
                            }else if(chatRoomsObj.getInt("status")==0){
                                cr.setLastMessage(chatRoomsObj.getString("message"));
                                cr.setUnreadCount(0);
                            }
                            chatRoomArrayList.add(cr);

                        }

                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


                // subscribing to all chat room topics
                subscribeToAllTopics();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                View parentlayout = findViewById(R.id.materialViewPager);
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                showSnackbar(parentlayout, (int) R.string.message_no_network_connection, true);
            }
        });

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void subscribeToGlobalTopic() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
        intent.putExtra(GcmIntentService.TOPIC, Config.TOPIC_GLOBAL);
        startService(intent);
    }

    private void subscribeToAllTopics() {
        for (ChatRoom cr : chatRoomArrayList) {

            Intent intent = new Intent(this, GcmIntentService.class);
            intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
            intent.putExtra(GcmIntentService.TOPIC, "topic_" + cr.getId());
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clearing the notification tray
        NotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    // starting the service to register with GCM
    private void registerGCM() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    public static void changeFragment(int continerViewId, Fragment fragment, boolean doAddToBackStack) {
        //TabFragment.tabLayout.setVisibility(View.GONE);
        mViewPager.setEnabled(false);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(continerViewId, fragment);
        if (doAddToBackStack) {
            fragmentTransaction.addToBackStack(null);
            MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(false);
            activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        } else {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mDrawerToggle.syncState();
        }
        fragmentTransaction.commit(); 
    }

    private void showToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.isDrawerIndicatorEnabled()&&mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
            PesanTiket.getInstance().popBackStack();
        MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(true);

    }
}