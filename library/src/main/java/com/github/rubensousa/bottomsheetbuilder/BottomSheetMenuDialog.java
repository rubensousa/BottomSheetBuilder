package com.github.rubensousa.bottomsheetbuilder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MotionEventCompat;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class BottomSheetMenuDialog extends BottomSheetDialog {

    private BottomSheetBehavior.BottomSheetCallback mCallback;
    private BottomSheetBehavior mBehavior;

    public BottomSheetMenuDialog(Context context) {
        super(context);
    }

    public BottomSheetMenuDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void setContentView(View view) {
        getDelegate().setContentView(wrapInBottomSheet(view));
    }

    public void setBottomSheetCallback(BottomSheetBehavior.BottomSheetCallback callback) {
        mCallback = callback;
    }

    public BottomSheetBehavior getBehavior() {
        return mBehavior;
    }

    private View wrapInBottomSheet(View view) {
        final CoordinatorLayout coordinator = (CoordinatorLayout) View.inflate(getContext(),
                R.layout.bottomsheetbuilder_dialog, null);

        CoordinatorLayout.LayoutParams layoutParams
                = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setBehavior(new BottomSheetBehavior());
        coordinator.addView(view, layoutParams);
        mBehavior = BottomSheetBehavior.from(view);
        mBehavior.setBottomSheetCallback(mBottomSheetCallback);
        view.post(new Runnable() {
            @Override
            public void run() {
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        if (shouldWindowCloseOnTouchOutside()) {
            final View finalView = view;
            coordinator.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (isShowing() &&
                            MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_UP &&
                            !coordinator.isPointInChildBounds(finalView,
                                    (int) event.getX(), (int) event.getY())) {
                        cancel();
                        return true;
                    }
                    return false;
                }
            });
        }
        return coordinator;
    }

    private boolean shouldWindowCloseOnTouchOutside() {
        TypedValue value = new TypedValue();

        return getContext().getTheme()
                .resolveAttribute(android.R.attr.windowCloseOnTouchOutside, value, true)
                && value.data != 0;
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback
            = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet,
                                   @BottomSheetBehavior.State int newState) {

            if (mCallback != null) {
                mCallback.onStateChanged(bottomSheet, newState);
            }
            if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                dismiss();
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
