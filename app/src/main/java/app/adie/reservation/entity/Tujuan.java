package app.adie.reservation.entity;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Adie on 15/04/2016.
 */
public class Tujuan implements Serializable {
    protected static Context mContext;
    protected static Tujuan mInstance;
    public String kode_asal;
    public String kota_tujuan;
    public String kode_tujuan;
    public String kode;
    public String tujuan;





    public Tujuan(Context context) {
        mContext = context;
    }

    public Tujuan() {

    }


    public static synchronized Tujuan getInstance(Context context) {
        Tujuan tujuan;
        synchronized (Tujuan.class) {
            if (mInstance == null) {
                mInstance = new Tujuan(context);
            }
            tujuan = mInstance;
        }
        return tujuan;
    }

    public ArrayList<Tujuan> render(JSONObject results) {
        ArrayList<Tujuan> list = new ArrayList();
        try {
            JSONArray tujuan = results.getJSONArray("jadwal");
            for (int i = 0; i < tujuan.length(); i++) {
                JSONObject object = tujuan.getJSONObject(i);
                Tujuan tuj = new Tujuan();
                tuj.kode_asal = object.getString("kode_asal");
                tuj.kode_tujuan = object.getString("kode_tujuan");
                tuj.kota_tujuan = object.getString("kota_tujuan");
                tuj.kode = object.getString("kode");
                tuj.tujuan= object.getString("tujuan");
                list.add(tuj);

            }
        } catch (Exception e) {

        }
        return list;
    }


}
