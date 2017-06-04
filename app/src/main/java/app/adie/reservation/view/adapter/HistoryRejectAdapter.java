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

import app.adie.reservation.activity.HistoryRejectActivity;
import app.adie.reservation.R;
import app.adie.reservation.entity.DisplayableItem;
import app.adie.reservation.entity.Rejected;
import app.adie.reservation.utils.DateUtils;
import app.adie.reservation.view.widget.AbsAdapterDelegate;

public class HistoryRejectAdapter extends AbsAdapterDelegate<List<DisplayableItem>> {
    private LayoutInflater inflater;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
        public TextView mCabang;
        public TextView mKeberangkatan;
        public ImageView mLogo;
        public Rejected mReject;
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
            Intent intent = new Intent(v.getContext(), HistoryRejectActivity.class);
            intent.putExtra("asal", mReject.cabangAsal);
            intent.putExtra("tujuan", mReject.cabangTujuan);
            intent.putExtra("kode", mReject.kodebook);
            intent.putExtra("nama", mReject.nama_penumpang);
            intent.putExtra("no_seat", mReject.no_seat);
            intent.putExtra("tgl", mReject.tanggalBerangkat);
            intent.putExtra("jam", mReject.jamBerangkat);
            intent.putExtra("harga", mReject.harga);
            intent.putExtra("diskon", mReject.diskon);
            intent.putExtra("total", mReject.total);
            intent.putExtra("id_pem",mReject.id_pemesanan);
            v.getContext().startActivity(intent);
        }
    }

    public HistoryRejectAdapter(FragmentActivity activity, int viewType) {
        super(viewType);
        this.inflater = activity.getLayoutInflater();
    }

    public boolean isForViewType(@NonNull List<DisplayableItem> items, int position) {
        return items.get(position) instanceof Rejected;
    }

    @NonNull
    public android.support.v7.widget.RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(this.inflater.inflate(R.layout.list_item_history_reject, parent, false));
    }

    public void onBindViewHolder(@NonNull List<DisplayableItem> items, int position, @NonNull android.support.v7.widget.RecyclerView.ViewHolder holder) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Rejected rejected = (Rejected) items.get(position);
        viewHolder.mReject = rejected;
        viewHolder.mCabang.setText(rejected.cabangAsal + " → " + rejected.cabangTujuan);
        viewHolder.mKeberangkatan.setText(DateUtils.getStringDate(rejected.tanggalBerangkat) + " " + rejected.jamBerangkat);
    }
}
