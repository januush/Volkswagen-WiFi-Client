package com.example.kfgclient.ui.main;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ScrollView;

/**
 * Implements a listener which can drag&drop an object view when touching it.
 */
public class ViewTouchDragListener implements OnTouchListener {

    /**
     * Start x-position before moving the view.
     */
    private float startX;

    /**
     * Start y-position before moving the view.
     */
    private float startY;

    /**
     * Original x-position of draggable view.
     */
    private float origX;

    /**
     * Original y-position of draggable view.
     */
    private float origY;

    /**
     * View that should be draggable
     */
    private ScrollView containerScrollView;

    /**
     * Constructor.
     *
     * @param containerScrollView view that will be draggable
     */
    public ViewTouchDragListener(ScrollView containerScrollView) {
        this.containerScrollView = containerScrollView;
    }

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view. Implements drag&drop principle during action down, up and move.
     *
     * @param view The view the touch event has been dispatched to.
     * @param me   The MotionEvent object containing full information about
     *             the event.
     * @return {@code true} if the listener has consumed the event, {@code false} otherwise.
     */
    @Override
    public boolean onTouch(View view, MotionEvent me) {

        if (null != containerScrollView) {
            containerScrollView.requestDisallowInterceptTouchEvent(true);
        }

        float distX = me.getRawX() - startX;
        float distY = me.getRawY() - startY;
        float targetX = origX + distX;
        float targetY = origY + distY;

        LayoutParams flp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        switch (me.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = me.getRawX();
                startY = me.getRawY();

                ViewGroup.MarginLayoutParams vlp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                origY = vlp.topMargin;
                origX = vlp.leftMargin;

                view.bringToFront();

                // Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
                // Canvas c = new Canvas(b);
                // view.draw(c);
                return true;

            case MotionEvent.ACTION_UP:

                flp.setMargins((int) targetX, (int) targetY, 0, 0);
                view.setLayoutParams(flp);

                return true;

            case MotionEvent.ACTION_MOVE:

                flp.setMargins((int) targetX, (int) targetY, 0, 0);
                view.setLayoutParams(flp);
                return true;
            default:
                return false;
        }

    }
}
