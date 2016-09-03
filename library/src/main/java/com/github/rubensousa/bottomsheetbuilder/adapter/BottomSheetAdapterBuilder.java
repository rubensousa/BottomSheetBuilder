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

package com.github.rubensousa.bottomsheetbuilder.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.LinearLayout;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.R;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BottomSheetAdapterBuilder {
    @DrawableRes
    private int mBackgroundDrawable;

    @ColorRes
    private int mBackgroundColor;

    @DrawableRes
    private int mDividerBackground;

    @DrawableRes
    private int mItemBackground;

    @ColorRes
    private int mItemTextColor;

    @ColorRes
    private int mTitleTextColor;

    @ColorRes
    private int mIconTintColor = -1;

    private BottomSheetItemClickListener mItemClickListener;

    private int mMode;
    private Menu mMenu;
    private Context mContext;
    private boolean mEditorEnabled=false;

    void setAddedSubMenu(boolean addedSubMenu) {
        mAddedSubMenu = addedSubMenu;
    }

    private boolean mAddedSubMenu;
    private HashMap<MenuItem,List<BottomSheetItem>> mBinds;

    private List<BottomSheetItem> mItems;

    public BottomSheetAdapterBuilder(Context context) {
        mContext = context;
    }

    public void setMenu(Menu menu) {
        mMenu = menu;
    }

    public void setMode(int mode) {
        mMode = mode;
    }

    @SuppressLint("InflateParams")
    public BottomSheetView createView() {

        if (mMenu == null) {
            throw new IllegalStateException("You need to provide at least one Menu" +
                    "or a Menu resource id");
        }

        mItems=new ArrayList<>();
        mBinds=new HashMap<>();

        BottomSheetEditor editor=mEditorEnabled?new BottomSheetEditor(this):null;

        BottomSheetView sheet = mMode == BottomSheetBuilder.MODE_GRID ?
                BottomSheetView.from(mContext,R.layout.bottomsheetbuilder_sheet_grid,editor)
                : BottomSheetView.from(mContext,R.layout.bottomsheetbuilder_sheet_list,editor);


        final RecyclerView recyclerView = (RecyclerView) sheet.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (mBackgroundDrawable != 0) {
            recyclerView.setBackgroundResource(mBackgroundDrawable);
        } else {
            if (mBackgroundColor != 0) {
                recyclerView.setBackgroundColor(ContextCompat.getColor(mContext, mBackgroundColor));
            }
        }

        mItems = createAdapterItems();

        final BottomSheetItemAdapter adapter = new BottomSheetItemAdapter(mItems, mMode,
                mItemClickListener);

        if (editor!=null) {
            editor.setAdapter(adapter);
            editor.setItems(mItems);
            editor.setRecycler(recyclerView);
        }

        if (mMode == BottomSheetBuilder.MODE_LIST) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(adapter);
        } else {
            final int columns = mContext.getResources().getInteger(R.integer.bottomsheet_grid_columns);
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, columns);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.post(new Runnable() {
                @Override
                public void run() {

                    float margin = mContext.getResources()
                            .getDimensionPixelSize(R.dimen.bottomsheet_grid_horizontal_margin);
                    adapter.setItemWidth((int) ((recyclerView.getWidth() - 2 * margin) / columns));
                    recyclerView.setAdapter(adapter);
                }
            });
        }

        return sheet;

    }

    List<BottomSheetItem> createAdapterItems() {

        mAddedSubMenu = false;

        for (int i = 0; i < mMenu.size(); i++) {
            MenuItem item = mMenu.getItem(i);
                addMenuItem(item,i,mItems.size());
        }

        mAddedSubMenu=false;
        return mItems;
    }

    void addMenuItem(MenuItem item,int i,int toPosition) {
        addMenuItem(item,i,toPosition,null,-1);
    }


    void addMenuItem(MenuItem item,int i,int toPosition,List<BottomSheetItem> bindsToAdd,int specificPos) {
        BottomSheetItem toAdd;
        List<BottomSheetItem> binds;
        if (bindsToAdd!=null) {
            binds=bindsToAdd;
        }
        else {
            binds=new ArrayList<BottomSheetItem>();
            mBinds.put(item,binds);
        }

        if (specificPos<0 || specificPos>binds.size()) {
            specificPos=binds.size();
        }

        if (item.isVisible()) {
            if (item.hasSubMenu()) {
                SubMenu subMenu = item.getSubMenu();

                if (i != 0 && mAddedSubMenu) {
                    if (mMode == BottomSheetBuilder.MODE_GRID) {
                        throw new IllegalArgumentException("MODE_GRID can't have submenus." +
                                " Use MODE_LIST instead");
                    }
                    addDivider(toPosition++,specificPos++,binds);
                }

                CharSequence title = item.getTitle();
                if (title != null && !title.equals("")) {
                    addHeader(title,toPosition++,specificPos++,binds);
                }

                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subItem = subMenu.getItem(j);
                    if (subItem.isVisible()) {
                        toAdd=new BottomSheetMenuItem(subItem, mItemTextColor,
                                mItemBackground, mIconTintColor);
                        mItems.add(toPosition++,toAdd);
                        binds.add(specificPos++,toAdd);
                        mAddedSubMenu = true;
                    }
                }
            } else {
                toAdd=new BottomSheetMenuItem(item, mItemTextColor, mItemBackground, mIconTintColor);
                mItems.add(toPosition,toAdd);
                binds.add(specificPos,toAdd);
            }
        }
    }

    void addHeader(CharSequence title,int itemsPos,int bindsPos, List<BottomSheetItem> binds) {
        BottomSheetItem toAdd=new BottomSheetHeader(title.toString(), mTitleTextColor);
        mItems.add(itemsPos,toAdd);
        binds.add(bindsPos,toAdd);
    }

    void addDivider(int itemsPos,int bindsPos, List<BottomSheetItem> binds) {
        BottomSheetItem toAdd=new BottomSheetDivider(mDividerBackground);
        mItems.add(itemsPos,toAdd);
        binds.add(bindsPos,toAdd);
    }

    boolean addedSubMenu() {
        return mAddedSubMenu;
    }

    public void setItemTextColor(int itemTextColor) {
        this.mItemTextColor = itemTextColor;
    }

    public void setItemClickListener(BottomSheetItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setTitleTextColor(int titleTextColor) {
        mTitleTextColor = titleTextColor;
    }

    public void setBackground(int background) {
        mBackgroundDrawable = background;
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public void setDividerBackground(int dividerBackground) {
        mDividerBackground = dividerBackground;
    }

    public void setItemBackground(int itemBackground) {
        mItemBackground = itemBackground;
    }

    public void setIconTintColorResource(int iconTintColorResource) {
        mIconTintColor = iconTintColorResource;
    }

    public void setIconTintColor(int iconTintColor) {
        mIconTintColor = iconTintColor;
    }

    HashMap<MenuItem, List<BottomSheetItem>> getBinds() {
        return mBinds;
    }

    int getBackgroundDrawable() {
        return mBackgroundDrawable;
    }

    int getBackgroundColor() {
        return mBackgroundColor;
    }

    int getDividerBackground() {
        return mDividerBackground;
    }

    int getItemBackground() {
        return mItemBackground;
    }

    int getItemTextColor() {
        return mItemTextColor;
    }

    int getTitleTextColor() {
        return mTitleTextColor;
    }

    int getIconTintColor() {
        return mIconTintColor;
    }

    BottomSheetItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    int getMode() {
        return mMode;
    }

    Menu getMenu() {
        return mMenu;
    }

    List<BottomSheetItem> getItems() {
        return mItems;
    }

    public void setEditorEnabled(boolean editorEnabled) {
        mEditorEnabled = editorEnabled;
    }
}
