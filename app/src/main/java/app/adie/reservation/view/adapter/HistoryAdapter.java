package app.adie.reservation.view.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import java.util.List;

import app.adie.reservation.entity.DisplayableItem;
import app.adie.reservation.view.widget.AdapterDelegatesManager;

public class HistoryAdapter extends Adapter {
    private AdapterDelegatesManager<List<DisplayableItem>> delegatesManager = new AdapterDelegatesManager();
    private List<DisplayableItem> items;

    public HistoryAdapter(FragmentActivity activity, List<DisplayableItem> items) {
        this.items = items;
        this.delegatesManager.addDelegate(new HistoryUnpaidAdapter(activity, 0));
        this.delegatesManager.addDelegate(new HistoryPaidAdapter(activity, 1));
        this.delegatesManager.addDelegate(new HistoryInProgressAdapter(activity, 2));
        this.delegatesManager.addDelegate(new HistoryCancelAdapter(activity, 3));
        this.delegatesManager.addDelegate(new HistoryHabisAdapter(activity, 4));
        this.delegatesManager.addDelegate(new HistoryRejectAdapter(activity, 5));

    }

    public int getItemViewType(int position) {
        return this.delegatesManager.getItemViewType(this.items, position);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return this.delegatesManager.onCreateViewHolder(parent, viewType);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        this.delegatesManager.onBindViewHolder(this.items, position, holder);
    }

    public int getItemCount() {
        return this.items.size();
    }
}
