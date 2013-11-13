package com.grootcode.base;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.grootcode.android.ui.SimpleSectionedListAdapter;
import com.grootcode.android.util.LogUtils;

public abstract class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final String TAG = LogUtils.makeLogTag(MainActivity.class);

    public static abstract class SlidingMenuItem {
        private String title;
        private String subTitle;
        private int iconRes;

        public SlidingMenuItem(String title, String subTitle, int iconRes) {
            this.title = title;
            this.subTitle = subTitle;
            this.iconRes = iconRes;
        }

        public SlidingMenuItem(String title, String subTitle) {
            this.title = title;
            this.subTitle = subTitle;
        }

        public String getTitle() {
            return title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public int getIconRes() {
            return iconRes;
        }

        public abstract Fragment getFragment();
    }

    private class MenuAdapter extends ArrayAdapter<SlidingMenuItem> {

        private class Holder {
            TextView title;
        }

        public MenuAdapter(Context context, SlidingMenuItem[] menuItems) {
            super(context, 0, menuItems);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_list_item, null);

                holder = new Holder();
                holder.title = (TextView) convertView;

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            SlidingMenuItem menuItem = getItem(position);
            if (TextUtils.isEmpty(menuItem.subTitle)) {
                holder.title.setText(menuItem.title);
            } else {
                holder.title.setText(menuItem.subTitle);
            }
            if (menuItem.getIconRes() != 0) {
                holder.title.setCompoundDrawablesWithIntrinsicBounds(menuItem.getIconRes(), 0, 0, 0);
            }

            return convertView;
        }
    }

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private SimpleSectionedListAdapter mSectionedAdapter;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private CharSequence mSubTitle;

    private SlidingMenuItem[] menuItems;
    private SimpleSectionedListAdapter.Section[] menuSections;

    public MainActivity(SlidingMenuItem[] menuItems, SimpleSectionedListAdapter.Section[] menuSections) {
        this.menuItems = menuItems;
        this.menuSections = menuSections;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSectionedAdapter = new SimpleSectionedListAdapter(this, R.layout.menu_list_item_header,
                new MenuAdapter(this, menuItems));
        mSectionedAdapter.setSections(menuSections);

        setContentView(R.layout.activity_base);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), mSectionedAdapter);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        selectItem(mSectionedAdapter.sectionedPositionToPosition(position));
    }

    private void selectItem(int position) {
        SlidingMenuItem slidingMenuItem = menuItems[position];

        // update the main content by replacing fragments
        Fragment newFragment = slidingMenuItem.getFragment();

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment)
                .commit();

        // update selected item and title, then close the drawer
        mTitle = slidingMenuItem.getTitle();
        mSubTitle = slidingMenuItem.getSubTitle();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(mTitle);
        actionBar.setSubtitle(mSubTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_base, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
//            ((MainActivity) activity).onSectionAttached(
//                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
