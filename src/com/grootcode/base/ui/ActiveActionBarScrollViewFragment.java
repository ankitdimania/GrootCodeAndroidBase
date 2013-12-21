package com.grootcode.base.ui;

import static com.grootcode.android.util.LogUtils.makeLogTag;
import roboguice.fragment.RoboFragment;
import android.annotation.TargetApi;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.grootcode.android.ui.widget.ObservableScrollView.Callbacks;
import com.grootcode.android.util.UIUtils;
import com.grootcode.base.R;

public class ActiveActionBarScrollViewFragment extends RoboFragment implements Callbacks, OnTouchListener,
        GestureDetector.OnGestureListener {
    @SuppressWarnings("unused") private static String TAG = makeLogTag(ActiveActionBarScrollViewFragment.class);

    private static final Drawable TRANSPARENT = new ColorDrawable(Color.TRANSPARENT);

    private final Handler mHandler = new Handler();
    private final Drawable.Callback drawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            getActionBar().setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            mHandler.postAtTime(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
            mHandler.removeCallbacks(what);
        }
    };

    private TransitionDrawable td;
    private GestureDetectorCompat mDetector;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TypedValue typedValue = new TypedValue();
        int[] backgroundAttr;
        if (UIUtils.hasHoneycomb()) {
            getActivity().getTheme().resolveAttribute(android.R.attr.actionBarStyle, typedValue, true);
            backgroundAttr = new int[] {
                android.R.attr.background
            };
        } else {
            getActivity().getTheme().resolveAttribute(R.attr.actionBarStyle, typedValue, true);
            backgroundAttr = new int[] {
                R.attr.background
            };
        }

        int indexOfAttrBackground = 0;
        TypedArray a = getActivity().obtainStyledAttributes(typedValue.data, backgroundAttr);
        Drawable actionBarBackground = a.getDrawable(indexOfAttrBackground);
        a.recycle();

        td = new TransitionDrawable(new Drawable[] {
                actionBarBackground,
                TRANSPARENT,
        });
        td.setCrossFadeEnabled(true);

        mDetector = new GestureDetectorCompat(getActivity(), this);
    }

    @Override
    public void onResume() {
        super.onResume();

        // workaround for broken ActionBarContainer drawable handling on pre-API 17 builds
        // https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
        if (!UIUtils.hasJellyBeanMR1()) {
            td.setCallback(drawableCallback);
        } else {
            getActionBar().setBackgroundDrawable(td);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Prevents null pointer by not storing this activity context into action bar
        getActionBar().setBackgroundDrawable(td.getDrawable(0));
    }

    protected void onLoadComplete() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isVisible()) {
                    actionBar();
                }
            }
        }, 1000);
    }

    private final Runnable mHideActionBarRunnable = new Runnable() {
        @Override
        public void run() {
            if (isVisible()) {
                hideActionBar();
            }
        }
    };

    private void actionBar() {
        mHandler.postDelayed(mHideActionBarRunnable, 700);
        setActionBarTransparent();
    }

    protected void onHideActionBar() {}

    protected void onShowActionBar() {}

    private boolean actionBarSolid = true;

    private void setActionBarSolid() {
        if (!actionBarSolid) {
            td.reverseTransition(500);

            // http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
            getActionBar().setDisplayShowTitleEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(true);

            actionBarSolid = true;
        }
    }

    private void setActionBarTransparent() {
        if (actionBarSolid) {
            td.startTransition(500);

            // http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
            getActionBar().setDisplayShowTitleEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(true);

            actionBarSolid = false;
        }
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    private void showActionBar() {
        if (!getActionBar().isShowing()) {
            getActionBar().show();
            onShowActionBar();
        }
    }

    private void hideActionBar() {
        if (getActionBar().isShowing()) {
            getActionBar().hide();
            onHideActionBar();
        }
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (t <= 3) {
            setActionBarTransparent();
        } else {
            setActionBarSolid();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return this.mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {}

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (e1.getY() - e2.getY() < -50) {
            showActionBar();
            mHandler.removeCallbacks(mHideActionBarRunnable);
            mHandler.postDelayed(mHideActionBarRunnable, 4000);
        } else if (distanceY > 0) {
            hideActionBar();
            mHandler.removeCallbacks(mHideActionBarRunnable);
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {}

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
