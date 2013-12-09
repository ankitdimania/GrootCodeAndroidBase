package com.grootcode.base.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.grootcode.base.R;

public abstract class FragmentPagerSlidingTabStrip extends Fragment {
    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_sliding_pager_position";

    protected PagerSlidingTabStrip mSlidingTabStrip;
    private ViewPager mSlidingTabPager;
    private FragmentPagerAdapter mPagerAdapter;

    protected int mCurrentSelectedPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }
    }

    /**
     * Create the view for this fragment, using the arguments given to it.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pager_sliding_tab_strip, container, false);

        mSlidingTabStrip = (PagerSlidingTabStrip) root.findViewById(R.id.sliding_tab_strip);
        mSlidingTabPager = (ViewPager) root.findViewById(R.id.sliding_tab_pager);
        mPagerAdapter = createFragmentPagerAdapter();

        mSlidingTabPager.setAdapter(mPagerAdapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        mSlidingTabPager.setPageMargin(pageMargin);

        mSlidingTabStrip.setViewPager(mSlidingTabPager);

        mSlidingTabPager.setCurrentItem(mCurrentSelectedPosition);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int currentItem = mSlidingTabPager.getCurrentItem();
        outState.putInt(STATE_SELECTED_POSITION, currentItem);
    }

    public ViewPager getViewPager() {
        return mSlidingTabPager;
    }

    public FragmentPagerAdapter getPagerAdapter() {
        return mPagerAdapter;
    }

    protected void notifyDataSetChanged() {
        mPagerAdapter = createFragmentPagerAdapter();

        mSlidingTabPager.setAdapter(mPagerAdapter);
        mSlidingTabStrip.notifyDataSetChanged();
        mPagerAdapter.notifyDataSetChanged();
        mSlidingTabPager.setCurrentItem(mCurrentSelectedPosition);
    }

    public abstract FragmentPagerAdapter createFragmentPagerAdapter();
}
