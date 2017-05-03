package com.example.vlad01.excersize2.places;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PlacesContent {

    /**
     * An array of place  items.
     */
    public static final List<PlaceItem> ITEMS = new ArrayList<PlaceItem>();

    /**
     * A map of place  items items, by ID.
     */
    public static final Map<String, PlaceItem> ITEM_MAP = new HashMap<String, PlaceItem>();

    //private static final int COUNT = 25;

//    static {
//        // Add some place  items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem( createPlaceItem(i));
//        }
//    }

    public static void addItem(PlaceItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void clearItems()
    {
        ITEMS.clear();
        ITEM_MAP.clear();
    }

//    public static PlaceItem createPlaceItem(int position) {
//        return new PlaceItem(String.valueOf(position), "Item " + position, makeDetails(position));
//    }

//    private static String makeDetails(int position) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("Details about Item: ").append(position);
//        for (int i = 0; i < position; i++) {
//            builder.append("\nMore details information here.");
//        }
//        return builder.toString();
//    }

    /**
     * place  item representing a piece of content.
     */
    public static class PlaceItem {
        public final String id;
        public final String icon;
        public final String name;
        public final String streetAddress;
        public final String priceLevel;
        public final String rating;
        public final String openingHours;
        public final String photo;
        //public final String detail;

        public PlaceItem(String id, String name, String streetAddress, String icon,String priceLevel, String rating, String openingHours, String photo) {
            this.id = id;
            this.name = name;
            this.streetAddress = streetAddress;
            this.icon = icon;
            this.priceLevel = priceLevel;
            this.rating = rating;
            this.openingHours = openingHours;
            this.photo = photo;
        }

//        @Override
//        public String toString() {
//            return "TODO";
//        }
    }
}
