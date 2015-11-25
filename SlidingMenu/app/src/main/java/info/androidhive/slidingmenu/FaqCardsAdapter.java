package info.androidhive.slidingmenu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by natraj.gadumala on 7/13/2015.
 */
public class FaqCardsAdapter extends ArrayAdapter<Integer> {

    private final Context mContext;

    ArrayList<Boolean> a=new ArrayList<Boolean>();
    int p;
    String tilte[]={"Dictumst cras sit amet est eget dui viverra scelerisque duis ?",
            "Nam feugiat arcu in turpis ?",
            "Morbi fermentum nisi interdum  ?",
            "John Franklin","John Franklin","John Franklin","John Franklin","John Franklin"};

    String description[]= {"Dictumst cras sit amet est eget dui viverra scelerisque duis Dictumst cras sit amet est eget dui viverra scelerisque duis Dictumst cras sit amet est eget dui viverra scelerisque duis Dictumst cras sit amet est eget dui viverra scelerisque duis",
            "Morbi dignissim tristique turpis sed sodales. In tincidunt dapibus semper. Nullam non orci eu massa tempus aliquam! Quisque placerat metus at neque aliquam sit amet iaculis",
            "Morbi dignissim tristique turpis sed sodales. In tincidunt dapibus semper. Nullam non orci eu massa tempus aliquam! Quisque placerat metus at neque aliquam sit amet iaculis",
            "Morbi dignissim tristique turpis sed sodales. In tincidunt dapibus semper. Nullam non orci eu massa tempus aliquam! Quisque placerat metus at neque aliquam sit amet iaculis",
            "Morbi dignissim tristique turpis sed sodales. In tincidunt dapibus semper. Nullam non orci eu massa tempus aliquam! Quisque placerat metus at neque aliquam sit amet iaculis",
            "Morbi dignissim tristique turpis sed sodales. In tincidunt dapibus semper. Nullam non orci eu massa tempus aliquam! Quisque placerat metus at neque aliquam sit amet iaculis",
            "Morbi dignissim tristique turpis sed sodales. In tincidunt dapibus semper. Nullam non orci eu massa tempus aliquam! Quisque placerat metus at neque aliquam sit amet iaculis",
            "Morbi dignissim tristique turpis sed sodales. In tincidunt dapibus semper. Nullam non orci eu massa tempus aliquam! Quisque placerat metus at neque aliquam sit amet iaculis"
    };

    FaqCardsAdapter(final Context context, int c) {
        mContext = context;
        p=c;
   for(int i=0;i<c;i++){
       a.add(true);
   }
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.faq_items, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView1 = (TextView) view.findViewById(R.id.quetion);

            viewHolder.textView2 = (TextView) view.findViewById(R.id.answer);

            viewHolder.expand = (Button) view.findViewById(R.id.expamd);
            view.setTag(viewHolder);
            //a.add(true);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if(a.get(position)){


            viewHolder.textView2.setVisibility(View.GONE);
            viewHolder.expand.setText("+");
        }else{
            viewHolder.textView2.setVisibility(View.VISIBLE);
            viewHolder.textView2.setText(description[position]);
            viewHolder.expand.setText("-");
        }

        Log.v("-------------------","-------------------------"+a);

          viewHolder.textView1.setText(tilte[position]);
//        viewHolder.textView2.setVisibility(View.GONE);
//        viewHolder.expand.setText("+");
        viewHolder.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(a.get(position)){
                    viewHolder.textView2.setVisibility(View.VISIBLE);
                    viewHolder.textView2.setText(description[position]);
                    viewHolder.expand.setText("-");
                    a.set(position, false);
                }else{
                    a.set(position, true);
                    viewHolder.textView2.setVisibility(View.GONE);
                    viewHolder.expand.setText("+");
                }

          //      Log.v("-------------------","-------------------------"+a);
            }
        });



        return view;
    }

//    private void setImageView(final ViewHolder viewHolder, final int position) {
//        int imageResId;
//        switch (getItem(position) % 5) {
//            case 0:
//                imageResId = images[position];
//                break;
//            case 1:
//                imageResId =images[position];
//                break;
//            case 2:
//                imageResId =images[position];
//                break;
//            case 3:
//                imageResId =images[position];
//                break;
//            default:
//                imageResId = images[position];
//        }
//
//        Bitmap bitmap = getBitmapFromMemCache(imageResId);
//        if (bitmap == null) {
//            bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResId);
//            addBitmapToMemoryCache(imageResId, bitmap);
//        }
//        viewHolder.imageView.setImageBitmap(bitmap);
//    }




    @SuppressWarnings({"PackageVisibleField", "InstanceVariableNamingConvention"})
    private static class ViewHolder {
        TextView textView1;
        TextView textView2;
        Button expand;
    }
}