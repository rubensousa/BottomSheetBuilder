# BottomSheetBuilder
A simple library that creates BottomSheets according to the Material Design specs: https://material.google.com/components/bottom-sheets.html

Available from API 14.

## Screenshots
<img src="screens/demo.gif" width="350">

<img src="screens/tablet_demo.gif">

<img src="screens/tablet_grid.png">

## How to use

- Add the following to your build.gradle:
```groovy
repositories{
  maven { url "https://jitpack.io" }
}

dependencies {
  compile 'com.github.rubensousa:BottomSheetBuilder:1.3'
}
```

- Create a view (the builder already inflates the view inside the coordinatorLayout):
```java
View bottomSheet = new BottomSheetBuilder(context, coordinatorLayout)
        .setMode(BottomSheetBuilder.MODE_GRID)
        .setBackgroundColor(android.R.color.white)
        .setMenu(R.menu.menu_bottom_grid_sheet)
        .setItemClickListener(this)
        .createView();
```
- Create a BottomSheetMenuDialog:
```java
BottomSheetMenuDialog dialog = new BottomSheetBuilder(context, R.style.AppTheme_BottomSheetDialog)
              .setMode(BottomSheetBuilder.MODE_LIST)
              .setMenu(R.menu.menu_bottom_simple_sheet)
              .setItemClickListener(new BottomSheetItemClickListener() {
                        @Override
                        public void onBottomSheetItemClick(MenuItem item) {
                                
                        }
                })
              .createDialog();
              
dialog.show();
```
- If you have a long view, you should consider adding the AppBarLayout to the builder so that the dialog doesn't overlap with it:

```java
BottomSheetMenuDialog dialog = new BottomSheetBuilder(context, R.style.AppTheme_BottomSheetDialog)
              .setAppBarLayout(appbar)
              ...
```
- If you want to expand the dialog automatically:

```java
BottomSheetMenuDialog dialog = new BottomSheetBuilder(context, R.style.AppTheme_BottomSheetDialog)
              .expandOnStart(true)
              ...
```

- If you want to tint the menu icons:
BottomSheetMenuDialog dialog = new BottomSheetBuilder(context, R.style.AppTheme_BottomSheetDialog)
              .setIconTintColorResource(R.color.colorPrimary)
              ...

## Styling

Make sure the style passed in the BottomSheetBuilder's constructor extends from the Theme.Design.BottomSheetDialog family:
```xml
    <style name="AppTheme.BottomSheetDialog" parent="Theme.Design.Light.BottomSheetDialog">
        <item name="bottomSheetStyle">@style/AppTheme.BottomSheetStyle</item>
    </style>

    <style name="AppTheme.BottomSheetStyle" parent="Widget.Design.BottomSheet.Modal">
        <item name="android:background">@android:color/white</item>
        <item name="behavior_hideable">true</item>
        <item name="behavior_skipCollapsed">true</item>
    </style>
```
## Sample

The sample includes 4 view modes: grid, list, long list and list with one submenu.
It also has a save/restore state example.

## Customization methods
```java
setItemTextColor(@ColorRes int color)
setTitleTextColor(@ColorRes int color)
setIconTintColorResource(@ColorRes int color)
setIconTintColor(int color)
setBackground(@DrawableRes int background)
setBackgroundColor(@ColorRes int background)
setDividerBackground(@DrawableRes int background)
setItemBackground(@DrawableRes int background)
setAppBarLayout(AppBarLayout appbar) -> To avoid overlapping
expandOnStart(boolean expand) -> Defaults to false
delayDismissOnItemClick(boolean delayDismiss) -> Defaults to true
```

## Changelog

https://github.com/rubensousa/BottomSheetBuilder/blob/master/CHANGELOG.md

## License

    Copyright 2016 Rúben Sousa
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
