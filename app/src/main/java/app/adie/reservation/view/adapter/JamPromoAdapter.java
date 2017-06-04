package app.adie.reservation.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.adie.reservation.activity.CalendarPromoActivity;
import app.adie.reservation.JSONParser;
import app.adie.reservation.activity.KursiActivity;
import app.adie.reservation.R;
import app.adie.reservation.entity.JamPromo;
import app.adie.reservation.utils.StringUtils;

public class JamPromoAdapter extends Adapter<JamPromoAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<JamPromo> mJamBer;
    private static String asal,tuju,nama_penum,nama,email,phone,tgl,id_user;
    private static int jmlh,jmlhrow;


    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
        public JamPromo mJamBer;
        public TextView mPrimary;
        public TextView mSecondary;
        public TextView mThird;
        public ImageView mSale;
        Snackbar snackbar;
        String url = "http://10.42.0.1/kraka/api/cekuser.php";
        JSONParser jParser = new JSONParser();

        public void showSnackbar(View view, int message, boolean length) {

            if (length) {
                snackbar= Snackbar.make(view, message, Snackbar.LENGTH_LONG);
                View snackBarview = snackbar.getView();
                snackBarview.setBackgroundColor(ContextCompat.getColor(CalendarPromoActivity.activity, R.color.red));
                snackbar.show();
            } else {
                snackbar=Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
                View snackBarview = snackbar.getView();
                snackBarview.setBackgroundColor(ContextCompat.getColor(CalendarPromoActivity.activity,R.color.red));
                snackbar.show();
            }
        }
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.mPrimary = (TextView) itemLayoutView.findViewById(R.id.primary);
            this.mSecondary = (TextView) itemLayoutView.findViewById(R.id.secondary);
            this.mThird = (TextView) itemLayoutView.findViewById(R.id.third);
            this.mSale = (ImageView) itemLayoutView.findViewById(R.id.sale);
            itemLayoutView.setOnClickListener(this);
        }

        public void onClick(View v) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            List<NameValuePair> nvp = new ArrayList<NameValuePair>();
            nvp.add(new BasicNameValuePair("id_user", id_user));
            nvp.add(new BasicNameValuePair("id_jadwal", String.valueOf(mJamBer.id_jadwal)));
            nvp.add(new BasicNameValuePair("tanggal", tgl));
            JSONObject jsson = jParser.makeHttpRequest(url, "GET", nvp);
            int jumlahrow = 0;
            try {
                jumlahrow = jsson.getInt("jumlah");
                Log.d("stat: ", String.valueOf(jumlahrow));
                if(jmlh> mJamBer.kursi){
                    showSnackbar(mPrimary, (int) R.string.eror_jml, false);
                }
                else if(jumlahrow>=2){
                    showSnackbar(mPrimary, (int) R.string.eror_pesan_lebih, false);
                }else{

                    Intent intent = new Intent(v.getContext(), KursiActivity.class);
                    intent.putExtra("id_jadwal", mJamBer.id_jadwal);
                    intent.putExtra("jam", mJamBer.jam);
                    intent.putExtra("batas", mJamBer.batas);
                    intent.putExtra("keberangkatan",asal );
                    intent.putExtra("tujuan", tuju);
                    intent.putExtra("nama_penumpang", nama_penum);
                    intent.putExtra("jml_penumpang",jmlh);
                    intent.putExtra("nama",nama);
                    intent.putExtra("email",email);
                    intent.putExtra("phone",phone);
                    intent.putExtra("id_armada", mJamBer.id_armada);
                    intent.putExtra("harga", mJamBer.harga_tiket);
                    intent.putExtra("potong", mJamBer.potongan);
                    intent.putExtra("tanggal", tgl);
                    v.getContext().startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
        public JamPromoAdapter(Context context, ArrayList<JamPromo> jamber,String asl,String tuj,String nam_pen,int jml,String nam,String ema,String pho,String tggl,String id) {
            this.mContext = context;
            this.mJamBer = jamber;
            asal = asl;
            tuju = tuj;
            nama_penum = nam_pen;
            jmlh= jml;
            nama=nam;
            email=ema;
            phone=pho;
            tgl=tggl;
            id_user = id;
        }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_jam, null));
    }

   @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        JamPromo jamber = (JamPromo) this.mJamBer.get(position);
        String harga = StringUtils.toRupiahFormat(String.valueOf(jamber.harga_tiket));
        String primary = "Pukul <font color='#FF9800'><b>" + jamber.jam + "</b></font> " + "Tersedia <font color='#FF9800'><b>" + jamber.kursi + "</b></font> Kursi";
        viewHolder.mJamBer = jamber;
        viewHolder.mPrimary.setText(Html.fromHtml(primary), BufferType.SPANNABLE);
        int hargaPotongan;
        String hargaPotonganToRupiah;
        String hargaNormal;
        String hargaPromo;
        if(jamber.potongan!=0){
            hargaPotongan = jamber.harga_tiket - jamber.potongan;
            hargaPotonganToRupiah = StringUtils.toRupiahFormat(String.valueOf(hargaPotongan));
            hargaNormal = "<font color='#f44336'><strike>" + harga + "</strike></font>";
            hargaPromo = "<font color='#0e707b'><b>" + hargaPotonganToRupiah + "</b></font>";
            viewHolder.mSecondary.setText(Html.fromHtml(hargaNormal), BufferType.SPANNABLE);
            viewHolder.mThird.setText(Html.fromHtml(hargaPromo), BufferType.SPANNABLE);
            viewHolder.mSecondary.setPaintFlags(viewHolder.mSecondary.getPaintFlags() | 16);
            viewHolder.mSale.setImageResource(R.drawable.ic_sale);
        }else{

        }

    }


    public int getItemCount() {
        return this.mJamBer.size();
    }
}
