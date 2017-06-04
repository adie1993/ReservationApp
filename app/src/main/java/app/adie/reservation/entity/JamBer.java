package app.adie.reservation.entity;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class JamBer implements Serializable {
    protected static Context mContext;
    protected static JamBer mInstance;
    public String jam,batas;
    public int kursi;
    public int id_jadwal;
    public int id_armada;
    public int harga_tiket;
    public int potongan;
    public Date jdwl;
    public Date psn;
    public long jd,ps;


    public JamBer(Context context) {
        mContext = context;
    }

    public JamBer() {

    }


    public static synchronized JamBer getInstance(Context context) {
        JamBer jamber;
        synchronized (JamBer.class) {
            if (mInstance == null) {
                mInstance = new JamBer(context);
            }
            jamber = mInstance;
        }
        return jamber;
    }

    public ArrayList<JamBer> render(JSONObject results) {
        ArrayList<JamBer> list = new ArrayList();
        try {
            JSONArray JamBer = results.getJSONArray("jamrud");

            for (int i = 0; i < JamBer.length(); i++) {
                JSONObject object = JamBer.getJSONObject(i);
                    if(object.getInt("kursi")>=1){


                    JamBer jam = new JamBer();
                    jam.id_armada = object.getInt("id_armada");
                    jam.id_jadwal = object.getInt("id_jadwal");
                    jam.jam = object.getString("jam");
                        jam.batas = object.getString("batas");
                    jam.kursi = object.getInt("kursi");
                    jam.harga_tiket = object.getInt("harga_tiket");
                        jam.potongan = object.getInt("potongan");
                    list.add(jam);
                    }

            }
        } catch (Exception e) {

        }
        return list;
    }
}
