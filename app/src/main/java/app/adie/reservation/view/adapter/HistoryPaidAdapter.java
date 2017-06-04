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

import app.adie.reservation.activity.HistoryPaidActivity;
import app.adie.reservation.R;
import app.adie.reservation.entity.DisplayableItem;
import app.adie.reservation.entity.Paid;
import app.adie.reservation.utils.DateUtils;
import app.adie.reservation.view.widget.AbsAdapterDelegate;

public class HistoryPaidAdapter extends AbsAdapterDelegate<List<DisplayableItem>> {
    private LayoutInflater inflater;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
        public TextView mCabang;
        public TextView mKeberangkatan;
        public ImageView mLogo;
        public Paid mPaid;
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
            Intent intent = new Intent(v.getContext(), HistoryPaidActivity.class);
            intent.putExtra("asal", mPaid.cabangAsal);
            intent.putExtra("tujuan", mPaid.cabangTujuan);
            intent.putExtra("kode", mPaid.kodebook);
            intent.putExtra("kodetiket", mPaid.kodetiket);
            intent.putExtra("nama", mPaid.nama_penumpang);
            intent.putExtra("no_seat", mPaid.no_seat);
            intent.putExtra("tgl", mPaid.tanggalBerangkat);
            intent.putExtra("jam", mPaid.jamBerangkat);
            intent.putExtra("harga", mPaid.harga);
            intent.putExtra("total", mPaid.total);
            intent.putExtra("id_pem",mPaid.id_pemesanan);
            v.getContext().startActivity(intent);
        }
    }

    public HistoryPaidAdapter(FragmentActivity activity, int viewType) {
        super(viewType);
        this.inflater = activity.getLayoutInflater();
    }

    public boolean isForViewType(@NonNull List<DisplayableItem> items, int position) {
        return items.get(position) instanceof Paid;
    }

    @NonNull
    public android.support.v7.widget.RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(this.inflater.inflate(R.layout.list_item_history_paid, parent, false));
    }

    public void onBindViewHolder(@NonNull List<DisplayableItem> items, int position, @NonNull android.support.v7.widget.RecyclerView.ViewHolder holder) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Paid paid = (Paid) items.get(position);
        viewHolder.mPaid = paid;
        viewHolder.mCabang.setText(paid.cabangAsal + " → " + paid.cabangTujuan);
        viewHolder.mKeberangkatan.setText(DateUtils.getStringDate(paid.tanggalBerangkat) + " " + paid.jamBerangkat);
    }
}
