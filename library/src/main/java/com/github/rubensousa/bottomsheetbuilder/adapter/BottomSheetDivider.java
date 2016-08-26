package com.github.rubensousa.bottomsheetbuilder.adapter;


import android.support.annotation.DrawableRes;


class BottomSheetDivider implements BottomSheetItem {

    @DrawableRes
    private int mBackgroundDrawable;

    public BottomSheetDivider(@DrawableRes int background) {
        mBackgroundDrawable = background;
    }

    @DrawableRes
    public int getBackground() {
        return mBackgroundDrawable;
    }

    @Override
    public String getTitle() {
        return "";
    }
}
