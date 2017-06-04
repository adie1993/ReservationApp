package app.adie.reservation.view.widget;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

public class AdapterDelegatesManager<T> {
    SparseArrayCompat<AdapterDelegate<T>> delegates = new SparseArrayCompat();
    private AdapterDelegate<T> fallbackDelegate;

    public AdapterDelegatesManager<T> addDelegate(@NonNull AdapterDelegate<T> delegate) {
        return addDelegate(delegate, false);
    }

    public AdapterDelegatesManager<T> addDelegate(@NonNull AdapterDelegate<T> delegate, boolean allowReplacingDelegate) {
        if (delegate == null) {
            throw new NullPointerException("AdapterDelegate is null!");
        }
        int viewType = delegate.getItemViewType();
        if (this.fallbackDelegate != null && this.fallbackDelegate.getItemViewType() == viewType) {
            throw new IllegalArgumentException("Conflict: the passed AdapterDelegate has the same ViewType integer (value = " + viewType + ") as the fallback AdapterDelegate");
        } else if (allowReplacingDelegate || this.delegates.get(viewType) == null) {
            this.delegates.put(viewType, delegate);
            return this;
        } else {
            throw new IllegalArgumentException("An AdapterDelegate is already registered for the viewType = " + viewType + ". Already registered AdapterDelegate is " + this.delegates.get(viewType));
        }
    }

    public AdapterDelegatesManager<T> removeDelegate(@NonNull AdapterDelegate<T> delegate) {
        if (delegate == null) {
            throw new NullPointerException("AdapterDelegate is null");
        }
        AdapterDelegate<T> queried = (AdapterDelegate) this.delegates.get(delegate.getItemViewType());
        if (queried != null && queried == delegate) {
            this.delegates.remove(delegate.getItemViewType());
        }
        return this;
    }

    public AdapterDelegatesManager<T> removeDelegate(int viewType) {
        this.delegates.remove(viewType);
        return this;
    }

    public int getItemViewType(@NonNull T items, int position) {
        if (items == null) {
            throw new NullPointerException("Items datasource is null!");
        }
        int delegatesCount = this.delegates.size();
        for (int i = 0; i < delegatesCount; i++) {
            AdapterDelegate<T> delegate = (AdapterDelegate) this.delegates.valueAt(i);
            if (delegate.isForViewType(items, position)) {
                return delegate.getItemViewType();
            }
        }
        if (this.fallbackDelegate != null) {
            return this.fallbackDelegate.getItemViewType();
        }
        throw new IllegalArgumentException("No AdapterDelegate added that matches position=" + position + " in data source");
    }

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterDelegate<T> delegate = (AdapterDelegate) this.delegates.get(viewType);
        if (delegate == null) {
            if (this.fallbackDelegate == null) {
                throw new NullPointerException("No AdapterDelegate added for ViewType " + viewType);
            }
            delegate = this.fallbackDelegate;
        }
        ViewHolder vh = delegate.onCreateViewHolder(parent);
        if (vh != null) {
            return vh;
        }
        throw new NullPointerException("ViewHolder returned from AdapterDelegate " + delegate + " for ViewType =" + viewType + " is null!");
    }

    public void onBindViewHolder(@NonNull T items, int position, @NonNull ViewHolder viewHolder) {
        AdapterDelegate<T> delegate = (AdapterDelegate) this.delegates.get(viewHolder.getItemViewType());
        if (delegate == null) {
            if (this.fallbackDelegate == null) {
                throw new NullPointerException("No AdapterDelegate added for ViewType " + viewHolder.getItemViewType());
            }
            delegate = this.fallbackDelegate;
        }
        delegate.onBindViewHolder(items, position, viewHolder);
    }

    public AdapterDelegatesManager<T> setFallbackDelegate(@Nullable AdapterDelegate<T> fallbackDelegate) {
        if (fallbackDelegate != null) {
            int delegatesCount = this.delegates.size();
            int fallbackViewType = fallbackDelegate.getItemViewType();
            for (int i = 0; i < delegatesCount; i++) {
                AdapterDelegate<T> delegate = (AdapterDelegate) this.delegates.valueAt(i);
                if (delegate.getItemViewType() == fallbackViewType) {
                    throw new IllegalArgumentException("Conflict: The given fallback - delegate has the same ViewType integer (value = " + fallbackViewType + ")  as an already assigned AdapterDelegate " + delegate.getClass().getName());
                }
            }
        }
        this.fallbackDelegate = fallbackDelegate;
        return this;
    }
}
