package app.adie.reservation.entity;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Adie on 15/04/2016.
 */
public class Pesan implements DisplayableItem,Serializable {
    protected static Context mContext;
    protected static Pesan mInstance;






    public Pesan(Context context) {
        mContext = context;
    }

    public Pesan() {

    }


    public static synchronized Pesan getInstance(Context context) {
        Pesan pesan;
        synchronized (Pesan.class) {
            if (mInstance == null) {
                mInstance = new Pesan(context);
            }
            pesan = mInstance;
        }
        return pesan;
    }

    public ArrayList<Unpaid> renderUnpaid(JSONObject results) {
        ArrayList<Unpaid> list = new ArrayList();
        try {
            JSONArray tujuan = results.getJSONArray("unpaid");
            for (int i = 0; i < tujuan.length(); i++) {
                JSONObject object = tujuan.getJSONObject(i);
                Unpaid pes = new Unpaid();
                pes.id_pemesanan = object.getString("id_pem");
                pes.batas_waktu = object.getString("batas");
                pes.nama_penumpang = object.getString("nama");
                pes.no_seat = object.getString("no_seat");
                pes.harga = object.getInt("harga");
                pes.diskon = object.getInt("diskon");
                pes.total = object.getString("total");
                pes.kodebook = object.getString("kd_book");
                pes.cabangAsal = object.getString("asal");
                pes.cabangTujuan = object.getString("tujuan");
                pes.tanggalBerangkat = object.getString("tanggal");
                pes.jamBerangkat= object.getString("jam");
                list.add(pes);
            }
        } catch (Exception e) {

        }
        return list;
    }

    public ArrayList<Habis> renderHabis(JSONObject results) {
        ArrayList<Habis> list = new ArrayList();
        try {
            JSONArray tujuan = results.getJSONArray("habis");
            for (int i = 0; i < tujuan.length(); i++) {
                JSONObject object = tujuan.getJSONObject(i);
                Habis pes = new Habis();
                pes.id_pemesanan = object.getString("id_pem");
                pes.nama_penumpang = object.getString("nama");
                pes.no_seat = object.getString("no_seat");
                pes.harga = object.getInt("harga");
                pes.diskon = object.getInt("diskon");
                pes.total = object.getString("total");
                pes.kodebook = object.getString("kd_book");
                pes.cabangAsal = object.getString("asal");
                pes.cabangTujuan = object.getString("tujuan");
                pes.tanggalBerangkat = object.getString("tanggal");
                pes.jamBerangkat= object.getString("jam");
                list.add(pes);
            }
        } catch (Exception e) {

        }
        return list;
    }


    public ArrayList<Paid> renderPaid(JSONObject results) {
        ArrayList<Paid> list = new ArrayList();
        try {
            JSONArray tujuan = results.getJSONArray("paid");
            for (int i = 0; i < tujuan.length(); i++) {
                JSONObject object = tujuan.getJSONObject(i);
                Paid pes = new Paid();
                pes.id_pemesanan = object.getString("id_pem");
                pes.nama_penumpang = object.getString("nama");
                pes.no_seat = object.getString("no_seat");
                pes.harga = object.getInt("harga");
                pes.total = object.getString("total");
                pes.kodebook = object.getString("kd_book");
                pes.kodetiket = object.getString("kd_tiket");
                pes.cabangAsal = object.getString("asal");
                pes.cabangTujuan = object.getString("tujuan");
                pes.tanggalBerangkat = object.getString("tanggal");
                pes.jamBerangkat= object.getString("jam");
                list.add(pes);

            }
        } catch (Exception e) {

        }
        return list;
    }
    public ArrayList<InProgress> renderInProgress(JSONObject results) {
        ArrayList<InProgress> list = new ArrayList();
        try {
            JSONArray tujuan = results.getJSONArray("inprogress");
            for (int i = 0; i < tujuan.length(); i++) {
                JSONObject object = tujuan.getJSONObject(i);
                InProgress pes = new InProgress();
                pes.id_pemesanan = object.getString("id_pem");
                pes.nama_penumpang = object.getString("nama");
                pes.no_seat = object.getString("no_seat");
                pes.buktitransfer = object.getString("bukti");
                pes.harga = object.getInt("harga");
                pes.diskon = object.getInt("diskon");
                pes.total = object.getString("total");
                pes.kodebook = object.getString("kd_book");
                pes.cabangAsal = object.getString("asal");
                pes.cabangTujuan = object.getString("tujuan");
                pes.tanggalBerangkat = object.getString("tanggal");
                pes.jamBerangkat= object.getString("jam");
                list.add(pes);
            }
        } catch (Exception e) {

        }
        return list;
    }

    public ArrayList<Cancel> renderCancel(JSONObject results) {
        ArrayList<Cancel> list = new ArrayList();
        try {
            JSONArray tujuan = results.getJSONArray("cancel");
            for (int i = 0; i < tujuan.length(); i++) {
                JSONObject object = tujuan.getJSONObject(i);
                Cancel pes = new Cancel();
                pes.id_pemesanan = object.getString("id_pem");
                pes.nama_penumpang = object.getString("nama");
                pes.no_seat = object.getString("no_seat");
                pes.harga = object.getInt("harga");
                pes.diskon = object.getInt("diskon");
                pes.total = object.getString("total");
                pes.uangkem = object.getInt("uang_kembali");
                pes.kodebook = object.getString("kd_book");
                pes.kodetiket = object.getString("kd_tiket");
                pes.cabangAsal = object.getString("asal");
                pes.cabangTujuan = object.getString("tujuan");
                pes.tanggalBerangkat = object.getString("tanggal");
                pes.jamBerangkat= object.getString("jam");
                list.add(pes);
            }
        } catch (Exception e) {

        }
        return list;
    }
    public ArrayList<Rejected> renderReject(JSONObject results) {
        ArrayList<Rejected> list = new ArrayList();
        try {
            JSONArray reject = results.getJSONArray("reject");
            for (int i = 0; i < reject.length(); i++) {
                JSONObject object = reject.getJSONObject(i);
                Rejected pes = new Rejected();
                pes.id_pemesanan = object.getString("id_pem");
                pes.nama_penumpang = object.getString("nama");
                pes.no_seat = object.getString("no_seat");
                pes.harga = object.getInt("harga");
                pes.diskon = object.getInt("diskon");
                pes.total = object.getString("total");
                pes.kodebook = object.getString("kd_book");
                pes.cabangAsal = object.getString("asal");
                pes.cabangTujuan = object.getString("tujuan");
                pes.tanggalBerangkat = object.getString("tanggal");
                pes.jamBerangkat= object.getString("jam");
                list.add(pes);
            }
        } catch (Exception e) {

        }
        return list;
    }

}
