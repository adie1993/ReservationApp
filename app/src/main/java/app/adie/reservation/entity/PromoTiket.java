package app.adie.reservation.entity;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class PromoTiket implements Serializable {
    protected static Context mContext;
    protected static PromoTiket mInstance;
    public String asal;
    public int harga;
    public int potongan;
    public String tanggal;
    public String kode,id_jadwal;
    public String tujuan,foto;

    public PromoTiket(Context context) {
        mContext = context;
    }

    public PromoTiket() {

    }

    public static synchronized PromoTiket getInstance(Context context) {
        PromoTiket promoTiket;
        synchronized (PromoTiket.class) {
            if (mInstance == null) {
                mInstance = new PromoTiket(context);
            }
            promoTiket = mInstance;
        }
        return promoTiket;
    }

    public ArrayList<PromoTiket> render(JSONObject results) {
        ArrayList<PromoTiket> list = new ArrayList();
        try {
            JSONArray jsonArray = results.getJSONArray("promo");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                PromoTiket promoTiket = new PromoTiket();
                promoTiket.asal = object.getString("cabang_asal");
                promoTiket.tujuan = object.getString("cabang_tujuan");
                promoTiket.kode = object.getString("kode");
                promoTiket.tanggal = object.getString("tanggal");
                promoTiket.harga = object.getInt("harga");
                promoTiket.potongan = object.getInt("potongan");
                promoTiket.foto = object.getString("foto_promo");

                list.add(promoTiket);
            }
        } catch (Exception e) {

        }
        return list;
    }
}
