package app.adie.reservation.view.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.adie.reservation.activity.HistoryInProgressActivity;
import app.adie.reservation.R;
import app.adie.reservation.entity.DisplayableItem;
import app.adie.reservation.entity.InProgress;
import app.adie.reservation.utils.DateUtils;
import app.adie.reservation.view.widget.AbsAdapterDelegate;


public class HistoryInProgressAdapter extends AbsAdapterDelegate<List<DisplayableItem>> {
    private LayoutInflater inflater;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
        public TextView mCabang;
        public TextView mKeberangkatan;
        public TextView mStatus;
        public InProgress mInProg;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.mCabang = (TextView) itemLayoutView.findViewById(R.id.cabang);
            this.mKeberangkatan = (TextView) itemLayoutView.findViewById(R.id.keberangkatan);
            this.mStatus = (TextView) itemLayoutView.findViewById(R.id.status_pembayaran);

            itemLayoutView.setOnClickListener(this);
        }

        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), HistoryInProgressActivity.class);
            intent.putExtra("asal", mInProg.cabangAsal);
            intent.putExtra("tujuan", mInProg.cabangTujuan);
            intent.putExtra("kode", mInProg.kodebook);
            intent.putExtra("nama", mInProg.nama_penumpang);
            intent.putExtra("no_seat", mInProg.no_seat);
            intent.putExtra("bukti", mInProg.buktitransfer);
            intent.putExtra("tgl", mInProg.tanggalBerangkat);
            intent.putExtra("jam", mInProg.jamBerangkat);
            intent.putExtra("harga", mInProg.harga);
            intent.putExtra("diskon", mInProg.diskon);
            intent.putExtra("total", mInProg.total);
            intent.putExtra("id_pem",mInProg.id_pemesanan);
            v.getContext().startActivity(intent);
        }
    }

    public HistoryInProgressAdapter(FragmentActivity activity, int viewType) {
        super(viewType);
        this.inflater = activity.getLayoutInflater();
    }

    public boolean isForViewType(@NonNull List<DisplayableItem> items, int position) {
        return items.get(position) instanceof InProgress;
    }

    @NonNull
    public android.support.v7.widget.RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(this.inflater.inflate(R.layout.list_item_history_progress, parent, false));
    }

    public void onBindViewHolder(@NonNull List<DisplayableItem> items, int position, @NonNull android.support.v7.widget.RecyclerView.ViewHolder holder) {
        ViewHolder viewHolder = (ViewHolder) holder;
        InProgress inProgress = (InProgress) items.get(position);
        viewHolder.mInProg = inProgress;
        viewHolder.mCabang.setText(inProgress.cabangAsal + " → " + inProgress.cabangTujuan);
        viewHolder.mKeberangkatan.setText(DateUtils.getStringDate(inProgress.tanggalBerangkat) + " " + inProgress.jamBerangkat);
    }
}
