package app.adie.reservation.entity;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class JamPromo implements Serializable {
    protected static Context mContext;
    protected static JamPromo mInstance;
    public String jam,batas;
    public int kursi;
    public int id_jadwal;
    public int id_armada;
    public int harga_tiket;
    public int potongan;
    public Date jdwl;
    public Date psn;
    public long jd,ps;


    public JamPromo(Context context) {
        mContext = context;
    }

    public JamPromo() {

    }


    public static synchronized JamPromo getInstance(Context context) {
        JamPromo jamber;
        synchronized (JamPromo.class) {
            if (mInstance == null) {
                mInstance = new JamPromo(context);
            }
            jamber = mInstance;
        }
        return jamber;
    }

    public ArrayList<JamPromo> render(JSONObject results) {
        ArrayList<JamPromo> list = new ArrayList();
        try {
            JSONArray JamBer = results.getJSONArray("jamrud");

            for (int i = 0; i < JamBer.length(); i++) {
                JSONObject object = JamBer.getJSONObject(i);
                    if(object.getInt("kursi")>=1 && object.getInt("potongan")!=0){


                    JamPromo jam = new JamPromo();

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
