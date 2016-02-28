# BottomSheetBuilder
A simple library that creates BottomSheets according to the Material Design specs

## How to use

- Add the following to your build.gradle:

        repositories{
          maven { url "https://jitpack.io" }
        }
        
        dependencies {
          compile 'com.github.rubensousa:BottomSheetBuilder:0.1'
        }


- Create a view (the builder already inflates the view inside the coordinatorLayout):

        View bottomSheet = new BottomSheetBuilder(context, coordinatorLayout)
                .setMode(BottomSheetBuilder.MODE_GRID)
                .setBackgroundColor(android.R.color.white)
                .setMenu(R.menu.menu_bottom_grid_sheet)
                .setItemListener(this)
                .createView();

- Create a BottomSheetDialog:

        BottomSheetDialog dialog = new BottomSheetBuilder(context, R.style.AppTheme_BottomSheetDialog)
                      .setMode(BottomSheetBuilder.MODE_LIST)
                      .setBackgroundColor(android.R.color.white)
                      .setMenu(R.menu.menu_bottom_simple_sheet)
                      .setItemListener(new BottomSheetItemAdapter.BottomSheetItemListener() {
                          @Override
                          public void onBottomSheetItemClick(BottomSheetMenuItem item) {
                              mBottomSheetDialog.dismiss();
                          }
                      })
                      .createDialog();
                      
        dialog.show();

## Styling

Make sure the style passed in the BottomSheetBuilder's constructor extends from Theme.Design.BottomSheetDialog:

    <style name="AppTheme.BottomSheetDialog" parent="Theme.Design.BottomSheetDialog">
        <!-- This changes the overlay background -->
        <item name="android:background">@color/colorAccent</item>
    </style>

## Sample

The sample includes 3 view modes: grid, list and list with one submenu. Check the screenshots below.

## Customization methods

    setItemTextColor(@ColorRes int color)
    setTitleTextColor(@ColorRes int color)
    setBackground(@DrawableRes int background)
    setBackgroundColor(@ColorRes int background)
    setDividerBackground(@DrawableRes int background)
    setItemBackground(@DrawableRes int background)

## Known bugs

There's some bugs that affect the BottomSheetDialog and the BottomSheetBehavior in the Support Library v23.2.0:

- https://code.google.com/p/android/issues/detail?id=201793
- https://code.google.com/p/android/issues/detail?id=201825
- https://code.google.com/p/android/issues/detail?id=201826

## Screenshots
<img src="screens/sheet-list-submenu.png" width="350"> <img src="screens/sheet-list-simple.png" width="350">
<img src="screens/sheet-grid.png" width="350"> 
