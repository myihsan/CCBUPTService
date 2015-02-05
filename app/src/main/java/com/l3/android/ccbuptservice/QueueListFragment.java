package com.l3.android.ccbuptservice;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ihsan on 15/2/5.
 */
public class QueueListFragment extends ListFragment {
    private static final String TAG = "QueueListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QueueAdapter adapter = new QueueAdapter(QueueArray.get(getActivity()).getQueues());
        setListAdapter(adapter);
        new FetchQueueTask().execute();
    }

    public void updateAdapter() {
        ((QueueAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class FetchQueueTask extends AsyncTask<Void,Void,ArrayList<Queue>>{
        @Override
        protected ArrayList<Queue> doInBackground(Void... params) {
            return new DataFetcher().fetchQueue();
        }

        @Override
        protected void onPostExecute(ArrayList<Queue> queues) {
            QueueArray.get(getActivity()).refreshQueues(queues);
            updateAdapter();
        }
    }

    private class QueueAdapter extends ArrayAdapter<Queue> {
        public QueueAdapter(ArrayList<Queue> queues){
            super(getActivity(),0,queues);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView= getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_queue, null);
            }

            Queue queue=getItem(position);

            TextView titleTextView=
                    (TextView)convertView.findViewById(R.id.queue_list_item_titleTextView);
            titleTextView.setText(queue.getTitle());

            return convertView;
        }
    }
}
