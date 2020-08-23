package com.example.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;
//from https://blog.csdn.net/Afanbaby/article/details/79240620
public class BottomAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public BottomAdapter(FragmentManager fm){
        super(fm);
        this.fragments = new ArrayList<>();
    }
    @Override
    public Fragment getItem(int position){
        return fragments.get(position);
    }
    @Override
    public int getCount(){
        return fragments.size();
    }
    public void addFragment(Fragment fragment){
        fragments.add(fragment);
    }
}
