package app.adie.reservation.stickyheadersrecyclerview;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

;

public interface StickyRecyclerHeadersAdapter<VH extends ViewHolder> {
  long getHeaderId(int i);

  int getItemCount();

  void onBindHeaderViewHolder(VH vh, int i);

  VH onCreateHeaderViewHolder(ViewGroup viewGroup);
}
