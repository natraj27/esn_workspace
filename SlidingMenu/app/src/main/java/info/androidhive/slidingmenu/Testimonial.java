package info.androidhive.slidingmenu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;

public class Testimonial extends Fragment {



    private static final int INITIAL_DELAY_MILLIS = 200;

    private TestimonialCardsAdapter mTestimonialCardsAdapter;
    public Testimonial(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_testmonial, container, false);



        ListView listView = (ListView)rootView.findViewById(R.id.activity_googlecards_listview);

        mTestimonialCardsAdapter = new TestimonialCardsAdapter(getActivity());
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter=new SwingBottomInAnimationAdapter(mTestimonialCardsAdapter) ;
        swingBottomInAnimationAdapter.setAbsListView(listView);

        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);

        listView.setAdapter(swingBottomInAnimationAdapter);

        for (int i = 0; i < 8; i++) {
            mTestimonialCardsAdapter.add(i);
        }
        return rootView;
    }

//    @Override
//    public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
//        for (int position : reverseSortedPositions) {
//            mGoogleCardsAdapter.remove(position);
//        }
//    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu items for use in the action bar
//        MenuInflater inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.menu_home, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

}
