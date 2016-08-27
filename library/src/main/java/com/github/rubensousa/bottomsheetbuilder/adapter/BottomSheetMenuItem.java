package com.github.rubensousa.bottomsheetbuilder.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.MenuItem;


class BottomSheetMenuItem implements BottomSheetItem {

    private Drawable mDrawable;
    private String mTitle;
    private int mId;
    private MenuItem mMenuItem;

    @ColorRes
    private int mTextColor;

    @DrawableRes
    private int mBackground;

    public BottomSheetMenuItem(MenuItem item, @ColorRes int textColor, @DrawableRes int background) {
        mMenuItem = item;
        mId = item.getItemId();
        mDrawable = item.getIcon();
        mTitle = item.getTitle().toString();
        mTextColor = textColor;
        mBackground = background;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public MenuItem getMenuItem(){
        return mMenuItem;
    }

    @DrawableRes
    public int getBackground() {
        return mBackground;
    }

    public int getId() {
        return mId;
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
