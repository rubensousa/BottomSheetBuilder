package com.github.rubensousa.bottomsheetbuilder.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class BottomSheetEditor {
    private List<BottomSheetItem> mItems;
    private HashMap<MenuItem,List<BottomSheetItem>> mBinds;
    private BottomSheetAdapterBuilder mBuilder;
    private BottomSheetItemAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Menu mMenu;
    private boolean searchIsSubItemFlag;
    private MenuItem searchMenuItem;
    private int searchResultItemsPos;
    private int searchResultBindsOffsetPos;
    private Comparator<MenuItem> mComparator;
    private Comparator<MenuItem> mIdComparator=new Comparator<MenuItem>() {
        @Override
        public int compare(MenuItem menuItem, MenuItem t1) {
            return menuItem.getItemId()==t1.getItemId()?0:-1;
        }
    };


    public BottomSheetEditor(BottomSheetAdapterBuilder builder) {
        mBuilder=builder;
        mBinds=builder.getBinds();
        mItems=builder.getItems();
        mMenu=builder.getMenu();
        mComparator=mIdComparator;
    }

    public void setComparator(Comparator<MenuItem> comparator) {
        mComparator=comparator;
    }

    void setItems(List<BottomSheetItem> items) {
        mItems=items;
    }

    void setAdapter(BottomSheetItemAdapter adapter) {
        mAdapter=adapter;
    }

    void setRecycler(RecyclerView recycler) {
        mRecyclerView = recycler;
    }

    public void setBottomSheetFixedSize(boolean fixedSize) {
        mRecyclerView.setHasFixedSize(fixedSize);
    }

    public void changeItem(MenuItem item) {
        changeItem(item,true);
    }

    public void changeItem(MenuItem item,String title,Drawable icon) {
        changeItem(item,title,icon,true);
    }

    public void changeItem(MenuItem item,String title) {
        changeItem(item,title,null,true);
    }

    private void changeItem(MenuItem item, String title, Drawable icon, boolean notify) {
        MenuItem menu=getMenuItemWithSameID(item,true);
        if (menu==null)
            throw new NoSuchElementException("Item is not found in menu tree.");
        else {
            boolean isTitleNewEmpty=(title==null || title.compareTo("")==0);

            //in case title cannot be empty
            /*if (isTitleNewEmpty) {
                if (!menu.hasSubMenu() && !searchIsSubItemFlag)
                    throw new IllegalArgumentException("Menu must have a title, unless it has submenu.");
                else if (searchIsSubItemFlag)
                    throw new IllegalArgumentException("Submenu must have a title.");
            */

            if (title!=null) menu.setTitle(title);
            if (icon!=null) menu.setIcon(icon);

            if (searchResultBindsOffsetPos>=0) {
                mBinds.get(searchMenuItem).remove(searchResultBindsOffsetPos);
                mItems.remove(searchResultItemsPos);
                //menu that has BottomSheetHeader title but now it shouldnt have
                if (menu==searchMenuItem && !searchIsSubItemFlag && menu.hasSubMenu() && isTitleNewEmpty) {
                    if (notify) mAdapter.notifyItemRemoved(searchResultItemsPos);
                } else {
                    if (menu.hasSubMenu())
                        mBuilder.addHeader(menu.getTitle(),searchResultItemsPos,searchResultBindsOffsetPos,mBinds.get(menu));
                    else
                        mBuilder.addMenuItem(menu,Integer.MAX_VALUE,searchResultItemsPos,mBinds.get(searchMenuItem),searchResultBindsOffsetPos);
                    if (notify) mAdapter.notifyItemChanged(searchResultItemsPos);
                }
            }
            else if (!isTitleNewEmpty) {
                int posToAdd=mBinds.get(menu).size()-menu.getSubMenu().size();
                mBuilder.addHeader(menu.getTitle(),searchResultItemsPos+posToAdd,posToAdd,mBinds.get(menu));
                if (notify) mAdapter.notifyItemChanged(searchResultItemsPos);
            }
        }
    }

    private void changeItem(MenuItem item,boolean notify) {
        changeItem(item,item.getTitle().toString(),item.getIcon(),notify);
    }


    public void removeItem(MenuItem item) {
        removeItem(item,true,true);
    }

    private void removeItem(MenuItem item,boolean notify,boolean removeFromMenu) {
        if (getMenuItemWithSameID(item,false)!=null) {
            int pos= getMenuItemPos(item);
            removeItem(pos,notify,removeFromMenu);
        }
        else
            //look for item in subMenus
            for (MenuItem i:mBinds.keySet())
                if (i.hasSubMenu()) {
                    SubMenu j = i.getSubMenu();
                    for (int k=0;k<j.size();++k)
                        if (mComparator.compare(j.getItem(k),item)==0) {
                            //last submenu then remove the menu
                            if (j.size()==1)
                                removeItem(getMenuItemPos(i,mIdComparator),notify,removeFromMenu);
                            else {
                                int posInBinds=getSubItemBindsPos(mBinds.get(i),k,j);
                                int index=mItems.indexOf(mBinds.get(i).get(posInBinds));
                                mItems.remove(index);
                                mBinds.get(i).remove(posInBinds);
                                if (removeFromMenu)
                                    j.removeItem(j.getItem(k).getItemId());
                                if (notify)
                                    mAdapter.notifyItemRemoved(index);
                            }
                            return;
                        }
                }
    }

    private int getSubItemBindsPos(List<BottomSheetItem> bottomSheetItems, int posInSubMenu, SubMenu subMenu) {
        return bottomSheetItems.size()-subMenu.size()+posInSubMenu;
    }

    private MenuItem getMenuItemWithSameID(MenuItem item,boolean searchSubMenus) {
        for (MenuItem i:mBinds.keySet())
            if (mComparator.compare(i,item)==0) {
                searchIsSubItemFlag=false;
                if (i.hasSubMenu()) {
                    searchResultItemsPos=findHeaderInSubMenu(mItems.indexOf(mBinds.get(i).get(0)),mBinds.get(i).size());
                    searchResultBindsOffsetPos =searchResultItemsPos-mItems.indexOf(mBinds.get(i).get(0));
                }
                else {
                    searchResultItemsPos=mItems.indexOf(mBinds.get(i).get(0));
                    searchResultBindsOffsetPos=0;
                }
                searchMenuItem=i;
                return i;
            }
            else if (i.hasSubMenu() && searchSubMenus) {
                SubMenu j = i.getSubMenu();
                for (int k=0;k<j.size();++k)
                    if (mComparator.compare(j.getItem(k),item)==0) {
                        List<BottomSheetItem> bind=mBinds.get(i);
                        searchResultItemsPos=mItems.indexOf(bind.get(bind.size()-j.size()+k));
                        searchIsSubItemFlag=true;
                        searchResultBindsOffsetPos =searchResultItemsPos-mItems.indexOf(bind.get(0));
                        searchMenuItem=i;
                        return j.getItem(k);
                    }
            }
        return null;
    }

    private int findHeaderInSubMenu(int posInItems, int size) {
        for (int i=posInItems;i<posInItems+size;++i) {
            if (mItems.get(i) instanceof BottomSheetHeader)
                return i;
        }
        return -1;
    }


    private int getMenuItemPos(MenuItem item) {
        return getMenuItemPos(item,mComparator);
    }

    private int getMenuItemPos(MenuItem item,Comparator<MenuItem> comparator) {
        int i=0;
        while (i<mMenu.size() && comparator.compare(mMenu.getItem(i++),item)!=0);

        //item not found
        if (--i==mMenu.size())
            return -1;
        else
            return i;
    }


    public void removeItem(int position) {
        removeItem(position,true,true);
    }

    private void removeItem(int position,boolean notify,boolean removeFromMenu) {
        MenuItem menuToBeRemoved=mMenu.getItem(position);
        List<BottomSheetItem> itemsToBeRemoved=mBinds.get(menuToBeRemoved);
        mBinds.remove(menuToBeRemoved);

        int positionOfMenu=mItems.indexOf(itemsToBeRemoved.get(0));
        int numberOfItemsToBeRemoved=itemsToBeRemoved.size();

        mItems.removeAll(itemsToBeRemoved);

        if (!hasSubMenuBefore(position) && mMenu.size()>position+1 && mMenu.getItem(position+1).hasSubMenu()) {
            List<BottomSheetItem> list=mBinds.get(mMenu.getItem(position + 1));
            if (list.get(0) instanceof BottomSheetDivider) {
                BottomSheetItem item = list.remove(0);
                mItems.remove(item);
                numberOfItemsToBeRemoved++;
            }
        }
        if (removeFromMenu) mMenu.removeItem(menuToBeRemoved.getItemId());
        if (notify) mAdapter.notifyItemRangeRemoved(positionOfMenu,numberOfItemsToBeRemoved);
    }

    public void addItem(MenuItem item,int position) {
        addItem(item,position,true);
    }

    public void addItem(MenuItem item) {
        addItem(item,mMenu.size());
    }

    private void addItem(MenuItem item,int position,boolean notify) {
        if (item.hasSubMenu() && mBuilder.getMode()== BottomSheetBuilder.MODE_GRID) throw new IllegalArgumentException("MODE_GRID can't have submenus.Use MODE_LIST instead");
        if (hasSubMenuBefore(mMenu.size())) {
            mBuilder.setAddedSubMenu(true);
        }
        else {
            mBuilder.setAddedSubMenu(false);
        }
        //item.hasSubMenu - no sub-sub menus allowed
        if (item.hasSubMenu() || position>=mMenu.size() || !mMenu.getItem(position).hasSubMenu()) {
            //item will be placed at the end of menus

            //new MenuItem has to be created
            MenuItem newItem;
            if (item.hasSubMenu()) {
                mMenu.addSubMenu(item.getGroupId(), item.getItemId(), item.getOrder(), item.getTitle().toString()).setIcon(item.getIcon());
                newItem=mMenu.getItem(mMenu.size()-1);
                inflateSubMenu(newItem.getSubMenu(),item.getSubMenu());
            }
            else
                newItem=mMenu.add(item.getGroupId(),item.getItemId(),item.getOrder(),item.getTitle().toString()).setIcon(item.getIcon());
            int oldSize=mItems.size();
            mBuilder.addMenuItem(newItem,mMenu.size(),mItems.size());
            if (notify) mAdapter.notifyItemRangeInserted(oldSize,mItems.size()-oldSize);
        }
        else {
            MenuItem newItem;
            //item can be placed at the and of a submenu
            newItem=mMenu.getItem(position).getSubMenu().add(item.getGroupId(),item.getItemId(),item.getOrder(),item.getTitle().toString()).setIcon(item.getIcon());
            int subMenuSize=mMenu.getItem(position).getSubMenu().size();
            MenuItem subMenuItem=mMenu.getItem(position);

            BottomSheetItem subMenuLastBottomSheetItem=mBinds.get(subMenuItem).get(mBinds.get(subMenuItem).size()-1);

            int index = mItems.indexOf(subMenuLastBottomSheetItem)+1;
            mBuilder.addMenuItem(newItem,position,index,mBinds.get(subMenuItem),-1);
            if (notify) mAdapter.notifyItemInserted(mItems.indexOf(subMenuLastBottomSheetItem)+1);
        }
    }

    private boolean hasSubMenuBefore(int position) {
        if (mMenu.size()>=position || position<=0) {
            for (int i=0;i<position;++i) {
                if (mMenu.getItem(i).hasSubMenu())
                    return true;
            }
            return false;
        }
        else
            return false;
    }

    private void inflateSubMenu(SubMenu subMenuToBeInflated, SubMenu subMenu) {
        for (int i=0;i<subMenu.size();++i) {
            MenuItem item=subMenu.getItem(i);
            subMenuToBeInflated.add(item.getGroupId(),item.getItemId(),item.getOrder(),item.getTitle()).setIcon(item.getIcon());
        }
    }

    public Menu getMenu() {
        return mMenu;
    }

    public BottomSheetColors getColors() {
        return mBuilder.getColors();
    }

    public int getMode() {
        return mBuilder.getMode();
    }

    /*
    @Deprecated
    private int findMenuItem(int bottomSheetItemsStart,int menuItemPosition) {
        int menuItemPosIter=-1;
        SubMenu subMenu;

        for (int i=bottomSheetItemsStart;i<mItems.size();++i) {
            if (mItems.get(i) instanceof BottomSheetHeader) {
                if (++menuItemPosIter==menuItemPosition) {
                    if (i-1>-1 &&  mItems.get(i-1) instanceof BottomSheetDivider)
                        i--;
                    return i;
                }
                else if (mMenu.getItem(menuItemPosIter).hasSubMenu()) {
                    i=i+mMenu.getItem(menuItemPosIter).getSubMenu().size();
                }
                else {
                    throw new IllegalStateException("BottomSheet header should have submenu.");
                }
            }
            else if (mItems.get(i) instanceof BottomSheetMenuItem) {
                 if (++menuItemPosIter==menuItemPosition)
                        return i;
                }
            }
        return mItems.size();
    }

    @Deprecated
    private int findMenuItem(int bottomSheetItemsStart,MenuItem item) {
        int menuItemPosIter=-1;

        for (int i=bottomSheetItemsStart;i<mItems.size();++i) {
            if (mItems.get(i) instanceof BottomSheetHeader) {
                if (mMenu.getItem(++menuItemPosIter)==item) {
                    if (i-1>-1 && mItems.get(i-1) instanceof BottomSheetDivider)
                        i--;
                    return i;
                }
                else if (mMenu.getItem(menuItemPosIter).hasSubMenu()) {
                    SubMenu subMenu=mMenu.getItem(menuItemPosIter).getSubMenu();
                    for (int j=i;j<i+subMenu.size();++j)
                        if (subMenu.getItem(j)==item) {
                            searchIsSubItemFlag=true;
                            subItemPos=menuItemPosIter;
                            return j;
                        }
                    i=i+subMenu.size();
                }
                else {
                    throw new IllegalStateException("BottomSheet header should have submenu.");
                }
            }
            else if (mItems.get(i) instanceof BottomSheetMenuItem) {
                if (mMenu.getItem(++menuItemPosIter)==item)
                    return i;
            }
        }
        return mItems.size();
    }

    @Deprecated
    private int normalizePoition(int position) {
        if (position<0)
            return 0;
        else if (position>=mMenu.size())
            return mMenu.size();
        else
            return position;
    }*/

}
