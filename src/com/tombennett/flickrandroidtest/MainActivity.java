package com.tombennett.flickrandroidtest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.interestingness.InterestingnessInterface;
import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.PhotoList;

public class MainActivity extends Activity {
    // TODO: Move to resources
    final private static String flickrApiKey = "86bad3670fcd1c5966036d3159252889";
    private static final String TAG = "FlickrAndroidTest";

    private Flickr mFlickr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFlickr = new Flickr(flickrApiKey);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new GetInterestingPhotosClass().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class GetInterestingPhotosClass extends AsyncTask<Void, Void, PhotoList> {

        @Override
        protected PhotoList doInBackground(Void... params) {
            InterestingnessInterface interestingInterface = mFlickr.getInterestingnessInterface();

            try {
                return interestingInterface.getList();
            } catch (Exception exception) {
                Log.e(TAG, exception.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(PhotoList photoList) {
            if (photoList != null) {
                for (Photo photo : photoList) {
                    final String title = (photo.getTitle() == null) ? "No Title" : photo.getTitle();
                    Log.d(TAG, String.format("Title: %s, Url: %s", title, photo.getUrl()));
                }
            }
        }

    }
}