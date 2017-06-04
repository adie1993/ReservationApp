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

import app.adie.reservation.activity.HistoryCancelActivity;
import app.adie.reservation.R;
import app.adie.reservation.entity.Cancel;
import app.adie.reservation.entity.DisplayableItem;
import app.adie.reservation.utils.DateUtils;
import app.adie.reservation.view.widget.AbsAdapterDelegate;

public class HistoryCancelAdapter extends AbsAdapterDelegate<List<DisplayableItem>> {
    private LayoutInflater inflater;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
        public TextView mCabang;
        public TextView mKeberangkatan;
        public ImageView mLogo;
        public Cancel mCancel;
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
            Intent intent = new Intent(v.getContext(), HistoryCancelActivity.class);
            intent.putExtra("asal", mCancel.cabangAsal);
            intent.putExtra("tujuan", mCancel.cabangTujuan);
            intent.putExtra("kode", mCancel.kodebook);
            intent.putExtra("kodetiket", mCancel.kodetiket);
            intent.putExtra("uangkem", mCancel.uangkem);
            intent.putExtra("nama", mCancel.nama_penumpang);
            intent.putExtra("no_seat", mCancel.no_seat);
            intent.putExtra("tgl", mCancel.tanggalBerangkat);
            intent.putExtra("jam", mCancel.jamBerangkat);
            intent.putExtra("harga", mCancel.harga);
            intent.putExtra("diskon", mCancel.diskon);
            intent.putExtra("total", mCancel.total);
            intent.putExtra("id_pem",mCancel.id_pemesanan);
            v.getContext().startActivity(intent);
        }
    }

    public HistoryCancelAdapter(FragmentActivity activity, int viewType) {
        super(viewType);
        this.inflater = activity.getLayoutInflater();
    }

    public boolean isForViewType(@NonNull List<DisplayableItem> items, int position) {
        return items.get(position) instanceof Cancel;
    }

    @NonNull
    public android.support.v7.widget.RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(this.inflater.inflate(R.layout.list_item_history_cancel, parent, false));
    }

    public void onBindViewHolder(@NonNull List<DisplayableItem> items, int position, @NonNull android.support.v7.widget.RecyclerView.ViewHolder holder) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Cancel cancel = (Cancel) items.get(position);
        viewHolder.mCancel = cancel;
        viewHolder.mCabang.setText(cancel.cabangAsal + " → " + cancel.cabangTujuan);
        viewHolder.mKeberangkatan.setText(DateUtils.getStringDate(cancel.tanggalBerangkat) + " " + cancel.jamBerangkat);
    }
}
