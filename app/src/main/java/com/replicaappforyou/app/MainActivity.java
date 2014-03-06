/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.replicaappforyou.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to display a grid of coupons. The number of columns varies based on
 * screen width and goes down to a one-column grid on a small devicde such as a phone.
 *
 * <p>A coupon consists of a photo, title, and subtitle</p>
 *
 * <p>Tapping on a coupon to redeem it brings up the Android "share"
 * dialog with a pre-populated message based on the coupon text</p>
 */

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    //Name of person giving out these coupons. When the user clicks on a coupon and
    //shares to another app, this name will be part of the pre-populated text
    //TODO: Fill in your name here
    private static final String SENDER_NAME = "John";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Fetch the {@link LayoutInflater} service so that new views can be created
        LayoutInflater inflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        //TODO: create grid layout
        //Find the {@link GridView} that was already defined in the XML layout
        GridView gridView = (GridView) findViewById(R.id.grid);

        //Initialize the adapter with all the coupons setting the adapter on the {@link GridView}
        gridView.setAdapter(new CouponAdapter(inflater, createAllCoupons()));

        //Set a click listener for each coupon in the grid
        gridView.setOnItemClickListener(this);
    }



    /**
     * Generate the list of all coupons
     * @return The list of coupons
     */
    private List<Coupon> createAllCoupons(){
        //TODO: Customize this list of coupons for your personal use
        List<Coupon> coupons = new ArrayList<Coupon>();
        coupons.add(new Coupon("Walk in the park",
                "Take a stroll in the flower garden", "park.jpg"));
        coupons.add(new Coupon("Trip to the zoo",
                "See the cute zoo animals", "zoo.jpg"));
        coupons.add(new Coupon("Watch sunrise",
                "Drive out to the vista point and watch the sunrise at 6am", "sunrise.jpg"));
        coupons.add(new Coupon("Hawaii getaway",
                "Relax in Hawaii by going to the beach and attending luaus", "hawaii.jpg"));
        coupons.add(new Coupon("Spa day",
                "Receive a massage and enjoy the peace and quiet", "spa.jpg"));
        coupons.add(new Coupon("Homemade dinner",
                "Your favorite meal cooked by yours truly", "dinner.jpg"));
        coupons.add(new Coupon("Day on the water",
                "Boat ride down the river on a breezy day", "boat.jpg"));
        coupons.add(new Coupon("Flowers",
                "Delivered to your front door", "rose.jpg"));
        coupons.add(new Coupon("Picnic",
                "Wine, bread, and cheese at the vineyard", "picnic.jpg"));
        coupons.add(new Coupon("Surprise gift",
                "Won't be revealed until redeemed", "present.jpg"));
        return coupons;

    }

    /**
     * Callback method for when a coupon is clicked. A new share Intent is created with the
     * coupon title. The the user can select which app to share the content of the coupon with
     * @param parent The AdapterView where the click happened
     * @param view The view within the AdapterView that was clicked (this will be a mvi provided
     *             by the adapter)
     * @param position The position of the view in the adapter that was clicked
     * @param id The row ID of the item that was clicked
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Find coupon that was clicked based off of position in adapter
        Coupon coupon = (Coupon) parent.getItemAtPosition(position);
        //Create the share Intent
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setText(getShareText(coupon))
                .setType("image/jpeg")
                .setStream(coupon.mImageUri)
                .setChooserTitle("Redeem using")
                .createChooserIntent();
        startActivity(shareIntent);
    }

    /**
     * Create the share Intent text based on the coupon title, subtitle and whether or not
     * there is a SENDER_NAME
     * @param coupon to create the intent text for
     * @return string to be used in the share intent
     */
    private String getShareText(Coupon coupon){
        //If there is no sender name, just use the coupon title and subtitl
        //TODO: Create the strings in string.xml
        if(TextUtils.isEmpty(SENDER_NAME)){
            return getString(R.string.message_format_without_sender, coupon.mTitle, coupon.mSubtitle);
        }else{
            return getString(R.string.message_format_with_sender, SENDER_NAME, coupon.mTitle, coupon.mSubtitle);
        }
    }

    /**
     * Adapter for grid of coupons
     */
    private static class CouponAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<Coupon> mAllCoupons;

        /**
         * Constructs a new {@link CouponAdapter}
         *
         * @param mInflater to create new views
         * @param mAllCoupons for the list of all coupons to be displayed
         */
        private CouponAdapter(LayoutInflater mInflater, List<Coupon> mAllCoupons) {
            if(mAllCoupons == null){
                throw new IllegalStateException("Can't have null list of coupons");
            }
            this.mInflater = mInflater;
            this.mAllCoupons = mAllCoupons;
        }

        @Override
        public int getCount() {
            return mAllCoupons.size();
        }

        @Override
        public Coupon getItem(int position) {
            return mAllCoupons.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * Get a View that displays the data at the specified position in the data set.
         * You can either create a View manually or inflate it from an XML layout file.
         * When the View is inflated, the parent View (GridView, ListView...) will
         * apply default layout parameters unless you use
         * inflate(int, android.view.ViewGroup, boolean) to specify a root view
         * and to prevent attachment to the root.
         * @param position of the element we are parsing
         * @param convertView the view we are going to customize
         * @param parent the parent in where our list is stored
         * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View result = convertView;
            if(result == null){
                //TODO: Create grid_item layout
                result = mInflater.inflate(R.layout.grid_item, parent, false);
            }
            //Try to get view cache or create a new one if needed
            ViewCache viewCache = (ViewCache) result.getTag();
            if(viewCache == null){
                viewCache = new ViewCache(result);
                result.setTag(viewCache);
            }

            //Fetch item
            Coupon coupon = getItem(position);

            //Bind the data
            viewCache.mTitleView.setText(coupon.mTitle);
            viewCache.mSubtitleView.setText(coupon.mSubtitle);
            viewCache.mImageView.setImageURI(coupon.mImageUri);

            return result;
        }
    }


    /**
     * Cache of views in the grid item view to make recycling of views quicker. This avoids
     * additional {@link View#findViewById(int)}} calls after the {@link com.replicaappforyou.app.MainActivity.ViewCache}
     * is first created for a view. See:
     * {@link CouponAdapter#getView(int position, View convertView, ViewGroup parent)}
     */
    private static class ViewCache {
        //View that displays the title of the coupon
        private final TextView mTitleView;
        //View that displays the description of the coupon
        private final TextView mSubtitleView;
        //View that displays tha image associated with the coupon
        private final ImageView mImageView;

        //TODO: Modify your layout in order to have these fields
        private ViewCache(View view) {
            this.mTitleView = (TextView) view.findViewById(R.id.title);
            this.mSubtitleView = (TextView) view.findViewById(R.id.subtitle);
            this.mImageView = (ImageView) view.findViewById(R.id.image);
        }
    }

    /**
     * Model object for coupons
     */
    private static class Coupon {
        //Title of the coupon
        private final String mTitle;
        //Description del coupon
        private final String mSubtitle;
        //Content URI of the image for the coupon
        private final Uri mImageUri;

        /**
         * Constructs a new {@link Coupon}
         * @param mTitle is the title
         * @param mSubtitle is the description
         * @param mImageUri is the file path from the application's assets folder for
         *                  the image associated with this coupon
         */
        private Coupon(String mTitle, String mSubtitle, String mImageUri) {
            this.mTitle = mTitle;
            this.mSubtitle = mSubtitle;
            this.mImageUri = Uri.parse("content://" + AssetProvider.CONTENT_URI + "/" + mImageUri);
        }
    }

}
