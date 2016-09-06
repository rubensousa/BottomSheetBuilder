package com.github.rubensousa.bottomsheetbuilder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetColors;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetEditor;

import java.io.Serializable;

public class BottomSheetView extends LinearLayout {
    private BottomSheetEditor mEditor;


    public BottomSheetView(Context context) {
        super(context);
    }

    public BottomSheetView(Context context,BottomSheetEditor editor) {
        super(context);
        mEditor=editor;
    }

    public static BottomSheetView from(Context context,@LayoutRes int layout,BottomSheetEditor editor) {
        BottomSheetView view=new BottomSheetView(context);
        view.mEditor=editor;
        LayoutInflater.from(context).inflate(layout,view);
        return view;
    };

    public BottomSheetBehavior getBehavior() {
        CoordinatorLayout.LayoutParams params= (CoordinatorLayout.LayoutParams) getLayoutParams();
        return ((BottomSheetBehavior) params.getBehavior());
    }

    public BottomSheetEditor getEditor() {
        if (mEditor==null) throw new IllegalStateException("Editor is not enabled for this view.");
        return mEditor;
    }
}
