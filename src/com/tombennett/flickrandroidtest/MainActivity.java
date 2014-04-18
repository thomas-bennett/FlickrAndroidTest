package com.tombennett.flickrandroidtest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.GridView;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.interestingness.InterestingnessInterface;
import com.gmail.yuyang226.flickr.photos.PhotoList;

public class MainActivity extends Activity {

    private Flickr mFlickr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlickr = new Flickr(Constants.FLICKR_API_KEY);
        new GetInterestingPhotosClass().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class GetInterestingPhotosClass extends AsyncTask<Void, Void, PhotoList> {
        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Loading");
            mProgressDialog.setMessage("Loading interesting images");
            mProgressDialog.show();
        }

        @Override
        protected PhotoList doInBackground(Void... params) {
            InterestingnessInterface interestingInterface = mFlickr.getInterestingnessInterface();

            try {
                return interestingInterface.getList();
            } catch (Exception exception) {
                Log.e(Constants.TAG, exception.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(PhotoList photoList) {
            mProgressDialog.dismiss();

            if (photoList != null) {
                Activity activity = MainActivity.this;
                FlickrImageAdapter adapter = new FlickrImageAdapter(activity, photoList);
                GridView gridView = (GridView) activity.findViewById(R.id.gridview);
                gridView.setAdapter(adapter);
            }
        }

    }
}