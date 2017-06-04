package app.adie.reservation.view.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.adie.reservation.activity.HistoryHabisActivity;
import app.adie.reservation.R;
import app.adie.reservation.entity.DisplayableItem;
import app.adie.reservation.entity.Habis;
import app.adie.reservation.utils.DateUtils;
import app.adie.reservation.view.widget.AbsAdapterDelegate;

public class HistoryHabisAdapter extends AbsAdapterDelegate<List<DisplayableItem>> {
    private LayoutInflater inflater;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
        public TextView mCabang;
        public TextView mKeberangkatan;
        public ImageView mLogo;
        public Habis mHabis;
        public TextView mStatus;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.mCabang = (TextView) itemLayoutView.findViewById(R.id.cabang);
            this.mKeberangkatan = (TextView) itemLayoutView.findViewById(R.id.keberangkatan);
            this.mStatus = (TextView) itemLayoutView.findViewById(R.id.status_pembayaran);
            this.mLogo = (ImageView) itemLayoutView.findViewById(R.id.logo_agen);
            itemLayoutView.setOnClickListener(this);
        }

        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), HistoryHabisActivity.class);
            intent.putExtra("asal", mHabis.cabangAsal);
            intent.putExtra("tujuan", mHabis.cabangTujuan);
            intent.putExtra("kode", mHabis.kodebook);
            intent.putExtra("nama", mHabis.nama_penumpang);
            intent.putExtra("no_seat", mHabis.no_seat);
            intent.putExtra("tgl", mHabis.tanggalBerangkat);
            intent.putExtra("jam", mHabis.jamBerangkat);
            intent.putExtra("harga", mHabis.harga);
            intent.putExtra("diskon", mHabis.diskon);
            intent.putExtra("total", mHabis.total);
            intent.putExtra("id_pem",mHabis.id_pemesanan);
            v.getContext().startActivity(intent);
        }
    }

    public HistoryHabisAdapter(FragmentActivity activity, int viewType) {
        super(viewType);
        this.inflater = activity.getLayoutInflater();
    }

    public boolean isForViewType(@NonNull List<DisplayableItem> items, int position) {
        return items.get(position) instanceof Habis;
    }

    @NonNull
    public android.support.v7.widget.RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(this.inflater.inflate(R.layout.list_item_history_habis, parent, false));
    }

    public void onBindViewHolder(@NonNull List<DisplayableItem> items, int position, @NonNull android.support.v7.widget.RecyclerView.ViewHolder holder) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Habis habis = (Habis) items.get(position);
        viewHolder.mHabis = habis;
        viewHolder.mCabang.setText(habis.cabangAsal + " → " + habis.cabangTujuan);
        viewHolder.mKeberangkatan.setText(DateUtils.getStringDate(habis.tanggalBerangkat) + " " + habis.jamBerangkat);
    }
}
