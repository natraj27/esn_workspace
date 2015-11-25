package com.rochdev.example.countdownlistview;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CountDownList extends ListActivity {

	private Button mButton;
    boolean flip =true;
	private ArrayList<MyData> mDataList = new ArrayList<MyData>();

	private Handler mHandler;
	private ArrayAdapter<MyData> mListAdapter;
	private boolean mCountersActive;

	public CountDownList() {
		mHandler = new Handler();
	}

	private final Runnable mRunnable = new Runnable() {
		public void run() {
			MyData myData;
			// if counters are active
			if (mCountersActive) {				
				if (mDataList != null) {
					for (int i=0; i < mDataList.size(); i++) {
						myData = mDataList.get(i);
						if (myData.getCount() >= 0) {
							if(myData.isB()){

							}else{
								myData.reduceCount();

							}
						}
					}
					// notify that data has been changed
					mListAdapter.notifyDataSetChanged();
				}
				// update every second
				mHandler.postDelayed(this, 1000);
			}
		}
	};

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        MyData myData1;
        myData1 =mDataList.get(position);
        if(myData1.isB()){
            myData1.setB(false);
         }else {
            myData1.setB(true);
         }
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// start and stop button
		mButton = (Button) findViewById(R.id.myButton);
		mButton.setText("Stop");
		mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopStart();
            }
        });

		// add some test data
		mDataList = new ArrayList<MyData>();
		MyData data;
		int j = 60;
		for (int i=0; i < 10; i++) {
			data = new MyData(Integer.toString(j), j,false);
			mDataList.add(data);

		}

		initData();
	}

	private void initData() {
		// set the list adapter
		mListAdapter = new MyListAdapter(this, R.layout.row, mDataList);
		setListAdapter(mListAdapter);
		// start counters
		stopStart();
	}	

	private void stopStart() {
		if (mCountersActive) {
			mCountersActive = false;
			mButton.setText("Start");
		} else {
			mCountersActive = true;
			mHandler.post(mRunnable);
			mButton.setText("Stop");
		}
	}

	private class MyListAdapter extends ArrayAdapter<MyData> {

		private ArrayList<MyData> items;
		private LayoutInflater vi = (LayoutInflater)
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		public MyListAdapter(Context context, int textViewResourceId,
				ArrayList<MyData> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = vi.inflate(R.layout.row, null);
			}

			MyData myData = items.get(position);

			if (myData != null) {
				TextView text = (TextView) convertView.findViewById(R.id.myTextView);
				if (text != null) {
					text.setText(myData.getText());
				}

				TextView counter = (TextView) convertView.findViewById(R.id.myTextViewTwo);
				if (counter != null) {
					counter.setText(myData.getCountAsString());
				}
			}

			return convertView;
		}
	}

	private class MyData {
		private String text;
		private int count;
		private boolean b;

		public MyData(String text, int count,boolean b) {
			this.text = text;
			this.count = count;
			this.b=b;
		}

		public boolean isB() {
			return b;
		}

        public void setB(boolean b) {
            this.b = b;
        }

        public String getText() {
			return text;
		}

		public int getCount() {
			return count;
		}

		public String getCountAsString() {
			return Integer.toString(count);
		}

		public void reduceCount() {
			if (count > 0) {
				count--;
			}
		}
	}
}