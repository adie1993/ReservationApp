package app.adie.reservation.view.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;
import app.adie.reservation.activity.PesanTiket;
import app.adie.reservation.R;
import app.adie.reservation.entity.Jurusan;
import app.adie.reservation.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
/**
 * Created by Adie on 15/04/2016.
 */
public class JurusanAdapter extends RecyclerArrayAdapter<Jurusan, ViewHolder> implements StickyRecyclerHeadersAdapter<ViewHolder>{

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cabang, parent, false)) {
        };
    }


    public void onBindViewHolder(ViewHolder holder,  final int i) {
        TextView mName = (TextView) holder.itemView.findViewById(R.id.primary);
        TextView mKode = (TextView) holder.itemView.findViewById(R.id.secondary);
        mName.setText(((Jurusan) getItem(i)).nama);
        mKode.setText(((Jurusan) getItem(i)).kode);
        holder.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PesanTiket.getInstance().getJurusanFromFragmentContainer((Jurusan)JurusanAdapter.this.getItem(i));
            }
        });
    }


    public long getHeaderId(int position) {
        return Long.parseLong(((Jurusan) getItem(position)).kota, 36);
    }


    public ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header, parent, false)) {
        };
    }


    public void onBindHeaderViewHolder(ViewHolder holder, int position) {
        TextView mName = (TextView) holder.itemView.findViewById(R.id.primary);
        mName.setText(((Jurusan) getItem(position)).kota);
    }
}
