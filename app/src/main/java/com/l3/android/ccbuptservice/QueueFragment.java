package com.l3.android.ccbuptservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;

/**
 * Created by Ihsan on 15/2/5.
 */
public class QueueFragment extends Fragment {
    private static final int CHOOSE_QUEUE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_queue,container,false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.notice_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QueueListActivity.class);
                startActivityForResult(intent, CHOOSE_QUEUE);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO: Refresh this view
    }
}
