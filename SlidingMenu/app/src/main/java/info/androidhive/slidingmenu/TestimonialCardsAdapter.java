/*
 * Copyright 2014 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.androidhive.slidingmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;

public class TestimonialCardsAdapter extends ArrayAdapter<Integer> {

    private final Context mContext;

    String tilte[]={"John Franklin","John Franklin","John Franklin","John Franklin","John Franklin","John Franklin","John Franklin","John Franklin"};

    String description[]= {"Morbi dignissim tristique turpis sed sodales. In tincidunt dapibus semper. Nullam non orci eu massa tempus aliquam! Quisque placerat metus at neque aliquam sit amet iaculis",
            "Morbi dignissim tristique turpis sed sodales. In tincidunt dapibus semper. Nullam non orci eu massa tempus aliquam! Quisque placerat metus at neque aliquam sit amet iaculis",
            "Morbi dignissim tristique turpis sed sodales. In tincidunt dapibus semper. Nullam non orci eu massa tempus aliquam! Quisque placerat metus at neque aliquam sit amet iaculis",
            "Morbi dignissim tristique turpis sed sodales. In tincidunt dapibus semper. Nullam non orci eu massa tempus aliquam! Quisque placerat metus at neque aliquam sit amet iaculis",
            "Morbi dignissim tristique turpis sed sodales. In tincidunt dapibus semper. Nullam non orci eu massa tempus aliquam! Quisque placerat metus at neque aliquam sit amet iaculis",
            "Morbi dignissim tristique turpis sed sodales. In tincidunt dapibus semper. Nullam non orci eu massa tempus aliquam! Quisque placerat metus at neque aliquam sit amet iaculis",
            "Morbi dignissim tristique turpis sed sodales. In tincidunt dapibus semper. Nullam non orci eu massa tempus aliquam! Quisque placerat metus at neque aliquam sit amet iaculis",
            "Morbi dignissim tristique turpis sed sodales. In tincidunt dapibus semper. Nullam non orci eu massa tempus aliquam! Quisque placerat metus at neque aliquam sit amet iaculis"
    };

            TestimonialCardsAdapter(final Context context) {
        mContext = context;
     }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.testimonial_items, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView1 = (TextView) view.findViewById(R.id.activity_googlecards_card_textview1);

            viewHolder.textView2 = (TextView) view.findViewById(R.id.activity_googlecards_card_textview2);

            viewHolder.textView3 = (TextView) view.findViewById(R.id.activity_googlecards_card_textview3);
            view.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) view.getTag();
        }



         viewHolder.textView1.setText(tilte[position]);
        viewHolder.textView2.setText(description[position]);

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
        TextView textView3;
    }
}