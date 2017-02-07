/*
 * Copyright 2016 Rúben Sousa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.rubensousa.bottomsheetbuilder;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetAdapterBuilder;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;


public class BottomSheetBuilder {

    public static final int MODE_LIST = 0;
    public static final int MODE_GRID = 1;

    @DrawableRes
    private int mBackgroundDrawable;

    @DrawableRes
    private int mDividerBackground;

    @DrawableRes
    private int mItemBackground;

    @StyleRes
    private int mTheme;

    private int mBackgroundColor;
    private int mItemTextColor;
    private int mTitleTextColor;

    private boolean mDelayedDismiss = true;
    private boolean mExpandOnStart = false;
    private int mIconTintColor = -1;
    private int mMode;
    private Menu mMenu;
    private BottomSheetAdapterBuilder mAdapterBuilder;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private Context mContext;
    private BottomSheetItemClickListener mItemClickListener;

    public BottomSheetBuilder(Context context, CoordinatorLayout coordinatorLayout) {
        mContext = context;
        mCoordinatorLayout = coordinatorLayout;
        mAdapterBuilder = new BottomSheetAdapterBuilder(mContext);
    }

    public BottomSheetBuilder(Context context) {
        this(context, 0);
    }

    public BottomSheetBuilder(Context context, @StyleRes int theme) {
        mContext = context;
        mTheme = theme;
        mAdapterBuilder = new BottomSheetAdapterBuilder(mContext);
    }

    public BottomSheetBuilder setMode(int mode) {
        if (mode != MODE_LIST && mode != MODE_GRID) {
            throw new IllegalArgumentException("Mode must be one of BottomSheetBuilder.MODE_LIST" +
                    "or BottomSheetBuilder.MODE_GRID");
        }
        mMode = mode;
        mAdapterBuilder.setMode(mode);
        return this;
    }

    public BottomSheetBuilder setItemClickListener(BottomSheetItemClickListener listener) {
        mItemClickListener = listener;
        return this;
    }

    public BottomSheetBuilder setMenu(@MenuRes int menu) {
        mMenu = new MenuBuilder(mContext);
        new SupportMenuInflater(mContext).inflate(menu, mMenu);
        return setMenu(mMenu);
    }

    public BottomSheetBuilder setMenu(Menu menu) {
        mMenu = menu;
        mAdapterBuilder.setMenu(mMenu);
        return this;
    }

    public BottomSheetBuilder addTitleItem(@StringRes int title) {
        return addTitleItem(mContext.getString(title));
    }

    public BottomSheetBuilder addTitleItem(String title) {
        if (mMode == BottomSheetBuilder.MODE_GRID) {
            throw new IllegalStateException("You can't add a title with MODE_GRID. " +
                    "Use MODE_LIST instead");
        }
        mAdapterBuilder.addTitleItem(title, mTitleTextColor);
        return this;
    }

    public BottomSheetBuilder addDividerItem() {
        if (mMode == BottomSheetBuilder.MODE_GRID) {
            throw new IllegalStateException("You can't add a divider with MODE_GRID. " +
                    "Use MODE_LIST instead");
        }
        mAdapterBuilder.addDividerItem(mDividerBackground);
        return this;
    }

    public BottomSheetBuilder addItem(int id, @StringRes int title, @DrawableRes int icon) {
        return addItem(id, mContext.getString(title), ContextCompat.getDrawable(mContext, icon));
    }

    public BottomSheetBuilder addItem(int id, @StringRes int title, Drawable icon) {
        return addItem(id, mContext.getString(title), icon);
    }

    public BottomSheetBuilder addItem(int id, String title, @DrawableRes int icon) {
        return addItem(id, title, ContextCompat.getDrawable(mContext, icon));
    }

    public BottomSheetBuilder addItem(int id, String title, Drawable icon) {
        mAdapterBuilder.addItem(id, title, icon, mItemTextColor, mItemBackground, mIconTintColor);
        return this;
    }

    public BottomSheetBuilder setItemTextColor(@ColorInt int color) {
        mItemTextColor = color;
        return this;
    }

    public BottomSheetBuilder setTitleTextColor(@ColorInt int color) {
        mTitleTextColor = color;
        return this;
    }

    public BottomSheetBuilder setBackgroundColor(@ColorInt int color) {
        mBackgroundColor = color;
        return this;
    }

    public BottomSheetBuilder setItemTextColorResource(@ColorRes int color) {
        mItemTextColor = ResourcesCompat.getColor(mContext.getResources(), color,
                mContext.getTheme());
        return this;
    }

    public BottomSheetBuilder setTitleTextColorResource(@ColorRes int color) {
        mTitleTextColor = ResourcesCompat.getColor(mContext.getResources(), color,
                mContext.getTheme());
        return this;
    }

    public BottomSheetBuilder setBackground(@DrawableRes int background) {
        mBackgroundDrawable = background;
        return this;
    }

    public BottomSheetBuilder setBackgroundColorResource(@ColorRes int background) {
        mBackgroundColor = ResourcesCompat.getColor(mContext.getResources(), background,
                mContext.getTheme());
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

    public BottomSheetBuilder expandOnStart(boolean expand) {
        mExpandOnStart = expand;
        return this;
    }

    public BottomSheetBuilder delayDismissOnItemClick(boolean dismiss) {
        mDelayedDismiss = dismiss;
        return this;
    }

    public BottomSheetBuilder setAppBarLayout(AppBarLayout appbar) {
        mAppBarLayout = appbar;
        return this;
    }

    public BottomSheetBuilder setIconTintColorResource(@ColorRes int color) {
        mIconTintColor = ContextCompat.getColor(mContext, color);
        return this;
    }

    public BottomSheetBuilder setIconTintColor(int color) {
        mIconTintColor = color;
        return this;
    }

    public View createView() {

        if (mMenu == null && mAdapterBuilder.getItems().isEmpty()) {
            throw new IllegalStateException("You need to provide at least one Menu " +
                    "or an item with addItem");
        }

        if (mCoordinatorLayout == null) {
            throw new IllegalStateException("You need to provide a coordinatorLayout" +
                    "so the view can be placed on it");
        }

        View sheet = mAdapterBuilder.createView(mTitleTextColor, mBackgroundDrawable,
                mBackgroundColor, mDividerBackground, mItemTextColor, mItemBackground,
                mIconTintColor, mItemClickListener);

        ViewCompat.setElevation(sheet, mContext.getResources()
                .getDimensionPixelSize(R.dimen.bottomsheet_elevation));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sheet.findViewById(R.id.fakeShadow).setVisibility(View.GONE);
        }

        CoordinatorLayout.LayoutParams layoutParams
                = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.setBehavior(new BottomSheetBehavior());

        if (mContext.getResources().getBoolean(R.bool.tablet_landscape)) {
            layoutParams.width = mContext.getResources()
                    .getDimensionPixelSize(R.dimen.bottomsheet_width);
        }

        mCoordinatorLayout.addView(sheet, layoutParams);
        mCoordinatorLayout.postInvalidate();
        return sheet;
    }

    public BottomSheetMenuDialog createDialog() {

        if (mMenu == null && mAdapterBuilder.getItems().isEmpty()) {
            throw new IllegalStateException("You need to provide at least one Menu " +
                    "or an item with addItem");
        }

        BottomSheetMenuDialog dialog = mTheme == 0
                ? new BottomSheetMenuDialog(mContext, R.style.BottomSheetBuilder_DialogStyle)
                : new BottomSheetMenuDialog(mContext, mTheme);

        if (mTheme != 0) {
            setupThemeColors(mContext.obtainStyledAttributes(mTheme, new int[]{
                    R.attr.bottomSheetBuilderBackgroundColor,
                    R.attr.bottomSheetBuilderItemTextColor,
                    R.attr.bottomSheetBuilderTitleTextColor}));
        } else {
            setupThemeColors(mContext.getTheme().obtainStyledAttributes(new int[]{
                    R.attr.bottomSheetBuilderBackgroundColor,
                    R.attr.bottomSheetBuilderItemTextColor,
                    R.attr.bottomSheetBuilderTitleTextColor,}));
        }

        View sheet = mAdapterBuilder.createView(mTitleTextColor, mBackgroundDrawable,
                mBackgroundColor, mDividerBackground, mItemTextColor, mItemBackground,
                mIconTintColor, dialog);

        sheet.findViewById(R.id.fakeShadow).setVisibility(View.GONE);
        dialog.setAppBar(mAppBarLayout);
        dialog.expandOnStart(mExpandOnStart);
        dialog.delayDismiss(mDelayedDismiss);
        dialog.setBottomSheetItemClickListener(mItemClickListener);

        if (mContext.getResources().getBoolean(R.bool.tablet_landscape)) {
            FrameLayout.LayoutParams layoutParams
                    = new FrameLayout.LayoutParams(mContext.getResources()
                    .getDimensionPixelSize(R.dimen.bottomsheet_width),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setContentView(sheet, layoutParams);
        } else {
            dialog.setContentView(sheet);
        }

        return dialog;
    }

    @SuppressWarnings("ResourceType")
    private void setupThemeColors(TypedArray typedArray) {
        int backgroundRes = typedArray.getResourceId(0, mBackgroundColor);
        int textRes = typedArray.getResourceId(1, mItemTextColor);
        int titleRes = typedArray.getResourceId(2, mTitleTextColor);

        if (backgroundRes != mBackgroundColor) {
            mBackgroundColor = ContextCompat.getColor(mContext, backgroundRes);
        }

        if (titleRes != mTitleTextColor) {
            mTitleTextColor = ContextCompat.getColor(mContext, titleRes);
        }

        if (textRes != mItemTextColor) {
            mItemTextColor = ContextCompat.getColor(mContext, textRes);
        }

        typedArray.recycle();
    }

}
