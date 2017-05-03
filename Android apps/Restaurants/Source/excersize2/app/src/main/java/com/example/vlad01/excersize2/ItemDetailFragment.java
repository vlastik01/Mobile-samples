package com.example.vlad01.excersize2;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.vlad01.excersize2.places.PlacesContent;

import java.util.Arrays;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private PlacesContent.PlaceItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = PlacesContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
//            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
//            if (appBarLayout != null) {
//                appBarLayout.setTitle(mItem.name);
//            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.name)).setText(mItem.name);
            ((TextView) rootView.findViewById(R.id.street_address)).setText(mItem.streetAddress);
            float rating = mItem.rating == "" ? 0 : Float.parseFloat(mItem.rating);
            ((RatingBar) rootView.findViewById(R.id.ratingBar)).setRating(rating);
            Integer priceLevel = mItem.priceLevel == "" ? 0 : Integer.parseInt(mItem.priceLevel);
            char[] chars = new char[priceLevel];
            Arrays.fill(chars, '$');
            String bucks = new String(chars);
            ((TextView) rootView.findViewById(R.id.price_level)).setText(bucks);
            ((TextView) rootView.findViewById(R.id.opening_hours)).setText(mItem.openingHours == "" ? "" : (mItem.openingHours == "false" ? "Now Closed" : "Now Open"));
            if (mItem.photo != "")
            {
                StringBuilder strb= new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?");
                strb.append("maxwidth=600").append("&photoreference=").append(mItem.photo).append("&key=").append(this.getString(R.string.google_photo_key));

                new GetIcon((ImageView) rootView.findViewById(R.id.imageDetail), this.getActivity()).execute(strb.toString());
            }
        }

        return rootView;
    }


}
