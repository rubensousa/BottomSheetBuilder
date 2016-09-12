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

package com.github.rubensousa.bottomsheetbuilder.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.BottomSheetBehavior;

public class BottomSheetBuilderUtils {

    public static final String SAVED_STATE = "saved_behavior_state";

    public static void delayDismiss(final BottomSheetBehavior behavior) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }, 300);
    }

    public static void saveState(Bundle outState, BottomSheetBehavior behavior) {
        if (outState != null) {
            outState.putInt(SAVED_STATE, behavior.getState());
        }
    }

    public static void restoreState(final Bundle savedInstanceState,
                                    final BottomSheetBehavior behavior) {
        if (savedInstanceState != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int state = savedInstanceState.getInt(SAVED_STATE);
                    if (state == BottomSheetBehavior.STATE_EXPANDED && behavior != null) {
                        behavior.setState(state);
                    }
                }
            }, 300);
        }
    }
}
