package com.github.rubensousa.bottomsheetbuilder.sample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.github.rubensousa.bottomsheetbuilder.util.BottomSheetBuilderUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements BottomSheetItemClickListener {

    public static final String STATE_SIMPLE = "state_simple";
    public static final String STATE_HEADER = "state_header";
    public static final String STATE_GRID = "state_grid";
    public static final String STATE_LONG = "state_long";

    private BottomSheetMenuDialog mBottomSheetDialog;
    private BottomSheetBehavior mBehavior;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private boolean mShowingSimpleDialog;
    private boolean mShowingHeaderDialog;
    private boolean mShowingGridDialog;
    private boolean mShowingLongDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        View bottomSheet = new BottomSheetBuilder(this, coordinatorLayout)
                .setMode(BottomSheetBuilder.MODE_GRID)
                .setBackgroundColorResource(android.R.color.white)
                .setMenu(R.menu.menu_bottom_grid_sheet)
                .setItemClickListener(this)
                .createView();

        mBehavior = BottomSheetBehavior.from(bottomSheet);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    fab.show();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BottomSheetBuilderUtils.saveState(outState, mBehavior);
        outState.putBoolean(STATE_SIMPLE, mShowingSimpleDialog);
        outState.putBoolean(STATE_GRID, mShowingGridDialog);
        outState.putBoolean(STATE_HEADER, mShowingHeaderDialog);
        outState.putBoolean(STATE_LONG, mShowingLongDialog);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        BottomSheetBuilderUtils.restoreState(savedInstanceState, mBehavior);
        if (savedInstanceState.getBoolean(STATE_GRID)) onShowDialogGridClick();

        if (savedInstanceState.getBoolean(STATE_HEADER)) onShowDialogHeadersClick();

        if (savedInstanceState.getBoolean(STATE_SIMPLE)) onShowDialogClick();

        if (savedInstanceState.getBoolean(STATE_LONG)) onShowLongDialogClick();
    }

    @Override
    protected void onDestroy() {
        // Avoid leaked windows
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
        super.onDestroy();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.fab)
    public void onFabClick() {
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        fab.hide();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.showViewBtn)
    public void onShowViewClick() {
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.showDialogBtn)
    public void onShowDialogClick() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }

        mShowingSimpleDialog = true;
        mBottomSheetDialog = new BottomSheetBuilder(this)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setAppBarLayout(appBarLayout)
                .addTitleItem("Dynamic title")
                .addItem(0, "Preview", R.drawable.ic_preview_24dp)
                .addItem(1, "Share", R.drawable.ic_share_24dp)
                .addDividerItem()
                .addItem(2, "Get link", R.drawable.ic_link_24dp)
                .addItem(3, "Make a copy", R.drawable.ic_content_copy_24dp)
                .expandOnStart(true)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        Log.d("Item click", item.getTitle() + "");
                        mShowingSimpleDialog = false;
                    }
                })
                .createDialog();
        mBottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mShowingSimpleDialog = false;
            }
        });
        mBottomSheetDialog.show();
    }


    @SuppressWarnings("unused")
    @OnClick(R.id.showDialogHeadersBtn)
    public void onShowDialogHeadersClick() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
        mShowingHeaderDialog = true;
        mBottomSheetDialog = new BottomSheetBuilder(this, R.style.AppTheme_BottomSheetDialog_Custom)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setAppBarLayout(appBarLayout)
                .setMenu(R.menu.menu_bottom_headers_sheet)
                .expandOnStart(true)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        Log.d("Item click", item.getTitle() + "");
                        mShowingHeaderDialog = false;
                    }
                })
                .createDialog();
        mBottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mShowingHeaderDialog = false;
            }
        });
        mBottomSheetDialog.show();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.showDialogGridBtn)
    public void onShowDialogGridClick() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
        mShowingGridDialog = true;
        mBottomSheetDialog = new BottomSheetBuilder(this, R.style.AppTheme_BottomSheetDialog)
                .setMode(BottomSheetBuilder.MODE_GRID)
                .setAppBarLayout(appBarLayout)
                .setMenu(getResources().getBoolean(R.bool.tablet_landscape)
                        ? R.menu.menu_bottom_grid_tablet_sheet : R.menu.menu_bottom_grid_sheet)
                .expandOnStart(true)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        Log.d("Item click", item.getTitle() + "");
                        mShowingGridDialog = false;
                    }
                })
                .createDialog();

        mBottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mShowingGridDialog = false;
            }
        });
        mBottomSheetDialog.show();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.showDialogLongBtn)
    public void onShowLongDialogClick() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
        mShowingLongDialog = true;
        mBottomSheetDialog = new BottomSheetBuilder(this, R.style.AppTheme_BottomSheetDialog_Custom)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setAppBarLayout(appBarLayout)
                .setMenu(R.menu.menu_bottom_list_sheet)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        Log.d("Item click", item.getTitle() + "");
                        mShowingLongDialog = false;
                    }
                })
                .createDialog();

        mBottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mShowingLongDialog = false;
            }
        });
        mBottomSheetDialog.show();
    }

    @Override
    public void onBottomSheetItemClick(MenuItem item) {
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}