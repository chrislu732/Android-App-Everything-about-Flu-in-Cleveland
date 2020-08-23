package com.example.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.afinal.solver;
import com.example.afinal.ResultReportActivity;
import com.example.afinal.R;
import com.example.bean.QuestionBean;
import com.example.view.NoScrollGridView;

//from https://github.com/wildcreek/MultiChoice
public class ScantronItemFragment extends Fragment {
    LocalBroadcastManager mLocalBroadcastManager;
    public ScantronItemFragment() {

    }

    int count = solver.questionlist.size();
    int[] mIds = new int[count];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initData();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        View rootView = inflater.inflate(R.layout.pager_item_scantron,
                container, false);
        NoScrollGridView gv = (NoScrollGridView) rootView.findViewById(R.id.gridview);
        TextView tv_submit_result = (TextView) rootView.findViewById(R.id.tv_submit_result);
        tv_submit_result.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResultReportActivity.class);
                startActivity(intent);

            }
        });

        return rootView;

    }

    private void initData() {
        for (int i = 0; i < count; i++) {
            mIds[i] = i + 1;
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

}