package com.github.rubensousa.bottomsheetbuilder.sample;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetView;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetEditor;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RuntimeEditing extends AppCompatActivity implements AdapterView.OnItemSelectedListener, BottomSheetViewHolder,BottomSheetItemClickListener{
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mSettingsViewPager;
    private Spinner mSpinner;
    private CoordinatorLayout mCoord;
    private BottomSheetView mBottomSheet;
    private Menu mMenu;
    private SettingsSectionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_editing);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set up the ViewPager with the sections adapter.
        mSettingsViewPager = (ViewPager) findViewById(R.id.viewpager_settings);
        mAdapter=new SettingsSectionAdapter(getSupportFragmentManager());
        mSettingsViewPager.setAdapter(mAdapter);
        ((TabLayout)findViewById(R.id.tablayout_bs)).setupWithViewPager(mSettingsViewPager);

        mSpinner = (Spinner) findViewById(R.id.spinner_nav);
        List<String> items=new ArrayList<>();
        items.add("List");
        items.add("Grid");


        // Creating adapter for mSpinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.textview_spinner, items);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to mSpinner
        mSpinner.setAdapter(dataAdapter);
        mSpinner.setOnItemSelectedListener(this);

        mCoord= (CoordinatorLayout) findViewById(R.id.coord_bs);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_runtime_editing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item=adapterView.getSelectedItem().toString();

        if (mBottomSheet!=null)
            mCoord.removeView(mBottomSheet);

        int mode;
        mMenu=new MenuBuilder(this);
        if (item=="List") {
            new MenuInflater(this).inflate(R.menu.menu_bottom_headers_sheet_runtime,mMenu);
            mode=BottomSheetBuilder.MODE_LIST;
        }
        else {
            mode=BottomSheetBuilder.MODE_GRID;
            new MenuInflater(this).inflate(R.menu.menu_bottom_grid_sheet_runtime,mMenu);
        }
        mBottomSheet=new BottomSheetBuilder(getBaseContext(),mCoord)
                .setMenu(mMenu)
                .setMode(mode)
                .setBackgroundColor(android.R.color.white)
                .setEditorEnabled(true)
                .setItemClickListener((BottomSheetItemClickListener) RuntimeEditing.this)
                .createView();
        mBottomSheet.getBehavior().setSkipCollapsed(true);
        mBottomSheet.getBehavior().setHideable(true);
        mBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        mBottomSheet.getEditor().setComparator(new Comparator<MenuItem>() {
            @Override
            public int compare(MenuItem menuItem, MenuItem t1) {
                return menuItem.getTitle().toString().compareTo(t1.getTitle().toString())==0?0:-1;
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @OnClick(R.id.expand)
    public void onBottomSheetExpandBtnClick(Button button) {
        if (mBottomSheet!=null)
            mBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public BottomSheetEditor getEditor() {
        return mBottomSheet==null?null:mBottomSheet.getEditor();
    }

    @Override
    public void onBottomSheetItemClick(MenuItem item) {
        mAdapter.onMenuItemClick(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class AddPlaceholderFragment extends Fragment {
        @BindView(R.id.add_numberPicker)
        NumberPicker mPicker;

        @BindView(R.id.add_title)
        EditText mAddTitle;

        public AddPlaceholderFragment() {
        }
        public static AddPlaceholderFragment newInstance() {
            AddPlaceholderFragment fragment = new AddPlaceholderFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add, container, false);
            ButterKnife.bind(this,rootView);
            mPicker.setMaxValue(10);
            return rootView;
        }

        private BottomSheetViewHolder mHolder;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            mHolder=(BottomSheetViewHolder) context;
        }

        @OnClick(R.id.button_add_menu)
        public void onMenuAddBtnClick() {
            Menu menu=new MenuBuilder(getContext());
            MenuItem item=menu.add(Menu.NONE,(int)(Math.random()*Integer.MAX_VALUE),Menu.NONE,mAddTitle.getText())
            .setIcon(ResourcesCompat.getDrawable(getResources(), android.R.drawable.ic_menu_add, null));
            try {
                mHolder.getEditor().addItem(item, mPicker.getValue());
            }
            catch (Exception e) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Wrong input")
                        .setMessage(e.getMessage())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }

        @OnClick(R.id.button_add_submenu)
        public void onSubMenuAddBtnClick() {
            Menu menu=new MenuBuilder(getContext());
            menu.addSubMenu(Menu.NONE,(int)(Math.random()*Integer.MAX_VALUE),Menu.NONE,mAddTitle.getText());
            try {
                mHolder.getEditor().addItem(menu.getItem(0));
            }
            catch (Exception e) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Wrong input")
                        .setMessage(e.getMessage())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ChangePlaceholderFragment extends Fragment {
        public ChangePlaceholderFragment() {
        }

        @BindView(R.id.editText_change_from)
        EditText mFrom;

        @BindView(R.id.editText_change_to)
        EditText mTo;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ChangePlaceholderFragment newInstance() {
            ChangePlaceholderFragment fragment = new ChangePlaceholderFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_change, container, false);
            ButterKnife.bind(this,rootView);
            return rootView;
        }

        private BottomSheetViewHolder mHolder;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            mHolder=(BottomSheetViewHolder) context;
        }

        @OnClick(R.id.button_change)
        public void onChangeBtnClick() {
            Menu menu=new MenuBuilder(getContext());
            MenuItem item=menu.add(Menu.NONE,(int)(Math.random()*Integer.MAX_VALUE),Menu.NONE,mFrom.getText());
            try {
                mHolder.getEditor().changeItem(item, mTo.getText().toString());
            }
            catch (Exception e) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Wrong input")
                        .setMessage(e.getMessage())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }

        void onMenuItemClick(MenuItem item) {
            if(mFrom!=null) mFrom.setText(item.getTitle());
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DeletePlaceholderFragment extends Fragment {

        @BindView(R.id.editText_delete)
        EditText mDeleteTitle;


        public DeletePlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DeletePlaceholderFragment newInstance() {
            DeletePlaceholderFragment fragment = new DeletePlaceholderFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_delete, container, false);
            ButterKnife.bind(this,rootView);
            return rootView;
        }

        private BottomSheetViewHolder mHolder;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            mHolder=(BottomSheetViewHolder) context;
        }

        @OnClick(R.id.button_delete)
        public void onDeleteBtnClick() {
            Menu menu=new MenuBuilder(getContext());
            MenuItem item=menu.add(mDeleteTitle.getText());
            try {
                mHolder.getEditor().removeItem(item);
            }
            catch (Exception e) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Wrong input")
                        .setMessage(e.getMessage())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }

        void onMenuItemClick(MenuItem item) {
            if (mDeleteTitle!=null) mDeleteTitle.setText(item.getTitle());
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SettingsSectionAdapter extends FragmentPagerAdapter {
        private DeletePlaceholderFragment mDelete =DeletePlaceholderFragment.newInstance();
        private ChangePlaceholderFragment mChange=ChangePlaceholderFragment.newInstance();

        public SettingsSectionAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a AddPlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return AddPlaceholderFragment.newInstance();
                case 1:
                    return mChange;
                case 2:
                    return mDelete;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Add";
                case 1:
                    return "Change";
                case 2:
                    return "Delete";
            }
            return null;
        }

        void onMenuItemClick(MenuItem item) {
            mDelete.onMenuItemClick(item);
            mChange.onMenuItemClick(item);
        }
    }
}
