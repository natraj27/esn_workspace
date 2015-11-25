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
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;

public class MyExpandableListItemAdapter extends ExpandableListItemAdapter<Integer> {

    private final Context mContext;

     String que[]={"Dictumst cras sit amet est eget dui viverra scelerisque duis"};

    public MyExpandableListItemAdapter(final Context context) {
        super(context, R.layout.activity_expandablelistitem_card, R.id.activity_expandablelistitem_card_title, R.id.activity_expandablelistitem_card_content);
        mContext = context;

        for (int i = 0; i < 5; i++) {
            add(i);
        }
    }

    @NonNull
    @Override
    public View getTitleView(final int position, final View convertView, @NonNull final ViewGroup parent) {
        TextView tv = (TextView) convertView;
        if (tv == null) {
            tv = new TextView(mContext);
        }
        tv.setText("count");
        return tv;
    }

    @NonNull
    @Override
    public View getContentView(final int position, final View convertView, @NonNull final ViewGroup parent) {
        TextView quetionstext = (TextView) convertView;
        if (quetionstext == null) {
            quetionstext = new TextView(mContext);
         }


        quetionstext.setText("Dictumst cras sit amet est eget dui viverra scelerisque duis");

        return quetionstext;
    }


}