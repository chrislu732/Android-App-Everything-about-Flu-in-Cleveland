package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.adapter.BottomAdapter;
import com.example.adapter.BottomNavigationViewHelper;
import com.example.fragment.Tab1Fragment;
import com.example.fragment.Tab2Fragment;
import com.example.fragment.Tab3Fragment;
import com.example.fragment.Tab4Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBv;
    private ViewPager mVp;
    private BottomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView() {
        mBv = (BottomNavigationView) findViewById(R.id.main_ac_bottomNavigationView);
        mVp = (ViewPager) findViewById(R.id.vp);
        BottomNavigationViewHelper.disableShiftMode(mBv);

        //这里可true是一个消费过程，同样可以使用break，外部返回true也可以
        mBv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_tab1:
                        mVp.setCurrentItem(0,false);
                        break;

                    case R.id.item_tab2:
                        mVp.setCurrentItem(1,false);
                        break;

                    case R.id.item_tab3:
                        mVp.setCurrentItem(2,false);
                        break;

                    case R.id.item_tab4:
                        mVp.setCurrentItem(3,false);
                        break;
                }
                return true;
            }
        });
        setupViewPager(mVp);
        int limit = (adapter.getCount()>1?adapter.getCount()-1:1);
        mVp.setOffscreenPageLimit(limit);
        //ViewPager监听
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBv.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void setupViewPager(ViewPager viewPager){
        adapter = new BottomAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment());
        adapter.addFragment(new Tab2Fragment());
        adapter.addFragment(new Tab3Fragment());
        adapter.addFragment(new Tab4Fragment());
        viewPager.setAdapter(adapter);
    }
}

