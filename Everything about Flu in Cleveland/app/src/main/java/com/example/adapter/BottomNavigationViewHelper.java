package com.example.adapter;

import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;



//from https://blog.csdn.net/Afanbaby/article/details/79240620
public class BottomNavigationViewHelper {

    public static void disableShiftMode(BottomNavigationView view){
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView,false);
            shiftingMode.setAccessible(false);
            for (int i = 0;i < menuView.getChildCount();i++){
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShifting(false);
                item.setChecked(item.getItemData().isChecked());
            }
        }catch (NoSuchFieldException e){
            Log.e("bnHelper","unable to get shift mode field",e);
        }catch (IllegalAccessException e){
            Log.e("bnHelper","unable to change value shift mode",e);

        }
    }
}