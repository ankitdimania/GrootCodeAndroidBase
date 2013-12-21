package com.grootcode.base.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.grootcode.android.ui.SimpleSectionedListAdapter;
import com.grootcode.base.R;

public abstract class MainActivity extends BaseActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    public static class SlidingMenuItem {
        private final String title;
        private final String subTitle;
        private final Fragment fragment;
        private final int iconRes;

        public SlidingMenuItem(String title, Fragment fragment) {
            this(title, null, fragment);
        }

        public SlidingMenuItem(String title, String subTitle, Fragment fragment) {
            this(title, subTitle, fragment, 0);
        }

        public SlidingMenuItem(String title, String subTitle, Fragment fragment, int iconRes) {
            this.title = title;
            this.subTitle = subTitle;
            this.fragment = fragment;
            this.iconRes = iconRes;
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

        public Fragment getFragment() {
            return fragment;
        }
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
            if (!TextUtils.isEmpty(menuItem.title)) {
                holder.title.setText(menuItem.title);
            } else if (!TextUtils.isEmpty(menuItem.subTitle)) {
                holder.title.setText(menuItem.subTitle);
            } else {
                holder.title.setText("");
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

    private final SlidingMenuItem[] menuItems;
    private final SimpleSectionedListAdapter.Section[] menuSections;

    public MainActivity(SlidingMenuItem[] menuItems, SimpleSectionedListAdapter.Section[] menuSections) {
        this.menuItems = menuItems;
        this.menuSections = menuSections;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSectionedAdapter = new SimpleSectionedListAdapter(this, R.layout.menu_list_item_header, new MenuAdapter(this,
                menuItems));
        mSectionedAdapter.setSections(menuSections);

        setContentView(R.layout.activity_base);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(
                R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout),
                mSectionedAdapter);
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
        fragmentManager.beginTransaction().replace(R.id.container, newFragment).commit();

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
        if (mNavigationDrawerFragment != null && !mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

}
