package com.example.afinal;

import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//from https://github.com/wildcreek/MultiChoice
public class ResultReportActivity extends FragmentActivity {
    LocalBroadcastManager mLocalBroadcastManager;
    int count = solver.questionlist.size();
    int[] mIds = new int[count];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_result_report);

        initData();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        GridView gv = (GridView) findViewById(R.id.report_gv);
        TextView tv_report_total_question = (TextView) findViewById(R.id.tv_report_total_question);
        //TextView tv_report_exam_type = (TextView) findViewById(R.id.tv_report_exam_type);
        RelativeLayout rl_result_panel = (RelativeLayout) findViewById(R.id.rl_result_panel);
        TextView all_analysis = findViewById(R.id.tv_all_analysis);
        rl_result_panel.setFocusable(true);
        rl_result_panel.setFocusableInTouchMode(true);
        rl_result_panel.requestFocus();
        all_analysis.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Answer.class);
                startActivity(intent);
            }
        });
        tv_report_total_question.setText("you have solved "+solver.total_score+"/"+5+" questions");
        tv_report_total_question.setTextSize(20);
        MyAdapter adapter = new MyAdapter(this);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent("com.leyikao.jumptopage");
                intent.putExtra("index", position);
                mLocalBroadcastManager.sendBroadcast(intent);
            }
        });
    }
    private void initData() {
        for (int i = 0; i < count; i++) {
            mIds[i] = i + 1;
        }
    }

    private class MyAdapter extends BaseAdapter {
        private Context mContext;

        public MyAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mIds.length;
        }

        @Override
        public Object getItem(int position) {
            return mIds[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(mContext);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(new GridView.LayoutParams(70, 70));
            tv.setPadding(8, 8, 8, 8);

            tv.setText(mIds[position] + "");
            tv.setBackgroundResource(R.drawable.ic_launcher_foreground);
            return tv;
        }

    }

}
