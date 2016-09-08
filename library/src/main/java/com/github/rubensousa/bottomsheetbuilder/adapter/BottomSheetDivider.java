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


import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;


class BottomSheetDivider implements BottomSheetItem {

    private Drawable mBackgroundDrawable;

    public BottomSheetDivider(Drawable background) {
        mBackgroundDrawable = background;
    }

    public Drawable getBackground() {
        return mBackgroundDrawable;
    }

    @Override
    public String getTitle() {
        return "";
    }
}
