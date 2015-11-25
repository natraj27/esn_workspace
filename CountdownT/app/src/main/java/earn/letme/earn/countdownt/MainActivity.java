package earn.letme.earn.countdownt;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private ArrayList<MyData> mDataList = new ArrayList<MyData>();

    private Handler mHandler;
  //  private ArrayAdapter<MyData> mListAdapter;
    private boolean mCountersActive;
    private boolean mCountersActive2;

    public MainActivity() {
        mHandler = new Handler();
    }
    ImageAdapter ai;
    GridView gridview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

          gridview = (GridView) findViewById(R.id.lvItems);



        mDataList = new ArrayList<MyData>();
        MyData data;
        int j = 60;
        for (int i=0; i < 9; i++) {
            data = new MyData(Integer.toString(j), j,false);
            mDataList.add(data);

        }

        initData();


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
//                Toast.makeText(MainActivity.this, "" + position,
//                        Toast.LENGTH_SHORT).show();


                     MyData data3;
                    data3 = mDataList.get(position);
                   boolean s=data3.isB();
                if (s) {

                    data3.setB(false);
                }else{
                    data3.setB(true);
                }


            }
        });
    }
    private void initData() {
        // set the list adapter
        ai=new ImageAdapter(this,mDataList);
        gridview.setAdapter(ai);
        // start counters
        stopStart();
    }

    private void stopStart() {
        if (mCountersActive) {
            mCountersActive = false;
         } else {
            mCountersActive = true;
            mHandler.post(mRunnable);
         }
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
                  ai.notifyDataSetChanged();
                  ai.updateContent(mDataList);
                }
                // update every second
                mHandler.postDelayed(this, 1000);
            }
        }
    };
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        ArrayList<MyData> arry;
        public ImageAdapter(Context c ,ArrayList<MyData>  aa) {
            mContext = c;
            arry=aa;
        }
        public void updateContent (ArrayList<MyData> updates) {
             this.notifyDataSetChanged();                  //--- Screen still shows original 16 images--
        }

        public int getCount() {
            return arry.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new TextView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                 imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (TextView) convertView;
            }
             MyData m2=arry.get(position);
            imageView.setText(" "+m2.getCount());
          //  imageView.setBackground(Color.red());
            return imageView;
        }

        // references to our images

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