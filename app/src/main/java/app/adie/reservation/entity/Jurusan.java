package app.adie.reservation.entity;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Adie on 15/04/2016.
 */
public class Jurusan implements Serializable {
    protected static Context mContext;
    protected static Jurusan mInstance;
    public String kode;
    public String kota;
    public String nama;




    public Jurusan(Context context) {
        mContext = context;
    }

    public Jurusan() {

    }


    public static synchronized Jurusan getInstance(Context context) {
        Jurusan jurusan;
        synchronized (Jurusan.class) {
            if (mInstance == null) {
                mInstance = new Jurusan(context);
            }
            jurusan = mInstance;
        }
        return jurusan;
    }

    public ArrayList<Jurusan> render(JSONObject results) {
        ArrayList<Jurusan> list = new ArrayList();
        try {
            JSONArray jurusan = results.getJSONArray("Jurusan");
            for (int i = 0; i < jurusan.length(); i++) {
                JSONObject object = jurusan.getJSONObject(i);
                Jurusan jur = new Jurusan();
                jur.kota = object.getString("kota");
                jur.kode = object.getString("kode");
                jur.nama= object.getString("nama");
                list.add(jur);
            }
        } catch (Exception e) {

        }
        return list;
    }
}
