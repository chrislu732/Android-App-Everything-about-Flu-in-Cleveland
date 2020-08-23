package com.example.fragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;

import com.example.Constant;
import com.example.adapter.NormalExpandableListAdapter;
import com.example.adapter.OnGroupExpandedListener;
import com.example.afinal.R;
import com.google.android.gms.maps.SupportMapFragment;
//https://blog.csdn.net/Afanbaby/article/details/79240620
public class Tab3Fragment extends Fragment {
    WebView webView;

    WebViewClient webViewClient;
    private static final String TAG = "??";
    private ExpandableListView mExpandableListView;
    private NormalExpandableListAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_tab3_fragment,null);
        mExpandableListView = view.findViewById(R.id.expand_list);
        adapter = new NormalExpandableListAdapter(Constant.BOOKS,Constant.FIGURES);
        webView = (WebView) view.findViewById(R.id.webview);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl("file:///android_asset/flu_tip-1.html");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mExpandableListView.setAdapter(adapter);
        adapter.setOnGroupExpandedListener(new OnGroupExpandedListener() {
            @Override
            public void onGroupExpanded(int groupPosition) {
                expandOnlyOne(groupPosition);
            }
        });
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d(TAG, "onGroupClick: groupPosition:" + groupPosition + ", id:" + id);

                return false;
            }
        });

        //
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return true;
            }
        });
    }

    private boolean expandOnlyOne(int expandedPosition) {
        boolean result = true;
        int groupLength = mExpandableListView.getExpandableListAdapter().getGroupCount();
        for (int i = 0; i < groupLength; i++) {
            if (i != expandedPosition && mExpandableListView.isGroupExpanded(i)) {
                result &= mExpandableListView.collapseGroup(i);
            }
        }
        return result;
    }
}

