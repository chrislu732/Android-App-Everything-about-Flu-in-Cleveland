package com.example.adapter;


import com.example.afinal.solver;
import com.example.bean.QuestionBean;
import com.example.fragment.QuestionItemFragment;
import com.example.fragment.ScantronItemFragment;
import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class ItemAdapter extends FragmentStatePagerAdapter {
    Context context;
    public ItemAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int arg0) {
        if(arg0 == solver.questionlist.size()){
            return new ScantronItemFragment();
        }
        return new QuestionItemFragment(arg0);
    }


    @Override
    public int getCount() {
        return solver.questionlist.size()+1;
    }
}