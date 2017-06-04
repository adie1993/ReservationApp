package app.adie.reservation.entity;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Adie on 15/04/2016.
 */
public class Sopir implements Serializable {
    protected static Context mContext;
    protected static Sopir mInstance;
    public String kode;
    public String alamat;
    public String nama;




    public Sopir(Context context) {
        mContext = context;
    }

    public Sopir() {

    }


    public static synchronized Sopir getInstance(Context context) {
        Sopir sopir;
        synchronized (Sopir.class) {
            if (mInstance == null) {
                mInstance = new Sopir(context);
            }
            sopir = mInstance;
        }
        return sopir;
    }

    public ArrayList<Sopir> render(JSONObject results) {
        ArrayList<Sopir> list = new ArrayList();
        try {
            JSONArray sopir = results.getJSONArray("sopir");
            for (int i = 0; i < sopir.length(); i++) {
                JSONObject object = sopir.getJSONObject(i);
                Sopir jur = new Sopir();
                jur.alamat = object.getString("phone");
                jur.nama= object.getString("nama");
                list.add(jur);
            }
        } catch (Exception e) {

        }
        return list;
    }
}
