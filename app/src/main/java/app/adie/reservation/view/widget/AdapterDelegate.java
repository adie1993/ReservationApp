package app.adie.reservation.view.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

public interface AdapterDelegate<T> {
    int getItemViewType();

    boolean isForViewType(@NonNull T t, int i);

    void onBindViewHolder(@NonNull T t, int i, @NonNull ViewHolder viewHolder);

    @NonNull
    ViewHolder onCreateViewHolder(ViewGroup viewGroup);
}
