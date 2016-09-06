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
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.R;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BottomSheetAdapterBuilder {
    private BottomSheetColors mColors;

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

    public BottomSheetAdapterBuilder(Context context, BottomSheetColors colors) {
        mContext = context;
        mColors=colors;
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

        if (mColors.getBackground() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                recyclerView.setBackground(mColors.getBackground());
            }
            else
                //noinspection deprecation
                recyclerView.setBackgroundDrawable(mColors.getBackground());
        } else
                recyclerView.setBackgroundColor(mColors.getBackgroundColor());

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
                        toAdd=new BottomSheetMenuItem(subItem, mColors.getItemTextColor(),
                                mColors.getItemBackground(), mColors.getIconTintColor());
                        mItems.add(toPosition++,toAdd);
                        binds.add(specificPos++,toAdd);
                        mAddedSubMenu = true;
                    }
                }
            } else {
                toAdd=new BottomSheetMenuItem(item, mColors.getItemTextColor(), mColors.getItemBackground(), mColors.getIconTintColor());
                mItems.add(toPosition,toAdd);
                binds.add(specificPos,toAdd);
            }
        }
    }

    void addHeader(CharSequence title,int itemsPos,int bindsPos, List<BottomSheetItem> binds) {
        BottomSheetItem toAdd=new BottomSheetHeader(title.toString(), mColors.getTitleTextColor());
        mItems.add(itemsPos,toAdd);
        binds.add(bindsPos,toAdd);
    }

    void addDivider(int itemsPos,int bindsPos, List<BottomSheetItem> binds) {
        BottomSheetItem toAdd=new BottomSheetDivider(mColors.getDividerBackground());
        mItems.add(itemsPos,toAdd);
        binds.add(bindsPos,toAdd);
    }

    boolean addedSubMenu() {
        return mAddedSubMenu;
    }

    HashMap<MenuItem, List<BottomSheetItem>> getBinds() {
        return mBinds;
    }


    BottomSheetItemClickListener getItemClickListener() {
        return mItemClickListener;
    }


    public void setItemClickListener(BottomSheetItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
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

    public void setColors(BottomSheetColors colors) {
        mColors = colors;
    }

    public BottomSheetColors getColors() {
        return mColors;
    }
}
