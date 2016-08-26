package com.github.rubensousa.bottomsheetbuilder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.github.rubensousa.bottomsheetbuilder.items.BottomSheetDivider;
import com.github.rubensousa.bottomsheetbuilder.items.BottomSheetHeader;
import com.github.rubensousa.bottomsheetbuilder.items.BottomSheetItem;
import com.github.rubensousa.bottomsheetbuilder.items.BottomSheetMenuItem;

import java.util.ArrayList;
import java.util.List;


public class BottomSheetBuilder {

    public static final int MODE_LIST = 0;
    public static final int MODE_GRID = 1;

    @DrawableRes
    private int mBackgroundDrawable;

    @ColorRes
    private int mBackgroundColor;

    @DrawableRes
    private int mDividerBackground;

    @DrawableRes
    private int mItemBackground;

    @ColorRes
    private int mItemTextColor;

    @ColorRes
    private int mTitleTextColor;

    @StyleRes
    private int mTheme;

    @MenuRes
    private int mMenuRes;

    private Menu mMenu;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private Context mContext;
    private BottomSheetItemClickListener mItemClickListener;
    private int mMode = MODE_LIST;

    public BottomSheetBuilder(Context context, CoordinatorLayout coordinatorLayout) {
        this(context, coordinatorLayout, null);
    }

    public BottomSheetBuilder(Context context, CoordinatorLayout coordinatorLayout,
                              AppBarLayout appBarLayout) {
        mContext = context;
        mCoordinatorLayout = coordinatorLayout;
        mAppBarLayout = appBarLayout;
    }

    public BottomSheetBuilder(Context context) {
        this(context, 0);
    }

    public BottomSheetBuilder(Context context, @StyleRes int theme) {
        mContext = context;
        mTheme = theme;
    }

    public BottomSheetBuilder setMode(int mode) {

        if (mode != MODE_LIST && mode != MODE_GRID) {
            throw new IllegalArgumentException("Mode must be one of BottomSheetBuilder.MODE_LIST" +
                    "or BottomSheetBuilder.MODE_GRID");
        }

        mMode = mode;
        return this;
    }

    public BottomSheetBuilder setItemClickListener(BottomSheetItemClickListener listener) {
        mItemClickListener = listener;
        return this;
    }

    public BottomSheetBuilder setMenu(@MenuRes int menu) {
        mMenuRes = menu;
        return this;
    }

    public BottomSheetBuilder setMenu(Menu menu) {
        mMenu = menu;
        return this;
    }

    public BottomSheetBuilder setItemTextColor(@ColorRes int color) {
        mItemTextColor = color;
        return this;
    }

    public BottomSheetBuilder setTitleTextColor(@ColorRes int color) {
        mTitleTextColor = color;
        return this;
    }

    public BottomSheetBuilder setBackground(@DrawableRes int background) {
        mBackgroundDrawable = background;
        return this;
    }

    public BottomSheetBuilder setBackgroundColor(@ColorRes int background) {
        mBackgroundColor = background;
        return this;
    }

    public BottomSheetBuilder setDividerBackground(@DrawableRes int background) {
        mDividerBackground = background;
        return this;
    }

    public BottomSheetBuilder setItemBackground(@DrawableRes int background) {
        mItemBackground = background;
        return this;
    }

    public View createView() {

        if (mCoordinatorLayout == null) {
            throw new IllegalStateException("You need to provide a coordinatorLayout" +
                    "so the view can be placed on it");
        }

        final View sheet = setupView();
        ViewCompat.setElevation(sheet, Resources.getSystem().getDisplayMetrics().densityDpi
                / DisplayMetrics.DENSITY_DEFAULT * 16);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sheet.findViewById(R.id.fakeShadow).setVisibility(View.GONE);
        }

        final CoordinatorLayout.LayoutParams layoutParams
                = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT);


        layoutParams.setBehavior(new BottomSheetBehavior());
        mCoordinatorLayout.addView(sheet, layoutParams);
        mCoordinatorLayout.postInvalidate();
        return sheet;
    }

    public BottomSheetMenuDialog createDialog() {
        BottomSheetMenuDialog dialog = mTheme == 0 ? new BottomSheetMenuDialog(mContext)
                : new BottomSheetMenuDialog(mContext, mTheme);

        View sheet = setupView();
        sheet.findViewById(R.id.fakeShadow).setVisibility(View.GONE);
        dialog.setBottomSheetItemClickListener(mItemClickListener);
        dialog.setContentView(sheet);

        return dialog;
    }

    @SuppressLint("InflateParams")
    private View setupView() {

        if (mMenu == null && mMenuRes == 0) {
            throw new IllegalStateException("You need to provide at least one Menu or a Menu resource id");
        }

        final List<BottomSheetItem> items = createMenuItems();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View sheet;

        if (mMode == MODE_GRID) {
            sheet = layoutInflater.inflate(R.layout.bottomsheetbuilder_sheet_grid, null);
        } else {
            sheet = layoutInflater.inflate(R.layout.bottomsheetbuilder_sheet_list, null);
        }

        final RecyclerView recyclerView = (RecyclerView) sheet.findViewById(R.id.recyclerView);

        if (mBackgroundDrawable != 0) {
            recyclerView.setBackgroundResource(mBackgroundDrawable);
        } else {
            if (mBackgroundColor != 0) {
                recyclerView.setBackgroundColor(ContextCompat.getColor(mContext, mBackgroundColor));
            }
        }

        recyclerView.setHasFixedSize(true);

        if (mMode == MODE_LIST) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(new BottomSheetItemAdapter(items, mMode, mItemClickListener));
        }

        if (mMode == MODE_GRID) {
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 1;
                }
            });
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new BottomSheetItemAdapter(items, mMode, mItemClickListener));
            recyclerView.post(new Runnable() {
                @Override
                public void run() {

                    BottomSheetItemAdapter adapter
                            = (BottomSheetItemAdapter) recyclerView.getAdapter();

                    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
                    float margins = 24 * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
                    adapter.setItemWidth((int) ((recyclerView.getWidth() - 2 * margins) / 3));
                    recyclerView.setAdapter(adapter);
                }
            });
        }

        return sheet;
    }

    private List<BottomSheetItem> createMenuItems() {
        List<BottomSheetItem> items = new ArrayList<>();
        Menu menu;

        if (mMenu == null) {
            menu = new MenuBuilder(mContext);
            new SupportMenuInflater(mContext).inflate(mMenuRes, menu);
        } else {
            menu = mMenu;
        }

        boolean addedSubMenu = false;

        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);

            if (item.isVisible()) {
                if (item.hasSubMenu()) {
                    SubMenu subMenu = item.getSubMenu();

                    if (i != 0 && addedSubMenu) {
                        items.add(new BottomSheetDivider(mDividerBackground));
                    }

                    CharSequence title = item.getTitle();
                    if (title != null && !title.equals("")) {
                        items.add(new BottomSheetHeader(title.toString(), mTitleTextColor));
                    }

                    for (int j = 0; j < subMenu.size(); j++) {
                        MenuItem subItem = subMenu.getItem(j);
                        if (subItem.isVisible()) {
                            items.add(new BottomSheetMenuItem(subItem, mItemTextColor, mItemBackground));
                            addedSubMenu = true;
                        }
                    }
                } else {
                    items.add(new BottomSheetMenuItem(item, mItemTextColor, mItemBackground));
                }
            }
        }

        return items;
    }

}
