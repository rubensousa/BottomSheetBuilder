package com.github.rubensousa.bottomsheetbuilder.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

public class BottomSheetColors implements Serializable {
    private transient Context mContext;
    private transient Drawable mBackgroundDrawable;

    private String mBackgroundUri;

    @DrawableRes
    private int mBackgroundRes;

    @ColorInt
    private int mBackgroundColor;

    private transient Drawable mDividerBackgroundDrawable;

    private transient Drawable mItemBackgroundDrawable;

    private String mDividerBackgroundUri;

    private String mItemBackgroundUri;

    @DrawableRes
    private int mDividerBackgroundRes;

    @DrawableRes
    private int mItemBackgroundRes;

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
        mBackgroundRes=background;
    }

    public void setBackground(Uri background) {
        mBackgroundUri=background.toString();
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
        mDividerBackgroundRes = dividerBackground;
    }

    public void setDividerBackground(Uri dividerBackground) {
        mDividerBackgroundUri = dividerBackground.toString();
    }

    public void setDividerBackground(Drawable dividerBackground) {
        mDividerBackgroundDrawable = dividerBackground;
    }

    public void setItemBackground(@DrawableRes int itemBackground) {
        mItemBackgroundRes = itemBackground;
    }

    public void setItemBackground(Uri itemBackground) {
        mItemBackgroundUri = itemBackground.toString();
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


    public @ColorInt int getBackgroundColor() {
        return mBackgroundColor;
    }

    public Drawable getDividerBackground() {
        if (mDividerBackgroundDrawable!=null)
            return mDividerBackgroundDrawable;
        else if (mDividerBackgroundRes!=0)
            return ContextCompat.getDrawable(mContext,mDividerBackgroundRes);
        else if (mDividerBackgroundUri!=null)
            return getDrawableFromUri(mContext,Uri.parse(mDividerBackgroundUri));
        else
            return null;
    }

    public void setContext(Context context) {
        mContext=context;
    }

    public Drawable getItemBackground() {
        if (mItemBackgroundDrawable!=null)
            return mItemBackgroundDrawable;
        else if (mItemBackgroundRes!=0)
            return ContextCompat.getDrawable(mContext,mItemBackgroundRes);
        else if (mItemBackgroundUri!=null)
            return getDrawableFromUri(mContext,Uri.parse(mItemBackgroundUri));
        else
            return null;
    }

    public Drawable getBackground() {
        if (mBackgroundDrawable!=null)
            return mBackgroundDrawable;
        else if (mBackgroundRes!=0)
            return ContextCompat.getDrawable(mContext,mBackgroundRes);
        else if (mBackgroundUri!=null)
            return getDrawableFromUri(mContext,Uri.parse(mBackgroundUri));
        else
            return null;
    }

    public @ColorInt int getItemTextColor() {
        return mItemTextColor;
    }


    @ColorInt int getTitleTextColor() {
        return mTitleTextColor;
    }

    @ColorInt int getIconTintColor() {
        return mIconTintColor;
    }


    private Drawable getDrawableFromUri(Context context,Uri uri) {
        if (context==null)
            throw new IllegalStateException("Call setContext after deserialization.");
        Drawable newDrawable;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            newDrawable = Drawable.createFromStream(inputStream, uri.toString() );
        } catch (FileNotFoundException e) {
            newDrawable = null;
        }
        return newDrawable;
    }

}
