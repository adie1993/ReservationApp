package app.adie.reservation.view.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public abstract class RecyclerArrayAdapter<M, VH extends ViewHolder> extends Adapter<VH> {
    private ArrayList<M> items = new ArrayList();

    public RecyclerArrayAdapter() {
        setHasStableIds(true);
    }

    public void add(M object) {
        this.items.add(object);
        notifyDataSetChanged();
    }

    public void add(int index, M object) {
        this.items.add(index, object);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends M> collection) {
        if (collection != null) {
            this.items.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void addAll(M... items) {
        addAll(Arrays.asList(items));
    }

    public void clear() {
        this.items.clear();
        notifyDataSetChanged();
    }

    public void remove(M object) {
        this.items.remove(object);
        notifyDataSetChanged();
    }

    public M getItem(int position) {
        return this.items.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return this.items.size();
    }
}
