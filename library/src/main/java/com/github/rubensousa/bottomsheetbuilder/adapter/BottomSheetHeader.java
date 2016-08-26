package com.github.rubensousa.bottomsheetbuilder.adapter;

import android.support.annotation.ColorRes;

class BottomSheetHeader implements BottomSheetItem {

    private String mTitle;

    @ColorRes
    private int mTextColor;

    public BottomSheetHeader(String title, @ColorRes int color) {
        mTitle = title;
        mTextColor = color;
    }

    @ColorRes
    public int getTextColor() {
        return mTextColor;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }
}
