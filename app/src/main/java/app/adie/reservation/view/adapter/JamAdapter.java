package app.adie.reservation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import java.util.ArrayList;

import app.adie.reservation.activity.PesanTiket;
import app.adie.reservation.R;
import app.adie.reservation.entity.JamBer;
import app.adie.reservation.utils.StringUtils;

public class JamAdapter extends Adapter<JamAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<JamBer> mJamBer;



    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
        public JamBer mJamBer;
        public TextView mPrimary;
        public TextView mSecondary;
        public TextView mThird;
        public ImageView mSale;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.mPrimary = (TextView) itemLayoutView.findViewById(R.id.primary);
            this.mSecondary = (TextView) itemLayoutView.findViewById(R.id.secondary);
            this.mThird = (TextView) itemLayoutView.findViewById(R.id.third);
            this.mSale = (ImageView) itemLayoutView.findViewById(R.id.sale);
            itemLayoutView.setOnClickListener(this);
        }

        public void onClick(View v) {
            PesanTiket.getInstance().getJamFromFragmentContainer(this.mJamBer);
        }
    }
        public JamAdapter(Context context, ArrayList<JamBer> jamber) {
            this.mContext = context;
            this.mJamBer = jamber;
        }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_jam, null));
    }

   @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        JamBer jamber = (JamBer) this.mJamBer.get(position);
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
            viewHolder.mSecondary.setText(Html.fromHtml("<font color='#0e707b'>" + harga + "</b></font>"), BufferType.SPANNABLE);
            viewHolder.mThird.setText("");
        }

    }


    public int getItemCount() {
        return this.mJamBer.size();
    }
}
