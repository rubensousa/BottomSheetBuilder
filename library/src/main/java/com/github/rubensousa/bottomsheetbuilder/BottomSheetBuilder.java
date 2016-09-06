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
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetAdapterBuilder;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetColors;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;


public class BottomSheetBuilder {

    public static final int MODE_LIST = 0;
    public static final int MODE_GRID = 1;

    @StyleRes
    private int mTheme;

    private BottomSheetColors mColors;

    private boolean mDelayedDismiss = true;
    private boolean mExpandOnStart = false;
    private BottomSheetAdapterBuilder mAdapterBuilder;
    private CoordinatorLayout mCoordinatorLayout;
    private AppBarLayout mAppBarLayout;
    private Context mContext;
    private BottomSheetItemClickListener mItemClickListener;

    public BottomSheetBuilder(Context context, CoordinatorLayout coordinatorLayout) {
        mContext = context;
        mCoordinatorLayout = coordinatorLayout;
        mColors=new BottomSheetColors(mContext);
        mAdapterBuilder = new BottomSheetAdapterBuilder(mContext,mColors);
    }

    public BottomSheetBuilder(Context context) {
        this(context, 0);
    }

    public BottomSheetBuilder(Context context, @StyleRes int theme) {
        mContext = context;
        mTheme = theme;
        mColors=new BottomSheetColors(context);
        mAdapterBuilder = new BottomSheetAdapterBuilder(mContext, mColors);
    }

    public BottomSheetBuilder setMode(int mode) {

        if (mode != MODE_LIST && mode != MODE_GRID) {
            throw new IllegalArgumentException("Mode must be one of BottomSheetBuilder.MODE_LIST" +
                    "or BottomSheetBuilder.MODE_GRID");
        }

        mAdapterBuilder.setMode(mode);
        return this;
    }

    public BottomSheetBuilder setItemClickListener(BottomSheetItemClickListener listener) {
        mAdapterBuilder.setItemClickListener(listener);
        return this;
    }

    public BottomSheetBuilder setMenu(@MenuRes int menu) {
        Menu mMenu = new MenuBuilder(mContext);
        new SupportMenuInflater(mContext).inflate(menu, mMenu);
        return setMenu(mMenu);
    }

    public BottomSheetBuilder setMenu(Menu menu) {
        mAdapterBuilder.setMenu(menu);
        return this;
    }

    public BottomSheetBuilder setColors(BottomSheetColors colors) {
        mColors=colors;
        mAdapterBuilder.setColors(mColors);
        return this;
    }

    public BottomSheetBuilder setItemTextColor(@ColorRes int color) {
        mColors.setItemTextColorRes(color);
        return this;
    }

    public BottomSheetBuilder setTitleTextColor(@ColorRes int color) {
        mColors.setTitleTextColorRes(color);
        return this;
    }

    public BottomSheetBuilder setBackground(@DrawableRes int background) {
        mColors.setBackground(background);
        return this;
    }

    public BottomSheetBuilder setBackgroundColor(@ColorRes int background) {
        mColors.setBackgroundColorRes(background);
        return this;
    }

    public BottomSheetBuilder setDividerBackground(@DrawableRes int background) {
        mColors.setDividerBackground(background);
        return this;
    }

    public BottomSheetBuilder setItemBackground(@DrawableRes int background) {
        mColors.setItemBackground(background);
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
        mColors.setIconTintColorRes(color);
        return this;
    }

    public BottomSheetBuilder setIconTintColor(@ColorInt int color) {
        mColors.setIconTintColor(color);
        return this;
    }

    public BottomSheetBuilder setEditorEnabled(boolean editorEnabled) {
        mAdapterBuilder.setEditorEnabled(editorEnabled);
        return this;
    }

    public BottomSheetView createView() {
        if (mCoordinatorLayout == null) {
            throw new IllegalStateException("You need to provide a coordinatorLayout" +
                    "so the view can be placed on it");
        }

        BottomSheetView sheet = mAdapterBuilder.createView();

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
        BottomSheetMenuDialog dialog = mTheme == 0
                ? new BottomSheetMenuDialog(mContext, R.style.BottomSheetBuilder_DialogStyle)
                : new BottomSheetMenuDialog(mContext, mTheme);

        setItemClickListener(dialog);

        View sheet = mAdapterBuilder.createView();

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

}
