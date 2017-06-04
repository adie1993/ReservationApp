package app.adie.reservation.entity;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TreeMap;

public class Kursi {
    private static Context mContext;
    private static Kursi mInstance;
    public int cols;
    public ArrayList<KursiDetil> kursiList;
    public int rows;

    public Kursi(Context context) {
        mContext = context;
    }

    public Kursi() {

    }

    public static synchronized Kursi getInstance(Context context) {
        Kursi kursi;
        synchronized (Kursi.class) {
            if (mInstance == null) {
                mInstance = new Kursi(context);
            }
            kursi = mInstance;
        }
        return kursi;
    }

    public Kursi render(JSONObject results, TreeMap<String, String> kursiReserved) {
        Kursi kursi = new Kursi();
        try {
            ArrayList<KursiDetil> list = new ArrayList();
           // kursi.rows = results.getInt("rows");
            kursi.cols = results.getInt("cols");
            JSONArray seat = results.getJSONArray("seat");
            for (int i = 0; i < seat.length(); i++) {
                JSONObject object = seat.getJSONObject(i);
                KursiDetil kursiDetil = new KursiDetil();
               // kursiDetil.row = object.getInt("row");
              //  kursiDetil.col = object.getInt("col");
                kursiDetil.no = object.getString("no");
                kursiDetil.type = gantiTipeKursi(object.getString("no"), object.getString("type"), kursiReserved);
                list.add(kursiDetil);
            }
            kursi.kursiList = list;
        } catch (Exception e) {

        }
        return kursi;
    }

    private String gantiTipeKursi(String noKursi, String type, TreeMap<String, String> kursiReserved) {
        String type_kursi = type;
        for (int i = 0; i < kursiReserved.size(); i++) {
            if (kursiReserved.values().toArray()[i].equals(noKursi)) {
                return "B";
            }
        }
        return type_kursi;
    }

    public TreeMap<String, String> renderReserved(JSONObject results) {
        TreeMap<String, String> reserved = new TreeMap();

        try {

            String[] no_kursi = results.getString("kursi").split(",");
            for (int i = 0; i < no_kursi.length; i++) {
                reserved.put(no_kursi[i], no_kursi[i]);
            }
        } catch (Exception e) {

        }
        return reserved;
    }
}
