package com.github.rubensousa.bottomsheetbuilder.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import java.io.Serializable;

public class BottomSheetColors implements Serializable {

    private final Context mContext;
    private Drawable mBackgroundDrawable;

    @ColorInt
    private int mBackgroundColor;

    private Drawable mDividerBackgroundDrawable;

    private Drawable mItemBackgroundDrawable;

    @ColorInt
    private int mItemTextColor;

    @ColorInt
    private int mTitleTextColor;

    @ColorInt
    private int mIconTintColor=-1;

    public BottomSheetColors(Context context) {
        mContext=context;
    }

    public void setItemTextColorRes(@ColorRes int itemTextColor) {
        mItemTextColor = ContextCompat.getColor(mContext,itemTextColor);
    }

    public void setItemTextColor(@ColorInt int itemTextColor) {
        mItemTextColor = itemTextColor;
    }

    public void setTitleTextColorRes(@ColorRes int titleTextColor) {
        mTitleTextColor = ContextCompat.getColor(mContext,titleTextColor);;
    }

    public void setTitleTextColor(@ColorInt int titleTextColor) {
        mTitleTextColor = titleTextColor;
    }

    public void setBackground(@DrawableRes int background) {
        mBackgroundDrawable = ContextCompat.getDrawable(mContext,background);
    }

    public void setBackground(Drawable background) {
        mBackgroundDrawable = background;
    }

    public void setBackgroundColorRes(@ColorRes int backgroundColor) {
        mBackgroundColor = ContextCompat.getColor(mContext,backgroundColor);;
    }

    public void setBackgroundColor(@ColorInt int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public void setDividerBackground(@DrawableRes int dividerBackground) {
        mDividerBackgroundDrawable = ContextCompat.getDrawable(mContext,dividerBackground);
    }

    public void setDividerBackground(Drawable dividerBackground) {
        mDividerBackgroundDrawable = dividerBackground;
    }

    public void setItemBackground(@DrawableRes int itemBackground) {
        mItemBackgroundDrawable = ContextCompat.getDrawable(mContext,itemBackground);
    }

    public void setItemBackground(Drawable itemBackground) {
        mItemBackgroundDrawable = itemBackground;
    }

    public void setIconTintColorRes(@ColorRes int iconTintColor) {
        mIconTintColor = ContextCompat.getColor(mContext,iconTintColor);
    }

    public void setIconTintColor(@ColorInt int iconTintColor) {
        mIconTintColor = iconTintColor;
    }


    @ColorInt int getBackgroundColor() {
        return mBackgroundColor;
    }

    Drawable getDividerBackground() {
        return mDividerBackgroundDrawable;
    }


    Drawable getItemBackground() {
        return mItemBackgroundDrawable;
    }

    Drawable getBackground() {
        return mBackgroundDrawable;
    }

    @ColorInt int getItemTextColor() {
        return mItemTextColor;
    }


    @ColorInt int getTitleTextColor() {
        return mTitleTextColor;
    }

    @ColorInt int getIconTintColor() {
        return mIconTintColor;
    }
}
