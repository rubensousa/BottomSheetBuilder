# BottomSheetBuilder
A simple library that creates BottomSheets according to the Material Design specs

Available from API 14.

## Screenshots
<img src="screens/demo.gif" width="350">

<img src="screens/sheet-list-submenu.png" width="250"><img src="screens/sheet-list-simple.png" width="250">

## How to use

- Add the following to your build.gradle:
```groovy
repositories{
  maven { url "https://jitpack.io" }
}

dependencies {
  compile 'com.github.rubensousa:BottomSheetBuilder:1.2'
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
              .setBackgroundColor(android.R.color.white)
              .setMenu(R.menu.menu_bottom_simple_sheet)
              .setItemClickListener(new BottomSheetItemClickListener() {
                        @Override
                        public void onBottomSheetItemClick(MenuItem item) {
                                
                        }
                })
              .createDialog();
              
dialog.show();
```

## Styling

Make sure the style passed in the BottomSheetBuilder's constructor extends from Theme.Design.BottomSheetDialog:
```xml
<style name="AppTheme.BottomSheetDialog" parent="Theme.Design.BottomSheetDialog">
    <!-- This changes the overlay background -->
    <item name="android:background">@color/colorAccent</item>
</style>
```
## Sample

The sample includes 3 view modes: grid, list and list with one submenu.
It also has a save/restore state example.

## Customization methods
```java
setItemTextColor(@ColorRes int color)
setTitleTextColor(@ColorRes int color)
setBackground(@DrawableRes int background)
setBackgroundColor(@ColorRes int background)
setDividerBackground(@DrawableRes int background)
setItemBackground(@DrawableRes int background)
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
