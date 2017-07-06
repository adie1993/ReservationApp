package app.adie.reservation.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Date;
import java.util.List;

import app.adie.reservation.activity.CalendarPromoActivity;
import app.adie.reservation.R;
import app.adie.reservation.entity.PromoTiket;
import app.adie.reservation.utils.DateUtils;
import app.adie.reservation.utils.StringUtils;



public class PromoAdapter extends Adapter<PromoAdapter.ViewHolder> {
    private Context mContext;
    private List<PromoTiket> mPromoTikets;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
        public TextView mCabangAsal;
        public TextView mCabangTujuan;
        public TextView mHargaNormal;
        public TextView mHargaPotongan;
        public TextView mHargaPromo;
        public TextView mKeterangan;
        public ImageView mLogo;
        public PromoTiket mPromoTiket;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.mCabangAsal = (TextView) itemLayoutView.findViewById(R.id.cabang_asal);
            this.mCabangTujuan = (TextView) itemLayoutView.findViewById(R.id.cabang_tujuan);
            this.mKeterangan = (TextView) itemLayoutView.findViewById(R.id.keterangan);
            this.mHargaNormal = (TextView) itemLayoutView.findViewById(R.id.harga_normal);
            this.mHargaPotongan = (TextView) itemLayoutView.findViewById(R.id.harga_potongan);
            this.mHargaPromo = (TextView) itemLayoutView.findViewById(R.id.harga_promo);
            this.mLogo = (ImageView) itemLayoutView.findViewById(R.id.imageView3);
            itemLayoutView.setOnClickListener(this);
        }

        public void onClick(View v) {
            searchTicket(v);
        }

        private void searchTicket(View v) {
            try {
                Intent intent = new Intent(v.getContext(), CalendarPromoActivity.class);
                intent.putExtra("asal", mPromoTiket.asal);
                intent.putExtra("tujuan", mPromoTiket.tujuan);
                intent.putExtra("kode", mPromoTiket.kode);
                intent.putExtra("tgl_akhir", mPromoTiket.tanggal);
                v.getContext().startActivity(intent);
            } catch (Exception e) {

            }
        }
    }

    public PromoAdapter(Context context, List<PromoTiket> promoTikets) {
        this.mContext = context;
        this.mPromoTikets = promoTikets;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_promo, null));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mPromoTiket = (PromoTiket) this.mPromoTikets.get(position);
        int harga_promo = viewHolder.mPromoTiket.harga - viewHolder.mPromoTiket.potongan;
        Date tgl_akhir = DateUtils.stringToDate(viewHolder.mPromoTiket.tanggal);
        String tgl = DateUtils.getDateFormatCard(tgl_akhir);
        String gambar = viewHolder.mPromoTiket.foto;
        String fix = "http://10.0.2.2/kraka/"+gambar;
        Glide.with(mContext).load(fix).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolder.mLogo);
        viewHolder.mCabangAsal.setText(viewHolder.mPromoTiket.asal);
        viewHolder.mCabangTujuan.setText(viewHolder.mPromoTiket.tujuan);
        viewHolder.mKeterangan.setText("Promo Ini Berakhir Pada " + tgl);
        viewHolder.mHargaNormal.setText("Harga Normal" + " " + StringUtils.toRupiahFormat(viewHolder.mPromoTiket.harga));
        viewHolder.mHargaPotongan.setText("Diskon" + " " + StringUtils.toRupiahFormat(viewHolder.mPromoTiket.potongan));
        viewHolder.mHargaPromo.setText("" + StringUtils.toRupiahFormat(harga_promo));
    }

    public int getItemCount() {
        return this.mPromoTikets.size();
    }
}
