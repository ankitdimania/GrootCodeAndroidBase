package com.grootcode.base.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class FragmentRestorablePagerSlidingTabStrip extends FragmentPagerSlidingTabStrip {

    /**
     * Persist the current tab across application restarts.
     */
    private static final String PREF_CURRENT_SELECTED_POSITION = "sliding_tab_selected_position";

    /**
     * Create the view for this fragment, using the arguments given to it.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);

        restoreCurrentSelectedPosition();

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        int currentSelectedPosition = getViewPager().getCurrentItem();
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        sp.edit().putInt(PREF_CURRENT_SELECTED_POSITION + getClass().getSimpleName(), currentSelectedPosition).commit();
    }

    @Override
    protected void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        restoreCurrentSelectedPosition();
    }

    private void restoreCurrentSelectedPosition() {
        // Read in the flag indicating the value of currently selected tab fragment.
        // See PREF_CURRENT_SELECTED_POSITION for details.
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        int currentSelectedPosition = sp.getInt(PREF_CURRENT_SELECTED_POSITION + getClass().getSimpleName(), 0);
        getViewPager().setCurrentItem(currentSelectedPosition);
    }
}
