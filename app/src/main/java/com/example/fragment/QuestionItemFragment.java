package com.example.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.adapter.OptionsListAdapter;
import com.example.afinal.R;
import com.example.afinal.solver;
import com.example.bean.QuestionBean;
import com.example.view.NoScrollListview;


//from https://github.com/wildcreek/MultiChoice
public class QuestionItemFragment extends Fragment {
    QuestionBean questionBean;
    int index ;
    private OptionsListAdapter adapter;
    private StringBuffer sb;
    private NoScrollListview lv;
    private String correct;
    LocalBroadcastManager mLocalBroadcastManager;

    public QuestionItemFragment(int index){
        this.index = index;
        questionBean = solver.questionlist.get(index);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        View rootView = inflater.inflate(R.layout.pager_item,
                container, false);
        lv = (NoScrollListview) rootView.findViewById(R.id.lv_options);
        TextView tv_paper_name = (TextView) rootView.findViewById(R.id.tv_paper_name);
        TextView tv_sequence = (TextView) rootView.findViewById(R.id.tv_sequence);
        TextView tv_total_count = (TextView) rootView.findViewById(R.id.tv_total_count);
        TextView tv_description = (TextView) rootView.findViewById(R.id.tv_description);
        Button btn_submit = (Button) rootView.findViewById(R.id.btn_submit);
        adapter = new OptionsListAdapter(getActivity(), questionBean.getQuestionOptions(),lv,index);

        lv.setAdapter(adapter);

        tv_sequence.setText((index+1)+"");
        tv_total_count.setText("/"+solver.questionlist.size());

        tv_description.setText(questionBean.getDescription());

        int questionType = questionBean.getQuestionType();
        sb = new StringBuffer();
        if(questionType == 1){
            SpannableStringBuilder ssb = new SpannableStringBuilder("(single choice)");
            ssb.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(questionBean.getDescription());
            tv_description.setText(ssb);
            lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            lv.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    adapter.notifyDataSetChanged();

                }
            });
            btn_submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    long[] ids = lv.getCheckedItemIds();
                    String answer = "";
                    for (int i = 0; i < ids.length; i++) {
                        long id = ids[i];
                        answer+=String.valueOf(id);
                        sb.append(questionBean.getQuestionOptions().get((int)id).getName()).append(" ");
                    }
                    if(answer.equals(solver.questionlist.get(index).getCorrect())){
                        Intent intent = new Intent("question corrected");
                        mLocalBroadcastManager.sendBroadcast(intent);
                    }
                    Intent intent = new Intent("com.leyikao.jumptonext");
                    mLocalBroadcastManager.sendBroadcast(intent);
                    Toast.makeText(getActivity(), "your choice is "+sb.toString(), Toast.LENGTH_SHORT).show();
                    sb.setLength(0);
                }
            });
        }else if(questionType == 2){//多选
            SpannableStringBuilder ssb = new SpannableStringBuilder("(multi choices)");
            ssb.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(questionBean.getDescription());
            tv_description.setText(ssb);
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            lv.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    adapter.notifyDataSetChanged();
                }
            });
            btn_submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    long[] ids = lv.getCheckedItemIds();
                    String answer = "";
                    for (int i = 0; i < ids.length; i++) {
                        long id = ids[i];
                        answer+=String.valueOf(id);
                        sb.append(questionBean.getQuestionOptions().get((int)id).getName()).append(" ");
                    }
                    Log.e("TAG",answer);
                    for(int i=0;i<ids.length;i++){
                        Log.e("TAG",String.valueOf(ids[i]));
                    }
                    if(answer.equals(solver.questionlist.get(index).getCorrect())){
                        Intent intent = new Intent("question corrected");
                        mLocalBroadcastManager.sendBroadcast(intent);
                    }
                    Intent intent = new Intent("com.leyikao.jumptonext");
                    mLocalBroadcastManager.sendBroadcast(intent);
                    Toast.makeText(getActivity(), "your choices are "+sb.toString(), Toast.LENGTH_SHORT).show();
                    sb.setLength(0);
                }
            });
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

}