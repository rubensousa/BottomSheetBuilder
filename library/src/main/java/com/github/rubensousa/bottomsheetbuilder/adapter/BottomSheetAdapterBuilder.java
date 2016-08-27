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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.R;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetAdapterBuilder {

    private int mMode;
    private Menu mMenu;
    private Context mContext;

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
    public View createView(int itemTextColor, int titleTextColor, int backgroundDrawable,
                           int backgroundColor, int dividerBackground, int itemBackground,
                           BottomSheetItemClickListener itemClickListener) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View sheet = mMode == BottomSheetBuilder.MODE_GRID ?
                layoutInflater.inflate(R.layout.bottomsheetbuilder_sheet_grid, null)
                : layoutInflater.inflate(R.layout.bottomsheetbuilder_sheet_list, null);


        final RecyclerView recyclerView = (RecyclerView) sheet.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        if (backgroundDrawable != 0) {
            recyclerView.setBackgroundResource(backgroundDrawable);
        } else {
            if (backgroundColor != 0) {
                recyclerView.setBackgroundColor(ContextCompat.getColor(mContext, backgroundColor));
            }
        }

        List<BottomSheetItem> items = createAdapterItems(dividerBackground, titleTextColor,
                itemTextColor, itemBackground);

        final BottomSheetItemAdapter adapter = new BottomSheetItemAdapter(items, mMode,
                itemClickListener);

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

    private List<BottomSheetItem> createAdapterItems(int dividerBackground, int titleTextColor,
                                                     int itemTextColor, int itemBackground) {
        List<BottomSheetItem> items = new ArrayList<>();

        boolean addedSubMenu = false;

        for (int i = 0; i < mMenu.size(); i++) {
            MenuItem item = mMenu.getItem(i);

            if (item.isVisible()) {
                if (item.hasSubMenu()) {
                    if (mMode == BottomSheetBuilder.MODE_GRID) {
                        throw new IllegalArgumentException("MODE_GRID can't have submenus." +
                                " Use MODE_LIST instead");
                    }
                    SubMenu subMenu = item.getSubMenu();

                    if (i != 0 && addedSubMenu) {
                        items.add(new BottomSheetDivider(dividerBackground));
                    }

                    CharSequence title = item.getTitle();
                    if (title != null && !title.equals("")) {
                        items.add(new BottomSheetHeader(title.toString(), titleTextColor));
                    }

                    for (int j = 0; j < subMenu.size(); j++) {
                        MenuItem subItem = subMenu.getItem(j);
                        if (subItem.isVisible()) {
                            items.add(new BottomSheetMenuItem(subItem, itemTextColor,
                                    itemBackground));
                            addedSubMenu = true;
                        }
                    }
                } else {
                    items.add(new BottomSheetMenuItem(item, itemTextColor, itemBackground));
                }
            }
        }

        return items;
    }

}
