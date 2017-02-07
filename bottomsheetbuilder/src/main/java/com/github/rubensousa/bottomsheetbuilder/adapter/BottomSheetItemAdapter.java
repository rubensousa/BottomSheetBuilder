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

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.R;

import java.util.List;


class BottomSheetItemAdapter extends RecyclerView.Adapter<BottomSheetItemAdapter.ViewHolder> {

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_DIVIDER = 2;

    private List<BottomSheetItem> mItems;
    BottomSheetItemClickListener mListener;
    private int mMode;
    private int mItemWidth;

    public BottomSheetItemAdapter(List<BottomSheetItem> items, int mode,
                                  BottomSheetItemClickListener listener) {
        mMode = mode;
        mItems = items;
        mListener = listener;
    }

    public void setItemWidth(int width) {
        mItemWidth = width;
    }

    public void setListener(BottomSheetItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        BottomSheetItem item = mItems.get(position);

        if (item instanceof BottomSheetMenuItem) {
            return TYPE_ITEM;
        } else if (item instanceof BottomSheetDivider) {
            return TYPE_DIVIDER;
        } else if (item instanceof BottomSheetHeader) {
            return TYPE_HEADER;
        }

        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mMode == BottomSheetBuilder.MODE_GRID) {
            View layout = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bottomsheetbuilder_grid_adapter, parent, false);

            ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
            layoutParams.width = mItemWidth;
            layout.setLayoutParams(layoutParams);
            return new ItemViewHolder(layout);
        }

        if (mMode == BottomSheetBuilder.MODE_LIST) {

            if (viewType == TYPE_HEADER) {
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.bottomsheetbuilder_list_header, parent, false));
            }

            if (viewType == TYPE_ITEM) {
                return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.bottomsheetbuilder_list_adapter, parent, false));
            }

            if (viewType == TYPE_DIVIDER) {
                return new DividerViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.bottomsheetbuilder_list_divider, parent, false));
            }

        }

        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bottomsheetbuilder_list_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BottomSheetItem item = mItems.get(position);

        if (mMode == BottomSheetBuilder.MODE_LIST) {
            if (holder.getItemViewType() == TYPE_ITEM) {
                ((ItemViewHolder) holder).setData((BottomSheetMenuItem) item);
            } else if (holder.getItemViewType() == TYPE_HEADER) {
                ((HeaderViewHolder) holder).setData((BottomSheetHeader) item);
            } else if (holder.getItemViewType() == TYPE_DIVIDER) {
                ((DividerViewHolder) holder).setData((BottomSheetDivider) item);
            }
        } else {
            ((ItemViewHolder) holder).setData((BottomSheetMenuItem) item);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

    public class DividerViewHolder extends ViewHolder {

        public View divider;

        public DividerViewHolder(View itemView) {
            super(itemView);
            divider = itemView;
        }

        public void setData(BottomSheetDivider item) {
            int background = item.getBackground();
            if (background != 0) {
                divider.setBackgroundResource(background);
            }
        }
    }

    public class HeaderViewHolder extends ViewHolder {

        public TextView textView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }

        public void setData(BottomSheetHeader item) {
            textView.setText(item.getTitle());
            int color = item.getTextColor();

            if (color != 0) {
                textView.setTextColor(color);
            }
        }
    }


    public class ItemViewHolder extends ViewHolder implements View.OnClickListener {

        public AppCompatImageView imageView;
        public TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (AppCompatImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }

        public void setData(BottomSheetMenuItem item) {
            imageView.setImageDrawable(item.getIcon());
            textView.setText(item.getTitle());
            int color = item.getTextColor();
            int background = item.getBackground();

            if (color != 0) {
                textView.setTextColor(color);
            }

            if (background != 0) {
                itemView.setBackgroundResource(background);
            }

        }

        @Override
        public void onClick(View v) {
            BottomSheetMenuItem item = (BottomSheetMenuItem) mItems.get(getLayoutPosition());

            if (mListener != null) {
                mListener.onBottomSheetItemClick(item.getMenuItem());
            }
        }
    }
}
