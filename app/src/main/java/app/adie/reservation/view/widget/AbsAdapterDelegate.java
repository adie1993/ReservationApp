package app.adie.reservation.view.widget;

public abstract class AbsAdapterDelegate<T> implements AdapterDelegate<T> {
    protected int viewType;

    public AbsAdapterDelegate(int viewType) {
        this.viewType = viewType;
    }

    public int getItemViewType() {
        return this.viewType;
    }
}
