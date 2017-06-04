package app.adie.reservation.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class DividerItemDecorationPadding extends ItemDecoration {
    private static final int[] ATTRS = new int[]{16843284};
    private Drawable mDivider;

    public DividerItemDecorationPadding(Context context) {
        TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        this.mDivider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    public DividerItemDecorationPadding(Context context, int resId) {
        this.mDivider = ContextCompat.getDrawable(context, resId);
    }

    public void onDrawOver(Canvas c, RecyclerView parent, State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int top = child.getBottom() + ((LayoutParams) child.getLayoutParams()).bottomMargin;
            this.mDivider.setBounds(left, top, right, top + this.mDivider.getIntrinsicHeight());
            this.mDivider.draw(c);
        }
    }
}
