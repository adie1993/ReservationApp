package app.adie.reservation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.adie.reservation.R;
import app.adie.reservation.entity.KursiDetil;


public class KursiAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;

    private ArrayList<KursiDetil> mListKursi;

    static class ViewHolder {
        ImageView kursi;
        TextView nomor_kursi;

        ViewHolder() {
        }
    }

    public KursiAdapter(Context context, ArrayList<KursiDetil> listKursi) {
        this.mContext = context;
        this.mListKursi = listKursi;
        this.mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return this.mListKursi.size();
    }

    public Object getItem(int position) {
        return this.mListKursi.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = this.mInflater.inflate(R.layout.grid_item_kursi, null);
            holder = new ViewHolder();
            holder.kursi = (ImageView) convertView.findViewById(R.id.kursi);
            holder.nomor_kursi = (TextView) convertView.findViewById(R.id.nomor_kursi);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        KursiDetil kd = (KursiDetil) this.mListKursi.get(position);
        holder.nomor_kursi.setText(kd.no);
        holder.kursi.setImageResource(R.drawable.kursi_selector);
        if (kd.type.equals("S")) {
            holder.kursi.setImageResource(R.drawable.ic_kursi_driver);
        } else if (kd.type.equals("K")) {
            holder.kursi.setImageResource(R.drawable.ic_kursi_noseat);
        } else if (kd.type.equals("B")) {
            holder.kursi.setImageResource(R.drawable.ic_kursi_sold);
        }
        return convertView;
    }
}
