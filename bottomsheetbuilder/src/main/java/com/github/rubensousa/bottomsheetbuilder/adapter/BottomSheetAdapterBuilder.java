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
import android.widget.TextView;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.R;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetAdapterBuilder {

    private int mTitles;
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
                           int tintColor, BottomSheetItemClickListener itemClickListener) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View sheet = mMode == BottomSheetBuilder.MODE_GRID ?
                layoutInflater.inflate(R.layout.bottomsheetbuilder_sheet_grid, null)
                : layoutInflater.inflate(R.layout.bottomsheetbuilder_sheet_list, null);


        final RecyclerView recyclerView = (RecyclerView) sheet.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);


        if (backgroundDrawable != 0) {
            sheet.setBackgroundResource(backgroundDrawable);
        } else {
            if (backgroundColor != 0) {
                sheet.setBackgroundColor(backgroundColor);
            }
        }

        List<BottomSheetItem> items = createAdapterItems(dividerBackground, titleTextColor,
                itemTextColor, itemBackground, tintColor);

        // If we only have one title and it's the first item, set it as fixed
        if (mTitles == 1 && mMode == BottomSheetBuilder.MODE_LIST) {
            BottomSheetItem header = items.get(0);
            TextView headerTextView = (TextView) sheet.findViewById(R.id.textView);
            if (header instanceof BottomSheetHeader) {
                headerTextView.setVisibility(View.VISIBLE);
                headerTextView.setText(header.getTitle());
                if (titleTextColor != 0) {
                    headerTextView.setTextColor(titleTextColor);
                }
                items.remove(0);
            }
        }

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
                                                     int itemTextColor, int itemBackground,
                                                     int tintColor) {
        List<BottomSheetItem> items = new ArrayList<>();
        mTitles = 0;

        boolean addedSubMenu = false;

        for (int i = 0; i < mMenu.size(); i++) {
            MenuItem item = mMenu.getItem(i);

            if (item.isVisible()) {
                if (item.hasSubMenu()) {
                    SubMenu subMenu = item.getSubMenu();

                    if (i != 0 && addedSubMenu) {
                        if (mMode == BottomSheetBuilder.MODE_GRID) {
                            throw new IllegalArgumentException("MODE_GRID can't have submenus." +
                                    " Use MODE_LIST instead");
                        }
                        items.add(new BottomSheetDivider(dividerBackground));
                    }

                    CharSequence title = item.getTitle();
                    if (title != null && !title.equals("")) {
                        items.add(new BottomSheetHeader(title.toString(), titleTextColor));
                        mTitles++;
                    }

                    for (int j = 0; j < subMenu.size(); j++) {
                        MenuItem subItem = subMenu.getItem(j);
                        if (subItem.isVisible()) {
                            items.add(new BottomSheetMenuItem(subItem, itemTextColor,
                                    itemBackground, tintColor));
                            addedSubMenu = true;
                        }
                    }
                } else {
                    items.add(new BottomSheetMenuItem(item, itemTextColor, itemBackground, tintColor));
                }
            }
        }

        return items;
    }

}
