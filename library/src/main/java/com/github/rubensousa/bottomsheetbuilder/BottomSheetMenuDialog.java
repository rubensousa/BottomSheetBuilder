package com.github.rubensousa.bottomsheetbuilder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.github.rubensousa.bottomsheetbuilder.util.BottomSheetBuilderUtils;

public class BottomSheetMenuDialog extends BottomSheetDialog implements BottomSheetItemClickListener {

    private BottomSheetBehavior.BottomSheetCallback mCallback;
    private BottomSheetBehavior mBehavior;
    private BottomSheetItemClickListener mClickListener;
    private AppBarLayout mAppBarLayout;
    private boolean mExpandOnStart;
    private boolean mDelayDismiss;
    private boolean mClicked;

    public BottomSheetMenuDialog(Context context) {
        super(context);
    }

    public BottomSheetMenuDialog(Context context, int theme) {
        super(context, theme);
    }


    /**
     * Dismiss the BottomSheetDialog while animating the sheet.
     */
    public void dismissWithAnimation() {
        if (mBehavior != null) {
            mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FrameLayout sheet = (FrameLayout) findViewById(R.id.design_bottom_sheet);
        if (sheet != null) {
            mBehavior = BottomSheetBehavior.from(sheet);
            mBehavior.setBottomSheetCallback(mBottomSheetCallback);

            // Make sure the sheet doesn't overlap the appbar
            if (mAppBarLayout != null) {
                CoordinatorLayout.LayoutParams layoutParams
                        = (CoordinatorLayout.LayoutParams) sheet.getLayoutParams();
                layoutParams.topMargin = mAppBarLayout.getHeight();
                sheet.setLayoutParams(layoutParams);
            }

            if (mExpandOnStart) {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

        }
    }

    public void setAppBar(AppBarLayout appBar) {
        mAppBarLayout = appBar;
    }

    public void expandOnStart(boolean expand) {
        mExpandOnStart = expand;
    }

    public void delayDismiss(boolean dismiss) {
        mDelayDismiss = dismiss;
    }

    public void setBottomSheetCallback(BottomSheetBehavior.BottomSheetCallback callback) {
        mCallback = callback;
    }

    public void setBottomSheetItemClickListener(BottomSheetItemClickListener listener) {
        mClickListener = listener;
    }

    public BottomSheetBehavior getBehavior() {
        return mBehavior;
    }

    @Override
    public void onBottomSheetItemClick(MenuItem item) {
        if (!mClicked) {

            if (mBehavior != null) {
                if (mDelayDismiss) {
                    BottomSheetBuilderUtils.delayDismiss(mBehavior);
                } else {
                    mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }

            if (mClickListener != null) {
                mClickListener.onBottomSheetItemClick(item);
            }

            mClicked = true;
        }
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback
            = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet,
                                   @BottomSheetBehavior.State int newState) {

            if (mCallback != null) {
                mCallback.onStateChanged(bottomSheet, newState);
            }

            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                mBehavior.setBottomSheetCallback(null);
                BottomSheetMenuDialog.super.dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            if (mCallback != null) {
                mCallback.onSlide(bottomSheet, slideOffset);
            }
        }
    };
}
