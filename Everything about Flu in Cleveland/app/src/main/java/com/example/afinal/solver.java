package com.example.afinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.adapter.ItemAdapter;
import com.example.bean.QuestionBean;
import com.example.bean.QuestionOptionBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

//from https://github.com/wildcreek/MultiChoice
public class solver extends FragmentActivity{
    public static int total_score = 0;
    List<View> list = new ArrayList<>();
    public static List<QuestionBean> questionlist = new ArrayList<>();
    private static QuestionBean question;
    public List<QuestionOptionBean> options1 = new ArrayList<>();
    public List<QuestionOptionBean> options2 = new ArrayList<QuestionOptionBean>();
    public static QuestionOptionBean option;
    private ViewPager vp;
    private ItemAdapter pagerAdapter;
    View pager_item;
    public static int currentIndex = 0;
    private TextView tv_time;
    private TextView tv_share;
    private TextView tv_answercard;
    private TextView tv_back;
    private List<QuestionOptionBean> options3 = new ArrayList<>();
    private List<QuestionOptionBean> options4 = new ArrayList<>();
    private List<QuestionOptionBean> options5 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solver);
        loadData();

        vp = (ViewPager) findViewById(R.id.vp);

        vp.setCurrentItem(0);
        pagerAdapter = new ItemAdapter(getSupportFragmentManager());
        vp.setAdapter(pagerAdapter);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            @Override
            public void onPageScrollStateChanged(int position) {
                currentIndex = position;
            }
        });
        IntentFilter filter = new IntentFilter();
        {
            filter.addAction("com.leyikao.jumptonext");
            filter.addAction("com.leyikao.jumptopage");
            filter.addAction("question corrected");
        }
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(mMessageReceiver, filter);
    }


    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        super.onDestroy();
    }

    protected void onPause(){
        questionlist.clear();
        super.onPause();
    }



    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.leyikao.jumptonext")) {
                jumpToNext() ;
            } else if (intent.getAction().equals("com.leyikao.jumptopage")) {
                int index = intent.getIntExtra("index", 0);
                jumpToPage(index);
            }else if(intent.getAction().equals("question corrected")){
                total_score++;
                Log.i("TAG",String.valueOf(total_score));
            }
        }
    };

    public void jumpToNext() {
        int position = vp.getCurrentItem();
        vp.setCurrentItem(position + 1);

    }
    public void jumpToPage(int index) {
        vp.setCurrentItem(index);
    }

    private void loadData() {
        option = new QuestionOptionBean("A", "Fever over 102F");
        options1.add(option);
        option = new QuestionOptionBean("B", "headache");
        options1.add(option);
        option = new QuestionOptionBean("C", "cough");
        options1.add(option);
        option = new QuestionOptionBean("D", "Extreme Exhaustion");
        options1.add(option);
        option = new QuestionOptionBean("E", "watery eyes");
        options1.add(option);
        option = new QuestionOptionBean("F", "Green or yellow nasal discharge");
        options1.add(option);
        question = new QuestionBean("0001", "Which of the following are the symptoms of the flu?"
                , 2, "flu cause", "001", options1,"0123");
        questionlist.add(question);

        // 初始化数据
        option = new QuestionOptionBean("T", "Yes");
        options2.add(option);
        option = new QuestionOptionBean("F", "No");
        options2.add(option);
        question = new QuestionBean("0002", "Should pregnant women get vaccinated？", 1, "vaccination", "001",
                options2,"0");
        questionlist.add(question);

        option = new QuestionOptionBean("T", "Yes");
        options3.add(option);
        option = new QuestionOptionBean("F", "No");
        options3.add(option);
        question = new QuestionBean("0003", "Can children younger than 6 months of age get a flu shot?"
                , 1, "vaccination", "001", options3,"1");
        questionlist.add(question);

        // 初始化数据
        option = new QuestionOptionBean("A", "October to March");
        options4.add(option);
        option = new QuestionOptionBean("B", "April to September");
        options4.add(option);
        question = new QuestionBean("0004", "When is the best time to get a flu shot in U.S.?"
                , 1, "nation policy", "001", options4,"1");
        questionlist.add(question);

        option = new QuestionOptionBean("T", "Yes");
        options5.add(option);
        option = new QuestionOptionBean("F", "No");
        options5.add(option);
        question = new QuestionBean("0005", "Can the vaccine give me the flu?"
                , 1, "vaccination", "001", options5,"0");
        questionlist.add(question);
    }
}
