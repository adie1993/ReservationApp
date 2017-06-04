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

import app.adie.reservation.activity.HistoryDetailActivity;
import app.adie.reservation.R;
import app.adie.reservation.entity.DisplayableItem;
import app.adie.reservation.entity.Unpaid;
import app.adie.reservation.utils.DateUtils;
import app.adie.reservation.view.widget.AbsAdapterDelegate;


public class HistoryUnpaidAdapter extends AbsAdapterDelegate<List<DisplayableItem>> {
    private LayoutInflater inflater;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
        public TextView mCabang;
        public TextView mKeberangkatan;
        public TextView mStatus;
        public Unpaid mUnpaid;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.mCabang = (TextView) itemLayoutView.findViewById(R.id.cabang);
            this.mKeberangkatan = (TextView) itemLayoutView.findViewById(R.id.keberangkatan);
            this.mStatus = (TextView) itemLayoutView.findViewById(R.id.status_pembayaran);

            itemLayoutView.setOnClickListener(this);
        }

        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), HistoryDetailActivity.class);
            intent.putExtra("asal", mUnpaid.cabangAsal);
            intent.putExtra("tujuan", mUnpaid.cabangTujuan);
            intent.putExtra("kode", mUnpaid.kodebook);
            intent.putExtra("nama", mUnpaid.nama_penumpang);
            intent.putExtra("no_seat", mUnpaid.no_seat);
            intent.putExtra("batas", mUnpaid.batas_waktu);
            intent.putExtra("tgl", mUnpaid.tanggalBerangkat);
            intent.putExtra("jam", mUnpaid.jamBerangkat);
            intent.putExtra("harga", mUnpaid.harga);
            intent.putExtra("diskon", mUnpaid.diskon);
            intent.putExtra("total", mUnpaid.total);
            intent.putExtra("id_pem",mUnpaid.id_pemesanan);
            v.getContext().startActivity(intent);
        }
    }

    public HistoryUnpaidAdapter(FragmentActivity activity, int viewType) {
        super(viewType);
        this.inflater = activity.getLayoutInflater();
    }

    public boolean isForViewType(@NonNull List<DisplayableItem> items, int position) {
        return items.get(position) instanceof Unpaid;
    }

    @NonNull
    public android.support.v7.widget.RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(this.inflater.inflate(R.layout.list_item_history_unpaid, parent, false));
    }

    public void onBindViewHolder(@NonNull List<DisplayableItem> items, int position, @NonNull android.support.v7.widget.RecyclerView.ViewHolder holder) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Unpaid unpaid = (Unpaid) items.get(position);
        viewHolder.mUnpaid = unpaid;
        viewHolder.mCabang.setText(unpaid.cabangAsal + " → " + unpaid.cabangTujuan);
        viewHolder.mKeberangkatan.setText(DateUtils.getStringDate(unpaid.tanggalBerangkat) + " " + unpaid.jamBerangkat);
    }
}
